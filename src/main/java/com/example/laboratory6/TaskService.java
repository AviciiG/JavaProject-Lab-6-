package com.example.laboratory6;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    public TaskService(TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<Task> getAllTasks(String title, String category, Pageable pageable) {
        if (title != null && category != null) {
            return taskRepository.findByTitleContainingIgnoreCaseAndCategory_Name(title, category, pageable);
        } else if (title != null) {
            return taskRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (category != null) {
            return taskRepository.findByCategory_Name(category, pageable);
        } else {
            return taskRepository.findAll(pageable);
        }
    }

    public Task addTask(String title, String description, String dueDate, Long categoryId) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(LocalDate.parse(dueDate));

        if (categoryId != null) {
            categoryRepository.findById(categoryId).ifPresent(task::setCategory);
        }

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
