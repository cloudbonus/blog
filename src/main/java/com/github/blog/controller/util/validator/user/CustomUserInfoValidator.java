package com.github.blog.controller.util.validator.user;

import com.github.blog.controller.annotation.user.ValidUserInfo;
import com.github.blog.controller.dto.request.UserInfoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * @author Raman Haurylau
 */
public class CustomUserInfoValidator implements ConstraintValidator<ValidUserInfo, UserInfoRequest> {

    @Override
    public boolean isValid(UserInfoRequest object, ConstraintValidatorContext context) {
        boolean isEducationValid = object.getUniversityName() != null && object.getMajorName() != null && compareToRegex(object.getUniversityName()) && compareToRegex(object.getMajorName());
        boolean isJobValid = object.getCompanyName() != null && object.getJobTitle() != null && compareToRegex(object.getCompanyName()) && compareToRegex(object.getJobTitle());

        if (isEducationValid && isJobValid) {
            return true;
        } else if (isEducationValid && object.getCompanyName() == null && object.getJobTitle() == null) {
            return true;
        } else return object.getUniversityName() == null && object.getMajorName() == null && isJobValid;
    }

    private boolean compareToRegex(String text) {
        return Pattern.compile("^[A-Za-z][a-zA-Z .'-]{6,30}$")
                .matcher(text)
                .matches();
    }
}
