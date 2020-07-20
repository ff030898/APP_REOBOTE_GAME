package com.reobotetechnology.reobotegame.ui.amigos_list;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.AmigosPesquisarAdapters;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.amigos_profile.Amigos_PerfilActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AmigosActivity extends AppCompatActivity {

    RecyclerView recyclerAmigosPesquisar;
    AmigosPesquisarAdapters adapter;
    List<UsuarioModel> lista = new ArrayList<>();
    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();

    private ProgressBar progressBar2;
    private EditText editPesquisar;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        //Configurações iniciais
        progressBar2 = findViewById(R.id.progressBar2);
        editPesquisar = findViewById(R.id.editPesquisar);
        recyclerAmigosPesquisar = findViewById(R.id.recyclerAmigosPesquisar);

        progressBar2.setVisibility(View.VISIBLE);
        recyclerAmigosPesquisar.setVisibility(View.GONE);
        editPesquisar.setVisibility(View.GONE);

        adapter = new AmigosPesquisarAdapters(lista, getApplicationContext(), 0);

        //RecyclerRanking
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerAmigosPesquisar.setLayoutManager(layoutManager);
        recyclerAmigosPesquisar.setHasFixedSize(true);
        recyclerAmigosPesquisar.setAdapter(adapter);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Procurar Amigos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        this.listarUsuarios("");

        editPesquisar.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if(editPesquisar.getText().toString().equals("")) {

                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        ((InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE)))
                                .hideSoftInputFromWindow(editPesquisar.getWindowToken(), 0);
                    }*/

                    listarUsuarios("");

                }else{


                    listarUsuarios(editPesquisar.getText().toString());

                }


            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        //Configurar evento de clique no recyclerview
        recyclerAmigosPesquisar.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerAmigosPesquisar,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                UsuarioModel usuarioSelecionado = lista.get( position );
                                Intent i = new Intent(getApplicationContext(), Amigos_PerfilActivity.class);
                                i.putExtra("id", usuarioSelecionado.getEmail());
                                startActivity( i );


                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );




    }

    private void listarUsuarios(String texto){

        firebaseRef.child("usuarios").orderByChild("nome")
                .startAt(texto)
                .endAt(texto+"\uf8ff").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    lista.clear();
                    for (DataSnapshot dados: dataSnapshot.getChildren() ){

                        UsuarioModel usuario2Model = dados.getValue( UsuarioModel.class );

                        assert usuario2Model != null;
                        if (!usuario2Model.getEmail().equals(user.getEmail())) {
                            lista.add(usuario2Model);
                        }

                    }

                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
                progressBar2.setVisibility(View.GONE);
                editPesquisar.setVisibility(View.VISIBLE);
                recyclerAmigosPesquisar.setVisibility(View.VISIBLE);

            }
        }, 2000);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sair, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // Finaliza a Activity atual e assim volta para a tela anterior
                break;
            case R.id.menu_sair:
                finish();
                break;

            default:
                break;
        }
        return true;
    }
}
