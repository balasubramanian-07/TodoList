package com.flipkart.todolist.fragments;




//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
//import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;

import com.flipkart.todolist.Constants;
import com.flipkart.todolist.async_tasks.AddTask;
import com.flipkart.todolist.db.DbGateway;
import com.flipkart.todolist.R;
import com.flipkart.todolist.TodoListApplication;
import com.flipkart.todolist.entities.Task;

import static com.flipkart.todolist.Constants.SINGLE_TASK_TAG;

public class TaskDetailFragment extends Fragment {

    private static final String TAG = "TaskDetailFragment";
    private Task task;
    private TextView dueDate;
    private TextView dueTime;
    private EditText taskTitle;
    private EditText taskNotes;
    private ImageButton FABSaveTask;
    private EditText priority;
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
        setWidgetsData();
        setListeners();

        return fragView;
    }

    private void setWidgetsData() {

        Bundle bundle = getArguments();
        if(bundle != null){
            task = (Task) bundle.getSerializable(SINGLE_TASK_TAG);
            Log.i(TAG, "Populating the Edit view for existing Task with id : " + task.getTask_id());
            taskTitle.setText(task.getTitle());
            taskNotes.setText(task.getNotes());
            dueDate.setText(task.getDate());
            dueTime.setText(task.getDueTime());
            priority.setText(String.valueOf(task.getPriority()));
        }else{
            Log.i(TAG, "No Existing view to update");
        }
    }

    private void setListeners() {
        setSaveButtonListener();
        setDatePickerListener();
        setTimePickerListener();
    }

    private void setSaveButtonListener() {
        FABSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = taskTitle.getText().toString();
                String notes = taskNotes.getText().toString();
                String date = dueDate.getText().toString();
                String time = dueTime.getText().toString();
                String prio = priority.getText().toString();

                if(task == null){
                    task = new Task(title, notes, date, time);
                }else{
                    task.setPriority(Integer.parseInt(prio));
                    task.setDueDate(date);
                    task.setDueTime(time);
                    task.setTitle(title);
                    task.setNotes(notes);
                }

                if(validateTaskInputs(task, prio)){
//              Using Async task to insert/update to db
                    Log.i(TAG, "All validations complete : going to pop backstack");
                    int taskpriority = Integer.parseInt(priority.getText().toString());
                    task.setPriority(taskpriority);
                    insertTaskInDb(task);
                    FragmentManager fragmentManager = getFragmentManager();
                    if(fragmentManager.getBackStackEntryCount() == 0){
                        getActivity().finish();
                        showToastNotification("Tasks Updated : 1");
                    }else{
                        fragmentManager.popBackStack();
                    }
                }
           }
        });
    }

    private boolean validateTaskInputs(Task task, String priority) {

        if(task.getTitle().isEmpty()){
            showToastNotification("Title is mandatory parameter: Please Enter Task Title");
            return false;
        }
        if(task.getDate().isEmpty() || task.getDate() == null){
            showToastNotification("Date is mandatory parameter: Please Enter Date");
            return false;
        }

        if(priority.isEmpty() || priority == null){
            showToastNotification("Enter valid integer Priority: 0/1/2");
            return false;
        }
        return true;
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
                datePickerFragment.show(getFragmentManager(), "datePicker");
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
        FABSaveTask = (ImageButton) fragView.findViewById(R.id.imageButtonSaveTask);
        priority = (EditText) fragView.findViewById(R.id.priorityEditText);
    }

    private void showToastNotification(String msg) {

        Toast message = Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT);
        message.show();
    }
}
