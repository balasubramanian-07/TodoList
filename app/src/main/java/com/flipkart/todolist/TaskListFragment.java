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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class TaskListFragment extends Fragment implements AsyncTaskCompletedListener<SimpleCursorAdapter> {

    private static final String TAG = "TaskListFragment";
    private SwitchToAddTodoFragmentDelegate delegate;
    private Button taskDetailButton;
    private ListView taskListView;
    private ViewTaskList viewTaskList;
    private DbGateway dbGateway;
    private Bundle bundle;


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

        dbGateway = ((TodoListApplication) getActivity().getApplication()).dbGateway;
        viewTaskList = new ViewTaskList(dbGateway,fragmentView,getActivity().getApplicationContext(),this);
        viewTaskList.execute();

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return fragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void taskDetail() {
        if ( delegate != null) {
            delegate.switchFragment(bundle);
        }
    }

    @Override
    public void onTaskComplete(SimpleCursorAdapter simpleCursorAdapter) {
        simpleCursorAdapter.notifyDataSetChanged();
    }
}
