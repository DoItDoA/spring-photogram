package com.cos.insta.repository;

import com.cos.insta.domain.subscribe.Subscribe;
import com.cos.insta.repository.subscribeQuerydsl.SubscribeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer>, SubscribeRepositoryCustom {
    @Modifying
    @Query(value = "INSERT INTO subscribe(fromUserId, toUserId, createDate) VALUES(:fromUserId, :toUserId, now())", nativeQuery = true)
    void mSubscribe(@Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId);

    @Modifying
    @Query("DELETE FROM Subscribe s WHERE s.fromUser.id = :fromUserId AND s.toUser.id = :toUserId")
    void mUnSubscribe(@Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId);

    @Query("SELECT COUNT(s) FROM Subscribe s WHERE s.fromUser.id = :principalId AND s.toUser.id = :pageUserId")
    int mSubscribeState(@Param("principalId") int principalId, @Param("pageUserId") int pageUserId);

    @Query("SELECT COUNT(s) FROM Subscribe s WHERE s.fromUser.id = :pageUserId")
    int mSubscribeCount(@Param("pageUserId") int pageUserId);

    @Query("SELECT COUNT(s) FROM Subscribe s WHERE s.fromUser.id = :pageUserId")
    int mFollowingCount(@Param("pageUserId") int pageUserId);

    @Query("SELECT COUNT(s) FROM Subscribe s WHERE s.toUser.id = :pageUserId")
    int mFollowerCount(@Param("pageUserId") int pageUserId);
}
