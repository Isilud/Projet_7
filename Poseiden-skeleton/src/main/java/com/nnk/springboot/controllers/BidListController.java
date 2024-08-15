package com.nnk.springboot.controllers;

import com.nnk.springboot.model.BidList;
import com.nnk.springboot.services.BidListService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@Controller
public class BidListController {

    private final BidListService bidListService;

    public BidListController(BidListService bidListService) {
        this.bidListService = bidListService;
    }

    @GetMapping("/bidList/list")
    public String home(Model model) {
        model.addAttribute("bids", bidListService.getAllBidList());
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@RequestBody @Valid BidList bid, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            bidListService.saveBidList(bid);
            model.addAttribute("bids", bidListService.getAllBidList());
            return "redirect:/bidList/list";
        }
        return "bidList/add";
    }

    @GetMapping("/bidList/find/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList bid = bidListService.getBidList(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bid list Id:" + id));
        model.addAttribute("bid", bid);
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id,
            @RequestBody @Valid BidList bidList,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/update";
        }

        bidList.setBidListId(id);
        bidListService.saveBidList(bidList);
        model.addAttribute("bids", bidListService.getAllBidList());
        return "redirect:/bidList/list";
    }

    @DeleteMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        BidList bidList = bidListService.getBidList(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bidList Id:" + id));
        bidListService.deleteBidList(bidList);
        model.addAttribute("bids", bidListService.getAllBidList());
        return "redirect:/bidList/list";
    }
}
