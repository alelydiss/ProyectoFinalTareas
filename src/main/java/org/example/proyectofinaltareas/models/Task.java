package org.example.proyectofinaltareas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task")
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String titulo;
    private boolean completada;

    @ManyToOne @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    public Task(String titulo, boolean completada, Category category, User user) {
        this.titulo = titulo;
        this.completada = completada;
        this.category = category;
        this.user = user;
    }

    public Task() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
