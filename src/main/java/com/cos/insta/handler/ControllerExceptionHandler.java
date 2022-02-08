package com.cos.insta.handler;

import com.cos.insta.handler.ex.CustomApiException;
import com.cos.insta.handler.ex.CustomException;
import com.cos.insta.handler.ex.CustomValidationApiException;
import com.cos.insta.handler.ex.CustomValidationException;
import com.cos.insta.util.Script;
import com.cos.insta.web.dto.CMRespDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@ControllerAdvice // 도든 exception 발생시 실행
public class ControllerExceptionHandler {

    @ExceptionHandler(CustomValidationException.class) // RuntimeException 오류 감지
    public String validationException(CustomValidationException e) {
        if(e.getErrorMap()==null){
            return Script.back(e.getMessage());
        }
        return Script.back(e.getErrorMap().toString());
    }

    @ExceptionHandler(CustomValidationApiException.class) // RuntimeException 오류 감지
    public ResponseEntity<?> validationApiException(CustomValidationApiException e) {
        return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomApiException.class) // RuntimeException 오류 감지
    public ResponseEntity<?> apiException(CustomApiException e) {
        return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class) // RuntimeException 오류 감지
    public String Exception(CustomException e) {

        return Script.back(e.getMessage());
    }
}
