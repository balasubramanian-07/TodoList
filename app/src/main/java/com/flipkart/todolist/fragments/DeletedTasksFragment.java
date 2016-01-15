package com.flipkart.todolist.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.flipkart.todolist.R;
import com.flipkart.todolist.adapters.ListViewAdapter;
import com.flipkart.todolist.entities.Task;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeletedTasksFragment extends Fragment {

    private static final String TAG = "DeletedTasksFragment";
    private ListView deletedTaskListView;
    public DeletedTasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "inside DeleteTasksFragment");
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_deleted_tasks, null);
        setWidgets(fragmentView);
        return fragmentView;
    }

    private void setWidgets(View fragmentView) {

        deletedTaskListView = (ListView) fragmentView.findViewById(R.id.deletedTasksListview);
        deletedTaskListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        ListViewAdapter listViewAdapter = new ListViewAdapter(getContext(),new ArrayList<Task>());
        deletedTaskListView.setAdapter(listViewAdapter);
    }


}
