package com.warys.scrooge.core.common.validator;

import com.warys.scrooge.core.model.builder.UserBuilder;
import com.warys.scrooge.core.model.user.User;
import org.junit.Test;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidatorShould {

    @Test(expected = ValidationException.class)
    public void throw_ValidationException_when_username_is_not_valid() {
        User tom = new UserBuilder().with(
                o -> {
                    o.id = "userId";
                    o.username = null;
                    o.email = "email@goo.com";
                }
        ).build();

        Validator.of(tom).validate(User::getUsername, Objects::nonNull, "name cannot be null").get();
    }

    @Test(expected = ValidationException.class)
    public void throw_ValidationException_when_creation_date_is_not_valid() {
        User john = new UserBuilder().with(
                o -> {
                    o.id = "userId";
                    o.username = "John";
                    o.email = "email@goo.com";
                    o.creationDate = LocalDateTime.now().plusWeeks(2);
                }
        ).build();
        Validator
                .of(john)
                .validate(User::getUsername, Objects::nonNull, "name cannot be null")
                .validate(User::getCreationDate, creationDate -> creationDate.isBefore(LocalDateTime.now()), "user is underaged")
                .get();
    }

    @Test(expected = ValidationException.class)
    public void throw_ValidationException_when_creation_date_and_username_are_not_valid() {
        User john = new UserBuilder().with(
                o -> {
                    o.id = "userId";
                    o.username = null;
                    o.email = "email@goo.com";
                    o.creationDate = LocalDateTime.now().plusWeeks(2);
                }
        ).build();
        Validator
                .of(john)
                .validate(User::getUsername, Objects::nonNull, "name cannot be null")
                .validate(User::getCreationDate, creationDate -> creationDate.isBefore(LocalDateTime.now()), "user is underaged")
                .get();
    }

    @Test
    public void make_successful_validation() {
        User sarah = new UserBuilder().with(
                o -> {
                    o.id = "userId";
                    o.username = "Sarah";
                    o.email = "email@goo.com";
                    o.creationDate = LocalDateTime.now().minusWeeks(2);
                }
        ).build();
        final User validated = Validator
                .of(sarah)
                .validate(User::getUsername, Objects::nonNull, "name cannot be null")
                .validate(User::getCreationDate, creationDate -> creationDate.isBefore(LocalDateTime.now()), "user is underaged")
                .get();

        assertThat(validated).isNotNull();
        assertThat(validated).isEqualTo(sarah);

    }
}