package com.flipkart.todolist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.flipkart.todolist.R;
import com.flipkart.todolist.Task;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Task> tasks;

    public ListViewAdapter(Context context, List<Task> tasks) {

        this.context = context;
        this.tasks = tasks;
    }


    @Override
    public int getCount() {

        return tasks.size();
    }

    @Override
    public Object getItem(int position) {

        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View recycledView, ViewGroup parent) {

        View mainView = recycledView != null ? recycledView : createView(LayoutInflater.from(context));

        Task task = tasks.get(position);
        ViewHolder viewHolder = (ViewHolder) mainView.getTag();
        viewHolder.taskTitle.setText(task.getTitle());
        viewHolder.taskDueDate.setText(task.getDate());
        viewHolder.taskPriority.setText(task.getPriority());

        return mainView;
    }

    private View createView(LayoutInflater inflater) {

        View view = inflater.inflate(R.layout.row, null);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.taskTitle = (TextView) view.findViewById(R.id.taskTitle);
        viewHolder.taskDueDate = (EditText) view.findViewById(R.id.taskDueDate);
        viewHolder.taskPriority = (TextView) view.findViewById(R.id.taskPriority);

        view.setTag(viewHolder);

        return view;
    }

    private static class ViewHolder {

        TextView taskTitle;
        EditText taskDueDate;
        TextView taskPriority;
    }
}
