package com.warys.scrooge.domain.common.validator;

import com.warys.scrooge.domain.model.user.User;
import com.warys.scrooge.domain.model.builder.UserBuilder;
import org.junit.Test;

import javax.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserValidatorShould {

    private static final String USER_ID = "userId";
    private static final String VALID_USERNAME = "validusername";
    private static final String VALID_EMAIL = "validemail@goo.com";

    @Test
    public void validate_user_command_when_all_is_valid() {
        User tom = new UserBuilder().with(
                o -> {
                    o.id = USER_ID;
                    o.username = VALID_USERNAME;
                    o.email = VALID_EMAIL;
                }
        ).buildCommand();

        final User userCommand = UserValidator.of(tom).validateForUpdate();

        assertThat(userCommand).isNotNull();
        assertThat(userCommand).isEqualTo(tom);
    }

    @Test(expected = ValidationException.class)
    public void throw_ValidationException_when_email_is_not_valid() {
        User tom = new UserBuilder().with(
                o -> {
                    o.id = USER_ID;
                    o.username = null;
                    o.email = "avalidemailgoo.com";
                }
        ).buildCommand();

        UserValidator.of(tom).validateForUpdate();
    }

    @Test(expected = ValidationException.class)
    public void throw_ValidationException_when_username_is_not_valid() {
        User tom = new UserBuilder().with(
                o -> {
                    o.id = USER_ID;
                    o.username = "bad";
                    o.email = VALID_EMAIL;
                }
        ).buildCommand();

        UserValidator.of(tom).validateForUpdate();
    }
}