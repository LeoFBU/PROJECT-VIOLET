package com.example.projectviolet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectviolet.models.Post;
import com.example.projectviolet.util.OnSwipeTouchListener;
import com.parse.ParseFile;

public class OtherUserProfileActivity extends AppCompatActivity {

    private TextView tvNumFollowers;
    private TextView tvNumFollowing;
    private TextView tvNumPosts;
    private TextView tvUsername;
    private ImageView ivProfilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        View view =  getWindow().getDecorView().getRootView();
        view.setOnTouchListener(new OnSwipeTouchListener(this){

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                    finish();
            }
        });

        Post post = getIntent().getParcelableExtra("post");

        tvNumFollowers = findViewById(R.id.tvFollowersNumberPostCreator);
        tvNumFollowing = findViewById(R.id.tvFollowingNumberPostCreator);
        tvNumPosts = findViewById(R.id.tvPostsNumberPostCreator);
        tvUsername = findViewById(R.id.tvUsernameProfilePostCreator);
        ivProfilePic = findViewById(R.id.ivProfilePicturePostCreator);

        tvUsername.setText(post.getUser().getUsername());
        tvNumFollowers.setText(String.valueOf(post.getUser().getInt("numOfFollowers")));
        tvNumFollowing.setText(String.valueOf(post.getUser().getInt("numOfFollowing")));
        tvNumPosts.setText(String.valueOf(post.getUser().getInt("numOfPosts")));
        ParseFile profileImage = post.getUserProfileImage();
        Glide.with(this).load(profileImage.getUrl()).circleCrop().into(ivProfilePic);



    }
}