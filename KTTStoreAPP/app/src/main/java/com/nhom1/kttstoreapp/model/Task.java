package com.nhom1.kttstoreapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task {
    private String id;
    private String title;
    private String description;
    private String status; // "Cần làm", "Đang làm", "Hoàn thành"
    private Date dueDate;
    private List<Label> labels;
    private List<Member> members;
    private List<Comment> comments;
    private List<String> checklists;
    private Date createdAt;
    private Date updatedAt;

    public Task() {
        this.labels = new ArrayList<>();
        this.members = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.checklists = new ArrayList<>();
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Task(String id, String title, String description, String status) {
        this();
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<String> getChecklists() {
        return checklists;
    }

    public void setChecklists(List<String> checklists) {
        this.checklists = checklists;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

