package com.sahin.library_management.component;

import com.sahin.library_management.infra.exception.ErrorResponse;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(value = MyRuntimeException.class)
    protected ResponseEntity<ErrorResponse> handle(MyRuntimeException ex) {
        log.error(ex.getTitle() + ": " + ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getTitle(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handle(AccessDeniedException ex) {
        log.error("Access: " + ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse("Access", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorResponse> handle(Exception ex) {
        log.error("General" + ": " + ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse("General", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}