package ca.ashleyhasler.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;
import java.util.Calendar;

public class TaskManager {
    private ArrayList<Task> listOfTasks;
    private Calendar calendar;

    //add singleton reference
    private static TaskManager instance = null;

    //--------------------------singleton "constructor" method
    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    //constructor
    public TaskManager() {
        listOfTasks = new ArrayList<>();
        calendar = Calendar.getInstance();
    }

    //----------------------------------get/set methods
    public void setListOfTasks(ArrayList<Task> myListOfTasks) {
        listOfTasks = myListOfTasks;
    }

    public ArrayList<Task> getListOfTasks() {
        return listOfTasks;
    }

    public void setAlarm(Context context, Task task) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        intent.putExtra("title", task.getTaskTitle());
        intent.putExtra("id", task.getTaskId());

        //sends alarmReceiver the intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getTaskId(), intent, 0);

        //sets the alarm
        alarmManager.set(alarmManager.RTC_WAKEUP, task.getTaskDueDate().getTime(), pendingIntent);
    }

    public Calendar getDate() {
        return calendar;
    }

    public void resetDateTracker() {
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
    }

    public void cancelAlarm(Context context, Task task) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        //sends alarmReceiver the intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getTaskId(), intent, 0);
        alarmManager.cancel(pendingIntent);
    }

}
