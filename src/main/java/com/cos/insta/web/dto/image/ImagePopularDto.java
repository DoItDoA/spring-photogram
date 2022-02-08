package com.cos.insta.web.dto.image;

import com.cos.insta.domain.image.Image;
import com.cos.insta.domain.user.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImagePopularDto {

    private int imageId;
    private String postImageUrl;
    private int userId;
    private Long likesCount;
    private Integer commentsCount;
}
