package com.example.projectviolet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectviolet.R;
import com.example.projectviolet.adapters.EndlessRecyclerViewScrollListener;
import com.example.projectviolet.adapters.FollowsAdapter;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class SuggestedFollowsFragment extends Fragment {

    public static final String QUERY = "suggested";
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "SuggestedFollowersFrag";
    private int mPage;

    protected FollowsAdapter adapter;
    private RecyclerView rvSuggestedFollow;
    private ArrayList<ParseUser> userList;
    ParseUser user;
    FollowersFragment.MyStringListener interfaceListener;
    private EndlessRecyclerViewScrollListener scrollListener;


    public SuggestedFollowsFragment() {
    }

    public static SuggestedFollowsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SuggestedFollowsFragment fragment = new SuggestedFollowsFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suggested_follows, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvSuggestedFollow = view.findViewById(R.id.rvSuggestedFollows);
        userList = new ArrayList<>();
        interfaceListener = (FollowersFragment.MyStringListener) getContext();
        user = interfaceListener.returnUser();
        adapter = new FollowsAdapter(getContext(), userList, user);
        rvSuggestedFollow.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvSuggestedFollow.setLayoutManager(layoutManager);

        interfaceListener.queryUserInfo(0, user, userList, adapter, QUERY);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                interfaceListener.queryUserInfo(totalItemsCount, user, userList, adapter, QUERY);
            }
        };
        rvSuggestedFollow.addOnScrollListener(scrollListener);

    }
}