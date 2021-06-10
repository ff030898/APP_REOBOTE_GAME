package com.reobotetechnology.reobotegame.ui.oracion;

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
import com.reobotetechnology.reobotegame.adapter.OracionAdapters;
import com.reobotetechnology.reobotegame.adapter.PersonAdapters;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.model.OracionModel;

import java.util.ArrayList;
import java.util.List;

public class OracionListActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();


    private ProgressBar progressBar;

    //Animation
    private Animation topAnim;

    private CoordinatorLayout constraintMain;
    private ImageButton btn_back;

    //Oracion

    private OracionAdapters adapterOracions;
    private List<OracionModel> listOracion = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oracion_list);

        //Configurações iniciais
        progressBar = findViewById(R.id.progressBar);
        constraintMain = findViewById(R.id.constraintMain);
        RecyclerView recyclerListOracions = findViewById(R.id.recyclerListOracions);

        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        txt_title.setText("Oração");
        txt_subtitle.setText("Escolha uma campanha para participar");

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

        adapterOracions = new OracionAdapters(listOracion, getApplicationContext());

        //RecyclerOracions
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerListOracions.setLayoutManager(layoutManager);
        recyclerListOracions.setHasFixedSize(true);
        recyclerListOracions.setAdapter(adapterOracions);

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

    private void listOracion(){

        listOracion.clear();

        listOracion.add(new OracionModel("1", "21 Dias de Daniel", "", "03/06/21", "24/06/21", "23:00 - 00:00", "Em andamento", 10));
        listOracion.add(new OracionModel("2", "150 Dias de Salmos", "", "09/06/21", "31/08/21", "23:00 - 00:00", "Aguardando", 10));
        listOracion.add(new OracionModel("3", "12 Dias de Gratidão", "", "05/06/21", "1/06/21", "23:00 - 00:00", "Em andamento", 10));

        adapterOracions.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        listOracion();
        super.onStart();
    }
}
