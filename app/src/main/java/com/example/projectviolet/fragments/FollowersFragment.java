package com.example.projectviolet.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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

import com.example.projectviolet.FollowsActivity;
import com.example.projectviolet.R;
import com.example.projectviolet.adapters.EndlessRecyclerViewScrollListener;
import com.example.projectviolet.adapters.FollowsAdapter;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FollowersFragment extends Fragment {

    public interface MyStringListener{
        void queryUserInfo(int skipAmount, ParseUser user, ArrayList<ParseUser> usersToReturn, FollowsAdapter adapter, String userColumn);
        ParseUser returnUser();
    }

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "FollowersFragment";
    public static final String QUERY = "followers";

    protected FollowsAdapter adapter;
    private RecyclerView rvFollows;
    private ArrayList<ParseUser> userList;
    private int mPage;
    ParseUser user;
    MyStringListener interfaceListener;
    private EndlessRecyclerViewScrollListener scrollListener;


    public FollowersFragment() {
    }

    public static FollowersFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FollowersFragment fragment = new FollowersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_followers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFollows = view.findViewById(R.id.rvFollows);
        userList = new ArrayList<>();
        interfaceListener = (MyStringListener) getContext();
        user = interfaceListener.returnUser();
        adapter = new FollowsAdapter(getContext(), userList, user);
        rvFollows.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFollows.setLayoutManager(layoutManager);

        interfaceListener.queryUserInfo(0, user, userList, adapter, QUERY);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                interfaceListener.queryUserInfo(totalItemsCount, user, userList, adapter, QUERY);
            }
        };
        rvFollows.addOnScrollListener(scrollListener);

    }

}