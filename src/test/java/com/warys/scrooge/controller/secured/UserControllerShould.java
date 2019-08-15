package com.warys.scrooge.controller.secured;

import com.warys.scrooge.command.account.PasswordCommand;
import com.warys.scrooge.command.account.UserCommand;
import com.warys.scrooge.core.model.builder.UserBuilder;
import com.warys.scrooge.core.model.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
public class UserControllerShould extends SecuredTest {

    @Before
    public void setUp() {
        init();
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
        when(userRepository.save(any(User.class))).thenReturn(user);
    }

    @Test
    public void get_me() throws Exception {
        this.mockMvc.perform(
                get("/me")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void return_Unauthorized_when_invalid_token_is_given() throws Exception {
        this.mockMvc.perform(
                get("/me")
                        .header("Authorization", "Bearer " + "BAD" + VALID_TOKEN + "SOO BAD")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    public void update_me() throws Exception {
        UserCommand request = new UserBuilder().with(o -> {
            o.id = USER_ID;
            o.email = EMAIL;
            o.password = PASSWORD;
            o.username = "newname";
        }).buildCommand();

        this.mockMvc.perform(
                put("/me")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .content(om.writeValueAsString(request))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("newname")));
    }

    @Test
    public void partial_update_me() throws Exception {
        UserCommand request = new UserBuilder().with(o -> {
            o.username = "newnewname";
        }).buildCommand();

        this.mockMvc.perform(
                patch("/me")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .content(om.writeValueAsString(request))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(USER_ID)))
                .andExpect(jsonPath("$.username", is("newnewname")))
                .andExpect(jsonPath("$.email", is(EMAIL)));
    }

    @Test
    public void update_password() throws Exception {
        PasswordCommand request = new PasswordCommand("newpassword@gmail.com");

        this.mockMvc.perform(
                put("/me/password")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .content(om.writeValueAsString(request))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void not_update_password_when_invalid_email() throws Exception {
        PasswordCommand request = new PasswordCommand("email");

        this.mockMvc.perform(
                put("/me/password")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .content(om.writeValueAsString(request))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.exception", is("InconsistentElementException")))
                .andExpect(jsonPath("$.path", is("uri=/me/password")));
    }
}