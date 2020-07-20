package com.reobotetechnology.reobotegame.ui.cadastro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.content.Intent;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.home.HomeActivity;
import com.reobotetechnology.reobotegame.ui.login.LoginActivity;
import com.reobotetechnology.reobotegame.ui.main.WelcomeActivity;

import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroActivity extends AppCompatActivity {

    ProgressBar progresso;
    LinearLayout layout;
    Button btnEntrar, btnLogin;
    TextInputEditText editNome, editEmail, editSenha;
    ProgressDialog progressDialog;
    CheckBox ckeckTermo;
    CircleImageView imagemPerfil;
    FrameLayout frameLayout3;
    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebaseDataBase();
    private FirebaseAuth autenticacao;
    private UsuarioModel usuario;

    private static int PReqCode = 1;
    private static int REQUESCODE = 1;
    Uri pickedImgUri = null;
    private BottomSheetDialog bottomSheetDialog;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Tela de Cadastro");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnLogin = findViewById(R.id.btnLogin);
        btnEntrar = findViewById(R.id.btnEntrar);

        layout = findViewById(R.id.layout);

        progresso = findViewById(R.id.progresso);
        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        ckeckTermo = findViewById(R.id.Acordo);
        frameLayout3 = findViewById(R.id.frameLayout3);
        imagemPerfil = findViewById(R.id.imagemPerfil);

        progresso.setVisibility(View.VISIBLE);

        layout.setVisibility(View.GONE);
        frameLayout3.setVisibility(View.GONE);
        imagemPerfil.setVisibility(View.GONE);
        btnEntrar.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cadastrando Usuario....");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progresso.setVisibility(View.GONE);

                layout.setVisibility(View.VISIBLE);
                frameLayout3.setVisibility(View.VISIBLE);
                imagemPerfil.setVisibility(View.VISIBLE);
                btnEntrar.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.VISIBLE);


            }
        }, 3000);

        imagemPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetPickPhoto();
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 String nome = Objects.requireNonNull(editNome.getText()).toString().trim();
                 String email = Objects.requireNonNull(editEmail.getText()).toString().trim();
                 String senha = Objects.requireNonNull(editSenha.getText()).toString().trim();

                 //validar
                if(nome.isEmpty()){
                    editNome.setError("Digite o seu nome");
                    editNome.setFocusable(true);
                }else if(nome.equals("Reobote")){
                    editNome.setError("Esse nome já está reservado pelo sistema");
                    editNome.setFocusable(true);
                }
                else if(email.isEmpty()){
                    editEmail.setError("Email não informado!");
                    editEmail.setFocusable(true);
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    //setarErro
                    editEmail.setError("Email inválido!");
                    editEmail.setFocusable(true);
                }else if(senha.length()<6){
                    editSenha.setError("Digite uma senha de no mínimo 6 caracteres");
                    editSenha.setFocusable(true);
                }else if(!ckeckTermo.isChecked()){
                   ckeckTermo.setError("Clique aqui para aceitar os termos");
                   ckeckTermo.isFocusable();

                }else{
                    //Tudo válido
                    try {

                        usuario = new UsuarioModel("1", nome, email, senha, "",
                                "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,true,false);
                        cadastrarUsuario(usuario);

                    }catch (Exception e){
                     Toast.makeText(getApplicationContext(), "Erro ao cadastrar usuário: "+e.getMessage(), Toast.LENGTH_LONG).show();
                     Log.d("INFO-CAD", Objects.requireNonNull(e.getMessage()));
                    }
                }

            }
        });
    }

    private void showBottomSheetPickPhoto() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_photo,null);

        ((View) view.findViewById(R.id.ln_gallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }else{
                    openGallery();
                }
                bottomSheetDialog.dismiss();
            }
        });
        ((View) view.findViewById(R.id.ln_camera)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"A Camera ainda não funciona. Clica em galeria",Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialog=null;
            }
        });

        bottomSheetDialog.show();
    }


    private void openGallery(){
      Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
      galleryIntent.setType("image/*");
      startActivityForResult(galleryIntent, REQUESCODE);
    }


    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
           != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                alert("Por favor aceite a permissão!");
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        }else{
            openGallery();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){
           pickedImgUri = data.getData();
            imagemPerfil.setImageURI(pickedImgUri);
        }
    }

    public void login(View view){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void cadastrarUsuario(final UsuarioModel usuario){

        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();

        progressDialog.show();

        //Insert de Usuario
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setId(idUsuario);
                    usuario.salvar();
                    atualizarToken(Objects.requireNonNull(autenticacao.getCurrentUser()).getEmail());
                    updateUserInfo(usuario.getNome(), pickedImgUri, autenticacao.getCurrentUser());
                }else{

                    progressDialog.dismiss();

                    String excessao;

                    try{
                        throw Objects.requireNonNull(task.getException());

                    }catch(FirebaseAuthWeakPasswordException e){
                        excessao = "Digite uma senha mais forte";
                    }
                    catch(FirebaseAuthInvalidCredentialsException e)
                    {
                        excessao = "Por favor, digite um email válido";
                    }
                    catch(FirebaseAuthUserCollisionException e){
                        excessao = "Esta conta já foi cadastrada!";
                    }
                    catch(Exception e) {
                        excessao = "Erro ao cadastrar usuário " +e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), excessao, Toast.LENGTH_SHORT).show();
                    Log.d("INFO-CAD2", excessao);
                }
            }

    });


}

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateUserInfo(final String nome, Uri pickedImgUri, final FirebaseUser currentUser) {

        if(pickedImgUri != null){

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("usuarios_photos");

        final StorageReference imageFilePath = mStorage.child(Objects.requireNonNull(pickedImgUri.getLastPathSegment()));
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                  @Override
                  public void onSuccess(Uri uri) {

                      UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                              .setDisplayName(nome)
                              .setPhotoUri(uri)
                              .build();

                      atualizarImagem(uri.toString());

                      currentUser.updateProfile(profileUpdate)
                              .addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {

                                      if (task.isSuccessful()) {
                                          updateUI();
                                      }

                                  }
                              });

                  }
              });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        }else{
            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();

            currentUser.updateProfile(profileUpdate)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                updateUI();

                            }

                        }
                    });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void atualizarImagem(String imagem){

        String emailUsuario = Objects.requireNonNull(autenticacao.getCurrentUser()).getEmail();
        assert emailUsuario != null;
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("imagem").setValue(imagem);
    }

    private void atualizarToken(final String email) {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                String idUsuario = Base64Custom.codificarBase64(email);
                DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
                usuarioRef.child("token").setValue(instanceIdResult.getToken());
            }
        });
    }

    private void updateUI() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
        progressDialog.dismiss();
    }


    private void alert(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
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


