package com.cos.insta.domain.notipication;

import lombok.Getter;

@Getter
public enum NotificationType {
    LIKE("좋아요"), COMMENT("댓글작성"), FOLLOW("팔로우");

    NotificationType(String key) {
        this.key = key;
    }

    private String key;
}
