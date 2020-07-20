package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.BibliaModel;

import java.util.ArrayList;
import java.util.List;

public class BibliaAdapters extends RecyclerView.Adapter<BibliaAdapters.myViewHolder> {

    private BibliaAdapterListener listener;
    public final SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int currentSelectedPos;
    private List<BibliaModel> lista;
    private Context context;

    public BibliaAdapters(List<BibliaModel> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    public List<BibliaModel> getBiblia() {
        return lista;
    }

    public void setListener(BibliaAdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_biblia, parent, false);
        return new myViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, final int position) {
        BibliaModel v = lista.get(position);
        holder.txtTexto.setText(v.getVerso() + ". " + v.getText());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItems.size() > 0 && listener != null)
                    listener.onItemClick(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null)
                    listener.onItemLongClick(position);
                return true;
            }
        });

        if (v.isSelected()) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            gradientDrawable.setCornerRadius(32f);
            gradientDrawable.setColor(Color.rgb(232, 240, 253));
            holder.itemView.setBackground(gradientDrawable);
        } else {
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            gradientDrawable.setCornerRadius(32f);
            gradientDrawable.setColor(Color.WHITE);
            holder.itemView.setBackground(gradientDrawable);
        }

        if (currentSelectedPos == position) currentSelectedPos = -1;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void copyVerses(String lc, boolean compartilhar) {
        List<BibliaModel> versosSelecionados = new ArrayList<>();

        for (BibliaModel bibliaModel : this.lista) {
            if (bibliaModel.isSelected())
                versosSelecionados.add(bibliaModel);
        }

        int size = versosSelecionados.size();
        StringBuilder versos = new StringBuilder();

        for (int i = 0; i < size; i++) {
            if(i == 0){
                versos.append(versosSelecionados.get(i).getVerso()).append("- ").append(versosSelecionados.get(i).getText());
            }else {
                versos.append("\n").append(versosSelecionados.get(i).getVerso()).append("- ").append(versosSelecionados.get(i).getText());
            }
        }

        int primeiroVerso = versosSelecionados.get(0).getVerso();
        int ultimoVerso = versosSelecionados.get(size-1).getVerso();
        String livro = "";
        if(size > 1) {
            livro = lc + ":" + primeiroVerso + "-" + ultimoVerso;
        }else{
            livro = lc + ":" + primeiroVerso;
        }

        String versoFinal = "";
        if(size > 1) {
            versoFinal = versos + "\n\n" + livro;
        }else{
            versoFinal = versosSelecionados.get(0).getText() + "\n\n" + livro;
        }

        if(compartilhar){
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            share.putExtra(Intent.EXTRA_TEXT,
                    versoFinal);

            Intent chooserIntent = Intent.createChooser(share, "Compartilhar");
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(chooserIntent);


        }else{
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Versiculos", versoFinal);
            assert clipboardManager != null;
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(context, "Texto copiado para area de transferência", Toast.LENGTH_LONG).show();

            notifyDataSetChanged();
            currentSelectedPos = -1;
        }

    }

    public void toggleSelection(int position) {
        currentSelectedPos = position;
        if (selectedItems.get(position)) {
            selectedItems.delete(position);
            lista.get(position).setSelected(false);
        } else {
            selectedItems.put(position, true);
            lista.get(position).setSelected(true);
        }
        notifyItemChanged(position);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {

        TextView txtTexto;

        myViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTexto = itemView.findViewById(R.id.txtTexto);
        }

    }

    public interface BibliaAdapterListener {
        void onItemClick(int position);

        void onItemLongClick(int position);
    }


}
