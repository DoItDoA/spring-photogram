package com.cos.insta.controller.api;

import com.cos.insta.config.auth.PrincipalDetails;
import com.cos.insta.domain.user.User;
import com.cos.insta.handler.ex.CustomValidationApiException;
import com.cos.insta.handler.ex.CustomValidationException;
import com.cos.insta.service.SubscribeService;
import com.cos.insta.service.UserService;
import com.cos.insta.web.dto.CMRespDto;
import com.cos.insta.web.dto.search.SearchNameDto;
import com.cos.insta.web.dto.subscribe.SubscribeDto;
import com.cos.insta.web.dto.user.UserUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserApiController {

    @Autowired
    UserService userService;
    @Autowired
    SubscribeService subscribeService;

    @PutMapping("/api/user/{principalId}/profileImageUrl")
    public ResponseEntity<?> profileImageUrlUpdate(@PathVariable int principalId,
                                                   MultipartFile profileImageFile,
                                                   @AuthenticationPrincipal PrincipalDetails principalDetails) { // 변수명은 html의 name과 일치해야한다

        User userEntity = userService.userProfileImageUpdateS3(principalId, profileImageFile);
        principalDetails.setUser(userEntity); // 세션 변경
        return new ResponseEntity<>(new CMRespDto<>(1, "프로필사진 변경 성공", null), HttpStatus.OK);
    }

    @GetMapping("/api/user/{pageUserId}/followingList")
    public ResponseEntity<?> followingList(@PathVariable int pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<SubscribeDto> subscribeDto = subscribeService.followingList(principalDetails.getUser().getId(), pageUserId);

        return new ResponseEntity<>(new CMRespDto<>(1, "팔로잉 정보 리스트 가져오기 성공", subscribeDto), HttpStatus.OK);
    }

    @GetMapping("/api/user/{pageUserId}/followerList")
    public ResponseEntity<?> followerList(@PathVariable int pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<SubscribeDto> subscribeDto = subscribeService.followerList(principalDetails.getUser().getId(), pageUserId);

        return new ResponseEntity<>(new CMRespDto<>(1, "팔로워 정보 리스트 가져오기 성공", subscribeDto), HttpStatus.OK);
    }

    @PutMapping("/api/user/{id}")
    public CMRespDto<?> update(@PathVariable int id,
                               @Validated UserUpdateDto userUpdateDto,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal PrincipalDetails principalDetails) {

        User userEntity = userService.userUpdate(id, userUpdateDto.toEntity());
        principalDetails.setUser(userEntity);
        return new CMRespDto<>(1, "회원수정완료", userEntity);
    }

    @GetMapping("/api/user/{condition}")
    public ResponseEntity<?> search(@PathVariable String condition) {
        List<SearchNameDto> search = userService.search(condition);
        return new ResponseEntity<>(new CMRespDto<>(1, "유저검색 정보리스트 가져오기 성공", search), HttpStatus.OK);
    }
}
