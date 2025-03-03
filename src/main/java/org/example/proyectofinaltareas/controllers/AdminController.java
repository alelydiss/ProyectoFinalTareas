package org.example.proyectofinaltareas.controllers;

import org.example.proyectofinaltareas.models.Category;
import org.example.proyectofinaltareas.models.Task;
import org.example.proyectofinaltareas.repositories.CategoryRepository;
import org.example.proyectofinaltareas.repositories.TaskRepository;
import org.example.proyectofinaltareas.repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public AdminController(TaskRepository taskRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("tasks", taskRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "admin";
    }

    @PostMapping("/tasks/edit")
    public String editTaskTitle(@RequestParam int id, @RequestParam String title) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setTitulo(title);
            taskRepository.save(task);
        }
        return "redirect:/admin";
    }

    @PostMapping("/tasks/status")
    public String changeTaskStatus(@RequestParam int id, @RequestParam boolean status) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setCompletada(status);
            taskRepository.save(task);
        }
        return "redirect:/admin";
    }

    @GetMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable int id) {
        taskRepository.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping("/categories/add")
    public String addCategory(@RequestParam String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        return "redirect:/admin";
    }


    @PostMapping("/categories/edit")
    public String editCategory(@RequestParam int id, @RequestParam String name) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            category.setName(name);
            categoryRepository.save(category);
        }
        return "redirect:/admin";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id) {
        categoryRepository.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }
}
