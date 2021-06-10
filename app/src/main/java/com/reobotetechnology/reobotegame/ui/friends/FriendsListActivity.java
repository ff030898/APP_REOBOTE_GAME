package com.reobotetechnology.reobotegame.ui.friends;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.FriendsRectangleAdapters;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendsListActivity extends AppCompatActivity {

    private FriendsRectangleAdapters adapter;
    private List<UserModel> listFriends = new ArrayList<>();
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();


    private ProgressBar progressBar;

    private TextView txt_subtitle;

    //Animation
    private Animation topAnim;

    private CoordinatorLayout constraintMain;
    private ImageButton btn_back;

    private String child = "seguidores";
    private String child2 = "seguindo";
    private String eventList, userSearch, userName;

    private List<String> usersList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        //token = extras.getString("token");
        eventList = extras.getString("eventList");
        userSearch = extras.getString("userSearch");
        userName = extras.getString("userName");

        //Configurações iniciais
        progressBar = findViewById(R.id.progressBar);
        constraintMain = findViewById(R.id.constraintMain);
        RecyclerView recyclerFriends = findViewById(R.id.recyclerFriends);


        btn_back = findViewById(R.id.btn_back);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        constraintMain.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new FriendsRectangleAdapters(listFriends, getApplicationContext());

        //RecyclerFriends
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerFriends.setLayoutManager(layoutManager);
        recyclerFriends.setHasFixedSize(true);
        recyclerFriends.setAdapter(adapter);


        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        txt_subtitle = findViewById(R.id.txt_subtitle);

        assert eventList != null;
        if (eventList.equals(getString(R.string.amigos_sugeridosM))) {
            txt_title.setText(eventList);
            txt_subtitle.setText(getString(R.string.pessoas_recomendadas_que_voc_talvez_conhe_a));
            listFriends();
        } else if (eventList.equals(getString(R.string.seguidoresMin))) {
            if(userSearch != null){
                String idUser = Base64Custom.codificarBase64(Objects.requireNonNull(userSearch));
                listFriendsFollow(idUser);
                txt_title.setText(eventList + " de "+userName);

            }else {
                String idUser = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));
                listFriendsFollow(idUser);
                txt_title.setText(eventList);
            }
        } else if (eventList.equals(getString(R.string.seguindoMin))) {
            if(userSearch != null){
                String idUser = Base64Custom.codificarBase64(Objects.requireNonNull(userSearch));
                listFriendsFollow2(idUser);
                txt_title.setText(eventList);
            }else {
                String idUser = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));
                listFriendsFollow2(idUser);
                txt_title.setText(eventList);
            }
        }

    }

    private void listFriends() {

        firebaseRef.child("usuarios").orderByChild("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usersList.clear();
                listFriends.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    final UserModel usuario2Model = dados.getValue(UserModel.class);

                    assert usuario2Model != null;
                    if (!usuario2Model.getEmail().equals(user.getEmail())) {
                        String idUser = Base64Custom.codificarBase64(usuario2Model.getEmail());
                        firebaseRef.child(child).child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                try {
                                    Objects.requireNonNull(dataSnapshot.getValue());
                                    usuario2Model.setFollow(true);

                                } catch (Exception e) {
                                    usuario2Model.setFollow(false);
                                    listFriends.add(usuario2Model);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }

                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                constraintMain.setVisibility(View.VISIBLE);
                btn_back.setAnimation(topAnim);

            }
        }, 2000);


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void listFriendsFollow(String idUser) {

        usersList.clear();


        firebaseRef.child(child).child(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    String idUser = Objects.requireNonNull(dados.getValue()).toString();
                    usersList.add(idUser);
                    listFriendId(usersList);

                }

                if (usersList.size() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            constraintMain.setVisibility(View.VISIBLE);
                            btn_back.setAnimation(topAnim);

                        }
                    }, 2000);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void listFriendsFollow2(String idUser) {

        usersList.clear();

        firebaseRef.child(child2).child(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    String idUser = dados.getValue().toString();
                    usersList.add(idUser);
                    listFriendId2(usersList);

                }

                if (usersList.size() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            constraintMain.setVisibility(View.VISIBLE);
                            btn_back.setAnimation(topAnim);

                        }
                    }, 2000);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void listFriendId(final List<String> usersList) {
        firebaseRef.child("usuarios").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listFriends.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    final UserModel usuario2Model = dados.getValue(UserModel.class);

                    for (int i = 0; i < usersList.size(); i++) {
                        assert usuario2Model != null;
                        String email = Base64Custom.codificarBase64(usuario2Model.getEmail());
                        if (email.equals(usersList.get(i))) {
                            firebaseRef.child(child).child(usersList.get(i)).addValueEventListener(new ValueEventListener() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    try {
                                        Objects.requireNonNull(dataSnapshot.getValue());
                                        usuario2Model.setFollow(true);

                                    } catch (Exception e) {
                                        usuario2Model.setFollow(false);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            listFriends.add(usuario2Model);
                            if(userSearch != null){

                                txt_subtitle.setText(userName + " tem " + listFriends.size() + " " + eventList.toLowerCase());

                            }else {

                                txt_subtitle.setText(getString(R.string.descriptionFollow2) + " " + listFriends.size() + " " + eventList.toLowerCase());
                            }
                        }
                    }


                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                constraintMain.setVisibility(View.VISIBLE);
                btn_back.setAnimation(topAnim);

            }
        }, 2000);
    }

    private void listFriendId2(final List<String> usersList) {
        firebaseRef.child("usuarios").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listFriends.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    UserModel usuario2Model = dados.getValue(UserModel.class);

                    for (int i = 0; i < usersList.size(); i++) {
                        assert usuario2Model != null;
                        String email = Base64Custom.codificarBase64(usuario2Model.getEmail());
                        if (email.equals(usersList.get(i))) {
                            usuario2Model.setFollow(true);
                            listFriends.add(usuario2Model);
                        }
                    }
                }

                if(userSearch != null){

                    txt_subtitle.setText(userName + " está seguindo " + listFriends.size() + " " + getString(R.string.pessoas));
                }else {

                    txt_subtitle.setText(getString(R.string.descriptionFollow) + " " + listFriends.size() + " " + getString(R.string.pessoas));
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                constraintMain.setVisibility(View.VISIBLE);
                btn_back.setAnimation(topAnim);

            }
        }, 2000);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}
