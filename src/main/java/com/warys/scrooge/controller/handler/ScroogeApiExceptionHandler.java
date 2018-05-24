package com.warys.scrooge.controller.handler;

import com.warys.scrooge.command.error.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@RestController
public class ScroogeApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder errors = new StringBuilder();

        ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("[%s] : %s; ", error.getField(), error.getDefaultMessage()))
                .forEach(errors::append);
        ex.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(error -> String.format("[%s] : %s; ", error.getObjectName(), error.getDefaultMessage()))
                .forEach(errors::append);

        ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                errors.toString(),
                request.getDescription(false), ex.getClass().getSimpleName());

        return handleExceptionInternal(
                ex, errorResponse, headers, BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleApiException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false), ex.getClass().getSimpleName());
        return new ResponseEntity<>(errorResponse, resolveResponseStatus(ex));
    }

    private HttpStatus resolveResponseStatus(Exception exception) {
        ResponseStatus annotation = findMergedAnnotation(exception.getClass(), ResponseStatus.class);
        return (annotation != null) ? annotation.value() : INTERNAL_SERVER_ERROR;
    }
}
