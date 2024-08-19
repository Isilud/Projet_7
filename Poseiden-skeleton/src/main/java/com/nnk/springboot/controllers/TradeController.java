package com.nnk.springboot.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.nnk.springboot.exceptions.TradeNotFoundException;
import com.nnk.springboot.exceptions.TradeValidationException;
import com.nnk.springboot.model.Trade;
import com.nnk.springboot.services.TradeService;

import jakarta.validation.Valid;

@Controller
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @RequestMapping("/trade/list")
    @ResponseStatus(HttpStatus.OK)
    public String home(Model model) {
        model.addAttribute("trades", tradeService.getAllTrade());
        return "trade/list";
    }

    @GetMapping("/trade/add")
    @ResponseStatus(HttpStatus.OK)
    public String addTradeForm(Trade trade) {
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    @ResponseStatus(HttpStatus.CREATED)
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            tradeService.saveTrade(trade);
            model.addAttribute("trades", tradeService.getAllTrade());
            return "redirect:/trade/list";
        }
        throw new TradeValidationException("trade/update", result);
    }

    @GetMapping("/trade/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Trade trade = tradeService.getTrade(id)
                .orElseThrow(() -> new TradeNotFoundException(id));
        model.addAttribute("trade", trade);
        return "trade/update";
    }

    @PutMapping("/trade/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateBid(@PathVariable("id") Integer id, @Valid Trade trade,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            throw new TradeValidationException("trade/update", result);
        }

        trade.setId(id);
        tradeService.saveTrade(trade);
        model.addAttribute("trades", tradeService.getAllTrade());
        return "redirect:/trade/list";
    }

    @DeleteMapping("/trade/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        Trade trade = tradeService.getTrade(id)
                .orElseThrow(() -> new TradeNotFoundException(id));
        tradeService.deleteTrade(trade);
        model.addAttribute("trades", tradeService.getAllTrade());
        return "redirect:/trade/list";
    }
}
