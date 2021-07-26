package com.example.projectviolet;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_CAPTION = "caption";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_PROFILE_IMAGE = "profileImage";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_VIDEO = "video";
    public static final String KEY_FOLLOWERS_AMOUNT = "numOfFollowing";
    public static final String KEY_FOLLOWING_AMOUNT = "numOfFollowers";
    public static final String KEY_POSTS_AMOUNT = "numOfPosts";
    public static final String KEY_YOUTUBE_URL = "videoYoutube";
    public static final String KEY_LIKED_LIST = "likedPosts";
    public static final String KEY_LIKED_BY_USERS = "usersThatLiked";
    public static final String KEY_NUM_OF_COMMENTS = "numberComments";

    public String getCaption() {
        return getString(KEY_CAPTION);
    }
    public void setCaption(String description) {
        put(KEY_CAPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }
    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }




    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseFile getUserProfileImage() {
        return getUser().getParseFile(KEY_PROFILE_IMAGE);
    }

    public int getNumFollowers() {
        return getUser().getInt(KEY_FOLLOWERS_AMOUNT);
    }

    public int getNumFollowing() {
        return getUser().getInt(KEY_FOLLOWING_AMOUNT);
    }

    public int getNumPosts() {
        return getUser().getInt(KEY_POSTS_AMOUNT);
    }


    public ParseFile getVideo() {
        return getParseFile(KEY_VIDEO);
    }
    public void setVideo(ParseFile video){
        put(KEY_VIDEO, video);
    }

    public String getYoutubeLink(){
        return getString(KEY_YOUTUBE_URL);
    }
    public void setYoutubeLink(String youtubeUrl){
        put(KEY_YOUTUBE_URL, youtubeUrl);
    }


    public String getNumberOfLikes(List<String> likes){
        return String.valueOf(likes.size());
    }
    public int getNumberOfComments(){
        return getInt(KEY_NUM_OF_COMMENTS);
    }

    public List<String> getPostsLikedUsers(){
        return getList(KEY_LIKED_BY_USERS);
    }
    public void setPostsLikedUsers(List<String> list){
        put(KEY_LIKED_BY_USERS, list);
    }

    public List<String> getUserSavedPosts(){
        return getList("savedPosts");
    }


    /**
     * Get video file screenshot
     *
     * @ param path the path of the video file
     * @ return bitmap returns the obtained bitmap
     */

    public Bitmap getVideoThumb(String path) {

        MediaMetadataRetriever media = new MediaMetadataRetriever();

        media.setDataSource(path);
        return media.getFrameAtTime();

    }


    public String getPostTimestamp(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }

}