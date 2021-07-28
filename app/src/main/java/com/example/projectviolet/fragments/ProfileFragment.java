package com.example.projectviolet.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectviolet.LoginActivity;
import com.example.projectviolet.R;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ProfileFragment extends Fragment {



    public static final String TAG = "ProfileFragment.java: ";
    public static final String ARG_PAGE = "ARG_PAGE";

    private ImageButton ibLogout;
    private ImageView ivProfilePic;
    private TextView tvNumOfPosts;
    private TextView tvNumOfFollowing;
    private TextView tvNumOfFollowers;
    private TextView tvUsername;

    private int mPage;

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

        ViewPager viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new ProfileFragmentPagerAdapter(getChildFragmentManager(), getContext()));

        TabLayout tabLayout =  view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

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

    }


}