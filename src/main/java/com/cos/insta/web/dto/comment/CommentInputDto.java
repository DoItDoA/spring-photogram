package com.cos.insta.web.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

// @NotBlank 빈 값이거나 null 체크, 빈 공백 체크
// @NotEmpty 빈 값이거나 null 체크
// @NotNull null 체크

@Data
public class CommentInputDto {
    @NotBlank // 빈 값이거나 null 체크, 빈 공백 체크
    private String content;
    @NotNull // 빈 값이거나 null 체크
    private Integer imageId;
}
