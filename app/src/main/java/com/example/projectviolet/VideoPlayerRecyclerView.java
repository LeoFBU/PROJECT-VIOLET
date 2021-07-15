package com.example.projectviolet;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.parse.ParseFile;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayerRecyclerView extends RecyclerView {

    public static final String TAG = "VideoPlayerRecyclerView";
    private enum VolumeState {ON, OFF};

    // utilized for user interface
    private ImageView ivThumbnail, ivVolumeControl;
    private ProgressBar progressBar;
    private View viewHolderParent;
    private FrameLayout frameLayout;
    private PlayerView pvVideoSurfaceView;
    private SimpleExoPlayer epVideoPLayer;

    // utilized for variable logic
    private List<Post> postObjects = new ArrayList<>();
    private int videoSurfaceDefaultHeight = 0;
    private int screenDefaultHeight = 0;
    private Context context;
    private int playPosition = -1;
    private boolean isVideoViewAdded;
    private RequestManager requestManager;

    // utilized for controlling playbackstate
    private VolumeState volumeState;

    public VideoPlayerRecyclerView(@NotNull Context context) {
        super(context);
        init(context);
    }


    public VideoPlayerRecyclerView(@NotNull Context context, @Nullable AttributeSet attr) {
        super(context, attr);
        init(context);
    }

    private void init(Context context){

        this.context = context.getApplicationContext();
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        videoSurfaceDefaultHeight = point.x;
        screenDefaultHeight = point.y;

        pvVideoSurfaceView = new PlayerView(this.context);
        pvVideoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // Creating the player
        epVideoPLayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        //

        pvVideoSurfaceView.setUseController(false);
        pvVideoSurfaceView.setPlayer(epVideoPLayer);
        setVolumeControl(VolumeState.ON);

        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(TAG, "onScrollStateChanged: called.");
                    if(ivThumbnail != null){ // show the old thumbnail
                        ivThumbnail.setVisibility(VISIBLE);
                    }

                    // Special Case for bottom of RecyclerView list
                    if(!recyclerView.canScrollVertically(1)){
                        playVideo(true);
                    }
                    else{
                        playVideo(false);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull @NotNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull @NotNull View view) {
                if(viewHolderParent != null && viewHolderParent.equals(view)){
                    resetVideoView();
                }
            }
        });

        epVideoPLayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable @org.jetbrains.annotations.Nullable Object manifest, int reason) {
            }
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            }
            @Override
            public void onLoadingChanged(boolean isLoading) {
            }
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch(playbackState){

                    case Player.STATE_BUFFERING:
                        Log.e(TAG, "onPlayerStateChanged: BufferingVideo");
                        if(progressBar != null){
                            progressBar.setVisibility(VISIBLE);
                        }
                        break;

                    case Player.STATE_ENDED:
                        Log.d(TAG, "onPlayerStateChanged: Video Ended.");
                        epVideoPLayer.seekTo(0);
                        break;

                    case Player.STATE_IDLE:
                        break;

                    case Player.STATE_READY:
                        Log.d(TAG, "onPlayerStateChanged: Ready to play.");
                        if(progressBar != null){
                            progressBar.setVisibility(GONE);
                        }
                        if(!isVideoViewAdded){
                            addVideoView();
                        }
                        break;

                    default:
                        break;
                }
            }
            @Override
            public void onRepeatModeChanged(int repeatMode) {
            }
            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            }
            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }
            @Override
            public void onPositionDiscontinuity(int reason) {
            }
            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }
            @Override
            public void onSeekProcessed() {
            }
        });

    }

    private void playVideo(boolean isEndOfList){

        int targetPosition;

        if(!isEndOfList){

            int startPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
            int endPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();

            // if there are more than two items on the screen, set difference to be 1
            if(endPosition - startPosition > 1){
                endPosition = startPosition + 1;
            }
            // something wrong happened, return
            if(startPosition < 0 || endPosition < 0){
                return;
            }

            // if there is more than 1 list item on screen
            if(startPosition != endPosition){
                int startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition);
                int endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition);

                targetPosition = startPositionVideoHeight > endPositionVideoHeight ? startPosition : endPosition;
            }
            else{
                targetPosition = startPosition;
            }
        }
        else{
            targetPosition = postObjects.size()-1;
        }

        Log.d(TAG, "playVideo: target position" + targetPosition);

        // video already playing so return
        if(targetPosition == playPosition){
            return;
        }

        // set position of the list item thats going to be played
        playPosition = targetPosition;
        if(pvVideoSurfaceView == null){
            return;
        }


        pvVideoSurfaceView.setVisibility(INVISIBLE);
        removeVideoView(pvVideoSurfaceView);

        int currentPosition = targetPosition - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();

        View child = getChildAt(currentPosition);
        if(child == null){
            return;
        }

        PostsAdapter.ViewHolderPosts holder = (PostsAdapter.ViewHolderPosts) child.getTag();
        if(holder == null){
            playPosition = -1;
            return;
        }

        ivThumbnail = holder.ivThumbnailPlaceholder;
        progressBar = holder.progressBar;
        ivVolumeControl = holder.ivVolumeControl;
        viewHolderParent = holder.itemView;
        frameLayout = holder.itemView.findViewById(R.id.media_container);


        pvVideoSurfaceView.setPlayer(epVideoPLayer);
        viewHolderParent.setOnClickListener(videoViewClickListener);


        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "RecyclerView VideoPlayer"));


        String mediaUrl = null;
        try{
            ParseFile videoFile = postObjects.get(targetPosition).getParseFile("video");
            mediaUrl = videoFile.getUrl();
        }
        catch(Exception e){
            if(mediaUrl == null){
                mediaUrl = postObjects.get(targetPosition).getYoutubeLink();
            }
        }

        if(mediaUrl != null){
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mediaUrl));
            epVideoPLayer.prepare(videoSource);
            epVideoPLayer.setPlayWhenReady(true);
        }

    }

    private void addVideoView(){
        frameLayout.addView(pvVideoSurfaceView);
        isVideoViewAdded = true;
        pvVideoSurfaceView.requestFocus();
        pvVideoSurfaceView.setVisibility(VISIBLE);
        pvVideoSurfaceView.setAlpha(1);
        ivThumbnail.setVisibility(GONE);
    }

    private void resetVideoView(){
        if(isVideoViewAdded){
            removeVideoView(pvVideoSurfaceView);
            playPosition = -1;
            pvVideoSurfaceView.setVisibility(INVISIBLE);
            ivThumbnail.setVisibility(VISIBLE);
        }
    }

    private void removeVideoView(PlayerView videoView) {
        ViewGroup parent = (ViewGroup) videoView.getParent();
        if (parent == null) {
            return;
        }

        int index = parent.indexOfChild(videoView);
        if (index >= 0) {
            parent.removeViewAt(index);
            isVideoViewAdded = false;
            viewHolderParent.setOnClickListener(null);
        }

    }

    private void setVolumeControl(VolumeState state){
        volumeState = state;
        if(state == VolumeState.OFF){
            epVideoPLayer.setVolume(0f);
            animateVolumeControl();
        }
        else if(state == VolumeState.ON){
            epVideoPLayer.setVolume(1f);
            animateVolumeControl();
        }
    }


    private void animateVolumeControl(){
        if(ivVolumeControl != null){
            ivVolumeControl.bringToFront();
            if(volumeState == VolumeState.OFF){
                Glide.with(context).load(R.drawable.ic_volume_off_grey_24dp)
                        .into(ivVolumeControl);
            }
            else if(volumeState == VolumeState.ON){
                Glide.with(context).load(R.drawable.ic_volume_up_grey_24dp)
                        .into(ivVolumeControl);
            }
            ivVolumeControl.animate().cancel();

            ivVolumeControl.setAlpha(1f);

            ivVolumeControl.animate()
                    .alpha(0f)
                    .setDuration(600).setStartDelay(1000);
        }
    }

    private int getVisibleVideoSurfaceHeight(int playPosition) {
        int at = playPosition - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        Log.d(TAG, "getVisibleVideoSurfaceHeight: at: " + at);

        View child = getChildAt(at);
        if (child == null) {
            return 0;
        }

        int[] location = new int[2];
        child.getLocationInWindow(location);

        if (location[1] < 0) {
            return location[1] + videoSurfaceDefaultHeight;
        } else {
            return screenDefaultHeight - location[1];
        }
    }

    private OnClickListener videoViewClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            toggleVolume();
        }
    };

    private void toggleVolume() {
        if (epVideoPLayer != null) {
            if (volumeState == VolumeState.OFF) {
                Log.d(TAG, "togglePlaybackState: enabling volume.");
                setVolumeControl(VolumeState.ON);

            } else if(volumeState == VolumeState.ON) {
                Log.d(TAG, "togglePlaybackState: disabling volume.");
                setVolumeControl(VolumeState.OFF);

            }
        }
    }

    public void setPostObjects(ArrayList<Post> postObjects){
        this.postObjects = postObjects;
    }

    public void releasePlayer() {

        if (epVideoPLayer != null) {
            epVideoPLayer.release();
            epVideoPLayer = null;
        }

        viewHolderParent = null;
    }





}
