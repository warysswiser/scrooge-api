package com.warys.scrooge.application.rest.secured;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warys.scrooge.domain.model.builder.UserBuilder;
import com.warys.scrooge.infrastructure.repository.mongo.entity.UserDocument;
import com.warys.scrooge.infrastructure.repository.mongo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

abstract class SecuredTest {

    static final String VALID_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRoZW50aWNhdGlvbiB0b2tlbiIsImV4cCI6MjUwOTU2MTYwOCwidXNlcklkIjoiVkFMSURfSUQifQ.YdMa2YHaB8ubhEWDEGCwWMr2qDhIxWREQxYkGG1LCfx7AroeAnLua1McYIcJ4L6OX398CnOR-15u7wpRs7WibQ";

    static final String EMAIL = "myemail@domain.com";
    static final String PASSWORD = "mypassword123";

    static final ObjectMapper om = new ObjectMapper();
    static final String USER_ID = "VALID_ID";
    static final String USERNAME = "myusername";

    @MockBean
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    void init() {
        UserDocument user = new UserBuilder().with(o -> {
            o.id = USER_ID;
            o.email = EMAIL;
            o.password = PASSWORD;
            o.username = USERNAME;
        }).build();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.findByEmailAndPassword(EMAIL, PASSWORD)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        when(userRepository.insert(any(UserDocument.class))).thenReturn(user);
        when(userRepository.save(any(UserDocument.class))).thenReturn(user);
    }
}
