package com.reobotetechnology.reobotegame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.BibliaModel;

import java.util.List;

public class BibliaAdapters extends RecyclerView.Adapter<BibliaAdapters.myViewHolder> {

    private List<BibliaModel> biblia;
    private Context context;

    public BibliaAdapters(List<BibliaModel> listaBiblia, Context c) {

        this.biblia = listaBiblia;
        this.context = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_biblia, parent, false);
        return new myViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
       BibliaModel b = biblia.get(position);
       holder.livro.setText(b.getNome());
    }

    @Override
    public int getItemCount() {
        return biblia.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        TextView livro;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            livro = itemView.findViewById(R.id.txtLivro);
        }
    }
}
