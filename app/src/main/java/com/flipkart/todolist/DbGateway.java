package com.flipkart.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.flipkart.todolist.TaskTable.*;

public final class DbGateway extends SQLiteOpenHelper {

    private static final String TAG = "DbGateway" ;

    public DbGateway(Context context) {

        super(context, "todolist.db", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,"Create table : " + TASK_TABLE_NAME);

        String query = "CREATE TABLE " + TASK_TABLE_NAME
                + "(" + ID + " INTEGER PRIMARY KEY"
                + "," + TITLE + " TEXT NOT NULL"
                + "," + NOTES + " TEXT NOT NULL"
                + "," + DUE_DATE + " DATETIME NOT NULL"
                + "," + PRIORITY + " INT NOT NULL"
                + "," + STATUS + " TEXT NOT NULL"
                + "," + CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "drop table if exists " + TASK_TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }
}
