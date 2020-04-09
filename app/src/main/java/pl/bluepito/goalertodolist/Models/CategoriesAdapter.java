package pl.bluepito.goalertodolist.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.bluepito.goalertodolist.R;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyHolder> {

    Context context;
    ArrayList<Category> categoriesList = new ArrayList<>();

    public CategoriesAdapter(Context context, ArrayList<Category> categoriesList) {
        this.context = context;
        if (categoriesList.get(0).getId() < 0) {
            this.categoriesList = new ArrayList<>();
        } else {
            this.categoriesList = categoriesList;

        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_card, viewGroup, false);

        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        holder.idCatTV.setText(String.valueOf(categoriesList.get(position).getId()));
        holder.catNameTV.setText(categoriesList.get(position).getName());
        holder.createdAtTV.setText("Created At: " + categoriesList.get(position).getInsertDate());


    }

    @Override
    public int getItemCount() {
        return (categoriesList == null) ? 0 : categoriesList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView idCatTV, catNameTV, createdAtTV;

        public MyHolder(View itemView) {
            super(itemView);
            idCatTV = itemView.findViewById(R.id.category_id_card);
            catNameTV = itemView.findViewById(R.id.category_name_card);
            createdAtTV = itemView.findViewById(R.id.category_created_card);
        }

    }


}
