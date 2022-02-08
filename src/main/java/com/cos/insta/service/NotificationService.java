package com.cos.insta.service;

import com.cos.insta.domain.notipication.Notification;
import com.cos.insta.repository.NotificationRepository;
import com.cos.insta.web.dto.notification.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public List<Notification> alarmList(int loginUserId) {

        return notificationRepository.findAllByToUserIdOrderByCreateDateDesc(loginUserId);


    }

    @Transactional
    public void alarmDelete(int notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}