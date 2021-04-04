package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.HarpeCModel;

import java.util.List;

public class HarpeCAdapters extends RecyclerView.Adapter<HarpeCAdapters.myViewHolder> {

    private List<HarpeCModel> listHC;
    private Context context;

    public HarpeCAdapters(List<HarpeCModel> listHC, Context context) {
        this.listHC = listHC;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_verses_biblia, parent, false);
        return new myViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        HarpeCModel b = listHC.get(position);

        String texto = Html.fromHtml(b.getLyrics()).toString().trim();


        holder.txtTexto.setVisibility(View.GONE);
        //String[] linhas = texto.split("[A-Z] | [À-Ú]");

        String[] linhas = texto.split(";\\s");

        for (String linha : linhas) {

            holder.txtTexto.append(linha +"\n\n");
            Log.d("Linha: ", linha);
        }

        holder.txtTexto.setVisibility(View.VISIBLE);
        holder.txtTexto.setTextSize(16);
        //holder.txtTexto.setText(texto);


    }


    @Override
    public int getItemCount() {
        return listHC.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder {

        TextView txtTexto;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTexto = itemView.findViewById(R.id.txtTexto);
        }
    }


}
