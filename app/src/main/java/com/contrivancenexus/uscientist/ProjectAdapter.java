package com.contrivancenexus.uscientist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder>{
    public ProjectAdapter(Context context, ArrayList<Project> projectArrayList) {
        this.context = context;
        this.list = projectArrayList;
    }

    Context context;
    ArrayList<Project> list;
    int lastPosition = -1;


    @NonNull
    @Override
    public ProjectAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(context).inflate(R.layout.project_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.MyViewHolder holder, int position) {
        Project project = list.get(position);
        holder.name.setText(project.getName());
        holder.desc.setText(project.getDesc());
        holder.viewWebsite.setOnClickListener(view -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(project.getUrl()));
                    context.startActivity(intent);
        });
        Glide.with(context)
                .load(project.getimageurl())
                .placeholder(R.drawable.load)
                .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                .into(holder.img);
        rvAnimation(holder.itemView, position);
    }

    private void rvAnimation(View itemView, int position) {
        if(position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, desc;
        ImageView img;
        Button viewWebsite, like;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            desc = itemView.findViewById(R.id.tvDesc);
            img = itemView.findViewById(R.id.imgProject);
            viewWebsite = itemView.findViewById(R.id.btnViewWebsite);
            like = itemView.findViewById(R.id.btnLike);
        }
    }
}
