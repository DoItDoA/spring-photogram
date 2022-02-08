package com.cos.insta.service;

import com.cos.insta.domain.image.Image;
import com.cos.insta.domain.likes.Likes;
import com.cos.insta.domain.notipication.Notification;
import com.cos.insta.domain.notipication.NotificationType;
import com.cos.insta.domain.user.User;
import com.cos.insta.handler.ex.CustomApiException;
import com.cos.insta.repository.ImageRepository;
import com.cos.insta.repository.LikesRepository;
import com.cos.insta.repository.NotificationRepository;
import com.cos.insta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class LikesService {
    @Autowired
    LikesRepository likesRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NotificationRepository notificationRepository;

    public void like(int imageId, int principalId) {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> {
            throw new CustomApiException("이미지 아이디를 찾을 수 없습니다.");
        });
        User user = userRepository.findById(principalId).orElseThrow(() -> {
            throw new CustomApiException("유저 아이디를 찾을 수 없습니다.");
        });

        likesRepository.save(new Likes(image, user));
        if (image.getUser().getId() != user.getId()) {
            notificationRepository.save(new Notification(NotificationType.LIKE, user, image.getUser()));
        }
    }

    public void unlike(int imageId, int principalId) {
        likesRepository.mUnlike(imageId, principalId);
    }
}
