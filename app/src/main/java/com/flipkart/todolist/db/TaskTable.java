package com.flipkart.todolist.db;

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
    public enum ValidStatus {
        CREATED("created"), COMPLETED("completed"), SOFT_DELETED("soft_deleted");

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

    public static String deleteTaskQuery(String taskTitle) {
        String deleteQuery = "DELETE FROM " + TASK_TABLE_NAME
                + " WHERE " + TITLE
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
        String readQuery =  "SELECT * FROM "
                            +TASK_TABLE_NAME
                            +" WHERE " + STATUS + " ="
                            +"\"" + ValidStatus.CREATED + "\""
                            +" ORDER BY " + DUE_DATE;

        return readQuery;
    }
}
