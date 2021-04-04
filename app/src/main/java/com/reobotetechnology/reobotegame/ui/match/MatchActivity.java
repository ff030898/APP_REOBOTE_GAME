package com.reobotetechnology.reobotegame.ui.match;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;

import androidx.annotation.RequiresApi;

import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.annotation.SuppressLint;

import android.app.Dialog;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Build;

import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.QuestionModel;
import com.reobotetechnology.reobotegame.model.UserModel;
import com.reobotetechnology.reobotegame.ui.home.HomeActivity;
import com.reobotetechnology.reobotegame.utils.ChecarSegundoPlano;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.reobotetechnology.reobotegame.R.drawable.btn_quiz_timer_red;
import static com.reobotetechnology.reobotegame.R.drawable.btn_quiz_timer;

public class MatchActivity extends AppCompatActivity {

    //Itens da Interface
    TextView pergunta, indicador, acertos, acertos2;
    LinearLayout linearAlternativas, linearPergunta;
    Button btnProximo, btnSair, btn01, btn02, btn03, btn04, btnTimer;
    TextView txtUsuario1, txtUsuario2;
    CircleImageView imagemPerfil, imagemPerfil2;
    FloatingActionButton btnDica;
    //Seleciona uma alternativa até ocabar as 15 pode mudar
    Button alternativaSelecionada;

    Animation modal_anima;

    //Vem da Modal Desistir
    Dialog desistirDIalog;
    Button desistirModal;
    CardView desistirTimer;
    TextView txtdesistir;


    //Variaveis Globais
    private int count = 0;
    List<QuestionModel> list = new ArrayList<>();
    private int position = 0;
    private int score = 0, scoreJogador2 = 0;
    private int cronometro = 15;
    private boolean clicado;

    //Pontos Jogador1
    int pontosG;
    int vitorias;
    int derrotas;
    int empates;
    int partidas;

    //Conexões da Partida
    String idPartida;
    boolean conectado = true;
    boolean internet = true;
    boolean desistir = false;
    boolean sair = false;

    //Vem da Activity Carregar Partida
    String nomeJogador2, emailJogador2, imagem;

    //Configuração FireBase
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private FirebaseUser userF = autenticacao.getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();

    //Timer de 15 para cada pergunta
    Timer tempo;
    int cont = 0;


    @SuppressLint("SetTextI18n")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        //Vem da Activity CarregarPartida
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        idPartida = extras.getString("id");
        nomeJogador2 = extras.getString("nome");
        emailJogador2 = extras.getString("email");
        imagem = extras.getString("imagem");

        //Componentes da Interface
        txtUsuario1 = findViewById(R.id.txtUsuario1);
        txtUsuario2 = findViewById(R.id.txtUsuario2);
        imagemPerfil = findViewById(R.id.imagemPerfil);
        imagemPerfil2 = findViewById(R.id.imagemPerfil2);
        btn01 = findViewById(R.id.btn01);
        btn02 = findViewById(R.id.btn02);
        btn03 = findViewById(R.id.btn03);
        btn04 = findViewById(R.id.btn04);
        btnTimer = findViewById(R.id.btnTimer);
        linearAlternativas = findViewById(R.id.linearAlternativas);
        linearPergunta = findViewById(R.id.linearPergunta);
        pergunta = findViewById(R.id.txtPergunta);
        indicador = findViewById(R.id.txtIndicador);
        acertos = findViewById(R.id.txtScorePlayer1);
        acertos2 = findViewById(R.id.txtScorePlayer2);
        btnDica = findViewById(R.id.btnDica);
        btnProximo = findViewById(R.id.btnProximo);
        btnSair = findViewById(R.id.btnCompartilhar);


        //Animação da Modal de Tempo Esgotado
        modal_anima = AnimationUtils.loadAnimation(this, R.anim.modal_animation);


        //Btn de Desistir da partida
        btnSair.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        carregarPerguntas();

