package com.cos.insta.repository;

import com.cos.insta.domain.comment.Comment;
import com.cos.insta.repository.commentQuerydsl.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> , CommentRepositoryCustom {

}
