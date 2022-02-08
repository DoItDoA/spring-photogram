package com.cos.insta.repository.subscribeQuerydsl;

import com.cos.insta.domain.subscribe.QSubscribe;
import com.cos.insta.domain.user.QUser;
import com.cos.insta.web.dto.subscribe.QSubscribeDto;
import com.cos.insta.web.dto.subscribe.SubscribeDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.cos.insta.domain.subscribe.QSubscribe.*;
import static com.cos.insta.domain.user.QUser.*;
import static org.springframework.util.StringUtils.hasText;


public class SubscribeRepositoryImpl implements SubscribeRepositoryCustom {


    private final JPAQueryFactory queryFactory;

    public SubscribeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<SubscribeDto> followingList(Integer principalId, Integer pageUserId) {
        QSubscribe subscribeSub = new QSubscribe("subscribeSub");
        QUser userSub = new QUser("userSub");
        return queryFactory
                .select(Projections.fields(SubscribeDto.class,
                                user.id,
                                user.username,
                                user.name,
                                user.profileImageUrl,
                                ExpressionUtils.as(JPAExpressions
                                        .selectFrom(subscribeSub)
                                        .where(subscribeSub.fromUser.id.eq(principalId)
                                                .and(subscribeSub.toUser.eq(user))
                                        ).exists(), "subscribeState"),
                                ExpressionUtils.as(JPAExpressions
                                        .selectFrom(userSub)
                                        .where(user.id.eq(principalId))
                                        .exists(), "equalUserState")
                        )
                )
                .from(subscribe)
                .join(subscribe.toUser, user)
                .where(subscribe.fromUser.id.eq(pageUserId))
                .fetch();

    }

    @Override
    public List<SubscribeDto> followerList(Integer principalId, Integer pageUserId) {
        QSubscribe subscribeSub = new QSubscribe("subscribeSub");
        QUser userSub = new QUser("userSub");
        return queryFactory
                .select(Projections.fields(SubscribeDto.class,
                                user.id,
                                user.username,
                                user.name,
                                user.profileImageUrl,
                                ExpressionUtils.as(JPAExpressions
                                        .selectFrom(subscribeSub)
                                        .where(subscribeSub.fromUser.id.eq(principalId)
                                                .and(subscribeSub.toUser.eq(user))
                                        ).exists(), "subscribeState"),
                                ExpressionUtils.as(JPAExpressions
                                        .selectFrom(userSub)
                                        .where(user.id.eq(principalId))
                                        .exists(), "equalUserState")
                        )
                )
                .from(subscribe)
                .join(subscribe.fromUser, user)
                .where(subscribe.toUser.id.eq(pageUserId))
                .fetch();
    }

}
