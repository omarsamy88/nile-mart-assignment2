package org.route.controller;

import jakarta.validation.Valid;
import org.route.model.Brand;
import org.route.service.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public String listBrands(Model model) {
        model.addAttribute("brands", brandService.getAllBrands());
        return "brands";
    }

    @GetMapping("/new")
    public String showBrandForm(Model model) {
        model.addAttribute("brand", new Brand());
        return "brandForm";
    }

    @PostMapping
    public String saveBrand(
            @Valid @ModelAttribute("brand") Brand brand, BindingResult result) {
        if (result.hasErrors()) {
            return "brandForm";
        }
        try {
            brandService.saveBrand(brand);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Brand name already exists")) {
                result.rejectValue("name", "duplicate", e.getMessage());
            } else if (e.getMessage().equals("Slug already exists")) {
                result.rejectValue("slug", "duplicate", e.getMessage());
            }
            return "brandForm";
        }
        return "redirect:/brands";
    }
}
