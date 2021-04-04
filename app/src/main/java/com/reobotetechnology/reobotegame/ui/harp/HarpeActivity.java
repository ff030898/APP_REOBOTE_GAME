package com.reobotetechnology.reobotegame.ui.harp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.HarpeCAdapters;
import com.reobotetechnology.reobotegame.dao.DataBaseHCAcess;
import com.reobotetechnology.reobotegame.model.HarpeCModel;
import com.reobotetechnology.reobotegame.ui.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HarpeActivity extends AppCompatActivity {

    private RecyclerView recyclerHarpe;
    private TextView titleHarpe;
    private List<HarpeCModel> listHarpe = new ArrayList<>();
    private HarpeCAdapters adapter;

    String title;
    int id;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harpe);

        recyclerHarpe = findViewById(R.id.recyclerHarpe);
        titleHarpe = findViewById(R.id.titleHarpe);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            id = extras.getInt("id");
            title = extras.getString("title");
            titleHarpe.setText(title);
            Objects.requireNonNull(getSupportActionBar()).setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        adapter = new HarpeCAdapters(listHarpe, getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerHarpe.setLayoutManager(layoutManager);
        recyclerHarpe.setHasFixedSize(true);
        recyclerHarpe.setAdapter(adapter);

        listHarpe(id);

    }

    private void listHarpe(int num){
        listHarpe.clear();

        DataBaseHCAcess dataBaseHCAcess = DataBaseHCAcess.getInstance(getApplicationContext());
        List<HarpeCModel> list = new ArrayList<>();
        list = dataBaseHCAcess.listHCID(num);
        listHarpe.addAll(list);
        adapter.notifyDataSetChanged();

        //Toast.makeText(getApplicationContext(), "fui: "+list.get(0).getLyrics(), Toast.LENGTH_LONG).show();
    }

    public void confirmation(){
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Prontoooo!")
                .setContentText("Este capítulo foi marcado como lido!")
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hc, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // Finaliza a Activity atual e assim volta para a tela anterior
                break;
            case R.id.menu_text:
                //open modal or other activity
                confirmation();
                break;

            case R.id.menu_search:
                //open modal or other activity
                confirmation();
                break;

            case R.id.menu_sair:
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
                break;

            default:
                break;
        }
        return true;
    }
}
