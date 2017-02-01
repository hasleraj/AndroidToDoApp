package ca.ashleyhasler.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnAddNewTask;
    private MainPresenter mainPresenter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bundle database with this app on first visit
        DatabaseBundler.bundle(this, "tasksdb.db");

        //get reference and set up listeners for buttons
        btnAddNewTask = (ImageButton) findViewById(R.id.btnAddNewTask);
        btnAddNewTask.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                mainPresenter.showFormActivity();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mainPresenter = new MainPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainPresenter.updateTasks();
    }

    public void showTasks(ArrayList<Task> tasks) {
        // specify an adapter
        mAdapter = new TaskAdapter(tasks, mainPresenter);
        mRecyclerView.setAdapter(mAdapter);
    }
}