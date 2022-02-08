package com.cos.insta.controller;

import com.cos.insta.config.auth.PrincipalDetails;
import com.cos.insta.domain.notipication.Notification;
import com.cos.insta.service.NotificationService;
import com.cos.insta.web.dto.notification.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @GetMapping("/notification/{loginUserId}")
    public String notification(@PathVariable int loginUserId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getUser().getId() == loginUserId) {
            List<Notification> notifications = notificationService.alarmList(loginUserId);
            model.addAttribute("notifications", notifications);
        }
        return "notification/notification";
    }

}
