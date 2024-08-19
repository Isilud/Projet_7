package com.nnk.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends IllegalArgumentException {

    public UserNotFoundException(Integer id) {
        super("Invalid user Id:" + id);
    }

    public UserNotFoundException(String username) {
        // TODO Auto-generated constructor stub
    }
}
