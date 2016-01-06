package com.flipkart.todolist.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.flipkart.todolist.R;
import com.flipkart.todolist.delegates.SwitchToAddTodoFragmentDelegate;
import com.flipkart.todolist.fragments.TaskDetailFragment;
import com.flipkart.todolist.fragments.TaskListFragment;

import static com.flipkart.todolist.Constants.LIST_TO_DETAIL_FRAGMENT_CODE;
import static com.flipkart.todolist.Constants.TASK_DETAIL_FRAGMENT_TAG;
import static com.flipkart.todolist.Constants.TASK_LIST_FRAGMENT_TAG;

public class MainActivity extends AppCompatActivity implements SwitchToAddTodoFragmentDelegate {

    private static final String TAG = "MainActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            loadTodoListFragment();
        }
        // TODO: Need to handle case when device is rotated
    }

    private void loadTodoListFragment() {
        TaskListFragment taskListFragment = new TaskListFragment();
        taskListFragment.setDelegate(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_layout, taskListFragment, TASK_LIST_FRAGMENT_TAG);
        transaction.commit();
    }

    @Override
    public void switchFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        TaskListFragment taskListFragment = (TaskListFragment) fragmentManager.findFragmentByTag(TASK_LIST_FRAGMENT_TAG);

        TaskDetailFragment taskDetailFragment = new TaskDetailFragment();

        if (taskListFragment != null) {
            taskDetailFragment.setTargetFragment(taskListFragment, LIST_TO_DETAIL_FRAGMENT_CODE);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(taskListFragment);
            transaction.add(R.id.main_layout, taskDetailFragment, TASK_DETAIL_FRAGMENT_TAG);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}