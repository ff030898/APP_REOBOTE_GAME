package com.reobotetechnology.reobotegame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.MensagensModel;

import java.util.List;

public class MensagensChatAdapters extends RecyclerView.Adapter<MensagensChatAdapters.myViewHolder> {

    private List<MensagensModel> mensagens;
    private Context context;
    private static final int TIPO_REMETENTE     = 0;
    private static final int TIPO_DESTINATARIO  = 1;

    public MensagensChatAdapters(List<MensagensModel> mensagens, Context context) {
        this.mensagens = mensagens;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = null;
        if ( viewType == TIPO_REMETENTE ){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_mensagem_remetente, parent, false);
        }else if( viewType == TIPO_DESTINATARIO ){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_mensagem_destinatario, parent, false);
        }

        assert item != null;
        return new myViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        MensagensModel mensagem = mensagens.get( position );

        String msg = mensagem.getTexto();
        String hora = mensagem.getHora();

        holder.textMensagemTexto.setText(msg);
        holder.textMensagemHora.setText(hora);



    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {

        MensagensModel mensagem = mensagens.get( position );

        ///String idUsuario = UsuarioFirebase.getIdentificadorUsuario();

        int tipo = mensagem.getTipo();

        if ( tipo == 0 ){
            return TIPO_REMETENTE;
        }

        return TIPO_DESTINATARIO;

    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        TextView textMensagemTexto, textMensagemHora;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            textMensagemTexto = itemView.findViewById(R.id.textMensagemTexto);
            textMensagemHora = itemView.findViewById(R.id.textMensagemHora);
        }
    }
}
