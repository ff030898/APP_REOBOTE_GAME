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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedImageView;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.PostModel;
import com.reobotetechnology.reobotegame.ui.amigos.amigos_profile.Amigos_PerfilActivity;
import com.reobotetechnology.reobotegame.ui.blog.PostActivity;
import com.reobotetechnology.reobotegame.ui.perfil.PerfilActivity;
import com.reobotetechnology.reobotegame.ui.play_videos.PlayActivity;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapters extends RecyclerView.Adapter<PostAdapters.myViewHolder> {
    private List<PostModel> lista;
    private Context context;
    private static final int TIPO_BLOG = 0;
    private static final int TIPO_VIDEO = 1;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();

    public PostAdapters(List<PostModel> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = null;
        if ( viewType == TIPO_BLOG){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_blog_post, parent, false);
        }else if( viewType == TIPO_VIDEO){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_blog_video, parent, false);
        }

        assert item != null;
        return new myViewHolder(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, int position) {
         final PostModel p = lista.get(position);

         holder.txtTitulo.setText(p.getTitulo());
         holder.txtNomeUsuario.setText(p.getUser().getNome());
         holder.txtHora.setText(p.getData() + " as " +p.getHora());

        try {
            if (p.getUser().getImagem().isEmpty()) {

                Glide
                        .with(context)
                        .load(R.drawable.user)
                        .centerCrop()
                        .placeholder(R.drawable.img_feed)
                        .into(holder.imgUsuarioPost);
            } else {

                Glide
                        .with(context)
                        .load(p.getUser().getImagem())
                        .centerCrop()
                        .placeholder(R.drawable.img_feed)
                        .into(holder.imgUsuarioPost);
            }
        } catch (Exception e) {
            Glide
                    .with(context)
                    .load(R.drawable.user)
                    .centerCrop()
                    .placeholder(R.drawable.img_feed)
                    .into(holder.imgUsuarioPost);
        }

        if(p.getTipo() == 1) {

            try{

                if(p.getImagem().isEmpty()){

                    Glide
                            .with(context)
                            .load(R.drawable.fundo)
                            .centerCrop()
                            .placeholder(R.drawable.img_feed)
                            .into(holder.img_video);
                }else{

                    Glide
                            .with(context)
                            .load(p.getImagem())
                            .centerCrop()
                            .placeholder(R.drawable.img_feed)
                            .into(holder.img_video);
                }

            }catch(Exception e){
                Glide
                        .with(context)
                        .load(R.drawable.fundo)
                        .centerCrop()
                        .placeholder(R.drawable.img_feed)
                        .into(holder.img_video);
            }

            holder.btn_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayActivity.class);
                    context.startActivity(intent);
                }
            });

        }else{
            try{

                if(p.getImagem().isEmpty()){

                    Glide
                            .with(context)
                            .load(R.drawable.img_feed)
                            .centerCrop()
                            .placeholder(R.drawable.img_feed)
                            .into(holder.img_post);
                }else{
                    Glide
                            .with(context)
                            .load(p.getImagem())
                            .centerCrop()
                            .placeholder(R.drawable.img_feed)
                            .into(holder.img_post);
                }

            }catch(Exception e){
                Glide
                        .with(context)
                        .load(R.drawable.img_feed)
                        .centerCrop()
                        .placeholder(R.drawable.img_feed)
                        .into(holder.img_post);
            }

            holder.img_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, PostActivity.class);
                    //i.putExtra("id", usuarioSelecionado.getEnvia().getId());
                    context.startActivity( i );
                }
            });
        }


        holder.img_amei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img_amei.setImageResource(R.drawable.ic_amei_colorido);
            }
        });

        holder.txtTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, PostActivity.class);
                //i.putExtra("id", usuarioSelecionado.getEnvia().getId());
                context.startActivity( i );
            }
        });

        holder.txtNomeUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(p.getUser().getEmail().equals(user.getEmail())){
                    context.startActivity(new Intent(context, PerfilActivity.class));
                }else {
                    Intent i = new Intent(context, Amigos_PerfilActivity.class);
                    i.putExtra("id", p.getUser().getEmail());
                    context.startActivity( i );
                }
            }
        });

        holder.imgUsuarioPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(p.getUser().getEmail().equals(user.getEmail())){
                    context.startActivity(new Intent(context, PerfilActivity.class));
                }else {
                    Intent i = new Intent(context, Amigos_PerfilActivity.class);
                    i.putExtra("id", p.getUser().getEmail());
                    context.startActivity( i );
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public int getItemViewType(int position) {

        final PostModel p = lista.get(position);

        int tipo = p.getTipo();

        if ( tipo == 0 ){
            return TIPO_BLOG;
        }

        return TIPO_VIDEO;

    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imgUsuarioPost;
        TextView txtNomeUsuario, txtHora, txtTitulo, txtDesc;
        RoundedImageView img_post, img_video;
        ImageView img_amei, btn_play;

        private myViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUsuarioPost = itemView.findViewById(R.id.imgUsuarioPost);
            txtNomeUsuario = itemView.findViewById(R.id.txtNomeUsuario);
            txtHora = itemView.findViewById(R.id.txtHora);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            img_post = itemView.findViewById(R.id.img_post);
            img_amei = itemView.findViewById(R.id.img_amei);
            img_video = itemView.findViewById(R.id.img_video);
            btn_play = itemView.findViewById(R.id.btn_play);

        }
    }
}
