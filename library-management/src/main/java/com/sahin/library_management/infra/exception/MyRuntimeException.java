package com.sahin.library_management.infra.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class MyRuntimeException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String title;

    public MyRuntimeException(String title) {
        this(title, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public MyRuntimeException(String title, HttpStatus httpStatus) {
        super();
        this.httpStatus = httpStatus;
        this.title = title;
    }

    public MyRuntimeException(String title, String message) {
        this(title, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public MyRuntimeException(String title, String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.title = title;
    }

    public MyRuntimeException(String title, String message, Throwable cause) {
        this(title, message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public MyRuntimeException(String title, String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.title = title;
    }
}
