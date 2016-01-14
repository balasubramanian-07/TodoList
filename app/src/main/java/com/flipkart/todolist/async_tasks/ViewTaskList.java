package com.flipkart.todolist.async_tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.flipkart.todolist.Constants;
import com.flipkart.todolist.db.DbGateway;
import com.flipkart.todolist.db.TaskTable;
import com.flipkart.todolist.delegates.AsyncTaskCompletedListener;

import static com.flipkart.todolist.Constants.APP_LAUNCHER_VIEW_QUERY_TAG;
import static com.flipkart.todolist.Constants.SORT_BY_PRIORITY;
import static com.flipkart.todolist.db.TaskTable.DUE_DATE;
import static com.flipkart.todolist.db.TaskTable.TASK_TABLE_NAME;

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
    private AsyncTaskCompletedListener<Cursor> callback;

    public ViewTaskList(DbGateway dbGateway, Context context) {

        this.dbGateway = dbGateway;
        this.context = context;
    }

    @Override
    protected Cursor doInBackground(String... params) {

        database = dbGateway.getWritableDatabase();
        String query = null;
        switch (params[0]){
            case APP_LAUNCHER_VIEW_QUERY_TAG:
                query = TaskTable.showTasksOnAppLaunch();

            case SORT_BY_PRIORITY:
                query = TaskTable.sortTasksByPriority();

            default:

        }
        executeSqlQuery(query);
        return cursor;
    }

    private Cursor executeSqlQuery(String query){

        if (query != null){
            Log.i(TAG, "Query fired : " + query);
            cursor = database.rawQuery(query,null);
        }
        return cursor;
    }

    @Override
    protected void onPostExecute(Cursor cursor) {

        super.onPostExecute(cursor);
        callback.onTaskComplete(cursor);
    }

    public void setCallback(AsyncTaskCompletedListener<Cursor> callback) {

        this.callback = callback;
    }
}
