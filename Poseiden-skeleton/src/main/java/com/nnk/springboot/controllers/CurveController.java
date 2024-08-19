package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnk.springboot.model.CurvePoint;
import com.nnk.springboot.services.CurvePointService;

import jakarta.validation.Valid;

@Controller
public class CurveController {

    private final CurvePointService curvePointService;

    public CurveController(CurvePointService curvePointService) {
        this.curvePointService = curvePointService;
    }

    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        model.addAttribute("curves", curvePointService.getAllCurvePoint());
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurveForm(CurvePoint curve) {
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@RequestBody @Valid CurvePoint curve, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            curvePointService.saveCurvePoint(curve);
            model.addAttribute("curves", curvePointService.getAllCurvePoint());
            return "redirect:/curvePoint/list";
        }
        return "curvePoint/add";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePoint curve = curvePointService.getCurvePoint(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid curve list Id:" + id));
        model.addAttribute("curve", curve);
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id,
            @RequestBody @Valid CurvePoint curve,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "curvePoint/update";
        }

        curve.setId(id);
        curvePointService.saveCurvePoint(curve);
        model.addAttribute("curvePoints", curvePointService.getAllCurvePoint());
        return "redirect:/curvePoint/list";
    }

    @DeleteMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        CurvePoint curve = curvePointService.getCurvePoint(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid curve Id:" + id));
        curvePointService.deleteCurvePoint(curve);
        model.addAttribute("curves", curvePointService.getAllCurvePoint());
        return "redirect:/curvePoint/list";
    }
}
