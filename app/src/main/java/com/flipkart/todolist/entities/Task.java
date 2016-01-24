package com.flipkart.todolist.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    private String title;
    private String notes;
    private String dueDate;
    private String dueTime;
    private int priority;
    private Integer task_id;

    public Task(String title, String notes, String dueDate,String dueTime) {
        this.title = title;
        this.notes = notes;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
    }

    public String getTitle() {

        return title;
    }

    public String getNotes() {

        return notes;
    }

    public String getDate() {

        return dueDate;
    }

    public int getPriority() {

        return priority;
    }

    public void setPriority(int priority) {

        this.priority =  priority;
    }

    public void setDueDate(String dueDate) {

        this.dueDate =  dueDate;
    }

    public void setDueTime(String dueTime) {

        this.dueTime =  dueTime;
    }

    public void setTitle(String title){

        this.title = title;
    }

    public void setNotes(String notes){

        this.notes = notes;
    }


    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = (Integer) task_id;
    }

    public String getDueTime() {
        return dueTime;
    }
}

