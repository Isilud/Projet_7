package com.nnk.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RuleNotFoundException extends IllegalArgumentException {

    public RuleNotFoundException(Integer id) {
        super("Invalid ruleName Id:" + id);
    }

    public RuleNotFoundException(String username) {
        // TODO Auto-generated constructor stub
    }
}
