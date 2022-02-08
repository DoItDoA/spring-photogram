package com.cos.insta.service;

import com.cos.insta.domain.notipication.Notification;
import com.cos.insta.domain.notipication.NotificationType;
import com.cos.insta.domain.subscribe.Subscribe;
import com.cos.insta.domain.user.User;
import com.cos.insta.handler.ex.CustomApiException;
import com.cos.insta.repository.NotificationRepository;
import com.cos.insta.repository.SubscribeRepository;
import com.cos.insta.web.dto.search.SearchNameDto;
import com.cos.insta.web.dto.subscribe.SubscribeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class SubscribeService {

    @Autowired
    SubscribeRepository subscribeRepository;
    @Autowired
    NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public List<SubscribeDto> followingList(Integer principalId, Integer pageUserId) {
        return subscribeRepository.followingList(principalId, pageUserId);
    }

    @Transactional(readOnly = true)
    public List<SubscribeDto> followerList(Integer principalId, Integer pageUserId) {
        return subscribeRepository.followerList(principalId, pageUserId);
    }

    @Transactional
    public int subscribe(int fromUserId, int toUserId) {

        try {
            User user1 = new User();
            User user2 = new User();
            user1.setId(fromUserId);
            user2.setId(toUserId);

            subscribeRepository.save(new Subscribe(user1, user2));
            notificationRepository.save(new Notification(NotificationType.FOLLOW, user1, user2));
            return subscribeRepository.mFollowerCount(toUserId);
        } catch (Exception e) {
            throw new CustomApiException("이미 팔로우를 하였습니다.");
        }
    }

    @Transactional
    public int unSubscribe(int fromUserId, int toUserId) {
        subscribeRepository.mUnSubscribe(fromUserId, toUserId);
        return subscribeRepository.mFollowerCount(toUserId);
    }

}
