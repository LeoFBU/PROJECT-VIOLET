package com.example.projectviolet.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public static final String KEY_COMMENT_CONTENT = "commentContent";
    public static final String TIMESTAMP = "createdAt";
    public static final String KEY_POST_ID = "postID";
    public static final String KEY_PROFILE_PIC = "profileImage";
    public static final String KEY_USER = "user";


    public String getCommentContent(){
        return getString(KEY_COMMENT_CONTENT);
    }
    public void setContent(String content){
        put(KEY_COMMENT_CONTENT, content);
    }

    public ParseUser getCommentUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public void setObjectID(String objectId){
        put(KEY_POST_ID, objectId);
    }

    public String getCommentCreatorUsername(){
        return getCommentUser().getUsername();
    }

    public String getCommentTimestamp(){
        Date date = getCreatedAt();
        return formatCommentTimestamp(date);
    }

    public String formatCommentTimestamp(Date createdAt) {

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

    public ParseFile getCommentProfilePic(){
        return getCommentUser().getParseFile(KEY_PROFILE_PIC);
    }

}
