package com.nhom1.kttstoreapp.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private String id;
    private String name;
    private List<Task> tasks;
    private List<String> columns; // Status columns like "Cần làm", "Đang làm", "Hoàn thành"

    public Board() {
        this.tasks = new ArrayList<>();
        this.columns = new ArrayList<>();
        // Default columns
        this.columns.add("Cần làm");
        this.columns.add("Đang làm");
        this.columns.add("Hoàn thành");
    }

    public Board(String id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<Task> getTasksByStatus(String status) {
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getStatus().equals(status)) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }
}

