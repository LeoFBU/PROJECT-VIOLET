package com.example.projectviolet.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectviolet.LoginActivity;
import com.example.projectviolet.Post;
import com.example.projectviolet.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ProfileFragment extends Fragment {

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

        // TODO: finish setting up profile page with correct information.

        tvUsername = view.findViewById(R.id.tvUsernameProfile);
        tvNumOfFollowers = view.findViewById(R.id.tvFollowersNumber);
        tvNumOfFollowing = view.findViewById(R.id.tvFollowingNumber);
        ivProfilePic = view.findViewById(R.id.ivProfilePicProfile);


        Post post = new Post();
        post.setUser(ParseUser.getCurrentUser());
        ParseUser user = ParseUser.getCurrentUser();

        user.fetchInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                tvNumOfFollowers.setText(String.valueOf(post.getAmountFollowers()));

            }
        });





        ParseFile profileImage = post.getUserProfileImage();
        Glide.with(getContext()).load(profileImage.getUrl()).circleCrop().into(ivProfilePic);



    }
}