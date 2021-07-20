package com.example.projectviolet;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.Parse;
import com.parse.ParseFile;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolderPosts> {

    public static final String TAG =  "PostsAdapter: ";
    private ArrayList<Post> posts;
    private Context context;

    public PostsAdapter(Context context, ArrayList<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NotNull
    @Override
    public ViewHolderPosts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
        return new ViewHolderPosts(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPosts holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolderPosts extends RecyclerView.ViewHolder {

        View parent;
        TextView tvUsernameFeed;
        TextView tvCaptionFeed;
        TextView tvNumLikesFeed;
        ImageView ivProfilePicFeed;
        VideoView vvVideo;
        ImageButton ibPlay;

        ImageView ivThumbnailPlaceholder;
        ImageView ivVolumeControl;
        ProgressBar progressBar;
        FrameLayout media_container;


        // TODO: Still deciding on which implementation of a videoplayer I want to use.
        //      as of right now, Fenster is the SDK that is commented out, and I am using
        //      https://github.com/yqritc/Android-ScalableVideoView.
        //      Video functionality is not working as intended right now.
        // THIS VERSION IS HARDLY FUNCTIONAL, A BUNCH OF CODE IS HERE FOR DIFFERENT VIDEO PLAYERS.
        // THIS COMMIT IS PURELY BEFORE A LARGE IMPLEMENTATION OF EXOPLAYER CODE


        public ViewHolderPosts(@NonNull @NotNull View itemView) {
            super(itemView);

            parent = itemView;

//            tvCaptionFeed = itemView.findViewById(R.id.tvCaptionFeed);
//            tvNumLikesFeed = itemView.findViewById(R.id.tvNumLikesFeed);
//            ivProfilePicFeed = itemView.findViewById(R.id.ivProfilePicFeed);
//            ivThumbnailPlaceholder = itemView.findViewById(R.id.ivThumbnail);
//            ibPlay = itemView.findViewById(R.id.ibPlay);

            media_container = itemView.findViewById(R.id.media_container);
            tvCaptionFeed = itemView.findViewById(R.id.title);
            tvUsernameFeed = itemView.findViewById(R.id.tvUsernameFeed);
            progressBar = itemView.findViewById(R.id.progressBar);
            ivVolumeControl = itemView.findViewById(R.id.volume_control);
            ivProfilePicFeed = itemView.findViewById(R.id.ivProfilePicFeed);
            ivThumbnailPlaceholder = itemView.findViewById(R.id.thumbnail);


        }

        public void bind(Post post) {

            parent.setTag(this);

            tvCaptionFeed.setText(post.getCaption());
            tvUsernameFeed.setText(post.getUser().getUsername());
//            tvNumLikesFeed.setText(post.getLikes());

            ParseFile profileImage = post.getUserProfileImage();
            Glide.with(context).load(profileImage.getUrl()).circleCrop().into(ivProfilePicFeed);

            Glide.with(context).load(R.drawable.video_player_placeholder).centerCrop().into(ivThumbnailPlaceholder);
            // TODO: Figure out why the feed becomes so laggy after this code is implemented for thumbnails and how to fix.
//            ParseFile video = post.getVideo();
//            Bitmap thumbnailBitmap = post.getVideoThumb(video.getUrl());
//            Glide.with(context).load(thumbnailBitmap).placeholder(R.drawable.video_player_placeholder).into(ivThumbnailPlaceholder);


        }
    }


}
