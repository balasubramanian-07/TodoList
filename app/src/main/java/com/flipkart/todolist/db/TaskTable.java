package com.flipkart.todolist.db;

import android.util.Log;

import com.flipkart.todolist.Constants;

public final class TaskTable {
    public static final String TASK_TABLE_NAME = "tasks";
    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String NOTES = "notes";
    public static final String PRIORITY = "priority";
    public static final String DUE_DATE = "due_date";
    public static final String DUE_TIME = "due_time";
    public static final String STATUS = "status";
    public static final String CREATED_AT = "created_at";


    public static String createTableQuery(){
        return "CREATE TABLE " + TASK_TABLE_NAME
                + "(" + ID + " INTEGER PRIMARY KEY"
                + "," + TITLE + " TEXT NOT NULL"
                + "," + NOTES + " TEXT NOT NULL"
                + "," + DUE_DATE + " TEXT NOT NULL"
                + "," + DUE_TIME + " TEXT NOT NULL"
                + "," + PRIORITY + " INT NOT NULL"
                + "," + STATUS + " TEXT NOT NULL"
                + "," + CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";
    }

    public static String permanentDeleteTaskQuery(String taskTitle) {

        String deleteQuery = "UPDATE " + TASK_TABLE_NAME
                + " SET  " + STATUS + "=" + "\"" + ValidStatus.DELETED + "\" "
                + "WHERE " + TITLE
                + " = \"" + taskTitle
                + "\"";

        return deleteQuery;
    }

    public static String undoDeleteTaskQuery(String taskTitle) {

        String undoDeleteQuery = "UPDATE " + TASK_TABLE_NAME
                + " SET  " + STATUS + "=" + "\"" + ValidStatus.CREATED + "\" "
                + "WHERE " + TITLE
                + " = \"" + taskTitle
                + "\"";

        return undoDeleteQuery;
    }

    public enum ValidStatus {
        CREATED("created"), COMPLETED("completed"), SOFT_DELETED("soft_deleted"), DELETED("deleted");

        private final String value;

        ValidStatus(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return getValue();
        }

    }
    public static String sortTasksByPriority() {

        String sortOnPriorityQuery = selectQueryForStatusCreated();
        sortOnPriorityQuery = sortOnPriorityQuery + PRIORITY;
        return sortOnPriorityQuery;
    }

    public static String showRecycledBinTasks() {

        String showRecycledTasks =
                "SELECT * FROM "
                +TASK_TABLE_NAME
                +" WHERE " + STATUS + " ="
                +"\"" + ValidStatus.SOFT_DELETED + "\""
                +" ORDER BY " + DUE_DATE;

        return  showRecycledTasks;
    }

    private static String selectQueryForStatusCreated(){
        String readQuery =  "SELECT * FROM "
                +TASK_TABLE_NAME
                +" WHERE " + STATUS + " ="
                +"\"" + ValidStatus.CREATED + "\""
                +" ORDER BY ";

        return readQuery;
    }

    public static String deleteTaskQuery(String taskTitle) {
        String deleteQuery = "UPDATE " + TASK_TABLE_NAME
                + " SET  " + STATUS + "=" + "\"" + ValidStatus.SOFT_DELETED + "\" "
                + "WHERE " + TITLE
                + " = \"" + taskTitle
                + "\"";

        return deleteQuery;
    }

    public static String completeTaskQuery(String taskTitle){
        String completeQuery = "UPDATE " + TASK_TABLE_NAME
                + " SET  " + STATUS + "=" + "\"" + ValidStatus.COMPLETED + "\" "
                + "WHERE " + TITLE
                + " = \"" + taskTitle
                + "\"";

        return completeQuery;
    }

    public static String showTasksOnAppLaunch(){

        String readQuery =  selectQueryForStatusCreated() + DUE_DATE;

        Log.i("Inside App launch query", readQuery);
        return readQuery;
    }
}
