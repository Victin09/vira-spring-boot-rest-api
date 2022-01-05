package es.vira.domain.validator.impl;

import es.vira.domain.model.User;
import es.vira.domain.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

@Component
public class UserValidatorImpl implements UserValidator {

    public static final String USERNAME_AND_PASSWORD_REGEX = "[a-zA-Z0-9]+";
    public final int minPasswordLength;
    public final int maxPasswordLength;
    private final Validator validator;

    @Autowired
    public UserValidatorImpl(@Value("${application.options.minPasswordLength}") int minPasswordLength,
                             @Value("${application.options.maxPasswordLength}") int maxPasswordLength,
                             Validator validator) {
        this.minPasswordLength = minPasswordLength;
        this.maxPasswordLength = maxPasswordLength;
        this.validator = validator;
    }

    @Override
    public void validate(User user) {
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        Optional<ConstraintViolation<User>> constraintViolationOpt = constraintViolations.stream().findFirst();
        if (constraintViolationOpt.isPresent()) {
            ConstraintViolation<User> constraintViolation = constraintViolationOpt.get();
            String msg = String.format("%s field %s", constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password field can't be null or blank");
        }

        int passwordLength = password.length();
        if (passwordLength < minPasswordLength || passwordLength > maxPasswordLength) {
            throw new IllegalArgumentException(String.format("password field must be greater than %d and less than %d",
                    minPasswordLength, maxPasswordLength));
        }

        if (!password.matches(USERNAME_AND_PASSWORD_REGEX)) {
            throw new IllegalArgumentException(String.format("password field must match %s", USERNAME_AND_PASSWORD_REGEX));
        }
    }
}
