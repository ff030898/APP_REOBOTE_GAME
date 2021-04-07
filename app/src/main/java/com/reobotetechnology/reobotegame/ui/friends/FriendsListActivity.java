package com.reobotetechnology.reobotegame.ui.friends;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.FriendsRectangleAdapters;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity {

    private FriendsRectangleAdapters adapter;
    private List<UserModel> listFriends = new ArrayList<>();
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();


    private ProgressBar progressBar;

    //Animation
    private Animation topAnim;

    private CoordinatorLayout constraintMain;
    private ImageButton btn_back;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        //token = extras.getString("token");
        String eventList = extras.getString("eventList");


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
        RecyclerView.LayoutManager layoutManagerMenu2 = new LinearLayoutManager(getApplicationContext());
        recyclerFriends.setLayoutManager(layoutManagerMenu2);
        recyclerFriends.setHasFixedSize(true);
        recyclerFriends.setAdapter(adapter);


        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        assert eventList != null;
        if (eventList.equals(getString(R.string.amigos_sugeridosM))) {
            txt_title.setText(eventList);
            txt_subtitle.setText(getString(R.string.pessoas_recomendadas_que_voc_talvez_conhe_a));
        } else if (eventList.equals(getString(R.string.seguidoresMin))) {
            txt_title.setText(eventList);
            txt_subtitle.setText(getString(R.string.descriptionFollow2) + " " + listFriends.size() + " " + eventList.toLowerCase());
        } else if (eventList.equals(getString(R.string.seguindoMin))) {
            txt_title.setText(eventList);
            txt_subtitle.setText(getString(R.string.descriptionFollow) + " " + listFriends.size() + " " + getString(R.string.pessoas));
        }

    }

    private void listFriends() {

        firebaseRef.child("usuarios").orderByChild("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listFriends.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    UserModel usuario2Model = dados.getValue(UserModel.class);

                    assert usuario2Model != null;
                    if (!usuario2Model.getEmail().equals(user.getEmail())) {
                        listFriends.add(usuario2Model);
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


    @Override
    protected void onStart() {
        super.onStart();
        listFriends();
    }
}
