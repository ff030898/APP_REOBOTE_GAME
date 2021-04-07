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
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.android.material.textfield.TextInputLayout;
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
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.UserModel;
import com.reobotetechnology.reobotegame.ui.home.HomeActivity;

import com.tapadoo.alerter.Alerter;


import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class CreateAccountActivity extends AppCompatActivity {

    private ConstraintLayout constraintAll;
    private static final int RC_SIGN_IN = 100;
    Button mGoogle;
    private GoogleSignInClient mGoogleSignInClient;
    SweetAlertDialog pDialog;
    Button btnCadastro;

    ProgressBar progressoIniciar;
    TextView txtLogin;


    private UserModel usuario;

    private FirebaseAuth mAuth;
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    //private static final int version = 2;

    //Animation
    Animation topAnim;

    //Token user
    String token = "";


    TextInputLayout login_nome, login_email, login_password;

    private ImageButton btn_back;


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mAuth = FirebaseAuth.getInstance();
        mGoogle = findViewById(R.id.btnGoogle);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#0066cc"));
        pDialog.setTitleText("Carregando");
        pDialog.setCancelable(false);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        txt_title.setText(getString(R.string.bem_vindo));
        txt_subtitle.setText(getString(R.string.preencha_os_dados_abaixo));

        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCadastro = findViewById(R.id.btnEntrar);
        progressoIniciar = findViewById(R.id.progressoIniciar);

        constraintAll = findViewById(R.id.constraintAll);

        login_nome = findViewById(R.id.login_nome);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);


        progressoIniciar.setVisibility(View.VISIBLE);
        constraintAll.setVisibility(View.GONE);

        txtLogin = findViewById(R.id.txtCadastrar);
        SpannableString content = new SpannableString(getString(R.string.j_tem_conta_entrar));
        content.setSpan(new UnderlineSpan(), 14, content.length(), 0);
        txtLogin.setText(content);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginAccountActivity.class));
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                iniciar();


            }
        }, 3000);


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

        //FOCUS

        Objects.requireNonNull(login_nome.getEditText()).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    String name = (Objects.requireNonNull(login_nome.getEditText()).getText()).toString().trim();

                    if (name.isEmpty()) {
                        login_nome.setError("Digite seu nome!");
                        login_nome.setFocusable(true);
                    }
                } else {
                    validateEmail();
                }
            }
        });

        Objects.requireNonNull(login_email.getEditText()).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    String email = (Objects.requireNonNull(login_email.getEditText()).getText()).toString().trim();

                    if (email.isEmpty()) {
                        login_email.setError("Digite seu email!");
                        login_email.setFocusable(true);
                    }
                } else {
                    validateEmail();
                }
            }
        });

        Objects.requireNonNull(login_password.getEditText()).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    String password = (Objects.requireNonNull(login_password.getEditText()).getText()).toString().trim();

                    if (password.isEmpty()) {
                        login_password.setError("Digite sua senha!");
                        login_password.setFocusable(true);
                    }
                } else {
                    validatePassword();
                }
            }
        });


        //VALIDATE

        Objects.requireNonNull(login_nome.getEditText()).addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                validateName();

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        Objects.requireNonNull(login_email.getEditText()).addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                validateEmail();

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        Objects.requireNonNull(login_password.getEditText()).addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                validatePassword();

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        //LOGIN COM DADOS

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateName();
                validateEmail();
                validatePassword();

                if (validateName() && validateEmail() && validatePassword()) {
                    //Tudo válido
                    try {

                        String nome = (login_nome.getEditText().getText()).toString().trim();
                        String email = (login_email.getEditText().getText()).toString().trim();
                        String senha = (login_password.getEditText().getText()).toString().trim();

                        usuario = new UserModel("1", nome, email, senha, "",
                                "", "", "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, false, false, false, false, false, false, false, false);
                        cadastrarUsuario(usuario);



                    } catch (Exception e) {

                        Alerter.create(CreateAccountActivity.this)
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean validateName() {

        String nome = (Objects.requireNonNull(login_nome.getEditText()).getText()).toString().trim();

        if (nome.isEmpty()) {

            login_nome.setError("Digite o seu nome");
            login_nome.setFocusable(true);
            return false;

        } else if (nome.equals("Reobote") || nome.equals("Amigo") || nome.equals(getString(R.string.name_robot)) || nome.equals(getString(R.string.app_name))) {
            login_nome.setError("Esse nome já está reservado pelo sistema");
            login_nome.setFocusable(true);
            return false;
        } else if (nome.length() < 3) {

            login_nome.setError("O nome deve ter no mínimo 3 caracteres");
            login_nome.setFocusable(true);
            return false;


        } else {
            login_nome.setError(null);
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean validateEmail() {

        String email = (Objects.requireNonNull(login_email.getEditText()).getText()).toString().trim();

        if (email.isEmpty()) {
            login_email.setError("Email não informado!");
            login_email.setFocusable(true);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //setarErro
            login_email.setError("Email inválido!");
            login_email.setFocusable(true);
            return false;

        } else {
            login_email.setError(null);
            return true;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean validatePassword() {

        String password = (Objects.requireNonNull(login_password.getEditText()).getText()).toString().trim();

        if (password.length() < 6) {
            login_password.setError("Digite uma senha de no mínimo 6 caracteres");
            login_password.setFocusable(true);
            return false;

        } else {
            login_password.setError(null);
            return true;
        }
    }

    public void cadastrarUsuario(final UserModel usuario) {

        mAuth = ConfigurationFireBase.getFirebaseAutenticacao();


        pDialog.show();


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

                    pDialog.dismiss();

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
                    Alerter.create(CreateAccountActivity.this)
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
        startActivity(new Intent(getApplicationContext(), CreateImageAccountActivity.class));
        finish();
        pDialog.dismiss();
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
        constraintAll.setVisibility(View.VISIBLE);
        btn_back.setAnimation(topAnim);

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
                Alerter.create(CreateAccountActivity.this)
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


                                UserModel usuario2Model = new UserModel(idUsuario, user.getDisplayName(), email, "", imagem,
                                        "", "", "", 0, 0, 0, 0,
                                        0, 0, 0, 0, 0,
                                        0, false, false, false, false, false,
                                        false, false, false, false);


                                usuario2Model.salvar();
                                atualizarToken(email);
                            }
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.

                            Alerter.create(CreateAccountActivity.this)
                                    .setTitle("Oops...")
                                    .setText("LOGIN COM O GOOGLE SÓ VAI FUNCIONAR QUANDO O APP ESTIVER PRONTO. CLIQUE EM CONTINUAR COM EMAIL E SE CADASTRE: " + task.getException())
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

                            updateUI();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Alerter.create(CreateAccountActivity.this)
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

    private void updateUI() {
        pDialog.dismiss();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    private void atualizarToken(final String email) {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                token = instanceIdResult.getToken();
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


