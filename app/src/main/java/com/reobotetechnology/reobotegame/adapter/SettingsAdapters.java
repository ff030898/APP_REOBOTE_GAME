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
import com.reobotetechnology.reobotegame.model.SettingsModel;
import com.reobotetechnology.reobotegame.model.ThemesModel;

import java.util.List;

public class SettingsAdapters extends RecyclerView.Adapter<SettingsAdapters.myViewHolder> {


    private List<SettingsModel> listTSettings;
    private Context context;

    public SettingsAdapters(List<SettingsModel> listTSettings, Context c) {
        this.listTSettings = listTSettings;
        this.context = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_settings, parent, false);
        return new myViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        SettingsModel setting = listTSettings.get(position);

        switch (setting.getId()) {
            case 1:
                holder.image.setImageResource(R.drawable.ic_user_form);
                break;

            case 2:
                holder.image.setImageResource(R.drawable.ic_password_form);
                break;
            case 3:
                holder.image.setImageResource(R.drawable.ic_info);
                break;
            case 4:
                holder.image.setImageResource(R.drawable.ic_help_outline);
                break;
            case 5:
                holder.image.setImageResource(R.drawable.ic_password_form);
                break;
            case 6:
                holder.image.setImageResource(R.drawable.ic_add_blog);
                break;
            case 7:
                holder.image.setImageResource(R.drawable.ic_exit_setting);
                break;

        }


        holder.txt_description.setText(setting.getText());

    }

    @Override
    public int getItemCount() {
        return listTSettings.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_description;
        private ImageView image;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_description = itemView.findViewById(R.id.textView22);
            image = itemView.findViewById(R.id.imageView7);

        }
    }


}
