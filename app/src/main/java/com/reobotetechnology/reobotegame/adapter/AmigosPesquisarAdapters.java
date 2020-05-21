package com.reobotetechnology.reobotegame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.UsuarioModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AmigosPesquisarAdapters extends RecyclerView.Adapter<AmigosPesquisarAdapters.myViewHolder> {

    private List<UsuarioModel> usuario;
    private Context context;

    public AmigosPesquisarAdapters(List<UsuarioModel> usuario, Context context) {
        this.usuario = usuario;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_amigos_pesquisar, parent, false);
        return new myViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, int position) {

        UsuarioModel usuarioModel = usuario.get(position);

        holder.txtNomeUsuarioPesquisar.setText(usuarioModel.getNome());
        holder.imgUsuarioPesquisar.setImageResource(usuarioModel.getImg());
        holder.txtRankingUsuarioPesquisar.setText("Ranking: "+usuarioModel.getRanking());
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

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUsuarioPesquisar = itemView.findViewById(R.id.imgUsuarioPesquisar);
            txtNomeUsuarioPesquisar = itemView.findViewById(R.id.txtNomeUsuario);
            txtRankingUsuarioPesquisar = itemView.findViewById(R.id.txtRankingUsuario);
            btnSeguirUsuario = itemView.findViewById(R.id.btnSeguirUsuario);
        }
    }


}
