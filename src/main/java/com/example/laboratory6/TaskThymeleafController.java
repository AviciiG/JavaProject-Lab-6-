package com.example.laboratory6;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/web/tasks")
public class TaskThymeleafController {

    private final TaskService taskService;

    public TaskThymeleafController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String showTasks(@RequestParam(required = false) String title,
                            @RequestParam(required = false) String category,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model,
                            HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null || username.isEmpty()) {
            return "redirect:/web/auth/login";
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = taskService.getAllTasks(title, category, pageable);

        model.addAttribute("username", username);
        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("categories", taskService.getAllCategories());
        model.addAttribute("pageNumber", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        return "tasks";
    }

    @PostMapping("/{id}/upload-image")
    public String uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        try {
            taskService.uploadTaskImage(id, image.getBytes(), image.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/web/tasks";
    }

    @PostMapping("/add")
    public String addTask(@RequestParam String title,
                          @RequestParam String description,
                          @RequestParam String dueDate,
                          @RequestParam Long categoryId) {
        taskService.addTask(title, description, LocalDate.parse(dueDate), categoryId);
        return "redirect:/web/tasks";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/web/tasks";
    }
}
