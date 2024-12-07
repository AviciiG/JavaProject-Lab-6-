package com.example.laboratory6;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<Page<Task>> getAllTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(taskService.getAllTasks(title, category, PageRequest.of(page, size)));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTask(@RequestBody TaskDto taskDto) {
        try {
            // Convert dueDate from String to LocalDate
            LocalDate dueDate = LocalDate.parse(taskDto.getDueDate());
            Task task = taskService.addTask(
                    taskDto.getTitle(),
                    taskDto.getDescription(),
                    dueDate,
                    taskDto.getCategoryId()
            );
            return ResponseEntity.ok(task);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
