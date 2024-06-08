package com.github.blog.controller.util.validator.user;

import com.github.blog.controller.annotation.user.ValidUserInfo;
import com.github.blog.controller.dto.request.UserInfoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author Raman Haurylau
 */
public class CustomUserInfoValidator implements ConstraintValidator<ValidUserInfo, UserInfoRequest> {

    @Override
    public boolean isValid(UserInfoRequest userInfo, ConstraintValidatorContext context) {
        boolean isEducationInfoValid = userInfo.university() != null && userInfo.major() != null;
        boolean isJobInfoValid = userInfo.company() != null && userInfo.job() != null;
        return isEducationInfoValid || isJobInfoValid;
    }
}
