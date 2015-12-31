package com.flipkart.todolist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static com.flipkart.todolist.TaskTable.DUE_DATE;
import static com.flipkart.todolist.TaskTable.PRIORITY;
import static com.flipkart.todolist.TaskTable.TASK_TABLE_NAME;
import static com.flipkart.todolist.TaskTable.TITLE;

/**
 * Created by mayank.gupta on 30/12/15.
 */
public class ViewTaskList extends AsyncTask<String,Void,Cursor> {
    private static final String TAG = "ViewTaskList" ;
    private Cursor cursor;
    private DbGateway dbGateway;
    private SQLiteDatabase database;
    private View taskListFragmentView;
    private Context context;
    private ListView taskListView;
    private AsyncTaskCompletedListener<SimpleCursorAdapter> callback;

    public ViewTaskList(DbGateway dbGateway, View taskListView, Context context, AsyncTaskCompletedListener callback) {
        this.dbGateway = dbGateway;
        this.taskListFragmentView = taskListView;
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Cursor doInBackground(String... params) {
        database = dbGateway.getWritableDatabase();
        String query = "SELECT * FROM " + TASK_TABLE_NAME +" order by " + DUE_DATE;
        Log.i(TAG, "Query fired : " + query);
        cursor = database.rawQuery(query,null);
        return cursor;
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);

        taskListView = (ListView) taskListFragmentView.findViewById(R.id.taskListView);
        String[] from = {DUE_DATE,TITLE,PRIORITY};
        int[] to = {R.id.taskDueDate,R.id.taskTitle, R.id.taskPriority};
        SimpleCursorAdapter sCursorAdapter = new SimpleCursorAdapter(context, R.layout.row,cursor,from,to);
        taskListView.setAdapter(sCursorAdapter);

        callback.onTaskComplete(sCursorAdapter);
    }
}
