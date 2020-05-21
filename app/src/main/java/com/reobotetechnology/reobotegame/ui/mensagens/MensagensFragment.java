package com.reobotetechnology.reobotegame.ui.mensagens;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.AmigosAdapters;
import com.reobotetechnology.reobotegame.adapter.MensagensAdapters;
import com.reobotetechnology.reobotegame.adapter.RankingAdapters;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.MensagensModel;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.Amigos_PerfilActivity;
import com.reobotetechnology.reobotegame.ui.ChatActivity;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MensagensFragment extends Fragment {

    RecyclerView recyclerAmigos, recyclerMensagens;
    private AmigosAdapters adapter;
    private MensagensAdapters adapterMensagens;
    private ArrayList<UsuarioModel> lista = new ArrayList<>();
    private ArrayList<MensagensModel> listaMensagens = new ArrayList<>();

    private MensagensViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.mensagens_fragment, container, false);



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
        recyclerAmigos = root.findViewById(R.id.recyclerAmigosOnline);
        recyclerMensagens = root.findViewById(R.id.recyclerConversas);

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


        int imgR = R.drawable.reobote;
        int img = R.drawable.eu;
        int img2  = R.drawable.danilo;
        int img3 = R.drawable.isabela;
        int img4 = R.drawable.sabrina;
        int img5 = R.drawable.maria;
        int img6 = R.drawable.julia;


        UsuarioModel amigo = new UsuarioModel(1,"Reobote Technology", imgR, 1800, 1);
        //lista.add(amigo);

        MensagensModel m = new MensagensModel(1, amigo, amigo, data, hora_atual,
                "Olá, Tudo bem? Eu sou a inteligência artifical do Reobote Game", 0, "", 1,false);
        listaMensagens.add(m);

        amigo = new UsuarioModel(1,"Fabrício", img, 1800, 1);
        lista.add(amigo);

        m = new MensagensModel(1, amigo, amigo, data, hora_atual,
                "Olá, Tudo bem? Meu nome é "+amigo.getNome()+". Estou enviando está mensagem para você testar o layout", 0, "", 1,false);
        listaMensagens.add(m);

        amigo = new UsuarioModel(2,"Isabela", img3, 1600, 2);
        lista.add(amigo);
        m = new MensagensModel(1, amigo, amigo, data, hora_atual,
                "Olá, Tudo bem? Meu nome é "+amigo.getNome()+". Estou enviando está mensagem para você testar o layout", 0, "", 1,false);
        listaMensagens.add(m);



        amigo = new UsuarioModel(3, "Danilo", img2, 1500, 3);
        lista.add(amigo);
        m = new MensagensModel(1, amigo, amigo, data, hora_atual,
                "Olá, Tudo bem? Meu nome é "+amigo.getNome()+". Estou enviando está mensagem para você testar o layout", 0, "", 1,false);
        listaMensagens.add(m);


        amigo = new UsuarioModel(4, "Sabrina", img4, 1400, 4);
        lista.add(amigo);
        m = new MensagensModel(1, amigo, amigo, data, hora_atual,
                "Olá, Tudo bem? Meu nome é "+amigo.getNome()+". Estou enviando está mensagem para você testar o layout", 0, "", 1,false);
        listaMensagens.add(m);


        amigo = new UsuarioModel(5, "Maria", img5, 1300, 5);
        lista.add(amigo);
        m = new MensagensModel(1, amigo, amigo, data, hora_atual,
                "Olá, Tudo bem? Meu nome é "+amigo.getNome()+". Estou enviando está mensagem para você testar o layout", 0, "", 1,false);
        listaMensagens.add(m);


        amigo = new UsuarioModel(6, "Julia", img6, 1250, 6);
        lista.add(amigo);
        m = new MensagensModel(1, amigo, amigo, data, hora_atual,
                "Olá, Tudo bem? Meu nome é "+amigo.getNome()+". Estou enviando está mensagem para você testar o layout", 0, "", 1,false);
        listaMensagens.add(m);


        for(int i = 0; i<=5; i++) {
            amigo = new UsuarioModel(1, "Fabrício", img, 1200, i+7);
            lista.add(amigo);
            m = new MensagensModel(1, amigo, amigo, data, hora_atual,
                    "Olá, Tudo bem? Meu nome é "+amigo.getNome()+". Estou enviando está mensagem para você testar o layout", 0, "", 1,false);
            listaMensagens.add(m);
        }


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
                                i.putExtra("imagem", usuarioSelecionado.getImg());
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
                                i.putExtra("imagem", usuarioSelecionado.getEnvia().getImg());
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


}
