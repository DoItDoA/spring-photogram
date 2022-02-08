package com.cos.insta.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cos.insta.domain.user.User;
import com.cos.insta.handler.ex.CustomApiException;
import com.cos.insta.handler.ex.CustomException;
import com.cos.insta.handler.ex.CustomValidationApiException;
import com.cos.insta.repository.SubscribeRepository;
import com.cos.insta.repository.UserRepository;
import com.cos.insta.web.dto.search.SearchNameDto;
import com.cos.insta.web.dto.user.UserProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SubscribeRepository subscribeRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}") // application.yml 안의 경로 가리킴
    private String bucket;

    @Value("${file.path}") // application.yml 안의 경로 가리킴
    private String uploadFolder;

    @Transactional
    public User userProfileImageUpdate(int principalId, MultipartFile profileImageFile) {
        UUID uuid = UUID.randomUUID();
        String encFileName = URLEncoder.encode(Objects.requireNonNull(profileImageFile.getOriginalFilename()), StandardCharsets.UTF_8); // 파일명이 한글일 경우 인코딩
        String imageFileName = uuid + "_" + encFileName; // 1.jpg


        Path imageFilePath = Paths.get(uploadFolder + imageFileName);

        try {
            Files.write(imageFilePath, profileImageFile.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        User userEntity = userRepository.findById(principalId).orElseThrow(() -> {
            throw new CustomApiException("유저를 찾을 수 없습니다.");
        });
        userEntity.setProfileImageUrl(imageFileName);

        return userEntity;
    }

    @Transactional
    public User userProfileImageUpdateS3(int principalId, MultipartFile profileImageFile) {
        UUID uuid = UUID.randomUUID();
        String encFileName = URLEncoder.encode(Objects.requireNonNull(profileImageFile.getOriginalFilename()), StandardCharsets.UTF_8); // 파일명이 한글일 경우 인코딩
        String imageFileName = uuid + "_" + encFileName; // 1.jpg

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(profileImageFile.getSize());
        objectMetadata.setContentType(profileImageFile.getContentType());

        try (InputStream inputStream = profileImageFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, imageFileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            e.printStackTrace();
        }
        User userEntity = userRepository.findById(principalId).orElseThrow(() -> {
            throw new CustomApiException("유저를 찾을 수 없습니다.");
        });
        userEntity.setProfileImageUrl(imageFileName);

        return userEntity;
    }

    @Transactional(readOnly = true)
    public UserProfileDto UserProfile(int pageUserId, int principalId) {
        UserProfileDto dto = new UserProfileDto();

        User userEntity = userRepository.findById(pageUserId).orElseThrow(() -> {
            throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
        });

        dto.setUser(userEntity);
        dto.setPageOwnerState(pageUserId == principalId);
        dto.setImageCount(userEntity.getImages().size());

        int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
        int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);
        int followerCount = subscribeRepository.mFollowerCount(pageUserId);
        int followingCount = subscribeRepository.mFollowingCount(pageUserId);

        dto.setSubscribeState(subscribeState == 1);
        dto.setSubscribeCount(subscribeCount);
        dto.setFollowerCount(followerCount);
        dto.setFollowingCount(followingCount);

        userEntity.getImages().forEach((i) -> {
            i.setLikesCount(i.getLikes().size());
            i.setCommentsCount(i.getComments().size());
        }); // 좋아요 카운트 수

        return dto;
    }

    @Transactional
    public User userUpdate(int id, User user) {
        User userEntity = userRepository.findById(id).orElseThrow(() -> new CustomValidationApiException("찾을 수 없는 ID입니다."));
        userEntity.setName(user.getName());

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);

        userEntity.setPassword(encPassword);
        userEntity.setBio(user.getBio());
        userEntity.setWebsite(user.getWebsite());
        userEntity.setPhone(user.getPhone());
        userEntity.setGender(user.getGender());
        return userEntity;
    }

    @Transactional(readOnly = true)
    public List<SearchNameDto> search(String condition) {
        List<User> users = userRepository.findAllByUsernameOrName(condition, condition);
        List<SearchNameDto> searchedUsers = new ArrayList<>();
        users.forEach(user -> {
            SearchNameDto searchNameDto = new SearchNameDto(user.getId(), user.getUsername(), user.getName(), user.getProfileImageUrl());
            searchedUsers.add(searchNameDto);
        });

        return searchedUsers;
    }
}
