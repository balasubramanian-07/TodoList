package com.flipkart.todolist.delegates;

/**
 * Created by mayank.gupta on 31/12/15.
 */
public interface AsyncTaskCompletedListener<T> {
    public void onTaskComplete(T t );
}
