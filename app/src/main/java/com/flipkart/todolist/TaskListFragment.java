package com.flipkart.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class TaskListFragment extends Fragment {

    private static final String TAG = "TaskListFragment";
    private SwitchToAddTodoFragmentDelegate delegate;
    private Button taskDetailButton;
    private ListView taskListView;
    private ArrayList<Task> taskList;

    public void setDelegate(SwitchToAddTodoFragmentDelegate delegate) {

        this.delegate = delegate;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_task_list, null);
        taskListView = (ListView) fragmentView.findViewById(R.id.taskListView);
        taskDetailButton = (Button) fragmentView.findViewById(R.id.taskDetailButton);

        taskDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskDetail();
            }
        });
        return fragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == Constants.LIST_TO_DETAIL_FRAGMENT_CODE) && (resultCode == Activity.RESULT_OK)) {

            taskList = getTaskList();
//            TODO: This code will be removed, this is just to check functionality
            String title = data.getStringExtra(Constants.TASK_TITLE);
            String notes = data.getStringExtra(Constants.TASK_NOTES);
            String formattedDate =  data.getStringExtra(Constants.TASK_DUE_DATE);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = sdf.parse(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Task task = new Task(title, notes, date, 1);
            taskList.add(task);
            Log.i(TAG, "Task Got Added: " + taskList.get(0).getTitle());
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void taskDetail() {
        if ( delegate != null) {
            delegate.switchFragment();
        }
    }

    private ArrayList<Task> getTaskList() {
        if (taskList == null) {
            taskList = new ArrayList<Task>();
        }
        return taskList;
    }

}
