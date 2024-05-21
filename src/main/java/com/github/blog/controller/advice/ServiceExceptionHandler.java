package com.github.blog.controller.advice;

import com.github.blog.service.exception.CustomEntityException;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CommentException;
import com.github.blog.service.exception.impl.CommentReactionException;
import com.github.blog.service.exception.impl.OrderException;
import com.github.blog.service.exception.impl.PostException;
import com.github.blog.service.exception.impl.PostReactionException;
import com.github.blog.service.exception.impl.ReactionException;
import com.github.blog.service.exception.impl.RoleException;
import com.github.blog.service.exception.impl.TagException;
import com.github.blog.service.exception.impl.UserException;
import com.github.blog.service.exception.impl.UserInfoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Raman Haurylau
 */
@RestControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler({UserException.class, UserInfoException.class, TagException.class,
            RoleException.class, PostReactionException.class, PostException.class,
            OrderException.class, CommentReactionException.class, CommentException.class, ReactionException.class})
    public ResponseEntity<ErrorResponse> handleCustomEntityException(CustomEntityException e) {
        ExceptionEnum exceptionEnum = e.getExceptionEnum();
        return ResponseEntity
                .status(exceptionEnum.getStatus())
                .body(new ErrorResponse(exceptionEnum.name(), exceptionEnum.getMessage(), System.currentTimeMillis()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), e.getMessage(), System.currentTimeMillis()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage(), System.currentTimeMillis()));
    }

    public record ErrorResponse(String status, String message, Long timestamp) {
    }
}
