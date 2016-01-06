package com.flipkart.todolist.mappers;

import android.database.Cursor;

import com.flipkart.todolist.Task;
import com.flipkart.todolist.TaskTable;

import java.util.ArrayList;
import java.util.List;

import static com.flipkart.todolist.TaskTable.*;

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
//            String due_time = resultSet.getString(resultSet.getColumnIndex(TasksTable.DUE_TIME));

//            tasks.add(new Task(title, description, due_date, due_time));
            tasks.add(new Task(title, description, due_date,1));
        }

        return tasks;
    }
}
