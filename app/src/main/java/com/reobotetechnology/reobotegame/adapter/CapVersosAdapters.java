package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.CapVersosModel;

import java.util.List;

public class CapVersosAdapters extends RecyclerView.Adapter<CapVersosAdapters.myViewHolder> {

    List<CapVersosModel> lista;
    Context context;

    public CapVersosAdapters(List<CapVersosModel> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cap_versos, parent, false);
        return new myViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        CapVersosModel c = lista.get(position);
        holder.btnCapVersos.setText(""+c.getCapVerso());

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        Button btnCapVersos;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            btnCapVersos = itemView.findViewById(R.id.btnCapVersos);
        }
    }
}
