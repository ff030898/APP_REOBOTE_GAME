package com.reobotetechnology.reobotegame.ui.blog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.CommentsAdapters;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.model.CommentModel;
import com.reobotetechnology.reobotegame.model.UserModel;
import com.reobotetechnology.reobotegame.ui.comment.AnotattionActivity;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogDetails extends AppCompatActivity {

    //Animation
    private Animation topAnim;


    private ProgressBar progressBar;
    private CoordinatorLayout constraintPrincipal;
    private ImageButton btn_back;

    //Configurações do banco de dados
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();

    //ImageBlog
    private ImageView image;
    private Button btnComment;
    private TextView txt_description;

    //ProfileFriendsVizualizations

    private CircleImageView v1, v2,v3,v4;
    private List<UserModel> listView = new ArrayList<>();

    //Comments
    private CommentsAdapters adapter;
    private List<CommentModel> list = new ArrayList<>();
    private CircleImageView imageProfile;
    private TextView txtCountComment;

    private BottomSheetDialog bottomSheetDialog;


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);

        progressBar = findViewById(R.id.progressBar);
        constraintPrincipal = findViewById(R.id.constraintPrincipal);


        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        image = findViewById(R.id.image);
        btnComment = findViewById(R.id.button6);
        txt_description = findViewById(R.id.txt_description);

        //Vem da Activity Principal
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            txt_title.setText(extras.getString("title"));
            txt_subtitle.setText(extras.getString("date")+" - "+extras.getString("time"));
            String text = extras.getString("description")+Html.fromHtml("<br><br> <b>"+extras.getString("reference")+"</b>");
            txt_description.setText(text);

            String imagePost = extras.getString("image");

            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);

            Glide.with(this)
                    .load(imagePost)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(image);

        }



        btn_back = findViewById(R.id.btn_back);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        constraintPrincipal.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        //Visualization

        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        v4 = findViewById(R.id.v4);

        TextView txt_visualizations = findViewById(R.id.txt_visualizations);
        txt_visualizations.setText(10 + " visualizações");

        //Like

        final ImageButton like = findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like.setImageResource(R.drawable.ic_amei_colorido);
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
                constraintPrincipal.setVisibility(View.VISIBLE);
                btn_back.setAnimation(topAnim);

            }
        }, 2000);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Comment

        txtCountComment = findViewById(R.id.textView44);

        RecyclerView recyclerComments = findViewById(R.id.recyclerComments);

        adapter = new CommentsAdapters(list, getApplicationContext());

        //RecyclerComment
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerComments.setLayoutManager(layoutManager);
        recyclerComments.setHasFixedSize(true);
        recyclerComments.setAdapter(adapter);

        imageProfile = findViewById(R.id.imageProfile);
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAnotattion();
            }
        });

    }


    private void getAllFriendsVisualizations(){
        firebaseRef.child("usuarios").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listView.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    try {
                        UserModel usuario2Model = dados.getValue(UserModel.class);

                        assert usuario2Model != null;
                        if (!usuario2Model.getEmail().equals(user.getEmail())) {
                            listView.add(usuario2Model);

                        }
                    } catch (Exception e) {
                        Log.i("ERRO: ", Objects.requireNonNull(e.getMessage()));
                    }

                }

                String img01 = listView.get(0).getImagem();
                String img02 = listView.get(1).getImagem();
                String img03 = listView.get(2).getImagem();
                String img04 = listView.get(3).getImagem();



                if(img01 == null){
                    Glide
                            .with(getApplicationContext())
                            .load(R.drawable.profile)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(v1);

                }else{
                    Glide
                            .with(getApplicationContext())
                            .load(listView.get(0).getImagem())
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(v1);
                }

                if(img02 == null){
                    Glide
                            .with(getApplicationContext())
                            .load(R.drawable.profile)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(v2);

                }else{
                    Glide
                            .with(getApplicationContext())
                            .load(listView.get(1).getImagem())
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(v2);
                }

                if(img03 == null){
                    Glide
                            .with(getApplicationContext())
                            .load(R.drawable.profile)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(v3);

                }else{
                    Glide
                            .with(getApplicationContext())
                            .load(listView.get(2).getImagem())
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(v3);
                }

                if(img04 == null){
                    Glide
                            .with(getApplicationContext())
                            .load(R.drawable.profile)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(v4);

                }else{
                    Glide
                            .with(getApplicationContext())
                            .load(listView.get(3).getImagem())
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(v4);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void getAllComments(){
        list.clear();

        list.add(new CommentModel("fabricio@gmail.com", "Simplesmente maravilhosooo. Que conteúdo fantástico. Ameii <3", "10/03/2021", "3", 3));
        list.add(new CommentModel("jesus@gmail.com", "Simplesmente maravilhosooo. Que conteúdo fantástico. Ameii <3", "10/03/2021", "2", 5));
        list.add(new CommentModel("bateseba@gmail.com", "Simplesmente maravilhosooo. Que conteúdo fantástico. Ameii <3", "10/03/2021", "3", 3));
        list.add(new CommentModel("elias@gmail.com", "Simplesmente maravilhosooo. Que conteúdo fantástico. Ameii <3", "10/03/2021", "7", 2));


        if (user.getPhotoUrl() == null) {
            imageProfile.setImageResource(R.drawable.profile);
        } else {

            Glide
                    .with(getApplicationContext())
                    .load(user.getPhotoUrl())
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(imageProfile);
        }

        txtCountComment.setText("("+list.size()+")");

        adapter.notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    private void openAnotattion(){

        startActivity(new Intent(getApplicationContext(), AnotattionActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllFriendsVisualizations();
        getAllComments();
    }
}
