package com.example.projectviolet.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.projectviolet.adapters.EndlessRecyclerViewScrollListener;
import com.example.projectviolet.models.Post;
import com.example.projectviolet.adapters.PostsAdapter;
import com.example.projectviolet.R;
import com.example.projectviolet.adapters.VideoPlayerRecyclerView;
import com.example.projectviolet.util.verticalSpacingItem;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {

    public static final String TAG = "FeedFragment: ";
    protected PostsAdapter adapter;
    protected ArrayList<Post> feedPostList;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresher;
    private EndlessRecyclerViewScrollListener scrollListener;
    private VideoPlayerRecyclerView feedRecyclerView;

    public FeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        feedRecyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.pbProgressBarFeed);
        feedPostList = new ArrayList<>();


        adapter = new PostsAdapter(getContext(), feedPostList);
        feedRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        feedRecyclerView.setLayoutManager(layoutManager);
        verticalSpacingItem.VerticalSpacingItemDecorator itemDecorator = new verticalSpacingItem.VerticalSpacingItemDecorator(5);
        feedRecyclerView.addItemDecoration(itemDecorator);

        queryPosts(0);


        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.e(TAG, "onLoadMore: page = " + page + "\nTotal items count= " + totalItemsCount);
                queryPosts(totalItemsCount);
            }
        };
        feedRecyclerView.addOnScrollListener(scrollListener);

        swipeRefresher = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerFragment);
        swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: Refresh Listener Triggered");
                feedPostList.clear();
                adapter.notifyDataSetChanged();
                queryPosts(0);
                swipeRefresher.setRefreshing(false);
            }
        });
        setRefreshColors();

    }

    private void queryPosts(int skipAmount) {

        progressBar.setVisibility(ProgressBar.VISIBLE);
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(5);

        ParseUser user = ParseUser.getCurrentUser();
        List<String> preferredTags = user.getList("prefferedTags");
        query.whereContainedIn("gameTag", preferredTags);

        if(skipAmount != 0){
            query.setSkip(skipAmount);
        }

        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting Feed: " + e, e);
                    progressBar.setVisibility(ProgressBar.GONE);
                    return;
                }
                for(Post post : posts){
                    Log.i(TAG, "Post: " + post.getUser().getUsername() + ": " + post.getCaption());
                }
                progressBar.setVisibility(ProgressBar.GONE);
                posts.size();
                feedPostList.addAll(posts);
                adapter.notifyDataSetChanged();
                feedRecyclerView.setPostList(feedPostList);

            }
        });
    }


    private void setRefreshColors(){
        swipeRefresher.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void onDestroy() {
        if(feedRecyclerView !=null)
            feedRecyclerView.releasePlayer();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        feedRecyclerView.pauseVideo();
    }

    @Override
    public void onResume() {
        super.onResume();
        feedRecyclerView.continueVideo();
    }
}