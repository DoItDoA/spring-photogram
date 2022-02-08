package com.cos.insta.repository;

import com.cos.insta.domain.notipication.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findAllByToUserIdOrderByCreateDateDesc(int loginUserId);
}
