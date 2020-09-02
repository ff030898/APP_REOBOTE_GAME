package com.reobotetechnology.reobotegame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.CategoriaModel;


import java.util.List;
import java.util.Random;

public class CategoriasAdapters extends RecyclerView.Adapter<CategoriasAdapters.myViewHolder> {

    private List<CategoriaModel> categoria;
    private Context context;

    public CategoriasAdapters(List<CategoriaModel> listaCategoria, Context context) {
        this.categoria = listaCategoria;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_categoria, parent, false);
        return new CategoriasAdapters.myViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        CategoriaModel cat = categoria.get(position);
        holder.txtCategoria.setText(cat.getNome());

        int[] rainbow = context.getResources().getIntArray(R.array.rainbow);
        int tamanho = rainbow.length;

        int numero = new Random().nextInt(tamanho);

        holder.bg_categoria.setCardBackgroundColor(rainbow[numero]);
    }

    @Override
    public int getItemCount() {
        return categoria.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder {

        TextView txtCategoria;
        CardView bg_categoria;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            bg_categoria = itemView.findViewById(R.id.bg_categoria);
        }
    }
}
