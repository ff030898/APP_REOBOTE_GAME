package com.reobotetechnology.reobotegame.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.CapitulosAdapters;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.VersiculosModel;
import com.reobotetechnology.reobotegame.utils.LinearLayoutManagerWithSmoothScroller;

import java.util.ArrayList;
import java.util.List;

public class BibliaActivity extends AppCompatActivity {

    RecyclerView recyclerVersos;
    private List<VersiculosModel> lista = new ArrayList<>();
    private CapitulosAdapters adapter;
    private CapitulosAdapters adapter2;
    int livro = 0;
    String nm_livro;

    //String l = int livro.toString()
    String livroS;

    //int c é variavel contadora do next e do voltar
    int c;

    //cap = capitulo e versiculo da palavra do dia
    int cap = 0;
    int versiculo = 0;

    //int capitulos vem do metodo do DBhelper capitulos(num)
    int capitulos;

    int contadoraAuxiliar = 0;



    private TextView txtCapitulo;

    private ImageView imgVoltar, imgNext;
    Spinner spinner, spinner2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblia);

        txtCapitulo = findViewById(R.id.txtCapitulo);
        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());

        //Vem da Activity Principal
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            livro = extras.getInt("livroSelecionado");
            nm_livro = extras.getString("nm_livro");
            cap = extras.getInt("capitulo");
            versiculo = extras.getInt("versiculo");

            if(cap != 0){
                c = cap;

            }else{
                c = 1;
            }
            assert nm_livro != null;
            livroS = "" + livro;
        }


        recyclerVersos = findViewById(R.id.recyclerVersiculos);
        imgVoltar = findViewById(R.id.imgVoltar);
        imgNext = findViewById(R.id.imgNext);


        imgVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                voltar();

            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                next();
            }
        });

        //configurarAdapter
        adapter = new CapitulosAdapters(lista, getApplicationContext());

        //RecyclerAmigos
        recyclerVersos.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(this));
        if(cap != 0){
            recyclerVersos.smoothScrollToPosition(versiculo-1);
        }else{
            recyclerVersos.smoothScrollToPosition(0);
        }
        recyclerVersos.setHasFixedSize(true);
        recyclerVersos.setAdapter(adapter);

        //Spinners
        spinner = findViewById(R.id.spinner3);
        spinner2 = findViewById(R.id.spinner4);

        capitulos();
        popular(livroS, "" + c);
        popularCombobox();
        spinner.setSelection(livro-1);
        spinner2.setSelection(c-1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int livro2 = spinner.getSelectedItemPosition();
                String nome = spinner.getSelectedItem().toString();

                try{

                    int somarLivro = livro2+1;
                    livroS = ""+somarLivro;
                    nm_livro = nome;
                    livro = somarLivro;

                    if(cap != 0){
                        c = cap;

                    }else{
                        c = 1;
                    }
                    capitulos();
                    popular(livroS, ""+c);


                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                contadoraAuxiliar = contadoraAuxiliar + 1;

                int capituloT = spinner2.getSelectedItemPosition();

                try{
                    int somarCapitulo = capituloT+1;
                    if(cap != 0){
                        c = cap;

                    }else{
                        c = somarCapitulo;
                    }

                    popular(livroS, ""+c);
                    spinner2.setSelection(c-1);

                    if(contadoraAuxiliar > 1) {
                        cap = 0;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Configurar evento de clique no recyclerview
        recyclerVersos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerVersos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                VersiculosModel verso = lista.get( position );

                                String texto = verso.getText()+"\n\n"+nm_livro+" "+c+":"+ verso.getVerso();

                                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("Versiculos", texto);
                                clipboardManager.setPrimaryClip(clipData);
                                alert("Texto copiado para area de transferência");
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                                VersiculosModel verso = lista.get( position );

                                String texto = verso.getText()+"\n\n"+nm_livro+" "+c+":"+ verso.getVerso();

                                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("Versiculos", texto);
                                clipboardManager.setPrimaryClip(clipData);
                                alert("Texto copiado para area de transferência");
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

    }


    private void capitulos(){

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
        capitulos = dataBaseAcess.num(livro);

        //Atualiza numero de capitulos

        List<String> listaCapitulos = new ArrayList<>();

        for (int ii=1; ii<=capitulos; ii++){
            listaCapitulos.add(""+ii);
        }

        ArrayAdapter<String> spinnerArrayAdapter2  = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaCapitulos);
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(spinnerArrayAdapter2);

    }

    private void popular(String livro2, String capitulo) {


        lista.clear();
        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
        List<VersiculosModel> lista2 = new ArrayList<>();
        lista2 = dataBaseAcess.listarVersos(livro2, capitulo);
        lista.addAll(lista2);
        imgVoltar.setVisibility(View.GONE);
        imgNext.setVisibility(View.GONE);


        if(c == capitulos){
            imgNext.setVisibility(View.GONE);

        }else{
            imgNext.setVisibility(View.VISIBLE);

        }

        if (c == 1) {
            imgVoltar.setVisibility(View.GONE);


        } else {
            imgVoltar.setVisibility(View.VISIBLE);

        }

        adapter.notifyDataSetChanged();
        txtCapitulo.setText(nm_livro + " " + c);

        if(cap == 0){
            recyclerVersos.smoothScrollToPosition(0);
        }


    }

    public void popularCombobox(){

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
        List<String> listaBiblia = new ArrayList<>();
        listaBiblia = dataBaseAcess.listarLivrosNome();

        ArrayAdapter<String> spinnerArrayAdapter  = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaBiblia);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);


    }

    private void next() {

        lista.clear();
        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
        c = c + 1;
        assert nm_livro != null;
        txtCapitulo.setText(nm_livro + " " + c);
        List<VersiculosModel> lista3 = new ArrayList<>();
        lista3 = dataBaseAcess.listarVersos(livroS, ""+c);
        lista.addAll(lista3);
        adapter.notifyDataSetChanged();
        imgVoltar.setVisibility(View.VISIBLE);
        recyclerVersos.smoothScrollToPosition(0);

        if(c == capitulos){
            imgNext.setVisibility(View.GONE);
            spinner.setSelection(livro-1);
            spinner2.setSelection(0);
        }else{
            imgNext.setVisibility(View.VISIBLE);
            spinner.setSelection(livro-1);
            spinner2.setSelection(c-1);
        }

        if (c == 1) {
            imgVoltar.setVisibility(View.GONE);
            spinner.setSelection(livro-1);
            spinner2.setSelection(0);

        } else {
            imgVoltar.setVisibility(View.VISIBLE);
            spinner.setSelection(livro-1);
            spinner2.setSelection(c-1);
        }




    }

    private void voltar() {

        lista.clear();
        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
        c = c - 1;
        lista.clear();
        assert nm_livro != null;

        txtCapitulo.setText(nm_livro + " " + c);

        List<VersiculosModel> lista4 = new ArrayList<>();
        lista4 = dataBaseAcess.listarVersos(livroS, ""+c);
        lista.addAll(lista4);
        adapter.notifyDataSetChanged();
        recyclerVersos.smoothScrollToPosition(0);

        if (c == 1) {
            imgVoltar.setVisibility(View.GONE);
            spinner.setSelection(livro-1);
            spinner2.setSelection(0);
        } else {
            imgVoltar.setVisibility(View.VISIBLE);
            spinner.setSelection(livro-1);
            spinner2.setSelection(c-1);
        }

        if(c == capitulos){
            imgNext.setVisibility(View.GONE);
            spinner.setSelection(livro-1);
            spinner2.setSelection(0);
        }else{
            imgNext.setVisibility(View.VISIBLE);
            spinner.setSelection(livro-1);
            spinner2.setSelection(c-1);
        }





    }

    private void alert(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }




}