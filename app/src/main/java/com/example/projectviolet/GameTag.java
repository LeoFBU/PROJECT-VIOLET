package com.example.projectviolet;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("GameTags")
public class GameTag extends ParseObject {

    public static final String ICON_KEY = "gameIcon";
    public static final String NAME_KEY = "gameName";
    public static final String SUBSCRIBED_USERS_KEY = "subscribedUsers";


    ParseFile getGameIcon(){
        return getParseFile(ICON_KEY);
    }

    String getGameName(){
        return getString(NAME_KEY);
    }
}
