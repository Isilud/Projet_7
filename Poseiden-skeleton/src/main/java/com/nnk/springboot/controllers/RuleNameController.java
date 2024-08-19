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

import com.nnk.springboot.exceptions.RuleNotFoundException;
import com.nnk.springboot.exceptions.RuleValidationException;
import com.nnk.springboot.model.RuleName;
import com.nnk.springboot.services.RuleNameService;

import jakarta.validation.Valid;

@Controller
public class RuleNameController {

    private final RuleNameService ruleNameService;

    public RuleNameController(RuleNameService ruleNameService) {
        this.ruleNameService = ruleNameService;
    }

    @RequestMapping("/ruleName/list")
    @ResponseStatus(HttpStatus.OK)
    public String home(Model model) {
        model.addAttribute("rules", ruleNameService.getAllRuleName());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    @ResponseStatus(HttpStatus.OK)
    public String addRuleForm(RuleName rule) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    @ResponseStatus(HttpStatus.CREATED)
    public String validate(@Valid RuleName rule, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            ruleNameService.saveRuleName(rule);
            model.addAttribute("rules", ruleNameService.getAllRuleName());
            return "redirect:/ruleName/list";
        }
        throw new RuleValidationException("ruleName/update", result);
    }

    @GetMapping("/ruleName/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RuleName rule = ruleNameService.getRuleName(id)
                .orElseThrow(() -> new RuleNotFoundException(id));
        model.addAttribute("rule", rule);
        return "ruleName/update";
    }

    @PutMapping("/ruleName/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateBid(@PathVariable("id") Integer id,
            @Valid RuleName rule,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            throw new RuleValidationException("ruleName/update", result);
        }

        rule.setId(id);
        ruleNameService.saveRuleName(rule);
        model.addAttribute("ruleNames", ruleNameService.getAllRuleName());
        return "redirect:/ruleName/list";
    }

    @DeleteMapping("/ruleName/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        RuleName rule = ruleNameService.getRuleName(id)
                .orElseThrow(() -> new RuleNotFoundException(id));
        ruleNameService.deleteRuleName(rule);
        model.addAttribute("rules", ruleNameService.getAllRuleName());
        return "redirect:/ruleName/list";
    }
}
