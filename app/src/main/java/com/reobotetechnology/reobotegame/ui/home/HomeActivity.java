package com.reobotetechnology.reobotegame.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.RankingAdapters;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.helper.UserOnline;
import com.reobotetechnology.reobotegame.model.Notification;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.amigos_list.AmigosActivity;
import com.reobotetechnology.reobotegame.ui.configuraçoes.ConfiguracoesActivity;
import com.reobotetechnology.reobotegame.ui.main.WelcomeActivity;
import com.reobotetechnology.reobotegame.ui.notificações.NotificacoesActivity;
import com.reobotetechnology.reobotegame.ui.partida.CarregarPartidaActivity;
import com.reobotetechnology.reobotegame.ui.perfil.PerfilActivity;
import com.reobotetechnology.reobotegame.utils.ChecarSegundoPlano;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Constraints;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;
    private AppBarConfiguration mAppBarConfiguration;

    CircleImageView imagemUsuarioMenu;
    TextView txtUsuarioMenu, txtEmailMenu;


    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private FirebaseUser user;
    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebaseDataBase();

    TextView textNotifications;
    int mNotificationsCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        user = autenticacao.getCurrentUser();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_biblia, R.id.nav_slideshow, R.id.nav_ranking, R.id.nav_mensagens)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        NavigationUI.setupWithNavController(navigation, navController);

        imagemUsuarioMenu = drawer.findViewById(R.id.imagemUsuarioMenu);
        txtUsuarioMenu = drawer.findViewById(R.id.txtUsuarioMenu);
        txtEmailMenu = drawer.findViewById(R.id.txtEmailMenu);

        BadgeDrawable badgeDrawable = navigation.getBadge(R.id.nav_slideshow);
        if (badgeDrawable == null) {
            navigation.getOrCreateBadge(R.id.nav_slideshow).setNumber(15);
        }

        BadgeDrawable badgeDrawable2 = navigation.getBadge(R.id.nav_mensagens);
        if (badgeDrawable2 == null) {
            navigation.getOrCreateBadge(R.id.nav_mensagens).setNumber(1);
        }

        BadgeDrawable badgeDrawable3 = navigation.getBadge(R.id.nav_biblia);
        if (badgeDrawable3 == null) {
            navigation.getOrCreateBadge(R.id.nav_biblia).setNumber(5);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void pesquisarConvites() {

        String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(Objects.requireNonNull(autenticacao.getCurrentUser()).getEmail()));

        DatabaseReference usuarioRef = firebaseRef.child("notifications").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {

                    Notification notification = dataSnapshot.getValue(Notification.class);

                    assert notification != null;

                    if (notification.getTipo().equals("partida")) {

                        String idUsuario = Base64Custom.decodificarBase64(notification.getFromId());
                        String idExcluir = notification.getToId();
                        mNotificationsCount = 1;
                        setupBadge();
                        modalConvite(notification.getFromName(), notification.getIdPartida(), idUsuario, idExcluir, notification.getFromImage());

                    }


                } catch (Exception e) {
                    Log.i("Erro: ", Objects.requireNonNull(e.getMessage()));

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void modalConvite(final String nome, final String idPartida, final String email, final String idExcluir, final String imagem) {


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.modal_convidar, null);
        alertDialog.setCancelable(false);

        final CircleImageView img_jogador_invite = mView.findViewById(R.id.img_jogador_invite);
        final Button btnAceitar = mView.findViewById(R.id.btnAceitar);
        final Button btnRecusar = mView.findViewById(R.id.btnRecusar);
        final TextView txtConvite = mView.findViewById(R.id.txtDescJogador);
        txtConvite.setText("O jogador(a): " + nome + " está te convidando para jogar uma partida online.\n\nDeseja aceitar o desafio?");

        try {
            if (imagem.isEmpty()) {
                //Picasso.get().load(R.drawable.user).into(holder.img);
                img_jogador_invite.setImageResource(R.drawable.user);
            } else {
                Picasso.get().load(imagem).into(img_jogador_invite);
            }
        } catch (Exception e) {
            img_jogador_invite.setImageResource(R.drawable.user);
        }

        alertDialog.setView(mView);
        final AlertDialog alert = alertDialog.create();
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        alert.getWindow().setLayout(Constraints.LayoutParams.WRAP_CONTENT, Constraints.LayoutParams.WRAP_CONTENT);
        InsetDrawable inset = new InsetDrawable(back, 130);
        Objects.requireNonNull(alert.getWindow()).setBackgroundDrawable(inset);
        alert.show();

            btnAceitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);

                        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                try {

                                    String desconectado = Objects.requireNonNull(dataSnapshot.child("desconectado").getValue()).toString();

                                    if (desconectado.equals("true")) {
                                        Toast.makeText(getApplicationContext(), "OOH NÃO!\nO jogador(a) " + nome + " desistiu da partida", Toast.LENGTH_LONG).show();
                                        alert.dismiss();
                                    } else {

                                        DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);
                                        usuarioRef.child("aceito").setValue(true);
                                        DatabaseReference usuarioRef2 = firebaseRef.child("notifications");
                                        usuarioRef2.child(idExcluir).removeValue();
                                        Intent intent = new Intent(getApplicationContext(), CarregarPartidaActivity.class);
                                        intent.putExtra("token", "");
                                        intent.putExtra("email", email);
                                        intent.putExtra("convidado", "sim");
                                        intent.putExtra("idPartida", idPartida);
                                        startActivity(intent);
                                        finish();
                                        alert.dismiss();
                                    }

                                } catch (Exception e) {


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } catch (Exception e) {

                    }

                }
            });

            btnRecusar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);
                    usuarioRef.child("recusado").setValue(true);
                    DatabaseReference usuarioRef2 = firebaseRef.child("notifications");
                    usuarioRef2.child(idExcluir).removeValue();
                    alert.dismiss();
                }
            });


    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);

        final MenuItem menuItem = menu.findItem(R.id.menu_notificacoes);

        View actionView = menuItem.getActionView();
        textNotifications = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    onOptionsItemSelected(menuItem);
                }
            }
        });

        return true;
    }

    private void setupBadge() {
        if (textNotifications != null) {
            if (mNotificationsCount == 0) {
                if (textNotifications.getVisibility() != View.GONE) {
                    textNotifications.setVisibility(View.GONE);
                }
            } else {
                textNotifications.setText(String.valueOf(Math.min(mNotificationsCount, 99)));
                if (textNotifications.getVisibility() != View.VISIBLE) {
                    textNotifications.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    //ao clicar nos itens do menu
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_sair) {
            finish();

        } else if (item.getItemId() == R.id.menu_logoff) {

            /*if (user != null) {
                String idUsuario = Base64Custom.codificarBase64((Objects.requireNonNull(user.getEmail())));
                DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
                usuarioRef.child("online").setValue(false);
            }*/
            autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
            autenticacao.signOut();
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();

        } else if (item.getItemId() == R.id.menu_perfil) {
            startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
        } else if (item.getItemId() == R.id.menu_configuracoes) {
            startActivity(new Intent(getApplicationContext(), ConfiguracoesActivity.class));
        } else if (item.getItemId() == R.id.menu_notificacoes) {
            mNotificationsCount = 0;
            setupBadge();
            startActivity(new Intent(getApplicationContext(), NotificacoesActivity.class));
        } else if (item.getItemId() == R.id.menu_amigos) {
            startActivity(new Intent(getApplicationContext(), AmigosActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getApplicationContext(), "Pressione novamente para sair", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateNavHeader() {

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        txtUsuarioMenu = headerView.findViewById(R.id.txtUsuarioMenu);
        txtEmailMenu = headerView.findViewById(R.id.txtEmailMenu);
        imagemUsuarioMenu = headerView.findViewById(R.id.imagemUsuarioMenu);

        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();

        if (autenticacao.getCurrentUser() != null) {

            try {

                if (user.getPhotoUrl() == null) {
                    Picasso.get().load(R.drawable.user).into(imagemUsuarioMenu);
                } else {
                    Picasso.get().load(user.getPhotoUrl()).into(imagemUsuarioMenu);
                }
            } catch (Exception e) {
                Picasso.get().load(R.drawable.user).into(imagemUsuarioMenu);
            }

            txtUsuarioMenu.setText(user.getDisplayName());
            txtEmailMenu.setText(user.getEmail());

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            updateNavHeader();
            UserOnline userOnline = (UserOnline) getApplication();
            getApplication().registerActivityLifecycleCallbacks(userOnline);
            pesquisarConvites();

        }
    }
}
