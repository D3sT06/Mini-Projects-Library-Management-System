package com.sahin.library_management.component;

import com.sahin.library_management.infra.exception.ErrorResponse;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = MyRuntimeException.class)
    protected ResponseEntity<ErrorResponse> handle(MyRuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getTitle(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorResponse> handle(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("General", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}