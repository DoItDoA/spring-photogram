package com.cos.insta.controller.api;

import com.cos.insta.service.NotificationService;
import com.cos.insta.web.dto.CMRespDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationApiController {
    @Autowired
    NotificationService notificationService;

    @DeleteMapping("/api/notification/{notificationId}")
    public ResponseEntity<?> notificationDelete(@PathVariable int notificationId) {
        notificationService.alarmDelete(notificationId);
        return new ResponseEntity<>(new CMRespDto<>(1, "알림 삭제 성공", null), HttpStatus.OK);
    }
}
