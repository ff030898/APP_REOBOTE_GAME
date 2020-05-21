package com.reobotetechnology.reobotegame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_amigos, parent, false);
        return new myViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        UsuarioModel usuarioModel = usuario.get(position);

        holder.nome.setText(usuarioModel.getNome());
        holder.img.setImageResource(usuarioModel.getImg());

        /*if( usuario.getFoto() != null ){
            Uri uri = Uri.parse( usuario.getFoto() );
            Glide.with( context ).load( uri ).into( holder.foto );
        }else {
            holder.foto.setImageResource( R.drawable.padrao );
        }*/


    }

    @Override
    public int getItemCount() {
        return usuario.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView nome;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgAmigo);
            nome= itemView.findViewById(R.id.txtAmigo);
        }
    }
}
