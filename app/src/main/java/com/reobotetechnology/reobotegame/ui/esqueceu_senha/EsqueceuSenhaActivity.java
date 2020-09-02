package com.reobotetechnology.reobotegame.ui.esqueceu_senha;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.tapadoo.alerter.Alerter;

import java.util.Objects;


public class EsqueceuSenhaActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private ProgressDialog progressDialog;
    Button btnRecuperar;
    TextInputEditText editEmail;
    ConstraintLayout constraintAll;
    ProgressBar progresso;
    ImageView voltar;

    //Animation
    Animation topAnim;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recuperando Senha....");

        editEmail = findViewById(R.id.editEmail);

        btnRecuperar = findViewById(R.id.btnRecuperar);
        progresso = findViewById(R.id.progresso);
        constraintAll = findViewById(R.id.constraintAll);
        voltar = findViewById(R.id.ic_voltar);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(editEmail.getText()).toString();

                if(email.isEmpty()){
                    editEmail.setError("Email não informado!");
                    editEmail.setFocusable(true);

                    Alerter.create(EsqueceuSenhaActivity.this)
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
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    editEmail.setError("Email inválido!");
                    editEmail.setFocusable(true);



                    Alerter.create(EsqueceuSenhaActivity.this)
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

                }else{
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
                voltar.setAnimation(topAnim);

            }
        }, 2000);
    }

    private void beginRecovery(String email){

        progressDialog.show();

        autenticacao.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    progressDialog.dismiss();

                    Alerter.create(EsqueceuSenhaActivity.this)
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

                }else{
                    progressDialog.dismiss();

                    Alerter.create(EsqueceuSenhaActivity.this)
                            .setTitle("Oops...")
                            .setText("ERRO AO RESETAR SENHA. VERIFIQUE SE O EMAIL DIGITADO ESTÁ CORRETO!")
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Alerter.create(EsqueceuSenhaActivity.this)
                        .setTitle("Oops...")
                        .setText("ERRO AO RESETAR SENHA. VERIFIQUE SE O EMAIL DIGITADO ESTÁ CORRETO!")
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
