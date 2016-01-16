package com.flipkart.todolist.fragments;


import android.app.FragmentManager;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.flipkart.todolist.Constants;
import com.flipkart.todolist.R;
import com.flipkart.todolist.TodoListApplication;
import com.flipkart.todolist.adapters.ListViewAdapter;
import com.flipkart.todolist.async_tasks.ViewTaskList;
import com.flipkart.todolist.db.DbGateway;
import com.flipkart.todolist.delegates.AsyncTaskCompletedListener;
import com.flipkart.todolist.entities.Task;
import com.flipkart.todolist.mappers.TaskMapper;

import java.util.ArrayList;

import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE_MODAL;
import static com.flipkart.todolist.Constants.SHOW_DELETED_TASKS;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeletedTasksFragment extends Fragment implements AsyncTaskCompletedListener<Cursor> {

    private static final String TAG = "DeletedTasksFragment";
    private ListView deletedTaskListView;
    private DbGateway dbGateway;
    private ArrayList<Task> tasks;
    private ListViewAdapter listViewAdapter;

    public DeletedTasksFragment() {
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

        Log.i(TAG, "inside DeleteTasksFragment");
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_deleted_tasks, null);

        setWidgets(fragmentView);
        showDeletedTasksInUI();
        setCustomActionBar();

        return fragmentView;
    }

    private void setCustomActionBar() {

        deletedTaskListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }


    private void showDeletedTasksInUI() {

        ViewTaskList viewTaskList = new ViewTaskList(dbGateway,getActivity().getApplicationContext());
        viewTaskList.setCallback(this);
        viewTaskList.execute(SHOW_DELETED_TASKS);
    }


    private void setWidgets(View fragmentView) {

        deletedTaskListView = (ListView) fragmentView.findViewById(R.id.deletedTasksListview);
        deletedTaskListView.setChoiceMode(CHOICE_MODE_MULTIPLE_MODAL);
        listViewAdapter = new ListViewAdapter(getContext(),new ArrayList<Task>());
        deletedTaskListView.setAdapter(listViewAdapter);
    }


    @Override
    public void onTaskComplete(Cursor cursor) {

        Log.i(TAG,"Inside the onTaskComplete: Showing results on Task Lst UI");
        tasks = (ArrayList<Task>) TaskMapper.map(cursor);
        listViewAdapter.setTasks(tasks);
        listViewAdapter.notifyDataSetChanged();
    }
}
