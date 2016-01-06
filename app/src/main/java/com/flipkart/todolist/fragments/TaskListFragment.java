package com.flipkart.todolist.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        return fragmentView;
    }

    private void setWidgets(View fragmentView) {

        taskListView = (ListView) fragmentView.findViewById(R.id.taskListView);
        listViewAdapter = new ListViewAdapter(getContext(), new ArrayList<Task>());
        taskListView.setAdapter(listViewAdapter);

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

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Inside onItemClick on list view");
                TextView title = (TextView) view.findViewById(R.id.taskTitle);
                TextView dueDate = (TextView) view.findViewById(R.id.taskDueDate);
                String taskTitle = title.getText().toString();
                String taskDueDate = dueDate.getText().toString();
//                task = new Task(taskTitle, null, taskDueDate, 1);
                goToTaskDetailFragment();
            }
        });
    }

    public void goToTaskDetailFragment() {

        if (delegate != null) {
            delegate.switchFragment();
        }
    }

    @Override
    public void onTaskComplete(Cursor cursor) {

        tasks = (ArrayList<Task>) TaskMapper.map(cursor);
        listViewAdapter.setTasks(tasks);
        listViewAdapter.notifyDataSetChanged();
    }
}
