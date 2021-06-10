package com.reobotetechnology.reobotegame.ui.persons;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.DatabaseReference;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.PersonAdapters;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.model.PersonModel;

import java.util.ArrayList;
import java.util.List;

public class PersonsListActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();


    private ProgressBar progressBar;

    //Animation
    private Animation topAnim;

    private CoordinatorLayout constraintMain;
    private ImageButton btn_back;

    //Person
    private PersonAdapters adapterPerson;
    private List<PersonModel> listPerson = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons_list);

        //Configurações iniciais
        progressBar = findViewById(R.id.progressBar);
        constraintMain = findViewById(R.id.constraintMain);
        RecyclerView recyclerListPersons = findViewById(R.id.recyclerListPersons);


        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        txt_title.setText("Personagens");
        txt_subtitle.setText("Escolha um personagem para conhecer");

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

        adapterPerson = new PersonAdapters(listPerson, getApplicationContext());

        //RecyclerFriends
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerListPersons.setLayoutManager(layoutManager);
        recyclerListPersons.setAdapter(adapterPerson);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                constraintMain.setVisibility(View.VISIBLE);
                btn_back.setAnimation(topAnim);

            }
        }, 2000);
    }

    private void listPerson() {

        listPerson.clear();

        listPerson.add(new PersonModel("1", "Elias", ""));
        listPerson.add(new PersonModel("2", "Moises", ""));
        listPerson.add(new PersonModel("3", "Noé", ""));
        listPerson.add(new PersonModel("1", "Elias", ""));
        listPerson.add(new PersonModel("2", "Moises", ""));
        listPerson.add(new PersonModel("3", "Noé", ""));

        adapterPerson.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        listPerson();
        super.onStart();
    }
}
