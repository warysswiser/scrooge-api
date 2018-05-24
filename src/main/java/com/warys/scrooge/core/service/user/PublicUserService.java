package com.warys.scrooge.core.service.user;

import com.warys.scrooge.command.account.UserCommand;
import com.warys.scrooge.core.common.mapper.BeanMapper;
import com.warys.scrooge.core.common.mapper.UserMapper;
import com.warys.scrooge.core.model.user.User;
import com.warys.scrooge.core.repository.UserRepository;
import com.warys.scrooge.infrastructure.exception.business.DuplicatedInformationException;
import com.warys.scrooge.infrastructure.exception.business.auth.InvalidCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class PublicUserService implements UserService {

    private final UserRepository userRepository;
    private final BeanMapper<User, UserCommand> mapper;

    public PublicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        mapper = new UserMapper();
    }

    @Override
    public UserCommand getUserByCredentials(final String email, final String password) throws InvalidCredentialsException {
        return userRepository.findByEmailAndPassword(email, password)
                .map(mapper.mapToInput())
                .orElseThrow(() -> new InvalidCredentialsException("Unknown user for given credentials"));
    }

    @Override
    public void checkUserEmail(final String email) throws DuplicatedInformationException {
        if (userRepository.findByEmail(email).isPresent())
            throw new DuplicatedInformationException("User already exists for email : " + email);
    }
}
