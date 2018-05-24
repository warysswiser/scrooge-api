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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticatedUserService implements UserService {

    private final UserRepository userRepository;
    private final UserCommandValidator validator;
    private final BeanMapper<User, UserCommand> mapper;

    public AuthenticatedUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.validator = new UserCommandValidator();
        this.mapper = new UserMapper();
    }

    @Override
    public UserCommand getMe(SessionUser me) {
        return userRepository.findById(me.getId()).map(mapper.mapToInput()).get();
    }

    @Override
    public UserCommand updateUser(SessionUser user, UserCommand newUser) {
        validator.validateForUpdate(newUser);
        final User userToUpdate = userRepository.findById(user.getId()).get();
        BeanUtil.copyBean(userToUpdate, newUser);
        return Optional.of(userRepository.save(userToUpdate)).map(mapper.mapToInput()).get();
    }

    @Override
    public UserCommand partialUpdateUser(SessionUser user, UserCommand partialItem) {
        validator.validateForUpdate(partialItem);
        final User userToUpdate = userRepository.findById(user.getId()).get();
        BeanUtil.copyBean(userToUpdate, partialItem);

        return Optional.of(userRepository.save(userToUpdate)).map(mapper.mapToInput()).get();
    }

    @Override
    public void updatePassword(SessionUser me, PasswordCommand newPassword) {
        final User userToUpdate = userRepository.findById(me.getId()).get();
        userToUpdate.setPassword(newPassword.getPassword());
        userRepository.save(userToUpdate);
    }

}
