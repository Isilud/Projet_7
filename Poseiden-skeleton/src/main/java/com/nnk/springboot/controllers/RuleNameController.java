package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String home(Model model) {
        model.addAttribute("rules", ruleNameService.getAllRuleName());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName rule) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName rule, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            ruleNameService.saveRuleName(rule);
            model.addAttribute("rules", ruleNameService.getAllRuleName());
            return "redirect:/ruleName/list";
        }
        return "ruleName/add";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RuleName rule = ruleNameService.getRuleName(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid rule list Id:" + id));
        model.addAttribute("rule", rule);
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid RuleName rule,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/update";
        }

        rule.setId(id);
        ruleNameService.saveRuleName(rule);
        model.addAttribute("ruleNames", ruleNameService.getAllRuleName());
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        RuleName rule = ruleNameService.getRuleName(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid rule Id:" + id));
        ruleNameService.deleteRuleName(rule);
        model.addAttribute("rules", ruleNameService.getAllRuleName());
        return "redirect:/ruleName/list";
    }
}
