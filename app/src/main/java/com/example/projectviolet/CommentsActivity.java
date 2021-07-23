package com.example.projectviolet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    public static final String TAG = "CommentsActivity";

    private TextView tvPostUsername;
    private TextView tvPostCaption;
    private ImageView ivPostUserPfp;
    private ImageView ivPostThumbnail;
    private RecyclerView rvComments;
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

        List<String> validComments = post.getList("comments");

        queryComments(5, validComments, post);


//        List<String> commentIDS = post.getList("comments");
//
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
//        query.getInBackground("ZjS3tmoCgR", (object, e) -> {
//            if (e == null) {
//                //Object was successfully retrieved
//                String commentContent = object.getString("commentContent");
//                Log.e(TAG, "onCreate: "+ commentContent );
//            } else {
//                // something went wrong
//                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });




        Log.e(TAG, "onCreate: " + post.getObjectId());
    }

    private void queryComments(int i, List<String> Dacomments, Post post) {

        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_USER);
        query.setLimit(5);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts" + e, e);
                    return;
                }
                ///for(Comment comment : comments){
                //    Log.i(TAG, "Post: " + comment.getCommentUser().getUsername() + ": " + comment.getCommentContent());
                //}
                List<Comment> correctCommments = new ArrayList<>();
                for(int i = 0; i < comments.size();i++){
                    if(post.getObjectId().equals(comments.get(i).getString("postID"))){
                        correctCommments.add(comments.get(i));
                    }
                }

                allComments.addAll(correctCommments);
                adapter.notifyDataSetChanged();

            }
        });












    }


}