package ca.ashleyhasler.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    //private property variables
    private int taskId;
    private String taskTitle;
    private String taskDescription;
    private Date taskDueDate;

    //constructor
    public Task() {
        // initialization
        taskId = -1;
        taskTitle = "";
        taskDescription = "";
        taskDueDate = new Date();
    }

    //overload constructor
    public Task(int id, String title, String description, Date date) {
        // initialization
        taskId = id;
        taskTitle = title;
        taskDescription = description;
        taskDueDate = date;
    }


    public int getTaskId() {
        return taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public Date getTaskDueDate() {
        return taskDueDate;
    }

    public String getStringTaskDueDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(taskDueDate);
    }
}
