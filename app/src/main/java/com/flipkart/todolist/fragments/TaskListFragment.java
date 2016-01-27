package com.flipkart.todolist.fragments;

//import android.app.FragmentManager;
//import android.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flipkart.todolist.R;
import com.flipkart.todolist.TodoListApplication;
import com.flipkart.todolist.activities.DetailActivity;
import com.flipkart.todolist.adapters.ListViewAdapter;
import com.flipkart.todolist.async_tasks.ViewTaskList;
import com.flipkart.todolist.db.DbGateway;
import com.flipkart.todolist.db.TaskTable;
import com.flipkart.todolist.delegates.AsyncTaskCompletedListener;
import com.flipkart.todolist.delegates.SwitchToAddTodoFragmentDelegate;
import com.flipkart.todolist.entities.Task;
import com.flipkart.todolist.mappers.TaskMapper;

import java.util.ArrayList;

import static com.flipkart.todolist.Constants.APP_LAUNCHER_VIEW_QUERY_TAG;
import static com.flipkart.todolist.Constants.DELETED_TASK_LIST_FRAGMENT;
import static com.flipkart.todolist.Constants.SELECTED_TASK_POSITION_TAG;
import static com.flipkart.todolist.Constants.SORT_BY_DATE;
import static com.flipkart.todolist.Constants.SORT_BY_PRIORITY;
import static com.flipkart.todolist.Constants.TASKS_ARRAYLIST_TAG;
import static com.flipkart.todolist.Constants.TASK_LIST_FRAGMENT_TAG;

//import android.app.Fragment;

public class TaskListFragment extends Fragment implements AsyncTaskCompletedListener<Cursor> {

    private static final String TAG = "TaskListFragment";
    private SwitchToAddTodoFragmentDelegate delegate;
    private ImageButton FAB;
    private ListView taskListView;
    private ViewTaskList viewTaskList;
    private DbGateway dbGateway;
    private ListViewAdapter listViewAdapter;
    private ArrayList<Task> tasks;
    private SQLiteDatabase database;
    private Task task;
    private final String customTaskDelete = "delete";
    private final String customTaskComplete = "complete";
    private ActionMode actionMode;

    public void setDelegate(SwitchToAddTodoFragmentDelegate delegate) {
         this.delegate = delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        dbGateway = ((TodoListApplication) getActivity().getApplication()).dbGateway;
    }

    @Override
    public void onResume() {

        super.onResume();
        Log.i(TAG, "Inside onResume");
        showTasksInUI();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_task_list, null);
        Log.i(TAG, "Inside onCreatView");

        setWidgets(fragmentView);
        setListeners();
        showTasksInUI();
        setCustomActionBar();
        setHasOptionsMenu(true);

