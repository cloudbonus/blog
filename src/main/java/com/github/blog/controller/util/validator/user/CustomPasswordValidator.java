package com.github.blog.controller.util.validator.user;

import com.github.blog.controller.annotation.user.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.passay.AllowedRegexRule;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.MessageResolver;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author Raman Haurylau
 */
public class CustomPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    @SneakyThrows
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return true;
        } else {
            Properties props = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("passay.properties");
            props.load(inputStream);

            MessageResolver resolver = new PropertiesMessageResolver(props);

            PasswordValidator validator = new PasswordValidator(resolver, Arrays.asList(
                    new AllowedRegexRule("^[A-Za-z0-9!?@#$%^&*_-]*$"),
                    new LengthRule(8, 30),
                    new CharacterRule(EnglishCharacterData.UpperCase, 1),
                    new CharacterRule(EnglishCharacterData.LowerCase, 1),
                    new CharacterRule(EnglishCharacterData.Digit, 1),
                    new CharacterRule(EnglishCharacterData.Special, 1),
                    new WhitespaceRule(),
                    new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
                    new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false)
            ));

            RuleResult result = validator.validate(new PasswordData(password));

            if (result.isValid()) {
                return true;
            }

            List<String> messages = validator.getMessages(result);
            String messageTemplate = String.join(",", messages);
            context.buildConstraintViolationWithTemplate(messageTemplate)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            return false;
        }
    }
}
