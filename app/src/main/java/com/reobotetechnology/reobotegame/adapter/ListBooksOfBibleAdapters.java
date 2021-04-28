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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.model.BooksOfBibleModel;
import com.reobotetechnology.reobotegame.model.DescriptionBookModel;

import java.util.List;

public class ListBooksOfBibleAdapters extends RecyclerView.Adapter<ListBooksOfBibleAdapters.myViewHolder> {


    private List<BooksOfBibleModel> biblia;
    private Context context;

    public ListBooksOfBibleAdapters(List<BooksOfBibleModel> listaBiblia, Context c) {
        this.biblia = listaBiblia;
        this.context = c;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_books_bible, parent, false);
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

        holder.txtDescription.setText(list.get(0).getDescription());

        holder.txtSigla.setText(list.get(0).getSigle());
        if(list.get(0).getFavorited() == 0) {
            holder.btnFavorited.setImageResource(R.drawable.ic_favorited_book);
            holder.btnFavorited.setVisibility(View.GONE);
        }else{
            holder.btnFavorited.setImageResource(R.drawable.ic_favorite_book2_pint);
        }

        int progresso = b.getLearning();

        holder.progressBar.setMax(100);
        holder.progressBar.setProgress(progresso);
        holder.txtProgresso.setText(progresso + "%");

    }

    @Override
    public int getItemCount() {
        return biblia.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView livro, txtSigla, txtProgresso, txtDescription;
        CardView bg_livro;
        ProgressBar progressBar;
        ImageView image;
        ImageButton btnFavorited;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            livro = itemView.findViewById(R.id.txtLivro);
            txtSigla = itemView.findViewById(R.id.txtSigla);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            bg_livro = itemView.findViewById(R.id.bg_livro);
            txtProgresso = itemView.findViewById(R.id.txtProgresso);
            progressBar = itemView.findViewById(R.id.progressBar);
            image = itemView.findViewById(R.id.image);
            btnFavorited = itemView.findViewById(R.id.btnFavorited);
        }
    }


}
