package com.cos.insta.controller.api;

import com.cos.insta.config.auth.PrincipalDetails;
import com.cos.insta.domain.image.Image;
import com.cos.insta.domain.user.User;
import com.cos.insta.service.ImageService;
import com.cos.insta.service.LikesService;
import com.cos.insta.web.dto.CMRespDto;
import com.cos.insta.web.dto.image.ImagePagingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ImageApiController {

    @Autowired
    ImageService imageService;

    @Autowired
    LikesService likesService;

    @GetMapping("/api/image")
    public ResponseEntity<?> imageStory(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                        @RequestParam String search) {
        Page<ImagePagingDto> images = imageService.imageStory(principalDetails.getUser().getId(), pageable, search, -1);
        return new ResponseEntity<>(new CMRespDto<>(1, "이미지 불러오기 성공", images), HttpStatus.OK);
    }

    @GetMapping("/api/image/{imageId}/myStory")
    public ResponseEntity<?> myImageStory(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                          Pageable pageable,
                                          @PathVariable int imageId) {
        Page<ImagePagingDto> image = imageService.imageStory(principalDetails.getUser().getId(), pageable, null, imageId);
        return new ResponseEntity<>(new CMRespDto<>(1, "이미지 불러오기 성공", image), HttpStatus.OK);
    }

    @PostMapping("/api/image/{imageId}/likes")
    public ResponseEntity<?> likes(@PathVariable int imageId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        likesService.like(imageId, principalDetails.getUser().getId());
        return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 성공", null), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/image/{imageId}/likes")
    public ResponseEntity<?> unlikes(@PathVariable int imageId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        likesService.unlike(imageId, principalDetails.getUser().getId());
        return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 취소 성공", null), HttpStatus.OK);
    }

    @PutMapping("/api/image/{imageId}/imageUrl")
    public ResponseEntity<?> profileImageUrlUpdate(@PathVariable int imageId,
                                                   MultipartFile myStoryImageFile,
                                                   @AuthenticationPrincipal PrincipalDetails principalDetails) { // 변수명은 html의 name과 일치해야한다

        imageService.myStoryImageUpdateS3(imageId, myStoryImageFile);

        return new ResponseEntity<>(new CMRespDto<>(1, "이미지사진 변경 성공", null), HttpStatus.OK);
    }

    @DeleteMapping("/api/image/{imageId}/remove")
    public ResponseEntity<?> imageDelete(@PathVariable int imageId) {
        imageService.myStoryImageDelete(imageId);
        return new ResponseEntity<>(new CMRespDto<>(1, "이미지 삭제 성공", null), HttpStatus.OK);
    }
}
