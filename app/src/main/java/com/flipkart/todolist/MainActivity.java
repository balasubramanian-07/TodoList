package com.flipkart.todolist;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements SwitchToAddTodoFragmentDelegate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            loadTodoListFragment();
         }
    }

    private void loadTodoListFragment() {

        TodoListFragment todoListFragment = new TodoListFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_layout, todoListFragment, "TLF");
        transaction.commit();
    }

    @Override
    public void switchFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();


    }
}
