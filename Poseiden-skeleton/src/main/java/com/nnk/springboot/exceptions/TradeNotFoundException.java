package com.nnk.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TradeNotFoundException extends IllegalArgumentException {

    public TradeNotFoundException(Integer id) {
        super("Invalid trade Id:" + id);
    }

    public TradeNotFoundException(String username) {
        // TODO Auto-generated constructor stub
    }
}
