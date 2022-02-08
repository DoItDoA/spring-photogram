package com.cos.insta.service;

import com.cos.insta.domain.user.User;
import com.cos.insta.handler.ex.CustomException;
import com.cos.insta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void createUser(User user) {
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setRole("ROLE_USER");
        user.setPassword(encPassword);

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new CustomException("이미 존재하는 유저네임입니다.");
        }
    }
}
