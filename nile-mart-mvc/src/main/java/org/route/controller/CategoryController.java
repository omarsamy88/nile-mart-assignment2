package org.route.controller;

import jakarta.validation.Valid;
import org.route.model.Category;
import org.route.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories";
    }

    @GetMapping("/new")
    public String showCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("topLevelCategories", categoryService.getTopLevelCategories());
        return "categoryForm";
    }

    @PostMapping
    public String saveCategory(@Valid @ModelAttribute("category") Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("topLevelCategories", categoryService.getTopLevelCategories());
            return "categoryForm";
        }

        try {
            categoryService.saveCategory(category);
        } catch (IllegalArgumentException e) {

            if (e.getMessage().equals("Slug already exists")) {
                result.rejectValue("slug", "duplicate", e.getMessage());
            } else if (e.getMessage().startsWith(
                    "A category with this name already exists")) {
                result.rejectValue("name", "duplicate", e.getMessage());
            } else {
                result.rejectValue("parentSlug", "invalid", e.getMessage());
            }
            model.addAttribute("topLevelCategories", categoryService.getTopLevelCategories());
            return "categoryForm";
        }
        return "redirect:/categories";
    }
}
