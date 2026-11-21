
package org.route.repository;

import jakarta.annotation.PostConstruct;
import org.route.model.Category;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryCategoryRepository implements CategoryRepository {
    private final Map<String, Category> categories = new LinkedHashMap<>();
    @PostConstruct
    public void init() {
        save(Category.builder()
                .name("Electronics")
                .slug("electronics")
                .parentSlug("")
                .displayOrder(1)
                .active(true)
                .build());

        save(Category.builder()
                .name("Mobile Phones")
                .slug("mobile-phones")
                .parentSlug("electronics")
                .displayOrder(1)
                .active(true)
                .build());

        save(Category.builder()
                .name("Laptops")
                .slug("laptops")
                .parentSlug("electronics")
                .displayOrder(2)
                .active(true)
                .build());

        save(Category.builder()
                .name("Accessories")
                .slug("electronics-accessories")
                .parentSlug("electronics")
                .displayOrder(3)
                .active(true)
                .build());

        save(Category.builder()
                .name("Home & Kitchen")
                .slug("home-kitchen")
                .parentSlug("")
                .displayOrder(2)
                .active(true)
                .build());

        save(Category.builder()
                .name("Cookware")
                .slug("cookware")
                .parentSlug("home-kitchen")
                .displayOrder(1)
                .active(true)
                .build());

        save(Category.builder()
                .name("Lighting")
                .slug("lighting")
                .parentSlug("home-kitchen")
                .displayOrder(2)
                .active(true)
                .build());

        save(Category.builder()
                .name("Fashion")
                .slug("fashion")
                .parentSlug("")
                .displayOrder(3)
                .active(false)
                .build());

        save(Category.builder()
                .name("Men's Clothing")
                .slug("mens-clothing")
                .parentSlug("fashion")
                .displayOrder(1)
                .active(true)
                .build());
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(categories.values());
    }

    @Override
    public Category findBySlug(String slug) {
        return categories.get(slug);
    }

    @Override
    public void save(Category category) {
        categories.put(category.getSlug(), category);
    }
}
