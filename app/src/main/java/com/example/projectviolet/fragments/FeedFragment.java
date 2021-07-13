package com.example.projectviolet.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.projectviolet.LoginActivity;
import com.example.projectviolet.Post;
import com.example.projectviolet.PostsAdapter;
import com.example.projectviolet.R;
import com.parse.FindCallback;
import com.parse.ParseException;
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
        adapter = new PostsAdapter(getContext(), feedPosts);
        rvPosts.setAdapter(adapter);
        LinearLayoutManager Manager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(Manager);

        queryPosts(0);

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

                feedPosts.addAll(posts);
                adapter.notifyDataSetChanged();

            }
        });

    }
}