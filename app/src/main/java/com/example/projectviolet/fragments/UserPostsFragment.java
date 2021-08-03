package com.example.projectviolet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectviolet.models.Post;
import com.example.projectviolet.adapters.ProfileFeedGridAdapter;
import com.example.projectviolet.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class UserPostsFragment extends Fragment {

    public UserPostsFragment() {
        // Required empty public constructor
    }

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "UserPostsFragment";

    private RecyclerView rvProfile;
    private ArrayList<Post> usersOwnPosts;
    private ProfileFeedGridAdapter gridAdapter;

    public static UserPostsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        UserPostsFragment fragment = new UserPostsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usersOwnPosts = new ArrayList<>();
        rvProfile = view.findViewById(R.id.rvProfileUserPosts);
        rvProfile.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        gridAdapter = new ProfileFeedGridAdapter(getContext(), usersOwnPosts);
        rvProfile.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();

        queryPosts(0);

    }

    private void queryPosts(int skipAmount) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);

        ParseUser user = ParseUser.getCurrentUser();
        query.whereContains("user", user.getObjectId());

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
                for (Post post : posts) {
                    Log.e(TAG, "done: "+ post.getCaption());
                }
                usersOwnPosts.addAll(posts);
                gridAdapter.notifyDataSetChanged();
            }
        });
    }
}