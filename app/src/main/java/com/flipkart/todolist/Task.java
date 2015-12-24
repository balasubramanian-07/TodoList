package com.flipkart.todolist;

import java.util.Date;

public class Task {

    private String title;
    private String notes;
    private Date date;
    private int priority;

    public Task(String title, String notes, Date date, int priority) {

        this.title = title;
        this.notes = notes;
        this.date = date;
        this.priority = priority;
    }

    public String getTitle() {

        return title;
    }

    public String getNotes() {

        return notes;
    }

    public Date getDate() {

        return date;
    }

    public int getPriority() {

        return priority;
    }
}
