package com.reobotetechnology.reobotegame.ui.bible;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.reobotetechnology.reobotegame.model.BooksOfBibleModel;
import com.reobotetechnology.reobotegame.model.CheckChaptherModel;
import com.reobotetechnology.reobotegame.model.VersesBibleModel;

import com.reobotetechnology.reobotegame.ui.home.HomeActivity;
import com.reobotetechnology.reobotegame.utils.LinearLayoutManagerWithSmoothScroller;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BiblieActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;
    private RecyclerView recyclerVersos;
    private List<VersesBibleModel> lista = new ArrayList<>();
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

    private MenuItem menu_favorite, menu_check;


    @SuppressLint({"WrongConstant", "ResourceType"})
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblia);

        txtCapitulo = findViewById(R.id.txtCapitulo);


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
                    } else if (item.getItemId() == R.id.action_color) {
                        openSelectedColors();
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(androidx.appcompat.view.ActionMode mode) {
                    adapter.selectedItems.clear();
                    List<VersesBibleModel> biblia = adapter.getBiblia();
                    for (VersesBibleModel bibliaModel : biblia) {
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
        List<VersesBibleModel> lista2 = new ArrayList<>();
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

        favoriteBook();
        checkChapther();

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
        List<VersesBibleModel> lista3 = new ArrayList<>();
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

        List<VersesBibleModel> lista4 = new ArrayList<>();
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

    private void songBible() {
        Intent i = new Intent(getApplicationContext(), SongBibleActivity.class);
        i.putExtra("nm_book", nm_livro);
        i.putExtra("cap_book", c);
        startActivity(i);
    }

    private void search() {
        startActivity(new Intent(getApplicationContext(), SearchVersesAllActivity.class));
    }

    private void openSelectedColors() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.include_bottom_sheet_bible_colors, null);

        view.findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Alerter.create(BiblieActivity.this)
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

    private boolean checkChapther() {
        try {

            boolean check = false;

            DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
            List<CheckChaptherModel> listCheck;
            listCheck = dataBaseAcess.listCheck(livro, c);

            if (listCheck.size() > 0) {
                check = true;
                menu_check.setIcon(R.drawable.ic_check_validation);
            } else {
                menu_check.setIcon(R.drawable.ic_circle_check_outline);
            }

            return check;

        } catch (Exception e) {
            return false;
        }


    }

    private void createCheckChapther() {
        try {

            DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
            List<CheckChaptherModel> listCheck = new ArrayList<>();
            listCheck.add(new CheckChaptherModel(livro, c));
            dataBaseAcess.createCheckChapther(listCheck);

            Toast.makeText(getApplicationContext(), "Capítulo marcado como lido", Toast.LENGTH_LONG).show();
            updateLearningBook();
            checkChapther();

        } catch (Exception ignored) {

        }

    }

    private void deleteCheckChapther() {
        try {
            DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
            List<CheckChaptherModel> listRemoveCheck = new ArrayList<>();
            listRemoveCheck.add(new CheckChaptherModel(livro, c));
            dataBaseAcess.dropCheckChapther(listRemoveCheck);
            Toast.makeText(getApplicationContext(), "Capítulo removido como lido", Toast.LENGTH_LONG).show();
        } catch (Exception ignored) {

        }

    }

    private void deleteAllCheckChapther() {
        try {
            new SweetAlertDialog(BiblieActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Desmarcar Tudo")
                    .setContentText("Tem certeza que deseja desmarcar todos os capitulos lidos desse livro ?")
                    .setConfirmText("Sim")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
                            dataBaseAcess.dropBookAllCheckChapther(livro);
                            Toast.makeText(getApplicationContext(), "Capítulo(s) do livro removido(s) como lido(s)", Toast.LENGTH_LONG).show();
                            checkChapther();
                            sDialog.hide();

                        }
                    }).setCancelText("Não")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            sweetAlertDialog.hide();
                        }
                    })
                    .show();

        } catch (Exception ignored) {

        }

    }

    private boolean favoriteBook() {

        try {
            boolean favorited;

            DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());

            favorited = dataBaseAcess.favorited(livro);

            if (favorited) {
                menu_favorite.setIcon(R.drawable.ic_favorite_book_pint);
            } else {
                menu_favorite.setIcon(R.drawable.ic_favorite_book);
            }

            return favorited;

        } catch (Exception ignored) {

            return false;
        }

    }

    private void updateFavoriteBook(int favorited) {

        try {

            DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());

            dataBaseAcess.updateFavoritedBook(livro, favorited);

            favoriteBook();

            if (favorited != 0) {
                Toast.makeText(getApplicationContext(), "Livro adicionado na sua lista de favoritos ", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Livro removido da sua lista de favoritos ", Toast.LENGTH_LONG).show();
            }

        } catch (Exception ignored) {

        }

    }

    private void updateLearningBook() {

        try {

            DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
            int chapthersBook = dataBaseAcess.num(livro);

            int chapthersSelecteds = dataBaseAcess.findChapthersLearningBook(livro);


            double var = ((double) chapthersSelecteds / (double) chapthersBook);
            double var2 = (var * 100);

            dataBaseAcess.updateLearningBook(livro, (int) var2);


        } catch (Exception e) {
            Log.d("Erro", "" + e.getMessage());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bible, menu);
        menu_favorite = menu.findItem(R.id.menu_favorite);
        menu_check = menu.findItem(R.id.menu_check);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_song:
                songBible();
                break;
            case R.id.menu_favorite:
                boolean favorited = favoriteBook();
                if (favorited) {
                    menu_favorite.setIcon(R.drawable.ic_favorite_book);
                    updateFavoriteBook(0);
                } else {
                    menu_favorite.setIcon(R.drawable.ic_favorite_book_pint);
                    updateFavoriteBook(1);
                }
                break;
            case R.id.menu_check:
                boolean check = checkChapther();
                if (check) {
                    menu_check.setIcon(R.drawable.ic_circle_check_outline);
                    deleteCheckChapther();
                } else {
                    menu_check.setIcon(R.drawable.ic_check_validation);
                    createCheckChapther();
                }
                break;
            case R.id.menu_delete:
                deleteAllCheckChapther();
                break;
            case R.id.menu_search:
                search();
                break;
            case R.id.menu_sair:
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}