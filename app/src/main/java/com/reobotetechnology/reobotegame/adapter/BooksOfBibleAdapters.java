package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.LivrosBibliaModel;

import java.util.List;
import java.util.Random;

public class BooksOfBibleAdapters extends RecyclerView.Adapter<BooksOfBibleAdapters.myViewHolder> {


    private List<LivrosBibliaModel> biblia;
    private Context context;

    public BooksOfBibleAdapters(List<LivrosBibliaModel> listaBiblia, Context c) {
        this.biblia = listaBiblia;
        this.context = c;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_books_bible, parent, false);
        return new myViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        LivrosBibliaModel b = biblia.get(position);
        LivrosBibliaModel b1 = biblia.get(0);
        holder.livro.setText(b.getNome());

        String[] siglaLivros = {"Gn", "Êx", "Lv", "Nm", "Dt",
                "Js", "Jz", "Rt", "1Sm", "2Sm", "1Rs", "2Rs", "1Cr",
                "2Cr", "Ed", "Ne", "Et", "Jó", "Sl", "Pv", "Ec",
                "Ct", "Is", "Jr", "Lm", "Ez", "Dn", "Os", "Jl",
                "Am", "Ob", "Jn", "Mq", "Na", "Hc", "Sf",
                "Ag", "Zc", "Ml",
        };

        String[] siglaLivrosNt = {
                "Mt", "Mc", "Lc", "Jo", "At",
                "Rm", "1Co", "2Co", "Gl", "Ef",
                "Fp", "Cl", "1Ts", "2Ts", "1Tm",
                "2Tm", "Tt", "Fm", "Hb", "Tg",
                "1Pe", "2Pe", "1Jo", "2Jo",
                "3Jo", "Jd", "Ap"};


        //int[] rainbow = context.getResources().getIntArray(R.array.rainbow);
        //int tamanho = rainbow.length;

        //int numero = new Random().nextInt(tamanho);

        //holder.bg_livro.setCardBackgroundColor(rainbow[numero]);


        if (b1.getNome().equals("Gênesis")) {
            holder.txtSigla.setText(siglaLivros[position]);
            holder.image.setImageResource(R.drawable.antigo);
        } else {
            holder.txtSigla.setText(siglaLivrosNt[position]);
            holder.image.setImageResource(R.drawable.novo_testamento);
        }

        int progresso = new Random().nextInt(100);

        holder.progressBar7.setMax(100);
        holder.progressBar7.setProgress(progresso);
        holder.txtProgresso.setText(progresso+"%");

    }

    @Override
    public int getItemCount() {
        return biblia.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView livro, txtSigla, txtProgresso;
        CardView bg_livro;
        ProgressBar progressBar7;
        ImageView image;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            livro = itemView.findViewById(R.id.txtLivro);
            txtSigla = itemView.findViewById(R.id.txtSigla);
            bg_livro = itemView.findViewById(R.id.bg_livro);
            txtProgresso = itemView.findViewById(R.id.txtProgresso);
            progressBar7 = itemView.findViewById(R.id.progressBar7);
            image = itemView.findViewById(R.id.image);
        }
    }


}
