package com.example.projectviolet.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectviolet.AccountSettingsActivity;
import com.example.projectviolet.FollowsActivity;
import com.example.projectviolet.LoginActivity;
import com.example.projectviolet.R;
import com.example.projectviolet.SelectGameTagsActivity;
import com.example.projectviolet.adapters.ProfileFragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends Fragment {


    public static final String TAG = "ProfileFragment.java: ";
    private final int[] imageRes = {R.drawable.ic_upload_filled, R.drawable.ic_save_filled};

    private ImageView ivProfilePic;
    private TextView tvNumOfPosts;
    private TextView tvNumOfFollowing;
    private TextView tvNumOfFollowers;
    private TextView tvUsername;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        tvUsername = view.findViewById(R.id.tvUsernameProfile);
        tvNumOfPosts = view.findViewById(R.id.tvPostsNumber);
        tvNumOfFollowers = view.findViewById(R.id.tvFollowersNumber);
        tvNumOfFollowing = view.findViewById(R.id.tvFollowingNumber);
        ivProfilePic = view.findViewById(R.id.ivProfilePicProfile);
        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
        ViewPager viewPager = view.findViewById(R.id.viewPagerFollows);

        viewPager.setAdapter(new ProfileFragmentPagerAdapter(getChildFragmentManager(), getContext()));
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(imageRes[i]);
        }

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

        tvNumOfFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startFollow = new Intent(getContext(), FollowsActivity.class);
                startFollow.putExtra("user", user);
                startActivity(startFollow);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.profilefrag_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSettings:
                startOptionsMenu();
                Log.e(TAG, "onOptionsItemSelected: SELECTED SETTINGS");
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void startOptionsMenu(){
        Intent intent = new Intent(getContext(), AccountSettingsActivity.class);
        startActivity(intent);
    }
}