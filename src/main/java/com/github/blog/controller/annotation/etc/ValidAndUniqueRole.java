package com.github.blog.controller.annotation.etc;

import com.github.blog.controller.util.validator.etc.CustomRoleValidator;
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
@Constraint(validatedBy = CustomRoleValidator.class)
public @interface ValidAndUniqueRole {

    public String message() default "Invalid role or already exists with this name";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default{};
}
