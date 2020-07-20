package com.reobotetechnology.reobotegame.ui.esqueceu_senha;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;

import java.util.Objects;

public class EsqueceuSenhaActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private ProgressDialog progressDialog;
    Button btnRecuperar;
    TextInputEditText editEmail;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Recuperar Senha");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recuperando Senha....");

        editEmail = findViewById(R.id.editEmail);

        btnRecuperar = findViewById(R.id.btnRecuperar);

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(editEmail.getText()).toString();

                if(email.isEmpty()){
                    editEmail.setError("Email não informado!");
                    editEmail.setFocusable(true);
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    editEmail.setError("Email inválido!");
                    editEmail.setFocusable(true);

                }else{
                    beginRecovery(email);
                }
            }
        });
    }

    private void beginRecovery(String email){

        progressDialog.show();

        autenticacao.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "EMAIL ENVIADO COM SUCESSO!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "ERRO AO RESETAR SENHA. VERIFIQUE SE O EMAIL DIGITADO ESTÁ CORRETO!", Toast.LENGTH_LONG).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "ERRO AO RESETAR SENHA. VERIFIQUE SE O EMAIL DIGITADO ESTÁ CORRETO!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
