package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.CommentModel;
import com.reobotetechnology.reobotegame.model.Notification;
import com.reobotetechnology.reobotegame.model.UsuarioModel;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapters extends RecyclerView.Adapter<CommentsAdapters.myViewHolder> {

    private List<CommentModel> comment;
    private Context context;

    public CommentsAdapters(List<CommentModel> listaComment, Context c) {
        this.comment = listaComment;
        this.context = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_comments, parent, false);
        return new myViewHolder(itemLista);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, int position) {
        final CommentModel commentModel = comment.get(position);

        holder.txtDescComment.setText(commentModel.getDescription());
        holder.txtTime.setText(commentModel.getTime()+" horas atrás");
        holder.txtCountLike.setText(""+commentModel.getLike());

        try {

            DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();

            String idUsuario = Base64Custom.codificarBase64("" + commentModel.getId_user());

            firebaseRef.child("usuarios").child(idUsuario).addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UsuarioModel user = dataSnapshot.getValue(UsuarioModel.class);
                    if (user != null) {
                        holder.txtNameUser.setText(user.getNome());

                        if (user.getImagem().isEmpty()) {
                            holder.imagemPerfil.setImageResource(R.drawable.profile);
                        } else {

                            Glide
                                    .with(context)
                                    .load(user.getImagem())
                                    .centerCrop()
                                    .placeholder(R.drawable.profile)
                                    .into(holder.imagemPerfil);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } catch (Exception e) {
            holder.imagemPerfil.setImageResource(R.drawable.profile);

        }

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.like.setImageResource(R.drawable.ic_like_comment_pint);
                holder.txtCountLike.setText(""+ (commentModel.getLike() + 1) );
            }
        });



    }

    @Override
    public int getItemCount() {
        return comment.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imagemPerfil;
        TextView txtDescComment, txtCountLike, txtNameUser, txtTime;
        ImageButton like;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imagemPerfil = itemView.findViewById(R.id.imagemPerfil);
            txtDescComment = itemView.findViewById(R.id.txtDescComment);
            txtCountLike = itemView.findViewById(R.id.txtCountLike);
            txtNameUser = itemView.findViewById(R.id.txtNameUser);
            txtTime = itemView.findViewById(R.id.txtTime);
            like = itemView.findViewById(R.id.like);

        }
    }
}
