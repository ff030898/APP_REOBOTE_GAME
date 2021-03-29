package com.reobotetechnology.reobotegame.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.dao.DataBaseHCAcess;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.main.create.CadastroActivity;
import com.reobotetechnology.reobotegame.ui.home.HomeActivity;
import com.reobotetechnology.reobotegame.ui.politices.PoliticesActivity;
import com.reobotetechnology.reobotegame.ui.termes.TermosActivity;


import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class WelcomeActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    Button mGoogle;
    private GoogleSignInClient mGoogleSignInClient;
    private SweetAlertDialog pDialog;

    Button btnCadastro;
    FrameLayout logo;
    ConstraintLayout itens;
    ProgressBar progressoIniciar;

    TextView txtRegras, txtPolitica;

    private FirebaseAuth mAuth;
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();

    private static final int version = 2;

    //Animation
    Animation topAnim, bottomAnim;
    String token = "";

    ConstraintLayout constraintPrincipal;


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();
        mGoogle = findViewById(R.id.btnGoogle);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#0066cc"));
        pDialog.setTitleText("Carregando");
        pDialog.setCancelable(false);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        btnCadastro = findViewById(R.id.btnEmail);
        itens = findViewById(R.id.itens);
        progressoIniciar = findViewById(R.id.progressoIniciar);
        logo = findViewById(R.id.logo);
        constraintPrincipal = findViewById(R.id.constraintPrincipal);

        progressoIniciar.setVisibility(View.VISIBLE);
        constraintPrincipal.setVisibility(View.GONE);

        txtRegras = findViewById(R.id.txtRegras);
        txtPolitica = findViewById(R.id.txtPolitica);

        SpannableString content = new SpannableString(getString(R.string.regras_e_termos_de_uso));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtRegras.setText(content);

        SpannableString content2 = new SpannableString(getString(R.string.politica_privacidade));
        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
        txtPolitica.setText(content2);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                iniciar();


            }
        }, 3000);

        try {
            inicializarBancoDeDados();
        } catch (Exception e) {
            Log.d("Erro: ", Objects.requireNonNull(e.getMessage()));
        }

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }


    public void usuarioLogado() {

        mAuth = ConfigurationFireBase.getFirebaseAutenticacao();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }
    }


    public void iniciar() {

        progressoIniciar.setVisibility(View.GONE);
        constraintPrincipal.setVisibility(View.VISIBLE);

    }

    public void cadastro(View view) {
        Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
        startActivity(intent);

    }

    public void termos(View view) {
        startActivity(new Intent(getApplicationContext(), TermosActivity.class));
    }

    public void politices(View view){
        startActivity(new Intent(getApplicationContext(), PoliticesActivity.class));
    }

    private void inicializarBancoDeDados() {

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
        DataBaseHCAcess dataBaseHCAcess = DataBaseHCAcess.getInstance(getApplicationContext());

        try {

            if (version > 1) {
                dataBaseAcess.onUpdate();

            } else {
                dataBaseAcess.onCreate();

            }

            dataBaseHCAcess.onCreate();



        } catch (Exception e) {
            alert("ERRO: O LOGIN COM O GOOGLE SÓ VAI FUNCIONAR QUANTO O APP ESTIVER PRONTO. CLIQUE EM CONTINUAR COM EMAIL");
        }

    }

    private void alert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                alert("ERRO: O LOGIN COM O GOOGLE SÓ VAI FUNCIONAR QUANTO O APP ESTIVER PRONTO. CLIQUE EM CONTINUAR COM EMAIL");

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        pDialog.show();
        //alert("firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getAdditionalUserInfo()).isNewUser()) {
                                assert user != null;
                                String email = user.getEmail();
                                assert email != null;
                                String idUsuario = Base64Custom.codificarBase64(email);
                                String imagem = Objects.requireNonNull(user.getPhotoUrl()).toString().replace("s96-c", "s384-c");
                                Log.i("EXECUTADO", "FUI EXECUTADO TOKEN");

                                UsuarioModel usuario2Model = new UsuarioModel(idUsuario, user.getDisplayName(), email, "", imagem,
                                        "", "", "", 0, 0, 0, 0,
                                        0, 0, 0, 0, 0, 0, false, false, false, false, false,
                                        false, false, false, false);
                                usuario2Model.salvar();
                                atualizarToken(email);
                            }
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //alert("Erro: "+task.getException());
                            alert("LOGIN COM O GOOGLE SÓ VAI FUNCIONAR QUANDO O APP ESTIVER PRONTO. CLIQUE EM CONTINUAR COM EMAIL E SE CADASTRE");

                            updateUI(null);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alert("ERRO AO TENTAR ENTRAR COM CONTA DA GOGOLE");
            }
        });
    }

    private void atualizarToken(final String email) {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                token = instanceIdResult.getToken();
                //Log.i("getInstanceId", "token getInstanceId: " + token);
                String idUsuario = Base64Custom.codificarBase64(email);
                DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
                usuarioRef.child("token").setValue(token);
            }
        });
    }


    private void updateUI(FirebaseUser currentUser) {
        pDialog.dismiss();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }


    @Override
    protected void onStart() {
        super.onStart();
        usuarioLogado();
    }
}
