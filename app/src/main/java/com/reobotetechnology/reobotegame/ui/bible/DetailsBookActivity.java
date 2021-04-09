package com.reobotetechnology.reobotegame.ui.bible;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.BooksOfBibleAdapters;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.BooksOfBibleModel;
import com.reobotetechnology.reobotegame.model.DescriptionBookModel;

import java.util.ArrayList;
import java.util.List;

public class DetailsBookActivity extends AppCompatActivity {


    //Animation
    private Animation topAnim;

    private ProgressBar progressBar;
    private LinearLayout linearTermes;
    private ImageButton btn_back;

    // List book favorite
    private BooksOfBibleAdapters adapterFavorites;
    private List<BooksOfBibleModel> listFavorites = new ArrayList<>();
    private int tamanho = 0;

    //Toolbar
    private TextView txt_subtitle;

    //Details
    private TextView detailsText;
    private TextView detailsTextReference;


    //AdMob
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_book);

        mAdView = findViewById(R.id.adView);

        progressBar = findViewById(R.id.progressBar);
        linearTermes = findViewById(R.id.linearTermes);
        detailsText = findViewById(R.id.detailsText);
        detailsTextReference = findViewById(R.id.detailsTextReference);


        //Include Books
        TextView txt_title_list_book = findViewById(R.id.txt_title_list_book);

        txt_title_list_book.setText(getString(R.string.book_recomendes));


        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        txt_subtitle = findViewById(R.id.txt_subtitle);



        //Vem da Activity Principal
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nm_book = extras.getString("nm_book");
            txt_title.setText(nm_book);
            bookDetails(nm_book);

        }


        btn_back = findViewById(R.id.btn_back);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        linearTermes.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
                linearTermes.setVisibility(View.VISIBLE);
                btn_back.setAnimation(topAnim);

            }
        }, 2000);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recyclerNovoTestamento = findViewById(R.id.recyclerLivrosNovo);

        adapterFavorites = new BooksOfBibleAdapters(listFavorites, getApplicationContext());

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerNovoTestamento.setLayoutManager(layoutManager2);
        recyclerNovoTestamento.setHasFixedSize(true);
        recyclerNovoTestamento.setAdapter(adapterFavorites);


        //Recycler Novo
        recyclerNovoTestamento.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerNovoTestamento,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                tamanho = listFavorites.size();

                                if (tamanho > 2) {

                                    BooksOfBibleModel livroSelecionado = listFavorites.get(position);
                                    Intent i = new Intent(getApplicationContext(), DetailsBookActivity.class);
                                    i.putExtra("nm_book", livroSelecionado.getNome());
                                    i.putExtra("livroSelecionado", livroSelecionado.getId());
                                    startActivity(i);
                                    finish();

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

        Button btn_screen = findViewById(R.id.btn_screen);

        btn_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ChaptersActivity.class);
                assert extras != null;
                String nameBook = extras.getString("nm_book");
                int id_book = extras.getInt("livroSelecionado");
                i.putExtra("nm_livro", nameBook);
                i.putExtra("livroSelecionado", id_book);
                startActivity(i);
            }
        });
    }

    private void bookDetails(String nm_book){
        try {
            DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());

            int id_book = dataBaseAcess.findBookName(nm_book);

            List<DescriptionBookModel> list;
            list = dataBaseAcess.listDescriptionBook(id_book);

            if (list.size() != 0) {
                String subtitle = getString(R.string.autor)+" "+list.get(0).getAuthor()+" em "+list.get(0).getDate();
                txt_subtitle.setText(subtitle);
                detailsText.setText(list.get(0).getDescription());
                detailsTextReference.setText(list.get(0).getReference());
            }else{
                txt_subtitle.setText(getString(R.string.leia_com_aten_o));
                detailsText.setText(getString(R.string.lorem2));
                detailsTextReference.setText(getString(R.string.reference));
            }

        }catch(Exception ignored){

        }
    }

    private void listBookFavorites() {

        listFavorites.clear();

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());

        List<BooksOfBibleModel> lista3;
        lista3 = dataBaseAcess.listarNovoTestamento();

        if (lista3.size() != 0) {
            listFavorites.addAll(lista3);
        }

        adapterFavorites.notifyDataSetChanged();

    }

    private void loadBannerAdMob(){
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }
        }, 1200);
    }

    @Override
    protected void onStart() {
        super.onStart();
        listBookFavorites();
        loadBannerAdMob();
    }
}
