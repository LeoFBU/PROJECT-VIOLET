package com.example.projectviolet;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_PROFILE_IMAGE = "profileImage";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_FOLLOWERS = "followers";
    public static final String KEY_FOLLOWING = "following";

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }
    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }


    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public ParseFile getUserProfileImage(){
        return getUser().getParseFile(KEY_PROFILE_IMAGE);
    }

    public int getFollowers(){
        return getUser().getInt(KEY_FOLLOWERS);
    }

    public String getLikes(){
        return String.valueOf(getInt(KEY_LIKES));
    }
    public void setLikes(int numberOfLikes){
        put(KEY_LIKES, numberOfLikes);
    }

}
