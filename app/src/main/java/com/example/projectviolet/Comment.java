package com.example.projectviolet;

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

    public Date getDateCreated(){
        return getDate(TIMESTAMP);
    }

    public ParseUser getCommentUser() {
        return getParseUser(KEY_USER);
    }
}
