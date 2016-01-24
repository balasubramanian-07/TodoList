package com.flipkart.todolist.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.flipkart.todolist.Constants;
import com.flipkart.todolist.R;
import com.flipkart.todolist.entities.Task;
import com.flipkart.todolist.fragments.TaskDetailFragment;

import java.util.ArrayList;

import static com.flipkart.todolist.Constants.SELECTED_TASK_POSITION_TAG;
import static com.flipkart.todolist.Constants.SINGLE_TASK_TAG;
import static com.flipkart.todolist.Constants.TASKS_ARRAYLIST_TAG;


public class DetailActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ArrayList<Task> tasks;
    private Task task;
    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent launchingIntent = getIntent();
        Bundle bundle = launchingIntent.getExtras();
        tasks = (ArrayList<Task>) bundle.getSerializable(TASKS_ARRAYLIST_TAG);
        selectedPosition = bundle.getInt(SELECTED_TASK_POSITION_TAG);

//        get access to viewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {

                task = tasks.get(position);
                TaskDetailFragment taskDetailFragment = new TaskDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(SINGLE_TASK_TAG,task);
                taskDetailFragment.setArguments(bundle);
                return taskDetailFragment;
            }

            @Override
            public int getCount() {
                return tasks.size();
            }
        });
        viewPager.setCurrentItem(selectedPosition);
    }
}
