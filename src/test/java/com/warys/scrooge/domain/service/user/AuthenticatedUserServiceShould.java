package com.warys.scrooge.domain.service.user;

import com.warys.scrooge.application.command.request.UpdatePassword;
import com.warys.scrooge.domain.exception.ElementNotFoundException;
import com.warys.scrooge.domain.model.builder.UserBuilder;
import com.warys.scrooge.domain.model.user.Session;
import com.warys.scrooge.domain.model.user.User;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.repository.mongo.UserRepository;
import com.warys.scrooge.infrastructure.repository.mongo.entity.UserDocument;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthenticatedUserServiceShould {

    private final String USER_ID = "USER_ID";
    private final UserRepository userRepository = mock(UserRepository.class);
    private final Session user = mock(Session.class);

    private final UserService tested = new AuthenticatedUserService(userRepository);

    @Test
    void throw_ElementNotFoundException_when_invalid_user_is_given() {
        final UserDocument expected = new UserBuilder().with(o -> {
            o.id = USER_ID;
            o.username = "user name";
            o.email = "anemail@domain.com";
        }).build();

        when(user.getId()).thenReturn(null);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(expected));
        assertThatExceptionOfType(ElementNotFoundException.class).isThrownBy(() -> tested.getMe(user));
    }

    @Test
    void get_me() throws ApiException {
        final UserDocument expected = new UserBuilder().with(o -> {
            o.id = USER_ID;
            o.username = "user name";
            o.email = "anemail@domain.com";
        }).build();

        when(user.getId()).thenReturn(USER_ID);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(expected));

        final User userCommand = tested.getMe(user);

        assertThat(userCommand).isEqualToComparingFieldByField(expected);
    }

    @Test
    void throws_UnsupportedOperationException_on_update() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> tested.update(user, "anId", null));
    }

    @Test
    void throws_UnsupportedOperationException_on_partial_update() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> tested.partialUpdate(user, "anId", null));
    }

    @Test
    void throws_UnsupportedOperationException_on_create() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> tested.create(user, new User()));
    }

    @Test
    void throws_UnsupportedOperationException_on_retrieve() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> tested.retrieve(user, "anId"));
    }

    @Test
    void throws_UnsupportedOperationException_on_get_all() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> tested.getAll(user));
    }

    @Test
    void throws_UnsupportedOperationException_on_remove() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> tested.remove(user, "anId"));
    }


    @Test
    void throws_NullPointerException_when_null_user_is_set_on_update() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> tested.updateUser(user, null));
    }

    @Test
    void throws_NullPointerException_when_null_user_is_set_on_partial_update() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> tested.partialUpdateUser(user, null));
    }

    @Test
    void throws_NullPointerException_when_null_password_is_set_on_password_update() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() ->
                        tested.updatePassword(user, null));
    }

    @Test
    void throws_UnsupportedOperationException_on_get_user_by_credentials() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> tested.getUserByCredentials("email", "password"));
    }

    @Test
    void throws_UnsupportedOperationException_on_check_user_email() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> tested.checkUserEmail(null));
    }

    @Test
    void make_a_partial_update_of_user() throws ApiException {
        final UserDocument oldUser = new UserBuilder()
                .with(o -> {
                    o.id = USER_ID;
                    o.email = "anemail@domain.com";
                    o.username = "oldUserName";
                    o.accountNonExpired = true;
                    o.accountNonLocked = true;
                    o.credentialsNonExpired = true;
                    o.enabled = true;
                })
                .build();

        final User newUserCommand = new UserBuilder()
                .with(o -> {
                    o.id = USER_ID;
                    o.email = "anemail@domain.com";
                    o.username = "newUserName";
                    o.accountNonExpired = true;
                    o.accountNonLocked = true;
                    o.credentialsNonExpired = true;
                    o.enabled = true;
                })
                .buildCommand();

        final UserDocument expected = new UserBuilder()
                .with(o -> {
                    o.id = USER_ID;
                    o.email = "anemail@domain.com";
                    o.username = "newUserName";
                    o.accountNonExpired = true;
                    o.accountNonLocked = true;
                    o.credentialsNonExpired = true;
                    o.enabled = true;
                })
                .build();

        when(user.getId()).thenReturn(USER_ID);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(oldUser));
        when(userRepository.save(any(UserDocument.class))).thenReturn(expected);

        tested.partialUpdateUser(user, newUserCommand);

        ArgumentCaptor<UserDocument> argumentCaptor = ArgumentCaptor.forClass(UserDocument.class);
        verify(userRepository).save(argumentCaptor.capture());
        UserDocument capturedArgument = argumentCaptor.getValue();

        assertThat(capturedArgument).isEqualToComparingFieldByField(expected);
    }

    @Test
    void make_an_update_of_user() throws ApiException {
        final UserDocument oldUser = new UserBuilder()
                .with(o -> {
                    o.id = USER_ID;
                    o.email = "anemail@domain.com";
                    o.username = "oldUserName";
                })
                .build();

        final User newUserCommand = new UserBuilder()
                .with(o -> {
                    o.id = USER_ID;
                    o.email = "anemail@domain.com";
                    o.username = "newUserName";
                    o.accountNonExpired = true;
                    o.accountNonLocked = true;
                    o.credentialsNonExpired = true;
                    o.enabled = true;
                })
                .buildCommand();

        final UserDocument expected = new UserBuilder()
                .with(o -> {
                    o.id = USER_ID;
                    o.email = "anemail@domain.com";
                    o.username = "newUserName";
                    o.accountNonExpired = true;
                    o.accountNonLocked = true;
                    o.credentialsNonExpired = true;
                    o.enabled = true;
                })
                .build();

        when(user.getId()).thenReturn(USER_ID);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(oldUser));
        when(userRepository.save(any(UserDocument.class))).thenReturn(expected);

        tested.updateUser(user, newUserCommand);

        ArgumentCaptor<UserDocument> argumentCaptor = ArgumentCaptor.forClass(UserDocument.class);
        verify(userRepository).save(argumentCaptor.capture());
        UserDocument capturedArgument = argumentCaptor.getValue();

        assertThat(capturedArgument).isEqualToComparingFieldByField(expected);
    }

    @Test
    void update_password() throws ApiException {
        final UpdatePassword command = new UpdatePassword();
        command.setPassword("aPassWord1234@domain.com");

        final UserDocument oldUser = new UserBuilder()
                .with(o -> {
                    o.id = USER_ID;
                    o.email = "anemail@domain.com";
                    o.username = "oldUserName";
                    o.password = "oldPassword";
                })
                .build();

        when(user.getId()).thenReturn(USER_ID);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(oldUser));

        tested.updatePassword(user, command);

        ArgumentCaptor<UserDocument> argumentCaptor = ArgumentCaptor.forClass(UserDocument.class);
        verify(userRepository).save(argumentCaptor.capture());
        UserDocument capturedArgument = argumentCaptor.getValue();

        assertThat(capturedArgument.getPassword()).isEqualTo(command.getPassword());
    }
}