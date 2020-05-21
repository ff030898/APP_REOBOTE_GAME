package com.reobotetechnology.reobotegame.ui.home;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.AmigosAdapters;
import com.reobotetechnology.reobotegame.adapter.RankingAdapters;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.BibliaModel;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.model.VersiculosModel;
import com.reobotetechnology.reobotegame.ui.AmigosActivity;
import com.reobotetechnology.reobotegame.ui.PartidaActivity;
import com.reobotetechnology.reobotegame.ui.BibliaActivity;
import com.reobotetechnology.reobotegame.ui.Amigos_PerfilActivity;
import com.reobotetechnology.reobotegame.ui.PerfilActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {


    RecyclerView recyclerAmigos, recyclerRanking;
    private AmigosAdapters adapter;
    private RankingAdapters adapterRanking;
    private ArrayList<UsuarioModel> lista = new ArrayList<>();
    private ArrayList<UsuarioModel> listaRanking = new ArrayList<>();


    //palavra do dia
    private int livro, capitulo, versiculo;
    private List<BibliaModel> livroLista;
    private List<VersiculosModel> versiculoLista;
    private String nm_versiculo, nm_livro;
    private TextView lerNaBiblia, txtNomeUsuario;
    private ImageView imgCopiar, imgCompartilhar, menuPesqusiarAmigos;
    TextView txtPalavra, txtVerso;
    CircleImageView profileImage;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        profileImage = root.findViewById(R.id.imagem_usuario_conversa);
        txtNomeUsuario = root.findViewById(R.id.txtNomeUsuario);
        final Button btnJogar = root.findViewById(R.id.btnJogar);
        menuPesqusiarAmigos = root.findViewById(R.id.menuPesqusiarAmigos);

        //Ler na Biblia
        txtPalavra = root.findViewById(R.id.txtPalavra);
        txtVerso = root.findViewById(R.id.txtVerso);
        lerNaBiblia = root.findViewById(R.id.lerNaBiblia);

        try{

        livro();
        capitulo();
        versiculo();

        txtPalavra.setText(nm_versiculo);
        txtVerso.setText(nm_livro+" "+capitulo+":"+versiculo);

        }catch (Exception e){
            txtPalavra.setText("ERRO");
            txtVerso.setText("ERRO 0:0");
        }

        //Copiar e Compartilhar
        imgCopiar = root.findViewById(R.id.imgCopiar);
        imgCompartilhar = root.findViewById(R.id.imgCompartilhar);

        imgCopiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(livro != 0) {
                    ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Versiculos", txtPalavra.getText() + "\n\n" + txtVerso.getText());
                    clipboardManager.setPrimaryClip(clipData);
                    alert("Texto copiado para area de transferência");
                }
            }
        });

        //Configurações iniciais
        recyclerAmigos = root.findViewById(R.id.recyclerAmigos);
        recyclerRanking = root.findViewById(R.id.recyclerRanking);

        //configurarAdapter
        adapter = new AmigosAdapters(lista, getActivity());
        adapterRanking = new RankingAdapters(listaRanking, getActivity());



        //RecyclerAmigos
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerAmigos.setLayoutManager(layoutManager);
        recyclerAmigos.setHasFixedSize(true);
        recyclerAmigos.setAdapter(adapter);

        //RecyclerRanking
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        recyclerRanking.setLayoutManager(layoutManager2);
        recyclerRanking.setHasFixedSize(true);
        recyclerRanking.setAdapter(adapterRanking);


        int img = R.drawable.eu;
        int img2  = R.drawable.danilo;
        int img3 = R.drawable.isabela;
        int img4 = R.drawable.sabrina;
        int img5 = R.drawable.maria;
        int img6 = R.drawable.julia;


        UsuarioModel amigo = new UsuarioModel(1,"Fabrício", img, 1800, 1);
        lista.add(amigo);
        listaRanking.add(amigo);

        amigo = new UsuarioModel(2,"Isabela", img3, 1600, 2);
        lista.add(amigo);
        listaRanking.add(amigo);

        amigo = new UsuarioModel(3, "Danilo", img2, 1500, 3);
        lista.add(amigo);
        listaRanking.add(amigo);

        amigo = new UsuarioModel(4, "Sabrina", img4, 1400, 4);
        lista.add(amigo);
        listaRanking.add(amigo);

        amigo = new UsuarioModel(5, "Maria", img5, 1300, 5);
        lista.add(amigo);
        listaRanking.add(amigo);

        amigo = new UsuarioModel(6, "Julia", img6, 1250, 6);
        lista.add(amigo);


        for(int i = 0; i<=5; i++) {
            amigo = new UsuarioModel(1, "Fabrício", img, 1200, i+7);
            lista.add(amigo);

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
                                Intent i = new Intent(getActivity(), Amigos_PerfilActivity.class);
                                i.putExtra("id", usuarioSelecionado.getId());
                                i.putExtra("nome", usuarioSelecionado.getNome());
                                i.putExtra("imagem", usuarioSelecionado.getImg());
                                i.putExtra("ranking", usuarioSelecionado.getRanking());
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
        recyclerRanking.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerRanking,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {


                                UsuarioModel usuarioSelecionado = lista.get( position );
                                Intent i = new Intent(getActivity(), Amigos_PerfilActivity.class);
                                i.putExtra("id", usuarioSelecionado.getId());
                                i.putExtra("nome", usuarioSelecionado.getNome());
                                i.putExtra("imagem", usuarioSelecionado.getImg());
                                i.putExtra("ranking", usuarioSelecionado.getRanking());
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


        btnJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PartidaActivity.class));
            }
        });

        lerNaBiblia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(livro != 0) {
                    Intent i = new Intent(getActivity(), BibliaActivity.class);
                    i.putExtra("nm_livro", nm_livro);
                    i.putExtra("livroSelecionado", livro);
                    i.putExtra("capitulo", capitulo);
                    i.putExtra("versiculo", versiculo);
                    startActivity(i);
                }
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PerfilActivity.class);
                startActivity( i );
            }
        });

        menuPesqusiarAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AmigosActivity.class));
            }
        });


        return root;

    }

    private void alert(String s) {
        Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
    }

    private void livro(){


        try {
            DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getActivity());
            livroLista = dataBaseAcess.listarLivroPalavra();
            BibliaModel p = livroLista.get(0);
            if (p.getId() != 0 ) {
                livro = p.getId();
                nm_livro = p.getNome();
            } else {
                livro = 0;
                nm_livro = "ERRO";
            }
        }catch (Exception e){
            livro = 0;
            nm_livro = "ERRO";
        }

    }

    private void capitulo(){

        try {

            if (livro != 0) {
                DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getActivity());
                capitulo = dataBaseAcess.capituloPalavra(livro);
            } else {
                capitulo = 0;
            }
        }catch (Exception e){
            capitulo = 0;
        }


    }

    private void versiculo(){

        try {

            if (livro != 0) {
                DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getActivity());
                versiculoLista = dataBaseAcess.listarVersiculoPalavra(livro, capitulo);
                VersiculosModel v = versiculoLista.get(0);
                versiculo = v.getVerso();
                nm_versiculo = v.getText();

            } else {
                versiculo = 0;
                nm_livro = "ERRO";
            }

        }catch (Exception e){
            versiculo = 0;
            nm_livro = "ERRO";
        }

    }


}
