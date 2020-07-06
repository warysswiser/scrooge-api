package com.warys.scrooge.application.command.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class RegisterRequest {

    @NotNull
    @Size(min = 5, max = 15, message = "username must be between 5 and 15 characters")
    private String username;
    @NotNull
    @Email(message = "Email should be valid")
    private String email;
    private String firstName;
    private String lastName;

}
