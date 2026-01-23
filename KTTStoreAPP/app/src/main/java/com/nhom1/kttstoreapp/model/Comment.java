package com.nhom1.kttstoreapp.model;

import java.util.Date;

public class Comment {
    private String id;
    private String content;
    private Member author;
    private Date createdAt;
    private String status; // Optional: for activity tracking

    public Comment() {
        this.createdAt = new Date();
    }

    public Comment(String id, String content, Member author) {
        this();
        this.id = id;
        this.content = content;
        this.author = author;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Member getAuthor() {
        return author;
    }

    public void setAuthor(Member author) {
        this.author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

