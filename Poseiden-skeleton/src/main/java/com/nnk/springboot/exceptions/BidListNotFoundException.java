package com.nnk.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BidListNotFoundException extends IllegalArgumentException {

    public BidListNotFoundException(Integer id) {
        super("Invalid bidList Id:" + id);
    }

    public BidListNotFoundException(String username) {
        // TODO Auto-generated constructor stub
    }
}
