package com.warys.scrooge.command.account;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    private String id;
    private String token;
    private String username;
    private String email;

    public LoginResponse(String token, String id, String username, String email) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
