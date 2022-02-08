package com.cos.insta.web.dto.user;

import com.cos.insta.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserProfileDto {
    private boolean pageOwnerState; // true 페이지 주인, false 주인이 아님
    private int imageCount;
    private boolean subscribeState; // 팔로우 상태
    private int subscribeCount;
    private int followerCount;
    private int followingCount;
    private User user;
}
