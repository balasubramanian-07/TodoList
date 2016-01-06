package com.flipkart.todolist.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    private String title;
    private String notes;
    private String date;
    private int priority;
    private Integer task_id;

    public Task(String title, String notes, String date, int priority) {
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

    public String getDate() {

        return date;
    }

    public int getPriority() {

        return priority;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = (Integer) task_id;
    }
}

