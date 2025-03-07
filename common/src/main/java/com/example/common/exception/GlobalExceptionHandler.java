package com.example.common.exception;


import com.example.common.BaseResponse;
import com.example.common.BaseResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<BaseResponseStatus>> handleGlobalException(BaseException e) {

        return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
    }
}
