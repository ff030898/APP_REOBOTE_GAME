package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

        holder.txtDescNotification.setText(notification.getFromName());
        try{

            if(notification.getFromName().equals("Reobote IA")){
                holder.imagemPerfil.setImageResource(R.drawable.reobote);
            }
            else if(notification.getFromImage().isEmpty()){
                holder.imagemPerfil.setImageResource(R.drawable.profile);
            }else {

                Glide
                        .with(context)
                        .load(notification.getFromImage())
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(holder.imagemPerfil);
            }
        }catch (Exception e){

            holder.imagemPerfil.setImageResource(R.drawable.profile);
        }

        if(!notification.getTipo().equals("blog")){
            holder.cardViewBlog.setVisibility(View.GONE);
        }

        if(notification.isView()){
            holder.view.setVisibility(View.GONE);
        }

        holder.txtTimeNotification.setText(notification.getText());

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imagemPerfil;
        TextView txtDescNotification, txtTimeNotification;
        CardView cardViewBlog;
        ImageView view;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imagemPerfil = itemView.findViewById(R.id.imagemPerfil);
            txtDescNotification = itemView.findViewById(R.id.txtDescNotification);
            txtTimeNotification = itemView.findViewById(R.id.txtTimeNotification);
            cardViewBlog = itemView.findViewById(R.id.cardViewBlog);
            view = itemView.findViewById(R.id.imageView3);

        }
    }
}
