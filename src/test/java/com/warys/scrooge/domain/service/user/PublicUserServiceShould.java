package com.warys.scrooge.domain.service.user;

import com.warys.scrooge.domain.exception.DuplicatedInformationException;
import com.warys.scrooge.domain.exception.InconsistentElementException;
import com.warys.scrooge.domain.exception.auth.InvalidCredentialsException;
import com.warys.scrooge.domain.model.builder.UserBuilder;
import com.warys.scrooge.domain.model.user.Session;
import com.warys.scrooge.domain.model.user.User;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.repository.mongo.UserRepository;
import com.warys.scrooge.infrastructure.repository.mongo.entity.UserDocument;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicUserServiceShould {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final Session user = mock(Session.class);

    private final UserService tested = new PublicUserService(userRepository);

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
    void throws_UnsupportedOperationException_on_update_user() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> tested.updateUser(user, null));
    }

    @Test
    void throws_UnsupportedOperationException_on_partial_update_user() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> tested.partialUpdateUser(user, null));
    }

    @Test
    void throws_UnsupportedOperationException_on_password_update() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> tested.updatePassword(user, null));
    }

    @Test
    void throws_UnsupportedOperationException_on_get_me() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> tested.getMe(user));
    }

    @Test
    void throws_InvalidCredentialsException_on_get_with_invalid_credentials() {
        assertThatExceptionOfType(InvalidCredentialsException.class)
                .isThrownBy(() ->
                        tested.getUserByCredentials("invalid_email", "invalid_password"));
    }

    @Test
    void get_user_with_valid_credentials() throws ApiException {
        final UserDocument expected = new UserBuilder()
                .with(o -> o.id = "anId").build();
        when(userRepository.findByEmailAndPassword("email", "password")).thenReturn(Optional.of(expected));
        final User userByCredentials = tested.getUserByCredentials("email", "password");

        assertThat(userByCredentials).isNotNull();
    }

    @Test
    void should_throw_DuplicatedInformationException_when_known_email_is_set_for_check() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserDocument()));
        assertThatExceptionOfType(DuplicatedInformationException.class)
                .isThrownBy(() ->
                        tested.checkUserEmail("email"));
    }

    @Test
    void should_throw_InconsistentElementException_when_is_null_for_check() {
        assertThatExceptionOfType(InconsistentElementException.class)
                .isThrownBy(() ->
                        tested.checkUserEmail(null));
    }

    @Test
    void should_not_throw_DuplicatedInformationException_when_known_email_is_set_for_check() throws ApiException {
        tested.checkUserEmail("email");
    }
}