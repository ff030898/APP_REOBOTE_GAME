package com.reobotetechnology.reobotegame.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.tapadoo.alerter.Alerter;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private SweetAlertDialog pDialog;
    Button btnRecuperar;
    ConstraintLayout constraintAll;
    ProgressBar progresso;


    //Animation
    Animation topAnim;

    TextInputLayout login_email;

    private ImageButton btn_back;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#0066cc"));
        pDialog.setTitleText("Carregando");
        pDialog.setCancelable(false);

        login_email = findViewById(R.id.login_email);

        btnRecuperar = findViewById(R.id.btnRecuperar);
        progresso = findViewById(R.id.progresso);
        constraintAll = findViewById(R.id.constraintAll);


        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        txt_title.setText(getString(R.string.recuperar_senha2));
        txt_subtitle.setText(getString(R.string.texto_email));

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //FOCUS
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

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(login_email.getEditText().getText()).toString();

                if (validateEmail()) {
                    login_email.setError(null);
                    beginRecovery(email);
                }
            }
        });

        progresso.setVisibility(View.VISIBLE);
        constraintAll.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progresso.setVisibility(View.GONE);
                constraintAll.setVisibility(View.VISIBLE);
                btn_back.setAnimation(topAnim);

            }
        }, 2000);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean validateEmail() {

        String email = (Objects.requireNonNull(login_email.getEditText()).getText()).toString().trim();

        if (email.isEmpty()) {
            login_email.setError("Email não informado!");
            //login_email.setErrorTextColor(ColorStateList.valueOf(Color.parseColor("#FB7181")));
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

    private void beginRecovery(String email) {

        pDialog.show();

        autenticacao.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    pDialog.dismiss();

                    Alerter.create(ForgotPasswordActivity.this)
                            .setTitle("Obaa...")
                            .setText("EMAIL ENVIADO COM SUCESSO!")
                            .setIcon(R.drawable.ic_success)
                            .setDuration(3000)
                            .setBackgroundColorRes(R.color.colorGreen1)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Alerter.hide();
                                }
                            })
                            .show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 4000);

                } else {
                    pDialog.dismiss();

                    login_email.setError("Email digitado não está cadastrado. Verifique se você digitou corretamente!");
                    login_email.setFocusable(true);

                }

            }
        });
    }

    public void voltar(View view) {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
