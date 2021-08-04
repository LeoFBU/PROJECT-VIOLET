package com.example.projectviolet.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("GameTags")
public class GameTag extends ParseObject {

    public static final String ICON_KEY = "gameIcon";
    public static final String NAME_KEY = "gameName";
    public static final String SUBSCRIBED_USERS_KEY = "subscribedUsers";
    boolean isSelected;

    public ParseFile getGameIcon(){
        return getParseFile(ICON_KEY);
    }

    public String getGameName(){
        return getString(NAME_KEY);
    }

    public Boolean isChecked(){
        return isSelected;
    }

    public void setChecked(boolean selected){
        isSelected = selected;
    }
}
