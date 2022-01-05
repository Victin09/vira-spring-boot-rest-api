package es.vira.domain.validator;

import es.vira.domain.model.User;

public interface UserValidator {
    void validate(User user);

    void validatePassword(String password);
}
