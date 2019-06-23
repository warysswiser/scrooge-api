package com.warys.scrooge.core.service.user;

import com.warys.scrooge.command.account.PasswordCommand;
import com.warys.scrooge.command.account.UserCommand;
import com.warys.scrooge.core.common.mapper.BeanMapper;
import com.warys.scrooge.core.common.mapper.UserMapper;
import com.warys.scrooge.core.common.util.BeanUtil;
import com.warys.scrooge.core.common.validator.UserCommandValidator;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.model.user.User;
import com.warys.scrooge.core.repository.UserRepository;
import com.warys.scrooge.infrastructure.exception.business.ElementNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthenticatedUserService implements UserService {

    private final UserRepository userRepository;
    private final BeanMapper<User, UserCommand> mapper;

    public AuthenticatedUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.mapper = new UserMapper();
    }

    @Override
    public UserCommand getMe(SessionUser me) throws ElementNotFoundException {
        return userRepository
                .findById(me.getId())
                .map(mapper.mapToInput())
                .orElseThrow(() -> new ElementNotFoundException("Can not find user"));
    }

    @Override
    public UserCommand updateUser(SessionUser user, UserCommand newUser) throws ElementNotFoundException {
        Objects.requireNonNull(newUser, "Null user set");

        final User userToUpdate = getUserById(user);

        UserCommandValidator.of(newUser).validateForUpdate();

        BeanUtil.copyBean(userToUpdate, newUser);

        return Optional.of(userRepository.save(userToUpdate)).map(mapper.mapToInput()).orElseThrow();
    }

    @Override
    public UserCommand partialUpdateUser(SessionUser user, UserCommand partialItem) throws ElementNotFoundException {
        Objects.requireNonNull(partialItem, "Null user set");

        UserCommandValidator.of(partialItem).validateForUpdate();

        final User userToUpdate = getUserById(user);
        BeanUtil.copyBean(userToUpdate, partialItem);

        return Optional.of(userRepository.save(userToUpdate)).map(mapper.mapToInput()).orElseThrow();
    }

    @Override
    public void updatePassword(SessionUser me, PasswordCommand newPassword) throws ElementNotFoundException {
        Objects.requireNonNull(newPassword, "Null password set");

        final User userToUpdate = getUserById(me);

        userToUpdate.setPassword(newPassword.getPassword());

        userRepository.save(userToUpdate);
    }

    private User getUserById(SessionUser user) throws ElementNotFoundException {
        return userRepository
                .findById(user.getId())
                .orElseThrow(() -> new ElementNotFoundException("Can not find user"));
    }

}
