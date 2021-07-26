package com.example.projectviolet;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


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
        TextView tvNumLikes;
        TextView tvNumComments;
        ImageView ivProfilePicFeed;


        ImageView ivThumbnailPlaceholder;
        ImageView ivVolumeControl;
        ProgressBar progressBar;
        FrameLayout media_container;

        ImageButton ibComments;
        LikeButton btnLike;
        LikeButton btnSave;


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
//            ivProfilePicFeed = itemView.findViewById(R.id.ivProfilePicFeed);
//            ivThumbnailPlaceholder = itemView.findViewById(R.id.ivThumbnail);
//            ibPlay = itemView.findViewById(R.id.ibPlay);

            tvNumLikes = itemView.findViewById(R.id.tvNumLikesPostFeed);
            tvNumComments = itemView.findViewById(R.id.tvNumCommentsPostFeed);
            tvCaptionFeed = itemView.findViewById(R.id.title);
            tvUsernameFeed = itemView.findViewById(R.id.tvUsernameFeed);
            media_container = itemView.findViewById(R.id.media_container);
            progressBar = itemView.findViewById(R.id.progressBar);
            ivVolumeControl = itemView.findViewById(R.id.volume_control);
            ivProfilePicFeed = itemView.findViewById(R.id.ivProfilePicFeed);
            ivThumbnailPlaceholder = itemView.findViewById(R.id.thumbnail);

            ibComments = itemView.findViewById(R.id.ibComments);
            btnLike = itemView.findViewById(R.id.likeButton);
            btnSave = itemView.findViewById(R.id.saveButton);

        }

        public void bind(Post post) {


            parent.setTag(this);
            tvCaptionFeed.setText(post.getCaption());
            tvUsernameFeed.setText(post.getUser().getUsername());

            ParseFile profileImage = post.getUserProfileImage();
            Glide.with(context).load(profileImage.getUrl()).circleCrop().into(ivProfilePicFeed);

            Glide.with(context).load(R.drawable.video_player_placeholder).centerCrop().into(ivThumbnailPlaceholder);
            // TODO: Figure out why the feed becomes so laggy after this code is implemented for thumbnails and how to fix.
//            ParseFile video = post.getVideo();
//            Bitmap thumbnailBitmap = post.getVideoThumb(video.getUrl());
//            Glide.with(context).load(thumbnailBitmap).placeholder(R.drawable.video_player_placeholder).into(ivThumbnailPlaceholder);


            ibComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentsActivity.class);
                    intent.putExtra("post", post);
                    v.getContext().startActivity(intent);

                }
            });

            ParseUser user = ParseUser.getCurrentUser();
            user.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {

                    List<String> savedPosts = user.getList("savedPosts");
                    List<String> likedByUsers = post.getList("usersThatLiked");
                    assert likedByUsers != null;
                    tvNumLikes.setText(post.getNumberOfLikes(likedByUsers));
                    //tvNumComments.setText(post.getNumberOfComments());


                    assert savedPosts != null;
                    if(savedPosts.contains(post.getObjectId())){
                        btnSave.setLiked(true);
                    }
                    else
                        btnSave.setLiked(false);

                    if(likedByUsers.contains(user.getObjectId())){
                        btnLike.setLiked(true);
                    }
                    else
                        btnLike.setLiked(false);


                    btnSave.setOnLikeListener( new OnLikeListener(  ) {
                        @Override
                        public void liked( LikeButton likeButton ) {

                            savedPosts.add(post.getObjectId());
                            //user.saveEventually();
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    user.put("savedPosts", savedPosts);
                                    Log.e(TAG, "SAVED: saved hahahahaha", e);
                                }
                            });
                        }

                        @Override
                        public void unLiked( LikeButton likeButton ) {

                            if(savedPosts.contains(post.getObjectId())){
                                int indexToRemove = savedPosts.indexOf(post.getObjectId());
                                savedPosts.remove(indexToRemove);
                            }

                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    user.put("savedPosts", savedPosts);
                                    Log.e(TAG, "UNSAVED: saved hahahahaha", e);
                                }
                            });
                        }

                    } );

                    //TODO: Implement the same tracking for saving a post
                    btnLike.setOnLikeListener( new OnLikeListener(  ) {
                        @Override
                        public void liked( LikeButton likeButton ) {
                            post.add("usersThatLiked", user.getObjectId());
                            post.saveInBackground();
                            Log.e(TAG, "Liking was successful!!");
                            int currentLikes = Integer.parseInt(tvNumLikes.getText().toString()) + 1;
                            tvNumLikes.setText(String.valueOf(currentLikes));
                        }

                        @Override
                        public void unLiked( LikeButton likeButton ) {

                            if(likedByUsers.contains(user.getObjectId())){
                                int indexToRemove = likedByUsers.indexOf(user.getObjectId());
                                likedByUsers.remove(indexToRemove);
                            }

                            post.put("usersThatLiked", likedByUsers);
                            Log.e(TAG, "UNLIKED: unliked", e);
                            post.saveInBackground();
                            int currentLikes = Integer.parseInt(tvNumLikes.getText().toString()) - 1;
                            tvNumLikes.setText(String.valueOf(currentLikes));

                        }
                    } );

                }
            });


        }

    }

}
