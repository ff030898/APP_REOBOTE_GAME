package com.reobotetechnology.reobotegame.ui.home.fragments.profile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.BooksOfBibleAdapters;
import com.reobotetechnology.reobotegame.adapter.ProfileConquistesAdapters;
import com.reobotetechnology.reobotegame.adapter.ProfileMatchesAdapters;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.helper.ConfigurationFirebase;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.ConquistesModel;
import com.reobotetechnology.reobotegame.model.LivrosBibliaModel;
import com.reobotetechnology.reobotegame.model.Notification;
import com.reobotetechnology.reobotegame.model.PartidaModel;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.bible.biblia_capitulos.CapitulosActivity;
import com.reobotetechnology.reobotegame.ui.bible.biblia_livros.ListBibliaGrid;
import com.reobotetechnology.reobotegame.ui.notifications.NotificacoesActivity;
import com.reobotetechnology.reobotegame.ui.profile.editar_perfil.EditarPerfilActivity;
import com.reobotetechnology.reobotegame.ui.view_images_screen.VisualizarImagemActivity;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    //SwipeRefresh
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private ConstraintLayout constraintPrincipal;

    //Configurações do banco de dados
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();
    private String imagem = null;


    //Toolbar
    private CircleImageView profileImage;
    private TextView textWelcome, textDescriptionNotifications;
    private Button btn_notifications;



    //Profile
    private CircleImageView profile_main;
    private TextView txt_username, txt_email, txtSeguindoUsuarioPerfil, txtSeguidoresUsuarioPerfil;


    //Status
    private TextView txtRankingUsuarioPerfil;
    private TextView bioDescription;

    //Status Info Completed
    private TextView txtRankingStatus;
    private TextView txtSeguindoStatus;
    private TextView txtSeguidoresStatus;
    private TextView txtVictoryStatus;
    private TextView txtDerrotedStatus;
    private TextView txtEmpatedStatus;
    private TextView txtBioUserStatus;


    //List Conquist
    private ProfileConquistesAdapters adapterConquist;
    private ArrayList<ConquistesModel> listConquist = new ArrayList<>();

    // List book favorite
    private BooksOfBibleAdapters adapterFavorites;
    private List<LivrosBibliaModel> listFavorites = new ArrayList<>();
    private int tamanho = 0;

    // list Matches
    private ProfileMatchesAdapters adapterMatches;
    private List<PartidaModel> listMatches = new ArrayList<>();

    private BottomSheetDialog bottomSheetDialog;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        progressBar = root.findViewById(R.id.progressBar3);
        constraintPrincipal = root.findViewById(R.id.constraintPrincipal);


        //configuracoes de objetos
        autenticacao = ConfigurationFirebase.getFirebaseAutenticacao();
        user = autenticacao.getCurrentUser();

        swipeRefresh = root.findViewById(R.id.swipe);

        progressBar.setVisibility(View.VISIBLE);
        constraintPrincipal.setVisibility(View.GONE);

        //Refresh
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        onStart();
                        swipeRefresh.setRefreshing(false);
                    }
                }, 2000);

            }
        });

        //Toolbar

        textWelcome = root.findViewById(R.id.textWelcome);
        textDescriptionNotifications = root.findViewById(R.id.textDescriptionNotifications);
        btn_notifications = root.findViewById(R.id.btn_notifications);

        profileImage = root.findViewById(R.id.profile);

        btn_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NotificacoesActivity.class));
            }
        });


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NotificacoesActivity.class));
            }
        });

        FloatingActionButton btn_edit = root.findViewById(R.id.btn_edit);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditarPerfilActivity.class));
            }
        });


        //status

        profile_main = root.findViewById(R.id.profile_main);

        profile_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), VisualizarImagemActivity.class);

                i.putExtra("nome", user.getDisplayName());
                i.putExtra("imagem", imagem);
                startActivity( i );
            }
        });

        txt_username = root.findViewById(R.id.txt_username);
        txt_email = root.findViewById(R.id.txt_email);
        txtRankingUsuarioPerfil = root.findViewById(R.id.txtRankingUsuarioPerfil);
        txtSeguindoUsuarioPerfil = root.findViewById(R.id.txtSeguindoUsuarioPerfil);
        txtSeguidoresUsuarioPerfil = root.findViewById(R.id.txtSeguidoresUsuarioPerfil);
        bioDescription = root.findViewById(R.id.textView13);


        TextView editar = root.findViewById(R.id.editar);
        editar.setText(Html.fromHtml("<u>"+getString(R.string.editar)+"</u>"));

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAnotattion();
            }
        });



        //StatusCompleted

        txtRankingStatus = root.findViewById(R.id.textView29);
        txtSeguindoStatus = root.findViewById(R.id.textView33);
        txtSeguidoresStatus = root.findViewById(R.id.textView35);
        txtVictoryStatus = root.findViewById(R.id.textView37);
        txtEmpatedStatus = root.findViewById(R.id.textView39);
        txtDerrotedStatus = root.findViewById(R.id.textView41);
        txtBioUserStatus = root.findViewById(R.id.textView17);

        RecyclerView recyclerRanking = root.findViewById(R.id.recyclerConquistes);
        adapterConquist = new ProfileConquistesAdapters(listConquist, getActivity());

        //RecyclerRanking
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerRanking.setLayoutManager(layoutManager);
        recyclerRanking.setHasFixedSize(true);
        recyclerRanking.setAdapter(adapterConquist);


        ImageView ic_new = root.findViewById(R.id.ic_novo);

        RecyclerView recyclerNovoTestamento = root.findViewById(R.id.recyclerLivrosNovo);

        adapterFavorites = new BooksOfBibleAdapters(listFavorites, getActivity());

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerNovoTestamento.setLayoutManager(layoutManager2);
        recyclerNovoTestamento.setHasFixedSize(true);
        recyclerNovoTestamento.setAdapter(adapterFavorites);

        ic_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ListBibliaGrid.class);
                i.putExtra("cd_testamento", 1);
                startActivity(i);
            }
        });

        //Recycler Novo
        recyclerNovoTestamento.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerNovoTestamento,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                tamanho = listFavorites.size();

                                if (tamanho > 2) {

                                    LivrosBibliaModel livroSelecionado = listFavorites.get(position);
                                    Intent i = new Intent(getActivity(), CapitulosActivity.class);
                                    i.putExtra("nm_livro", livroSelecionado.getNome());
                                    i.putExtra("livroSelecionado", livroSelecionado.getId());
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


        //List Matches

        RecyclerView recyclerMatch = root.findViewById(R.id.recyclerMatches);
        adapterMatches = new ProfileMatchesAdapters(listMatches, getActivity());

        //RecyclerRanking
        RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager(getActivity());
        recyclerMatch.setLayoutManager(layoutManager5);
        recyclerMatch.setHasFixedSize(true);
        recyclerMatch.setAdapter(adapterMatches);


        return root;
    }

    //Usuario Logado
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void viewProfile() {

        if (autenticacao.getCurrentUser() != null) {

            try {
                if (user.getPhotoUrl() == null) {
                    Glide
                            .with(ProfileFragment.this)
                            .load(R.drawable.profile)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(profileImage);

                    Glide
                            .with(ProfileFragment.this)
                            .load(R.drawable.profile)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(profile_main);


                } else {
                    imagem = user.getPhotoUrl().toString();

                    Glide
                            .with(ProfileFragment.this)
                            .load(user.getPhotoUrl())
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(profileImage);

                    Glide
                            .with(ProfileFragment.this)
                            .load(user.getPhotoUrl())
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(profile_main);


                }

                textWelcome.setText(getString(R.string.meu_perfil));
                textDescriptionNotifications.setText("Mantenha seu perfil atualizado");

                String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));

                firebaseRef.child("usuarios").child(idUsuario).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        UsuarioModel user = dataSnapshot.getValue(UsuarioModel.class);
                        if (user != null) {
                            //STATUS
                            txt_username.setText(user.getNome());
                            txt_email.setText(user.getEmail());
                            //txt_email.setText("Nível: "+user.getNivel());
                            txtRankingUsuarioPerfil.setText(user.getRanking()+"º");
                            txtSeguindoUsuarioPerfil.setText(""+user.getSeguidores());
                            txtSeguidoresUsuarioPerfil.setText(""+user.getSeguindo());
                            bioDescription.setText(getString(R.string.sobre));

                            //INFO
                            txtRankingStatus.setText(user.getRanking()+"");
                            txtSeguindoStatus.setText(""+user.getSeguidores());
                            txtSeguidoresStatus.setText(""+user.getSeguindo());
                            txtVictoryStatus.setText(""+user.getVitorias());
                            txtEmpatedStatus.setText(""+user.getEmpates());
                            txtDerrotedStatus.setText(""+user.getDerrotas());
                            txtBioUserStatus.setText(getString(R.string.lorem2));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            } catch (Exception e) {
                Glide
                        .with(ProfileFragment.this)
                        .load(R.drawable.profile)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(profileImage);


                Glide
                        .with(ProfileFragment.this)
                        .load(R.drawable.profile)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(profile_main);

            }


            progressBar.setVisibility(View.GONE);
            constraintPrincipal.setVisibility(View.VISIBLE);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getAllNotifications() {

        final String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));
        firebaseRef.child("notifications").child(idUsuario).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                try {
                    int countNoitificationsView = 0;
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        Notification notification = new Notification();
                        String view = Objects.requireNonNull(dados.child("view").getValue()).toString();
                        notification.setView(Boolean.parseBoolean(view));
                        if(!notification.isView()){
                            countNoitificationsView  = countNoitificationsView + 1;
                        }


                    }

                    btn_notifications.setText("" + countNoitificationsView);

                } catch (Exception e) {
                    Log.i("ERRO NOTIFICATION", Objects.requireNonNull(e.getMessage()));
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @SuppressLint("SetTextI18n")
    private void openAnotattion(){

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.include_bottom_sheet_anotation, null);

        view.findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Alerter.create(requireActivity())
                        .setTitle("Obaa...")
                        .setText("Status atualizado com sucesso!")
                        .setIcon(R.drawable.ic_success)
                        .setDuration(2000)
                        .setBackgroundColorRes(R.color.colorGreen1)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Alerter.hide();
                            }
                        })
                        .show();

            }
        });

        bottomSheetDialog = new BottomSheetDialog(requireActivity());
        bottomSheetDialog.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialog = null;
            }
        });

        bottomSheetDialog.show();
    }


    private void listConquist(){

        listConquist.clear();

        listConquist.add(new ConquistesModel("Cadastro", 10, true));
        listConquist.add(new ConquistesModel("Nível 1", 100, true));
        listConquist.add(new ConquistesModel("Nível 2", 150, true));
        listConquist.add(new ConquistesModel("Nível 3", 200, false));
        listConquist.add(new ConquistesModel("Nível 4", 250, false));
        listConquist.add(new ConquistesModel("Nível 5", 300, false));
        listConquist.add(new ConquistesModel("Nível 6", 350, false));
        listConquist.add(new ConquistesModel("Nível 7", 400, false));
        listConquist.add(new ConquistesModel("Nível 8", 450, false));

        adapterConquist.notifyDataSetChanged();
    }

    private void listBookFavorites() {

        listFavorites.clear();

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getActivity());

        List<LivrosBibliaModel> lista3;
        lista3 = dataBaseAcess.listarNovoTestamento();

        if (lista3.size() != 0) {
            listFavorites.addAll(lista3);
        }

        adapterFavorites.notifyDataSetChanged();


    }

    private void listMatches(){

        listMatches.clear();

        /*try {

            //firebaseRef.child("usuarios").orderByChild("pontosD").limitToLast(7)

            firebaseRef.child("partidas").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listMatches.clear();
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        //PartidaModel p = dados.getValue(PartidaModel.class);


                        //String t = dados.getValue().toString();

                        //listMatches.add(p);

                        //Log.d("partida", "id: "+t);

                    }


                    adapterMatches.notifyDataSetChanged();
                    //progressBar.setVisibility(View.GONE);
                    //constraintPrincipal.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception ignored) {

        }*/

        listMatches.add(new PartidaModel("27/02/2021 - 02:12", false, true, false, true, "v", "27/02/2021 - 02:12"));
        listMatches.add(new PartidaModel("25/02/2021 - 02:12", false, true, false, true, "v", "27/02/2021 - 02:12"));
        listMatches.add(new PartidaModel("22/02/2021 - 02:12", false, true, false, true, "v", "27/02/2021 - 02:12"));
        listMatches.add(new PartidaModel("23/02/2021 - 02:12", false, true, false, true, "v", "27/02/2021 - 02:12"));
        listMatches.add(new PartidaModel("21/02/2021 - 02:12", false, true, false, true, "v", "27/02/2021 - 02:12"));
        listMatches.add(new PartidaModel("20/02/2021 - 02:12", false, true, false, true, "v", "27/02/2021 - 02:12"));

        adapterMatches.notifyDataSetChanged();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        viewProfile();
        getAllNotifications();
        listConquist();
        listBookFavorites();
        listMatches();

    }
}
