package com.example.laboratory6;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/tasks")
public class TaskThymeleafController {
    private final TaskService taskService;
    private final CategoryRepository categoryRepository;

    public TaskThymeleafController(TaskService taskService, CategoryRepository categoryRepository) {
        this.taskService = taskService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String showTasks(@RequestParam(required = false) String title,
                            @RequestParam(required = false) String category,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        // Параметры пагинации
        Pageable pageable = PageRequest.of(page, size);

        // Получение задач с учетом фильтрации и пагинации
        Page<Task> taskPage = taskService.getAllTasks(title, category, pageable);

        // Добавление данных в модель
        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("param", new FilterParams(title, category));
        model.addAttribute("newTask", new TaskDto());

        return "tasks";
    }

    @PostMapping("/add")
    public String addTask(@ModelAttribute TaskDto taskDto) {
        taskService.addTask(
                taskDto.getTitle(),
                taskDto.getDescription(),
                taskDto.getDueDate(),
                taskDto.getCategoryId()
        );
        return "redirect:/web/tasks";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/web/tasks";
    }

    public static class FilterParams {
        private String title;
        private String category;

        public FilterParams(String title, String category) {
            this.title = title;
            this.category = category;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }
}
