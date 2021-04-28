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
import com.reobotetechnology.reobotegame.model.HarpeCModel;

import java.util.List;

public class ListHarpeCAdapters extends RecyclerView.Adapter<ListHarpeCAdapters.myViewHolder> {

    private List<HarpeCModel> listHC;
    private Context context;

    public ListHarpeCAdapters(List<HarpeCModel> listHC, Context context) {
        this.listHC = listHC;
        this.context = context;
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

        HarpeCModel b = listHC.get(position);

        holder.btnFavorited.setVisibility(View.GONE);

        if(b.getId() < 10) {
            holder.txtSigla.setText("00" + b.getId());
        }else if(b.getId() < 100){
            holder.txtSigla.setText("0" + b.getId());
        }else{
            holder.txtSigla.setText(""+ b.getId());
        }
        holder.livro.setText(b.getTitle());
        holder.progressBar7.setVisibility(View.GONE);
        holder.image.setImageResource(R.drawable.harpa);
    }

    @Override
    public int getItemCount() {
        return listHC.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder{

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
