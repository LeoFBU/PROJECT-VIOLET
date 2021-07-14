package com.example.projectviolet.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.projectviolet.LoginActivity;
import com.example.projectviolet.MainActivity;
import com.example.projectviolet.Post;
import com.example.projectviolet.PostsAdapter;
import com.example.projectviolet.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    public static final String TAG = "FeedFragment: ";
    private ImageButton ibLogout;
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> feedPosts;
    protected List<String> urls;


    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPostsFeed);
        feedPosts = new ArrayList<>();
        urls = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), feedPosts, urls);
        rvPosts.setAdapter(adapter);
        LinearLayoutManager Manager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(Manager);

        // TODO: Still deciding on which implementation of a videoplayer I want to use.
            //      as of right now, Fenster is the SDK that is commented out, and I am using
            //      https://github.com/yqritc/Android-ScalableVideoView.
            //      Video functionality is not working as intended right now.

        checkSystemWritePermission();       // This and any associated code is for Fenster, it requires permissions.



    }

    private void queryPosts(int skipAmount) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(10);

        if(skipAmount != 0){
            query.setSkip(skipAmount);
        }

        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting Feed: " + e, e);
                    return;
                }
                for(Post post : posts){
                    Log.i(TAG, "Post: " + post.getUser().getUsername() + ": " + post.getCaption());
                    urls.add(post.getVideo().getUrl());
                }

                feedPosts.addAll(posts);
                adapter.notifyDataSetChanged();

            }
        });

    }


    private boolean checkSystemWritePermission() {
        boolean retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(getContext());
            Log.d("TAG", "Can Write Settings: " + retVal);
            if(retVal){
                ///Permission granted by the user
                queryPosts(0);
            }else{
                //permission not granted navigate to permission screen
                openAndroidPermissionsMenu();
            }
        }
        return retVal;
    }

    private void openAndroidPermissionsMenu() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + getContext().getPackageName()));
        startActivity(intent);
    }


}