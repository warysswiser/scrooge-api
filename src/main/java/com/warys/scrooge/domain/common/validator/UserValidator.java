package com.warys.scrooge.domain.common.validator;

import com.warys.scrooge.domain.model.user.User;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class UserValidator extends Validator<User> {

    private UserValidator(User validatable) {
        super(validatable);
    }

    public static UserValidator of(User validatable) {
        return new UserValidator(Objects.requireNonNull(validatable));
    }

    public User validateForUpdate() {
        return validate(User::getEmail, isValidEmailPredicate(), "email is not valid")
                .validate(User::getUsername, match("^[a-zA-Z0-9]{5,15}$"), "username is not valid")
                //.validate(User::getPassword, match("^[a-zA-Z0-9]{8,100}$"), "password is not valid")
                .get();
    }

    private Predicate<String> isValidEmailPredicate() {
        return email -> {
            try {
                new InternetAddress(email).validate();
                return true;
            } catch (AddressException e) {
                return false;
            }
        };
    }

    private Predicate<String> match(String regex) {
        return value -> Pattern.compile(regex).matcher(value).matches();
    }
}
