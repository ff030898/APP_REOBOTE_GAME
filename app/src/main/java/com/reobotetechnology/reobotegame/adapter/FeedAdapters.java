package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.PostModel;
import com.reobotetechnology.reobotegame.ui.PlayActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedAdapters extends RecyclerView.Adapter<FeedAdapters.myViewHolder> {

    private List<PostModel> post;
    private Context context;

    private static final int TIPO_LINK     = 0;
    private static final int TIPO_IMAGEM  = 1;
    private static final int TIPO_TEXTO    = 2;
    private static final int TIPO_VIDEO     = 3;


    public FeedAdapters(List<PostModel> post, Context context) {
        this.post = post;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = null;

        if ( viewType == TIPO_LINK ){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item_link, parent, false);
        }else if( viewType == TIPO_IMAGEM ){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item_imagem, parent, false);
        }else if(viewType == TIPO_TEXTO){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item_texto, parent, false);
        }else if (viewType == TIPO_VIDEO){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_video, parent, false);
        }

        return new myViewHolder(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, int position) {

        PostModel p = post.get(position);

        holder.imgUsuario.setImageResource(p.getUser().getImg());
        holder.nomeUsuario.setText(p.getUser().getNome());
        holder.tempoUsuario.setText("Agora mesmo");
        holder.textoPostagem.setText(p.getDesc());

        if(p.getImagem() != 0) {
            holder.imagemPostagem.setImageResource(p.getImagem());

            holder.imgAmei.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    int img = R.drawable.ic_amei_colorido;
                    int img2 = R.drawable.ic_amei_preenchido;

                    String verificar = (String) holder.textoAmei.getText();
                    if(verificar.equals("0")) {
                       holder.imgAmei.setImageResource(img);
                       holder.textoAmei.setText("1");
                    }else{
                        holder.imgAmei.setImageResource(img2);
                        holder.textoAmei.setText("0");
                    }
                }
            });

        }else{

            holder.imgAmei.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    int img = R.drawable.ic_amei_colorido;
                    int img2 = R.drawable.ic_amei_preenchido;

                    String verificar = (String) holder.textoAmei.getText();
                    if(verificar.equals("0")) {
                        holder.imgAmei.setImageResource(img);
                        holder.textoAmei.setText("1");
                    }else{
                        holder.imgAmei.setImageResource(img2);
                        holder.textoAmei.setText("0");
                    }
                }
            });

           holder.imgPlay.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent i = new Intent(context, PlayActivity.class);
                   context.startActivity(i);
               }
           });

        }






    }



    @Override
    public int getItemCount() {
        return post.size();
    }

    @Override
    public int getItemViewType(int position) {

        PostModel p = post.get(position);

        if(p.getImagem() == 0){
            return TIPO_VIDEO;
        }else {
            return TIPO_IMAGEM;
        }

    }

    static class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imgUsuario;
        TextView nomeUsuario, tempoUsuario, textoPostagem, textoAmei, textoAmeiVideo;
        ImageView imagemPostagem, imgAmei, imgAmeiVideo, imgPlay;

        myViewHolder(View itemView) {
            super(itemView);

            imgUsuario = itemView.findViewById(R.id.imgUsuario);
            nomeUsuario = itemView.findViewById(R.id.nomeUsuario);
            tempoUsuario = itemView.findViewById(R.id.tempoUsuario);
            textoPostagem = itemView.findViewById(R.id.textoPostagem);
            imagemPostagem = itemView.findViewById(R.id.imagemPostagem);
            imgPlay = itemView.findViewById(R.id.imgPlay);
            imgAmei = itemView.findViewById(R.id.imgAmei);
            textoAmei = itemView.findViewById(R.id.textoAmei);




        }
    }
}
