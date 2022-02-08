package com.cos.insta.repository.likesQuerydsl;

import com.cos.insta.web.dto.image.ImagePopularDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cos.insta.domain.likes.QLikes.*;

public class LikesRepositoryImpl implements LikesRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public LikesRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Map<Integer, Boolean> findLikesMap(int imageId, int principalId) {
        Integer result = jpaQueryFactory
                .select(likes.image.id)
                .from(likes)
                .where(likes.image.id.eq(imageId).and(likes.user.id.eq(principalId)))
                .fetchOne();

        return new HashMap<>() {{
            if (result != null) {
                put(result, true);
            }
        }};

    }

    @Override
    public Integer findLikesCountMap(int imageId, int principalId) {
        return jpaQueryFactory
                .select(likes.image.id)
                .from(likes)
                .where(likes.image.id.eq(imageId))
                .fetch().size();

    }

    @Override
    public List<ImagePopularDto> mPopular() {
        return jpaQueryFactory
                .select(Projections.constructor(
                        ImagePopularDto.class,
                        likes.image.id,
                        likes.image.postImageUrl,
                        likes.image.user.id,
                        likes.image.count().as("likesCount"),
                        likes.image.comments.size())
                )
                .from(likes)
                .groupBy(likes.image)
                .orderBy(likes.image.count().desc())
                .fetch();
    }
}
