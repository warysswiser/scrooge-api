package com.warys.scrooge.core.service.user;

import com.warys.scrooge.command.account.PasswordCommand;
import com.warys.scrooge.command.account.UserCommand;
import com.warys.scrooge.core.common.mapper.BeanMapper;
import com.warys.scrooge.core.common.mapper.UserMapper;
import com.warys.scrooge.core.common.util.BeanUtil;
import com.warys.scrooge.core.common.validator.UserCommandValidator;
import com.warys.scrooge.core.common.validator.Validator;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.model.user.User;
import com.warys.scrooge.core.repository.UserRepository;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.exception.business.ElementNotFoundException;
import com.warys.scrooge.infrastructure.exception.business.InconsistentElementException;
import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
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
        UserCommandValidator.of(newUser).validateForUpdate();

        final User userToUpdate = getUserById(user);
        BeanUtil.copyBean(newUser, userToUpdate);

        return Optional.of(userRepository.save(userToUpdate)).map(mapper.mapToInput()).orElseThrow();
    }

    @Override
    public UserCommand partialUpdateUser(SessionUser user, UserCommand partialItem) throws ElementNotFoundException {
        Objects.requireNonNull(partialItem, "Null user set");

        final User userToUpdate = getUserById(user);
        BeanUtil.copyBean(partialItem, userToUpdate);

        return Optional.of(userRepository.save(userToUpdate)).map(mapper.mapToInput()).orElseThrow();
    }

    @Override
    public void updatePassword(SessionUser me, PasswordCommand newPassword) throws ApiException {
        Objects.requireNonNull(newPassword, "Null password set");
        try {
            new InternetAddress(newPassword.getPassword()).validate();
        } catch (AddressException e) {
            throw new InconsistentElementException(e.getMessage());
        }
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
