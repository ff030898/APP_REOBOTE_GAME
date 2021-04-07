package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.BlogPostModel;
import com.reobotetechnology.reobotegame.ui.blog.BlogDetails;

import java.util.List;

public class BlogPostStoryAdapters extends RecyclerView.Adapter<BlogPostStoryAdapters.myViewHolder> {


    private List<BlogPostModel> listBlog;
    private Context context;

    public BlogPostStoryAdapters(List<BlogPostModel> listBlog, Context c) {
        this.listBlog = listBlog;
        this.context = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_post_story, parent, false);
        return new myViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, final int position) {


        final BlogPostModel post = listBlog.get(position);

        holder.txt_title.setText("" + post.getTitle());
        holder.txt_time.setText("" + post.getTime());

        if (post.getId() == 1) {
            holder.image.setImageResource(R.drawable.reforma);
        } else if (post.getId() == 2) {
            holder.image.setImageResource(R.drawable.jardim);
        } else if (post.getId() == 3) {
            holder.image.setImageResource(R.drawable.johnwesley);
        } else if (post.getId() == 4) {
            holder.image.setImageResource(R.drawable.lutero);
        } else if (post.getId() == 5) {
            holder.image.setImageResource(R.drawable.calvino);
        } else if (post.getId() == 6) {
            holder.image.setImageResource(R.drawable.escatologia);
        } else if (post.getId() == 7) {
            holder.image.setImageResource(R.drawable.cristologia);
        } else if (post.getId() == 8) {
            holder.image.setImageResource(R.drawable.estevao);
        } else if (post.getId() == 9) {
            holder.image.setImageResource(R.drawable.timoteo);
        } else if (post.getId() == 10) {
            holder.image.setImageResource(R.drawable.infantil);
        } else if (post.getId() == 11) {
            holder.image.setImageResource(R.drawable.josias);
        } else if (post.getId() == 12) {
            holder.image.setImageResource(R.drawable.noe2);
        } else if (post.getId() == 13) {
            holder.image.setImageResource(R.drawable.abraao);
        } else if (post.getId() == 14) {
            holder.image.setImageResource(R.drawable.debora);
        } else if (post.getId() == 15) {
            holder.image.setImageResource(R.drawable.maria);
        } else if (post.getId() == 16) {
            holder.image.setImageResource(R.drawable.ester);
        } else if (post.getId() == 17) {
            holder.image.setImageResource(R.drawable.daniel);
        } else if (post.getId() == 18) {
            holder.image.setImageResource(R.drawable.feminicidio);
        } else if (post.getId() == 19) {
            holder.image.setImageResource(R.drawable.tribulacao);
        } else if (post.getId() == 20) {
            holder.image.setImageResource(R.drawable.visao_joao);
        } else if (post.getId() == 21) {
            holder.image.setImageResource(R.drawable.saulo);
        } else if (post.getId() == 22) {
            holder.image.setImageResource(R.drawable.edito);
        }

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, BlogDetails.class);
                i.putExtra("id_post", post.getId());
                i.putExtra("title", post.getTitle());
                i.putExtra("date", post.getTime());
                context.startActivity(i);
            }
        });

}

    @Override
    public int getItemCount() {
        return listBlog.size();
    }

public static class myViewHolder extends RecyclerView.ViewHolder {

    TextView txt_title, txt_time;
    ImageView image;


    public myViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_title = itemView.findViewById(R.id.txt_title);
        txt_time = itemView.findViewById(R.id.txt_time);
        image = itemView.findViewById(R.id.image);


    }
}


}
