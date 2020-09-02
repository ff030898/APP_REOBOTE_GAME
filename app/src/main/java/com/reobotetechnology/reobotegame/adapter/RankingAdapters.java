package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.UsuarioModel;


import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class RankingAdapters extends RecyclerView.Adapter<RankingAdapters.myViewHolder> {

    private List<UsuarioModel> usuario;
    private Context context;
    private int tipo;
    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();

    public RankingAdapters(List<UsuarioModel> listaUsuario, Context c, int tipo) {
        this.usuario = listaUsuario;
        this.context = c;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking, parent, false);
        return new myViewHolder(itemLista);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        UsuarioModel usuarioModel = usuario.get(getItemCount() - position - 1);

        int pos = (position + 1);

        DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebaseDataBase();

        holder.nome.setText(usuarioModel.getNome());

        try {
            if (usuarioModel.getImagem().isEmpty()) {

                Glide
                        .with(context)
                        .load(R.drawable.user)
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(holder.img);
            } else {
                Glide
                        .with(context)
                        .load(usuarioModel.getImagem())
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(holder.img);
            }
        } catch (Exception e) {
            Glide
                    .with(context)
                    .load(R.drawable.user)
                    .centerCrop()
                    .placeholder(R.drawable.user)
                    .into(holder.img);
        }

        if (tipo == 0) {
            holder.pontos.setText("" + usuarioModel.getPontosG());
            String idUsuario = Base64Custom.codificarBase64((Objects.requireNonNull(usuarioModel.getEmail())));
            DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
            usuarioRef.child("ranking").setValue(pos);
        } else if (tipo == 1) {
            holder.pontos.setText("" + usuarioModel.getPontosD());
        }
        holder.ranking.setText(pos + "º");

    }


    @Override
    public int getItemCount() {
        return usuario.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img;
        TextView nome, pontos, ranking;
        View guideline2;
        ConstraintLayout Principal;

        myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_user);
            nome = itemView.findViewById(R.id.txtNome);
            pontos = itemView.findViewById(R.id.txtPontos);
            ranking = itemView.findViewById(R.id.txtRanking);
            guideline2 = itemView.findViewById(R.id.guideline2);
            Principal = itemView.findViewById(R.id.Principal);

        }
    }
}
