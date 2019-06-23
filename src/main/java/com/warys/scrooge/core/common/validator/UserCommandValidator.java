package com.warys.scrooge.core.common.validator;

import com.warys.scrooge.command.account.UserCommand;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class UserCommandValidator extends Validator<UserCommand> {

    private UserCommandValidator(UserCommand validatable) {
        super(validatable);
    }

    public static UserCommandValidator of(UserCommand validatable) {
        return new UserCommandValidator(Objects.requireNonNull(validatable));
    }

    public UserCommand validateForUpdate() {
        return validate(UserCommand::getEmail, isValidEmailPredicate(), "email is not valid")
                .validate(UserCommand::getUsername, match("^[a-zA-Z0-9]{5,15}$"), "username is not valid")
                //.validate(UserCommand::getPassword, match("^[a-zA-Z0-9]{8,100}$"), "password is not valid")
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
