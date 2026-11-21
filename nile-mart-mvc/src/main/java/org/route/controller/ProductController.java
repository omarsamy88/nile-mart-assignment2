package org.route.controller;

import jakarta.validation.Valid;
import org.route.model.Product;
import org.route.service.ProductService;
import org.route.service.CategoryService;
import org.route.service.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    public ProductController(ProductService productService, CategoryService categoryService, BrandService brandService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    @GetMapping("/products")
    public String getProducts(Model model) {
        List<Product> products = productService.getAvailableProducts();
        model.addAttribute("availableProducts", products);
        model.addAttribute("productService", productService);
        return "products";
    }

    @GetMapping("/products/new")
    public String showForm(Model model) {
        model.addAttribute("product", new Product());
        addDropdownData(model);
        return "productForm";
    }

    @PostMapping("/products")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult result, Model model) {
        if (productService.isProductExist(product.getSku())) {
            result.rejectValue("sku", "duplicate", "SKU already exists");
        }

        if (result.hasErrors()) {
            addDropdownData(model);
            return "productForm";
        }

        try {
            productService.saveProduct(product);

        } catch (IllegalArgumentException e) {

            if (e.getMessage().contains("sub-category")) {
                result.rejectValue("category", "invalid", e.getMessage());

            } else {
                result.rejectValue("brandSlug", "invalid", e.getMessage());
            }
            addDropdownData(model);
            return "productForm";
        }
        return "redirect:/products";
    }

    private void addDropdownData(Model model) {
        model.addAttribute("subCategories", categoryService.getSubCategories());
        model.addAttribute("activeBrands", brandService.getActiveBrands());
    }
}
