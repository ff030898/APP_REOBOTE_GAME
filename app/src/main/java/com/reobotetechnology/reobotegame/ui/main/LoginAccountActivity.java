package com.reobotetechnology.reobotegame.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.view.MenuItem;
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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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


public class LoginAccountActivity extends AppCompatActivity {


    ProgressBar progresso;
    Button btnEntrar;
    SweetAlertDialog pDialog;
    TextView txtCadastrar;
    ConstraintLayout constraintAll;

    //Animation
    Animation topAnim;

    private static final int RC_SIGN_IN = 100;
    Button mGoogle;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();

    String token = "";

    TextInputLayout login_email, login_password;

    private ImageButton btn_back;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mGoogle = findViewById(R.id.btnGoogle);

        constraintAll = findViewById(R.id.constraintAll);
        btnEntrar = findViewById(R.id.btnEntrar);
        txtCadastrar = findViewById(R.id.txtCadastrar);

        SpannableString content = new SpannableString(getString(R.string.não_tem_conta));
        content.setSpan(new UnderlineSpan(), 15, content.length(), 0);
        txtCadastrar.setText(content);

        progresso = findViewById(R.id.progresso);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);

        progresso.setVisibility(View.VISIBLE);
        constraintAll.setVisibility(View.GONE);

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

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progresso.setVisibility(View.GONE);
                constraintAll.setVisibility(View.VISIBLE);
                btn_back.setAnimation(topAnim);

            }
        }, 2000);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                }else{
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
                }else{
                    validatePassword();
                }
            }
        });

        //VALIDATE

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

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = (login_email.getEditText().getText()).toString().trim();
                String senha = (login_password.getEditText().getText()).toString().trim();


                validateEmail();
                validatePassword();

                if (validateEmail() && validatePassword()) {
                    //Tudo válido
                    try {
                        login(email, senha);
                    } catch (Exception e) {

                        Alerter.create(LoginAccountActivity.this)
                                .setTitle("Oops...")
                                .setText("Erro ao tentar fazer login: " + e.getMessage())
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

        if (password.isEmpty()) {
            login_password.setError("Digite uma senha de no mínimo 6 caracteres");
            login_password.setFocusable(true);
            return false;

        } else {
            login_password.setError(null);
            return true;
        }
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
                Alerter.create(LoginAccountActivity.this)
                        .setTitle("Oops...")
                        .setText("ERRO AO TENTAR ENTRAR COM CONTA DA GOGOLE: " + e.getMessage())
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
                                        0, 0, 0, 0, 0, 0,  false, false, false, false, false,
                                        false, false, false, false);

                                usuario2Model.salvar();
                                atualizarToken(email);
                            }
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Alerter.create(LoginAccountActivity.this)
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

                            updateUI(null);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Alerter.create(LoginAccountActivity.this)
                        .setTitle("Oops...")
                        .setText("ERRO AO TENTAR ENTRAR COM CONTA DA GOGOLE: " + e)
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
                String idUsuario = Base64Custom.codificarBase64(email);
                DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
                usuarioRef.child("token").setValue(token);
            }
        });
    }

    private void updateUI(FirebaseUser currentUser) {
        pDialog.dismiss();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    public void login(String email, String senha) {
        //Classe Configuração FireBase
        FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();

        pDialog.show();

        autenticacao.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                    pDialog.dismiss();

                } else {

                    pDialog.dismiss();
                    String excessao = "";

                    try {
                        throw Objects.requireNonNull(task.getException());

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        login_password.setFocusable(true);
                        login_password.setError("Senha incorreta!");
                        login_password.setBoxStrokeColor(Color.parseColor("#0066cc"));
                    } catch (FirebaseAuthInvalidUserException e) {

                        login_email.setFocusable(true);
                        login_email.setError("Email não cadastrado!");
                    } catch (Exception e) {
                        excessao = "Erro ao fazer login " + e.getMessage();
                        Alerter.create(LoginAccountActivity.this)
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
                        e.printStackTrace();
                    }

                }
            }

        });
    }

    public void esqueceuSenha(View view) {
        startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
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
