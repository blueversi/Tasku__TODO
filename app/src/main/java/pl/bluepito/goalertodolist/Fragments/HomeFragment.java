package pl.bluepito.goalertodolist.Fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.bluepito.goalertodolist.Controllers.DatabaseHelper;
import pl.bluepito.goalertodolist.R;
import pl.bluepito.goalertodolist.Models.Task;
import pl.bluepito.goalertodolist.Models.TasksAdapter;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private DatabaseHelper databaseHelper;
    private ArrayList<Task> taskList;
    private TasksAdapter tasksAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        allTaskView();

        recyclerView = (RecyclerView) view.findViewById(R.id.taskRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        tasksAdapter = new TasksAdapter(view.getContext(), taskList);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(tasksAdapter);

        return view;
    }

    public void allTaskView() {

        taskList = databaseHelper.getAllTasks();

        Log.e(TAG, "Your Tasks has been loaded into listView");

    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.LEFT) {

                AddTaskFragmentDialog dialog = new AddTaskFragmentDialog(taskList.get(viewHolder.getAdapterPosition()));
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "AddTaskFragmentDialog");


                tasksAdapter.notifyDataSetChanged();
            } else if (direction == ItemTouchHelper.RIGHT) {
                System.out.println("LONG ID: " +taskList.get(viewHolder.getAdapterPosition()).getId());
                databaseHelper.deleteTask((long) taskList.get(viewHolder.getAdapterPosition()).getId());
                taskList.remove(viewHolder.getAdapterPosition());
                tasksAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Get RecyclerView item from the ViewHolde
                View itemView = viewHolder.itemView;
                Drawable icon;

                final ColorDrawable background;
                float margin = convertDpToPx(itemView.getContext(), 32);
                int iconHeight, iconWidth, iconTop, iconBottom, iconLeft =0, iconRight =0;
                int cellHeight = itemView.getBottom() - itemView.getTop();

                if (dX > 0) {
                    icon =  ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_delete);
                    assert icon != null;
                    iconHeight = icon.getIntrinsicHeight();
                    iconWidth = icon.getIntrinsicWidth();
                    iconTop = itemView.getTop() +(cellHeight - iconHeight)/2;
                    iconBottom = iconTop + iconHeight;

                    background = new ColorDrawable(getResources().getColor(R.color.watermelon));
                    background.setBounds(0, itemView.getTop(), itemView.getLeft() + (int) dX, itemView.getBottom());

                    iconLeft = (int)margin;
                    iconRight = (int)margin +iconWidth;
                    System.out.println("DELETE LEFT: " + iconLeft);
                    System.out.println("DELETE RIGHT: " + iconRight);


                } else {
                    icon = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_create_task);
                    iconHeight = icon.getIntrinsicHeight();
                    iconWidth = icon.getIntrinsicWidth();
                    iconTop = itemView.getTop() +(cellHeight - iconHeight)/2;
                    iconBottom = iconTop + iconHeight;



                    background = new ColorDrawable(getResources().getColor(R.color.coral));
                    background.setBounds((itemView.getRight() + (int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());


                    iconLeft = itemView.getRight() - (int)margin -iconWidth;
                    iconRight = itemView.getRight() - (int)margin ;
                    System.out.println("EDIT WIDTH: " + iconWidth);
                    System.out.println("EDIT LEFT: " + iconLeft);
                    System.out.println("EDIT RIGHT: " + iconRight);
                }
                background.draw(c);
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                icon.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    };

    public float convertDpToPx(Context context, float dp) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
