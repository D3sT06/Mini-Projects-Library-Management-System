package com.sahin.library_management.component;

import com.sahin.library_management.infra.exception.ErrorResponse;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ErrorResponse handle(AccessDeniedException ex) {
        log.error("Access: " + ex.getMessage(), ex);
        return new ErrorResponse("Access", ex.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse handle(Exception ex) {
        log.error("General" + ": " + ex.getMessage(), ex);
        return new ErrorResponse("General", ex.getMessage());
    }


}