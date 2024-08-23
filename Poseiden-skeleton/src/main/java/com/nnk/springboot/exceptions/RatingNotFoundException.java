package com.nnk.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RatingNotFoundException extends IllegalArgumentException {

    public RatingNotFoundException(Integer id) {
        super("Invalid rating Id:" + id);
    }

    public RatingNotFoundException(String username) {
        // TODO Auto-generated constructor stub
    }
}
