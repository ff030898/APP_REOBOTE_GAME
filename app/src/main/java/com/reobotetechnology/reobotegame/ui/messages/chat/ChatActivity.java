package com.reobotetechnology.reobotegame.ui.messages.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.MessagesChatAdapters;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.model.MensagensModel;
import com.reobotetechnology.reobotegame.model.UserModel;
import com.reobotetechnology.reobotegame.ui.view_images_screen.VisualizarImagemActivity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String nome, texto, imagem;
    private String data_completa, hora_atual;
    private int contador = 1;
    private Date data;

    private TextView txtUsername, txtOnlineUser;
    private CircleImageView profileUser;

    private RecyclerView recyclerChatMensagens;
    private MessagesChatAdapters adapter;
    private ArrayList<MensagensModel> lista = new ArrayList<>();

    private EditText editMensagem;

    //Configurações do banco de dados
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();

    private ProgressBar progressBar;



    private ConstraintLayout constraintMain;
    private ImageButton btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Configurações iniciais
        progressBar = findViewById(R.id.progressBar);
        constraintMain = findViewById(R.id.constraintMain);

        //TOOLBAR
        btn_back = findViewById(R.id.btn_back);

        constraintMain.setVisibility(View.GONE);

        progressBar.setVisibility(View.VISIBLE);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtUsername = findViewById(R.id.txtUsername);
        txtOnlineUser = findViewById(R.id.txtOnlineUser);
        profileUser = findViewById(R.id.profileUser);
        editMensagem = findViewById(R.id.editMensagem);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            imagem = extras.getString("imagem");
            nome = extras.getString("nome");
            getUserMessage(nome);
        }


        profileUser.setOnClickListener(new View.OnClickListener() {
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


        recyclerChatMensagens = findViewById(R.id.recyclerChatMensagens);
        adapter = new MessagesChatAdapters(lista, getApplicationContext());

        //RecyclerMensagens
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerChatMensagens.setLayoutManager(layoutManager);
        recyclerChatMensagens.setHasFixedSize(true);
        recyclerChatMensagens.setAdapter(adapter);


    }

    private void getUserMessage(String nome){

        txtUsername.setText(nome);


        if (nome.equals("Reobote IA")) {

            UserModel amigo = new UserModel("1", "", "", "", imagem,
                    "", "", "", 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, false, false, false, false, false,
                    false, false, false, false);

            try {

                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.reobote)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(profileUser);
            } catch (Exception e) {
                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.reobote)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(profileUser);
            }
            MensagensModel m = new MensagensModel(1, amigo, amigo, data, hora_atual,
                    "Olá, "+user.getDisplayName()+". Tudo bem? Eu sou a inteligência artifical do Reobote Game. É um prazer te conhecer.\n\nVocê é uma Bênção", 0, "", 1, false);
            lista.add(m);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                constraintMain.setVisibility(View.VISIBLE);

            }
        }, 2000);
    }

    public void sendMessage(View view) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                txtOnlineUser.setText(getString(R.string.digited));
                //txtOnlineUser.setTextColor(ColorStateList.valueOf(0xff505050));

            }
        }, 4000);


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

            UserModel amigo = new UserModel("1", "", "", "", imagem,
                    "", "", "", 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, false, false, false, false, false,
                    false, false, false, false);

            MensagensModel m = new MensagensModel(2, amigo, amigo, data, hora_atual,
                    texto, 0, "", 0, false);

            editMensagem.setText("");

            lista.add(m);
            adapter.notifyDataSetChanged();
            int tamanho = lista.size();
            recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);


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

                    UserModel amigo = new UserModel("1", "", "", "", imagem,
                            "", "", "", 0, 0, 0, 0,
                            0, 0, 0, 0, 0, 0, false, false, false, false, false,
                            false, false, false, false);

                    if (nome.equals("Reobote IA")) {

                        try {

                            if(contador == 1){
                                lista.add(new MensagensModel(1, amigo, amigo, data, hora_atual, "Você é uma pessoa incrivel. Sabia? Deus vai honrar a tua fé.\uD83D\uDE0D",
                                        0, "", 1, false));
                            }else if(contador == 2){
                                lista.add(new MensagensModel(2, amigo, amigo, data, hora_atual, "Vai dormir. Quem te prometeu garante <3",
                                        0, "", 1, false));

                            }else if(contador == 3){
                                lista.add(new MensagensModel(3, amigo, amigo, data, hora_atual, "Palavra do céu para você.\n\nResponderam eles: Crê no Senhor Jesus e serás salvo, tu e tua casa. \n" +
                                        "\n" +
                                        "Atos 16:31", 0, "", 1, false));

                            }else if(contador == 4){
                                lista.add(new MensagensModel(4, amigo, amigo, data, hora_atual, "Se acalma. Se você ainda está solteiro(a) vai casar antes da volta do Filho do Homem. \\uD83D\\uDE0D\\n\\n #Creia no Milagre\"",
                                        0, "", 1, false));
                            }else if(contador == 5){
                                lista.add(new MensagensModel(5, amigo, amigo, data, hora_atual,"Você ainda vai cantar, pregar e evangelizar muito pelo mundo inteiro.\n\n#O tempo de Deus é perfeito",
                                        0, "", 1, false));
                            }else if(contador == 6){
                                lista.add(new MensagensModel(6, amigo, amigo, data, hora_atual,"Nós somos os Profetas da última hora\n\nMuitas pessoas vão se converter atráves da sua oração.",
                                        0, "", 1, false));
                            }else if(contador == 7){
                                lista.add(new MensagensModel(7, amigo, amigo, data, hora_atual,"A Bíblia diz em Atos 28. Que Paulo foi picado por uma víbora.\n\nPorém ele não morreu. Mas, a Bíblia não diz se doeu ou não.\n\nAs lutas dentro da sua casa, igreja ou do mundo podem até doer e te entristecer. Mas, assim como Paulo você também não vai morrer por causa das víboras",
                                        0, "", 1, false));
                            }else if(contador == 8){
                                lista.add(new MensagensModel(8, amigo, amigo, data, hora_atual,"Palavra do céu para você.\n\nSe ouvires atentamente a voz do Senhor teu Deus, tendo cuidado de guardar todos os seus mandamentos que eu hoje te ordeno, o Senhor teu Deus te exaltará sobre todas as nações da terra; \n" +
                                        "\n" +
                                        "Deuteronômio 28:1",
                                        0, "", 1, false));
                            }else if(contador == 9){
                                lista.add(new MensagensModel(9, amigo, amigo, data, hora_atual,"Fica em paz! Todos aqueles que duvidaram do seu ministério vão ter que sentar para ouvir você cantar e pregar.\n\n#O tempo de Deus é perfeito",
                                        0, "", 1, false));
                            }else if(contador == 10){
                                lista.add(new MensagensModel(10, amigo, amigo, data, hora_atual,"Não há erros nos planos de Deus! Senão deu certo é porque Ele tem algo melhor preparado para ti.\n\n#Nem olhos viram nem ouvidos ouviram",
                                        0, "", 1, false));
                            }else if(contador == 11){

                                lista.add(new MensagensModel(11, amigo, amigo, data, hora_atual,"Mas o Senhor disse a Samuel: Não atentes para a sua aparência, nem para a grandeza da sua estatura, porque eu o rejeitei; porque o Senhor não vê como vê o homem, pois o homem olha para o que está diante dos olhos, porém o Senhor olha para o coraçao. \n" +
                                        "\n" +
                                        "1 Samuel 16:7",
                                        0, "", 1, false));
                            }else{
                                MensagensModel m = new MensagensModel(1, amigo, amigo, data, hora_atual, "AH NÃO :( . O MEU PROGRAMADOR SÓ ME ENSINOU ESSAS RESPOSTAS. VOLTE MAIS TARDE PARA QUE EU POSSA APRENDER MAIS COISAS!", 0, "", 1, false);
                                lista.add(m);
                            }


                        } catch (Exception e) {
                            MensagensModel m = new MensagensModel(1, amigo, amigo, data, hora_atual, "AH NÃO. MEUS CIRCUITOS INTERNOS FALHARAM. TENTE CONVERSAR COMIGO MAIS TARDE", 0, "", 1, false);
                            lista.add(m);
                        }


                    }


                    int tamanho = lista.size();
                    recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);
                    contador = contador + 1;
                    adapter.notifyDataSetChanged();
                    txtOnlineUser.setText(getString(R.string.online));
                    //txtOnlineUser.setTextColor(ColorStateList.valueOf(0xff707070));


                }
            }, 6000);

        }

    }


}
