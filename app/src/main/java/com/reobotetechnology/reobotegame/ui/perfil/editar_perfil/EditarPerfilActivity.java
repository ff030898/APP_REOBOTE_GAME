package com.reobotetechnology.reobotegame.ui.perfil.editar_perfil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.tapadoo.alerter.Alerter;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1000;
    private static int REQUESCODE = 1;
    Uri imageUriResultCrop = null;
    Uri uriImagem = null;

    CircleImageView imagemPerfil;
    Button btnEditarSalvar;
    TextInputEditText txtNome, txtEmail;
    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();
    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebaseDataBase();
    private BottomSheetDialog bottomSheetDialog;
    ProgressDialog progressDialog;
    FloatingActionButton button_profile;


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
        button_profile = findViewById(R.id.button_profile);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Atualizando Dados....");

        recuperaUsuario();

        button_profile.setOnClickListener(new View.OnClickListener() {
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

        if(!Objects.requireNonNull(txtNome.getText()).toString().isEmpty()) {
            progressDialog.show();
            updateUserInfo(txtNome.getText().toString(), imageUriResultCrop, autenticacao.getCurrentUser());
        }else{
            txtNome.setError("Digite o seu nome");
            txtNome.setFocusable(true);
        }

    }

    private void showBottomSheetPickPhoto() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_photo,null);

        view.findViewById(R.id.ln_gallery).setOnClickListener(new View.OnClickListener() {
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
        view.findViewById(R.id.ln_camera).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= 22){
                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }else {
                        openCamera();
                    }
                }
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

                    imagemPerfil.setImageResource(R.drawable.user);
                } else {

                    Glide
                            .with(getApplicationContext())
                            .load(user.getPhotoUrl())
                            .centerCrop()
                            .placeholder(R.drawable.user)
                            .into(imagemPerfil);
                }
            } catch (Exception e) {
                imagemPerfil.setImageResource(R.drawable.user);
            }

        }
    }

    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    private void openCamera(){

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NEW PICTURE");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        uriImagem = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagem);
        startActivityForResult(intent, 1000);
    }


    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                int PReqCode = 1;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        }else{
            openGallery();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            Uri pickedImgUri = data.getData();
            assert pickedImgUri != null;
            startCrop(pickedImgUri);

        }else if(resultCode == RESULT_OK && requestCode == PERMISSION_CODE && data != null){

                if(uriImagem != null) {
                    startCrop(uriImagem);
                }

        }else if(requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK){
            assert data != null;
            imageUriResultCrop = UCrop.getOutput(data);

            if(imageUriResultCrop!=null){
                Glide
                        .with(getApplicationContext())
                        .load(imageUriResultCrop)
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(imagemPerfil);
            }else{
                Alerter.create(EditarPerfilActivity.this)
                        .setTitle("Oops...")
                        .setText("Erro ao carregar imagem")
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

        }else{
            Alerter.create(EditarPerfilActivity.this)
                    .setTitle("Oops...")
                    .setText("Erro ao carregar imagem")
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

    private void startCrop(@NonNull Uri uri){
        String destinationFileName = ""+System.currentTimeMillis();
        destinationFileName +=".jpg";

        UCrop uCrop = UCrop.of(uri,Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop.withAspectRatio(1, 1);

        uCrop.withMaxResultSize(450,450);

        uCrop.withOptions(getCropOptions());

        uCrop.start(EditarPerfilActivity.this);

    }

    private UCrop.Options getCropOptions(){
        UCrop.Options options = new UCrop.Options();

        options.setCompressionQuality(70);

        //UI
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

        //Colors

        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));

        return options;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateUserInfo(final String nome, Uri pickedImgUri, final FirebaseUser currentUser) {

        if (pickedImgUri != null) {

            progressDialog.show();

            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("usuarios_photos");
            final String user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            final StorageReference nome_imagem = mStorage.child(user_id+".jpg");

            Glide.with(getApplicationContext()).asBitmap().load(pickedImgUri).apply(new RequestOptions().override(1024,768)).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    Alerter.create(EditarPerfilActivity.this)
                            .setTitle("Oops...")
                            .setText("Erro ao transformar imagem")
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

                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    resource.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    UploadTask uploadTask = nome_imagem.putStream(inputStream);

                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            return nome_imagem.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){

                                Uri uri = task.getResult();

                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .setDisplayName(nome)
                                        .build();


                                assert uri != null;
                                atualizarImagem(uri.toString(), nome);

                                currentUser.updateProfile(profileUpdate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {

                                                    progressDialog.dismiss();
                                                    Alerter.create(EditarPerfilActivity.this)
                                                            .setTitle("Obaa...")
                                                            .setText("DADOS ALTERADOS COM SUCESSO!")
                                                            .setIcon(R.drawable.ic_success)
                                                            .setDuration(2000)
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
                                                    }, 2500);

                                                }

                                            }
                                        });
                            }else{
                                progressDialog.dismiss();
                            }
                        }
                    });
                    return false;
                }
            }).submit();



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
