package com.github.blog.controller.annotation.etc;

import com.github.blog.controller.util.validator.etc.CustomReactionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Raman Haurylau
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomReactionValidator.class)
public @interface UniqueReaction {

    String message() default "Already exists with this name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
