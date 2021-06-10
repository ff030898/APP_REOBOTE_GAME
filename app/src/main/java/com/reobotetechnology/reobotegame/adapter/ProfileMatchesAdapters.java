package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.helper.ConfigurationFirebase;
import com.reobotetechnology.reobotegame.model.MatchModel;
import com.reobotetechnology.reobotegame.model.UserModel;
import com.reobotetechnology.reobotegame.utils.ChecarSegundoPlano;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileMatchesAdapters extends RecyclerView.Adapter<ProfileMatchesAdapters.myViewHolder> {


    //Configurações do banco de dados
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();

    private List<MatchModel> listMatch;
    private Context context;

    public ProfileMatchesAdapters(List<MatchModel> listMatch, Context c) {
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
    public void onBindViewHolder(@NonNull final myViewHolder holder, int position) {

        MatchModel match = listMatch.get(position);

        holder.txt_datetime.setText(match.getDatetime());

        try {

            firebaseRef.child("usuarios").child(match.getEmailUser()).addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        UserModel user = dataSnapshot.getValue(UserModel.class);

                        assert user != null;

                        String[] username = user.getNome().split(" ");
                        holder.txt_username1.setText(username[0]);

                        Glide
                                .with(context)
                                .load(user.getImagem())
                                .centerCrop()
                                .placeholder(R.drawable.profile)
                                .into(holder.img_user1);
                    } catch (Exception ignored) {

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception ignored) {

        }


        String emailRobot = Base64Custom.codificarBase64(context.getString(R.string.email_robot));

        if (match.getEmailUser2().equals(emailRobot)) {

            String[] username2 = context.getString(R.string.name_robot).split(" ");
            holder.txt_username2.setText(username2[0]);

            Glide
                    .with(context)
                    .load(R.drawable.reobote)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(holder.img_user2);

        } else {
            try {

                firebaseRef.child("usuarios").child(match.getEmailUser2()).addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        try {
                            UserModel user = dataSnapshot.getValue(UserModel.class);


                            assert user != null;

                            String[] username = user.getNome().split(" ");
                            holder.txt_username2.setText(username[0]);

                            Glide
                                    .with(context)
                                    .load(user.getImagem())
                                    .centerCrop()
                                    .placeholder(R.drawable.profile)
                                    .into(holder.img_user2);
                        } catch (Exception ignored) {

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } catch (Exception ignored) {

            }

        }


        //holder.border.setBackground(ColorStateList.valueOf(R.color.colorBlue));
        switch (match.getResultado()) {
            case "derrota":
                holder.border.setBackgroundTintList(ColorStateList.valueOf(0xff9B111E));
                holder.txt_placer1.setText(match.getScoreUser());
                holder.txt_placer2.setText(match.getScoreUser2());
                break;
            case "empate":
                holder.border.setBackgroundTintList(ColorStateList.valueOf(0xffAE841A));
                holder.txt_placer1.setText(match.getScoreUser());
                holder.txt_placer2.setText(match.getScoreUser2());
                break;
            case "vitoria":
                holder.border.setBackgroundTintList(ColorStateList.valueOf(0xff008000));
                holder.txt_placer1.setText(match.getScoreUser());
                holder.txt_placer2.setText(match.getScoreUser2());
                break;
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
