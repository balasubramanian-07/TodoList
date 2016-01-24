package com.flipkart.todolist.fragments;


import android.app.FragmentManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flipkart.todolist.Constants;
import com.flipkart.todolist.R;
import com.flipkart.todolist.TodoListApplication;
import com.flipkart.todolist.adapters.ListViewAdapter;
import com.flipkart.todolist.async_tasks.ViewTaskList;
import com.flipkart.todolist.db.DbGateway;
import com.flipkart.todolist.db.TaskTable;
import com.flipkart.todolist.delegates.AsyncTaskCompletedListener;
import com.flipkart.todolist.entities.Task;
import com.flipkart.todolist.mappers.TaskMapper;

import java.util.ArrayList;

import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE_MODAL;
import static com.flipkart.todolist.Constants.SHOW_DELETED_TASKS;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeletedTasksFragment extends Fragment implements AsyncTaskCompletedListener<Cursor> {

    private static final String TAG = "DeletedTasksFragment";
    private ListView deletedTaskListView;
    private DbGateway dbGateway;
    private ArrayList<Task> tasks;
    private ListViewAdapter listViewAdapter;
    private SQLiteDatabase database;
    private final String customTaskDeletePermanent = "permanently_delete";
    private final String customTaskUndoDelete = "move_delete_to_created";

    public DeletedTasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbGateway = ((TodoListApplication) getActivity().getApplication()).dbGateway;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "inside DeleteTasksFragment");
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_deleted_tasks, null);

        setWidgets(fragmentView);
        showDeletedTasksInUI();
        setCustomActionBar();

        return fragmentView;
    }

    private void setCustomActionBar() {

        deletedTaskListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                final int selectedItemCount = deletedTaskListView.getCheckedItemCount();
                mode.setTitle("Selection: " + selectedItemCount + " items");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                mode.getMenuInflater().inflate(R.menu.custom_action_bar, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.delete:
                        executeCustomAction(customTaskDeletePermanent);
                        mode.finish();
                        showDeletedTasksInUI();
                        return true;

                    case R.id.completeTask:
                        executeCustomAction(customTaskUndoDelete);
                        mode.finish();
                        showDeletedTasksInUI();
                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    private void executeCustomAction(String actionType) {

        int numOfSelectedTasks = 0;
        SparseBooleanArray selectedItemPositions = deletedTaskListView.getCheckedItemPositions();
        for (int i = 0; i < deletedTaskListView.getCount(); i++) {
            if (selectedItemPositions.get(i)) {
                numOfSelectedTasks = numOfSelectedTasks + 1;
                View view = deletedTaskListView.getChildAt(i);
                String taskTitle = getTaskTitle(view);
                switch (actionType){
                    case customTaskDeletePermanent:
                        permanentDeleteTaskWithTitle(taskTitle);
                        showToastNotification("Tasks Permanently Deleted : " + String.valueOf(numOfSelectedTasks));
                        break;
                    case customTaskUndoDelete:
                        undoDeletedTaskWithTitle(taskTitle);
                        showToastNotification("Tasks Recovered : " + String.valueOf(numOfSelectedTasks));
                        break;
                }
            }
        }
    }

    private String getTaskTitle(View view) {

        TextView title = (TextView) view.findViewById(R.id.taskTitle);
        String  taskTitle = title.getText().toString();
        Log.i(TAG, "The Task Title is: " + taskTitle);
        return  taskTitle;
    }

    private void permanentDeleteTaskWithTitle(String taskTitle) {

        String deleteQuery = TaskTable.permanentDeleteTaskQuery(taskTitle);
        executeDbQuery(deleteQuery);
    }

    private void undoDeletedTaskWithTitle(String taskTitle) {

        String deleteQuery = TaskTable.undoDeleteTaskQuery(taskTitle);
        executeDbQuery(deleteQuery);
    }

    private void executeDbQuery(String query){

        database = dbGateway.getWritableDatabase();
        database.execSQL(query);
    }

    private void showToastNotification(String msg) {

        Toast message = Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT);
        message.show();
    }

    private void showDeletedTasksInUI() {

        ViewTaskList viewTaskList = new ViewTaskList(dbGateway,getActivity().getApplicationContext());
        viewTaskList.setCallback(this);
        viewTaskList.execute(SHOW_DELETED_TASKS);
    }


    private void setWidgets(View fragmentView) {

        deletedTaskListView = (ListView) fragmentView.findViewById(R.id.deletedTasksListview);
        deletedTaskListView.setChoiceMode(CHOICE_MODE_MULTIPLE_MODAL);
        listViewAdapter = new ListViewAdapter(getContext(),new ArrayList<Task>());
        deletedTaskListView.setAdapter(listViewAdapter);
    }


    @Override
    public void onTaskComplete(Cursor cursor) {

        Log.i(TAG,"Inside the onTaskComplete: Showing results on Task Lst UI");
        tasks = (ArrayList<Task>) TaskMapper.map(cursor);
        listViewAdapter.setTasks(tasks);
        listViewAdapter.notifyDataSetChanged();
    }
}
