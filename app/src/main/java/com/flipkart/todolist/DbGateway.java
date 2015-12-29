package com.flipkart.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.flipkart.todolist.TaskTable.*;

public final class DbGateway extends SQLiteOpenHelper {

    public static final String TASK_TABLE = "tasks";

    public DbGateway(Context context) {

        super(context, "todolist.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TASK_TABLE
                + "(" + ID + " INTEGER PRIMARY KEY"
                + "," + TITLE + " TEXT NOT NULL"
                + "," + NOTES + " TEXT NOT NULL"
                + "," + DUE_DATE + " DATETIME NOT NULL"
                + "," + STATUS + " TEXT NOT NULL"
                + "," + CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
