package com.cos.insta.web.dto.subscribe;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@Data
public class SubscribeDto {
    private int id;
    private String username;
    private String name;
    private String profileImageUrl;
    private boolean subscribeState;
    private boolean equalUserState;

    @QueryProjection
    public SubscribeDto(int id, String username, String name, String profileImageUrl, boolean subscribeState, boolean equalUserState) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.subscribeState = subscribeState;
        this.equalUserState = equalUserState;
    }

}
