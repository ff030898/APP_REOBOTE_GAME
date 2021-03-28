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
import com.reobotetechnology.reobotegame.model.ThemesModel;
import java.util.List;

public class ThemesVersesOfBibleAdapters extends RecyclerView.Adapter<ThemesVersesOfBibleAdapters.myViewHolder> {


    private List<ThemesModel> listThemes;
    private Context context;

    public ThemesVersesOfBibleAdapters(List<ThemesModel> listThemes, Context c) {
        this.listThemes = listThemes;
        this.context = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_themes_verse, parent, false);
        return new myViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        ThemesModel theme = listThemes.get(position);

        //int[] rainbow = context.getResources().getIntArray(R.array.rainbow);
        //int tamanho = rainbow.length;

        //int numero = new Random().nextInt(tamanho);

        switch (theme.getThemeText()) {
            case "Amor":
                holder.image.setImageResource(R.drawable.amor);
                break;
            case "Amizade":
                holder.image.setImageResource(R.drawable.amizade);
                break;
            case "Ansiedade":
                holder.image.setImageResource(R.drawable.ansiedade);
                break;
            case "Namoro":
                holder.image.setImageResource(R.drawable.namoro);
                break;
            case "Casamento":
                holder.image.setImageResource(R.drawable.casamento);
                break;
        }

        holder.txt_theme.setText(theme.getThemeText());


    }

    @Override
    public int getItemCount() {
        return listThemes.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView txt_theme;
        //CardView bg_theme;
        ImageView image;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_theme = itemView.findViewById(R.id.txt_theme);
            //bg_theme = itemView.findViewById(R.id.bg_theme);
            image = itemView.findViewById(R.id.image);

        }
    }


}
