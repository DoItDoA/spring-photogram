package com.cos.insta.repository.commentQuerydsl;

import com.cos.insta.domain.comment.Comment;
import com.cos.insta.domain.comment.QComment;
import com.cos.insta.web.dto.comment.CommentOutputDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cos.insta.domain.comment.QComment.*;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    JPAQueryFactory jpaQueryFactory;

    public CommentRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Map<Integer, List<CommentOutputDto>> commentsByImageIds(List<Integer> imageIds) {
        List<CommentOutputDto> result = jpaQueryFactory
                .select(Projections.constructor(CommentOutputDto.class,
                        comment.id,
                        comment.content,
                        comment.image.id,
                        comment.user.id,
                        comment.user.username,
                        comment.createDate))
                .from(comment)
                .where(comment.image.id.in(imageIds))
                .orderBy(comment.createDate.desc())
                .fetch();

        return result
                .stream()
                .collect(Collectors.groupingBy(CommentOutputDto::getImageId));

    }
}
