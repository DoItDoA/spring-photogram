package com.cos.insta.web.dto.comment;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentOutputDto {
    private int id;
    private String content;
    private int imageId;
    private int userId;
    private String username;
    private LocalDateTime createDatetime;

}
