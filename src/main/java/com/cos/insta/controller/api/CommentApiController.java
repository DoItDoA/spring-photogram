package com.cos.insta.controller.api;

import com.cos.insta.config.auth.PrincipalDetails;
import com.cos.insta.domain.comment.Comment;
import com.cos.insta.handler.ex.CustomValidationApiException;
import com.cos.insta.service.CommentService;
import com.cos.insta.web.dto.CMRespDto;
import com.cos.insta.web.dto.comment.CommentInputDto;
import com.cos.insta.web.dto.comment.CommentOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CommentApiController {

    @Autowired
    CommentService commentService;

    @PostMapping("/api/comment")
    public ResponseEntity<?> commentSave(@Validated @RequestBody CommentInputDto commentInputDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        CommentOutputDto comment = commentService.writeComment(commentInputDto.getContent(), commentInputDto.getImageId(), principalDetails.getUser().getId());
        return new ResponseEntity<>(new CMRespDto<>(1, "댓글쓰기 성공", comment), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/comment/{id}")
    public ResponseEntity<?> commentDelete(@PathVariable int id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>(new CMRespDto<>(1, "댓글삭제 성공", null), HttpStatus.OK);
    }
}
