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

    private DatePicker datePicker;
    private EditText taskTitle;
    private EditText taskNotes;
    private Button saveTask ;
    private Button cancelTask;
    private Button deleteTask;
    private Spinner setPriority;

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "Inside onCreateView of detail fragment");

        View fragView = inflater.inflate(R.layout.fragment_task_detail, container, false);
        datePicker = (DatePicker) fragView.findViewById(R.id.datePicker);
        taskTitle = (EditText) fragView.findViewById(R.id.editTaskTitle);
        taskNotes = (EditText) fragView.findViewById(R.id.editTaskNotes);
        saveTask =  (Button) fragView.findViewById(R.id.saveTask);
        cancelTask =  (Button) fragView.findViewById(R.id.cancelTask);

        saveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = taskTitle.getText().toString();
                String notes = taskNotes.getText().toString();

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                final String formattedDate = sdf.format(calendar.getTime());

                Intent intent = new Intent();
                intent.putExtra(Constants.TASK_TITLE, title);
                intent.putExtra(Constants.TASK_NOTES, notes);
                intent.putExtra(Constants.TASK_DUE_DATE,formattedDate);

                getTargetFragment().onActivityResult(Constants.LIST_TO_DETAIL_FRAGMENT_CODE,
                        Activity.RESULT_OK, intent);
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();

            }
        });

        cancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        return fragView;
    }




}
