package com.nnk.springboot.controllers;

import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.exceptions.BidListValidationException;
import com.nnk.springboot.model.BidList;
import com.nnk.springboot.services.BidListService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class BidListController {

    private final BidListService bidListService;

    public BidListController(BidListService bidListService) {
        this.bidListService = bidListService;
    }

    @GetMapping("/bidList/list")
    public String home(Model model) {
        model.addAttribute("bidLists", bidListService.getAllBidList());
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            bidListService.saveBidList(bid);
            model.addAttribute("bidLists", bidListService.getAllBidList());
            return "redirect:/bidList/list";
        }
        throw new BidListValidationException("bidList/add", result);
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList bid = bidListService.getBidList(id)
                .orElseThrow(() -> new BidListNotFoundException(id));
        model.addAttribute("bidList", bid);
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id,
            @Valid BidList bidList,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            throw new BidListValidationException("bidList/update", result);
        }

        bidList.setBidListId(id);
        bidListService.saveBidList(bidList);
        model.addAttribute("bidLists", bidListService.getAllBidList());
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        BidList bidList = bidListService.getBidList(id)
                .orElseThrow(() -> new BidListNotFoundException(id));
        bidListService.deleteBidList(bidList);
        model.addAttribute("bidLists", bidListService.getAllBidList());
        return "redirect:/bidList/list";
    }
}
