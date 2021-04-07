package com.reobotetechnology.reobotegame.ui.messages;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.MessagesAdapters;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.MensagensModel;
import com.reobotetechnology.reobotegame.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MessagesListActivity extends AppCompatActivity {

    //SwipeRefresh
    private SwipeRefreshLayout swipeRefresh;

    private MessagesAdapters adapter;
    private List<MensagensModel> listMessages = new ArrayList<>();
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();


    private ProgressBar progressBar;

    //Animation
    private Animation topAnim;

    private CoordinatorLayout constraintMain;
    private ImageButton btn_back;


    String data_completa, hora_atual;
    Date data;

    String idUpdate;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        // OU
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm");

        Calendar cal = Calendar.getInstance();
        data = new Date();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        //Vem da Activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idUpdate = extras.getString("idUpdate");
        }

        data_completa = dateFormat.format(data_atual);

        hora_atual = dateFormat_hora.format(data_atual);

        swipeRefresh = findViewById(R.id.swipe);

        //Refresh
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        onStart();
                        swipeRefresh.setRefreshing(false);
                    }
                }, 2000);

            }
        });

        //Configurações iniciais
        progressBar = findViewById(R.id.progressBar);
        constraintMain = findViewById(R.id.constraintMain);
        RecyclerView recyclerMessages = findViewById(R.id.recyclerMessages);


        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        txt_title.setText(getString(R.string.mensagens));
        txt_subtitle.setText(getString(R.string.acompanhe_diariamente_para_ser_edificado));

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

        adapter = new MessagesAdapters(listMessages, getApplicationContext());

        //RecyclerMessages
        RecyclerView.LayoutManager layoutManagerMenu2 = new LinearLayoutManager(getApplicationContext());
        recyclerMessages.setLayoutManager(layoutManagerMenu2);
        recyclerMessages.setHasFixedSize(true);
        recyclerMessages.setAdapter(adapter);

        //Configurar evento de clique no recyclerview
        recyclerMessages.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerMessages,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onItemClick(View view, int position) {

                                Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                                i.putExtra("nome", getString(R.string.name_robot));
                                i.putExtra("imagem", R.drawable.reobote);
                                startActivity(i);

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                              getDeleted();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

    }

    private void listMessages() {

        listMessages.clear();

        UserModel amigo = new UserModel("1", getString(R.string.name_robot), "reobote@gmail.com", "", "",
                "", "", "", 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, false, false, false, false, false,
                false, false, false, false);

        MensagensModel m = new MensagensModel(1, amigo, amigo, data, hora_atual,
                "Olá, "+user.getDisplayName()+" Tudo bem? Eu sou a inteligência artifical do Reobote Game. É um prazer te conhecer.\n\nVocê é uma Bênção", 0, "", 1, false);

        listMessages.add(m);

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

    private void getDeleted(){
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apagar conversa")
                .setContentText("Tem certeza que deseja apagar a conversa com Reobote IA ?")
                .setConfirmText("Sim")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        listMessages.clear();
                        adapter.notifyDataSetChanged();
                        sDialog.hide();
                    }
                }).setCancelText("Não")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.hide();
                    }
                })
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        listMessages();
    }
}