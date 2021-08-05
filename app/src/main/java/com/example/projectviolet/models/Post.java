package com.example.projectviolet.models;

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
    public static final String KEY_IMAGE = "videoThumbnail";
    public static final String KEY_USER = "user";
    public static final String KEY_PROFILE_IMAGE = "profileImage";
    public static final String KEY_VIDEO = "video";
    public static final String KEY_YOUTUBE_URL = "videoYoutube";
    public static final String KEY_NUM_OF_COMMENTS = "numberComments";
    public static final String KEY_GAME_TAG = "gameTag";
    public static final String KEY_YOUTUBE_THUMBNAIL = "videoThumbnailYoutube";

    public void setGameTag(String gameTag){
        put(KEY_GAME_TAG, gameTag);
    }

    public String getCaption() {
        return getString(KEY_CAPTION);
    }
    public void setCaption(String description) {
        put(KEY_CAPTION, description);
    }

    public ParseFile getThumbnail() {
        return getParseFile(KEY_IMAGE);
    }
    public void setThumbnail(ParseFile parseFile) {
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

    public String getPostCreatorUsername(){
        return getUser().getUsername();
    }

    public String getPostTimeStamp(){
        Date date = getCreatedAt();
        return formatPostTimestamp(date);
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

    public String getYoutubeThumbnail(){
        return getString(KEY_YOUTUBE_THUMBNAIL);
    }

    public String getNumberOfLikes(List<String> likes){
        return String.valueOf(likes.size());
    }
    public int getNumberOfComments(){
        return getInt(KEY_NUM_OF_COMMENTS);
    }

    public String formatPostTimestamp(Date createdAt) {

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