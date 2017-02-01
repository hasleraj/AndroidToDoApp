package ca.ashleyhasler.todolist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainPresenter {
    // class constants
    private final int DATABASE_VERSION = 1;

    private MainActivity mainView;
    private FormActivity formView;
    private TaskManager taskManager;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    //constructor
    public MainPresenter(MainActivity myMainView) {
        mainView = myMainView;
        initializeVariables();
    }

    //overload constructor
    public MainPresenter (FormActivity myFormView) {
        initializeVariables();
        formView = myFormView;
    }

    public void initializeVariables() {
        //construct/ or get reference to our model via the singleton design pattern
        taskManager = TaskManager.getInstance();
        dbHelper = null;
        db = null;
    }

    public void showFormActivity(){
        //gets the calendar instance that has the date
        taskManager.resetDateTracker();
        // construct intent object and start activity
        Intent intent = new Intent("ca.ashleyhasler.todolist.HELPPOPUP");
        mainView.startActivity(intent);
    }

    public void closeFormActivity(){
        formView.finish();
    }

    //checks the active context, used to set and cancel the alarm
    private Context getActiveContext() {
        if (formView != null) {
            return formView.getApplicationContext();
        } else {
            return mainView.getApplicationContext();
        }
    }

    //gets calendar instance with date
    public Calendar getDateTracker() {
        return taskManager.getDate();
    }

    //show list of tasks in output textbox
    public void displayTasks() {
        mainView.showTasks(taskManager.getListOfTasks());
    }

    //get and display all tasks
    public void updateTasks(){
        AsyncRequest asyncRequest = new AsyncRequest(new Callback() {
            @Override
            public void call(Cursor result) {
                ArrayList<Task> aryTaskList = new ArrayList<>();
                SimpleDateFormat formatter = new SimpleDateFormat(Task.DATE_FORMAT);
                //result will be null if db is empty
                if (result != null && result.moveToFirst()) {
                    do {
                        //formatter.parse can fail so we need to do a try/catch
                        try {
                            Task t = new Task(
                                    result.getInt(0),
                                    result.getString(1),
                                    result.getString(2),
                                    formatter.parse(result.getString(3)));
                            aryTaskList.add(t);
                        } catch (Exception e) {
                            Log.d("Ashley", "Do nothing and move on");
                        }
                    } while (result.moveToNext());
                }
                taskManager.setListOfTasks(aryTaskList);
                displayTasks();
            }
        });
        asyncRequest.execute("SELECT * FROM tblTasks;");
    }

    //add a new task
    public void addATask(final String title, final String description, final Date dueDate){
        SimpleDateFormat formatter = new SimpleDateFormat(Task.DATE_FORMAT);

        AsyncRequest asyncRequest = new AsyncRequest(new Callback() {
            @Override
            public void call(Cursor result) {
                result.moveToFirst();
                //column 0 in the result row, this is the id of the task we just added
                Task task = new Task(result.getInt(0), title, description, dueDate);
                taskManager.setAlarm(getActiveContext(), task);
                formView.showSuccessToast();
                closeFormActivity();
            }
        });

        asyncRequest.execute("INSERT INTO tblTasks VALUES (null, ?, ?, ?);", title, description, formatter.format(dueDate));
    }

    //deletes task from the db
    public void delete(Task task) {
        AsyncRequest asyncRequest = new AsyncRequest(new Callback() {
            @Override
            public void call(Cursor result) {
                updateTasks();
            }
        });
        taskManager.cancelAlarm(getActiveContext(), task);
        asyncRequest.execute("DELETE FROM tblTasks WHERE taskId = ?;", String.valueOf(task.getTaskId()));
    }

    //displays the time picker
    public void showTimePicker(){
        FormActivity.TimePickerFragment timePicker = new FormActivity.TimePickerFragment();
        timePicker.show(formView.getSupportFragmentManager(), "timePicker");
    }

    //displays the date picker
    public void showDatePicker(){
        FormActivity.DatePickerFragment datePicker = new FormActivity.DatePickerFragment();
        datePicker.show(formView.getSupportFragmentManager(), "datePicker");
    }

    //---------------------------------------------------------inner class definitions
    //how you do callbacks in java
    interface Callback {
        void call(Cursor result);
    }

    public class AsyncRequest extends AsyncTask<String, Void, Cursor> {
        Callback onComplete;

        //constructor
        public AsyncRequest(Callback myOnComplete) {
            onComplete = myOnComplete;
        }

        private void open(Context context) {
            // construct SQLiteOpenHelper object
            dbHelper = new DBHelper(context);
            // get reference to SQLite db for both reading and writing
            db = dbHelper.getWritableDatabase();
        }

        @Override
        protected Cursor doInBackground(String... params) {
            String[] queryParams = new String[params.length - 1];
            Cursor result = null;
            String query;

            if(params.length == 0) {
                //check if sql statement was sent in
                return null;
            }else if(params.length > 1) {
                //put params in their own array
                for (int i = 1; i < params.length; i++) {
                    queryParams[i-1] = params[i];
                }
            }

            query = params[0];

            try {
                open(getActiveContext());
                if(query.startsWith("SELECT")) {
                    result = db.rawQuery(query, queryParams);
                } else {
                    db.execSQL(query, queryParams);
                    //getting the id of row that was just inserted
                    //so callback has access to it
                    if (query.startsWith("INSERT")) {
                        result = db.rawQuery("SELECT last_insert_rowid()", null);
                    }
                }
                return result;
            } catch (Exception e) {
                Log.d("ashley", "!!! Exception", e);
                return null;
            }
        }

        protected void onPostExecute(Cursor queryResult) {
            //this will happen in ui thread
            onComplete.call(queryResult);

            //onComplete call can destroy presenter and if so db is null because it has been destroyed
            if (db != null) {
                db.close();
            }
        }
    }

    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, DatabaseBundler.name, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
