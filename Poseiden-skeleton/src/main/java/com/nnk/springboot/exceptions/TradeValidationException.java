package com.nnk.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TradeValidationException extends RuntimeException {

    private final String path;
    private final BindingResult errors;

    public TradeValidationException(String path, BindingResult errors) {
        this.path = path;
        this.errors = errors;
    }

    public String getPath() {
        return path;
    }

    public BindingResult getErrors() {
        return errors;
    }
}
