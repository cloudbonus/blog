package com.github.blog.controller.advice;

import com.github.blog.service.exception.CommentErrorResult;
import com.github.blog.service.exception.CommentReactionErrorResult;
import com.github.blog.service.exception.OrderErrorResult;
import com.github.blog.service.exception.PostErrorResult;
import com.github.blog.service.exception.PostReactionErrorResult;
import com.github.blog.service.exception.RoleErrorResult;
import com.github.blog.service.exception.TagErrorResult;
import com.github.blog.service.exception.UserDetailErrorResult;
import com.github.blog.service.exception.UserErrorResult;
import com.github.blog.service.exception.impl.CommentException;
import com.github.blog.service.exception.impl.CommentReactionException;
import com.github.blog.service.exception.impl.OrderException;
import com.github.blog.service.exception.impl.PostException;
import com.github.blog.service.exception.impl.PostReactionException;
import com.github.blog.service.exception.impl.RoleException;
import com.github.blog.service.exception.impl.TagException;
import com.github.blog.service.exception.impl.UserDetailException;
import com.github.blog.service.exception.impl.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Raman Haurylau
 */
@RestControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        UserErrorResult errorResult = e.getErrorResult();
        return ResponseEntity
                .status(errorResult.getStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage(), System.currentTimeMillis()));
    }

    @ExceptionHandler(UserDetailException.class)
    public ResponseEntity<ErrorResponse> handleUserDetailException(UserDetailException e) {
        UserDetailErrorResult errorResult = e.getErrorResult();
        return ResponseEntity
                .status(errorResult.getStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage(), System.currentTimeMillis()));
    }

    @ExceptionHandler(TagException.class)
    public ResponseEntity<ErrorResponse> handleTagException(TagException e) {
        TagErrorResult errorResult = e.getErrorResult();
        return ResponseEntity
                .status(errorResult.getStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage(), System.currentTimeMillis()));
    }

    @ExceptionHandler(RoleException.class)
    public ResponseEntity<ErrorResponse> handleRoleException(RoleException e) {
        RoleErrorResult errorResult = e.getErrorResult();
        return ResponseEntity
                .status(errorResult.getStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage(), System.currentTimeMillis()));
    }

    @ExceptionHandler(PostReactionException.class)
    public ResponseEntity<ErrorResponse> handlePostReactionException(PostReactionException e) {
        PostReactionErrorResult errorResult = e.getErrorResult();
        return ResponseEntity
                .status(errorResult.getStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage(), System.currentTimeMillis()));
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<ErrorResponse> handlePostException(PostException e) {
        PostErrorResult errorResult = e.getErrorResult();
        return ResponseEntity
                .status(errorResult.getStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage(), System.currentTimeMillis()));
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResponse> handleOrderException(OrderException e) {
        OrderErrorResult errorResult = e.getErrorResult();
        return ResponseEntity
                .status(errorResult.getStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage(), System.currentTimeMillis()));
    }

    @ExceptionHandler(CommentReactionException.class)
    public ResponseEntity<ErrorResponse> handleCommentReactionException(CommentReactionException e) {
        CommentReactionErrorResult errorResult = e.getErrorResult();
        return ResponseEntity
                .status(errorResult.getStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage(), System.currentTimeMillis()));
    }

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<ErrorResponse> handleCommentException(CommentException e) {
        CommentErrorResult errorResult = e.getErrorResult();
        return ResponseEntity
                .status(errorResult.getStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage(), System.currentTimeMillis()));
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

    public record ErrorResponse(String code, String message, Long timestamp) {
    }
}
