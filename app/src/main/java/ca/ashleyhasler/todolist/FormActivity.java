package ca.ashleyhasler.todolist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class FormActivity extends AppCompatActivity {

    private EditText txtTaskTitle;
    private EditText txtTaskDescription;
    private TextView txtDueDate;
    private TextView txtDueTime;
    private ImageButton btnAddTask;
    private ImageButton btnCancel;
    private ImageButton btnDueDate;
    private ImageButton btnDueTime;
    private MainPresenter formPresenter;
    private Toast errorToast;
    private Toast successToast;
    protected Calendar dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        //set up references
        txtTaskTitle = (EditText) findViewById(R.id.txtTaskTitle);
        txtTaskTitle.requestFocus();
        txtTaskDescription = (EditText) findViewById(R.id.txtTaskDescription);
        txtDueDate = (TextView) findViewById(R.id.txtDueDate);
        txtDueTime = (TextView) findViewById(R.id.txtDueTime);
        btnAddTask = (ImageButton) findViewById(R.id.btnAddTask);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //feedback to user if they don't enter a title
                if (txtTaskTitle.getText().toString().isEmpty()){
                    showErrorToast();
                } else {
                    Log.d("ashley", txtTaskTitle.getText().toString());
                    formPresenter.addATask(
                            txtTaskTitle.getText().toString(),
                            txtTaskDescription.getText().toString(),
                            dueDate.getTime()
                    );
                }
            }
        });
        btnCancel = (ImageButton) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                formPresenter.closeFormActivity();
            }
        });
        btnDueDate = (ImageButton) findViewById(R.id.btnDueDate);
        btnDueDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                formPresenter.showDatePicker();
            }
        });
        btnDueTime = (ImageButton) findViewById(R.id.btnDueTime);
        btnDueTime.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                formPresenter.showTimePicker();
            }
        });

        //construct toast
        errorToast = Toast.makeText(this, this.getString(R.string.error), Toast.LENGTH_LONG);
        successToast = Toast.makeText(this, this.getString(R.string.success), Toast.LENGTH_LONG);


        formPresenter = new MainPresenter(this);
        dueDate = formPresenter.getDateTracker();

        updateTime();
        updateDate();
    }

    public void updateTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        txtDueTime.setText(formatter.format(dueDate.getTime()));
    }

    public void updateDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("MMM, dd, yyyy");
        txtDueDate.setText(formatter.format(dueDate.getTime()));
    }

    public void showErrorToast() {
        errorToast.show();
    }

    public void showSuccessToast() {
        successToast.show();
    }
    //---------------------------------------------------------inner class definitions

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            FormActivity activity = (FormActivity) getActivity();
            int hour = activity.dueDate.get(Calendar.HOUR_OF_DAY);
            int minute = activity.dueDate.get(Calendar.MINUTE);

            return new TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
            FormActivity activity = (FormActivity) getActivity();
            activity.dueDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            activity.dueDate.set(Calendar.MINUTE, minuteOfDay);
            activity.updateTime();
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            FormActivity activity = (FormActivity) getActivity();
            int day = activity.dueDate.get(Calendar.DAY_OF_MONTH);
            int month = activity.dueDate.get(Calendar.MONTH);
            int year = activity.dueDate.get(Calendar.YEAR);

            return new DatePickerDialog(activity, this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            FormActivity activity = (FormActivity) getActivity();
            activity.dueDate.set(Calendar.YEAR, year);
            activity.dueDate.set(Calendar.MONTH, month);
            activity.dueDate.set(Calendar.DAY_OF_MONTH, day);
            activity.updateDate();
        }
    }
}