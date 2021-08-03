package com.example.projectviolet.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.projectviolet.fragments.FollowersFragment;
import com.example.projectviolet.fragments.FollowingFragment;
import com.example.projectviolet.fragments.SavedPostsFragment;
import com.example.projectviolet.fragments.SuggestedFollowsFragment;
import com.example.projectviolet.fragments.UserPostsFragment;

import org.jetbrains.annotations.NotNull;

public class FollowsPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "followers", "following", "suggested" };
    private Context context;

    public FollowsPagerAdapter(@NotNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FollowersFragment.newInstance(position);
            case 1:
                return FollowingFragment.newInstance(position);
            case 2:
                return SuggestedFollowsFragment.newInstance(position);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}

