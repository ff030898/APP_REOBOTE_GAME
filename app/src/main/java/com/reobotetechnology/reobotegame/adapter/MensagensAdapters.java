package com.reobotetechnology.reobotegame.adapter;

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
import com.reobotetechnology.reobotegame.model.MensagensModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MensagensAdapters extends RecyclerView.Adapter<MensagensAdapters.myViewHolder> {

    List<MensagensModel> lista;
    Context context;

    public MensagensAdapters(List<MensagensModel> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_conversas, parent, false);
        return new MensagensAdapters.myViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
      MensagensModel m = lista.get(position);
      String nome = m.getEnvia().getNome();
      holder.nome_usuario_conversa.setText(nome);
        try{
            if(nome.equals("Reobote Technology")){

                Glide
                        .with(context)
                        .load(R.drawable.reobote)
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(holder.imagem_usuario_conversa);
            }else {
                if (m.getEnvia().getImagem().isEmpty()) {

                    Glide
                            .with(context)
                            .load(R.drawable.user)
                            .centerCrop()
                            .placeholder(R.drawable.user)
                            .into(holder.imagem_usuario_conversa);
                } else {

                    Glide
                            .with(context)
                            .load(m.getEnvia().getImagem())
                            .centerCrop()
                            .placeholder(R.drawable.user)
                            .into(holder.imagem_usuario_conversa);
                }
            }

        }catch (Exception e){
            Glide
                    .with(context)
                    .load(R.drawable.user)
                    .centerCrop()
                    .placeholder(R.drawable.user)
                    .into(holder.imagem_usuario_conversa);
        }

      holder.usuario_conversa_hora.setText(""+m.getHora());
      holder.usuario_conversa_texto.setText(m.getTexto());
      holder.usuario_conversa_qtd.setText(""+m.getId());

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imagem_usuario_conversa;
        TextView nome_usuario_conversa,usuario_conversa_hora, usuario_conversa_texto;
        Button usuario_conversa_qtd;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imagem_usuario_conversa = itemView.findViewById(R.id.imagem_usuario_conversa);
            nome_usuario_conversa = itemView.findViewById(R.id.nome_usuario_conversa);
            usuario_conversa_hora = itemView.findViewById(R.id.usuario_conversa_hora);
            usuario_conversa_texto = itemView.findViewById(R.id.usuario_conversa_texto);
            usuario_conversa_qtd = itemView.findViewById(R.id.usuario_conversa_qtd);
        }
    }
}
