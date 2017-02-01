package ca.ashleyhasler.todolist;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{
    List<Task> tasks;
    MainPresenter presenter;

    //taskViewHolder represents one card displaying one task from TaskManager Arraylist of Tasks, this gets stored in listTask<task> below
    //inner taskViewHolder class
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView taskTitle;
        TextView taskDescription;
        ImageButton btnDelete;

        TaskViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cardView);
            taskTitle = (TextView)itemView.findViewById(R.id.taskTitle);
            taskDescription = (TextView)itemView.findViewById(R.id.taskDescription);
            btnDelete = (ImageButton)itemView.findViewById(R.id.btnDelete);
        }
    }

    //constructor
    //takes a list of tasks so there are tasks to be displayed
    TaskAdapter(List<Task> myTasks, MainPresenter myPresenter){
        presenter = myPresenter;
        this.tasks = myTasks;
    }

    @Override
    public int getItemCount() {
        //gets the number of tasks
        return tasks.size();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //generates instance of taskViewHolder which is called every card
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_item, viewGroup, false);
        TaskViewHolder tvh = new TaskViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder taskViewHolder, int i) {
        //populates with the guts from task list
        final Task currentTask = tasks.get(i);
        taskViewHolder.taskTitle.setText(currentTask.getTaskTitle());
        taskViewHolder.taskDescription.setText(currentTask.getTaskDescription());
        taskViewHolder.btnDelete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                presenter.delete(currentTask);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        // calls original and must be overridden
        super.onAttachedToRecyclerView(recyclerView);
    }
}
