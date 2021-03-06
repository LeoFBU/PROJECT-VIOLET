package com.example.projectviolet;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.projectviolet.fragments.FeedFragment;
import com.example.projectviolet.fragments.ProfileFragment;
import com.example.projectviolet.fragments.UploadFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity: ";
    Fragment fragment;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item) {

                switch(item.getItemId()) {
                    case R.id.action_Home:
                        Log.e(TAG, "onNavigationItemSelected: clicked FEED");
                        fragment = new FeedFragment();
                        break;
                    case R.id.action_Compose:
                        Log.e(TAG, "onNavigationItemSelected: clicked UPLOAD");
                        fragment = new UploadFragment();
                        break;
                    case R.id.action_Profile:
                        Log.e(TAG, "onNavigationItemSelected: clicked PROFILE");
                        fragment = new ProfileFragment();
                        break;
                    default:
                        fragment = new FeedFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_Home);
    }
}