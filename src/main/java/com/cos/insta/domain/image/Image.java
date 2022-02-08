package com.cos.insta.domain.image;

import com.cos.insta.domain.comment.Comment;
import com.cos.insta.domain.likes.Likes;
import com.cos.insta.domain.tag.Hashtag;
import com.cos.insta.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String caption;
    private String postImageUrl;

    @JsonIgnoreProperties({"images"})
    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "image", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Likes> likes;

    @OneToMany(mappedBy = "image", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "image", cascade = CascadeType.REMOVE, orphanRemoval = true) //연관관계 주인의 변수명을 적는다.
    private List<Hashtag> tags;

    @Transient // DB에 컬럼이 만들어지지 않는다
    private int likesCount;

    @Transient // DB에 컬럼이 만들어지지 않는다
    private int commentsCount;

    private LocalDateTime createDate;

    @PrePersist
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }
}
