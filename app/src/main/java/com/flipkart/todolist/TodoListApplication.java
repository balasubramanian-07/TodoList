package com.flipkart.todolist;

import android.app.Application;

/**
 * Created by mayank.gupta on 30/12/15.
 */
public class TodoListApplication extends Application {
    public DbGateway dbGateway;

    @Override
    public void onCreate() {
        super.onCreate();

        dbGateway = new DbGateway(getApplicationContext());
    }
}
