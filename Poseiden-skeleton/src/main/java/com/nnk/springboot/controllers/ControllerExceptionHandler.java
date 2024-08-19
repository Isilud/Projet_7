package com.nnk.springboot.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.exceptions.BidListValidationException;
import com.nnk.springboot.exceptions.CurveNotFoundException;
import com.nnk.springboot.exceptions.CurveValidationException;
import com.nnk.springboot.exceptions.RatingNotFoundException;
import com.nnk.springboot.exceptions.RatingValidationException;
import com.nnk.springboot.exceptions.RuleNotFoundException;
import com.nnk.springboot.exceptions.RuleValidationException;
import com.nnk.springboot.exceptions.TradeNotFoundException;
import com.nnk.springboot.exceptions.TradeValidationException;
import com.nnk.springboot.exceptions.UserNotFoundException;
import com.nnk.springboot.exceptions.UserValidationException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BidListValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBidListValidationException(BidListValidationException ex, Model model) {
        model.addAttribute("org.springframework.validation.BindingResult.bidList", ex.getErrors());
        model.addAttribute("bidList", ex.getErrors().getTarget());
        return ex.getPath();
    }

    @ExceptionHandler(BidListNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleBidListNotFoundException(BidListNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CurveValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleCurveValidationException(CurveValidationException ex, Model model) {
        model.addAttribute("org.springframework.validation.BindingResult.curve", ex.getErrors());
        model.addAttribute("curve", ex.getErrors().getTarget());
        return ex.getPath();
    }

    @ExceptionHandler(CurveNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCurveNotFoundException(CurveNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(RatingValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleRatingValidationException(RatingValidationException ex, Model model) {
        model.addAttribute("org.springframework.validation.BindingResult.rating", ex.getErrors());
        model.addAttribute("rating", ex.getErrors().getTarget());
        return ex.getPath();
    }

    @ExceptionHandler(RatingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleRatingNotFoundException(RatingNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(RuleValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleRuleValidationException(RuleValidationException ex, Model model) {
        model.addAttribute("org.springframework.validation.BindingResult.ruleName", ex.getErrors());
        model.addAttribute("ruleName", ex.getErrors().getTarget());
        return ex.getPath();
    }

    @ExceptionHandler(RuleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleRuleNotFoundException(RuleNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(TradeValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTradeValidationException(TradeValidationException ex, Model model) {
        model.addAttribute("org.springframework.validation.BindingResult.trade", ex.getErrors());
        model.addAttribute("trade", ex.getErrors().getTarget());
        return ex.getPath();
    }

    @ExceptionHandler(TradeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleTradeNotFoundException(TradeNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUserValidationException(UserValidationException ex, Model model) {
        model.addAttribute("org.springframework.validation.BindingResult.user", ex.getErrors());
        model.addAttribute("user", ex.getErrors().getTarget());
        return ex.getPath();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserValidationException(UserNotFoundException ex) {
        return ex.getMessage();
    }
}
