package com.reobotetechnology.reobotegame.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.reobotetechnology.reobotegame.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilActivity extends AppCompatActivity {

    CircleImageView imagemPerfil;
    Button btnEditarSalvar;
    TextInputEditText txtNome, txtEmail, txtSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        getSupportActionBar().setTitle("Editar Perfil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnEditarSalvar = findViewById(R.id.btnEditarSalvar);
        btnEditarSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Alterações salvas com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_salvar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // Finaliza a Activity atual e assim volta para a tela anterior
                break;
            case R.id.salvar:
                Toast.makeText(getApplicationContext(), "Alterações salvas com sucesso", Toast.LENGTH_SHORT).show();
                finish();
                break;

            default:
                break;
        }
        return true;
    }
}
