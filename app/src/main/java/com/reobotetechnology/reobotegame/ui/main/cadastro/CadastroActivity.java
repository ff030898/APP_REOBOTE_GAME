package com.reobotetechnology.reobotegame.ui.main.cadastro;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.home.HomeActivity;

import com.reobotetechnology.reobotegame.ui.main.login.LoginActivity;
import com.reobotetechnology.reobotegame.ui.main.selected_image.ProfileActivity;
import com.tapadoo.alerter.Alerter;


import java.util.Objects;


public class CadastroActivity extends AppCompatActivity {

    private ConstraintLayout constraintAll;
    private static final int RC_SIGN_IN = 100;
    Button mGoogle;
    private GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progressDialog;
    Button btnCadastro;
    ImageView voltar;
    ProgressBar progressoIniciar;
    TextView txtLogin;
    TextView txtTermos;

    private UsuarioModel usuario;

    private FirebaseAuth mAuth;
    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebaseDataBase();
    private static final int version = 2;

    //Animation
    Animation topAnim;
    String token = "";


    TextInputEditText editNome, editEmail, editSenha;


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mAuth = FirebaseAuth.getInstance();
        mGoogle = findViewById(R.id.btnGoogle);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Aguarde....");

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        btnCadastro = findViewById(R.id.btnEntrar);
        progressoIniciar = findViewById(R.id.progressoIniciar);
        voltar = findViewById(R.id.ic_voltar);
        constraintAll = findViewById(R.id.constraintAll);

        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);


        progressoIniciar.setVisibility(View.VISIBLE);
        constraintAll.setVisibility(View.GONE);

        txtLogin = findViewById(R.id.txtCadastrar);
        SpannableString content = new SpannableString(getString(R.string.j_tem_conta_entrar));
        content.setSpan(new UnderlineSpan(), 14, content.length(), 0);
        txtLogin.setText(content);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });



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


        //LOGIN COM DADOS

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = Objects.requireNonNull(editNome.getText()).toString().trim();
                String email = Objects.requireNonNull(editEmail.getText()).toString().trim();
                String senha = Objects.requireNonNull(editSenha.getText()).toString().trim();


                //validar
                if (nome.isEmpty()) {
                    editNome.setError("Digite o seu nome");
                    editNome.setFocusable(true);

                    Alerter.create(CadastroActivity.this)
                            .setTitle("Oops...")
                            .setText("Digite o seu nome!")
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


                } else if (nome.equals("Reobote")) {
                    editNome.setError("Esse nome já está reservado pelo sistema");
                    editNome.setFocusable(true);

                    Alerter.create(CadastroActivity.this)
                            .setTitle("Oops...")
                            .setText("Esse nome já está reservado pelo sistema")
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

                } else if (nome.length() < 5) {


                    Alerter.create(CadastroActivity.this)
                            .setTitle("Oops...")
                            .setText("O nome deve ter no mínimo 5 caracteres")
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


                } else if (email.isEmpty()) {
                    editEmail.setError("Email não informado!");
                    editEmail.setFocusable(true);

                    Alerter.create(CadastroActivity.this)
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

                    Alerter.create(CadastroActivity.this)
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

                } else if (senha.length() < 6) {
                    editSenha.setError("Digite uma senha de no mínimo 6 caracteres");
                    editSenha.setFocusable(true);



                    Alerter.create(CadastroActivity.this)
                            .setTitle("Oops...")
                            .setText("Digite uma senha de no mínimo 6 caracteres")
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

                        usuario = new UsuarioModel("1", nome, email, senha, "",
                                "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true, false);
                        cadastrarUsuario(usuario);

                    } catch (Exception e) {

                        Alerter.create(CadastroActivity.this)
                                .setTitle("Oops...")
                                .setText("Erro ao cadastrar usuário: " + e.getMessage())
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
    }


    public void cadastrarUsuario(final UsuarioModel usuario) {

        mAuth = ConfiguracaoFireBase.getFirebaseAutenticacao();

        progressDialog.show();

        //Insert de Usuario
        mAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setId(idUsuario);
                    usuario.salvar();
                    atualizarToken(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
                    updateUserInfo(usuario.getNome(), mAuth.getCurrentUser());
                } else {

                    progressDialog.dismiss();

                    String excessao;

                    try {
                        throw Objects.requireNonNull(task.getException());

                    } catch (FirebaseAuthWeakPasswordException e) {
                        excessao = "Digite uma senha mais forte";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excessao = "Por favor, digite um email válido";
                    } catch (FirebaseAuthUserCollisionException e) {
                        excessao = "Esta conta já foi cadastrada!";
                    } catch (Exception e) {
                        excessao = "Erro ao cadastrar usuário " + e.getMessage();
                        e.printStackTrace();
                    }
                    Alerter.create(CadastroActivity.this)
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateUserInfo(final String nome, final FirebaseUser currentUser) {

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(nome)
                .build();

        currentUser.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            updateUIProfile();
                        }
                    }
                });
    }

    private void updateUIProfile() {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        finish();
        progressDialog.dismiss();
    }

    public void usuarioLogado() {

        mAuth = ConfiguracaoFireBase.getFirebaseAutenticacao();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }
    }

    public void iniciar() {

        progressoIniciar.setVisibility(View.GONE);
        constraintAll.setVisibility(View.VISIBLE);
        voltar.setAnimation(topAnim);

    }

    private void inicializarBancoDeDados() {

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());

        try {

            if (version > 1) {
                dataBaseAcess.onUpdate();
            } else {
                dataBaseAcess.onCreate();
            }

        } catch (Exception e) {

            Alerter.create(CadastroActivity.this)
                    .setTitle("Oops...")
                    .setText("ERRO: " + e.getMessage())
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

    public void voltar(View view) {
        finish();
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
                Alerter.create(CadastroActivity.this)
                        .setTitle("Oops...")
                        .setText("ERRO: " + e.getMessage())
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

                            Alerter.create(CadastroActivity.this)
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

                Alerter.create(CadastroActivity.this)
                        .setTitle("Oops...")
                        .setText("ERRO: " + e.getMessage())
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


    private void updateUI(FirebaseUser currentUser) {
        progressDialog.dismiss();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
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

    @Override
    protected void onStart() {
        super.onStart();
        usuarioLogado();
    }
}


