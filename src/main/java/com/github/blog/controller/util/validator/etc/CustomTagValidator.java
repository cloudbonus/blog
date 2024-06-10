package com.github.blog.controller.util.validator.etc;

import com.github.blog.controller.annotation.etc.UniqueTag;
import com.github.blog.controller.dto.request.TagRequest;
import com.github.blog.repository.TagRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@RequiredArgsConstructor
public class CustomTagValidator implements ConstraintValidator<UniqueTag, TagRequest> {

    private final TagRepository tagRepository;

    @Override
    public boolean isValid(TagRequest request, ConstraintValidatorContext context) {
        return tagRepository.findByNameIgnoreCase(request.name()).isEmpty();
    }
}

