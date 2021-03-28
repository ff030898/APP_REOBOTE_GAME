package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.BlogPostModel;
import com.reobotetechnology.reobotegame.model.ConquistesModel;

import java.util.List;

public class ProfileConquistesAdapters extends RecyclerView.Adapter<ProfileConquistesAdapters.myViewHolder> {


    private List<ConquistesModel> listConquist;
    private Context context;

    public ProfileConquistesAdapters(List<ConquistesModel> listConquist, Context c) {
        this.listConquist = listConquist;
        this.context = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_conquist, parent, false);
        return new myViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {


        ConquistesModel conquistesModel = listConquist.get(position);

        holder.txt_conquist.setText(""+conquistesModel.getDescription());
        holder.txt_conquist_count.setText("+"+conquistesModel.getCount());

        if(position > 0){

            if(conquistesModel.isEnabled()){
                holder.image.setBackground(context.getResources().getDrawable(R.drawable.badge_background_victory));
                holder.image.setImageResource(R.drawable.ic_victory);
            }else {
                holder.image.setBackground(context.getResources().getDrawable(R.drawable.badge_background_disabled));
                holder.image.setImageResource(R.drawable.ic_victory);
                holder.image.setAlpha(0.5f);
            }

        }else {
            holder.image.setBackground(context.getResources().getDrawable(R.drawable.badge_background));
            holder.image.setImageResource(R.drawable.ic_done);
        }

    }

    @Override
    public int getItemCount() {
        return listConquist.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView txt_conquist, txt_conquist_count;
        ImageButton image;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_conquist = itemView.findViewById(R.id.txt_conquist);
            txt_conquist_count = itemView.findViewById(R.id.txt_conquist_count);
            image = itemView.findViewById(R.id.image);

        }
    }


}
