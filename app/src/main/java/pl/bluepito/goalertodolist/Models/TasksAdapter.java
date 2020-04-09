package pl.bluepito.goalertodolist.Models;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.bluepito.goalertodolist.R;
import pl.bluepito.goalertodolist.Controllers.DatabaseHelper;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyHolder> {

    Context context;
    ArrayList<Task> taskList = new ArrayList<>();
    DatabaseHelper databaseHelper;

    public TasksAdapter(Context context, ArrayList<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_card, viewGroup, false);

        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.idTaskTV.setText(String.valueOf(taskList.get(position).getId()));
        holder.taskNameTV.setText(taskList.get(position).getTaskName());
        holder.catNameTV.setText(databaseHelper.getTaskCategory(taskList.get(position)).getName());
        holder.createdAtTV.setText(taskList.get(position).getInsertDate());


    }

    @Override
    public int getItemCount() {
        return (taskList == null) ? 0 : taskList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView idTaskTV, taskNameTV, catNameTV, createdAtTV;

        public MyHolder(View itemView) {
            super(itemView);
            idTaskTV = itemView.findViewById(R.id.task_id_card);
            taskNameTV = itemView.findViewById(R.id.task_name_card);
            catNameTV = itemView.findViewById(R.id.task_category_card);
            createdAtTV = itemView.findViewById(R.id.task_created_card);
        }

    }


}
