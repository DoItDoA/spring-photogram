package com.cos.insta.web.dto.likes;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class LikesDto {
    private int imageId;
    private int userId;
//    private int likesCount;
    private LocalDateTime localDateTime;

    @QueryProjection
    public LikesDto(int imageId, int userId, LocalDateTime localDateTime) {
        this.imageId = imageId;
        this.userId = userId;
//        this.likesCount = likesCount;
        this.localDateTime = localDateTime;
    }
}
