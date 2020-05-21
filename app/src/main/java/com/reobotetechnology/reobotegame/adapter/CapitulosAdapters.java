package com.reobotetechnology.reobotegame.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.VersiculosModel;

import java.util.List;

public class CapitulosAdapters extends RecyclerView.Adapter<CapitulosAdapters.myViewHolder> {

    private List<VersiculosModel> lista;
    private Context context;

    public CapitulosAdapters(List<VersiculosModel> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_versiculo, parent, false);
        return new myViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        VersiculosModel v = lista.get(position);
        holder.txtTexto.setText(v.getVerso()+". "+v.getText());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        TextView txtTexto;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);


            txtTexto = itemView.findViewById(R.id.txtTexto);
        }
    }


}
