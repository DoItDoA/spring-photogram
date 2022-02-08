package com.cos.insta.repository.hashtagQuerydsl;


import com.cos.insta.domain.tag.QHashtag;
import com.cos.insta.web.dto.hashtag.HashtagDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cos.insta.domain.tag.QHashtag.*;

public class HashtagRepositoryImpl implements HashtagRepositoryCustom {

    JPAQueryFactory queryFactory;

    public HashtagRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Map<Integer, List<HashtagDto>> hashtagsByImageIds(List<Integer> imageIds) {
        List<HashtagDto> result = queryFactory
                .select(Projections.constructor(
                        HashtagDto.class,
                        hashtag.name,
                        hashtag.image.id,
                        hashtag.createDate
                ))
                .from(hashtag)
                .where(hashtag.image.id.in(imageIds))
                .fetch();

        return result
                .stream()
                .collect(Collectors.groupingBy(HashtagDto::getImageId));
    }

    @Override
    public List<Integer> hashtagsBySearch(String search) {
        return queryFactory
                .select(hashtag.image.id)
                .from(hashtag)
                .where(hashtag.name.eq(search))
                .fetch();
    }
}
