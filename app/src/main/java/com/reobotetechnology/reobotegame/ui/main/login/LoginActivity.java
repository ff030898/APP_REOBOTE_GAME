package com.reobotetechnology.reobotegame.ui.main.login;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.home.HomeActivity;
import com.reobotetechnology.reobotegame.ui.main.WelcomeActivity;
import com.reobotetechnology.reobotegame.ui.esqueceu_senha.EsqueceuSenhaActivity;
import com.tapadoo.alerter.Alerter;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {


    ProgressBar progresso;
    Button btnEntrar;
    TextInputEditText editEmail, editSenha;
    ProgressDialog progressDialog;
    TextView txtCadastrar;
    ConstraintLayout constraintAll;

    ImageView voltar;

    //Animation
    Animation topAnim;

    private static final int RC_SIGN_IN = 100;
    Button mGoogle;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebaseDataBase();

    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mGoogle = findViewById(R.id.btnGoogle);

        constraintAll = findViewById(R.id.constraintAll);
        voltar = findViewById(R.id.ic_voltar);
        btnEntrar = findViewById(R.id.btnEntrar);
        txtCadastrar = findViewById(R.id.txtCadastrar);

        SpannableString content = new SpannableString(getString(R.string.não_tem_conta));
        content.setSpan(new UnderlineSpan(), 15, content.length(), 0);
        txtCadastrar.setText(content);

        progresso = findViewById(R.id.progresso);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);

        progresso.setVisibility(View.VISIBLE);
        constraintAll.setVisibility(View.GONE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Aguarde....");

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progresso.setVisibility(View.GONE);
                constraintAll.setVisibility(View.VISIBLE);
                voltar.setAnimation(topAnim);

            }
        }, 2000);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = editEmail.getText().toString().trim();
                String senha = editSenha.getText().toString().trim();

                if (email.isEmpty()) {
                    editEmail.setError("Email não informado!");
                    editEmail.setFocusable(true);

                    Alerter.create(LoginActivity.this)
                            .setTitle("Oops...")
                            .setText("Email não informado!")
                            .setIcon(R.drawable.ic_warning)
                            .setDuration(5000)
                            .setBackgroundColorRes(R.color.colorWarning)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Alerter.hide();
                                }
                            })
                            .show();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //setarErro
                    editEmail.setError("Email inválido!");
                    editEmail.setFocusable(true);

                    Alerter.create(LoginActivity.this)
                            .setTitle("Oops...")
                            .setText("Email inválido!")
                            .setIcon(R.drawable.ic_warning)
                            .setDuration(5000)
                            .setBackgroundColorRes(R.color.colorWarning)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Alerter.hide();
                                }
                            })
                            .show();

                } else if (senha.isEmpty()) {
                    editSenha.setError("Digite a sua senha");
                    editSenha.setFocusable(true);

                    Alerter.create(LoginActivity.this)
                            .setTitle("Oops...")
                            .setText("Digite a sua senha")
                            .setIcon(R.drawable.ic_warning)
                            .setDuration(5000)
                            .setBackgroundColorRes(R.color.colorWarning)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Alerter.hide();
                                }
                            })
                            .show();
                } else {
                    //Tudo válido
                    try {
                        login(email, senha);
                    } catch (Exception e) {

                        Alerter.create(LoginActivity.this)
                                .setTitle("Oops...")
                                .setText("Erro ao tentar fazer login: " +e.getMessage())
                                .setIcon(R.drawable.ic_warning)
                                .setDuration(5000)
                                .setBackgroundColorRes(R.color.colorWarning)
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Alerter.hide();
                                    }
                                })
                                .show();
                    }
                }

            }
        });

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
                Alerter.create(LoginActivity.this)
                        .setTitle("Oops...")
                        .setText("ERRO AO TENTAR ENTRAR COM CONTA DA GOGOLE: "+e.getMessage())
                        .setIcon(R.drawable.ic_warning)
                        .setDuration(5000)
                        .setBackgroundColorRes(R.color.colorWarning)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Alerter.hide();
                            }
                        })
                        .show();
                task.getException();

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        progressDialog.show();
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

                                UsuarioModel usuario2Model = new UsuarioModel(idUsuario, user.getDisplayName(), email, "", imagem, "",
                                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true, false);
                                usuario2Model.salvar();
                                atualizarToken(email);
                            }
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Alerter.create(LoginActivity.this)
                                    .setTitle("Oops...")
                                    .setText("LOGIN COM O GOOGLE SÓ VAI FUNCIONAR QUANDO O APP ESTIVER PRONTO. CLIQUE EM CONTINUAR COM EMAIL E SE CADASTRE: "+task.getException())
                                    .setIcon(R.drawable.ic_warning)
                                    .setDuration(5000)
                                    .setBackgroundColorRes(R.color.colorWarning)
                                    .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Alerter.hide();
                                        }
                                    })
                                    .show();

                            updateUI(null);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Alerter.create(LoginActivity.this)
                        .setTitle("Oops...")
                        .setText("ERRO AO TENTAR ENTRAR COM CONTA DA GOGOLE: "+e)
                        .setIcon(R.drawable.ic_warning)
                        .setDuration(5000)
                        .setBackgroundColorRes(R.color.colorWarning)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Alerter.hide();
                            }
                        })
                        .show();
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
        progressDialog.dismiss();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    public void login(String email, String senha) {
        //Classe Configuração FireBase
        FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();

        progressDialog.show();

        autenticacao.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                    progressDialog.dismiss();

                } else {

                    progressDialog.dismiss();
                    String excessao = "";

                    try {
                        throw task.getException();

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excessao = "Senha incorreta!";
                        editSenha.setFocusable(true);
                    } catch (FirebaseAuthInvalidUserException e) {
                        excessao = "Email não cadastrado!";
                        editEmail.setFocusable(true);
                    } catch (Exception e) {
                        excessao = "Erro ao fazer login " + e.getMessage();
                        e.printStackTrace();
                    }
                    Alerter.create(LoginActivity.this)
                            .setTitle("Oops...")
                            .setText(excessao)
                            .setIcon(R.drawable.ic_warning)
                            .setDuration(5000)
                            .setBackgroundColorRes(R.color.colorWarning)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Alerter.hide();
                                }
                            })
                            .show();
                }
            }

        });
    }

    public void esqueceuSenha(View view) {

        startActivity(new Intent(getApplicationContext(), EsqueceuSenhaActivity.class));

    }

    public void cadastrar(View view) {
        finish();
    }

    public void voltar(View view) {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));

    }

}
