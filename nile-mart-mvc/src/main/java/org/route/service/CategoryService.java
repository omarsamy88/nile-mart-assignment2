
package org.route.service;

import org.route.model.Category;
import org.route.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        List<Category> all = categoryRepository.findAll();
        return all.stream()
                .filter(category -> isTopLevel(category))
                .sorted(Comparator.comparing(Category::getDisplayOrder))
                .flatMap(parent -> {
                    List<Category> children = all.stream()
                            .filter(child ->
                                    Objects.equals(child.getParentSlug(), parent.getSlug()))
                            .sorted(Comparator.comparing(Category::getDisplayOrder))
                            .toList();
                    return java.util.stream.Stream
                            .concat(java.util.stream.Stream.of(parent),children.stream()
                    );
                })
                .toList();
    }

    public List<Category> getTopLevelCategories() {
        return categoryRepository.findAll().stream()
                .filter(this::isTopLevel)
                .sorted(Comparator.comparing(Category::getDisplayOrder))
                .toList();
    }

    public List<Category> getSubCategories() {
        return categoryRepository.findAll().stream()
                .filter(category -> !isTopLevel(category))
                .sorted(Comparator.comparing(Category::getDisplayOrder))
                .toList();
    }

    public Category findBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    public void saveCategory(Category category) {
        String parentSlug = category.getParentSlug();
        if (parentSlug != null && !parentSlug.isBlank()) {
            Category parent = categoryRepository.findBySlug(parentSlug);
            if (parent == null) {
                throw new IllegalArgumentException("Parent category does not exist");
            }
            if (!isTopLevel(parent)) {
                throw new IllegalArgumentException("Parent must be a top-level category");
            }
        }

        if (categoryRepository.findBySlug(category.getSlug()) != null) {
            throw new IllegalArgumentException("Slug already exists");
        }

        for (Category existing : categoryRepository.findAll()) {
            boolean sameParent = Objects.equals(normalizeParent(existing.getParentSlug()), normalizeParent(parentSlug));
            boolean sameName = existing.getName().equalsIgnoreCase(category.getName());
            if (sameParent && sameName) {
                String parentName = "top level";
                if (parentSlug != null && !parentSlug.isBlank()) {
                    parentName = categoryRepository.findBySlug(parentSlug).getName();
                }
                throw new IllegalArgumentException("A category with this name already exists under " + parentName);
            }
        }
        categoryRepository.save(category);
    }

    private boolean isTopLevel(Category category) {
        return category.getParentSlug() == null || category.getParentSlug().isBlank();
    }

    private String normalizeParent(String parentSlug) {
        return parentSlug == null || parentSlug.isBlank() ? "" : parentSlug;
    }
}
