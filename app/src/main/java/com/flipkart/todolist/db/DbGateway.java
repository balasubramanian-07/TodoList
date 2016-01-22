package com.flipkart.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.flipkart.todolist.db.TaskTable.*;

public final class DbGateway extends SQLiteOpenHelper {

    private static final String TAG = "DbGateway" ;


    public DbGateway(Context context) {

        super(context, "todolist.db", null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Create table : " + TASK_TABLE_NAME);

        String query = TaskTable.createTableQuery();
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "drop table if exists " + TASK_TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }
}
