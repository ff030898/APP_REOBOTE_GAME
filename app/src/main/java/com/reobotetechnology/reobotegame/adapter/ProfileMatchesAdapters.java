package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.helper.ConfigurationFirebase;
import com.reobotetechnology.reobotegame.model.ConquistesModel;
import com.reobotetechnology.reobotegame.model.PartidaModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileMatchesAdapters extends RecyclerView.Adapter<ProfileMatchesAdapters.myViewHolder> {


    //Configurações do banco de dados
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();

    private List<PartidaModel> listMatch;
    private Context context;

    public ProfileMatchesAdapters(List<PartidaModel> listMatch, Context c) {
        this.listMatch = listMatch;
        this.context = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_matches, parent, false);
        return new myViewHolder(itemLista);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        //configuracoes de objetos
        autenticacao = ConfigurationFirebase.getFirebaseAutenticacao();
        user = autenticacao.getCurrentUser();


        PartidaModel match = listMatch.get(position);

        holder.txt_datetime.setText(match.getId());
        String username[] = user.getDisplayName().split(" ");
        holder.txt_username1.setText(username[0]);
        holder.txt_username2.setText("Usuario2");


        if (autenticacao.getCurrentUser() != null) {
            if (user.getPhotoUrl() == null) {
                Glide
                        .with(context)
                        .load(R.drawable.profile)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(holder.img_user1);
            } else {
                Glide
                        .with(context)
                        .load(user.getPhotoUrl())
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(holder.img_user1);
            }


        } else {
            Glide
                    .with(context)
                    .load(user.getPhotoUrl())
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(holder.img_user1);
        }

        Glide
                .with(context)
                .load(R.drawable.profile)
                .centerCrop()
                .placeholder(R.drawable.profile)
                .into(holder.img_user2);


        //holder.border.setBackground(ColorStateList.valueOf(R.color.colorBlue));
        if (position == 1) {
            holder.border.setBackgroundTintList(ColorStateList.valueOf(0xff9B111E));
            holder.txt_placer1.setText("2");
            holder.txt_placer2.setText("6");
        } else if (position == 2) {
            holder.border.setBackgroundTintList(ColorStateList.valueOf(0xffAE841A));
            holder.txt_placer1.setText("7");
            holder.txt_placer2.setText("7");
        } else {
            holder.border.setBackgroundTintList(ColorStateList.valueOf(0xff008000));
            holder.txt_placer1.setText("6");
            holder.txt_placer2.setText("3");
        }

    }

    @Override
    public int getItemCount() {
        return listMatch.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView txt_datetime, txt_placer1, txt_placer2, txt_username1, txt_username2;
        CircleImageView img_user1, img_user2;
        View border;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_datetime = itemView.findViewById(R.id.txt_datetime);
            txt_placer1 = itemView.findViewById(R.id.txt_placer1);
            txt_placer2 = itemView.findViewById(R.id.txt_placer2);
            txt_username1 = itemView.findViewById(R.id.txt_username1);
            txt_username2 = itemView.findViewById(R.id.txt_username2);
            img_user1 = itemView.findViewById(R.id.img_user);
            img_user2 = itemView.findViewById(R.id.img_user4);
            border = itemView.findViewById(R.id.divider2);

        }
    }


}
