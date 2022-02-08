package com.cos.insta.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.cos.insta.config.auth.PrincipalDetails;
import com.cos.insta.domain.image.Image;
import com.cos.insta.domain.tag.Hashtag;
import com.cos.insta.handler.ex.CustomApiException;
import com.cos.insta.repository.CommentRepository;
import com.cos.insta.repository.HashtagRepository;
import com.cos.insta.repository.ImageRepository;
import com.cos.insta.repository.LikesRepository;
import com.cos.insta.util.Utils;
import com.cos.insta.web.dto.comment.CommentOutputDto;
import com.cos.insta.web.dto.hashtag.HashtagDto;
import com.cos.insta.web.dto.image.ImagePagingDto;
import com.cos.insta.web.dto.image.ImagePopularDto;
import com.cos.insta.web.dto.image.ImageUploadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ImageService {
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    LikesRepository likesRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    HashtagRepository hashtagRepository;

    @Autowired
    AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}") // application.yml 안의 경로 가리킴
    private String bucket;

    @Value("${file.path}") // application.yml 안의 경로 가리킴
    private String uploadFolder;

    @Transactional(readOnly = true)
    public List<ImagePopularDto> popularFeed() {
        return likesRepository.mPopular();
    }

    @Transactional(readOnly = true)
    public Page<ImagePagingDto> imageStory(int principalId, Pageable pageable, String search, int imageId) {
        Page<ImagePagingDto> images;

        if (imageId != -1) { // 개인 이미지 호출
            images = imageRepository.mMyStory(imageId, pageable);
        } else if (StringUtils.hasText(search)) { // 검색시 호출
            List<Integer> imageIdsBySearch = hashtagRepository.hashtagsBySearch(search);
            images = imageRepository.mStory(imageIdsBySearch, pageable);
        } else { // 팔로윙한 이미지 호출
            images = imageRepository.mStory(principalId, pageable);
        }

        Map<Integer, List<CommentOutputDto>> comments = commentRepository.commentsByImageIds(toListIds(images));
        Map<Integer, List<HashtagDto>> hashtags = hashtagRepository.hashtagsByImageIds(toListIds(images));

        images.forEach(image -> {
            Map<Integer, Boolean> likesStateMap = likesRepository.findLikesMap(image.getId(), principalId);
            Integer likesCountMap = likesRepository.findLikesCountMap(image.getId(), principalId);
            image.setComments(comments.get(image.getId()));
            image.setHashtags(hashtags.get(image.getId()));
            image.setLikeState(likesStateMap.get(image.getId()) != null);
            image.setLikesCount(likesCountMap);
        });

        return images;
    }

    private List<Integer> toListIds(Page<ImagePagingDto> images) {
        return images.stream()
                .map(ImagePagingDto::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ImageUpload(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) throws IOException {
        UUID uuid = UUID.randomUUID();
        String encFileName = URLEncoder.encode(imageUploadDto.getFile().getOriginalFilename(), StandardCharsets.UTF_8); // 파일명이 한글일 경우 인코딩
        String imageFileName = uuid + "_" + encFileName; // 1.jpg

        Path imageFilePath = Paths.get(uploadFolder + imageFileName);

        try {
            Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Image 저장
        Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser());
        Image imageEntity = imageRepository.save(image);

        // HashTag 저장
        List<String> hashtags = Utils.tagParse(imageUploadDto.getHashtags());
        for (String hashtag : hashtags) {
            Hashtag tag = Hashtag.builder()
                    .image(imageEntity)
                    .name(hashtag)
                    .build();
            hashtagRepository.save(tag);
        }
    }

    @Transactional
    public void ImageUploadS3(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) throws IOException {
        UUID uuid = UUID.randomUUID();
        String encFileName = URLEncoder.encode(imageUploadDto.getFile().getOriginalFilename(), StandardCharsets.UTF_8); // 파일명이 한글일 경우 인코딩
        String imageFileName = uuid + "_" + encFileName; // 1.jpg

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(imageUploadDto.getFile().getSize());
        objectMetadata.setContentType(imageUploadDto.getFile().getContentType());

        try (InputStream inputStream = imageUploadDto.getFile().getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, imageFileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Image 저장
        Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser());
        Image imageEntity = imageRepository.save(image);

        // HashTag 저장
        List<String> hashtags = Utils.tagParse(imageUploadDto.getHashtags());
        for (String hashtag : hashtags) {
            Hashtag tag = Hashtag.builder()
                    .image(imageEntity)
                    .name(hashtag)
                    .build();
            hashtagRepository.save(tag);
        }
    }

    @Transactional
    public void myStoryImageUpdate(int imageId, MultipartFile myStoryImageFile) {
        UUID uuid = UUID.randomUUID();
        String encFileName = URLEncoder.encode(Objects.requireNonNull(myStoryImageFile.getOriginalFilename()), StandardCharsets.UTF_8); // 파일명이 한글일 경우 인코딩
        String imageFileName = uuid + "_" + encFileName; // 1.jpg

        Path imageFilePath = Paths.get(uploadFolder + imageFileName);

        try {
            Files.write(imageFilePath, myStoryImageFile.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image image = imageRepository.findById(imageId).orElseThrow(() -> {
            throw new CustomApiException("이미지를 찾을 수 없습니다.");
        });
        image.setPostImageUrl(imageFileName);
    }

    @Transactional
    public void myStoryImageUpdateS3(int imageId, MultipartFile myStoryImageFile) {
        UUID uuid = UUID.randomUUID();
        String encFileName = URLEncoder.encode(Objects.requireNonNull(myStoryImageFile.getOriginalFilename()), StandardCharsets.UTF_8); // 파일명이 한글일 경우 인코딩
        String imageFileName = uuid + "_" + encFileName; // 1.jpg

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(myStoryImageFile.getSize());
        objectMetadata.setContentType(myStoryImageFile.getContentType());

        try (InputStream inputStream = myStoryImageFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, imageFileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image image = imageRepository.findById(imageId).orElseThrow(() -> {
            throw new CustomApiException("이미지를 찾을 수 없습니다.");
        });
        image.setPostImageUrl(imageFileName);
    }

    @Transactional
    public void myStoryImageDelete(int imageId) {
        imageRepository.deleteById(imageId);
    }
}
