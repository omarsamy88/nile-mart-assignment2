package org.route.service;

import org.route.model.Product;
import org.route.model.Category;
import org.route.model.Brand;
import org.route.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final BrandService brandService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService, BrandService brandService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    public List<Product> getAvailableProducts() {
        List<Product> availableProducts = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            if (p.getInStock() > 0) {
                availableProducts.add(p);
            }
        }
        return availableProducts;
    }

    public void saveProduct(Product product) {
        Category category = categoryService.findBySlug(product.getCategory());
        if (category == null || category.getParentSlug() == null || category.getParentSlug().isBlank()) {
            throw new IllegalArgumentException("Product must belong to a sub-category");
        }

        if (product.getBrandSlug() != null && !product.getBrandSlug().isBlank()) {
            Brand brand = brandService.getAllBrands().stream()
                    .filter(b -> b.getSlug().equals(product.getBrandSlug()))
                    .findFirst()
                    .orElse(null);

            if (brand == null || !brand.isActive()) {
                throw new IllegalArgumentException("Brand does not exist or is inactive");
            }
        } else {
            product.setBrandSlug(null);
        }
        productRepository.save(product);
    }

    public boolean isProductExist(String sku) {
        return productRepository.findBySku(sku) != null;
    }

    public String getCategoryName(String categorySlug) {
        Category category = categoryService.findBySlug(categorySlug);
        return category != null ? category.getName() : "—";
    }

    public String getBrandName(String brandSlug) {
        if (brandSlug == null || brandSlug.isBlank()) {
            return "—";
        }

        Brand brand = brandService.getAllBrands().stream()
                .filter(b -> b.getSlug().equals(brandSlug))
                .findFirst()
                .orElse(null);
        return brand != null ? brand.getName() : "—";
    }
}
