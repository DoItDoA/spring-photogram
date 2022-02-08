package com.cos.insta.service;

import com.cos.insta.domain.comment.Comment;
import com.cos.insta.domain.image.Image;
import com.cos.insta.domain.notipication.Notification;
import com.cos.insta.domain.notipication.NotificationType;
import com.cos.insta.domain.user.User;
import com.cos.insta.handler.ex.CustomApiException;
import com.cos.insta.repository.CommentRepository;
import com.cos.insta.repository.ImageRepository;
import com.cos.insta.repository.NotificationRepository;
import com.cos.insta.repository.UserRepository;
import com.cos.insta.web.dto.comment.CommentOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    NotificationRepository notificationRepository;

    public CommentOutputDto writeComment(String content, int imageId, int userId) {

        Image image = imageRepository.findById(imageId).orElseThrow(() -> {
            throw new CustomApiException("이미지 아이디를 찾을 수 없습니다.");
        });


        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CustomApiException("유저 아이디를 찾을 수 없습니다.");
        });

        Comment comment = commentRepository.save(new Comment(content, user, image));
        if (image.getUser().getId() != user.getId()) {
            notificationRepository.save(new Notification(NotificationType.COMMENT, user, image.getUser()));
        }
        return new CommentOutputDto(
                comment.getId(),
                comment.getContent(),
                comment.getImage().getId(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getCreateDate()
        );
    }

    public void deleteComment(int id) {
        try {
            commentRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomApiException(e.getMessage());
        }
    }
}
