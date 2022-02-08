package com.cos.insta.web.dto.image;

import com.cos.insta.domain.comment.Comment;
import com.cos.insta.domain.image.Image;
import com.cos.insta.domain.likes.Likes;
import com.cos.insta.domain.user.User;
import com.cos.insta.web.dto.comment.CommentOutputDto;
import com.cos.insta.web.dto.hashtag.HashtagDto;
import com.cos.insta.web.dto.likes.LikesDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
public class ImagePagingDto {

    private int id;
    private String caption;
    private String postImageUrl;
    private User user;
    private LocalDateTime createDate;
    private boolean likeState;
    private int likesCount;
    private List<CommentOutputDto> comments;
    private List<HashtagDto> hashtags;

    @QueryProjection
    public ImagePagingDto(int id, String caption, String postImageUrl, User user, LocalDateTime createDate) {
        this.id = id;
        this.caption = caption;
        this.postImageUrl = postImageUrl;
        this.user = user;
        this.createDate = createDate;
    }

}
