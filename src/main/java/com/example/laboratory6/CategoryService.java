package com.example.laboratory6;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Получить все категории
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
