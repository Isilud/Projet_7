package com.nnk.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CurveNotFoundException extends IllegalArgumentException {

    public CurveNotFoundException(Integer id) {
        super("Invalid curve Id:" + id);
    }

    public CurveNotFoundException(String username) {
        // TODO Auto-generated constructor stub
    }
}
