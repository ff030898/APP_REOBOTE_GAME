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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.BlogPostModel;
import com.reobotetechnology.reobotegame.model.CommentBlogPostModel;
import com.reobotetechnology.reobotegame.model.FollowBlogPostModel;
import com.reobotetechnology.reobotegame.model.Message;
import com.reobotetechnology.reobotegame.model.Notification;
import com.reobotetechnology.reobotegame.ui.main.EditProfileActivity;
import com.tapadoo.alerter.Alerter;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    private TextInputLayout post_title, post_description, post_reference;
    private TextInputEditText editTitle, editDescription, editReference;

    private Spinner spinner;
    private String category;
    private int positionCategory;

    //Conta ID dos posts
    private int count = 0;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

        post_title = findViewById(R.id.post_title);
        editTitle = findViewById(R.id.editTitle);
        //FOCUS
        Objects.requireNonNull(post_title.getEditText()).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    validateTitle();
                }
            }
        });

        post_description = findViewById(R.id.post_description);
        editDescription = findViewById(R.id.editDescription);

        //FOCUS
        Objects.requireNonNull(post_description.getEditText()).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    validateDescription();
                }
            }
        });
        post_reference = findViewById(R.id.post_reference);
        editReference = findViewById(R.id.editReference);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                createPost();
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

        spinner = findViewById(R.id.spinner);
        listCategory();
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try {

                    positionCategory = spinner.getSelectedItemPosition();
                    category = spinner.getSelectedItem().toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void listCategory() {
        List<String> listCategory = new ArrayList<>();
        listCategory.add("Selecione uma categoria");
        listCategory.add("TODOS");
        listCategory.add(getString(R.string.escatologia));
        listCategory.add(getString(R.string.cristologia));
        listCategory.add(getString(R.string.mulheres));
        listCategory.add(getString(R.string.jovens));
        listCategory.add(getString(R.string.crian_as));
        listCategory.add(getString(R.string.homens));

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCategory);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    private boolean validateCategory() {
        boolean validate = false;

        positionCategory = spinner.getSelectedItemPosition();

        if (positionCategory != 0) {
            validate = true;
        } else {
            Alerter.create(CreatePostBlogActivity.this)
                    .setTitle("Oops...")
                    .setText("Selecione uma categoria abaixo")
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
            spinner.setFocusable(true);
        }

        return validate;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean validateTitle() {
        boolean validate = false;

        String title = Objects.requireNonNull(editTitle.getText()).toString();

        if (title.isEmpty()) {
            post_title.setError("Digite o título");
            post_title.setFocusable(true);
        } else if (title.length() < 10) {
            post_title.setError("Digite um título que seja maior");
            post_title.setFocusable(true);
        } else {
            validate = true;
            post_title.setError(null);
        }

        return validate;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean validateDescription() {
        boolean validate = false;

        String description = Objects.requireNonNull(editDescription.getText()).toString();

        if (description.isEmpty()) {
            post_description.setError("Digite a descrição do post");
            post_description.setFocusable(true);
        } else if (description.length() < 20) {
            post_description.setError("Digite uma descrição que seja maior");
            post_description.setFocusable(true);
        } else {
            validate = true;
            post_description.setError(null);

        }

        return validate;
    }

    private void showBottomSheetPickPhoto() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.include_bottom_sheet_photo_profile, null);

        view.findViewById(R.id.ln_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }
                bottomSheetDialog.dismiss();
            }
        });
        view.findViewById(R.id.ln_camera).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 22) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
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
                bottomSheetDialog = null;
            }
        });

        bottomSheetDialog.show();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                int PReqCode = 1;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        } else {
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

        } else if (resultCode == RESULT_OK && requestCode == PERMISSION_CODE && data != null) {

            if (uriImagem != null) {
                startCrop(uriImagem);
            }

        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            assert data != null;
            imageUriResultCrop = UCrop.getOutput(data);

            if (imageUriResultCrop != null) {

                image_upload.setVisibility(View.VISIBLE);
                cardView.setCardBackgroundColor(ColorStateList.valueOf(0xff000000));
                txtDescriptionUpload.setTextColor(ColorStateList.valueOf(0xffffffff));

                Glide
                        .with(getApplicationContext())
                        .load(imageUriResultCrop)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(image_upload);
            } else {
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
    }

    private void startCrop(@NonNull Uri uri) {
        String destinationFileName = "" + System.currentTimeMillis();
        destinationFileName += ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop.withAspectRatio(1, 1);

        uCrop.withMaxResultSize(450, 450);

        uCrop.withOptions(getCropOptions());

        uCrop.start(CreatePostBlogActivity.this);

    }

    private UCrop.Options getCropOptions() {
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
    private void createPost() {

        if (!validateTitle()) {
            validateTitle();
        } else if (!validateDescription()) {
            validateDescription();
        } else if (!validateCategory()) {
            validateCategory();
        } else if (imageUriResultCrop == null) {
            Alerter.create(CreatePostBlogActivity.this)
                    .setTitle("Oops...")
                    .setText("Selecione uma imagem")
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
        } else {
            pDialog.show();
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();
            String reference = editReference.getText().toString();
            createPostDb(title, description, reference, imageUriResultCrop);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPostDb(final String title, final String description, final String reference, Uri pickedImgUri) {

        if (pickedImgUri != null) {

            pDialog.show();

            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("blog_photos");
            long timestamp = System.currentTimeMillis();
            final String image_id = ""+timestamp;
            final StorageReference nome_imagem = mStorage.child(image_id + ".jpg");

            Glide.with(getApplicationContext()).asBitmap().load(pickedImgUri).apply(new RequestOptions().override(1024, 768)).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    Alerter.create(CreatePostBlogActivity.this)
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
                            if (task.isSuccessful()) {

                                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatNotification = new SimpleDateFormat("dd-MM-yyyy");
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                                Calendar cal = Calendar.getInstance();
                                Date data = new Date();
                                cal.setTime(data);
                                Date data_atual = cal.getTime();

                                String dateNotification = dateFormatNotification.format(data);
                                String time = timeFormat.format(data_atual);


                                Uri uri = task.getResult();

                                String image = uri.toString();
                                countIdPost(title, description, reference, image, dateNotification, time);


                            } else {
                                pDialog.dismiss();
                            }
                        }
                    });
                    return false;
                }
            }).submit();


        }

    }


    private void countIdPost(final String title, final String description, final String reference, final String image, final String dateNotification, final String time) {

        try {

            firebaseRef.child("blog").addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    count = (int) dataSnapshot.getChildrenCount();
                    String id = "" + (count + 1);
                    FollowBlogPostModel follow = new FollowBlogPostModel();
                    CommentBlogPostModel commentBlogPostModel = new CommentBlogPostModel();
                    BlogPostModel blog = new BlogPostModel(id, title, description, reference, image, dateNotification, time, category, follow, commentBlogPostModel, 0);
                    blog.save();

                    pDialog.dismiss();
                    Alerter.create(CreatePostBlogActivity.this)
                            .setTitle("Obaaa...")
                            .setText("Post inserido com sucesso")
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
                    }, 2150);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception ignored) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
