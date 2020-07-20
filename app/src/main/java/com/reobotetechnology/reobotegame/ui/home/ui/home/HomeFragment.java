package com.reobotetechnology.reobotegame.ui.home.ui.home;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.AmigosAdapters;
import com.reobotetechnology.reobotegame.adapter.RankingAdapters;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.LivrosBibliaModel;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.model.BibliaModel;
import com.reobotetechnology.reobotegame.ui.amigos_list.AmigosActivity;
import com.reobotetechnology.reobotegame.ui.partida.ModoPartidaActivity;
import com.reobotetechnology.reobotegame.ui.biblia.BibliaActivity;
import com.reobotetechnology.reobotegame.ui.amigos_profile.Amigos_PerfilActivity;
import com.reobotetechnology.reobotegame.ui.perfil.PerfilActivity;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {


    private AmigosAdapters adapter;
    private RankingAdapters adapterRanking;
    private ArrayList<UsuarioModel> lista = new ArrayList<>();
    private ArrayList<UsuarioModel> listaRanking = new ArrayList<>();

    //palavra do dia
    private int livro, capitulo, versiculo;
    private String nm_versiculo, nm_livro;
    private TextView txtNomeUsuario;
    private TextView txtPalavra, txtVerso;
    private CircleImageView profileImage;
    private ProgressBar progressBar;
    private LinearLayout linearPrincipal;

    private DatabaseReference usuariosRef;
    DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebaseDataBase();
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerUsuario;
    private FirebaseUser user = autenticacao.getCurrentUser();

    private RecyclerView recyclerRanking;

    private AdView mAdView;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mAdView = root.findViewById(R.id.adView);

        progressBar = root.findViewById(R.id.progressBar3);
        linearPrincipal = root.findViewById(R.id.LinearPrincipal);
        profileImage = root.findViewById(R.id.imagem_usuario_conversa);
        txtNomeUsuario = root.findViewById(R.id.txtTitulo);
        ImageView imgTeste = root.findViewById(R.id.imgTeste);
        final Button btnJogar = root.findViewById(R.id.btnJogar);
        ImageView menuPesqusiarAmigos = root.findViewById(R.id.menuPesqusiarAmigos);

        progressBar.setVisibility(View.VISIBLE);
        linearPrincipal.setVisibility(View.GONE);

        //Ler na Biblia
        txtPalavra = root.findViewById(R.id.txtPalavra);
        txtVerso = root.findViewById(R.id.txtVerso);
        TextView lerNaBiblia = root.findViewById(R.id.lerNaBiblia);

        try {
            livro();
            capitulo();
            versiculo();
            txtPalavra.setText(nm_versiculo);
            txtVerso.setText(nm_livro + " " + capitulo + ":" + versiculo);

        } catch (Exception e) {
            txtPalavra.setText(R.string.versiculo);
            txtVerso.setText("Gênesis 26:22");
        }

        //Copiar e Compartilhar
        ImageView imgCopiar = root.findViewById(R.id.imgCopiar);
        ImageView imgCompartilhar = root.findViewById(R.id.imgCompartilhar);

        imgCopiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (livro != 0) {
                    ClipboardManager clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Versiculos", txtPalavra.getText() + "\n\n" + txtVerso.getText());
                    assert clipboardManager != null;
                    clipboardManager.setPrimaryClip(clipData);
                    alert("Texto copiado para area de transferência");
                }
            }
        });

        imgCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                /*share.putExtra(Intent.EXTRA_SUBJECT,
                        txtPalavra.getText() + "\n\n" + txtVerso.getText());*/
                share.putExtra(Intent.EXTRA_TEXT,
                        txtPalavra.getText() + "\n\n" + txtVerso.getText());

                startActivity(Intent.createChooser(share, "Compartilhar"));
            }
        });

        //Configurações iniciais
        RecyclerView recyclerAmigos = root.findViewById(R.id.recyclerPost);
        recyclerRanking = root.findViewById(R.id.recyclerRanking);

        //configurarAdapter
        adapter = new AmigosAdapters(lista, getActivity());


        //RecyclerAmigos
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerAmigos.setLayoutManager(layoutManager);
        recyclerAmigos.setHasFixedSize(true);
        recyclerAmigos.setAdapter(adapter);


        //Eventos de Clique

        //Configurar evento de clique no recyclerview
        recyclerAmigos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerAmigos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                UsuarioModel usuarioSelecionado = lista.get(position);
                                Intent i = new Intent(getActivity(), Amigos_PerfilActivity.class);
                                i.putExtra("id", usuarioSelecionado.getEmail());
                                startActivity(i);

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

                                int tamanho = listaRanking.size();
                                UsuarioModel usuarioSelecionado = listaRanking.get((tamanho - position - 1));
                                if(usuarioSelecionado.getEmail().equals(user.getEmail())){
                                    startActivity(new Intent(getActivity(), PerfilActivity.class));
                                }else {
                                    Intent i = new Intent(getActivity(), Amigos_PerfilActivity.class);
                                    i.putExtra("id", usuarioSelecionado.getEmail());
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


        btnJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ModoPartidaActivity.class));
            }
        });

        lerNaBiblia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (livro != 0) {
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
                startActivity(i);
            }
        });

        menuPesqusiarAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AmigosActivity.class));
            }
        });

        imgTeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return root;

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperaUsuario() {

        if (autenticacao.getCurrentUser() != null) {

            txtNomeUsuario.setText(user.getDisplayName());

            try {
                if (user.getPhotoUrl() == null) {
                    Picasso.get().load(R.drawable.user).into(profileImage);
                } else {
                    Picasso.get().load(user.getPhotoUrl()).into(profileImage);
                }
            } catch (Exception e) {
                Picasso.get().load(R.drawable.user).into(profileImage);
            }

        }


    }

    private void alert(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    private void livro() {


        try {
            DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getActivity());
            List<LivrosBibliaModel> livroLista = dataBaseAcess.listarLivroPalavra();
            LivrosBibliaModel p = livroLista.get(0);
            if (p.getId() != 0) {
                livro = p.getId();
                nm_livro = p.getNome();
            } else {
                livro = 0;
                nm_livro = "ERRO";
            }
        } catch (Exception e) {
            livro = 0;
            nm_livro = "ERRO";
        }

    }

    private void capitulo() {

        try {

            if (livro != 0) {
                DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getActivity());
                capitulo = dataBaseAcess.capituloPalavra(livro);
            } else {
                capitulo = 0;
            }
        } catch (Exception e) {
            capitulo = 0;
        }


    }

    private void versiculo() {

        try {

            if (livro != 0) {
                DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getActivity());
                List<BibliaModel> versiculoLista = dataBaseAcess.listarVersiculoPalavra(livro, capitulo);
                BibliaModel v = versiculoLista.get(0);
                versiculo = v.getVerso();
                nm_versiculo = v.getText();

            } else {
                versiculo = 0;
                nm_livro = "ERRO";
            }

        } catch (Exception e) {
            versiculo = 0;
            nm_livro = "ERRO";
        }

    }

    private void listarUsuarios() {

        firebaseRef.child("usuarios").addValueEventListener(new ValueEventListener() {
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
                    }catch(Exception e){
                        Log.i("ERRO: ", Objects.requireNonNull(e.getMessage()));
                    }

                }

                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void atualizarRanking(){
        final List<UsuarioModel> listaRankingAll = new ArrayList<>();


        try {

            firebaseRef.child("usuarios").orderByChild("pontosG").addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listaRankingAll.clear();
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        UsuarioModel usuario2Model = dados.getValue(UsuarioModel.class);

                        listaRankingAll.add(usuario2Model);
                    }

                    int tamanho = listaRankingAll.size();

                    for(int i=0; i<tamanho; i++){
                        int pos = (i + 1);
                        UsuarioModel usuarioSelecionado = listaRankingAll.get((tamanho - i - 1));
                        String idUsuario = Base64Custom.codificarBase64((Objects.requireNonNull(usuarioSelecionado.getEmail())));
                        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
                        usuarioRef.child("ranking").setValue(pos);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (Exception ignored){

        }
    }

    private void listarUsuariosRanking() {

        adapterRanking = new RankingAdapters(listaRanking, getActivity(), 1);

        //RecyclerRanking
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        recyclerRanking.setLayoutManager(layoutManager2);
        recyclerRanking.setHasFixedSize(true);
        recyclerRanking.setAdapter(adapterRanking);

        try {

            firebaseRef.child("usuarios").orderByChild("pontosD").limitToLast(5).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listaRanking.clear();
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        UsuarioModel usuario2Model = dados.getValue(UsuarioModel.class);

                        listaRanking.add(usuario2Model);

                    }

                    adapterRanking.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    linearPrincipal.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (Exception ignored){

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);

            }
        }, 2000);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        recuperaUsuario();
        listarUsuarios();
        atualizarRanking();
        listarUsuariosRanking();

    }
}
