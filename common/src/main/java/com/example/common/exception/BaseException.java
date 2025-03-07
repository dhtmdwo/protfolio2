package com.example.common.exception;

import com.example.common.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BaseException extends RuntimeException {
    private final BaseResponseStatus status;
    public BaseException(BaseResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
