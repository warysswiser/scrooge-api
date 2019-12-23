package com.warys.scrooge.command.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints .Size;


@AllArgsConstructor
@Getter
@Setter
public class LoginCommand {
    @NotNull
    @Email
    private String email;
    @Size(min = 8, max = 100, message = "password must be between 8 and 100 characters")
    private String password;
}
