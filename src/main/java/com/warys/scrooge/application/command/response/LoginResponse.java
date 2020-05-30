package com.warys.scrooge.application.command.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class LoginResponse implements Serializable {
    private String token;
    private String id;
    private String username;
    private String email;
}
