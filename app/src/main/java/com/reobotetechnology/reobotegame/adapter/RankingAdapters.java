package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.UserModel;


import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class RankingAdapters extends RecyclerView.Adapter<RankingAdapters.myViewHolder> {

    private List<UserModel> usuario;
    private Context context;


    public RankingAdapters(List<UserModel> listaUsuario, Context c) {
        this.usuario = listaUsuario;
        this.context = c;

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_ranking_list, parent, false);
        return new myViewHolder(itemLista);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        UserModel usuarioModel = usuario.get(getItemCount() - position - 1);

        int pos = (position + 1);

        DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();

        holder.nome.setText(usuarioModel.getNome());


        try {
            if (usuarioModel.getImagem().isEmpty()) {

                Glide
                        .with(context)
                        .load(R.drawable.profile)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(holder.img);
            } else {
                Glide
                        .with(context)
                        .load(usuarioModel.getImagem())
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(holder.img);
            }
        } catch (Exception e) {
            Glide
                    .with(context)
                    .load(R.drawable.profile)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(holder.img);
        }


            holder.pontos.setText(""+usuarioModel.getPontosG());
            String idUsuario = Base64Custom.codificarBase64((Objects.requireNonNull(usuarioModel.getEmail())));
            DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
            usuarioRef.child("ranking").setValue(pos);

           holder.ranking.setText(usuarioModel.getRanking() + "º");

    }


    @Override
    public int getItemCount() {
        return usuario.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img;
        TextView nome, pontos;
        Button ranking;
        ConstraintLayout Principal;

        myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_user);
            nome = itemView.findViewById(R.id.txtNome);
            pontos = itemView.findViewById(R.id.txtPontos);
            ranking = itemView.findViewById(R.id.btnRanking);
            Principal = itemView.findViewById(R.id.Principal);


        }
    }
}
