package com.flipkart.todolist;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TaskDetailFragment extends Fragment {

    private static final String TAG = "TaskDetailFragment";
    private Task task;
    private DatePicker datePicker;
    private EditText taskTitle;
    private EditText taskNotes;
    private Button saveTask ;
    private Button cancelTask;
    private Button deleteTask;
    private Spinner setPriority;

    private Bundle bundle;
    private AddTask addTask;
    private DbGateway dbGateway;

    public TaskDetailFragment() {
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
        // Inflate the layout for this fragment
        Log.i(TAG, "Inside onCreateView of detail fragment");

        View fragView = inflater.inflate(R.layout.fragment_task_detail, container, false);
        setWidgets(fragView);
        setListeners();

        return fragView;
    }

    private void setListeners() {
        setSaveButtonListener();
        setCancelButtonListener();
    }

    private void setCancelButtonListener() {
        cancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
            }
        });
    }

    private void setSaveButtonListener() {
        saveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = taskTitle.getText().toString();
                String notes = taskNotes.getText().toString();

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                final String formattedDate = sdf.format(calendar.getTime());
                task = new Task(title, notes, formattedDate, 1);

//              Using Async task to insert/update to db
                insertTaskInDb(task);

                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
            }
        });
    }

    private void insertTaskInDb(Task task) {
        addTask = new AddTask(dbGateway, task);
        addTask.execute();
    }

    private void setWidgets(View fragView) {
        datePicker = (DatePicker) fragView.findViewById(R.id.datePicker);
        taskTitle = (EditText) fragView.findViewById(R.id.editTaskTitle);
        taskNotes = (EditText) fragView.findViewById(R.id.editTaskNotes);
        saveTask =  (Button) fragView.findViewById(R.id.saveTask);
        cancelTask =  (Button) fragView.findViewById(R.id.cancelTask);
    }
}
