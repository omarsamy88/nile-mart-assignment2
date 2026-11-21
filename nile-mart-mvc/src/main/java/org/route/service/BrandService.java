package org.route.service;

import org.route.model.Brand;
import org.route.repository.BrandRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BrandService {
    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public List<Brand> getActiveBrands() {
        return brandRepository.findAll()
                .stream()
                .filter(Brand::isActive)
                .toList();
    }

    public void saveBrand(Brand brand) {
        for (Brand existing : brandRepository.findAll()) {
            if (existing.getName().equalsIgnoreCase(brand.getName())) {
                throw new IllegalArgumentException("Brand name already exists");
            }
        }
        if (brandRepository.findBySlug(brand.getSlug()) != null) {
            throw new IllegalArgumentException("Slug already exists");
        }
        brandRepository.save(brand);
    }
}
