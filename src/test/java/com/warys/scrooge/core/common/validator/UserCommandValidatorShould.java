package com.warys.scrooge.core.common.validator;

import com.warys.scrooge.command.account.UserCommand;
import com.warys.scrooge.core.model.builder.UserBuilder;
import org.junit.Test;

import javax.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserCommandValidatorShould {

    private static final String USER_ID = "userId";
    private static final String VALID_USERNAME = "validusername";
    private static final String VALID_EMAIL = "validemail@goo.com";

    @Test
    public void validate_user_command_when_all_is_valid() {
        UserCommand tom = new UserBuilder().with(
                o -> {
                    o.id = USER_ID;
                    o.username = VALID_USERNAME;
                    o.email = VALID_EMAIL;
                }
        ).buildCommand();

        final UserCommand userCommand = UserCommandValidator.of(tom).validateForUpdate();

        assertThat(userCommand).isNotNull();
        assertThat(userCommand).isEqualTo(tom);
    }

    @Test(expected = ValidationException.class)
    public void throw_ValidationException_when_email_is_not_valid() {
        UserCommand tom = new UserBuilder().with(
                o -> {
                    o.id = USER_ID;
                    o.username = null;
                    o.email = "avalidemailgoo.com";
                }
        ).buildCommand();

        UserCommandValidator.of(tom).validateForUpdate();
    }

    @Test(expected = ValidationException.class)
    public void throw_ValidationException_when_username_is_not_valid() {
        UserCommand tom = new UserBuilder().with(
                o -> {
                    o.id = USER_ID;
                    o.username = "bad";
                    o.email = VALID_EMAIL;
                }
        ).buildCommand();

        UserCommandValidator.of(tom).validateForUpdate();
    }
}