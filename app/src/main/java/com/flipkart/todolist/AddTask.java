package com.flipkart.todolist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import static com.flipkart.todolist.TaskTable.*;
import static com.flipkart.todolist.TaskTable.DUE_DATE;
import static com.flipkart.todolist.TaskTable.NOTES;
import static com.flipkart.todolist.TaskTable.PRIORITY;
import static com.flipkart.todolist.TaskTable.STATUS;
import static com.flipkart.todolist.TaskTable.TITLE;


/**
 * Created by mayank.gupta on 30/12/15.
 */
public class AddTask extends AsyncTask<String,Void,Void> {

    private DbGateway dbGateway;
    private SQLiteDatabase database;
    private Task task;
    private final static String TAG = "AddTask";

    public AddTask(DbGateway dbGateway, Task task) {
        this.dbGateway = dbGateway;
        this.task = task;
    }

    @Override
    protected Void doInBackground(String... params) {
        database = dbGateway.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put(TITLE, task.getTitle());
        row.put(NOTES, task.getNotes());
        row.put(DUE_DATE, String.valueOf(task.getDate()));
        row.put(PRIORITY, task.getPriority());
        row.put(STATUS, String.valueOf(ValidStatus.CREATED));
        if (task.getTask_id() == null){
            Log.i(TAG, "Inserting new row in database");
            database.insert(TASK_TABLE_NAME,null,row);
        }else {
            String whereClause = "_id" + task.getTask_id();
            Log.i(TAG, "updating row in database : " + task.getTask_id());
            database.update(TASK_TABLE_NAME, row,whereClause, null);
        }
      return null;
    }
}