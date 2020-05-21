package com.reobotetechnology.reobotegame.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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

import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.model.PerguntasModel;
import com.reobotetechnology.reobotegame.utils.ChecarSegundoPlano;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static com.reobotetechnology.reobotegame.R.drawable.btn_quiz_timer_red;
import static com.reobotetechnology.reobotegame.R.drawable.btn_quiz_timer;


public class PartidaActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;

    TextView pergunta, indicador, acertos;
    LinearLayout linearAlternativas;
    Button btnProximo, btnSair, btn01,btn02,btn03,btn04, btnTimer;

    //Vem da Modal Timer
    Dialog timerDIalog;
    Button btnModal;
    CardView cardTimer;
    Animation modal_anima;

    //Vem da modal_pontos
    Dialog pontosDIalog;
    Button btnFinalizar;
    CardView cardPontos;

    TextView txtTextoPontos, txtPontos;
    private int count = 0;
    List<PerguntasModel> list;
    private int position = 0;
    private int score = 0;
    private int cronometro = 30;
    private boolean clicado;


    Timer tempo;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("SetTextI18n")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        btn01 = findViewById(R.id.btn01);
        btn02 = findViewById(R.id.btn02);
        btn03 = findViewById(R.id.btn03);
        btn04 = findViewById(R.id.btn04);

        modal_anima = AnimationUtils.loadAnimation(this, R.anim.modal_animation);

        btnTimer = findViewById(R.id.btnTimer);
        linearAlternativas = findViewById(R.id.linearAlternativas);
        pergunta = findViewById(R.id.txtPergunta);
        indicador = findViewById(R.id.txtIndicador);
        //acertos = findViewById(R.id.txtAcertos);
        //timer = findViewById(R.id.txtTimer);
        btnProximo = findViewById(R.id.btnProximo);
        btnSair = findViewById(R.id.btnCompartilhar);

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        list = new ArrayList<>();
        List<PerguntasModel> lista2 = new ArrayList<>();

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
        lista2 = dataBaseAcess.listarPerguntas();

        if(lista2.size() == 0){
            Toast.makeText(getApplicationContext(), "Erro ao carregar perguntas do banco de dados.\n\nCarregando da 10 Perguntas da Lista Offline...", Toast.LENGTH_SHORT).show();
            list.add(new PerguntasModel(13, "1 - Quantas pragas foram enviadas ao Egito?", "7 Pragas","10 Pragas", "17 Pragas", "20 Pragas", "10 Pragas", ""));
            list.add(new PerguntasModel(14, "2 - Qual o nome de uma das esposas de Jacó?", "Raquel","Joana", "Eva", "Ada", "Raquel", ""));
            list.add(new PerguntasModel(15, "3 - Qual foi o primeiro filho de Adão e Eva?", "Caim","Abel", "Sete", "Enoque", "Caim", ""));
            list.add(new PerguntasModel(16, "4 - Como são chamados os 5 primeiros livros da Bíblia?", "Septuaginta","Profecias", "Pentateuco", "Cânticos", "Pentateuco", ""));
            list.add(new PerguntasModel(17, "5 - Qual o maior livro da Bíblia?", "Mateus","Salmos", "Isaias", "Gênesis", "Salmos", ""));
            list.add(new PerguntasModel(18, "6 - Quem foi o profeta que voou num redemoinho?", "Jonas","Eliseu", "Elias", "Moisés", "Elias", ""));
            list.add(new PerguntasModel(19, "7 - Qual desses homens morreu decapitado?", "Davi","Pedro", "Tiago", "João Batista", "João Batista", ""));
            list.add(new PerguntasModel(20, "8 - O véu do santuário rasgou-se de alto a baixo em que ocasião?", "Na santa ceia","Festa de pentecostes", "Na ressurreição", "Na morte de Cristo", "Na morte de Cristo", ""));
            list.add(new PerguntasModel(21, "9 - Em qual livro da Bíblia é descrita a Nova Jerusalém?", "Apocalipse","Salmos", "1 Reis", "Hebreus", "Apocalipse", "Este livro também é conhecido como: 'O LIVRO DA REVELAÇÃO' "));
            list.add(new PerguntasModel(22, "10 - Qual é o versículo mais longo da Bíblia?", "1 Samuel 16:7", "Rute 1:1", "Ester 8:9", "João 3:16", "Ester 8:9", "Jovem que casou com o Rei Assuero"));
        }else {
            list.addAll(lista2);
        }

        for (int i =0; i<4; i++) {
            linearAlternativas.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check((Button)v);
                }
            });

        }
        playAnim(pergunta, 0, list.get(position).getPergunta());
        Timer();

        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnProximo.setEnabled(false);
                btnProximo.setAlpha(0.7f);
                enabledOption(true);
                btn01.setTextColor(ColorStateList.valueOf(0xff000000));
                btn02.setTextColor(ColorStateList.valueOf(0xff000000));
                btn03.setTextColor(ColorStateList.valueOf(0xff000000));
                btn04.setTextColor(ColorStateList.valueOf(0xff000000));
                position++;
                if(position == list.size()){
                    try {
                        boolean foregroud = new ChecarSegundoPlano().execute(getApplicationContext()).get();
                        if(foregroud){
                            finalizarGame();
                        }

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }else {
                    count = 0;
                    playAnim(pergunta, 0, list.get(position).getPergunta());
                    Timer();


                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Timer(){

        clicado = false;
        btnTimer.setText("30");
        cronometro = 30;
        btnTimer.setBackground(getResources().getDrawable(btn_quiz_timer));

        if(tempo!=null){
            tempo.cancel();
            tempo = null;
        }
        tempo = new Timer();
        Task task = new Task();
        tempo.schedule(task, 1000, 1000);


    }


    @SuppressLint("InflateParams")
    public void modal() {

        tempo.cancel();

        timerDIalog = new Dialog(this);
        timerDIalog.setContentView(R.layout.modal_timer);

        cardTimer = (CardView) timerDIalog.findViewById(R.id.cardTimer);
        cardTimer.startAnimation(modal_anima);
        btnModal = (Button) timerDIalog.findViewById(R.id.btnTimer);
        btnModal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                timerDIalog.dismiss();
                nextPergunta();
            }
        });

        timerDIalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timerDIalog.setCancelable(false);

        timerDIalog.show();

    }

    public void finalizarGame(){

        tempo.cancel();
        pontosDIalog = new Dialog(this);
        pontosDIalog.setContentView(R.layout.modal_pontos);
        cardPontos = (CardView) pontosDIalog.findViewById(R.id.cardPontos);
        cardPontos.startAnimation(modal_anima);

        btnFinalizar = (Button) pontosDIalog.findViewById(R.id.btnFinalizar);
        txtTextoPontos = (TextView) pontosDIalog.findViewById(R.id.txtTextoPontos);
        txtTextoPontos.setText("Parabéns, você acertou "+score+ " perguntas");
        txtPontos = (TextView) pontosDIalog.findViewById(R.id.txtPontos);
        txtPontos.setText("+ "+score+" pontos");

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pontosDIalog.dismiss();
                //startActivity(new Intent(getApplicationContext(), PontuacaoActivity.class));
                finish();
            }
        });

        pontosDIalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pontosDIalog.setCancelable(false);
        pontosDIalog.show();

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void nextPergunta() {

        btnProximo.setEnabled(false);
        btnProximo.setAlpha(0.7f);
        enabledOption(true);
        btn01.setTextColor(ColorStateList.valueOf(0xff000000));
        btn02.setTextColor(ColorStateList.valueOf(0xff000000));
        btn03.setTextColor(ColorStateList.valueOf(0xff000000));
        btn04.setTextColor(ColorStateList.valueOf(0xff000000));
        position++;
        if(position == list.size()){
            this.finalizarGame();
        }else {
            count = 0;
            playAnim(pergunta, 0, list.get(position).getPergunta());
            Timer();


        }
    }

    class Task extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {


                    if (!clicado){
                        if (cronometro > 0) {
                            cronometro = cronometro - 1;
                            if (cronometro < 10) {
                                btnTimer.setText("0"+cronometro);
                                btnTimer.setBackground(getResources().getDrawable(btn_quiz_timer_red));

                            } else {
                                btnTimer.setText(""+cronometro);

                            }
                        }else{

                            try {
                                boolean foregroud = new ChecarSegundoPlano().execute(getApplicationContext()).get();
                                if(foregroud){
                                    modal();
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

    private void playAnim(final View view, final int valor, final String data){
        view.animate().alpha(valor).scaleX(valor).scaleY(valor).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(valor == 0 && count < 4){

                    String option = "";

                    if(count == 0){
                        option = list.get(position).getQuestaoA();
                    }
                    else if(count == 1){
                        option = list.get(position).getQuestaoB();
                    }
                    else if(count == 2){
                        option = list.get(position).getQuestaoC();
                    }
                    else if(count == 3){
                        option = list.get(position).getQuestaoD();
                    }
                    playAnim(linearAlternativas.getChildAt(count), 0, option);
                    count++;
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onAnimationEnd(Animator animation) {
                if (valor == 0){
                    try {
                        ((TextView) view).setText(data);
                        indicador.setText(position+1+"/"+list.size());
                    }catch (ClassCastException ex){
                        ((Button) view).setText(data);
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

    @SuppressLint({"NewApi", "SetTextI18n"})
    private void check(Button selectedOption){

        enabledOption(false);
        btnProximo.setEnabled(true);
        clicado = true;
        if(position+1 == list.size()){
            btnProximo.setText("FINALIZAR");
        }

        selectedOption.setTextColor(ColorStateList.valueOf(0xff989898));

        btnProximo.setAlpha(1);
        if(selectedOption.getText().toString().equals(list.get(position).getQuestaoCorreta())) {
            //correta
            score ++;

            btn01.setTextColor(ColorStateList.valueOf(0xff989898));
            btn02.setTextColor(ColorStateList.valueOf(0xff989898));
            btn03.setTextColor(ColorStateList.valueOf(0xff989898));
            btn04.setTextColor(ColorStateList.valueOf(0xff989898));
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(0xFF4CAF50));
            selectedOption.setTextColor(ColorStateList.valueOf(0xff000000));
            //acertos.setText(""+score);



        }else{
            //incorreta

            btn01.setTextColor(ColorStateList.valueOf(0xff989898));
            btn02.setTextColor(ColorStateList.valueOf(0xff989898));
            btn03.setTextColor(ColorStateList.valueOf(0xff989898));
            btn04.setTextColor(ColorStateList.valueOf(0xff989898));
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(0xfffb0017));

            Button corretaOption = (Button) linearAlternativas.findViewWithTag(list.get(position).getQuestaoCorreta());


            corretaOption.setBackgroundTintList(ColorStateList.valueOf(0xFF4CAF50));
            corretaOption.setTextColor(ColorStateList.valueOf(0xff000000));

        }
    }
    @SuppressLint("NewApi")
    private void enabledOption(boolean enabled){
        clicado = true;
        for (int i =0; i<4; i++){
            linearAlternativas.getChildAt(i).setEnabled(enabled);
            if(enabled){
                linearAlternativas.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(0xff989898));
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            tempo.cancel();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Atenção a partida será cancelada e você perderá -3 pontos. Pressione novamente se deseja sair", Toast.LENGTH_LONG);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();

    }
}
