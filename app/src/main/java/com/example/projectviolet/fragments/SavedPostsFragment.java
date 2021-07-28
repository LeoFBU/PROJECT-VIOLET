package com.example.projectviolet.fragments;

import android.os.Bundle;

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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SavedPostsFragment extends Fragment {

    public SavedPostsFragment() {
        // Required empty public constructor
    }

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "SavedPostsFragment";
    private int mPage;

    RecyclerView rvProfileUserPosts;
    ArrayList<Post> userFeedPosts;
    ProfileFeedGridAdapter gridAdapter;

    public static SavedPostsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SavedPostsFragment fragment = new SavedPostsFragment();
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
        return inflater.inflate(R.layout.fragment_saved_posts, container, false);

    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userFeedPosts = new ArrayList<>();
        rvProfileUserPosts = view.findViewById(R.id.rvProfileSavedPosts);
        rvProfileUserPosts.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        gridAdapter = new ProfileFeedGridAdapter(getContext(), userFeedPosts);
        rvProfileUserPosts.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
        int numberOfColumns = 3;

        queryPosts(0);

        //queryPosts(0);


    }


    private void queryPosts(int skipAmount) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);

        ParseUser user = ParseUser.getCurrentUser();
        List<String> objectIDs = user.getList("savedPosts");
        query.whereContainedIn("objectId", objectIDs);

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
//                for(int i = 0; i < objectIDs.size(); i++){
//                    Log.e(TAG, "done: " + objectIDs.get(i) );
//                }

                userFeedPosts.addAll(posts);
                gridAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();


    }


}