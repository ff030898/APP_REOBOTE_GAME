package com.reobotetechnology.reobotegame.ui.admin.blog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.tapadoo.alerter.Alerter;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CreatePostBlogActivity extends AppCompatActivity {

    //Animation
    private Animation topAnim;


    private ImageButton btn_back;

    private CardView cardView;
    private ImageView image_upload;
    private ImageButton btn_upload;
    private TextView txtDescriptionUpload;
    private Button btn_save;


    private static final int PERMISSION_CODE = 1000;
    private static int REQUESCODE = 1;
    Uri imageUriResultCrop = null;
    Uri uriImagem = null;

    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private BottomSheetDialog bottomSheetDialog;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post_blog);

        cardView = findViewById(R.id.cardView);
        image_upload = findViewById(R.id.image_upload);
        btn_upload = findViewById(R.id.btn_upload);
        txtDescriptionUpload = findViewById(R.id.txtDescriptionUpload);
        btn_save = findViewById(R.id.btn_save);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#0066cc"));
        pDialog.setTitleText("Salvando Post");
        pDialog.setCancelable(false);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        pDialog.dismiss();
                        Alerter.create(CreatePostBlogActivity.this)
                                .setTitle("Obaaa...")
                                .setText("Post inserido com sucesso")
                                .setIcon(R.drawable.ic_success)
                                .setDuration(5000)
                                .setBackgroundColorRes(R.color.colorGreen1)
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Alerter.hide();
                                    }
                                })
                                .show();

                    }
                }, 2000);

            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetPickPhoto();
            }
        });

        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        txt_title.setText(getString(R.string.create_post));
        txt_subtitle.setText(getString(R.string.description_create_post));

        btn_back = findViewById(R.id.btn_back);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                btn_back.setAnimation(topAnim);

            }
        }, 2000);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void showBottomSheetPickPhoto() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.include_bottom_sheet_photo_profile,null);

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
                        //openCamera();
                        Alerter.create(CreatePostBlogActivity.this)
                                .setTitle("Oops...")
                                .setText("Escolha uma imagem da galeria")
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

                image_upload.setVisibility(View.VISIBLE);
                cardView.setCardBackgroundColor(ColorStateList.valueOf(0xff000000));
                txtDescriptionUpload.setTextColor(ColorStateList.valueOf(0xffffffff));

                Glide
                        .with(getApplicationContext())
                        .load(imageUriResultCrop)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(image_upload);
            }else{
                Alerter.create(CreatePostBlogActivity.this)
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
            Alerter.create(CreatePostBlogActivity.this)
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

        uCrop.start(CreatePostBlogActivity.this);

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
}
