package com.cos.insta.repository;

import com.cos.insta.domain.tag.Hashtag;
import com.cos.insta.repository.hashtagQuerydsl.HashtagRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Integer> , HashtagRepositoryCustom {
}
