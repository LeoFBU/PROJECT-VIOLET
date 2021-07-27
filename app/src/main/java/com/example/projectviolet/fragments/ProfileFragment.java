package com.example.projectviolet.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectviolet.LoginActivity;
import com.example.projectviolet.Post;
import com.example.projectviolet.ProfileFeedGridAdapter;
import com.example.projectviolet.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ProfileFragment extends Fragment {

    RecyclerView rvProfile;
    ArrayList<Post> userFeedPosts;
    ProfileFeedGridAdapter gridAdapter;

    public static final String TAG = "ProfileFragment.java: ";
    private ImageButton ibLogout;
    private ImageView ivProfilePic;
    private TextView tvNumOfPosts;
    private TextView tvNumOfFollowing;
    private TextView tvNumOfFollowers;
    private TextView tvUsername;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated( @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userFeedPosts = new ArrayList<>();

        rvProfile = view.findViewById(R.id.rvProfileSavedPosts);
        int numberOfColumns = 3;
        rvProfile.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        gridAdapter = new ProfileFeedGridAdapter(getContext(), userFeedPosts);
        rvProfile.setAdapter(gridAdapter);

        ibLogout = view.findViewById(R.id.ibLogout);
        ibLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        tvUsername = view.findViewById(R.id.tvUsernameProfile);
        tvNumOfPosts = view.findViewById(R.id.tvPostsNumber);
        tvNumOfFollowers = view.findViewById(R.id.tvFollowersNumber);
        tvNumOfFollowing = view.findViewById(R.id.tvFollowingNumber);
        ivProfilePic = view.findViewById(R.id.ivProfilePicProfile);

        ParseUser user = ParseUser.getCurrentUser();
        String numberOfFollowers = String.valueOf(user.getNumber("numOfFollowing"));
        String numberOfFollowing = String.valueOf(user.getNumber("numOfFollowers"));
        String numberOfPosts = String.valueOf(user.getNumber("numOfPosts"));
        String username = user.getUsername();
        tvNumOfFollowers.setText(numberOfFollowers);
        tvNumOfFollowing.setText(numberOfFollowing);
        tvNumOfPosts.setText(numberOfPosts);
        tvUsername.setText(username);


        ParseFile profileImage = user.getParseFile("profileImage");
        Glide.with(requireContext()).load(profileImage.getUrl()).circleCrop().into(ivProfilePic);

        queryPosts(0);
    }


    private void queryPosts(int skipAmount) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        List<Post> savedPosts = new ArrayList<>();
        ParseUser user = ParseUser.getCurrentUser();

        List<String> fromArray = new ArrayList<>();
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
                for(int i = 0; i < objectIDs.size(); i++){
                    Log.e(TAG, "done: " + objectIDs.get(i) );
                }
//                for(int i = 0; i < posts.size(); i++){
//                    fromArray.add(posts.get(i).getObjectId());
//                }
//                for(int i = 0; i < objectIDs.size(); i++){
//                    if(posts.contains(objectIDs.get(i))){
//                        int index = posts.indexOf(objectIDs);
//                        savedPosts.add(posts.get(objectIDs.get(i)));
//                    }
//                }
                posts.size();
                userFeedPosts.addAll(posts);
                gridAdapter.notifyDataSetChanged();
            }
        });
    }

}