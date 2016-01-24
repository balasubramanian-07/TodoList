package com.flipkart.todolist.mappers;

import android.database.Cursor;

import com.flipkart.todolist.entities.Task;

import java.util.ArrayList;
import java.util.List;

import static com.flipkart.todolist.db.TaskTable.*;

/**
 * Created by mayank.gupta on 06/01/16.
 */
public class TaskMapper {
    public static List<Task> map(Cursor resultSet) {

        if(resultSet == null) {

            return null;
        }

        List<Task> tasks = new ArrayList<>();

        while(resultSet.moveToNext()) {

            String title = resultSet.getString(resultSet.getColumnIndex(TITLE));
            String description = resultSet.getString(resultSet.getColumnIndex(NOTES));
            String due_date = resultSet.getString(resultSet.getColumnIndex(DUE_DATE));
            String due_time = resultSet.getString(resultSet.getColumnIndex(DUE_TIME));
            int priority = resultSet.getInt(resultSet.getColumnIndex(PRIORITY));
            int id = resultSet.getInt(resultSet.getColumnIndex("_id"));
            Task task = new Task(title, description, due_date,due_time);
            task.setPriority(priority);
            task.setTask_id(id);
            tasks.add(task);
        }

        return tasks;
    }
}
