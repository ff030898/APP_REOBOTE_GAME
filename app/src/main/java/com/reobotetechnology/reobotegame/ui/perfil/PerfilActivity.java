package com.reobotetechnology.reobotegame.ui.perfil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import com.reobotetechnology.reobotegame.ui.perfil.editar_perfil.EditarPerfilActivity;
import com.reobotetechnology.reobotegame.ui.visualizar_imagens.VisualizarImagemActivity;


import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class PerfilActivity extends AppCompatActivity {

    CircleImageView imagemPerfil;
    Button btn_editar_perfil;
    TextView txtRankingUsuarioPerfil, txtNomeUsuarioPerfil, txtSeguindoUsuarioPerfil, txtSeguidoresUsuarioPerfil;

    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private FirebaseUser userF = autenticacao.getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    String imagem = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Meu Perfil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagemPerfil = findViewById(R.id.imgPerfil);
        btn_editar_perfil = findViewById(R.id.btn_editar_perfil);
        txtNomeUsuarioPerfil = findViewById(R.id.txtNomeUsuarioPerfil);
        txtRankingUsuarioPerfil = findViewById(R.id.txtRankingUsuarioPerfil);
        txtSeguindoUsuarioPerfil = findViewById(R.id.txtSeguindoUsuarioPerfil);
        txtSeguidoresUsuarioPerfil = findViewById(R.id.txtSeguidoresUsuarioPerfil);

        recuperaUsuario();


        btn_editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditarPerfilActivity.class));
            }
        });

        imagemPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), VisualizarImagemActivity.class);

                i.putExtra("nome", userF.getDisplayName());
                i.putExtra("imagem", imagem);
                startActivity( i );
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperaUsuario() {

        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();

        if (autenticacao.getCurrentUser() != null) {
            String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(userF.getEmail()));

            databaseReference.child("usuarios").child(idUsuario).addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UsuarioModel user = dataSnapshot.getValue(UsuarioModel.class);

                    if(user!=null){

                        txtNomeUsuarioPerfil.setText(userF.getDisplayName());
                        txtRankingUsuarioPerfil.setText(user.getRanking()+"º");
                        txtSeguidoresUsuarioPerfil.setText(""+user.getSeguidores());
                        txtSeguindoUsuarioPerfil.setText(""+user.getSeguindo());

                        try{

                            if(user.getImagem().isEmpty()){

                                Glide
                                        .with(getApplicationContext())
                                        .load(R.drawable.user)
                                        .centerCrop()
                                        .placeholder(R.drawable.user)
                                        .into(imagemPerfil);
                            }else {

                                imagem = user.getImagem();
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStart() {
        super.onStart();
        recuperaUsuario();
    }
}
