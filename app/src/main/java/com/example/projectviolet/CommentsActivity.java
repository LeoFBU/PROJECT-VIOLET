package com.example.projectviolet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

public class CommentsActivity extends AppCompatActivity {

    public static final String TAG = "CommentsActivity";

    private TextView tvPostUsername;
    private TextView tvPostCaption;
    private ImageView ivPostUserPfp;
    private ImageView ivPostThumbnail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Post post = getIntent().getParcelableExtra("post");

        tvPostUsername = findViewById(R.id.tvCommentsPostUsername);
        tvPostCaption = findViewById(R.id.tvCommentsPostCaption);
        ivPostUserPfp = findViewById(R.id.ivCommentsPostUserPfp);
        ivPostThumbnail = findViewById(R.id.ivCommentsThumbnail);

        tvPostUsername.setText(post.getUser().getUsername());
        tvPostCaption.setText(post.getCaption());
        ParseFile profileImage = post.getUserProfileImage();
        Glide.with(this).load(profileImage.getUrl()).circleCrop().into(ivPostUserPfp);
        ParseFile thumbnail = (ParseFile) post.get("videoThumbnail");
        Glide.with(this).load(thumbnail.getUrl()).into(ivPostThumbnail);


        Log.e(TAG, "onCreate: " + post.getObjectId());
    }


}