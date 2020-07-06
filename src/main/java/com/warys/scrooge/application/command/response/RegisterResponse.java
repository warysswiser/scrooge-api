package com.warys.scrooge.application.command.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterResponse {

    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private LocalDateTime deletionDate;
}
