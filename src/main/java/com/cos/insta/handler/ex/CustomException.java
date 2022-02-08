package com.cos.insta.handler.ex;

import java.util.Map;

public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1L; // 객체를 구분할 때

    public CustomException(String message) {
        super(message);
    }
}
