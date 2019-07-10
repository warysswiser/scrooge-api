package com.warys.scrooge.core.service.user;

import com.warys.scrooge.command.account.UserCommand;
import com.warys.scrooge.core.model.builder.UserBuilder;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.model.user.User;
import com.warys.scrooge.core.repository.UserRepository;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.exception.business.DuplicatedInformationException;
import com.warys.scrooge.infrastructure.exception.business.auth.InvalidCredentialsException;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PublicUserServiceShould {

    private final String USER_ID = "USER_ID";
    private final UserRepository userRepository = mock(UserRepository.class);
    private final SessionUser user = mock(SessionUser.class);

    private final UserService tested = new PublicUserService(userRepository);

    @Test(expected = UnsupportedOperationException.class)
    public void throws_UnsupportedOperationException_on_update() {
        tested.update(user, "anId", null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throws_UnsupportedOperationException_on_partial_update() {
        tested.partialUpdate(user, "anId", null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throws_UnsupportedOperationException_on_create() {
        tested.create(user, new UserCommand());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throws_UnsupportedOperationException_on_retrieve() {
        tested.retrieve(user, "anId");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throws_UnsupportedOperationException_on_get_all() {
        tested.getAll(user);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throws_UnsupportedOperationException_on_remove() {
        tested.remove(user, "anId");
    }


    @Test(expected = UnsupportedOperationException.class)
    public void throws_UnsupportedOperationException_on_update_user() throws ApiException {
        tested.updateUser(user, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throws_UnsupportedOperationException_on_partial_update_user() throws ApiException {
        tested.partialUpdateUser(user, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throws_UnsupportedOperationException_on_password_update() throws ApiException {
        tested.updatePassword(user, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throws_UnsupportedOperationException_on_get_me() throws ApiException {
        tested.getMe(user);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void throws_InvalidCredentialsException_on_get_with_invalid_credentials() throws ApiException {
        tested.getUserByCredentials("invalid_email", "invalid_password");
    }

    @Test
    public void get_user_with_valid_credentials() throws ApiException {
        final User expected = new UserBuilder()
                .with(o -> o.id = "anId").build();
        when(userRepository.findByEmailAndPassword("email", "password")).thenReturn(Optional.of(expected));
        final UserCommand userByCredentials = tested.getUserByCredentials("email", "password");

        assertThat(userByCredentials).isNotNull();
    }

    @Test(expected = DuplicatedInformationException.class)
    public void should_throw_DuplicatedInformationException_when_known_email_is_set_for_check() throws ApiException {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        tested.checkUserEmail("email");
    }

    @Test
    public void should_not_throw_DuplicatedInformationException_when_known_email_is_set_for_check() throws ApiException {
        tested.checkUserEmail("email");
    }
}