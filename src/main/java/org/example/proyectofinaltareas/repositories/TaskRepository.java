package org.example.proyectofinaltareas.repositories;

import org.example.proyectofinaltareas.models.Task;
import org.example.proyectofinaltareas.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByUser(User user);
}
