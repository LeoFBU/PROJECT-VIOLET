package com.example.projectviolet;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static final String TAG =  "PostsAdapter: ";
    private List<Post> posts;
    private Context context;

    public PostsAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsernameFeed;
        private TextView tvCaptionFeed;
        private TextView tvNumLikesFeed;
        private ImageView ivProfilePicFeed;
        private VideoView vvVideo;
        private ImageButton ibPlay;
        private MediaController mediaController;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvCaptionFeed = itemView.findViewById(R.id.tvCaptionFeed);
            tvUsernameFeed = itemView.findViewById(R.id.tvUsernameFeed);
            tvNumLikesFeed = itemView.findViewById(R.id.tvNumLikesFeed);
            ivProfilePicFeed = itemView.findViewById(R.id.ivProfilePicFeed);
            vvVideo = itemView.findViewById(R.id.vvVideo);
            ibPlay = itemView.findViewById(R.id.ibPlay);


        }

        public void bind(Post post) {

            tvCaptionFeed.setText(post.getCaption());
            tvUsernameFeed.setText(post.getUser().getUsername());
            tvNumLikesFeed.setText(post.getLikes());
            ParseFile profileImage = post.getUserProfileImage();
            Glide.with(context).load(profileImage.getUrl()).circleCrop().into(ivProfilePicFeed);

            ParseFile video = post.getVideo();
            vvVideo.setVideoPath(video.getUrl());
            mediaController = new MediaController(context);
            vvVideo.setMediaController(mediaController);
            mediaController.setAnchorView(vvVideo);
            vvVideo.requestFocus();
            vvVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    vvVideo.start();
                }
            });
        }
    }


}
