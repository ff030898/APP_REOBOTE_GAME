package com.reobotetechnology.reobotegame.ui.match;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.Message;
import com.reobotetechnology.reobotegame.model.Notification;
import com.reobotetechnology.reobotegame.model.MatchModel;
import com.reobotetechnology.reobotegame.model.QuestionModel;
import com.reobotetechnology.reobotegame.model.UserModel;
import com.reobotetechnology.reobotegame.ui.home.HomeActivity;
import com.reobotetechnology.reobotegame.utils.ChecarSegundoPlano;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MatchLoadingActivity extends AppCompatActivity {


    private int cronometro;
    Timer tempo;

    String nome, email, imagem, convidado = "";
    CircleImageView imagemPerfil, imagemPerfil2;
    TextView txtUsuario1, txtUsuario2, txtCronometro;
    LinearLayout linearLayout3;
    Button btnDesistir;
    TextView txtPartida;

    ProgressBar progressBar6;

    boolean desistir = true;
    boolean conectado = true;
    boolean internet = true;
    int cont = 0;


    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();

    String idPartida;


    //Pontos Usuario 1
    int pontosG;
    int vitorias;
    int derrotas;
    int empates;

    boolean bloquearSair = false;

    //Vem da Modal Desistir
    Dialog desistirDIalog;
    Button desistirModal;
    CardView desistirTimer;
    TextView txtdesistir;
    Animation modal_anima;

    //Animation
    private Animation topAnim;
    private ImageButton btn_back;

    //Toolbar
    TextView txt_title, txt_subtitle;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_match);

        imagemPerfil = findViewById(R.id.imagemPerfil);
        imagemPerfil2 = findViewById(R.id.imagemPerfil2);
        txtUsuario1 = findViewById(R.id.txtUsuario1);
        txtUsuario2 = findViewById(R.id.txtUsuario2);
        txtPartida = findViewById(R.id.txtPartida);
        btnDesistir = findViewById(R.id.btnDesistir);
        txtCronometro = findViewById(R.id.textTime);
        progressBar6 = findViewById(R.id.progressBar6);
        linearLayout3 = findViewById(R.id.linearConfirmation);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        //token = extras.getString("token");
        email = extras.getString("email");
        convidado = extras.getString("convidado");
        idPartida = extras.getString("idPartida");

        //TOOLBAR
        txt_title = findViewById(R.id.txt_title);
        txt_subtitle = findViewById(R.id.txt_subtitle);



        btn_back = findViewById(R.id.btn_back);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                btn_back.setAnimation(topAnim);

            }
        }, 2000);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        //Animação da Modal de Tempo Esgotado
        modal_anima = AnimationUtils.loadAnimation(this, R.anim.modal_animation);

        btnDesistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desistir();
            }
        });btnDesistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desistir();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    private void carregarPartida() {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");

        Calendar cal = Calendar.getInstance();
        Date data = new Date();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        if (convidado.equals("nao")) {

            //Cria uma partida
            idPartida = dateFormat.format(data_atual);
            MatchModel partida = new MatchModel(idPartida, false, false, false, true, "", idPartida);
            partida.salvar();

            List<QuestionModel> lista2;
            DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
            lista2 = dataBaseAcess.listarPerguntas();
            for (int i = 0; i < lista2.size(); i++) {

                QuestionModel p = new QuestionModel(
                        i + 1,
                        lista2.get(i).getPergunta(),
                        lista2.get(i).getQuestaoA(),
                        lista2.get(i).getQuestaoB(),
                        lista2.get(i).getQuestaoC(),
                        lista2.get(i).getQuestaoD(),
                        lista2.get(i).getQuestaoCorreta(),
                        lista2.get(i).getQuestaoDica());
                p.salvar(idPartida);

            }

            //Começa com 0 pontos cada jogador
            String jogador1 = Base64Custom.codificarBase64(Objects.requireNonNull(Objects.requireNonNull(autenticacao.getCurrentUser()).getEmail()));
            DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida).child(jogador1);
            usuarioRef.child("pontos").setValue(0);

            //Começa com 0 pontos cada jogador
            String jogador2 = Base64Custom.codificarBase64(Objects.requireNonNull(Objects.requireNonNull(email)));
            DatabaseReference usuarioRef2 = firebaseRef.child("partidas").child(idPartida).child(jogador2);
            usuarioRef2.child("pontos").setValue(0);

            enviarNotificacao();


            if (txtPartida.getText().equals("AGUARDANDO RESPOSTA")) {
                verificarConfirmacao();
            }

        } else {

            verificarConfirmacao();
        }

    }

    private void jogando(boolean b) {
        String emailUsuario = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            emailUsuario = Objects.requireNonNull(autenticacao.getCurrentUser()).getEmail();
        }
        assert emailUsuario != null;
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
        usuarioRef.child("jogando").setValue(b);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void verificarConfirmacao() {

        try {

            DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);

            usuarioRef.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        String aceito = Objects.requireNonNull(dataSnapshot.child("aceito").getValue()).toString();
                        String desconectado = Objects.requireNonNull(dataSnapshot.child("desconectado").getValue()).toString();
                        String internet2 = Objects.requireNonNull(dataSnapshot.child("internet").getValue()).toString();
                        String recusado = Objects.requireNonNull(dataSnapshot.child("recusado").getValue()).toString();

                        if (aceito.equals("true")) {
                            btnDesistir.setEnabled(false);
                            btnDesistir.setAlpha(0.7f);
                            desistir = false;
                            txtPartida.setText("CONVITE ACEITO. CARREGANDO PARTIDA.");
                            if (cont == 0) {
                                cont++;
                                Timer();
                            }

                        } else if (recusado.equals("true")) {

                            desistir = true;
                            jogando(false);
                            DatabaseReference usuarioRef = firebaseRef.child("partidas");
                            usuarioRef.child(idPartida).removeValue();
                            String msg = "O jogador(a) " + nome + " rejeitou o seu convite";
                            try {
                                boolean foregroud = new ChecarSegundoPlano().execute(getApplicationContext()).get();
                                if (foregroud) {
                                    modalAviso(msg);
                                }

                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                        if (desconectado.equals("true") || internet2.equals("false")) {
                            jogando(false);
                            conectado = false;
                        }

                        if (internet2.equals("false")) {
                            jogando(false);
                            internet = false;
                        }

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

    private void modalAviso(String msg) {
        desistirDIalog = new Dialog(this);
        desistirDIalog.setContentView(R.layout.include_modal_warning);

        desistirTimer = desistirDIalog.findViewById(R.id.cardTimer);
        desistirTimer.startAnimation(modal_anima);
        desistirModal = desistirDIalog.findViewById(R.id.btnTimer);
        txtdesistir = desistirDIalog.findViewById(R.id.txtDesistir);

        txtdesistir.setText(msg);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(desistirDIalog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        desistirDIalog.setCancelable(false);

        desistirDIalog.show();

        desistirModal.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                desistirDIalog.dismiss();
                if (convidado.equals("nao")) {
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
            }
        });
        //Modal de Recusado e Finish()
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void carregaUser() {

        String[] linhas = Objects.requireNonNull(user.getDisplayName()).split(" ");
        txtUsuario1.setText(linhas[0]);

        try {

            if (user.getPhotoUrl() == null) {

                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.profile)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(imagemPerfil);
            } else {

                Glide
                        .with(getApplicationContext())
                        .load(user.getPhotoUrl())
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(imagemPerfil);
            }

        } catch (Exception e) {
            Glide
                    .with(getApplicationContext())
                    .load(R.drawable.profile)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(imagemPerfil);


        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void carregaUser2() {

        String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(email));
        firebaseRef.child("usuarios").child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserModel usuarioModel = dataSnapshot.getValue(UserModel.class);

                assert usuarioModel != null;
                nome = usuarioModel.getNome();
                String[] nameInvite = nome.split(" ");
                txtUsuario2.setText(nameInvite[0]);

                String[] userMatch = Objects.requireNonNull(user.getDisplayName()).split(" ");

                txt_title.setText(userMatch[0] +" vs "+nameInvite[0]);
                txt_subtitle.setText(getString(R.string.descriptionMatch));

                try {

                    imagem = usuarioModel.getImagem();

                    if (imagem.isEmpty()) {
                        Glide
                                .with(getApplicationContext())
                                .load(R.drawable.profile)
                                .centerCrop()
                                .placeholder(R.drawable.profile)
                                .into(imagemPerfil2);
                    } else {

                        Glide
                                .with(getApplicationContext())
                                .load(imagem)
                                .centerCrop()
                                .placeholder(R.drawable.profile)
                                .into(imagemPerfil2);
                    }
                } catch (Exception e) {
                    Glide
                            .with(getApplicationContext())
                            .load(R.drawable.profile)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(imagemPerfil2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Timer() {

        cronometro = 10;
        txtCronometro.setText("10");

        if (tempo != null) {
            tempo.cancel();
            tempo = null;
        }
        tempo = new Timer();
        MatchLoadingActivity.Task task = new MatchLoadingActivity.Task();
        tempo.schedule(task, 1000, 1000);

    }

    class Task extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {

                    progressBar6.setVisibility(View.GONE);
                    linearLayout3.setVisibility(View.VISIBLE);

                    if (conectado) {

                        if (cronometro > 0) {

                            cronometro = cronometro - 1;
                            txtCronometro.setText("0" + cronometro);
                            if (cronometro == 3) {
                                txtPartida.setText("ATENÇÃO! A PARTIDA JÁ VAI COMEÇAR...");
                            }

                        } else {

                            try {
                                tempo.cancel();
                                boolean foregroud = new ChecarSegundoPlano().execute(getApplicationContext()).get();
                                if (foregroud) {
                                    Intent i = new Intent(getApplicationContext(), MatchActivity.class);
                                    String[] linhas = nome.split(" ");
                                    i.putExtra("id", idPartida);
                                    i.putExtra("nome", linhas[0]);
                                    i.putExtra("email", email);
                                    i.putExtra("imagem", imagem);
                                    startActivity(i);
                                    desistir = true;
                                    finish();
                                }

                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                    } else if (!internet) {
                        tempo.cancel();
                        desistir = true;
                        DatabaseReference usuarioRef = firebaseRef.child("partidas");
                        usuarioRef.child(idPartida).removeValue();
                        jogando(false);
                        String msg = "O jogador(a) " + nome + " perdeu a conexão com a internet!";
                        try {
                            boolean foregroud = new ChecarSegundoPlano().execute(getApplicationContext()).get();
                            if (foregroud) {
                                modalAviso(msg);
                            }

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } else {
                        tempo.cancel();
                        jogando(false);
                        DatabaseReference usuarioRef = firebaseRef.child("partidas");
                        usuarioRef.child(idPartida).removeValue();
                        desistir = true;
                        String msg = "O jogador(a) " + nome + " desistiu da partida.\nVocê venceu.\n+10 pontos";
                        try {
                            boolean foregroud = new ChecarSegundoPlano().execute(getApplicationContext()).get();
                            if (foregroud) {
                                modalAviso(msg);
                            }

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }


            });
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("InflateParams")

    private void esperar() {
        if (cont == 0) {
            try {

                new SweetAlertDialog(MatchLoadingActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Sem resposta")
                        .setContentText("Seu convite ainda não foi aceito. Deseja desistir da partida? ")
                        .setConfirmText("Sim")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                DatabaseReference usuarioRef = firebaseRef.child("partidas");
                                usuarioRef.child(idPartida).removeValue();
                                sDialog.hide();
                                finish();
                            }
                        }).setCancelText("Não")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.hide();
                            }
                        })
                        .show();
            } catch (
                    Exception ignored) {

            }
        }
    }

    private void desistir() {

        new SweetAlertDialog(MatchLoadingActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Desistir da Partida")
                .setContentText("Você tem certeza que deseja desistir da partida ?")
                .setConfirmText("Sim")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);
                        usuarioRef.child("desconectado").setValue(true);
                        jogando(false);
                        finish();
                        sDialog.hide();

                    }
                }).setCancelText("Não")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        bloquearSair = false;
                        jogando(true);
                        sweetAlertDialog.hide();
                    }
                })
                .show();

    }

    private void desistirP() {

        new SweetAlertDialog(MatchLoadingActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Desistir da Partida")
                .setContentText("Atenção o Jogador(a): " + nome + " já aceitou o convite. Se você desistir da partida vai perder -3 pontos. Tem certeza que deseja desistir ?")
                .setConfirmText("Sim")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        tempo.cancel();
                        DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);
                        usuarioRef.child("resultado").setValue(email);
                        usuarioRef.child("desconectado").setValue(true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            atualizarPontosJogador1(-3);
                            atualizarResultadoPartida(email);
                        }
                        jogando(false);
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        sDialog.hide();
                        finish();
                    }
                }).setCancelText("Não")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        bloquearSair = false;
                        jogando(true);
                        sweetAlertDialog.hide();
                    }
                })
                .show();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperarPontosJogador1() {

        String emailUsuario = Objects.requireNonNull(autenticacao.getCurrentUser()).getEmail();
        assert emailUsuario != null;
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String pontosGeral = Objects.requireNonNull(dataSnapshot.child("pontosG").getValue()).toString();
                String vitoriasText = Objects.requireNonNull(dataSnapshot.child("vitorias").getValue()).toString();
                String derrotasText = Objects.requireNonNull(dataSnapshot.child("derrotas").getValue()).toString();
                String empatesText = Objects.requireNonNull(dataSnapshot.child("empates").getValue()).toString();

                pontosG = Integer.parseInt(pontosGeral);
                vitorias = Integer.parseInt(vitoriasText);
                derrotas = Integer.parseInt(derrotasText);
                empates = Integer.parseInt(empatesText);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void atualizarPontosJogador1(int pontos) {

        String emailUsuario = Objects.requireNonNull(autenticacao.getCurrentUser()).getEmail();
        assert emailUsuario != null;
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
        int pontosGeral = pontosG + pontos;


        usuarioRef.child("pontosG").setValue(pontosGeral);

        if (pontos == 10) {
            int vitoria = vitorias + 1;
            usuarioRef.child("vitorias").setValue(vitoria);
        } else if (pontos == 5) {
            int empate = empates + 1;
            usuarioRef.child("vitorias").setValue(empate);
        } else if (pontos == -3) {
            int derrota = derrotas + 1;
            usuarioRef.child("derrotas").setValue(derrota);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void atualizarResultadoPartida(String email) {

        DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);
        usuarioRef.child("resultado").setValue(email);
        usuarioRef.child("desconectado").setValue(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void enviarNotificacao() {

        try {

            String emailUsuario = Objects.requireNonNull(user.getEmail());
            String idUsuario = Base64Custom.codificarBase64(emailUsuario);

            String emailUsuario2 = Objects.requireNonNull(email);
            String idUsuario2 = Base64Custom.codificarBase64(emailUsuario2);

            final String fromId = idUsuario;
            final String toId = idUsuario2;
            long timestamp = System.currentTimeMillis();

            final Message message = new Message();
            message.setFromId(fromId);
            message.setToId(toId);
            message.setTimestamp("" + timestamp);
            message.setText("Lhe enviou um convite para jogar");

            if (!message.getText().isEmpty() && convidado.equals("nao")) {

                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatNotification = new SimpleDateFormat("dd-MM-yyyy");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                Calendar cal = Calendar.getInstance();
                Date data = new Date();
                cal.setTime(data);
                Date data_atual = cal.getTime();

                String dateNotification = dateFormatNotification.format(data);
                String time = timeFormat.format(data_atual);

                Notification notification = new Notification();
                notification.setFromId(message.getFromId());
                notification.setToId(message.getToId());
                notification.setTimestamp(message.getTimestamp());
                notification.setText(message.getText());
                notification.setTipo("partida");
                notification.setFromName(user.getDisplayName());
                notification.setId(idPartida);
                notification.setDate(dateNotification);
                notification.setTime(time);
                if (user.getPhotoUrl() != null) {
                    notification.setFromImage(user.getPhotoUrl().toString());
                } else {
                    notification.setFromImage("");
                }
                notification.setView(false);
                DatabaseReference usuarioRef = firebaseRef.child("notifications");
                usuarioRef.child(idUsuario2).child("" + timestamp).setValue(notification);


            }
        }catch(Exception ignored){

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ShowToast")
    @Override
    public void onBackPressed() {

        if (desistir) {
            desistir();
        } else {
            desistirP();
        }

        if (!bloquearSair) {
            super.onBackPressed();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStart() {
        carregaUser();
        carregaUser2();
        recuperarPontosJogador1();
        carregarPartida();
        jogando(true);

        if (cont == 0 && desistir) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    esperar();
                }
            }, 60000);
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if(!desistir){
            DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);
            usuarioRef.child("desconectado").setValue(true);
            jogando(false);
        }
        super.onStop();
    }
}
