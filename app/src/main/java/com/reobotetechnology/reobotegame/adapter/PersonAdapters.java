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

import com.bumptech.glide.Glide;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.CategoryModel;
import com.reobotetechnology.reobotegame.model.PersonModel;

import java.util.List;

public class PersonAdapters extends RecyclerView.Adapter<PersonAdapters.myViewHolder> {


    private List<PersonModel> listPerson;
    private Context context;

    public PersonAdapters(List<PersonModel> listPerson, Context c) {
        this.listPerson = listPerson;
        this.context = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_person, parent, false);
        return new myViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        PersonModel personModel = listPerson.get(position);
        holder.txt_name.setText(personModel.getName());
       if(personModel.getId().equals("1")){
           Glide
                   .with(context)
                   .load(R.drawable.elias_person)
                   .centerCrop()
                   .placeholder(R.drawable.elias_person)
                   .into(holder.image);
       }else if(personModel.getId().equals("2")){
           Glide
                   .with(context)
                   .load(R.drawable.moises)
                   .centerCrop()
                   .placeholder(R.drawable.moises)
                   .into(holder.image);
       }

       else{
           Glide
                   .with(context)
                   .load(R.drawable.noe_person)
                   .centerCrop()
                   .placeholder(R.drawable.noe_person)
                   .into(holder.image);
       }
    }

    @Override
    public int getItemCount() {
        return listPerson.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_name;
        private ImageView image;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            image = itemView.findViewById(R.id.image);

        }
    }


}
