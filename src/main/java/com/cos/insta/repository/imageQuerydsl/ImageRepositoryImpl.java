package com.cos.insta.repository.imageQuerydsl;

import com.cos.insta.domain.image.Image;
import com.cos.insta.domain.image.QImage;
import com.cos.insta.domain.likes.QLikes;
import com.cos.insta.domain.subscribe.QSubscribe;
import com.cos.insta.domain.user.QUser;
import com.cos.insta.domain.user.User;
import com.cos.insta.web.dto.image.ImagePagingDto;
import com.cos.insta.web.dto.image.ImagePopularDto;
import com.cos.insta.web.dto.image.QImagePagingDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.cos.insta.domain.image.QImage.*;
import static com.cos.insta.domain.likes.QLikes.*;
import static com.cos.insta.domain.subscribe.QSubscribe.*;
import static com.cos.insta.domain.user.QUser.*;

public class ImageRepositoryImpl implements ImageRepositoryCustom {

    JPAQueryFactory jpaQueryFactory;

    public ImageRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ImagePagingDto> mStory(int principalId, Pageable pageable) {

        List<ImagePagingDto> result = jpaQueryFactory
                .select(new QImagePagingDto(image.id, image.caption, image.postImageUrl, image.user, image.createDate))
                .from(image)
                .where(image.user.id.in(JPAExpressions
                                .select(subscribe.toUser.id)
                                .from(subscribe)
                                .join(subscribe.fromUser, user)
                                .where(subscribe.fromUser.id.eq(principalId))
                        )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<ImagePagingDto> mStory(List<Integer> imageIds, Pageable pageable) {
        List<ImagePagingDto> result = jpaQueryFactory
                .select(new QImagePagingDto(image.id, image.caption, image.postImageUrl, image.user, image.createDate))
                .from(image)
                .where(image.id.in(imageIds))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<ImagePagingDto> mMyStory(int imageId, Pageable pageable) {
        List<ImagePagingDto> result = jpaQueryFactory
                .select(new QImagePagingDto(image.id, image.caption, image.postImageUrl, image.user, image.createDate))
                .from(image)
                .where(image.id.eq(imageId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }
}
