package com.reobotetechnology.reobotegame.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.MensagensChatAdapters;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.model.MensagensIAModel;
import com.reobotetechnology.reobotegame.model.MensagensModel;
import com.reobotetechnology.reobotegame.model.UsuarioModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String nome, texto;
    String data_completa, hora_atual;
    int imagem, contador = 1, id_pergunta = 0;
    Date data;

    TextView nomeUsuarioConversa;
    CircleImageView imagemUsuarioConversa;

    RecyclerView recyclerChatMensagens;
    MensagensChatAdapters adapter;
    ArrayList<MensagensModel> lista = new ArrayList<>();

    EditText editMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#0e4bef"));
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        // OU
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm");

        Calendar cal = Calendar.getInstance();
        data = new Date();
        cal.setTime(data);
        Date data_atual = cal.getTime();

       data_completa = dateFormat.format(data_atual);

       hora_atual = dateFormat_hora.format(data_atual);


        nomeUsuarioConversa = findViewById(R.id.nomeUsuarioConversa);
        imagemUsuarioConversa = findViewById(R.id.imagemUsuarioConversa);
        editMensagem = findViewById(R.id.editMensagem);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imagem = extras.getInt("imagem");
            nome = extras.getString("nome");

            nomeUsuarioConversa.setText(nome);
            imagemUsuarioConversa.setImageResource(imagem);
        }



        imagemUsuarioConversa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), ImagemActivity.class);
                i.putExtra("nome", nome);
                i.putExtra("imagem", imagem);
                startActivity( i );

            }
        });

        editMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tamanho = lista.size();
                recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);
            }
        });


        recyclerChatMensagens = findViewById(R.id.recyclerChatMensagens);
        adapter = new MensagensChatAdapters(lista, getApplicationContext());

        //RecyclerMensagens
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerChatMensagens.setLayoutManager(layoutManager);
        recyclerChatMensagens.setHasFixedSize(true);
        recyclerChatMensagens.setAdapter(adapter);

        int img = R.drawable.eu;
        int img2  = R.drawable.danilo;
        int img3 = R.drawable.isabela;
        int img4 = R.drawable.sabrina;
        int img5 = R.drawable.maria;
        int img6 = R.drawable.julia;



        UsuarioModel amigo = new UsuarioModel(1,"Fabrício", img, 1800, 1);

        if(nome.equals("Reobote Technology")){
            MensagensModel m = new MensagensModel(1, amigo, amigo, data, hora_atual,
                    "Olá, Tudo bem? Eu sou a inteligência artifical do Reobote Game. É um prazer te conhecer.\n\nVocê é uma Bênção", 0, "", 1,false);
            lista.add(m);
        }else {
            MensagensModel m = new MensagensModel(1, amigo, amigo, data, hora_atual,
                    "Olá, Tudo bem? Meu nome é "+nome+". Estou enviando está mensagem para você testar o layout", 0, "", 1, false);
            lista.add(m);
        }

    }

    public void enviarMensagem(View view){

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        // OU
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm");

        Calendar cal = Calendar.getInstance();
        data = new Date();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        data_completa = dateFormat.format(data_atual);

        hora_atual = dateFormat_hora.format(data_atual);


        texto = editMensagem.getText().toString();

        if(!texto.isEmpty()){

            int img = R.drawable.eu;
            UsuarioModel amigo = new UsuarioModel(1,"Fabrício", img, 1800, 1);

            MensagensModel m = new MensagensModel(2, amigo, amigo, data, hora_atual,
                    texto, 0, "", 0,false);

            editMensagem.setText("");

            lista.add(m);
            adapter.notifyDataSetChanged();
            int tamanho = lista.size();
            recyclerChatMensagens.smoothScrollToPosition(tamanho-1);

            //esconder teclado
            /*InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editMensagem.getWindowToken(), 0);*/

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
                        // OU
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm");

                        Calendar cal = Calendar.getInstance();
                        data = new Date();
                        cal.setTime(data);
                        Date data_atual = cal.getTime();

                        data_completa = dateFormat.format(data_atual);

                        hora_atual = dateFormat_hora.format(data_atual);

                        int img = R.drawable.eu;
                        UsuarioModel amigo = new UsuarioModel(1, "Fabrício", img, 1800, 1);

                    if(nome.equals("Reobote Technology")) {


                        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());

                        try {

                            List<MensagensIAModel> lista2 = new ArrayList<>(dataBaseAcess.listarMensagens(id_pergunta));
                            String msg = lista2.get(0).getMensagem();
                            id_pergunta = lista2.get(0).getId();

                            if(!msg.isEmpty()) {
                                MensagensModel m = new MensagensModel(1, amigo, amigo, data, hora_atual, msg, 0, "", 1, false);
                                lista.add(m);

                            }else{
                                MensagensModel m = new MensagensModel(2, amigo, amigo, data, hora_atual, "AH NÃO. MEUS CIRCUITOS INTERNOS FALHARAM. TENTE CONVERSAR COMIGO MAIS TARDE", 0, "", 1, false);
                                lista.add(m);
                            }

                        }catch (Exception e){
                            MensagensModel m = new MensagensModel(2, amigo, amigo, data, hora_atual, "AH NÃO. MEUS CIRCUITOS INTERNOS FALHARAM. TENTE CONVERSAR COMIGO MAIS TARDE", 0, "", 1, false);
                            lista.add(m);
                        }


                    }else if(contador == 1){
                        MensagensModel m = new MensagensModel(2, amigo, amigo, data, hora_atual,
                                "Agora que você já testou vou te deixar no vácuo a partir da próxima mensagem kkkkk\n\nSabe jogar vôlei? Seguraa esse bloqueio ai kkk", 0, "", 1, false);
                        lista.add(m);
                    }

                        adapter.notifyDataSetChanged();
                        int tamanho = lista.size();
                        recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);
                        //Quantas Mensagens foram enviadas por exemplo
                        contador = contador + 1;


                }
            }, 2000);

        }

    }

    private void alert(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }


}
