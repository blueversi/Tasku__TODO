package pl.bluepito.goalertodolist.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import pl.bluepito.goalertodolist.MainActivity;
import pl.bluepito.goalertodolist.R;
import pl.bluepito.goalertodolist.Controllers.DatabaseHelper;
import pl.bluepito.goalertodolist.Models.Category;

public class AddCategoryFragmentDialog extends DialogFragment {

    private static final String TAG = "AddTaskFragment";
    private Button saveBtn, closeBtn;
    private EditText inputField;
    private DatabaseHelper databaseHelper;
    private boolean displayed;
    private Category category;

    public AddCategoryFragmentDialog(){};
    public AddCategoryFragmentDialog(Category category){
        this.category=category;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        displayed = true;

        View view = inflater.inflate(R.layout.dialog_fragment_add_category, container, false);

        inputField = (EditText) view.findViewById(R.id.inputCatField);
        saveBtn = (Button) view.findViewById(R.id.saveBtn);
        closeBtn = (Button) view.findViewById(R.id.closeBtn);
        databaseHelper = new DatabaseHelper(getContext());


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Add task dialog closed");
                getDialog().dismiss();
            }
        });

        if(this.category == null) {
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String catName = inputField.getText().toString();
                    if (catName.length() != 0) {

                        inputField.setText("");

                        addCategory(new Category(catName));
                        Log.d(TAG, "Category succesfuly added");

                    } else {

                        Log.d(TAG, "Failed adding category");
                        toastMsg("Input fields cannot be empty!");
                    }

                    getDialog().dismiss();
                }
            });
        } else {
            inputField.setText(this.category.getName());
            final Category tmp = this.category;

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Category tmp2 = tmp;
                    String catName = inputField.getText().toString();

                    if (catName.length() != 0) {

                        inputField.setText("");
                        tmp2.setName(catName);
                        updateCategory(tmp2);
                        Log.d(TAG, "Category updated added");

                    } else {

                        Log.d(TAG, "Failed updating category");
                        toastMsg("Input fields cannot be empty!");
                    }

                    getDialog().dismiss();
                }
            });
        }

        return view;
    }

    public void addCategory(Category newCategory) {
        databaseHelper.addCategory(newCategory);
    }
    public void updateCategory(Category newCategory) {
        databaseHelper.updateCat(newCategory);
    }

    private void toastMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        displayed = false;
        Context context = getContext();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriesFragment()).commit();
    }

    public boolean isDisplayed() {
        return displayed;
    }

}
