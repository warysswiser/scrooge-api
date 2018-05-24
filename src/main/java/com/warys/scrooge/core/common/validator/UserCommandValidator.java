package com.warys.scrooge.core.common.validator;

import com.warys.scrooge.command.account.UserCommand;
import org.jetbrains.annotations.NotNull;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class UserCommandValidator {

    public void validateForUpdate(UserCommand partialItem) {
        Validator.of(partialItem)
                .validate(UserCommand::getEmail, isValidEmailPredicate(), "name is null")
                .validate(UserCommand::getUsername, match("^[a-zA-Z0-9]{5,15}$"), "name is empty")
                .validate(UserCommand::getPassword, match("^[a-zA-Z0-9]{8,100}$"), "name is empty");
    }

    @NotNull
    private Predicate<String> isValidEmailPredicate() {
        return email -> {
            try {
                new InternetAddress(email).validate();
                return false;
            } catch (AddressException e) {
                return false;
            }
        };
    }

    private Predicate<String> match(String regex) {
        return value -> Pattern.compile(regex).matcher(value).matches();
    }
}
