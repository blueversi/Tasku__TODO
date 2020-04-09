package pl.bluepito.goalertodolist.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.bluepito.goalertodolist.MainActivity;
import pl.bluepito.goalertodolist.Models.CategoriesAdapter;
import pl.bluepito.goalertodolist.Models.Category;
import pl.bluepito.goalertodolist.Controllers.DatabaseHelper;
import pl.bluepito.goalertodolist.R;

public class CategoriesFragment extends Fragment {

    private static final String TAG = "CategoriesFragment";
    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CategoriesAdapter categoriesAdapter;
    private ArrayList<Category> categoriesList;
    private int catId;

    public CategoriesFragment() {
    }

    public CategoriesFragment(int catId) {
        this.catId = catId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        databaseHelper = new DatabaseHelper(getContext());


        allCategoriesView();


        recyclerView = (RecyclerView) view.findViewById(R.id.categoriesRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        categoriesAdapter = new CategoriesAdapter(view.getContext(), categoriesList);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(categoriesAdapter);

        return view;
    }

    public void allCategoriesView() {

        categoriesList = databaseHelper.getAllCategories();

        Log.e(TAG, "Your Categories has been loaded into listView");

    }


    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.LEFT) {

                AddCategoryFragmentDialog dialog = new AddCategoryFragmentDialog(categoriesList.get(viewHolder.getAdapterPosition()));
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "AddTaskFragmentDialog");


                categoriesAdapter.notifyDataSetChanged();
            } else if (direction == ItemTouchHelper.RIGHT) {
                System.out.println("LONG ID: " +categoriesList.get(viewHolder.getAdapterPosition()).getId());
                databaseHelper.deleteCat(categoriesList.get(viewHolder.getAdapterPosition()), true );
                categoriesList.remove(viewHolder.getAdapterPosition());
                categoriesAdapter.notifyDataSetChanged();
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

    private void toastMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
