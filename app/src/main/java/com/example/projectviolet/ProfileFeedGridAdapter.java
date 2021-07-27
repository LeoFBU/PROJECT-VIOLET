package com.example.projectviolet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProfileFeedGridAdapter extends RecyclerView.Adapter<ProfileFeedGridAdapter.ViewHolderProfile> {

    public static final String TAG = "ProfileFeedGridAdapter";
    ArrayList<Post> savedPosts;
    Context context;

    public ProfileFeedGridAdapter(Context context, ArrayList<Post> savedPosts){
        this.context = context;
        this.savedPosts = savedPosts;
    }


    @NotNull
    @Override
    public ProfileFeedGridAdapter.ViewHolderProfile onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profilefeedpost_layout, parent, false);
        return new ViewHolderProfile(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileFeedGridAdapter.ViewHolderProfile holder, int position) {
        Post post = savedPosts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return savedPosts.size();
    }

    public class ViewHolderProfile extends RecyclerView.ViewHolder {

        ImageView ivProfilePostThumbnail;
        ImageView ivProfilePostUserPFP;

        public ViewHolderProfile(@NonNull @NotNull View itemView) {
            super(itemView);

            ivProfilePostThumbnail = itemView.findViewById(R.id.ivProfileFeedPostThumbnail);
            ivProfilePostUserPFP = itemView.findViewById(R.id.ivProfileFeedPostUserPFP);
        }

        public void bind(Post post) {
            Glide.with(context).load(post.getThumbnail().getUrl()).centerCrop().into(ivProfilePostThumbnail);
            Glide.with(context).load(post.getUserProfileImage().getUrl()).circleCrop().into(ivProfilePostUserPFP);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentsActivity.class);
                    intent.putExtra("post", post);
                    v.getContext().startActivity(intent);
                }
            });
        }




    }
}
