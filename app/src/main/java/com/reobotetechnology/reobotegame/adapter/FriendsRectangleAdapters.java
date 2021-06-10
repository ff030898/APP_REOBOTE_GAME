package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.reobotetechnology.reobotegame.model.Message;
import com.reobotetechnology.reobotegame.model.Notification;
import com.reobotetechnology.reobotegame.model.UserModel;
import com.reobotetechnology.reobotegame.ui.friends.FriendProfileActivity;
import com.reobotetechnology.reobotegame.utils.VerificyFollowUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsRectangleAdapters extends RecyclerView.Adapter<FriendsRectangleAdapters.myViewHolder> {

    private List<UserModel> usuario;
    private Context context;

    private int seguidores = 0;
    private int seguindoFollow = 0;

    private String child = "seguidores";
    private String child2 = "seguindo";

    //Configurações do banco de dados
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();

    public FriendsRectangleAdapters(List<UserModel> listaUsuario, Context c) {
        this.usuario = listaUsuario;
        this.context = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_friends_list, parent, false);
        return new myViewHolder(itemLista);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {

        final UserModel userModel = usuario.get(position);

        holder.userName.setText(userModel.getNome());
        holder.userPositionRanking.setText(userModel.getRanking() + "º");


        if (userModel.isFollow()) {
            holder.buttonMatch.setBackground(context.getResources().getDrawable(R.drawable.btn_screen));
            holder.buttonMatch.setTextColor(ColorStateList.valueOf(0xffffffff));
            holder.buttonMatch.setText(context.getString(R.string.seguindo));
        }

        try {

            if (userModel.getImagem().isEmpty()) {
                holder.profileImage.setImageResource(R.drawable.profile);


            } else {
                Glide
                        .with(context)
                        .load(userModel.getImagem())
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(holder.profileImage);

            }
        } catch (Exception e) {
            holder.profileImage.setImageResource(R.drawable.profile);
        }

        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, FriendProfileActivity.class);
                i.putExtra("id", userModel.getEmail());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });


        holder.buttonMatch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                String emailUsuario = Objects.requireNonNull(user.getEmail());
                final String idUsuario = Base64Custom.codificarBase64(emailUsuario);

                final String emailUsuario2 = Objects.requireNonNull(userModel.getEmail());
                final String idUsuario2 = Base64Custom.codificarBase64(emailUsuario2);


                final String idUserAuthenticate = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));

                firebaseRef.child(child).child(idUsuario2).addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        try {
                            dataSnapshot.child(idUserAuthenticate).getValue().toString();

                            holder.buttonMatch.setBackground(context.getResources().getDrawable(R.drawable.btn_google));
                            holder.buttonMatch.setTextColor(ColorStateList.valueOf(0xff707070));
                            holder.buttonMatch.setText(context.getString(R.string.seguir));

                            Toast.makeText(context, "Você deixou de seguir " + userModel.getNome(), Toast.LENGTH_LONG).show();

                            nowFollow(idUsuario2);



                        } catch (Exception e) {

                            followUser(emailUsuario2);
                            sendNotification(idUsuario, idUsuario2);

                            holder.buttonMatch.setBackground(context.getResources().getDrawable(R.drawable.btn_screen));
                            holder.buttonMatch.setTextColor(ColorStateList.valueOf(0xffffffff));
                            holder.buttonMatch.setText(context.getString(R.string.seguindo));
                            Toast.makeText(context.getApplicationContext(), "Você começou a seguir " + userModel.getNome(), Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        });


    }

    @Override
    public int getItemCount() {
        return usuario.size();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void followUser(String email) {

        String emailUsuario2 = Objects.requireNonNull(email);
        final String idUsuario = Base64Custom.codificarBase64(emailUsuario2);

        //Update Follow
        DatabaseReference follow = firebaseRef.child(child);
        final String idUserAutenticate = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));
        follow.child(idUsuario).child(idUserAutenticate).setValue(idUserAutenticate);

        DatabaseReference follow2 = firebaseRef.child(child2);
        follow2.child(idUserAutenticate).child(idUsuario).setValue(idUsuario);

        firebaseRef.child("usuarios").child(idUserAutenticate).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserModel user = dataSnapshot.getValue(UserModel.class);

                if (user != null) {
                    int seguindoFollow = user.getSeguindo();
                    int newUpdateFollow = seguindoFollow + 1;
                    //Update Follow
                    DatabaseReference follow = firebaseRef.child("usuarios");
                    follow.child(idUserAutenticate).child("seguindo").setValue(newUpdateFollow);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        firebaseRef.child("usuarios").child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserModel user = dataSnapshot.getValue(UserModel.class);

                if (user != null) {
                    int seguidores = user.getSeguidores();
                    int newUpdateFollow = seguidores + 1;
                    //Update Follow
                    DatabaseReference follow = firebaseRef.child("usuarios");
                    follow.child(idUsuario).child("seguidores").setValue(newUpdateFollow);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void nowFollow(final String idUsuario) {

        final String idUserAuthenticate = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));
        DatabaseReference usuarioRef = firebaseRef.child(child).child(idUsuario);
        usuarioRef.child(idUserAuthenticate).removeValue();

        DatabaseReference follow2 = firebaseRef.child(child2).child(idUserAuthenticate);
        follow2.child(idUsuario).removeValue();
        follow2.child(idUsuario).removeValue();

        firebaseRef.child("usuarios").child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserModel user = dataSnapshot.getValue(UserModel.class);

                if (user != null) {
                    int seguidores = user.getSeguidores();
                    int newUpdateFollow = seguidores - 1;
                    //Update Follow
                    DatabaseReference follow = firebaseRef.child("usuarios");
                    follow.child(idUsuario).child("seguidores").setValue(newUpdateFollow);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        firebaseRef.child("usuarios").child(idUserAuthenticate).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserModel user = dataSnapshot.getValue(UserModel.class);

                if (user != null) {
                    int seguindo = user.getSeguindo();
                    int newUpdateFollow = seguindo - 1;
                    //Update Follow
                    DatabaseReference follow = firebaseRef.child("usuarios");
                    follow.child(idUserAuthenticate).child("seguindo").setValue(newUpdateFollow);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void sendNotification(String idUsuario, String idUsuario2) {
        long timestamp = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatNotification = new SimpleDateFormat("dd-MM-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        Calendar cal = Calendar.getInstance();
        Date data = new Date();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        String dateNotification = dateFormatNotification.format(data);
        String time = timeFormat.format(data_atual);

        //Cria uma partida
        String idMessage = dateFormat.format(data_atual);

        final Message message = new Message();
        message.setFromId(idUsuario);
        message.setToId(idUsuario2);
        message.setTimestamp("" + timestamp);
        message.setText("Começou a seguir você");


        Notification notification = new Notification();
        notification.setFromId(message.getFromId());
        notification.setToId(message.getToId());
        notification.setTimestamp(message.getTimestamp());
        notification.setText(message.getText());
        notification.setTipo("follow");
        notification.setFromName(user.getDisplayName());
        notification.setId(idMessage);
        if (user.getPhotoUrl() != null) {
            notification.setFromImage(user.getPhotoUrl().toString());
        } else {
            notification.setFromImage("");
        }

        notification.setDate(dateNotification);
        notification.setTime(time);

        notification.setView(false);
        DatabaseReference usuarioRef = firebaseRef.child("notifications");
        usuarioRef.child(idUsuario2).child("" + timestamp).setValue(notification);

    }


    static class myViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImage;
        TextView userName, userPositionRanking;
        Button buttonMatch;
        ImageView online;

        myViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            userName = itemView.findViewById(R.id.userName);
            userPositionRanking = itemView.findViewById(R.id.textView4);
            buttonMatch = itemView.findViewById(R.id.buttonMatch);
            online = itemView.findViewById(R.id.online);

        }
    }
}
