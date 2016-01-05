package com.flipkart.todolist;

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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static com.flipkart.todolist.Constants.TASK_OBJECT_TAG;

public class TaskListFragment extends Fragment implements AsyncTaskCompletedListener<SimpleCursorAdapter> {

    private static final String TAG = "TaskListFragment";
    private SwitchToAddTodoFragmentDelegate delegate;
    private Button taskDetailButton;
    private ListView taskListView;
    private ViewTaskList viewTaskList;
    private DbGateway dbGateway;
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

    private void showTasksInUI(View fragmentView) {

        viewTaskList = new ViewTaskList(dbGateway, fragmentView, getActivity().getApplicationContext(), this);
        viewTaskList.execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_task_list, null);

        setWidgets(fragmentView);
        setListeners();
        showTasksInUI(fragmentView);

        return fragmentView;
    }

    private void setListeners() {
        setAddTaskButtonListener();
        setTaskListViewListener();
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
                task = new Task(taskTitle, null, taskDueDate, 1);
                goToTaskDetailFragment();
            }
        });
    }

    private void setWidgets(View fragmentView) {
        taskListView = (ListView) fragmentView.findViewById(R.id.taskListView);
        taskDetailButton = (Button) fragmentView.findViewById(R.id.taskDetailButton);
    }

    public void goToTaskDetailFragment() {
        if (delegate != null) {
            delegate.switchFragment();
        }
    }

    @Override
    public void onTaskComplete(SimpleCursorAdapter simpleCursorAdapter) {
        simpleCursorAdapter.notifyDataSetChanged();
    }
}
