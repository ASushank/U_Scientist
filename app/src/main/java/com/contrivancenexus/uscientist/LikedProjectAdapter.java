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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LikedProjectAdapter extends RecyclerView.Adapter<LikedProjectAdapter.MyViewHolder>{
    public LikedProjectAdapter(Context context, ArrayList<LikedProject> likedProjectArrayList) {
        this.context = context;
        this.list = likedProjectArrayList;
    }

    Context context;
    ArrayList<LikedProject> list;
    int lastPosition = -1;


    @NonNull
    @Override
    public LikedProjectAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(context).inflate(R.layout.liked_project_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedProjectAdapter.MyViewHolder holder, int position) {
        LikedProject likedProject = list.get(position);
        holder.name.setText(likedProject.getName());
        holder.desc.setText(likedProject.getDesc());
        holder.viewWebsite.setOnClickListener(view -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(likedProject.getUrl()));
                    context.startActivity(intent);
        });
        holder.discussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        Glide.with(context)
                .load(likedProject.getimageurl())
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
        Button viewWebsite, discussion;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvLikedName);
            desc = itemView.findViewById(R.id.tvLikedDesc);
            img = itemView.findViewById(R.id.imgLikedProject);
            viewWebsite = itemView.findViewById(R.id.btnLikedViewWebsite);
            discussion = itemView.findViewById(R.id.btnDiscussion);
        }
    }
}
