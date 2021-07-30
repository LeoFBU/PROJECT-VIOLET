package com.example.projectviolet.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectviolet.CommentsActivity;
import com.example.projectviolet.models.Post;
import com.example.projectviolet.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProfileFeedGridAdapter extends RecyclerView.Adapter<ProfileFeedGridAdapter.ViewHolderProfile> {

    public static final String TAG = "ProfileFeedGridAdapter";
    ArrayList<Post> savedPostsList;
    Context context;

    public ProfileFeedGridAdapter(Context context, ArrayList<Post> savedPostsList){
        this.context = context;
        this.savedPostsList = savedPostsList;
    }

    @NotNull
    @Override
    public ProfileFeedGridAdapter.ViewHolderProfile onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profilefeedpost_layout, parent, false);
        return new ViewHolderProfile(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileFeedGridAdapter.ViewHolderProfile holder, int position) {
        Post post = savedPostsList.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return savedPostsList.size();
    }

    public class ViewHolderProfile extends RecyclerView.ViewHolder {

        ImageView ivProfFeedPostThumbnail;
        ImageView ivProfPostUserProfilePic;

        public ViewHolderProfile(@NonNull @NotNull View itemView) {
            super(itemView);

            ivProfFeedPostThumbnail = itemView.findViewById(R.id.ivProfileFeedPostThumbnail);
            ivProfPostUserProfilePic = itemView.findViewById(R.id.ivProfileFeedPostUserPFP);
        }

        public void bind(Post post) {

            Glide.with(context).load(post.getThumbnail()
                    .getUrl())
                    .centerCrop()
                    .into(ivProfFeedPostThumbnail);
            Glide.with(context).load(post.getUserProfileImage()
                    .getUrl())
                    .circleCrop()
                    .into(ivProfPostUserProfilePic);

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