        for (int i = 0; i < 4; i++) {
            linearAlternativas.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    checkSelecionado((Button) v);
                    alternativaSelecionada = ((Button) v);
                }
            });

        }
        playAnim(linearPergunta, 0, list.get(position).getPergunta());
        Timer();

        btnDica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cronometro > 2) {
                    String dica = list.get(position).getQuestaoDica();
                    if (dica.isEmpty()) {

                        Alerter.create(MatchActivity.this)
                                .setTitle("Dica")
                                .setText("Ainda não cadastrei dica para essa pergunta. Tente na próxima pergunta")
                                .setDuration(5000)
                                .setBackgroundColorRes(R.color.colorBlue)
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Alerter.hide();
                                    }
                                })
                                .show();
                    } else {

                        Alerter.create(MatchActivity.this)
                                .setTitle("Dica")
                                .setText(list.get(position).getQuestaoDica())
                                .setDuration(5000)
                                .setBackgroundColorRes(R.color.colorBlue)
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Alerter.hide();
                                    }
                                })
                                .show();
                    }
                }
            }
        });
    }

    //carrega os dados do usuário logado (jogador1)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void carregaUser() {

        String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(userF.getEmail()));

        databaseReference.child("usuarios").child(idUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserModel usuario2Model = dataSnapshot.getValue(UserModel.class);

                assert usuario2Model != null;
                String[] linhas = usuario2Model.getNome().split(" ");
                txtUsuario1.setText(linhas[0]);
                txtUsuario2.setText(nomeJogador2);
                try {

                    if (usuario2Model.getImagem().isEmpty()) {
                        Glide
                                .with(getApplicationContext())
                                .load(R.drawable.profile)
                                .centerCrop()
                                .placeholder(R.drawable.profile)
                                .into(imagemPerfil);

                    } else {

                        Glide
                                .with(getApplicationContext())
                                .load(usuario2Model.getImagem())
                                .centerCrop()
                                .placeholder(R.drawable.profile)
                                .into(imagemPerfil);
                    }

                    if (imagem.isEmpty()) {
                        if (nomeJogador2.equals(getString(R.string.name_robot))) {
                            Glide
                                    .with(getApplicationContext())
                                    .load(R.drawable.reobote)
                                    .centerCrop()
                                    .placeholder(R.drawable.profile)
                                    .into(imagemPerfil2);
                        } else {
                            Glide
                                    .with(getApplicationContext())
                                    .load(R.drawable.profile)
                                    .centerCrop()
                                    .placeholder(R.drawable.profile)
                                    .into(imagemPerfil2);
                        }
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
                            .into(imagemPerfil);
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

    //A cada pergunta faz o select com os pontos do jogador convidado (jogador2)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperarPontosJogador2() {

        String emailUsuario = Objects.requireNonNull(emailJogador2);
        assert emailUsuario != null;
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida).child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    String pontos = Objects.requireNonNull(dataSnapshot.child("pontos").getValue()).toString();
                    if (!pontos.isEmpty()) {
                        acertos2.setText(pontos);
                        scoreJogador2 = Integer.parseInt(pontos);
                    }
                } catch (Exception e) {
                    acertos2.setText("" + scoreJogador2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Método responsável por verificar a conexão do usuário com a internet e com a partida
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void verificarConexao() {
        if (!nomeJogador2.equals(getString(R.string.name_robot))) {
            if (!desistir) {
                cont++;
                try {

                    if (cont == 1) {

                        DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);

                        usuarioRef.addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                try {

                                    String desconectado = Objects.requireNonNull(dataSnapshot.child("desconectado").getValue()).toString();
                                    String internet2 = Objects.requireNonNull(dataSnapshot.child("internet").getValue()).toString();

                                    if (desconectado.equals("true") || internet2.equals("false")) {
                                        conectado = false;
                                        jogando(false);
                                        tempo.cancel();
                                        modalDesistir();
                                    }

                                    if (internet2.equals("false")) {
                                        internet = false;
                                    }

                                } catch (Exception e) {

                                    Log.i("Erro", Objects.requireNonNull(e.getMessage()));
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.i("Erro", databaseError.getMessage());
                            }
                        });

                    }

                } catch (Exception e) {
                    Log.i("Erro", Objects.requireNonNull(e.getMessage()));
                }
            }
        }

    }

    //Alert Modal Avisando que o jogador(convidado) desistiu da partida
    @SuppressLint("SetTextI18n")
    public void modalDesistir() {

        tempo.cancel();
        if (!sair) {
            desistirDIalog = new Dialog(this);
            desistirDIalog.setContentView(R.layout.include_modal_warning);

            desistirTimer = desistirDIalog.findViewById(R.id.cardTimer);
            desistirTimer.startAnimation(modal_anima);
            desistirModal = desistirDIalog.findViewById(R.id.btnTimer);
            txtdesistir = desistirDIalog.findViewById(R.id.txtDesistir);

            txtdesistir.setText("O jogador(a) " + nomeJogador2 + " desistiu da partida.\nVocê venceu.\n+10 pontos");


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

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        atualizarPontosJogador1(10);
                    }
                    jogando(false);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
            });
        }

    }

    //Método responsável por animar e carregar as 10 perguntas da partida
    private void playAnim(final View view, final int valor, final String data) {
        view.animate().alpha(valor).scaleX(valor).scaleY(valor).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (valor == 0 && count < 4) {

                    String option = "";

                    if (count == 0) {
                        option = list.get(position).getQuestaoA();
                    } else if (count == 1) {
                        option = list.get(position).getQuestaoB();
                    } else if (count == 2) {
                        option = list.get(position).getQuestaoC();
                    } else if (count == 3) {
                        option = list.get(position).getQuestaoD();
                    }
                    playAnim(linearAlternativas.getChildAt(count), 0, option);
                    count++;

                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onAnimationEnd(Animator animation) {
                if (valor == 0) {
                    try {
                        //((TextView) view).setText(data);
                        ((Button) view).setText(data);
                        pergunta.setText(list.get(position).getPergunta());
                        indicador.setText(position + 1 + "/" + list.size());
                    } catch (ClassCastException ex) {
                        //((Button) view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view, 1, data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    //Método responsável por carregar as 10 perguntas da partida
    private void carregarPerguntas() {

        list.clear();

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());

        if (nomeJogador2.equals(getString(R.string.name_robot))) {
            List<QuestionModel> lista;
            lista = dataBaseAcess.listarPerguntas();
            list.addAll(lista);

        } else {

            firebaseRef.child("partidas").child(idPartida).child("perguntas").orderByChild("id")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //Isso faz com que o ArrayList seja limpa e não aparareça
                            list = new ArrayList<>();
                            for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                QuestionModel p = dados.getValue(QuestionModel.class);
                                list.add(p);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            //Sem isso não funciona. O Pq só Deus sabe. Tentar achar solução dps
            list.add(new QuestionModel(0, "Teste", "teste", "teste", "teste", "teste", "teste", ""));

        }

    }

    //Esse método exibe um alert de confirmação caso o usuário queira desistir da partida
    private void desistirP() {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Desistir da Partida")
                .setContentText("Atenção! Se você desistir da partida vai perder -3 pontos. Tem certeza que deseja desistir ?")
                .setConfirmText("Sim")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        tempo.cancel();
                        atualizarResultadoPartida(emailJogador2);
                        atualizarPontosJogador1(-3);
                        jogando(false);
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                        sair = true;
                        if (!nomeJogador2.equals(getString(R.string.name_robot))) {
                            DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);
                            usuarioRef.child("desconectado").setValue(true);
                        }
                    }
                }).setCancelText("Não")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        desistir = false;
                        jogando(true);
                        sweetAlertDialog.hide();
                    }
                })
                .show();
    }

    //Método responsável por carregar as perguntas da partida de 1 em 1
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void nextPergunta() {
        enabledOption(true);

        btn01.setTextColor(ColorStateList.valueOf(0xff000000));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btn01.setBackgroundTintList(ColorStateList.valueOf(0xffffffff));
            btn01.setBackground(getResources().getDrawable(R.drawable.btn_perguntas));
        }


        btn02.setTextColor(ColorStateList.valueOf(0xff000000));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btn02.setBackgroundTintList(ColorStateList.valueOf(0xffffffff));
            btn02.setBackground(getResources().getDrawable(R.drawable.btn_perguntas));
        }


        btn03.setTextColor(ColorStateList.valueOf(0xff000000));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btn03.setBackgroundTintList(ColorStateList.valueOf(0xffffffff));
            btn03.setBackground(getResources().getDrawable(R.drawable.btn_perguntas));
        }


        btn04.setTextColor(ColorStateList.valueOf(0xff000000));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btn04.setBackgroundTintList(ColorStateList.valueOf(0xffffffff));
            btn04.setBackground(getResources().getDrawable(R.drawable.btn_perguntas));
        }

        position++;

        if (position == 10) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                this.finalizarPartida();
            }
        } else {
            count = 0;
            playAnim(linearPergunta, 0, list.get(position).getPergunta());
            Timer();

        }
    }

    //Método responsável por colocar a borda azul na resposta selecionada pelo usuário
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkSelecionado(Button selectedOption) {

        clicado = true;

        btn01.setTextColor(ColorStateList.valueOf(0xff989898));
        btn01.setBackgroundTintList(ColorStateList.valueOf(0xffffffff));
        btn01.setBackground(this.getResources().getDrawable(R.drawable.btn_perguntas));

        btn02.setTextColor(ColorStateList.valueOf(0xff989898));
        btn02.setBackgroundTintList(ColorStateList.valueOf(0xffffffff));
        btn02.setBackground(this.getResources().getDrawable(R.drawable.btn_perguntas));

        btn03.setTextColor(ColorStateList.valueOf(0xff989898));
        btn03.setBackgroundTintList(ColorStateList.valueOf(0xffffffff));
        btn03.setBackground(this.getResources().getDrawable(R.drawable.btn_perguntas));

        btn04.setTextColor(ColorStateList.valueOf(0xff989898));
        btn04.setBackgroundTintList(ColorStateList.valueOf(0xffffffff));
        btn04.setBackground(this.getResources().getDrawable(R.drawable.btn_perguntas));

        selectedOption.setBackground(this.getResources().getDrawable(R.drawable.btn_perguntas));
        selectedOption.setBackgroundTintList(ColorStateList.valueOf(0xFF0066cc));
        selectedOption.setTextColor(ColorStateList.valueOf(0xffffffff));
    }

    //Método responsável por checar qual é aresposta certa da pergunta e atribuir acertos ou erros
    @SuppressLint({"NewApi", "SetTextI18n"})
    private void check(Button selectedOption) {

        enabledOption(false);
        clicado = true;

        selectedOption.setTextColor(ColorStateList.valueOf(0xffffffff));

        btnProximo.setAlpha(1);
        if (selectedOption.getText().toString().equals(list.get(position).getQuestaoCorreta())) {

            //correta
            score++;
            btn01.setTextColor(ColorStateList.valueOf(0xff989898));
            btn02.setTextColor(ColorStateList.valueOf(0xff989898));
            btn03.setTextColor(ColorStateList.valueOf(0xff989898));
            btn04.setTextColor(ColorStateList.valueOf(0xff989898));
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(0xFF247b37));
            selectedOption.setTextColor(ColorStateList.valueOf(0xffffffff));
            acertos.setText("" + score);
            Alerter.create(MatchActivity.this)
                    .setTitle("Obaa...")
                    .setText("Parabéns, você acertou!")
                    .setIcon(R.drawable.ic_success)
                    .setDuration(2000)
                    .setBackgroundColorRes(R.color.colorGreen1)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alerter.hide();
                        }
                    })
                    .show();

        } else {

            //incorreta
            btn01.setTextColor(ColorStateList.valueOf(0xff989898));
            btn02.setTextColor(ColorStateList.valueOf(0xff989898));
            btn03.setTextColor(ColorStateList.valueOf(0xff989898));
            btn04.setTextColor(ColorStateList.valueOf(0xff989898));
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(0xfffb0017));
            selectedOption.setTextColor(ColorStateList.valueOf(0xffffffff));
            Button corretaOption = linearAlternativas.findViewWithTag(list.get(position).getQuestaoCorreta());
            corretaOption.setBackgroundTintList(ColorStateList.valueOf(0xFF4CAF50));
            corretaOption.setTextColor(ColorStateList.valueOf(0xffffffff));

            Alerter.create(MatchActivity.this)
                    .setTitle("Oops...")
                    .setText("Oh não, você errou!")
                    .setIcon(R.drawable.ic_erro)
                    .setDuration(2000)
                    .setBackgroundColorRes(R.color.colorRed1)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alerter.hide();
                        }
                    })
                    .show();
        }


        if (nomeJogador2.equals(getString(R.string.name_robot))) {

            int numero = new Random().nextInt(5);
            if (numero <= 2) {
                scoreJogador2++;
                acertos2.setText("" + scoreJogador2);
            }
        } else {
            String jogador1 = Base64Custom.codificarBase64(Objects.requireNonNull(Objects.requireNonNull(autenticacao.getCurrentUser()).getEmail()));
            DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida).child(jogador1);
            usuarioRef.child("pontos").setValue(score);
            recuperarPontosJogador2();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                nextPergunta();
                jogando(true);

            }
        }, 2000);


    }

    //Stilos de cores na opção selecionada
    @SuppressLint("NewApi")
    private void enabledOption(boolean enabled) {
        clicado = true;
        for (int i = 0; i < 4; i++) {
            linearAlternativas.getChildAt(i).setEnabled(enabled);
            if (enabled) {
                linearAlternativas.getChildAt(i).setBackground(this.getResources().getDrawable(R.drawable.btn_perguntas));
            }else{
                linearAlternativas.getChildAt(i).setBackground(this.getResources().getDrawable(R.drawable.btn_perguntas));
            }
        }
    }

    //Método responsável por chamar a classe Timer de 15 segundos para cada pergunta (cont=15 cont>=0 cont--)
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Timer() {

        clicado = false;
        btnTimer.setText("15");
        cronometro = 15;
        btnTimer.setBackground(getResources().getDrawable(btn_quiz_timer));

        if (tempo != null) {
            tempo.cancel();
            tempo = null;
        }
        tempo = new Timer();
        Task task = new Task();
        tempo.schedule(task, 1000, 1000);
    }

    //Classe Timer
    class Task extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {

                    if (cronometro > 0) {
                        cronometro = cronometro - 1;
                        if (cronometro < 10) {
                            btnTimer.setText("0" + cronometro);
                            btnTimer.setBackground(getResources().getDrawable(btn_quiz_timer_red));

                        } else {
                            btnTimer.setText("" + cronometro);

                        }
                    } else {
                        if (clicado) {
                            check(alternativaSelecionada);
                            tempo.cancel();
                        } else {

                            try {
                                boolean foregroud = new ChecarSegundoPlano().execute(getApplicationContext()).get();
                                if (foregroud) {
                                    expiredTimer();
                                }

                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                }


            });
        }


    }

    //Modal de Aviso (Tempo Esgotado) *Provisória*
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("InflateParams")
    public void expiredTimer() {

        try {
            tempo.cancel();
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long milliseconds = 1000;
            assert vibrator != null;
            vibrator.vibrate(milliseconds);

            enabledOption(false);

            Alerter.create(this)
                    .setTitle("Oops...")
                    .setText("TEMPO ESGOTADO!")
                    .setIcon(R.drawable.ic_tempo)
                    .setDuration(2000)
                    .setBackgroundColorRes(R.color.colorWarning)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alerter.hide();
                        }
                    })
                    .show();

            if (!txtUsuario2.getText().equals(getString(R.string.name_robot))) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enabledOption(false);
                        recuperarPontosJogador2();
                        nextPergunta();
                    }
                }, 2000);

            } else {


                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        enabledOption(false);
                        scoreJogador2++;
                        acertos2.setText("" + scoreJogador2);
                        nextPergunta();
                    }
                }, 2000);
            }

        } catch (Exception e) {
            Log.i("BTN-TIMER", Objects.requireNonNull(e.getMessage()));
        }

    }

    /*==========================================================================
              Esses metodos são executados apenas no final da partida
    ==========================================================================*/

    //Recuperar os pontos do usuário logado (jogador1) no banco de dados
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperarPontosJogador1() {

        String emailUsuario = Objects.requireNonNull(autenticacao.getCurrentUser()).getEmail();
        assert emailUsuario != null;
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                String pontosGeral = Objects.requireNonNull(dataSnapshot.child("pontosG").getValue()).toString();
                String vitoriasText = Objects.requireNonNull(dataSnapshot.child("vitorias").getValue()).toString();
                String derrotasText = Objects.requireNonNull(dataSnapshot.child("derrotas").getValue()).toString();
                String empatesText = Objects.requireNonNull(dataSnapshot.child("empates").getValue()).toString();
                String partidasText = Objects.requireNonNull(dataSnapshot.child("partidas").getValue()).toString();


                pontosG = Integer.parseInt(pontosGeral);
                vitorias = Integer.parseInt(vitoriasText);
                derrotas = Integer.parseInt(derrotasText);
                empates = Integer.parseInt(empatesText);
                partidas = Integer.parseInt(partidasText);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Método para somar com os pontos da partida do jogador e atribuir no banco de dados
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void atualizarPontosJogador1(int pontos) {

        String emailUsuario = Objects.requireNonNull(autenticacao.getCurrentUser()).getEmail();
        assert emailUsuario != null;
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        int pontosGeral = pontosG + pontos;
        int partidasTotal = partidas + 1;

        usuarioRef.child("partidas").setValue(partidasTotal);
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

        usuarioRef.child("jogando").setValue(false);

    }

    //Método responsável por verificar se o jogador está jogando ou não
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

    //Método Responsável por finalizar a partida e verificar qual foi o resultado final
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    public void finalizarPartida() {
        tempo.cancel();
        String resultado;
        if (score == scoreJogador2) {
            //chama Modal de Empate
            atualizarPontosJogador1(5);
            atualizarResultadoPartida("empate");
            resultado = "empate";
            openModal(resultado);

        } else if (score < scoreJogador2) {
            //chama Modal de derrota
            atualizarPontosJogador1(-3);
            atualizarResultadoPartida(emailJogador2);
            resultado = "derrota";
            openModal(resultado);


        } else {

            atualizarPontosJogador1(10);
            atualizarResultadoPartida(userF.getEmail());
            resultado = "vitoria";
            openModal(resultado);


        }
        jogando(false);


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    private void openModal(final String resultado){
        final Dialog welcomeModal = new Dialog(this);
        welcomeModal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        welcomeModal.setCancelable(false);
        welcomeModal.setContentView(R.layout.include_modal);

        CardView cardModal = welcomeModal.findViewById(R.id.cardModal);
        cardModal.startAnimation(modal_anima);
        ImageView imageIcon = welcomeModal.findViewById(R.id.imageIcon);
        Button btnAction = welcomeModal.findViewById(R.id.btnAction);
        TextView txt_title = welcomeModal.findViewById(R.id.txt_title);
        TextView txtDescription = welcomeModal.findViewById(R.id.txtDescription);

        String [] name = Objects.requireNonNull(userF.getDisplayName()).split(" ");

        if(resultado.equals("vitoria")){
            imageIcon.setImageResource(R.drawable.ic_emogi_happy);
            txt_title.setText("Parabéns, "+name[0]+"!");
            txtDescription.setText("Você venceu e ganhou\n +10 pontos");
            btnAction.setText("GANHAR +10");
        }

        else if(resultado.equals("derrota")){
            imageIcon.setImageResource(R.drawable.ic_emogi_sad);
            txt_title.setText("Oh não, "+name[0]+"!");
            txtDescription.setText("Você perdeu a partida\n -3 pontos");
            btnAction.setText("GANHAR +3");
        }else {
            imageIcon.setImageResource(R.drawable.ic_emogi_cry);
            txt_title.setText("Faltou pouco, "+name[0]+"!");
            txtDescription.setText("Você empatou e ganhou\n +5 pontos");
            btnAction.setText("GANHAR +5");
        }


        cardModal.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                welcomeModal.dismiss();
                welcomeModal.hide();

                Intent i = new Intent(getApplicationContext(), MatchFinishDetailsActivity.class);
                i.putExtra("resultado", resultado);
                i.putExtra("pontos", score);
                i.putExtra("jogador2", nomeJogador2);
                i.putExtra("imagem", imagem);
                i.putExtra("pontos2", scoreJogador2);
                startActivity( i );
                finish();

            }
        });

        btnAction.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                welcomeModal.dismiss();
                welcomeModal.hide();

                Intent i = new Intent(getApplicationContext(), MatchFinishDetailsActivity.class);
                i.putExtra("resultado", resultado);
                i.putExtra("pontos", score);
                i.putExtra("jogador2", nomeJogador2);
                i.putExtra("imagem", imagem);
                i.putExtra("pontos2", scoreJogador2);
                startActivity( i );
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(welcomeModal.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        welcomeModal.setCancelable(false);

        welcomeModal.show();


    }

    //Método Responsável por inserir os dados da partida no banco
    private void atualizarResultadoPartida(String email) {

        if (!nomeJogador2.equals(getString(R.string.name_robot))) {
            DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);
            usuarioRef.child("resultado").setValue(email);
            usuarioRef.child("perguntas").removeValue();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {

        desistirP();
        if (desistir) {
            jogando(false);
            super.onBackPressed();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStart() {
        super.onStart();
        verificarConexao();
        carregaUser();
        recuperarPontosJogador1();
        jogando(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        jogando(false);
    }
}
