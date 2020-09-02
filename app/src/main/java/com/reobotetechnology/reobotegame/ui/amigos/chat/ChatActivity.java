package com.reobotetechnology.reobotegame.ui.amigos.chat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.MensagensChatAdapters;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.model.MensagensIAModel;
import com.reobotetechnology.reobotegame.model.MensagensModel;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.visualizar_imagens.VisualizarImagemActivity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String nome, texto, imagem;
    String data_completa, hora_atual;
    int contador = 1, id_pergunta = 0;
    Date data;

    TextView nomeUsuarioConversa;
    CircleImageView imagemUsuarioConversa;

    RecyclerView recyclerChatMensagens;
    MensagensChatAdapters adapter;
    ArrayList<MensagensModel> lista = new ArrayList<>();

    EditText editMensagem;

    ImageView imgCameraChat;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#0e4bef"));
        }*/

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
        imgCameraChat = findViewById(R.id.imgCameraChat);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            imagem = extras.getString("imagem");
            nome = extras.getString("nome");
            nomeUsuarioConversa.setText(nome);

        }


        imagemUsuarioConversa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), VisualizarImagemActivity.class);
                i.putExtra("nome", nome);
                i.putExtra("imagem", imagem);
                startActivity(i);

            }
        });

        editMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tamanho = lista.size();
                recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);
            }
        });

        imgCameraChat.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                showBottomSheetPickPhoto();
            }
        });

        recyclerChatMensagens = findViewById(R.id.recyclerChatMensagens);
        adapter = new MensagensChatAdapters(lista, getApplicationContext());

        //RecyclerMensagens
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerChatMensagens.setLayoutManager(layoutManager);
        recyclerChatMensagens.setHasFixedSize(true);
        recyclerChatMensagens.setAdapter(adapter);


        if (nome.equals("Reobote Technology")) {
            UsuarioModel amigo = new UsuarioModel("1", "Fabrício", "", "",
                    imagem, "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0,0,true, false);

            try {

                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.reobote)
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(imagemUsuarioConversa);
            } catch (Exception e) {
                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.reobote)
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(imagemUsuarioConversa);
            }
            MensagensModel m = new MensagensModel(1, amigo, amigo, data, hora_atual,
                    "Olá, Tudo bem? Eu sou a inteligência artifical do Reobote Game. É um prazer te conhecer.\n\nVocê é uma Bênção", 0, "", 1, false);
            lista.add(m);
        } else {
            UsuarioModel amigo = new UsuarioModel("1", "Fabrício", "", "",
                    imagem, "", 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0,0,0,true, false);
            try {

                Glide
                        .with(getApplicationContext())
                        .load(imagem)
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(imagemUsuarioConversa);
            } catch (Exception e) {
                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.user)
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(imagemUsuarioConversa);
            }
            MensagensModel m = new MensagensModel(1, amigo, amigo, data, hora_atual,
                    "Olá, Tudo bem? Meu nome é " + nome + ". Estou enviando está mensagem para você testar o layout", 0, "", 1, false);
            lista.add(m);
        }

    }

    private void showBottomSheetPickPhoto() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_photo, null);

        ((View) view.findViewById(R.id.ln_gallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Galeria", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });
        ((View) view.findViewById(R.id.ln_camera)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Camera", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialog = null;
            }
        });

        bottomSheetDialog.show();
    }

    public void enviarMensagem(View view) {

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

        if (!texto.isEmpty()) {

            UsuarioModel amigo = new UsuarioModel("1", "Fabrício", "", "",
                    imagem, "", 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0,0,0,true, false);

            MensagensModel m = new MensagensModel(2, amigo, amigo, data, hora_atual,
                    texto, 0, "", 0, false);

            editMensagem.setText("");

            lista.add(m);
            adapter.notifyDataSetChanged();
            int tamanho = lista.size();
            recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);

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

                    UsuarioModel amigo = new UsuarioModel("1", "Fabrício", "", "",
                            imagem, "", 0, 0, 0, 0, 0, 0, 0, 0, 0,
                            0,0,0,true, false);
                    if (nome.equals("Reobote Technology")) {

                        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());

                        try {

                            List<MensagensIAModel> lista2 = new ArrayList<>(dataBaseAcess.listarMensagens(id_pergunta));
                            String msg = lista2.get(0).getMensagem();
                            id_pergunta = lista2.get(0).getId();

                            if (!msg.isEmpty()) {
                                MensagensModel m = new MensagensModel(1, amigo, amigo, data, hora_atual, msg, 0, "", 1, false);
                                lista.add(m);

                            } else {
                                MensagensModel m = new MensagensModel(2, amigo, amigo, data, hora_atual, "AH NÃO. MEUS CIRCUITOS INTERNOS FALHARAM. TENTE CONVERSAR COMIGO MAIS TARDE", 0, "", 1, false);
                                lista.add(m);
                            }

                        } catch (Exception e) {
                            MensagensModel m = new MensagensModel(2, amigo, amigo, data, hora_atual, "AH NÃO. MEUS CIRCUITOS INTERNOS FALHARAM. TENTE CONVERSAR COMIGO MAIS TARDE", 0, "", 1, false);
                            lista.add(m);
                        }


                    } else if (contador == 1) {
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
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }


}
