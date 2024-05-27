package com.github.blog.controller.annotation.user;

import com.github.blog.controller.util.validator.user.CustomUserInfoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Raman Haurylau
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = CustomUserInfoValidator.class)
public @interface ValidUserInfo {
    String message() default "Please provide either your university or job details, or both, but not leave both blank";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
