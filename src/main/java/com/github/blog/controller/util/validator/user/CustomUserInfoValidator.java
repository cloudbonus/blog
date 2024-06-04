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
        boolean isEducationInfoValid = userInfo.getUniversity() != null && userInfo.getMajor() != null;
        boolean isJobInfoValid = userInfo.getCompany() != null && userInfo.getJob() != null;
        return isEducationInfoValid || isJobInfoValid;
    }
}
