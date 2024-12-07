package com.example.laboratory6;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCategory_Name(String categoryName);
    List<Task> searchByTitle(String keyword);
    // Поиск по названию задачи
    @Query("SELECT t FROM Task t WHERE t.title LIKE %:title%")
    Page<Task> findByTitleContaining(@Param("title") String title, Pageable pageable);

    // Фильтрация по категории
    @Query("SELECT t FROM Task t WHERE t.category.name = :categoryName")
    Page<Task> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);

    List<Task> findByTitleContainingIgnoreCase(String title);

    Page<Task> findByTitleContainingIgnoreCaseAndCategory_Name(String title, String categoryName, Pageable pageable);

    Page<Task> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Task> findByCategory_Name(String categoryName, Pageable pageable);

    Page<Task> findAll(Pageable pageable);

    Page<Task> findByTitleContainingAndCategoryNameContaining(String title, String category, Pageable pageable);

    Page<Task> findByCategoryNameContaining(String category, Pageable pageable);

    List<Task> findByTitleContainingAndCategoryName(String title, String categoryName);
    Page<Task> findByTitleContainingAndCategoryName(String title, String categoryName, Pageable pageable);

}
