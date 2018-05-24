package com.warys.scrooge.core.common.mapper;

import com.warys.scrooge.command.account.UserCommand;
import com.warys.scrooge.core.common.util.BeanUtil;
import com.warys.scrooge.core.model.user.User;

import java.util.function.Function;

public class UserMapper implements BeanMapper<User, UserCommand> {

    @Override
    public Function<UserCommand, User> mapToOutput() {
        return userCommand -> {
            final var user = new User();
            BeanUtil.copyBean(userCommand, user);
            return user;
        };
    }

    @Override
    public Function<User, UserCommand> mapToInput() {
        return user -> {
            final var userCommand = new UserCommand();
            BeanUtil.copyBean(user, userCommand);
            return userCommand;
        };
    }
}
