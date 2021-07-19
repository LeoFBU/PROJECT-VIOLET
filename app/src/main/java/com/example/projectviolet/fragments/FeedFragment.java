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
import com.example.projectviolet.VideoPlayerRecyclerView;
import com.example.projectviolet.util.verticalSpacingItem;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    public static final String TAG = "FeedFragment: ";
    private ImageButton ibLogout;
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected ArrayList<Post> feedPosts;
    protected List<String> urls;
    private SwipeRefreshLayout swipeRefresher;

    private VideoPlayerRecyclerView newRecyclerView;


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
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newRecyclerView = view.findViewById(R.id.recycler_view);
        feedPosts = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        newRecyclerView.setLayoutManager(layoutManager);
        verticalSpacingItem.VerticalSpacingItemDecorator itemDecorator = new verticalSpacingItem.VerticalSpacingItemDecorator(10);
        newRecyclerView.addItemDecoration(itemDecorator);

        swipeRefresher = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerFragment);


        queryPosts(0);


        swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: Refresh Listener Triggered");
                feedPosts.clear();
                queryPosts(0);
                swipeRefresher.setRefreshing(false);
            }
        });
        swipeRefresher.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


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
                }
                posts.size();
                feedPosts.addAll(posts);
                initRecyclerView(feedPosts);
            }
        });

    }



    private void initRecyclerView(ArrayList<Post> postList){
        // TODO: Look over the recyclerview code, i think it might be unnecessary to call
        // initRecyclerView everytime a refresh call is made.



        ArrayList<Post> mediaObjects = new ArrayList<Post>(postList);
        newRecyclerView.setPostObjects(mediaObjects);

        PostsAdapter adapter = new PostsAdapter(getContext(), postList);

        newRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        if(newRecyclerView!=null)
            newRecyclerView.releasePlayer();
        super.onDestroy();
    }
}
