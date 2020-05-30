package com.warys.scrooge.domain.common.mapper;

import com.warys.scrooge.domain.model.user.User;
import com.warys.scrooge.infrastructure.repository.mongo.entity.UserDocument;

import java.util.function.Function;

public class UserMapper implements BeanMapper<UserDocument, User> {

    @Override
    public Function<User, UserDocument> mapToOutput() {
        return userCommand -> {
            final var user = new UserDocument();
            map(userCommand, user);
            return user;
        };
    }

    @Override
    public Function<UserDocument, User> mapToInput() {
        return user -> {
            final var userCommand = new User();
            map(user, userCommand);
            return userCommand;
        };
    }
}
