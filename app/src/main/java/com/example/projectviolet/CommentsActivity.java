package com.example.projectviolet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectviolet.adapters.CommentsAdapter;
import com.example.projectviolet.models.Comment;
import com.example.projectviolet.models.Post;
import com.example.projectviolet.util.verticalSpacingItem;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
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
    static final int MAX_COMMENT_LENGTH = 50;

    private TextView tvPostUsername;
    private TextView tvPostCaption;
    private ImageView ivVolume;
    private ImageView ivPostUserPfp;
    private ImageView ivPostThumbnail;
    private ImageView ivCurrentUserPFP;
    private RecyclerView rvComments;
    private EditText etSubmissionContent;
    private SimpleExoPlayerView exoPlayerView;
    private SimpleExoPlayer exoPlayer;
    private Button btnSubmitComment;
    private Post post;
    private CardView cvVideoHolder;
    private Context context;
    protected CommentsAdapter adapter;
    protected ArrayList<Comment> allComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        context = this;
        rvComments = findViewById(R.id.rvComments);
        allComments = new ArrayList<>();
        adapter = new CommentsAdapter(this, allComments);
        rvComments.setAdapter(adapter);
        LinearLayoutManager Manager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(Manager);

        verticalSpacingItem.VerticalSpacingItemDecorator itemDecorator = new verticalSpacingItem.VerticalSpacingItemDecorator(5);
        rvComments.addItemDecoration(itemDecorator);

        post = getIntent().getParcelableExtra("post");

        ivVolume = findViewById(R.id.ivVolume);
        cvVideoHolder = findViewById(R.id.cvVideoHolder);
        tvPostUsername = findViewById(R.id.tvCommentsPostUsername);
        tvPostCaption = findViewById(R.id.tvCommentsPostCaption);
        ivPostUserPfp = findViewById(R.id.ivCommentsPostUserPfp);
        ivPostThumbnail = findViewById(R.id.ivCommentsThumbnail);
        ivCurrentUserPFP = findViewById(R.id.ivCommentCurrentUserPFP);
        etSubmissionContent = findViewById(R.id.etCommentSubmit);
        btnSubmitComment = findViewById(R.id.btnSubmitComment);
        exoPlayerView = findViewById(R.id.idExoPlayerVIew);

        tvPostUsername.setText(post.getPostCreatorUsername());
        tvPostCaption.setText(post.getCaption());
        ParseFile profileImage = post.getUserProfileImage();
        Glide.with(this).load(profileImage.getUrl()).circleCrop().into(ivPostUserPfp);
        ParseFile thumbnail = (ParseFile) post.get("videoThumbnail");
        Glide.with(this).load(thumbnail.getUrl()).centerCrop().into(ivPostThumbnail);
        ParseFile currentUserImage = ParseUser.getCurrentUser().getParseFile("profileImage");
        Glide.with(this).load(currentUserImage.getUrl()).circleCrop().into(ivCurrentUserPFP);

        queryComments(post);

        initExoPlayer();

        btnSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etSubmissionContent.getText().toString().length() > MAX_COMMENT_LENGTH) {
                    Toast.makeText(CommentsActivity.this, "Sorry, your commment is too long!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                submitComment(etSubmissionContent.getText().toString(), post);
            }
        });

        cvVideoHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: " + "CLICKED EXOPLAYERVIEW" );
                if(exoPlayer.getVolume() == 0f) {
                    Glide.with(context).load(R.drawable.ic_volume_up_grey_24dp).into(ivVolume);
                    exoPlayer.setVolume(1f);
                }
                else {
                    Glide.with(context).load(R.drawable.ic_volume_off_grey_24dp).into(ivVolume);
                    exoPlayer.setVolume(0f);
                }
            }
        });

    }

    private void initExoPlayer(){

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        String mediaUrl = null;
        try{
            ParseFile videoFile = post.getVideo();
            mediaUrl = videoFile.getUrl();
        }
        catch(Exception e){
            if(mediaUrl == null){
                mediaUrl = post.getYoutubeLink();

            }
        }
        Uri videouri = Uri.parse(mediaUrl);

        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);

        exoPlayerView.setUseController(false);
        exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        exoPlayerView.setPlayer(exoPlayer);

        exoPlayer.prepare(mediaSource);
        ivPostThumbnail.setVisibility(View.VISIBLE);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        if(exoPlayer.getPlayWhenReady()){
            ivPostThumbnail.setVisibility(View.GONE);
            exoPlayerView.setVisibility(View.VISIBLE);
        }

    }

    private void submitComment(String toString, Post post) {
        Comment newComment = new Comment();
        newComment.put("user", ParseUser.getCurrentUser());
        newComment.put("commentContent", toString);
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

    private void queryComments(Post post) {

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
                // this is here to correct the number of comments in case inconsistencies happen
                post.put("numberComments", comments.size());
                post.saveInBackground();
                allComments.addAll(comments);
                adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        exoPlayer.stop();
    }
}