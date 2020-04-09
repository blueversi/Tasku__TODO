package pl.bluepito.goalertodolist.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import pl.bluepito.goalertodolist.Controllers.DatabaseHelper;
import pl.bluepito.goalertodolist.R;
import pl.bluepito.goalertodolist.Models.Category;
import pl.bluepito.goalertodolist.Models.Task;

public class AddTaskFragmentDialog extends DialogFragment {

    private static final String TAG = "AddTaskFragment";
    private Button saveBtn, closeBtn;
    private EditText inputField;
    private DatabaseHelper databaseHelper;
    private Spinner spinner;
    private TextView chooseCategoryTV;
    private List<Category> categories;
    private List<String> catList;
    private List<Integer> catIdList;
    private Task task;

    public AddTaskFragmentDialog(){};
    public AddTaskFragmentDialog(Task task){
        this.task=task;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_fragment_add_task, container, false);

        inputField = (EditText) view.findViewById(R.id.taskNameInputField);
        saveBtn = (Button) view.findViewById(R.id.saveBtn);
        closeBtn = (Button) view.findViewById(R.id.closeBtn);
        spinner = (Spinner) view.findViewById(R.id.chooseCategory);
        chooseCategoryTV = (TextView) view.findViewById(R.id.textView2);
        databaseHelper = new DatabaseHelper(getContext());



        categories = databaseHelper.getAllCategories();
        catList = new ArrayList<>();
        catIdList = new ArrayList<>();

        for (Category item: categories) {
                catList.add(item.getName());
                catIdList.add(item.getId());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, catList);
        spinner.setAdapter(spinnerAdapter);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Add task dialog closed");
                getDialog().dismiss();
            }
        });

        if(this.task !=null){
            inputField.setText(this.task.getTaskName());
            spinner.setVisibility(View.INVISIBLE);
            chooseCategoryTV.setVisibility(View.INVISIBLE);
            final Task tmp = this.task;

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String taskName = inputField.getText().toString();
                    if (taskName.length() != 0) {

                        inputField.setText("");
                        Task tmp2 = tmp;
                        tmp2.setTaskName(taskName);
                        updateTask(tmp2);


                        Log.d(TAG, "Task succesfuly updated");
                    } else {

                        Log.d(TAG, "Failed updating task");
                        toastMsg("Input fields cannot be empty!");
                    }

                    getDialog().dismiss();
                }
            });
        } else {
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String taskName = inputField.getText().toString();
                    if (taskName.length() != 0) {

                        inputField.setText("");
                        String catName = spinner.getSelectedItem().toString();
                        int taskPosition = 0;
                        for (int i = 0; i < catList.size(); i++) {
                            String tmp = catList.get(i);
                            if (tmp.equals(catName)) {
                                taskPosition = i;
                                break;
                            }
                        }
                        int taskId = catIdList.get(taskPosition);

                        addTask(new Task(taskName, 0), taskId);


                        Log.d(TAG, "Task succesfuly added");
                    } else {

                        Log.d(TAG, "Failed adding task");
                        toastMsg("Input fields cannot be empty!");
                    }

                    getDialog().dismiss();
                }
            });
        }

        return view;
    }

    public void addTask(Task newTask, long catId){
        if(catId>0) {
            databaseHelper.addTask(newTask, catId);
        } else {
            databaseHelper.addTask(newTask);
        }
    }

    public void updateTask(Task newTask){
        databaseHelper.updateTask(newTask);
    }


    private void toastMsg(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }
}
