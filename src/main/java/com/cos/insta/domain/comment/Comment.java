package com.cos.insta.domain.comment;

import com.cos.insta.domain.image.Image;
import com.cos.insta.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100, nullable = false)
    private String content;

    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JsonIgnore
    @JoinColumn(name = "imageId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Image image;

    private LocalDateTime createDate;

    public Comment(String content, User user, Image image) {
        this.content = content;
        this.user = user;
        this.image = image;
    }

    @PrePersist
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }
}
