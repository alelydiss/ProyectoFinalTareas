package org.example.proyectofinaltareas.controllers;

import org.example.proyectofinaltareas.models.Category;
import org.example.proyectofinaltareas.models.Task;
import org.example.proyectofinaltareas.models.User;
import org.example.proyectofinaltareas.repositories.CategoryRepository;
import org.example.proyectofinaltareas.repositories.TaskRepository;
import org.example.proyectofinaltareas.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class TaskController {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TaskController(TaskRepository taskRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName()).orElse(null);
    }

    @GetMapping
    public String index(@RequestParam(required = false) String filtroNombre,
                        @RequestParam(required = false) Integer filtroCategoria,
                        @RequestParam(required = false) Boolean filtroEstado,
                        Model model) {
        User user = getAuthenticatedUser();
        if (user == null) return "redirect:/login";  // Redirige si no hay sesión

        List<Task> tasks = taskRepository.findByUser(user);
        if (filtroNombre != null && !filtroNombre.isEmpty()) {
            tasks = tasks.stream()
                    .filter(task -> task.getTitulo().toLowerCase().contains(filtroNombre.toLowerCase()))
                    .toList();
        }
        if (filtroCategoria != null) {
            tasks = tasks.stream()
                    .filter(task -> task.getCategory().getId() == filtroCategoria)
                    .toList();
        }
        if (filtroEstado != null) {
            tasks = tasks.stream()
                    .filter(task -> task.isCompletada() == filtroEstado)
                    .toList();
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("categories", categoryRepository.findAll());
        return "index";
    }

    @PostMapping("/addTask")
    public String addTask(@RequestParam String titulo, @RequestParam int categoryId, Model model) {
        User user = getAuthenticatedUser();
        if (user == null) return "redirect:/login";

        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isEmpty()) {
            model.addAttribute("errorMessage", "Categoría no encontrada.");
            return "redirect:/";
        }

        Task task = new Task(titulo, false, categoryOpt.get(), user);
        taskRepository.save(task);
        return "redirect:/";
    }

    @PostMapping("/toggleTask")
    public String toggleTask(@RequestParam int id) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        taskOpt.ifPresent(task -> {
            task.setCompletada(!task.isCompletada());
            taskRepository.save(task);
        });
        return "redirect:/";
    }

    @PostMapping("/editTask")
    public String editTask(@RequestParam int id, @RequestParam String nuevoTitulo) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        taskOpt.ifPresent(task -> {
            task.setTitulo(nuevoTitulo);
            taskRepository.save(task);
        });
        return "redirect:/";
    }

    @PostMapping("/deleteTask")
    public String deleteTask(@RequestParam int id) {
        taskRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/summary")
    public String summary(Model model) {
        User user = getAuthenticatedUser();
        if (user == null) return "redirect:/login";

        List<Task> tasks = taskRepository.findByUser(user);
        model.addAttribute("tasks", tasks);
        return "summary";
    }

}
