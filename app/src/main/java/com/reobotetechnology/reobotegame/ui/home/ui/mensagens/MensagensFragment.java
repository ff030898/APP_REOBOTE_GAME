package com.reobotetechnology.reobotegame.ui.home.ui.mensagens;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.AmigosAdapters;
import com.reobotetechnology.reobotegame.adapter.MensagensAdapters;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.MensagensModel;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.chat.ChatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MensagensFragment extends Fragment {

    RecyclerView recyclerAmigos, recyclerMensagens;
    private AmigosAdapters adapter;
    private MensagensAdapters adapterMensagens;
    private ArrayList<UsuarioModel> lista = new ArrayList<>();
    private ArrayList<MensagensModel> listaMensagens = new ArrayList<>();


    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebaseDataBase();
    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();

    private ProgressBar progressBar4;

    private LinearLayout Linear1, Linear2;

    private MensagensViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_mensagens, container, false);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        // OU
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm");

        Calendar cal = Calendar.getInstance();
        Date data = new Date();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        String data_completa = dateFormat.format(data_atual);

        String hora_atual = dateFormat_hora.format(data_atual);


        //Configurações iniciais
        recyclerAmigos = root.findViewById(R.id.recyclerPost);
        recyclerMensagens = root.findViewById(R.id.recyclerConversas);
        progressBar4 = root.findViewById(R.id.progressBar4);
        Linear1 = root.findViewById(R.id.Linear1);
        Linear2 = root.findViewById(R.id.Linear2);

        progressBar4.setVisibility(View.VISIBLE);
        Linear1.setVisibility(View.GONE);
        Linear2.setVisibility(View.GONE);

        //configurarAdapter
        adapter = new AmigosAdapters(lista, getActivity());
        adapterMensagens = new MensagensAdapters(listaMensagens, getActivity());


        //RecyclerAmigos
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerAmigos.setLayoutManager(layoutManager);
        recyclerAmigos.setHasFixedSize(true);
        recyclerAmigos.setAdapter(adapter);

        //RecyclerMensagens
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        recyclerMensagens.setLayoutManager(layoutManager2);
        recyclerMensagens.setHasFixedSize(true);
        recyclerMensagens.setAdapter(adapterMensagens);

        listarAmigos();


        UsuarioModel amigo = new UsuarioModel("1", "Reobote Technology", "", "", "",
                "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0,0,true, false);
        MensagensModel m = new MensagensModel(1, amigo, amigo, data, hora_atual,
                "Olá, Tudo bem? Eu sou a inteligência artifical do Reobote Game", 0, "", 1,false);
        listaMensagens.add(m);


        //Eventos de Clique

        //Configurar evento de clique no recyclerview
        recyclerAmigos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerAmigos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                UsuarioModel usuarioSelecionado = lista.get( position );
                                Intent i = new Intent(getActivity(), ChatActivity.class);
                                i.putExtra("id", usuarioSelecionado.getId());
                                i.putExtra("nome", usuarioSelecionado.getNome());
                                i.putExtra("imagem", usuarioSelecionado.getImagem());
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


        //Configurar evento de clique no recyclerview
        recyclerMensagens.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerMensagens,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                MensagensModel usuarioSelecionado = listaMensagens.get( position );
                                Intent i = new Intent(getActivity(), ChatActivity.class);
                                i.putExtra("id", usuarioSelecionado.getEnvia().getId());
                                i.putExtra("nome", usuarioSelecionado.getEnvia().getNome());
                                i.putExtra("imagem", usuarioSelecionado.getEnvia().getImagem());
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

        return root;
    }

    private void listarAmigos(){

        firebaseRef.child("usuarios").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                lista.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren() ){


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

                adapter.notifyDataSetChanged();

                progressBar4.setVisibility(View.GONE);
                Linear1.setVisibility(View.VISIBLE);
                Linear2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
