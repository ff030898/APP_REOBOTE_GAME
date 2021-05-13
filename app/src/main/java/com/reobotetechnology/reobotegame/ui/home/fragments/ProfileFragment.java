package com.reobotetechnology.reobotegame.ui.home.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
import com.reobotetechnology.reobotegame.model.BooksOfBibleModel;
import com.reobotetechnology.reobotegame.model.Notification;
import com.reobotetechnology.reobotegame.model.MatchModel;
import com.reobotetechnology.reobotegame.model.UserModel;
import com.reobotetechnology.reobotegame.ui.bible.ChaptersActivity;
import com.reobotetechnology.reobotegame.ui.bible.ListBiblieScreen;
import com.reobotetechnology.reobotegame.ui.comment.AnotattionActivity;
import com.reobotetechnology.reobotegame.ui.friends.FriendsListActivity;
import com.reobotetechnology.reobotegame.ui.notifications.NotificationsActivity;
import com.reobotetechnology.reobotegame.ui.main.EditProfileActivity;
import com.reobotetechnology.reobotegame.ui.friends.ViewImageScreenActivity;
import com.tapadoo.alerter.Alerter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    //SwipeRefresh
    //private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private CoordinatorLayout constraintPrincipal;

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
    private TextView txtNivelStatus;
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
    private List<BooksOfBibleModel> listFavorites = new ArrayList<>();
    private int tamanho = 0;

    // list Matches
    private ProfileMatchesAdapters adapterMatches;
    private List<MatchModel> listMatches = new ArrayList<>();

    private BottomSheetDialog bottomSheetDialog;


    private Animation modal_anima;
    private int score, nivelUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        progressBar = root.findViewById(R.id.progressBar3);
        constraintPrincipal = root.findViewById(R.id.constraintPrincipal);


        //configuracoes de objetos
        autenticacao = ConfigurationFirebase.getFirebaseAutenticacao();
        user = autenticacao.getCurrentUser();

        progressBar.setVisibility(View.VISIBLE);
        constraintPrincipal.setVisibility(View.GONE);

        //Animação da Modal de Tempo Esgotado
        modal_anima = AnimationUtils.loadAnimation(getActivity(), R.anim.modal_animation);


        //Toolbar

        textWelcome = root.findViewById(R.id.textWelcome);
        textDescriptionNotifications = root.findViewById(R.id.textDescriptionNotifications);
        btn_notifications = root.findViewById(R.id.btn_notifications);

        profileImage = root.findViewById(R.id.profile);

        btn_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NotificationsActivity.class));
            }
        });


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NotificationsActivity.class));
            }
        });

        FloatingActionButton btn_edit = root.findViewById(R.id.btn_edit);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });


        //status

        profile_main = root.findViewById(R.id.profile_main);

        profile_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), ViewImageScreenActivity.class);

                i.putExtra("nome", user.getDisplayName());
                i.putExtra("imagem", imagem);
                startActivity(i);
            }
        });

        txt_username = root.findViewById(R.id.txt_username);
        txt_email = root.findViewById(R.id.txt_email);
        txtRankingUsuarioPerfil = root.findViewById(R.id.txtRankingUsuarioPerfil);
        txtSeguindoUsuarioPerfil = root.findViewById(R.id.txtSeguindoUsuarioPerfil);
        txtSeguidoresUsuarioPerfil = root.findViewById(R.id.txtSeguidoresUsuarioPerfil);
        bioDescription = root.findViewById(R.id.textView13);


        TextView editar = root.findViewById(R.id.editar);
        editar.setText(Html.fromHtml("<u>" + getString(R.string.editar) + "</u>"));

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAnotattion();
            }
        });


        //StatusCompleted
        txtNivelStatus = root.findViewById(R.id.textViewN2);
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
        recyclerRanking.setAdapter(adapterConquist);


        ImageView ic_new = root.findViewById(R.id.ic_novo);

        RecyclerView recyclerNovoTestamento = root.findViewById(R.id.recyclerLivrosNovo);

        adapterFavorites = new BooksOfBibleAdapters(listFavorites, getActivity());

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerNovoTestamento.setLayoutManager(layoutManager2);
        recyclerNovoTestamento.setAdapter(adapterFavorites);

        ic_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ListBiblieScreen.class);
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

                                    BooksOfBibleModel livroSelecionado = listFavorites.get(position);
                                    Intent i = new Intent(getActivity(), ChaptersActivity.class);
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


        //Recycler Conquist
        recyclerRanking.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerRanking,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onItemClick(View view, int position) {
                                ConquistesModel nivel = listConquist.get(position);
                                //Se nível está ativo e não for o primeiro nível. OpenModal
                                if (nivel.isEnabled() && position > 0) {
                                    openModalConquist(nivel.getDescription(), nivel.getCount());
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
        recyclerMatch.setAdapter(adapterMatches);

        //OpenFriendsList
        txtSeguindoUsuarioPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FriendsListActivity.class);
                i.putExtra("eventList", getString(R.string.seguindoMin));
                startActivity(i);

            }
        });

        txtSeguidoresUsuarioPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FriendsListActivity.class);
                i.putExtra("eventList", getString(R.string.seguidoresMin));
                startActivity(i);
            }
        });


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

                try {
                    String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));

                    firebaseRef.child("usuarios").child(idUsuario).addValueEventListener(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            UserModel user = dataSnapshot.getValue(UserModel.class);
                            if (user != null) {
                                //STATUS
                                txt_username.setText(user.getNome());
                                txt_email.setText(user.getEmail());
                                score = user.getPontosG();
                                listConquist(score);
                                nivelUser = user.getNivel();
                                txtRankingUsuarioPerfil.setText(user.getRanking() + "º");
                                txtSeguindoUsuarioPerfil.setText("" + user.getSeguindo());
                                txtSeguidoresUsuarioPerfil.setText("" + user.getSeguidores());
                                bioDescription.setText("SOBRE MIM");

                                //INFO
                                txtNivelStatus.setText(user.getNivel()+"");
                                txtRankingStatus.setText(user.getRanking() + "");
                                txtSeguindoStatus.setText("" + user.getSeguindo());
                                txtSeguidoresStatus.setText("" + user.getSeguidores());
                                txtVictoryStatus.setText("" + user.getVitorias());
                                txtEmpatedStatus.setText("" + user.getEmpates());
                                txtDerrotedStatus.setText("" + user.getDerrotas());
                                String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. is. Sed tempus laoreea. " +
                                        "Ut vitae neque venenatis neque facilisis pellentesque. Sed tincidunt laoreet mauris sed molestie. " +
                                        "Duis sodales diam eu placerat dapibus.</string>\n";
                                if(user.getDescription().isEmpty() || user.getDescription() == null){
                                    txtBioUserStatus.setText(description);
                                }else {
                                    txtBioUserStatus.setText("" + user.getDescription());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } catch (Exception ignored) {

                }


            } catch (Exception e) {

                profileImage.setImageResource(R.drawable.profile);
                profile_main.setImageResource(R.drawable.profile);
            }




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
                        if (!notification.isView()) {
                            countNoitificationsView = countNoitificationsView + 1;
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
    private void openAnotattion() {
        startActivity(new Intent(requireActivity(), EditProfileActivity.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void listConquist(int score) {

        listConquist.clear();

        listConquist.add(new ConquistesModel("Cadastro", 20, true));

        int count = 1;
        int countNivel = 200;
        int nivelUser = 0;

        for (int i = countNivel; i <= 10000; i += countNivel) {

            if (score > i) {
                listConquist.add(new ConquistesModel("Nível " + count, i, true));
                nivelUser++;
            } else if ((i - score) <= countNivel) {
                listConquist.add(new ConquistesModel("Nível " + count, i, true));
                nivelUser++;
            } else {
                listConquist.add(new ConquistesModel("Nível " + count, i, false));
            }

            count++;
        }

        adapterConquist.notifyDataSetChanged();
        updateNivelUser(nivelUser);

    }

    private void updateNivelUser(int nivel) {

        try {

            String emailUsuario = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                emailUsuario = Objects.requireNonNull(autenticacao.getCurrentUser()).getEmail();
            }
            assert emailUsuario != null;
            String idUsuario = Base64Custom.codificarBase64(emailUsuario);
            DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

            usuarioRef.child("nivel").setValue(nivel);

        } catch (Exception ignored) {

        }

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void openModalConquist(String description, int count) {

        final Dialog nivelDIalog = new Dialog(requireActivity());
        nivelDIalog.setContentView(R.layout.include_modal_nivel);

        CardView cardModal = nivelDIalog.findViewById(R.id.cardModal);
        cardModal.startAnimation(modal_anima);

        ImageButton btn_close = nivelDIalog.findViewById(R.id.btn_close);
        ImageView image = nivelDIalog.findViewById(R.id.nivel);
        TextView txt_title = nivelDIalog.findViewById(R.id.txt_title);
        TextView txtDescription = nivelDIalog.findViewById(R.id.txtDescription);
        TextView txtProgresso = nivelDIalog.findViewById(R.id.txtProgresso);
        ProgressBar progressBar = nivelDIalog.findViewById(R.id.progressBar);
        progressBar.setMax(count);
        progressBar.setProgress(Math.min(score, count));

        txt_title.setText(description);
        if (score <= count) {
            txtProgresso.setText(score + "/" + count+" xp");
        } else {
            image.setImageResource(R.drawable.ic_emogi_happy);
            txtProgresso.setText(count + "/" + count+" xp");
            String desc = getString(R.string.congratulations)+" "+description.toLowerCase();
            txtDescription.setText(desc);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(nivelDIalog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nivelDIalog.dismiss();
            }
        });

        nivelDIalog.show();

    }

    private void listBookFavorites() {

        listFavorites.clear();

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getActivity());

        List<BooksOfBibleModel> lista2;
        lista2 = dataBaseAcess.listarNovoTestamento();

        if (lista2.size() != 0) {
            for (int i = 0; i < lista2.size(); i++) {

                int learningBook = dataBaseAcess.learningBook(lista2.get(i).getId());
                boolean favorited = dataBaseAcess.favorited(lista2.get(i).getId());
                lista2.get(i).setLearning(learningBook);
                lista2.get(i).setFavorited(favorited);
                listFavorites.add(lista2.get(i));
            }
        }

        adapterFavorites.notifyDataSetChanged();


    }

    private void listMatches() {

        listMatches.clear();

        try {

            //firebaseRef.child("usuarios").orderByChild("pontosD").limitToLast(7)

            firebaseRef.child("partidas").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //listMatches.clear();
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        //PartidaModel p = dados.getValue(PartidaModel.class);


                        //String t = dados.getValue().toString();

                        //listMatches.add(p);

                        //Log.d("partida", "partida " + dados.getValue());

                    }


                    //adapterMatches.notifyDataSetChanged();
                    //progressBar.setVisibility(View.GONE);
                    //constraintPrincipal.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception ignored) {

        }

        listMatches.add(new MatchModel("27/02/2021 - 02:12", false, true, false, true, "v", "27/02/2021 - 02:12"));
        listMatches.add(new MatchModel("25/02/2021 - 02:12", false, true, false, true, "v", "27/02/2021 - 02:12"));
        listMatches.add(new MatchModel("22/02/2021 - 02:12", false, true, false, true, "e", "27/02/2021 - 02:12"));
        listMatches.add(new MatchModel("23/02/2021 - 02:12", false, true, false, true, "d", "27/02/2021 - 02:12"));
        listMatches.add(new MatchModel("27/02/2021 - 02:12", false, true, false, true, "d", "27/02/2021 - 02:12"));


        adapterMatches.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                constraintPrincipal.setVisibility(View.VISIBLE);
            }
        }, 1000);

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        viewProfile();
        getAllNotifications();
        listBookFavorites();
        listMatches();

        super.onStart();
    }
}
