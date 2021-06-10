package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.CategoryModel;
import com.reobotetechnology.reobotegame.model.SettingsModel;

import java.util.List;

public class CategoryAdapters extends RecyclerView.Adapter<CategoryAdapters.myViewHolder> {


    private List<CategoryModel> listCategory;
    private Context context;

    public CategoryAdapters(List<CategoryModel> listCategory, Context c) {
        this.listCategory = listCategory;
        this.context = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_search, parent, false);
        return new myViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        CategoryModel categoryModel = listCategory.get(position);

        switch (categoryModel.getId()) {
            case 1:
                holder.image.setImageResource(R.drawable.verse_day);
                break;
            case 2:
                holder.image.setImageResource(R.drawable.amizade);
                break;
            case 3:
                holder.image.setImageResource(R.drawable.oracao);
                break;
            case 4:
                holder.image.setImageResource(R.drawable.verse_day2);
                break;
            case 5:
                holder.image.setImageResource(R.drawable.aprender);
                break;
            case 6:
                holder.image.setImageResource(R.drawable.match);
                break;


        }


        holder.txt_theme.setText(categoryModel.getText());

    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_theme;
        private ImageView image;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_theme = itemView.findViewById(R.id.txt_theme);
            image = itemView.findViewById(R.id.image);

        }
    }


}
