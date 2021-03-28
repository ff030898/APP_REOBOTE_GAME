package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.friends.friends_profile.FriendProfileActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsRectangleAdapters extends RecyclerView.Adapter<FriendsRectangleAdapters.myViewHolder> {

    private List<UsuarioModel> usuario;
    private Context context;

    public FriendsRectangleAdapters(List<UsuarioModel> listaUsuario, Context c) {
        this.usuario = listaUsuario;
        this.context = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_friends_rectangle, parent, false);
        return new myViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {

        final UsuarioModel usuarioModel = usuario.get(position);
        String[] linhas = usuarioModel.getNome().split(" ");
        holder.userName.setText(linhas[0]);
        try {

            if (usuarioModel.getImagem().isEmpty()) {
                holder.profileImage.setImageResource(R.drawable.profile);
            } else {
                Glide
                        .with(context)
                        .load(usuarioModel.getImagem())
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(holder.profileImage);

            }
        } catch (Exception e) {
            holder.profileImage.setImageResource(R.drawable.profile);
        }

        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context.getApplicationContext(), FriendProfileActivity.class);
                i.putExtra("id", usuarioModel.getEmail());
                context.startActivity(i);
            }
        });


        holder.buttonMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    holder.buttonMatch.setBackground(context.getResources().getDrawable(R.drawable.badge_background));
                    holder.buttonMatch.setImageResource(R.drawable.ic_done);
                    Toast.makeText(context.getApplicationContext(), "Você começou a seguir "+usuarioModel.getNome(), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return usuario.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImage;
        TextView userName;
        ImageButton buttonMatch;
        ImageView online;

        myViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            userName = itemView.findViewById(R.id.userName);
            buttonMatch = itemView.findViewById(R.id.buttonMatch);
            online = itemView.findViewById(R.id.online);

        }
    }
}
