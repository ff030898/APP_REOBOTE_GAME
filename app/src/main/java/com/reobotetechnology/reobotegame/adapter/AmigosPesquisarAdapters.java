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
import com.reobotetechnology.reobotegame.model.UsuarioModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AmigosPesquisarAdapters extends RecyclerView.Adapter<AmigosPesquisarAdapters.myViewHolder> {

    private List<UsuarioModel> usuario;
    private Context context;
    private int tipo;

    public AmigosPesquisarAdapters(List<UsuarioModel> usuario, Context context, int tipo) {
        this.usuario = usuario;
        this.context = context;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_amigos_pesquisar, parent, false);
        return new myViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, int position) {

        UsuarioModel usuarioModel = usuario.get(position);

        holder.txtNomeUsuarioPesquisar.setText(usuarioModel.getNome());

        try{

            if(usuarioModel.getImagem().isEmpty()){

                Glide
                        .with(context)
                        .load(R.drawable.user)
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(holder.imgUsuarioPesquisar);
            }else {

                Glide
                        .with(context)
                        .load(usuarioModel.getImagem())
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(holder.imgUsuarioPesquisar);
            }
        }catch (Exception e){
            Glide
                    .with(context)
                    .load(R.drawable.user)
                    .centerCrop()
                    .placeholder(R.drawable.user)
                    .into(holder.imgUsuarioPesquisar);
        }

        if(usuarioModel.isOnline()) {
            holder.online.setVisibility(View.VISIBLE);
        }

        holder.txtRankingUsuarioPesquisar.setText("Ranking: "+usuarioModel.getRanking());
        if(tipo == 1){
            holder.btnSeguirUsuario.setText("CONVIDAR");
        }
        holder.btnSeguirUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String texto = holder.btnSeguirUsuario.getText().toString();

                if (texto.equals("SEGUIR")) {
                    holder.btnSeguirUsuario.setText("SEGUINDO");
                }else{
                    holder.btnSeguirUsuario.setText("SEGUIR");
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return usuario.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imgUsuarioPesquisar;
        TextView txtNomeUsuarioPesquisar, txtRankingUsuarioPesquisar;
        Button btnSeguirUsuario;
        ImageView online;

        myViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUsuarioPesquisar = itemView.findViewById(R.id.imgUsuarioPesquisar);
            txtNomeUsuarioPesquisar = itemView.findViewById(R.id.txtTitulo);
            txtRankingUsuarioPesquisar = itemView.findViewById(R.id.txtRankingUsuario);
            btnSeguirUsuario = itemView.findViewById(R.id.btnSeguirUsuario);
            online = itemView.findViewById(R.id.online);
        }
    }


}
