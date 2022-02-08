package com.cos.insta.repository.likesQuerydsl;

import com.cos.insta.domain.likes.Likes;
import com.cos.insta.web.dto.image.ImagePopularDto;
import com.cos.insta.web.dto.likes.LikesDto;

import java.util.List;
import java.util.Map;

public interface LikesRepositoryCustom {
    Map<Integer, Boolean> findLikesMap(int imageId, int principalId);
    Integer findLikesCountMap(int imageId, int principalId);
    List<ImagePopularDto> mPopular();
}