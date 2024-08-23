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

import com.nnk.springboot.exceptions.CurveNotFoundException;
import com.nnk.springboot.exceptions.CurveValidationException;
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
    @ResponseStatus(HttpStatus.OK)
    public String home(Model model) {
        model.addAttribute("curves", curvePointService.getAllCurvePoint());
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    @ResponseStatus(HttpStatus.OK)
    public String addCurveForm(CurvePoint curve) {
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    @ResponseStatus(HttpStatus.CREATED)
    public String validate(@Valid CurvePoint curve, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            curvePointService.saveCurvePoint(curve);
            model.addAttribute("curves", curvePointService.getAllCurvePoint());
            return "redirect:/curvePoint/list";
        }
        throw new CurveValidationException("curvePoint/add", result);
    }

    @GetMapping("/curvePoint/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePoint curve = curvePointService.getCurvePoint(id)
                .orElseThrow(() -> new CurveNotFoundException(id));
        model.addAttribute("curve", curve);
        return "curvePoint/update";
    }

    @PutMapping("/curvePoint/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateBid(@PathVariable("id") Integer id,
            @Valid CurvePoint curve,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            throw new CurveValidationException("curvePoint/add", result);
        }

        curve.setId(id);
        curvePointService.saveCurvePoint(curve);
        model.addAttribute("curvePoints", curvePointService.getAllCurvePoint());
        return "redirect:/curvePoint/list";
    }

    @DeleteMapping("/curvePoint/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        CurvePoint curve = curvePointService.getCurvePoint(id)
                .orElseThrow(() -> new CurveNotFoundException(id));
        curvePointService.deleteCurvePoint(curve);
        model.addAttribute("curves", curvePointService.getAllCurvePoint());
        return "redirect:/curvePoint/list";
    }
}
