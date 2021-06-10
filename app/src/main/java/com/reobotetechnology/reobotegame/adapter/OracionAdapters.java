package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.OracionModel;
import com.reobotetechnology.reobotegame.model.PersonModel;

import java.util.List;

public class OracionAdapters extends RecyclerView.Adapter<OracionAdapters.myViewHolder> {


    private List<OracionModel> listOracion;
    private Context context;

    public OracionAdapters(List<OracionModel> listOracion, Context c) {
        this.listOracion = listOracion;
        this.context = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_oracion, parent, false);
        return new myViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        OracionModel oracionModel = listOracion.get(position);
        holder.txt_title.setText(oracionModel.getTitle());
        holder.txtDate.setText(oracionModel.getDate_after()+" - "+oracionModel.getDate_before());
        holder.txtTime.setText(oracionModel.getTime());
        holder.txtStatus.setText(oracionModel.getStatus());

        if(oracionModel.getId().equals("2")){
            holder.btnStatus.setBackground(context.getResources().getDrawable(R.drawable.badge_background_victory));
        }
    }

    @Override
    public int getItemCount() {
        return listOracion.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_title, txtDate, txtTime, txtStatus;
        private Button btnStatus, btn_send;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.textView3);
            txtDate = itemView.findViewById(R.id.textView25);
            txtTime = itemView.findViewById(R.id.textView50);
            txtStatus = itemView.findViewById(R.id.textView47);
            btnStatus = itemView.findViewById(R.id.button4);
            btn_send = itemView.findViewById(R.id.button5);

        }
    }


}
