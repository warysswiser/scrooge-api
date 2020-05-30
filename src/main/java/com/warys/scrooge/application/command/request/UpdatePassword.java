package com.warys.scrooge.application.command.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdatePassword {
    @NotNull
    @Size(min = 8, max = 100, message = "password must be between 8 and 100 characters")
    private String password;
}
