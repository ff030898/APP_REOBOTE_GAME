package com.reobotetechnology.reobotegame.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.MensagensModel;
import com.reobotetechnology.reobotegame.model.Message;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesChatAdapters extends RecyclerView.Adapter<MessagesChatAdapters.myViewHolder> {

    private List<Message> mensagens;
    private Context context;
    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;

    //Configurações do banco de dados
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();

    public MessagesChatAdapters(ArrayList<Message> mensagens, Context context) {
        this.mensagens = mensagens;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = null;
        if (viewType == TIPO_REMETENTE) {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_mesages_rem, parent, false);
        } else if (viewType == TIPO_DESTINATARIO) {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_mesage_dest, parent, false);
        }

        assert item != null;
        return new myViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, int position) {
        Message mensagem = mensagens.get(position);

        String msg = mensagem.getText();
        String[] hora = mensagem.getTimestamp().split("/");

        holder.textMensagemTexto.setText(Base64Custom.decodificarBase64(msg));
        holder.textMensagemHora.setText(hora[1]);

        if (mensagem.getType() == 0) {

            Glide
                    .with(context)
                    .load(user.getPhotoUrl())
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(holder.profileRem);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    holder.txtView.setText(context.getString(R.string.view));

                }
            }, 2000);

        }else{
            Glide
                    .with(context)
                    .load(R.drawable.reobote)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(holder.profileDes);
        }


    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {

        Message mensagem = mensagens.get(position);

        int tipo = mensagem.getType();

        if (tipo == 0) {
            return TIPO_REMETENTE;
        }

        return TIPO_DESTINATARIO;

    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView textMensagemTexto, textMensagemHora, txtView;
        CircleImageView profileRem, profileDes;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            textMensagemTexto = itemView.findViewById(R.id.textMensagemTexto);
            textMensagemHora = itemView.findViewById(R.id.textMensagemHora);
            txtView = itemView.findViewById(R.id.txtView);
            profileRem = itemView.findViewById(R.id.profileRem);
            profileDes = itemView.findViewById(R.id.profileDes);
        }
    }
}
