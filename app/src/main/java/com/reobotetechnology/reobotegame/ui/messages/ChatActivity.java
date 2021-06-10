package com.reobotetechnology.reobotegame.ui.messages;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.MessagesChatAdapters;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.MensagensModel;
import com.reobotetechnology.reobotegame.model.Message;
import com.reobotetechnology.reobotegame.model.UserModel;
import com.reobotetechnology.reobotegame.ui.friends.ViewImageScreenActivity;
import com.reobotetechnology.reobotegame.utils.GeneratorID;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String nome, texto, imagem;
    private String data_completa, hora_atual;
    private int contador = 1;

    private TextView txtUsername, txtOnlineUser;
    private CircleImageView profileUser;

    private RecyclerView recyclerChatMensagens;
    private MessagesChatAdapters adapter;
    private ArrayList<Message> lista = new ArrayList<>();

    private EditText editMensagem;

    //Configurações do banco de dados
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();

    private ProgressBar progressBar;


    private ConstraintLayout constraintMain;
    private ImageButton btn_back;

    private int tamanho = 0;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatNotification = new SimpleDateFormat("dd-MM-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        Calendar cal2 = Calendar.getInstance();
        Date data2 = new Date();
        cal2.setTime(data2);
        Date data_atual = cal2.getTime();

        data_completa = dateFormatNotification.format(data2);
        hora_atual = timeFormat.format(data_atual);


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

        }


        profileUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), ViewImageScreenActivity.class);
                i.putExtra("nome", nome);
                i.putExtra("imagem", imagem);
                startActivity(i);
            }
        });

        editMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tamanho = lista.size();
                if (tamanho > 0) {
                    recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);
                }
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getUserMessage(String nome) {

        try {

            txtUsername.setText(nome);

            if (nome.equals("Reobote IA")) {

                profileUser.setImageResource(R.drawable.reobote);
                String idUserChat = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));
                firebaseRef.child("chat").child(idUserChat).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        lista.clear();

                        for (DataSnapshot dados : dataSnapshot.getChildren()) {

                            try {
                                Message m = dados.getValue(Message.class);
                                lista.add(m);
                            } catch (Exception ignored) {

                            }

                        }


                        tamanho = lista.size();
                        recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

        } catch (Exception ignored) {

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendMessage(View view) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                txtOnlineUser.setText(getString(R.string.digited));
                //txtOnlineUser.setTextColor(ColorStateList.valueOf(0xff505050));

            }
        }, 4000);


        texto = editMensagem.getText().toString();

        if (!texto.isEmpty()) {

            Message m = new Message();
            String id = GeneratorID.id();
            m.setId(id);
            m.setText(Base64Custom.codificarBase64(texto));
            String date_time = data_completa + "/" + hora_atual;
            m.setTimestamp(date_time);
            m.setFromId(Base64Custom.codificarBase64(getString(R.string.email_robot)));
            m.setToId(Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail())));
            m.setView(true);
            m.setType(0);
            m.save();

            editMensagem.setText("");

            adapter.notifyDataSetChanged();
            recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    if (nome.equals("Reobote IA")) {

                        try {

                            if (contador == 1) {
                                Message m = new Message();
                                String id = GeneratorID.id();
                                m.setId(id);
                                m.setText(Base64Custom.codificarBase64("Você é uma pessoa incrivel. Sabia? Deus vai honrar a tua fé.\uD83D\uDE0D"));
                                String date_time = data_completa + "/" + hora_atual;
                                m.setTimestamp(date_time);
                                m.setFromId(Base64Custom.codificarBase64(getString(R.string.email_robot)));
                                m.setToId(Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail())));
                                m.setView(false);
                                m.setType(1);
                                m.save();

                                adapter.notifyDataSetChanged();
                                recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);


                            } else if (contador == 2) {

                                Message m = new Message();
                                String id = GeneratorID.id();
                                m.setId(id);
                                m.setText(Base64Custom.codificarBase64("Vai dormir. Quem te prometeu garante <3"));
                                String date_time = data_completa + "/" + hora_atual;
                                m.setTimestamp(date_time);
                                m.setFromId(Base64Custom.codificarBase64(getString(R.string.email_robot)));
                                m.setToId(Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail())));
                                m.setView(false);
                                m.setType(1);
                                m.save();

                                adapter.notifyDataSetChanged();
                                recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);


                            } else if (contador == 3) {

                                Message m = new Message();
                                String id = GeneratorID.id();
                                m.setId(id);
                                m.setText(Base64Custom.codificarBase64("Palavra do céu para você.\n\nResponderam eles: Crê no Senhor Jesus e serás salvo, tu e tua casa. \n\nAtos 16:31"));
                                String date_time = data_completa + "/" + hora_atual;
                                m.setTimestamp(date_time);
                                m.setFromId(Base64Custom.codificarBase64(getString(R.string.email_robot)));
                                m.setToId(Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail())));
                                m.setView(false);
                                m.setType(1);
                                m.save();

                                adapter.notifyDataSetChanged();
                                recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);


                            } else if (contador == 4) {

                                Message m = new Message();
                                String id = GeneratorID.id();
                                m.setId(id);
                                m.setText(Base64Custom.codificarBase64("Se acalma. Se você ainda está solteiro(a) vai casar antes da volta do Filho do Homem. \\uD83D\\uDE0D\\n\\n #Creia no Milagre\""));
                                String date_time = data_completa + "/" + hora_atual;
                                m.setTimestamp(date_time);
                                m.setFromId(Base64Custom.codificarBase64(getString(R.string.email_robot)));
                                m.setToId(Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail())));
                                m.setView(false);
                                m.setType(1);
                                m.save();

                                adapter.notifyDataSetChanged();
                                recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);

                            } else if (contador == 5) {

                                Message m = new Message();
                                String id = GeneratorID.id();
                                m.setId(id);
                                m.setText(Base64Custom.codificarBase64("Você ainda vai cantar, pregar e evangelizar muito pelo mundo inteiro.\n\n#O tempo de Deus é perfeito"));
                                String date_time = data_completa + "/" + hora_atual;
                                m.setTimestamp(date_time);
                                m.setFromId(Base64Custom.codificarBase64(getString(R.string.email_robot)));
                                m.setToId(Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail())));
                                m.setView(false);
                                m.setType(1);
                                m.save();

                                adapter.notifyDataSetChanged();
                                recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);


                            } else if (contador == 6) {


                                Message m = new Message();
                                String id = GeneratorID.id();
                                m.setId(id);
                                m.setText(Base64Custom.codificarBase64("Nós somos os Profetas da última hora\n\nMuitas pessoas vão se converter atráves da sua oração."));
                                String date_time = data_completa + "/" + hora_atual;
                                m.setTimestamp(date_time);
                                m.setFromId(Base64Custom.codificarBase64(getString(R.string.email_robot)));
                                m.setToId(Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail())));
                                m.setView(false);
                                m.setType(1);
                                m.save();

                                adapter.notifyDataSetChanged();
                                recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);

                            } else if (contador == 7) {

                                Message m = new Message();
                                String id = GeneratorID.id();
                                m.setId(id);
                                m.setText(Base64Custom.codificarBase64("A Bíblia diz em Atos 28. Que Paulo foi picado por uma víbora.\n\nPorém ele não morreu. Mas, a Bíblia não diz se doeu ou não.\n\nAs lutas dentro da sua casa, igreja ou do mundo podem até doer e te entristecer. Mas, assim como Paulo você também não vai morrer por causa das víboras"));
                                String date_time = data_completa + "/" + hora_atual;
                                m.setTimestamp(date_time);
                                m.setFromId(Base64Custom.codificarBase64(getString(R.string.email_robot)));
                                m.setToId(Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail())));
                                m.setView(false);
                                m.setType(1);
                                m.save();

                                adapter.notifyDataSetChanged();
                                recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);


                            } else if (contador == 8) {

                                Message m = new Message();
                                String id = GeneratorID.id();
                                m.setId(id);
                                m.setText(Base64Custom.codificarBase64("Palavra do céu para você.\n\nSe ouvires atentamente a voz do Senhor teu Deus, tendo cuidado de guardar todos os seus mandamentos que eu hoje te ordeno, o Senhor teu Deus te exaltará sobre todas as nações da terra; \n" +
                                        "\n" +
                                        "Deuteronômio 28:1"));
                                String date_time = data_completa + "/" + hora_atual;
                                m.setTimestamp(date_time);
                                m.setFromId(Base64Custom.codificarBase64(getString(R.string.email_robot)));
                                m.setToId(Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail())));
                                m.setView(false);
                                m.setType(1);
                                m.save();

                                adapter.notifyDataSetChanged();
                                recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);


                            } else if (contador == 9) {

                                Message m = new Message();
                                String id = GeneratorID.id();
                                m.setId(id);
                                m.setText(Base64Custom.codificarBase64("Fica em paz! Todos aqueles que duvidaram do seu ministério vão ter que sentar para ouvir você cantar e pregar.\n\n#O tempo de Deus é perfeito"));
                                String date_time = data_completa + "/" + hora_atual;
                                m.setTimestamp(date_time);
                                m.setFromId(Base64Custom.codificarBase64(getString(R.string.email_robot)));
                                m.setToId(Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail())));
                                m.setView(false);
                                m.setType(1);
                                m.save();

                                adapter.notifyDataSetChanged();
                                recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);


                            } else if (contador == 10) {

                                Message m = new Message();
                                String id = GeneratorID.id();
                                m.setId("" + id);
                                m.setText(Base64Custom.codificarBase64("Não há erros nos planos de Deus! Senão deu certo é porque Ele tem algo melhor preparado para ti.\n\n#Nem olhos viram nem ouvidos ouviram"));
                                String date_time = data_completa + "/" + hora_atual;
                                m.setTimestamp(date_time);
                                m.setFromId(Base64Custom.codificarBase64(getString(R.string.email_robot)));
                                m.setToId(Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail())));
                                m.setView(false);
                                m.setType(1);
                                m.save();

                                adapter.notifyDataSetChanged();
                                recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);


                            } else if (contador == 11) {

                                Message m = new Message();
                                String id = GeneratorID.id();
                                m.setId("" + id);
                                m.setText(Base64Custom.codificarBase64("Mas o Senhor disse a Samuel: Não atentes para a sua aparência, nem para a grandeza da sua estatura, porque eu o rejeitei; porque o Senhor não vê como vê o homem, pois o homem olha para o que está diante dos olhos, porém o Senhor olha para o coraçao. \n" +
                                        "\n" +
                                        "1 Samuel 16:7"));
                                String date_time = data_completa + "/" + hora_atual;
                                m.setTimestamp(date_time);
                                m.setFromId(Base64Custom.codificarBase64(getString(R.string.email_robot)));
                                m.setToId(Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail())));
                                m.setView(false);
                                m.setType(1);
                                m.save();

                                adapter.notifyDataSetChanged();
                                recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);


                            } else {
                                Message m = new Message();
                                String id = GeneratorID.id();
                                m.setId("" + id);
                                m.setText(Base64Custom.codificarBase64("AH NÃO :( . O MEU PROGRAMADOR SÓ ME ENSINOU ESSAS RESPOSTAS. VOLTE MAIS TARDE PARA QUE EU POSSA APRENDER MAIS COISAS!"));
                                String date_time = data_completa + "/" + hora_atual;
                                m.setTimestamp(date_time);
                                m.setFromId(Base64Custom.codificarBase64(getString(R.string.email_robot)));
                                m.setToId(Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail())));
                                m.setView(false);
                                m.setType(1);
                                m.save();

                                adapter.notifyDataSetChanged();
                                recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);


                            }


                        } catch (Exception e) {
                            Message m = new Message();
                            String id = GeneratorID.id();
                            m.setId("" + id);
                            m.setText(Base64Custom.codificarBase64("AH NÃO :( . O MEU PROGRAMADOR SÓ ME ENSINOU ESSAS RESPOSTAS. VOLTE MAIS TARDE PARA QUE EU POSSA APRENDER MAIS COISAS!"));
                            String date_time = data_completa + "/" + hora_atual;
                            m.setTimestamp(date_time);
                            m.setFromId(Base64Custom.codificarBase64(getString(R.string.email_robot)));
                            m.setToId(Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail())));
                            m.setView(false);
                            m.setType(1);
                            m.save();

                            adapter.notifyDataSetChanged();
                            recyclerChatMensagens.smoothScrollToPosition(tamanho - 1);
                        }


                    }


                    contador = contador + 1;
                    adapter.notifyDataSetChanged();
                    txtOnlineUser.setText(getString(R.string.online));
                    //txtOnlineUser.setTextColor(ColorStateList.valueOf(0xff707070));


                }
            }, 6000);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStart() {
        getUserMessage("Reobote IA");
        super.onStart();
    }
}
