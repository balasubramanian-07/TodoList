package com.flipkart.todolist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TaskDetailFragment extends Fragment {

    private static final String TAG = "TaskDetailFragment";

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "======================= Inside onCreateView of detail fragment");
        return inflater.inflate(R.layout.fragment_task_detail, container, false);
    }




}
