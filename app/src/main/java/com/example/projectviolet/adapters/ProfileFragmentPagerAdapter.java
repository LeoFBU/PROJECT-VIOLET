package com.example.projectviolet.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.projectviolet.R;
import com.example.projectviolet.fragments.SavedPostsFragment;
import com.example.projectviolet.fragments.UserPostsFragment;

import org.jetbrains.annotations.NotNull;

public class ProfileFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private final String[] tabTitles = new String[] {"posts", "saved"};
    private Context context;

    public ProfileFragmentPagerAdapter(@NotNull FragmentManager fm, Context context) {
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
                return UserPostsFragment.newInstance(position);

            case 1:
                return SavedPostsFragment.newInstance(position);
            default:
                throw new IllegalArgumentException();
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return null;
        // return tabTitles[position];
    }


}
