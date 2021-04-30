package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.model.BooksOfBibleModel;
import com.reobotetechnology.reobotegame.model.DescriptionBookModel;

import java.util.List;
import java.util.Random;

public class BooksOfBibleAdapters extends RecyclerView.Adapter<BooksOfBibleAdapters.myViewHolder> {


    private List<BooksOfBibleModel> biblia;
    private Context context;

    public BooksOfBibleAdapters(List<BooksOfBibleModel> listaBiblia, Context c) {
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
        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(context);

        BooksOfBibleModel b = biblia.get(position);
        BooksOfBibleModel b1 = biblia.get(0);
        holder.livro.setText(b.getNome());

        List<DescriptionBookModel> list = dataBaseAcess.listDescriptionBook(b.getId());

        //Verificar quando tiver os dois testamentos

        if (b1.getNome().equals("Gênesis")) {
            holder.image.setImageResource(R.drawable.antigo);
        } else {
            holder.image.setImageResource(R.drawable.novo_testamento);
        }

        holder.txtSigla.setText(list.get(0).getSigle());
        boolean favorited = dataBaseAcess.favorited(b.getId());
        if(!favorited) {
            holder.btnFavorited.setVisibility(View.GONE);
        }else{
            holder.btnFavorited.setImageResource(R.drawable.ic_favorite_book2_pint);
        }

        int progresso = b.getLearning();

        holder.progressBar7.setMax(100);
        holder.progressBar7.setProgress(progresso);
        holder.txtProgresso.setText(progresso + "%");

    }

    @Override
    public int getItemCount() {
        return biblia.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView livro, txtSigla, txtProgresso;
        CardView bg_livro;
        ProgressBar progressBar7;
        ImageView image;
        ImageButton btnFavorited;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            livro = itemView.findViewById(R.id.txtLivro);
            txtSigla = itemView.findViewById(R.id.txtSigla);
            bg_livro = itemView.findViewById(R.id.bg_livro);
            txtProgresso = itemView.findViewById(R.id.txtProgresso);
            progressBar7 = itemView.findViewById(R.id.progressBar7);
            image = itemView.findViewById(R.id.image);
            btnFavorited = itemView.findViewById(R.id.btnFavorited);
        }
    }


}
