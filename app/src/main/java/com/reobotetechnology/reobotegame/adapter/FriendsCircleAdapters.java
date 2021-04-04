package com.reobotetechnology.reobotegame.adapter;

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
import com.reobotetechnology.reobotegame.model.UserModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsCircleAdapters extends RecyclerView.Adapter<FriendsCircleAdapters.myViewHolder> {

    private List<UserModel> usuario;
    private Context context;

    public FriendsCircleAdapters(List<UserModel> listaUsuario, Context c) {
        this.usuario = listaUsuario;
        this.context = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_friends_circle, parent, false);
        return new myViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        UserModel usuarioModel = usuario.get(position);
        String[] linhas = usuarioModel.getNome().split(" ");
        holder.nome.setText(linhas[0]);
        try {

            if (usuarioModel.getNome().equals("Amigo")) {

                holder.img.setImageResource(R.drawable.ic_sala);
                holder.online.setVisibility(View.GONE);
            }else if(usuarioModel.getNome().equals("Reobote IA")){
                holder.img.setImageResource(R.drawable.reobote);
            }else {

                if (usuarioModel.isOnline()) {
                    holder.online.setVisibility(View.VISIBLE);
                }

                if (usuarioModel.getImagem().isEmpty()) {


                    holder.img.setImageResource(R.drawable.profile);

                } else {
                    Glide
                            .with(context)
                            .load(usuarioModel.getImagem())
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(holder.img);

                }
            }
        } catch (Exception e) {
            holder.img.setImageResource(R.drawable.profile);
        }



    }

    @Override
    public int getItemCount() {
        return usuario.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img;
        TextView nome;
        ImageView online;

        myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgAmigo);
            nome = itemView.findViewById(R.id.txtAmigo);
            online = itemView.findViewById(R.id.online);
        }
    }
}
