package com.example.projectviolet;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
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
import com.malmstein.fenster.controller.FensterPlayerController;
import com.malmstein.fenster.controller.FensterPlayerControllerVisibilityListener;
import com.malmstein.fenster.view.FensterVideoView;
import com.parse.ParseFile;
import com.sprylab.android.widget.TextureVideoView;
import com.yqritc.scalablevideoview.ScalableVideoView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static final String TAG =  "PostsAdapter: ";
    private List<Post> posts;
    private List<String> urls;
    private Context context;

    public PostsAdapter(Context context, List<Post> posts, List<String> urls){
        this.context = context;
        this.posts = posts;
        this.urls = urls;
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
        String url = urls.get(position);
        holder.bind(post, url);
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

        private ScalableVideoView svVideo;

        // TODO: Still deciding on which implementation of a videoplayer I want to use.
        //      as of right now, Fenster is the SDK that is commented out, and I am using
        //      https://github.com/yqritc/Android-ScalableVideoView.
        //      Video functionality is not working as intended right now.
        // THIS VERSION IS HARDLY FUNCTIONAL, A BUNCH OF CODE IS HERE FOR DIFFERENT VIDEO PLAYERS.
        // THIS COMMIT IS PURELY BEFORE A LARGE IMPLEMENTATION OF EXOPLAYER CODE

        private FensterVideoView textureVideoView;
        private FensterPlayerController playerController;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvCaptionFeed = itemView.findViewById(R.id.tvCaptionFeed);
            tvUsernameFeed = itemView.findViewById(R.id.tvUsernameFeed);
            tvNumLikesFeed = itemView.findViewById(R.id.tvNumLikesFeed);
            ivProfilePicFeed = itemView.findViewById(R.id.ivProfilePicFeed);

            //vvVideo = itemView.findViewById(R.id.vvVideo);

            svVideo = itemView.findViewById(R.id.video_view);


            ibPlay = itemView.findViewById(R.id.ibPlay);


//            textureVideoView = itemView.findViewById(R.id.play_video_texture);
//            playerController = itemView.findViewById(R.id.play_video_controller);

        }

        public void bind(Post post, String url) {
            tvCaptionFeed.setText(post.getCaption());
            tvUsernameFeed.setText(post.getUser().getUsername());
            tvNumLikesFeed.setText(post.getLikes());
            ParseFile profileImage = post.getUserProfileImage();
            Glide.with(context).load(profileImage.getUrl()).circleCrop().into(ivProfilePicFeed);

         //   ParseFile video = post.getVideo();


            //vvVideo.setVideoPath(video.getUrl());
            mediaController = new MediaController(context);
            //          vvVideo.setMediaController(mediaController);
 //           mediaController.setAnchorView(vvVideo);
//            vvVideo.requestFocus();
//            vvVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    vvVideo.start();
//                }
//            });

//
            try{
                svVideo.setDataSource(url);
                svVideo.prepareAsync(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Log.e(TAG, "onClick: CLICKED THE PLAY BUTTON");
                        svVideo.start();
                    }});

//

//                svVideo.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Log.e(TAG, "onClick: CLICKED THE PLAY BUTTON");
//                        svVideo.start();
//
//                    }
//                });

            } catch (IOException e) {
                e.printStackTrace();
            }


//            textureVideoView.setVideo(url);
//            textureVideoView.start();


        }
    }


}
