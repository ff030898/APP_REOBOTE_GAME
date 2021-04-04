package com.reobotetechnology.reobotegame.ui.harp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.ListHarpeCAdapters;
import com.reobotetechnology.reobotegame.dao.DataBaseHCAcess;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.HarpeCModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HarpeListActivity extends AppCompatActivity {

    ListHarpeCAdapters adapter;
    List<HarpeCModel> listHarpe = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harpe_list);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Harpa Cristã");

        //COMPONENTS
        final EditText editPesquisarLivros = findViewById(R.id.editPesquisarLivros);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        //Configurando Recycler
        final RecyclerView recyclerHarpe = findViewById(R.id.recyclerHarpeList);

        editPesquisarLivros.setVisibility(View.GONE);
        recyclerHarpe.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        adapter = new ListHarpeCAdapters(listHarpe, getApplicationContext());

        //RecyclerLivros
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerHarpe.setLayoutManager(layoutManager);
        recyclerHarpe.setHasFixedSize(true);
        recyclerHarpe.setAdapter(adapter);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
                //editPesquisarLivros.setVisibility(View.VISIBLE);
                recyclerHarpe.setVisibility(View.VISIBLE);


            }
        }, 2000);

        listHC();

        //Recycler Novo
        recyclerHarpe.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerHarpe,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                //clicar
                                HarpeCModel selected = listHarpe.get(position);
                                Intent i = new Intent(getApplicationContext(), HarpeActivity.class);
                                i.putExtra("id", selected.getId());
                                i.putExtra("title", selected.getTitle());
                                startActivity(i);

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

    }

    private void listHC() {

        listHarpe.clear();

        DataBaseHCAcess dataBaseHCAcess = DataBaseHCAcess.getInstance(getApplicationContext());
            List<HarpeCModel> harpeList = dataBaseHCAcess.listHC();
        if (harpeList.size() != 0) {
            listHarpe.addAll(harpeList);
        }

        adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
