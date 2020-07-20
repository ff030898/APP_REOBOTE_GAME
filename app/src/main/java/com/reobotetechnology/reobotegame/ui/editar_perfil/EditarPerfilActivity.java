package com.reobotetechnology.reobotegame.ui.editar_perfil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.home.HomeActivity;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilActivity extends AppCompatActivity {

    CircleImageView imagemPerfil;
    Button btnEditarSalvar;
    TextInputEditText txtNome, txtEmail;
    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();
    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebaseDataBase();
    private static int PReqCode = 1;
    private static int REQUESCODE = 1;
    Uri pickedImgUri = null;
    private BottomSheetDialog bottomSheetDialog;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Editar Perfil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        imagemPerfil = findViewById(R.id.imagemPerfil);

        recuperaUsuario();

        imagemPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetPickPhoto();
            }
        });

        btnEditarSalvar = findViewById(R.id.btnEditarSalvar);
        btnEditarSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarUsuario();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void atualizarUsuario() {
        if(!txtNome.getText().toString().isEmpty()) {
            updateUserInfo(txtNome.getText().toString(), pickedImgUri, autenticacao.getCurrentUser());
        }else{
            txtNome.setError("Digite o seu nome");
            txtNome.setFocusable(true);
        }

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    private void recuperaUsuario() {

        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();

        if (autenticacao.getCurrentUser() != null) {

            txtNome.setText(user.getDisplayName());
            txtEmail.setText(user.getEmail());

            try {

                if (user.getPhotoUrl() == null) {
                    Picasso.get().load(R.drawable.user).into(imagemPerfil);
                } else {
                    Picasso.get().load(user.getPhotoUrl()).into(imagemPerfil);
                }
            } catch (Exception e) {
                Picasso.get().load(R.drawable.user).into(imagemPerfil);
            }

        }
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

                            atualizarImagem(uri.toString(), nome);

                            currentUser.updateProfile(profileUpdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                // user info updated successfully
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
                                // user info updated successfully
                                String emailUsuario = Objects.requireNonNull(autenticacao.getCurrentUser()).getEmail();
                                assert emailUsuario != null;
                                String idUsuario = Base64Custom.codificarBase64(emailUsuario);
                                DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
                                usuarioRef.child("nome").setValue(nome);
                                updateUI();

                            }

                        }
                    });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void atualizarImagem(String imagem, String nome){

        String emailUsuario = Objects.requireNonNull(autenticacao.getCurrentUser()).getEmail();
        assert emailUsuario != null;
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("imagem").setValue(imagem);
        usuarioRef.child("nome").setValue(nome);
    }

    private void updateUI() {
        Toast.makeText(getApplicationContext(), "Alterações salvas com sucesso", Toast.LENGTH_SHORT).show();
        finish();
    }


    private void alert(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_salvar, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // Finaliza a Activity atual e assim volta para a tela anterior
                break;
            case R.id.salvar:
                atualizarUsuario();
                break;

            default:
                break;
        }
        return true;
    }
}
