package com.warys.scrooge.controller.secured;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warys.scrooge.core.model.builder.UserBuilder;
import com.warys.scrooge.core.model.user.User;
import com.warys.scrooge.core.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
public class UserControllerShould {

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRoZW50aWNhdGlvbiB0b2tlbiIsImV4cCI6MjUwOTU2MTYwOCwidXNlcklkIjoiVkFMSURfSUQifQ.YdMa2YHaB8ubhEWDEGCwWMr2qDhIxWREQxYkGG1LCfx7AroeAnLua1McYIcJ4L6OX398CnOR-15u7wpRs7WibQ";

    private static final String EMAIL = "my_email@domain";
    private static final String PASSWORD = "my_password";

    private static final ObjectMapper om = new ObjectMapper();
    private static final String USER_ID = "VALID_ID";
    private static final String USERNAME = "my_user_name";

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        User user = new UserBuilder().with(o -> {
            o.id = USER_ID;
            o.email = EMAIL;
            o.password = PASSWORD;
            o.username = USERNAME;
        }).build();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.findByEmailAndPassword(EMAIL, PASSWORD)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        when(userRepository.insert(any(User.class))).thenReturn(user);
    }

    @Test
    public void get_me() throws Exception {
        this.mockMvc.perform(
                get("/me")
                        .with(httpBasic(EMAIL, PASSWORD))
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }
}