package com.warys.scrooge.command.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse implements Serializable {
    private Date timestamp;
    private String message;
    private String path;
    private String exception;
}
