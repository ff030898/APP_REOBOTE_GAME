package com.reobotetechnology.reobotegame.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.ui.cadastro.CadastroActivity;
import com.reobotetechnology.reobotegame.ui.home.HomeActivity;
import com.reobotetechnology.reobotegame.ui.main.WelcomeActivity;
import com.reobotetechnology.reobotegame.ui.esqueceu_senha.EsqueceuSenhaActivity;

public class LoginActivity extends AppCompatActivity {


    ProgressBar progresso;
    LinearLayout layout;
    Button btnEntrar, btnCadastro;
    TextInputEditText editEmail, editSenha;
    ProgressDialog progressDialog;
    ImageView imagemPerfil;
    FrameLayout frameLayout3;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        getSupportActionBar().setTitle("Tela de Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnCadastro = findViewById(R.id.btnCadastro);
        btnEntrar = findViewById(R.id.btnEntrar);

        layout = findViewById(R.id.layout);

        progresso = findViewById(R.id.progresso);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        frameLayout3 = findViewById(R.id.frameLayout3);
        imagemPerfil = findViewById(R.id.imagemPerfil);

        progresso.setVisibility(View.VISIBLE);

        layout.setVisibility(View.GONE);
        frameLayout3.setVisibility(View.GONE);
        imagemPerfil.setVisibility(View.GONE);
        btnEntrar.setVisibility(View.GONE);
        btnCadastro.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Aguarde....");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progresso.setVisibility(View.GONE);

                layout.setVisibility(View.VISIBLE);
                frameLayout3.setVisibility(View.VISIBLE);
                imagemPerfil.setVisibility(View.VISIBLE);
                btnEntrar.setVisibility(View.VISIBLE);
                btnCadastro.setVisibility(View.VISIBLE);


            }
        }, 3000);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = editEmail.getText().toString().trim();
                String senha = editSenha.getText().toString().trim();

                if(email.isEmpty()){
                    editEmail.setError("Email não informado!");
                    editEmail.setFocusable(true);
                }
               else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    //setarErro
                    editEmail.setError("Email inválido!");
                    editEmail.setFocusable(true);

                }else if(senha.isEmpty()){
                   editSenha.setError("Digite a sua senha");
                   editSenha.setFocusable(true);
               }
               else{
                    //Tudo válido
                    try {
                        login(email,senha);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "Erro ao tentar fazer login "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    public void login(String email, String senha){
        //Classe Configuração FireBase
        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();

        progressDialog.show();

        autenticacao.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                    progressDialog.dismiss();

                }else{

                    progressDialog.dismiss();
                    String excessao = "";

                    try
                    {
                        throw task.getException();

                    }
                    catch(FirebaseAuthInvalidCredentialsException e)
                    {
                        excessao = "Senha incorreta!";
                        editSenha.setFocusable(true);
                    }

                    catch(FirebaseAuthInvalidUserException e)
                    {
                        excessao = "Email não cadastrado!";
                        editEmail.setFocusable(true);
                    }


                    catch(Exception e)
                    {
                        excessao = "Erro ao fazer login " +e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, excessao, Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public void esqueceuSenha(View view) {

        startActivity(new Intent(getApplicationContext(), EsqueceuSenhaActivity.class));

    }

    public void cadastrar(View view) {
        startActivity(new Intent(getApplicationContext(), CadastroActivity.class));
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
