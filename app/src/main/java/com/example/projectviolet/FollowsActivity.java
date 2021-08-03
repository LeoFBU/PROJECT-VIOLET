package com.example.projectviolet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.projectviolet.adapters.FollowsAdapter;
import com.example.projectviolet.adapters.FollowsPagerAdapter;
import com.example.projectviolet.fragments.FollowersFragment;
import com.example.projectviolet.models.Post;
import com.google.android.material.tabs.TabLayout;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FollowsActivity extends AppCompatActivity implements FollowersFragment.MyStringListener {

    public static final String TAG = "FollowsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follows);

        ParseUser user = getIntent().getParcelableExtra("user");
        Log.e(TAG, "onCreate: " + user.getUsername() );

        ViewPager viewPager = findViewById(R.id.viewPagerFollows);
        viewPager.setAdapter(new FollowsPagerAdapter(getSupportFragmentManager(),
                FollowsActivity.this));
        TabLayout tabLayout = findViewById(R.id.tabsFollows);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void queryUserInfo(int skipAmount, ParseUser user, ArrayList<ParseUser> usersToReturn, FollowsAdapter passedAdapter, String userColumn) {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.setLimit(10);
        List<String> usersFollowing = user.getList(userColumn);

        // to account for SuggestedFollowsFragment
        if(userColumn.equals("suggested")){
            usersFollowing = user.getList("following");
            query.whereNotContainedIn("objectId", usersFollowing);
        }
        else
            query.whereContainedIn("objectId", usersFollowing);

        if(skipAmount != 0){
            query.setSkip(skipAmount);
        }

        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e != null){
                    Log.e(TAG, "ERROR: issue querying users", e );
                }

                usersToReturn.addAll(users);
                passedAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public ParseUser returnUser() {
        return ParseUser.getCurrentUser();
    }

}