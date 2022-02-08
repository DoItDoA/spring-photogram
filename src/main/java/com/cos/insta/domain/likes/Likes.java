package com.cos.insta.domain.likes;

import com.cos.insta.domain.image.Image;
import com.cos.insta.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "subscribe_uk",
                        columnNames = {"imageId", "userId"}
                )
        }
)
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @JoinColumn(name = "imageId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Image image;

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

    private LocalDateTime createDate;

    public Likes(Image image, User user) {
        this.image = image;
        this.user = user;
    }

    @PrePersist
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }
}
