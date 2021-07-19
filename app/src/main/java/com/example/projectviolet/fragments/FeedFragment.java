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

import com.example.projectviolet.EndlessRecyclerViewScrollListener;
import com.example.projectviolet.Post;
import com.example.projectviolet.PostsAdapter;
import com.example.projectviolet.R;
import com.example.projectviolet.VideoPlayerRecyclerView;
import com.example.projectviolet.util.verticalSpacingItem;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    public static final String TAG = "FeedFragment: ";
    protected PostsAdapter adapter;
    protected ArrayList<Post> feedPosts;
    private SwipeRefreshLayout swipeRefresher;
    private EndlessRecyclerViewScrollListener scrollListener;
    private VideoPlayerRecyclerView feedRecyclerView;


    public FeedFragment() {
        // Required empty public constructor
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
        feedPosts = new ArrayList<>();

        adapter = new PostsAdapter(getContext(), feedPosts);
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
                feedPosts.clear();
                adapter.notifyDataSetChanged();
                queryPosts(0);
                swipeRefresher.setRefreshing(false);
            }
        });
        setRefreshColors();

    }

    private void queryPosts(int skipAmount) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(5);

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
                adapter.notifyDataSetChanged();
                feedRecyclerView.setPostObjects(feedPosts);
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
}
