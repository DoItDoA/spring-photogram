package com.cos.insta.repository.commentQuerydsl;


import com.cos.insta.domain.comment.Comment;
import com.cos.insta.web.dto.comment.CommentOutputDto;

import java.util.List;
import java.util.Map;

public interface CommentRepositoryCustom {

    Map<Integer, List<CommentOutputDto>> commentsByImageIds(List<Integer> imageIds);
}
