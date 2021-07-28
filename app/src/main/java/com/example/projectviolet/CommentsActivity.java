package com.example.projectviolet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectviolet.adapters.CommentsAdapter;
import com.example.projectviolet.models.Comment;
import com.example.projectviolet.models.Post;
import com.example.projectviolet.util.verticalSpacingItem;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    public static final String TAG = "CommentsActivity";

    private TextView tvPostUsername;
    private TextView tvPostCaption;
    private ImageView ivPostUserPfp;
    private ImageView ivPostThumbnail;
    private ImageView ivCurrentUserPFP;
    private RecyclerView rvComments;
    private EditText etSubmissionContent;
    private Button btnSubmitComment;
    protected CommentsAdapter adapter;
    protected ArrayList<Comment> allComments;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        rvComments = findViewById(R.id.rvComments);
        allComments = new ArrayList<>();
        adapter = new CommentsAdapter(this, allComments);
        rvComments.setAdapter(adapter);
        // set the layout manager on the recycler view
        LinearLayoutManager Manager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(Manager);

        verticalSpacingItem.VerticalSpacingItemDecorator itemDecorator = new verticalSpacingItem.VerticalSpacingItemDecorator(5);
        rvComments.addItemDecoration(itemDecorator);



        Post post = getIntent().getParcelableExtra("post");

        tvPostUsername = findViewById(R.id.tvCommentsPostUsername);
        tvPostCaption = findViewById(R.id.tvCommentsPostCaption);
        ivPostUserPfp = findViewById(R.id.ivCommentsPostUserPfp);
        ivPostThumbnail = findViewById(R.id.ivCommentsThumbnail);
        ivCurrentUserPFP = findViewById(R.id.ivCommentCurrentUserPFP);
        etSubmissionContent = findViewById(R.id.etCommentSubmit);
        btnSubmitComment = findViewById(R.id.btnSubmitComment);

        tvPostUsername.setText(post.getUser().getUsername());
        tvPostCaption.setText(post.getCaption());
        ParseFile profileImage = post.getUserProfileImage();
        Glide.with(this).load(profileImage.getUrl()).circleCrop().into(ivPostUserPfp);
        ParseFile thumbnail = (ParseFile) post.get("videoThumbnail");
        Glide.with(this).load(thumbnail.getUrl()).centerCrop().into(ivPostThumbnail);
        ParseFile currentUserImage = ParseUser.getCurrentUser().getParseFile("profileImage");
        Glide.with(this).load(currentUserImage.getUrl()).circleCrop().into(ivCurrentUserPFP);


        List<String> validComments = post.getList("comments");

        queryComments(5, validComments, post);

        btnSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment(etSubmissionContent.getText().toString(), post);
            }
        });


        Log.e(TAG, "onCreate: " + post.getObjectId());
    }

    private void submitComment(String toString, Post post) {
        Comment newComment = new Comment();
        newComment.put("user", ParseUser.getCurrentUser());
        newComment.put("commentContent", toString);
        //newComment.put("post", post.getObjectId());
        newComment.put("postID", post.getObjectId());

        int numComments = post.getInt("numberComments") + 1;
        post.put("numberComments", numComments);
        post.saveInBackground();
        allComments.add(0, newComment);
        adapter.notifyDataSetChanged();

        newComment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.e(TAG, "Post was successful!!" + e);
                etSubmissionContent.setText("");
            }
        });
    }

    private void queryComments(int i, List<String> Dacomments, Post post) {

        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_USER);
        // might want to create a query limit somehow
        // maybe even add the endless scroll to the specific recyclerview
        query.whereContains("postID", post.getObjectId());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts" + e, e);
                    return;
                }
                // this is here to correect the number of comments in case inconsistencies happen
                post.put("numberComments", comments.size());
                post.saveInBackground();
                allComments.addAll(comments);
                adapter.notifyDataSetChanged();

            }
        });



    }


}