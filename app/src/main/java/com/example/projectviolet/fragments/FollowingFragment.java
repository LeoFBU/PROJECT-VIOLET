package com.example.projectviolet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectviolet.R;
import com.example.projectviolet.adapters.EndlessRecyclerViewScrollListener;
import com.example.projectviolet.adapters.FollowsAdapter;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FollowingFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "FollowingFragment";
    public static final String QUERY = "following";
    private int mPage;

    protected FollowsAdapter adapter;
    private RecyclerView rvFollowing;
    private ArrayList<ParseUser> userList;
    ParseUser user;
    FollowersFragment.MyStringListener interfaceListener;
    private EndlessRecyclerViewScrollListener scrollListener;

    public FollowingFragment() {
    }

    public static FollowingFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FollowingFragment fragment = new FollowingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_following, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFollowing = view.findViewById(R.id.rvFollowing);
        userList = new ArrayList<>();
        adapter = new FollowsAdapter(getContext(), userList);
        rvFollowing.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFollowing.setLayoutManager(layoutManager);

        interfaceListener = (FollowersFragment.MyStringListener) getContext();
        user = interfaceListener.returnUser();
        interfaceListener.queryUserInfo(0, user, userList, adapter, QUERY);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                interfaceListener.queryUserInfo(totalItemsCount, user, userList, adapter, QUERY);
            }
        };
        rvFollowing.addOnScrollListener(scrollListener);
    }
}