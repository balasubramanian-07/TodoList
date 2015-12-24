package com.flipkart.todolist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;


public class TaskListFragment extends Fragment {

    private SwitchToAddTodoFragmentDelegate delegate;
    private Button taskDetailButton;
    private ListView taskListView;

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

    public void taskDetail() {
        if ( delegate != null) {
            delegate.switchFragment();
        }
    }
}
