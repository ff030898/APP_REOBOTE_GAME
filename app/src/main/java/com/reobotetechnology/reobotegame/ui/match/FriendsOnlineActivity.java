package com.reobotetechnology.reobotegame.ui.match;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.UsuarioModel;

import java.util.ArrayList;
import java.util.Objects;

public class FriendsOnlineActivity extends AppCompatActivity {
    RecyclerView recyclerAmigos;
    //private AmigosPesquisarAdapters adapter;

    private ArrayList<UsuarioModel> lista = new ArrayList<>();

    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();

    private ProgressBar progressBar2;
    private EditText editPesquisar;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos_online);



        //Configurações iniciais
        progressBar2 = findViewById(R.id.progressBar2);
        recyclerAmigos = findViewById(R.id.recyclerPost);
        editPesquisar = findViewById(R.id.editPesquisar);

        progressBar2.setVisibility(View.VISIBLE);
        recyclerAmigos.setVisibility(View.GONE);
        editPesquisar.setVisibility(View.GONE);

        //configurarAdapter
        //adapter = new AmigosPesquisarAdapters(lista, getApplicationContext(), 1);

        //RecyclerAmigos
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerAmigos.setLayoutManager(layoutManager);
        recyclerAmigos.setHasFixedSize(true);
        //recyclerAmigos.setAdapter(adapter);

        listarAmigos("");

        editPesquisar.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (editPesquisar.getText().toString().equals("")) {

                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        ((InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE)))
                                .hideSoftInputFromWindow(editPesquisar.getWindowToken(), 0);
                    }*/

                    listarAmigos("");

                } else {


                    listarAmigos(editPesquisar.getText().toString());

                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        //Configurar evento de clique no recyclerview
        recyclerAmigos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerAmigos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                UsuarioModel usuarioSelecionado = lista.get(position);

                                if (usuarioSelecionado.isJogando()) {
                                    //Se usuario estiver jogando
                                    //Modal de Aviso que usuário já está em outra partida
                                    alert("Jogador(a): " + usuarioSelecionado.getNome() + " está em uma partida no momento. Aguarde 5 minutos ou convide outro amigo");
                                } else {

                                    Intent i = new Intent(getApplicationContext(), LoadingMatchActivity.class);
                                    i.putExtra("token", usuarioSelecionado.getToken());
                                    i.putExtra("email", usuarioSelecionado.getEmail());
                                    i.putExtra("convidado", "nao");
                                    i.putExtra("idPartida", "");
                                    startActivity(i);

                                }

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



    private void listarAmigos(String texto) {

        firebaseRef.child("usuarios").orderByChild("nome")
                .startAt(texto)
                .endAt(texto + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                lista.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    try {

                        UsuarioModel usuario2Model = dados.getValue(UsuarioModel.class);

                        assert usuario2Model != null;
                        if (!usuario2Model.getEmail().equals(user.getEmail())) {
                            lista.add(usuario2Model);
                        }

                    }catch (Exception e){
                        Log.i("Erro: ", Objects.requireNonNull(e.getMessage()));
                    }

                }

                //adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //adapter.notifyDataSetChanged();
                progressBar2.setVisibility(View.GONE);
                editPesquisar.setVisibility(View.VISIBLE);
                recyclerAmigos.setVisibility(View.VISIBLE);

            }
        }, 2000);
    }

    private void alert(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }



}
