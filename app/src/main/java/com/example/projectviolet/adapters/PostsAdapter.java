package com.example.projectviolet.adapters;

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
import com.example.projectviolet.CommentsActivity;
import com.example.projectviolet.OtherUserProfileActivity;
import com.example.projectviolet.models.Post;
import com.example.projectviolet.R;
import com.example.projectviolet.util.OnSwipeTouchListener;
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

    public static final String TAG =  "PostsAdapter";
    private ArrayList<Post> postList;
    private Context context;

    public PostsAdapter(Context context, ArrayList<Post> postList){
        this.context = context;
        this.postList = postList;
    }

    @NotNull
    @Override
    public ViewHolderPosts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
        return new ViewHolderPosts(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPosts holder, int position) {
        Post post = postList.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolderPosts extends RecyclerView.ViewHolder {

        View parent;
        TextView tvPostUsername;
        TextView tvPostCaption;
        TextView tvPostNumLikes;
        TextView tvPostNumComments;
        TextView tvPostTimestamp;
        ImageView ivPostProfilePic;

        ImageView ivThumbnailPlaceholder;
        ImageView ivVolumeControl;
        ProgressBar progressBar;
        FrameLayout media_container;

        ImageButton ibComments;
        LikeButton btnLike;
        LikeButton btnSave;

        public ViewHolderPosts(@NonNull @NotNull View itemView) {
            super(itemView);

            parent = itemView;

            tvPostNumLikes = itemView.findViewById(R.id.tvNumLikesPostFeed);
            tvPostNumComments = itemView.findViewById(R.id.tvNumCommentsPostFeed);
            tvPostCaption = itemView.findViewById(R.id.title);
            tvPostUsername = itemView.findViewById(R.id.tvUsernameFeed);
            tvPostTimestamp = itemView.findViewById(R.id.tvTimestampPostFeed);

            media_container = itemView.findViewById(R.id.media_container);
            progressBar = itemView.findViewById(R.id.progressBar);

            ivVolumeControl = itemView.findViewById(R.id.volume_control);
            ivPostProfilePic = itemView.findViewById(R.id.ivProfilePicFeed);
            ivThumbnailPlaceholder = itemView.findViewById(R.id.thumbnail);

            ibComments = itemView.findViewById(R.id.ibComments);
            btnLike = itemView.findViewById(R.id.likeButton);
            btnSave = itemView.findViewById(R.id.saveButton);

        }

        public void bind(Post post) {

            parent.setTag(this);

            itemView.setOnTouchListener(new OnSwipeTouchListener(context){

                @Override
                public void onSwipeLeft() {
                    super.onSwipeRight();
                    Intent intent = new Intent(context, OtherUserProfileActivity.class);
                    intent.putExtra("post", post);
                    context.startActivity(intent);
                }
            });

            tvPostCaption.setText(post.getCaption());
            tvPostUsername.setText(post.getPostCreatorUsername());
            tvPostTimestamp.setText(post.getPostTimeStamp());
            int numComments = post.getNumberOfComments();
            tvPostNumComments.setText(String.valueOf(numComments));

            ParseFile profileImage = post.getUserProfileImage();
            Glide.with(context).load(profileImage.getUrl())
                    .circleCrop()
                    .into(ivPostProfilePic);
            ParseFile thumbnail = post.getThumbnail();
            Glide.with(context).load(thumbnail.getUrl())
                    .placeholder(R.drawable.video_player_placeholder)
                    .centerCrop()
                    .into(ivThumbnailPlaceholder);

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
                    tvPostNumLikes.setText(post.getNumberOfLikes(likedByUsers));

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

                            user.add("savedPosts", post.getObjectId());

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

                    btnLike.setOnLikeListener( new OnLikeListener(  ) {
                        @Override
                        public void liked( LikeButton likeButton ) {
                            post.add("usersThatLiked", user.getObjectId());
                            post.saveInBackground();
                            Log.e(TAG, "Liking was successful!!");

                            // updates the number of likes locally
                            int currentLikes = Integer.parseInt(tvPostNumLikes.getText().toString()) + 1;
                            tvPostNumLikes.setText(String.valueOf(currentLikes));
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
                            int currentLikes = Integer.parseInt(tvPostNumLikes.getText().toString()) - 1;
                            tvPostNumLikes.setText(String.valueOf(currentLikes));

                        }
                    } );

                }
            });


        }

    }

}
