package com.cos.insta.controller;

import com.cos.insta.domain.user.User;
import com.cos.insta.handler.ex.CustomValidationException;
import com.cos.insta.service.AuthService;
import com.cos.insta.web.dto.auth.SignupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/auth/signin")
    public String signinForm() {
        return "auth/signin";
    }

    @GetMapping("/auth/signup")
    public String signupForm() {
        return "auth/signup";
    }

    @PostMapping("/auth/signup")
    public String signup(@Valid SignupDto signupDto, BindingResult bindingResult) {

        User user = signupDto.toEntity(); // dto 정보를 User에 넣기
        authService.createUser(user);
        return "auth/signin";
    }
}
