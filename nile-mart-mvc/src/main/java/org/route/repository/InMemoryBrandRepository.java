
package org.route.repository;

import jakarta.annotation.PostConstruct;
import org.route.model.Brand;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryBrandRepository implements BrandRepository {
    private final Map<String, Brand> brands = new LinkedHashMap<>();
    @PostConstruct
    public void init() {
        save(Brand.builder()
                .name("Samsung")
                .slug("samsung")
                .active(true)
                .build());

        save(Brand.builder()
                .name("Asus")
                .slug("asus")
                .active(true)
                .build());

        save(Brand.builder()
                .name("Anker")
                .slug("anker")
                .active(true)
                .build());

        save(Brand.builder()
                .name("Tefal")
                .slug("tefal")
                .active(true)
                .build());

        save(Brand.builder()
                .name("Old Brand")
                .slug("old-brand")
                .active(false)
                .build());
    }

    @Override
    public List<Brand> findAll() {
        return new ArrayList<>(brands.values());
    }

    @Override
    public Brand findBySlug(String slug) {
        return brands.get(slug);
    }

    @Override
    public void save(Brand brand) {
        brands.put(brand.getSlug(), brand);
    }
}
