package com.reobotetechnology.reobotegame.ui.bible.biblia_toda;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.BibleAdapters;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.model.BibliaModel;

import com.reobotetechnology.reobotegame.ui.bible.DetailsBookActivity;
import com.reobotetechnology.reobotegame.ui.bible.bible_song.SongBibleActivity;
import com.reobotetechnology.reobotegame.ui.bible.biblia_versiculos.VersiculosActivity;
import com.reobotetechnology.reobotegame.ui.home.HomeActivity;
import com.reobotetechnology.reobotegame.utils.LinearLayoutManagerWithSmoothScroller;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BibliaActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;
    private RecyclerView recyclerVersos;
    private List<BibliaModel> lista = new ArrayList<>();
    private BibleAdapters adapter;
    private BibleAdapters adapter2;
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

    private ActionMode actionMode;

    private BottomSheetDialog bottomSheetDialog;


    @SuppressLint({"WrongConstant", "ResourceType"})
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

            if (cap != 0) {
                c = cap;

            } else {
                c = 1;
            }
            assert nm_livro != null;
            livroS = "" + livro;
        }


        String toolbarTitle = nm_livro + " " + c;

        Objects.requireNonNull(getSupportActionBar()).setTitle(toolbarTitle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


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
        adapter = new BibleAdapters(lista, getApplicationContext(), 0);

        //Recyclerversos
        recyclerVersos.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(this));
        if (cap != 0) {
            recyclerVersos.smoothScrollToPosition(versiculo - 1);
        } else {
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
        spinner.setSelection(livro - 1);
        spinner2.setSelection(c - 1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int livro2 = spinner.getSelectedItemPosition();
                String nome = spinner.getSelectedItem().toString();

                try {

                    int somarLivro = livro2 + 1;
                    livroS = "" + somarLivro;
                    nm_livro = nome;
                    livro = somarLivro;

                    if (cap != 0) {
                        c = cap;

                    } else {
                        c = 1;
                    }
                    capitulos();
                    popular(livroS, "" + c);


                } catch (Exception e) {
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

                try {
                    int somarCapitulo = capituloT + 1;
                    if (cap != 0) {
                        c = cap;

                    } else {
                        c = somarCapitulo;
                    }

                    popular(livroS, "" + c);
                    spinner2.setSelection(c - 1);

                    if (contadoraAuxiliar > 1) {
                        cap = 0;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapter.setListener(new BibleAdapters.BibliaAdapterListener() {
            @Override
            public void onItemClick(int position) {

                enableActionMode(position);

            }

            @Override
            public void onItemLongClick(int position) {
                enableActionMode(position);
            }
        });


    }

    private void enableActionMode(int position) {

        if (actionMode == null)
            actionMode = startSupportActionMode(new androidx.appcompat.view.ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.menu_copy_versos, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(androidx.appcompat.view.ActionMode mode, MenuItem item) {
                    if (item.getItemId() == R.id.action_copy) {
                        String lc = nm_livro + " " + c;
                        adapter.copyVerses(lc, false);
                        mode.finish();
                        return true;
                    } else if (item.getItemId() == R.id.action_compartilhar) {
                        String lc = nm_livro + " " + c;
                        adapter.copyVerses(lc, true);
                        mode.finish();
                        return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(androidx.appcompat.view.ActionMode mode) {
                    adapter.selectedItems.clear();
                    List<BibliaModel> biblia = adapter.getBiblia();
                    for (BibliaModel bibliaModel : biblia) {
                        if (bibliaModel.isSelected())
                            bibliaModel.setSelected(false);
                    }

                    adapter.notifyDataSetChanged();
                    actionMode = null;
                }
            });

        adapter.toggleSelection(position);
        final int size = adapter.selectedItems.size();
        if (size == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(size + "");
            actionMode.invalidate();
        }
    }


    private void capitulos() {

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
        capitulos = dataBaseAcess.num(livro);

        //Atualiza numero de capitulos

        List<String> listaCapitulos = new ArrayList<>();

        for (int ii = 1; ii <= capitulos; ii++) {
            listaCapitulos.add("" + ii);
        }

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaCapitulos);
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(spinnerArrayAdapter2);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    private void popular(String livro2, String capitulo) {

        lista.clear();
        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
        List<BibliaModel> lista2 = new ArrayList<>();
        lista2 = dataBaseAcess.listarVersos(livro2, capitulo);
        lista.addAll(lista2);
        imgVoltar.setVisibility(View.GONE);
        imgNext.setVisibility(View.GONE);


        if (c == capitulos) {
            imgNext.setVisibility(View.GONE);

        } else {
            imgNext.setVisibility(View.VISIBLE);

        }

        if (c == 1) {
            imgVoltar.setVisibility(View.GONE);


        } else {
            imgVoltar.setVisibility(View.VISIBLE);

        }

        adapter.notifyDataSetChanged();
        txtCapitulo.setText(nm_livro + " " + c);
        Objects.requireNonNull(getSupportActionBar()).setTitle(nm_livro + " " + c);

        if (cap == 0) {
            recyclerVersos.smoothScrollToPosition(0);
        }


    }

    private void popularCombobox() {
        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
        List<String> listaBiblia = new ArrayList<>();
        listaBiblia = dataBaseAcess.listarLivrosNome();

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaBiblia);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }


    @SuppressLint("SetTextI18n")
    private void next() {

        lista.clear();
        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
        c = c + 1;
        assert nm_livro != null;
        txtCapitulo.setText(nm_livro + " " + c);
        List<BibliaModel> lista3 = new ArrayList<>();
        lista3 = dataBaseAcess.listarVersos(livroS, "" + c);
        lista.addAll(lista3);
        adapter.notifyDataSetChanged();
        imgVoltar.setVisibility(View.VISIBLE);
        recyclerVersos.smoothScrollToPosition(0);

        if (c == capitulos) {
            imgNext.setVisibility(View.GONE);
            spinner.setSelection(livro - 1);
            spinner2.setSelection(0);
        } else {
            imgNext.setVisibility(View.VISIBLE);
            spinner.setSelection(livro - 1);
            spinner2.setSelection(c - 1);
        }

        if (c == 1) {
            imgVoltar.setVisibility(View.GONE);
            spinner.setSelection(livro - 1);
            spinner2.setSelection(0);

        } else {
            imgVoltar.setVisibility(View.VISIBLE);
            spinner.setSelection(livro - 1);
            spinner2.setSelection(c - 1);
        }


    }

    @SuppressLint("SetTextI18n")
    private void voltar() {

        lista.clear();
        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
        c = c - 1;
        lista.clear();
        assert nm_livro != null;

        txtCapitulo.setText(nm_livro + " " + c);

        List<BibliaModel> lista4 = new ArrayList<>();
        lista4 = dataBaseAcess.listarVersos(livroS, "" + c);
        lista.addAll(lista4);
        adapter.notifyDataSetChanged();
        recyclerVersos.smoothScrollToPosition(0);

        if (c == 1) {
            imgVoltar.setVisibility(View.GONE);
            spinner.setSelection(livro - 1);
            spinner2.setSelection(0);
        } else {
            imgVoltar.setVisibility(View.VISIBLE);
            spinner.setSelection(livro - 1);
            spinner2.setSelection(c - 1);
        }

        if (c == capitulos) {
            imgNext.setVisibility(View.GONE);
            spinner.setSelection(livro - 1);
            spinner2.setSelection(0);
        } else {
            imgNext.setVisibility(View.VISIBLE);
            spinner.setSelection(livro - 1);
            spinner2.setSelection(c - 1);
        }


    }

    public void confirmation() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Prontoooo!")
                .setContentText("Este capítulo foi marcado como lido!")
                .show();
    }

    private void songBible() {
        Intent i = new Intent(getApplicationContext(), SongBibleActivity.class);
        i.putExtra("nm_book", nm_livro);
        i.putExtra("cap_book", c);
        startActivity(i);
    }

    private void deatils(){
        Intent i = new Intent(getApplicationContext(), DetailsBookActivity.class);
        i.putExtra("nm_book", nm_livro);
        startActivity(i);
    }

    @SuppressLint("SetTextI18n")
    private void openAnotattion(){

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.include_bottom_sheet_anotation, null);


        view.findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Alerter.create(BibliaActivity.this)
                        .setTitle("Obaa...")
                        .setText("Anotações salvas com sucesso!")
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

    private void openFontSettings(){
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.include_bottom_sheet_bible_font, null);

        view.findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Alerter.create(BibliaActivity.this)
                        .setTitle("Obaa...")
                        .setText("Alterações realizadas com sucesso!")
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bible, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // Finaliza a Activity atual e assim volta para a tela anterior
                break;
            case R.id.menu_song:
                //open modal or other activity
                songBible();
                break;
            case R.id.menu_text:
                //open modal or other activity
                openFontSettings();
                break;
            case R.id.menu_edit:
                //open modal or other activity
                openAnotattion();
                break;
            case R.id.menu_check:
                confirmation();
                break;
            case R.id.menu_details:
                deatils();
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