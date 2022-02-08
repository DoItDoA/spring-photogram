package com.cos.insta.repository.imageQuerydsl;

import com.cos.insta.domain.image.Image;
import com.cos.insta.web.dto.image.ImagePagingDto;
import com.cos.insta.web.dto.image.ImagePopularDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ImageRepositoryCustom {
    Page<ImagePagingDto> mStory(int principalId, Pageable pageable);
    Page<ImagePagingDto> mStory(List<Integer> imageIds, Pageable pageable);
    Page<ImagePagingDto> mMyStory(int imageId, Pageable pageable);
}
