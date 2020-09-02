package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.Notification;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsAdapters extends RecyclerView.Adapter<NotificationsAdapters.myViewHolder> {

    private List<Notification> notifications;
    private Context context;

    public NotificationsAdapters(List<Notification> listaNotifications, Context c) {
        this.notifications = listaNotifications;
        this.context = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_notifications, parent, false);
        return new myViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        String[] linhas = notification.getFromName().split(" ");
        holder.txtDescNotification.setText(linhas[0]+" convidou você para uma partida online");
        try{

            if(notification.getFromImage().isEmpty()){
                holder.imagemPerfil.setImageResource(R.drawable.user);
            }else {

                Glide
                        .with(context)
                        .load(notification.getFromImage())
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(holder.imagemPerfil);
            }
        }catch (Exception e){

            holder.imagemPerfil.setImageResource(R.drawable.user);
        }

        holder.txtTimeNotification.setText("Agora");
        holder.btnNotification.setText("NOVO");

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imagemPerfil;
        TextView txtDescNotification, txtTimeNotification;
        Button btnNotification;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imagemPerfil = itemView.findViewById(R.id.imagemPerfil);
            txtDescNotification = itemView.findViewById(R.id.txtDescNotification);
            txtTimeNotification = itemView.findViewById(R.id.txtTimeNotification);
            btnNotification = itemView.findViewById(R.id.btnNotification);
        }
    }
}
