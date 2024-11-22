package com.example.laboratory6;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> categories = Arrays.asList("Work", "Study", "Planning", "Personal");

        for (String categoryName : categories) {
            if (!categoryRepository.existsByName(categoryName)) {
                Category category = new Category();
                category.setName(categoryName);
                categoryRepository.save(category);
            }
        }
    }
}
