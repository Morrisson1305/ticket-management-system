package com.we2stars.ticket_desk.exceptions;

import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Getter
public class InputFieldException extends RuntimeException{
    private final BindingResult bindingResult;
    private  final Map<String, String> mapError;

    public InputFieldException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
        this.mapError = bindingResult.getFieldErrors().stream().collect(collector);
    }

    Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
            fieldError -> fieldError.getField() + "Error",
            FieldError::getDefaultMessage
    );
}