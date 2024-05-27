package com.github.blog.controller.annotation.user;

import com.github.blog.controller.util.validator.user.CustomUniqueUsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Raman Haurylau
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomUniqueUsernameValidator.class)
public @interface UniqueUsername {
    String message() default "There is already user with this username";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
