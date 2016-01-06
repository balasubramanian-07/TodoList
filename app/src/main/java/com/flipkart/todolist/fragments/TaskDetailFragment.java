package com.flipkart.todolist.fragments;



import android.app.Fragment;
import android.os.Bundle;
//import android.app.Fragment;
//import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.flipkart.todolist.async_tasks.AddTask;
import com.flipkart.todolist.db.DbGateway;
import com.flipkart.todolist.R;
import com.flipkart.todolist.TodoListApplication;
import com.flipkart.todolist.entities.Task;

public class TaskDetailFragment extends Fragment {

    private static final String TAG = "TaskDetailFragment";
    private Task task;
    private TextView dueDate;
    private TextView dueTime;
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
        setDatePickerListener();
        setTimePickerListener();
    }

    private void setSaveButtonListener() {
        saveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = taskTitle.getText().toString();
                String notes = taskNotes.getText().toString();
                String date = dueDate.getText().toString();
                String time = dueTime.getText().toString();


                task = new Task(title, notes, date,time, 1);

//              Using Async task to insert/update to db
                insertTaskInDb(task);

                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
            }
        });
    }

    private void setTimePickerListener() {
        dueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setTextView(dueTime);
                timePickerFragment.show( getFragmentManager(), "timePicker");
            }
        });
    }

    private void setDatePickerListener() {
        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setTextView(dueDate);
                datePickerFragment.show( getFragmentManager(), "datePicker");
            }
        });

    }

    private void setCancelButtonListener() {
        cancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
            }
        });
    }

    private void insertTaskInDb(Task task) {
        addTask = new AddTask(dbGateway, task);
        addTask.execute();
    }

    private void setWidgets(View fragView) {
        dueDate = (TextView) fragView.findViewById(R.id.dueDateTextView);
        dueTime = (TextView) fragView.findViewById(R.id.dueTimeTextView);
        taskTitle = (EditText) fragView.findViewById(R.id.editTaskTitle);
        taskNotes = (EditText) fragView.findViewById(R.id.editTaskNotes);
        saveTask =  (Button) fragView.findViewById(R.id.saveTask);
        cancelTask =  (Button) fragView.findViewById(R.id.cancelTask);
    }
}
