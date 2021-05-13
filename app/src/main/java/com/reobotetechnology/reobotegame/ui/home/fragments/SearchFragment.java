package com.reobotetechnology.reobotegame.ui.home.fragments;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.CategoryAdapters;
import com.reobotetechnology.reobotegame.adapter.ThemesVersesOfBibleAdapters;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();

    private RecyclerView recyclerCategory;
    private CategoryAdapters adapter;
    private List<CategoryModel> listCategory = new ArrayList<>();

    private ProgressBar progressBar;

    private CoordinatorLayout coordinatorMain;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_search, container, false);

        progressBar = root.findViewById(R.id.progressBar3);
        coordinatorMain = root.findViewById(R.id.coordinatorMain);
        coordinatorMain.setVisibility(View.GONE);

        recyclerCategory = root.findViewById(R.id.recyclerSearch);

        adapter = new CategoryAdapters(listCategory, getActivity());

        //RecyclerThemes
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerCategory.setLayoutManager(layoutManager);
        recyclerCategory.setHasFixedSize(true);

        recyclerCategory.setAdapter(adapter);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
                coordinatorMain.setVisibility(View.VISIBLE);

            }
        }, 1000);

        //ThemesVerse
        recyclerCategory.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerCategory,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                CategoryModel categoryModel = listCategory.get(position);

                                int item = categoryModel.getId();

                                if (item == 1) {
                                    opeBible();
                                } else if (item == 2) {
                                    opeBible();
                                } else if (item == 3) {
                                    opeBible();
                                } else if (item == 4) {
                                    opeBible();
                                } else if (item == 5) {
                                    opeBible();
                                } else if (item == 6) {
                                    opeBible();
                                }

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );



        return root;
    }

    private void listCategory(){
        listCategory.clear();

        listCategory.add(new CategoryModel(1, "Bíblia"));
        listCategory.add(new CategoryModel(2, "Amigos"));
        listCategory.add(new CategoryModel(3, "Oração"));
        listCategory.add(new CategoryModel(4, "Temas"));
        listCategory.add(new CategoryModel(5, "Versos"));
        listCategory.add(new CategoryModel(6, "Jogar"));

        adapter.notifyDataSetChanged();
    }

    private void opeBible(){

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        listCategory();

    }
}
