package com.example.projectviolet.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public static final String COMMENT_CONTENT = "commentContent";
    public static final String TIMESTAMP = "createdAt";
    public static final String KEY_USER = "user";


    public String getCommentContent(){
        return getString(COMMENT_CONTENT);
    }

    public ParseUser getCommentUser() {
        return getParseUser(KEY_USER);
    }

    public String getCommentTimestamp(Date createdAt) {

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