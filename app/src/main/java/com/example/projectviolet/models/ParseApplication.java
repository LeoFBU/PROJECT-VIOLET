package com.example.projectviolet.models;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(GameTag.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("hgnYcqSIgPkJEfdyJz17fyJTemqJYe20rORRHQxF")
                .clientKey("PhCeF4brIdrGqeD2Mom90qSa059Do0lmZsGv5MJV")
                .server("https://parseapi.back4app.com")
                .build()
        );


    }

}
