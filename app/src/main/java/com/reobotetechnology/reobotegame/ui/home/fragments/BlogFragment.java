package com.reobotetechnology.reobotegame.ui.home.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.BlogPostStoryAdapters;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.helper.ConfigurationFirebase;
import com.reobotetechnology.reobotegame.model.BlogPostModel;
import com.reobotetechnology.reobotegame.model.CommentBlogPostModel;
import com.reobotetechnology.reobotegame.model.FollowBlogPostModel;
import com.reobotetechnology.reobotegame.model.Notification;
import com.reobotetechnology.reobotegame.ui.notifications.NotificationsActivity;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class BlogFragment extends Fragment {

    //SwipeRefresh
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar3;
    private ConstraintLayout constraintPrincipal;

    //Configurações do banco de dados
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();

    //Blog Story
    private BlogPostStoryAdapters adapterStory;
    private ArrayList<BlogPostModel> listStory = new ArrayList<>();
    private ConstraintLayout layout_story;

    //Blog Story2
    private BlogPostStoryAdapters adapterStory2;
    private ArrayList<BlogPostModel> listStory2 = new ArrayList<>();
    private ConstraintLayout layout_story2;

    //Blog Story3
    private BlogPostStoryAdapters adapterStory3;
    private ArrayList<BlogPostModel> listStory3 = new ArrayList<>();
    private ConstraintLayout layout_story3;

    //Blog Story4
    private BlogPostStoryAdapters adapterStory4;
    private ArrayList<BlogPostModel> listStory4 = new ArrayList<>();
    private ConstraintLayout layout_story4;

    //Blog Story5
    private BlogPostStoryAdapters adapterStory5;
    private ArrayList<BlogPostModel> listStory5 = new ArrayList<>();
    private ConstraintLayout layout_story5;

    //Blog Story6
    private BlogPostStoryAdapters adapterStory6;
    private ArrayList<BlogPostModel> listStory6 = new ArrayList<>();
    private ConstraintLayout layout_story6;

    //Blog Story7
    private BlogPostStoryAdapters adapterStory7;
    private ArrayList<BlogPostModel> listStory7 = new ArrayList<>();
    private ConstraintLayout layout_story7;


    //Toolbar
    private CircleImageView profileImage;
    private TextView textWelcome, textDescriptionNotifications;
    private Button btn_notifications;

    //AdMob
    private AdView mAdView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_blog, container, false);

        mAdView = root.findViewById(R.id.adView);

        //configuracoes de objetos
        autenticacao = ConfigurationFirebase.getFirebaseAutenticacao();
        user = autenticacao.getCurrentUser();

        swipeRefresh = root.findViewById(R.id.swipe);

        constraintPrincipal = root.findViewById(R.id.constraintPrincipal);
        progressBar3 = root.findViewById(R.id.progressBar3);

        //Refresh
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        onStart();
                        swipeRefresh.setRefreshing(false);
                    }
                }, 2000);

            }
        });

        //Toolbar

        textWelcome = root.findViewById(R.id.textWelcome);
        textDescriptionNotifications = root.findViewById(R.id.textDescriptionNotifications);
        btn_notifications = root.findViewById(R.id.btn_notifications);
        profileImage = root.findViewById(R.id.profile);

        btn_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NotificationsActivity.class));
            }
        });


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), NotificationsActivity.class));
            }
        });


        RecyclerView recyclerStory = root.findViewById(R.id.recyclerBlogStory);
        RecyclerView recyclerStory2 = root.findViewById(R.id.recyclerBlogStory2);
        RecyclerView recyclerStory3 = root.findViewById(R.id.recyclerBlogStory3);
        RecyclerView recyclerStory4 = root.findViewById(R.id.recyclerBlogStory4);
        RecyclerView recyclerStory5 = root.findViewById(R.id.recyclerBlogStory5);
        RecyclerView recyclerStory6 = root.findViewById(R.id.recyclerBlogStory6);
        RecyclerView recyclerStory7 = root.findViewById(R.id.recyclerBlogStory7);


        //configurarAdapter

        layout_story = root.findViewById(R.id.layout_story);
        layout_story2 = root.findViewById(R.id.layout_story2);
        layout_story3 = root.findViewById(R.id.layout_story3);
        layout_story4 = root.findViewById(R.id.layout_story4);
        layout_story5 = root.findViewById(R.id.layout_story5);
        layout_story6 = root.findViewById(R.id.layout_story6);
        layout_story7 = root.findViewById(R.id.layout_story7);


        adapterStory = new BlogPostStoryAdapters(listStory, getActivity());
        adapterStory2 = new BlogPostStoryAdapters(listStory2, getActivity());
        adapterStory3 = new BlogPostStoryAdapters(listStory3, getActivity());
        adapterStory4 = new BlogPostStoryAdapters(listStory4, getActivity());
        adapterStory5 = new BlogPostStoryAdapters(listStory5, getActivity());
        adapterStory6 = new BlogPostStoryAdapters(listStory6, getActivity());
        adapterStory7 = new BlogPostStoryAdapters(listStory7, getActivity());


        //Configuraçoes de Layout do Recycler

        //RecyclerStory
        RecyclerView.LayoutManager layoutManagerMenu = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerStory.setLayoutManager(layoutManagerMenu);

        recyclerStory.setAdapter(adapterStory);

        //RecyclerStory2
        RecyclerView.LayoutManager layoutManagerMenu2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerStory2.setLayoutManager(layoutManagerMenu2);

        recyclerStory2.setAdapter(adapterStory2);

        //RecyclerStory3
        RecyclerView.LayoutManager layoutManagerMenu3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerStory3.setLayoutManager(layoutManagerMenu3);

        recyclerStory3.setAdapter(adapterStory3);

        //RecyclerStory4
        RecyclerView.LayoutManager layoutManagerMenu4 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerStory4.setLayoutManager(layoutManagerMenu4);

        recyclerStory4.setAdapter(adapterStory4);

        //RecyclerStory5
        RecyclerView.LayoutManager layoutManagerMenu5 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerStory5.setLayoutManager(layoutManagerMenu5);

        recyclerStory5.setAdapter(adapterStory5);

        //RecyclerStory6
        RecyclerView.LayoutManager layoutManagerMenu6 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerStory6.setLayoutManager(layoutManagerMenu6);

        recyclerStory6.setAdapter(adapterStory6);

        //RecyclerStory7
        RecyclerView.LayoutManager layoutManagerMenu7 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerStory7.setLayoutManager(layoutManagerMenu7);

        recyclerStory7.setAdapter(adapterStory7);


        return root;
    }

    //Usuario Logado
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void viewProfile() {

        if (autenticacao.getCurrentUser() != null) {

            try {
                if (user.getPhotoUrl() == null) {
                    Glide
                            .with(BlogFragment.this)
                            .load(R.drawable.profile)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(profileImage);


                } else {

                    Glide
                            .with(BlogFragment.this)
                            .load(user.getPhotoUrl())
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(profileImage);

                }

                textWelcome.setText(getString(R.string.blog));
                textDescriptionNotifications.setText(getString(R.string.conte_do_exclusivo_para_voc));

            } catch (Exception e) {
                Glide
                        .with(BlogFragment.this)
                        .load(R.drawable.profile)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(profileImage);

            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getAllNotifications() {

        final String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));
        firebaseRef.child("notifications").child(idUsuario).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                try {
                    int countNoitificationsView = 0;
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        Notification notification = new Notification();
                        String view = Objects.requireNonNull(dados.child("view").getValue()).toString();
                        notification.setView(Boolean.parseBoolean(view));
                        if (!notification.isView()) {
                            countNoitificationsView = countNoitificationsView + 1;
                        }
                    }

                    btn_notifications.setText("" + countNoitificationsView);

                } catch (Exception e) {
                    Log.i("ERRO NOTIFICATION", Objects.requireNonNull(e.getMessage()));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void listPostBlog() {

        constraintPrincipal.setVisibility(View.GONE);
        progressBar3.setVisibility(View.VISIBLE);

        try {

            listStory.clear();
            listStory2.clear();
            listStory3.clear();
            listStory4.clear();
            listStory5.clear();
            listStory6.clear();
            listStory7.clear();

            firebaseRef.child("blog").addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        BlogPostModel blog = dados.getValue(BlogPostModel.class);

                        assert blog != null;

                        listStory.add(blog);
                        Collections.reverse(listStory);
                        if (blog.getCategory().equals(getString(R.string.escatologia))) {
                            listStory2.add(blog);
                        }
                        if (blog.getCategory().equals(getString(R.string.cristologia))) {
                            listStory3.add(blog);
                        }
                        if (blog.getCategory().equals(getString(R.string.jovens))) {
                            listStory4.add(blog);
                        }
                        if (blog.getCategory().equals(getString(R.string.crian_as))) {
                            listStory5.add(blog);
                        }
                        if (blog.getCategory().equals(getString(R.string.mulheres))) {
                            listStory6.add(blog);
                        }
                        if (blog.getCategory().equals(getString(R.string.homens))) {
                            listStory7.add(blog);
                        }

                    }

                    if (listStory.size() == 0) {
                        layout_story.setVisibility(View.GONE);
                        layout_story2.setVisibility(View.GONE);
                        layout_story3.setVisibility(View.GONE);
                        layout_story4.setVisibility(View.GONE);
                        layout_story5.setVisibility(View.GONE);
                        layout_story6.setVisibility(View.GONE);
                        layout_story7.setVisibility(View.GONE);
                    } else {
                        if (listStory2.size() == 0) {
                            layout_story2.setVisibility(View.GONE);
                        }
                        if (listStory3.size() == 0) {
                            layout_story3.setVisibility(View.GONE);
                        }
                        if (listStory4.size() == 0) {
                            layout_story4.setVisibility(View.GONE);
                        }
                        if (listStory5.size() == 0) {
                            layout_story5.setVisibility(View.GONE);
                        }
                        if (listStory6.size() == 0) {
                            layout_story6.setVisibility(View.GONE);
                        }
                        if (listStory7.size() == 0) {
                            layout_story7.setVisibility(View.GONE);
                        }
                    }

                    adapterStory.notifyDataSetChanged();
                    adapterStory2.notifyDataSetChanged();
                    adapterStory3.notifyDataSetChanged();
                    adapterStory4.notifyDataSetChanged();
                    adapterStory5.notifyDataSetChanged();
                    adapterStory6.notifyDataSetChanged();
                    adapterStory7.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            new Handler().postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {
                    progressBar3.setVisibility(View.GONE);
                    constraintPrincipal.setVisibility(View.VISIBLE);
                }
            }, 1000);


        } catch (Exception ignored) {

        }


    }

    private void loadBannerAdMob() {
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }
        }, 1200);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        viewProfile();
        getAllNotifications();
        listPostBlog();
        loadBannerAdMob();
        super.onStart();
    }
}
