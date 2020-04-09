package pl.bluepito.goalertodolist.Controllers;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;

import pl.bluepito.goalertodolist.Models.Task;

public class Controller {

    private static final String TAG = "Controller";
    private Context context;
    private DatabaseHelper databaseHelper;
    private HashMap<String, Integer> allCategories;


    public Controller() {
    }

    public Controller(Context context) {
        this.context = context;
        this.databaseHelper = new DatabaseHelper((context));
    }


    //Methods

    //CATEGORIES METHODS

    // TASK METHODS

    public void getAllTask(ListView taskList) {
        Log.d(TAG, ": Your Tasks has been loaded");

        List<Task> allTaskList = databaseHelper.getAllTasks();

        ListAdapter adapter = new ArrayAdapter<>(this.context, android.R.layout.simple_list_item_1, allTaskList);
        taskList.setAdapter(adapter);
    }

    // MENU METHODS



}