        return fragmentView;
    }

    private void setCustomActionBar() {
        taskListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//                Define what to show on the Custom Action Bar when items are selected.
                // Here you can do something when items are selected/de-selected,
                // such as update the title in the CAB
                final int selectedItemCount = taskListView.getCheckedItemCount();
                mode.setTitle("Selection: " + selectedItemCount + " items");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                Define the menu layout for the action bar
                mode.getMenuInflater().inflate(R.menu.custom_action_bar, menu);
                actionMode = mode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                Log.i(TAG, "Inside onPrepare");
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                Define actions on click on each item defined in Custom Action Bar
                switch (item.getItemId()) {
                    case R.id.delete:
                        executeCustomAction(customTaskDelete);
                        mode.finish();
                        showTasksInUI();
                        return true;

                    case R.id.completeTask:
                        executeCustomAction(customTaskComplete);
                        mode.finish();
                        showTasksInUI();
                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

                Log.i(TAG, "onDestroyAction mode called");
                actionMode = null;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.launchDeletedTaskshMenuItem:
                switchToDeleteTaskFragment();
                return true;
            case R.id.sortTaskMenuItem:
                searchTasks(SORT_BY_PRIORITY);
                return true;
            case R.id.sortTaskByDateMenuItem:
                searchTasks(SORT_BY_DATE);
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //Destroy action mode
        if(actionMode != null)
            actionMode.finish();
    }


    private void searchTasks(String param) {

        ViewTaskList viewTaskList = new ViewTaskList(dbGateway,getActivity().getApplicationContext());
        viewTaskList.setCallback(this);
        Log.i(TAG, "Executing app launch query :" + param);
        viewTaskList.execute(param);
    }

    private String getTaskTitle(View view) {

        TextView title = (TextView) view.findViewById(R.id.taskTitle);
        String  taskTitle = title.getText().toString();
        Log.i(TAG, "The Task Title is: " + taskTitle);
        return  taskTitle;
    }

    private void executeCustomAction(String actionType){
        int numOfSelectedTasks = 0;
        SparseBooleanArray selectedItemPositions = taskListView.getCheckedItemPositions();
        for (int i = 0; i < taskListView.getCount(); i++) {
            if (selectedItemPositions.get(i)) {
                numOfSelectedTasks = numOfSelectedTasks + 1;
                View view = taskListView.getChildAt(i);
                String taskTitle = getTaskTitle(view);
                switch (actionType){
                    case customTaskDelete:
                        deleteTaskWithTitle(taskTitle);
                        break;
                    case customTaskComplete:
                        completeTaskWithTitle(taskTitle);
                        break;
                }
            }
        }
        switch (actionType){
            case customTaskDelete:
                showToastNotification("Tasks Deleted : " + String.valueOf(numOfSelectedTasks));
                break;
            case customTaskComplete:
                showToastNotification("Tasks Completed : " + String.valueOf(numOfSelectedTasks));
                break;
        }
    }

    private void showToastNotification(String msg) {

        Toast message = Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT);
        message.show();
    }

    private void completeTaskWithTitle(String taskTitle) {
        String updateTaskStatusToCompleted = TaskTable.completeTaskQuery(taskTitle);
        executeDbQuery(updateTaskStatusToCompleted);
    }

    private void deleteTaskWithTitle(String taskTitle) {

        String deleteQuery = TaskTable.deleteTaskQuery(taskTitle);
        executeDbQuery(deleteQuery);
    }

    private void executeDbQuery(String query){

        database = dbGateway.getWritableDatabase();
        database.execSQL(query);
    }
    private void setWidgets(View fragmentView) {

        taskListView = (ListView) fragmentView.findViewById(R.id.taskListView);
//        Adding Custom Action Bar using multi choice model listener
        taskListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listViewAdapter = new ListViewAdapter(getContext(), new ArrayList<Task>());
        taskListView.setAdapter(listViewAdapter);
        FAB = (ImageButton) fragmentView.findViewById(R.id.imageButton);
    }

    private void setListeners() {

        setAddTaskButtonListener();
        setTaskListViewListener();
    }

    private void showTasksInUI() {

        searchTasks(APP_LAUNCHER_VIEW_QUERY_TAG);
    }

    private void setAddTaskButtonListener() {

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTaskDetailFragment();
            }
        });
    }

    private void setTaskListViewListener() {

        Log.i(TAG, "Inside setTaskListViewListener");
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Inside onItemClick on list view");
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(TASKS_ARRAYLIST_TAG,tasks);
                bundle.putInt(SELECTED_TASK_POSITION_TAG, position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void goToTaskDetailFragment() {

        if (delegate != null) {
            delegate.switchFragment();
        }
    }

    private void switchToDeleteTaskFragment(){

        FragmentManager fragmentManager = getFragmentManager();
        TaskListFragment taskListFragment = (TaskListFragment) fragmentManager.findFragmentByTag(TASK_LIST_FRAGMENT_TAG);
        DeletedTasksFragment deletedTasksFragment = new DeletedTasksFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(taskListFragment);
        transaction.add(R.id.main_layout,deletedTasksFragment, DELETED_TASK_LIST_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onTaskComplete(Cursor cursor) {

        Log.i(TAG,"Inside the onTaskComplete: Showing results on Task Lst UI");
        tasks = (ArrayList<Task>) TaskMapper.map(cursor);
        listViewAdapter.setTasks(tasks);
        listViewAdapter.notifyDataSetChanged();
    }
}
