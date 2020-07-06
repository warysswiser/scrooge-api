package com.warys.scrooge.application.command.response;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
public class LoginResponse implements Serializable {
    private String token;
    private String id;
    private String username;
    private String email;
}
