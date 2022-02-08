package com.cos.insta.web.dto.notification;

import com.cos.insta.domain.notipication.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class NotificationDto {
    private int id;
    private NotificationType notificationType;
    private int fromUserId;
    private int toUserId;
    private Timestamp createDate;
}
