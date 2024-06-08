package com.github.blog.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public enum ExceptionEnum {
    // Jwt
    AUTHENTICATION_TOKEN_EXCEPTION(HttpStatus.NOT_ACCEPTABLE, "Please verify the validity of your token/account"),
    // State Machine
    STATE_TRANSITION_EXCEPTION(HttpStatus.BAD_REQUEST, "Unable to change state"),
    // Auth
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "Authentication failed"),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "You have entered either the Username and/or Password incorrectly"),
    //  User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USERS_NOT_FOUND(HttpStatus.NOT_FOUND, "No users could be found"),
    //  UserInfo
    USER_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "No user info could be found"),
    //  Order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Order not found"),
    ORDERS_NOT_FOUND(HttpStatus.NOT_FOUND, "No orders could be found"),
    //  Tag
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "Tag not found"),
    TAGS_NOT_FOUND(HttpStatus.NOT_FOUND, "No tags could be found"),
    //  Role
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "Role not found"),
    ROLES_NOT_FOUND(HttpStatus.NOT_FOUND, "No roles could be found"),
    // Post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post not found"),
    POSTS_NOT_FOUND(HttpStatus.NOT_FOUND, "No posts could be found"),
    //  Post reaction
    POST_REACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Post reaction not found"),
    POST_REACTIONS_NOT_FOUND(HttpStatus.NOT_FOUND, "No post reactions could be found"),
    // Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment not found"),
    COMMENTS_NOT_FOUND(HttpStatus.NOT_FOUND, "No comments could be found"),
    //  Comment reaction
    COMMENT_REACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment reaction not found"),
    COMMENT_REACTIONS_NOT_FOUND(HttpStatus.NOT_FOUND, "No comment reactions could be found"),
    // Reaction
    REACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Reaction not found"),
    REACTIONS_NOT_FOUND(HttpStatus.NOT_FOUND, "No reactions could be found");

    private final HttpStatus status;
    private final String message;
}
