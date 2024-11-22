package com.example.laboratory6;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
