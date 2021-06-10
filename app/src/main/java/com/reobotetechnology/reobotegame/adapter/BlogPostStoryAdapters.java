package com.reobotetechnology.reobotegame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.model.BlogPostModel;
import com.reobotetechnology.reobotegame.ui.blog.BlogDetails;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BlogPostStoryAdapters extends RecyclerView.Adapter<BlogPostStoryAdapters.myViewHolder> {

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dateFormatNotification = new SimpleDateFormat("dd-MM-yyyy");

    private List<BlogPostModel> listBlog;
    private Context context;
    private boolean follow = false;

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
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {

        final BlogPostModel post = listBlog.get(position);

        String dateText = post.getDate().replace("-", "/");


        holder.txt_title.setText("" + post.getTitle());
        Calendar cal = Calendar.getInstance();
        Date data = new Date();
        cal.setTime(data);

        String dateNotification = dateFormatNotification.format(data);

        String[] dateSplit = dateNotification.split("-");

        int yearDateSplit = Integer.parseInt(dateSplit[2]);
        int monthDateSplit = Integer.parseInt(dateSplit[1]);
        int dayDateSplit = Integer.parseInt(dateSplit[0]);

        String[] dateDataBaseSplit = post.getDate().split("-");

        int yearDataBaseSplit = Integer.parseInt(dateDataBaseSplit[2]);
        int monthDataBaseSplit = Integer.parseInt(dateDataBaseSplit[1]);
        int dayDataBaseSplit = Integer.parseInt(dateDataBaseSplit[0]);

        if(yearDateSplit == yearDataBaseSplit){
            if(monthDateSplit == monthDataBaseSplit){
                int dateDifferent = (dayDateSplit - dayDataBaseSplit);
                if(dateDifferent == 0){
                    dateText = "Hoje ás "+post.getTime();
                }else if(dateDifferent == 1){
                    dateText = "Ontem ás "+post.getTime();
                }else {
                    dateText = "Há "+dateDifferent+" dias";
                }
            }
        }


        holder.txt_time.setText(dateText);
        holder.btnFavorited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(follow) {
                    holder.btnFavorited.setImageResource(R.drawable.ic_favorited_book);
                    follow = false;
                }else{
                    holder.btnFavorited.setImageResource(R.drawable.ic_favorite_book2_pint);
                    follow = true;
                }
            }
        });


        Glide.with(context)
                .load(post.getImage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.image);

        /*Glide
                .with(context)
                .load(post.getImage())
                .centerCrop()
                .placeholder(R.drawable.loading)
                .into(holder.image);*/

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, BlogDetails.class);
                i.putExtra("id", post.getId());
                i.putExtra("image", post.getImage());
                i.putExtra("title", post.getTitle());
                i.putExtra("description", post.getDescription());
                i.putExtra("reference", post.getReference());
                i.putExtra("date", post.getDate().replace("-", "/"));
                i.putExtra("time", post.getTime());
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
    ImageButton btnFavorited;
    ProgressBar progress;


    public myViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_title = itemView.findViewById(R.id.txt_title);
        txt_time = itemView.findViewById(R.id.txt_time);
        image = itemView.findViewById(R.id.image);
        btnFavorited = itemView.findViewById(R.id.btnFavorited);
        progress = itemView.findViewById(R.id.progress);


    }
}


}
