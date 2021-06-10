package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.MensagensModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapters extends RecyclerView.Adapter<MessagesAdapters.myViewHolder> {

    private List<MensagensModel> lista;
    private Context context;

    public MessagesAdapters(List<MensagensModel> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_mesages_list, parent, false);
        return new myViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, int position) {
        MensagensModel m = lista.get(position);
        String nome = m.getEnvia().getNome();
        holder.nome_usuario_conversa.setText(nome);

        try {
            if (nome.equals(context.getString(R.string.name_robot))) {

                Glide
                        .with(context)
                        .load(R.drawable.reobote)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(holder.imagem_usuario_conversa);
            } else {
                if (m.getEnvia().getImagem().isEmpty()) {

                    Glide
                            .with(context)
                            .load(R.drawable.profile)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(holder.imagem_usuario_conversa);
                } else {

                    Glide
                            .with(context)
                            .load(m.getEnvia().getImagem())
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(holder.imagem_usuario_conversa);
                }
            }

        } catch (Exception e) {
            Glide
                    .with(context)
                    .load(R.drawable.profile)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(holder.imagem_usuario_conversa);
        }

        holder.usuario_conversa_hora.setText("" + m.getHora());
        holder.usuario_conversa_texto.setText(m.getTexto());

        //A lógiva é essa. Mas, invés de ID precisa ser a quantidade de messages não visualizadas = parecido com as notifications
        final int id = m.getId();
        if (id > 0) {
            holder.usuario_conversa_qtd.setText("1");
        } else {
            holder.usuario_conversa_qtd.setVisibility(View.GONE);
        }

        holder.constraintMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id > 0) {
                    holder.usuario_conversa_qtd.setText("0");
                    holder.usuario_conversa_qtd.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imagem_usuario_conversa;
        TextView nome_usuario_conversa, usuario_conversa_hora, usuario_conversa_texto;
        Button usuario_conversa_qtd;
        ConstraintLayout constraintMain;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imagem_usuario_conversa = itemView.findViewById(R.id.image_user);
            nome_usuario_conversa = itemView.findViewById(R.id.txt_username);
            usuario_conversa_hora = itemView.findViewById(R.id.usuario_conversa_hora);
            usuario_conversa_texto = itemView.findViewById(R.id.usuario_conversa_texto);
            usuario_conversa_qtd = itemView.findViewById(R.id.usuario_conversa_qtd);
            constraintMain = itemView.findViewById(R.id.constraintMain);
        }
    }
}
