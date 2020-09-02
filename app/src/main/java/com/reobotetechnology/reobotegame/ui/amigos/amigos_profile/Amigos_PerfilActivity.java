package com.reobotetechnology.reobotegame.ui.amigos.amigos_profile;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.amigos.amigos_list.AmigosActivity;
import com.reobotetechnology.reobotegame.ui.visualizar_imagens.VisualizarImagemActivity;


import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Amigos_PerfilActivity extends AppCompatActivity {

    Button btnSeguir;
    CircleImageView imagemPerfil;
    TextView txtRanking, txtNomePerfil, txtSeguindo, txtSeguidores;
    String id, imagem, nome;

    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebaseDataBase();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos_perfil);


        imagemPerfil = findViewById(R.id.imagemPerfil);
        txtNomePerfil = findViewById(R.id.nomePerfil);

        txtRanking = findViewById(R.id.txtRanking);
        txtSeguindo = findViewById(R.id.txtSeguindo);
        txtSeguidores = findViewById(R.id.txtSeguidores);
        btnSeguir = findViewById(R.id.btnSeguir);


        Bundle extras = getIntent().getExtras();
        assert extras != null;
        id = extras.getString("id");

        recuperaUsuario();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        imagemPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), VisualizarImagemActivity.class);
                i.putExtra("nome", nome);
                i.putExtra("imagem", imagem);
                startActivity( i );
            }
        });


        btnSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String texto = btnSeguir.getText().toString();

                if (texto.equals("SEGUIR")) {


                    btnSeguir.setText("SEGUINDO");


                } else {
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        int background = R.drawable.btn_screen;
                        btnSeguir.setBackground(getResources().getDrawable(background));

                    }*/

                    btnSeguir.setText("SEGUIR");
                    //btnSeguir.setTextColor(ColorStateList.valueOf(0xffffffff));
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperaUsuario() {

        if (autenticacao.getCurrentUser() != null) {

            String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(id));

            firebaseRef.child("usuarios").child(idUsuario).addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UsuarioModel user = dataSnapshot.getValue(UsuarioModel.class);

                    if(user!=null){

                        nome = user.getNome();
                        imagem = user.getImagem();

                        txtNomePerfil.setText(nome);
                        txtRanking.setText(""+user.getRanking());
                        txtSeguidores.setText(""+user.getSeguidores());
                        txtSeguindo.setText(""+user.getSeguindo());

                        try{

                            if(imagem.isEmpty()){

                                Glide
                                        .with(getApplicationContext())
                                        .load(R.drawable.user)
                                        .centerCrop()
                                        .placeholder(R.drawable.user)
                                        .into(imagemPerfil);
                            }else {
                                Glide
                                        .with(getApplicationContext())
                                        .load(imagem)
                                        .centerCrop()
                                        .placeholder(R.drawable.user)
                                        .into(imagemPerfil);
                            }
                        }catch (Exception e){
                            Glide
                                    .with(getApplicationContext())
                                    .load(R.drawable.user)
                                    .centerCrop()
                                    .placeholder(R.drawable.user)
                                    .into(imagemPerfil);
                        }

                        Objects.requireNonNull(getSupportActionBar()).setTitle(nome);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ver_todos, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // Finaliza a Activity atual e assim volta para a tela anterior
                break;
            case R.id.menu_ver_todos_amigos:
              startActivity(new Intent(getApplicationContext(), AmigosActivity.class));
              finish();
              break;

            default:
                break;
        }
        return true;
    }





}
