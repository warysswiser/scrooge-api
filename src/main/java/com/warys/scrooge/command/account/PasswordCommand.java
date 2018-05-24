package com.warys.scrooge.command.account;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PasswordCommand {

    @NotNull
    @Size(min = 8, max = 100, message = "password must be between 8 and 100 characters")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
