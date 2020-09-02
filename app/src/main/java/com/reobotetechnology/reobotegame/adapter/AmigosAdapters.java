package com.reobotetechnology.reobotegame.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.UsuarioModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AmigosAdapters extends RecyclerView.Adapter<AmigosAdapters.myViewHolder> {

    private List<UsuarioModel> usuario;
    private Context context;

    public AmigosAdapters(List<UsuarioModel> listaUsuario, Context c) {
        this.usuario = listaUsuario;
        this.context = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_amigos, parent, false);
        return new myViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        UsuarioModel usuarioModel = usuario.get(position);
        String[] linhas = usuarioModel.getNome().split(" ");
        holder.nome.setText(linhas[0]);
        try{

            if(usuarioModel.getImagem().isEmpty()){
                holder.img.setImageResource(R.drawable.user);
            }else {
                Glide
                        .with(context)
                        .load(usuarioModel.getImagem())
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(holder.img);

            }
        }catch (Exception e){
            holder.img.setImageResource(R.drawable.user);
        }

        if(usuarioModel.isOnline()){
            holder.online.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return usuario.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder{

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
