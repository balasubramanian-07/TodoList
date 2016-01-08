package com.flipkart.todolist.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.flipkart.todolist.db.DbGateway;
import com.flipkart.todolist.R;
import com.flipkart.todolist.TodoListApplication;
import com.flipkart.todolist.async_tasks.ViewTaskList;
import com.flipkart.todolist.adapters.ListViewAdapter;
import com.flipkart.todolist.delegates.AsyncTaskCompletedListener;
import com.flipkart.todolist.delegates.SwitchToAddTodoFragmentDelegate;
import com.flipkart.todolist.entities.Task;
import com.flipkart.todolist.mappers.TaskMapper;

import java.util.ArrayList;

public class TaskListFragment extends Fragment implements AsyncTaskCompletedListener<Cursor> {

    private static final String TAG = "TaskListFragment";
    private SwitchToAddTodoFragmentDelegate delegate;
    private Button taskDetailButton;
    private ListView taskListView;
    private ViewTaskList viewTaskList;
    private DbGateway dbGateway;
    private ListViewAdapter listViewAdapter;
    private ArrayList<Task> tasks;
    private Task task;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_task_list, null);

        setWidgets(fragmentView);
        setListeners();
        showTasksInUI();
        setCustomActionBar();

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
                        deleteTask(item);
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    private void deleteTask(MenuItem item) {
        
    }

    private void setWidgets(View fragmentView) {

        taskListView = (ListView) fragmentView.findViewById(R.id.taskListView);
//        Adding Custom Action Bar using multi choice model listener
        taskListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listViewAdapter = new ListViewAdapter(getContext(), new ArrayList<Task>());

        taskDetailButton = (Button) fragmentView.findViewById(R.id.taskDetailButton);
    }

    private void setListeners() {

        setAddTaskButtonListener();
        setTaskListViewListener();
    }

    private void showTasksInUI() {

        viewTaskList = new ViewTaskList(dbGateway, getActivity().getApplicationContext());
        viewTaskList.setCallback(this);
        viewTaskList.execute();
    }

    private void setAddTaskButtonListener() {

        taskDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTaskDetailFragment();
            }
        });
    }

    private void setTaskListViewListener() {
        Log.i(TAG,"Inside setTaskListViewListener");
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Inside onItemClick on list view");
                TextView title = (TextView) view.findViewById(R.id.taskTitle);
//                TextView dueDate = (TextView) view.findViewById(R.id.dueDateTextView);
//                TextView dueTime = (TextView) view.findViewById(R.id.dueTimeTextView);
//                TextView notes = (TextView) view.findViewById(R.id.editTaskNotes);
                String taskTitle = title.getText().toString();
//                String taskDueDate = dueDate.getText().toString();
//                String taskDueTime = dueTime.getText().toString();
//                String taskNotes = notes.getText().toString();
//                task = new Task(taskTitle, taskNotes, taskDueDate,taskDueTime, 1);
                goToTaskDetailFragment();
            }
        });
        taskListView.setClickable(true);
        taskListView.setAdapter(listViewAdapter);
    }

    public void goToTaskDetailFragment() {

        if (delegate != null) {

            delegate.switchFragment();
        }
    }

    @Override
    public void onTaskComplete(Cursor cursor) {

        Log.i(TAG,"Inside the onTaskComplete: Showing results on Task Lst UI");
        tasks = (ArrayList<Task>) TaskMapper.map(cursor);
        listViewAdapter.setTasks(tasks);
        listViewAdapter.notifyDataSetChanged();
    }
}
