package com.example.laboratory6;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    public TaskService(TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<Task> getAllTasks(String title, String category, Pageable pageable) {
        if (title != null && category != null) {
            return taskRepository.findByTitleContainingAndCategoryName(title, category, pageable);
        } else if (title != null) {
            return taskRepository.findByTitleContaining(title, pageable);
        } else if (category != null) {
            return taskRepository.findByCategoryName(category, pageable);
        } else {
            return taskRepository.findAll(pageable);
        }
    }

    public void uploadTaskImage(Long taskId, byte[] imageBytes, String originalFilename) throws IOException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        String imageName = "task_" + taskId + "_" + originalFilename;
        Path imagePath = Paths.get(UPLOAD_DIR + imageName);
        Files.write(imagePath, imageBytes);

        task.setImageName(imageName);
        taskRepository.save(task);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Task addTask(String title, String description, LocalDate dueDate, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setCategory(category);

        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new IllegalArgumentException("Task not found");
        }
        taskRepository.deleteById(taskId);
    }
}
