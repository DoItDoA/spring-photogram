package com.cos.insta.repository.subscribeQuerydsl;

import com.cos.insta.web.dto.subscribe.SubscribeDto;

import java.util.List;

public interface SubscribeRepositoryCustom {

    List<SubscribeDto> followingList(Integer principalId, Integer pageUserId);
    List<SubscribeDto> followerList(Integer principalId, Integer pageUserId);

}
