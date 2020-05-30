package com.warys.scrooge.application.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warys.scrooge.application.command.request.LoginRequest;
import com.warys.scrooge.domain.model.user.User;
import com.warys.scrooge.domain.model.builder.UserBuilder;
import com.warys.scrooge.infrastructure.repository.mongo.entity.UserDocument;
import com.warys.scrooge.infrastructure.repository.mongo.UserRepository;
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

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
public class PublicUsersControllerShould {

    private static final String EMAIL = "my_email@domain.com";
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
    }

    @Test
    public void perform_login() throws Exception {
        this.mockMvc.perform(
                post("/public/login")
                        .content(om.writeValueAsString(new LoginRequest(EMAIL, PASSWORD)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(USER_ID)))
                .andExpect(jsonPath("$.email", is(EMAIL)))
                .andExpect(jsonPath("$.username", is(USERNAME)))
                .andExpect(jsonPath("$.token", notNullValue()));
    }


    @Test
    public void return_conflict_response_when_perform_register_with_know_email() throws Exception {
        User request = new UserBuilder().with(o -> {
            o.id = USER_ID;
            o.email = EMAIL;
            o.password = PASSWORD;
            o.username = USERNAME;
        }).buildCommand();

        this.mockMvc.perform(
                post("/public/register")
                        .content(om.writeValueAsString(request))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("UserDocument already exists for email : " + EMAIL)))
                .andExpect(jsonPath("$.exception", is("DuplicatedInformationException")));
    }


    @Test
    public void not_perform_register_with_null_email() throws Exception {
        User request = new UserBuilder().with(o -> {
            o.password = PASSWORD;
            o.username = USERNAME;
        }).buildCommand();

        this.mockMvc.perform(
                post("/public/register")
                        .content(om.writeValueAsString(request))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", is("[email] : must not be null; ")))
                .andExpect(jsonPath("$.exception", is("InconsistentElementException")))
                .andExpect(jsonPath("$.path", is("uri=/public/register")));
    }

    @Test
    public void perform_register() throws Exception {

        final String newEmail = "new_email@domain.com";

        UserDocument user = new UserBuilder().with(o -> {
            o.id = USER_ID;
            o.email = newEmail;
            o.password = PASSWORD;
            o.username = USERNAME;
        }).build();

        when(userRepository.insert(any(UserDocument.class))).thenReturn(user);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(newEmail)).thenReturn(Optional.empty());

        User request = new UserBuilder().with(o -> {
            o.email = newEmail;
            o.password = PASSWORD;
            o.username = USERNAME;
        }).buildCommand();

        this.mockMvc.perform(
                post("/public/register")
                        .content(om.writeValueAsString(request))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(USER_ID)))
                .andExpect(jsonPath("$.email", is(newEmail)));
    }
}