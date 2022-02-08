package com.cos.insta.repository.hashtagQuerydsl;

import com.cos.insta.web.dto.hashtag.HashtagDto;

import java.util.List;
import java.util.Map;

public interface HashtagRepositoryCustom {
    Map<Integer, List<HashtagDto>> hashtagsByImageIds(List<Integer> imageIds);
    List<Integer> hashtagsBySearch(String search);
}
