package com.cos.insta.repository;

import com.cos.insta.domain.likes.Likes;
import com.cos.insta.repository.likesQuerydsl.LikesRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikesRepository extends JpaRepository<Likes, Integer>, LikesRepositoryCustom {

    @Modifying
    @Query("DELETE FROM Likes l WHERE l.user.id = :principalId AND l.image.id = :imageId")
    int mUnlike(@Param("imageId") int imageId, @Param("principalId") int principalId);

}
