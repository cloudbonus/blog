package com.github.blog.controller.annotation.user;

import com.github.blog.controller.util.validator.user.CustomUniqueEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Raman Haurylau
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomUniqueEmailValidator.class)
public @interface UniqueEmail {

    String message() default "There is already user with this email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
