package com.warys.scrooge.core.service.user;

import com.warys.scrooge.command.account.PasswordCommand;
import com.warys.scrooge.command.account.UserCommand;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.service.MyCrudService;
import com.warys.scrooge.infrastructure.exception.ApiException;

import java.util.List;

public interface UserService extends MyCrudService<UserCommand, UserCommand> {

    default UserCommand updateUser(SessionUser user, UserCommand newUser) throws ApiException {
        throw new UnsupportedOperationException();
    }

    default UserCommand partialUpdateUser(SessionUser user, UserCommand partialNewUser) throws ApiException {
        throw new UnsupportedOperationException();
    }

    default UserCommand getUserByCredentials(String email, String password) throws ApiException {
        throw new UnsupportedOperationException();
    }

    default void checkUserEmail(String email) throws ApiException {
        throw new UnsupportedOperationException();
    }

    @Override
    default UserCommand retrieve(SessionUser me, String itemId) {
        throw new UnsupportedOperationException();
    }

    @Override
    default UserCommand create(SessionUser me, UserCommand payload) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void remove(SessionUser me, String itemId) {
        throw new UnsupportedOperationException();
    }

    @Override
    default UserCommand update(SessionUser me, String itemId, UserCommand payload) {
        throw new UnsupportedOperationException();
    }

    @Override
    default UserCommand partialUpdate(SessionUser me, String itemId, UserCommand partialPayload) {
        throw new UnsupportedOperationException();
    }

    @Override
    default List<UserCommand> getAll(SessionUser me) {
        throw new UnsupportedOperationException();
    }

    default void updatePassword(SessionUser me, PasswordCommand newPassword) throws ApiException {
        throw new UnsupportedOperationException();
    }

    default UserCommand getMe(SessionUser user) throws ApiException {
        throw new UnsupportedOperationException();
    }
}
