package com.example.projectviolet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

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
                        // do something here
                        Toast.makeText(MainActivity.this, "Feed", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onNavigationItemSelected: clicked FEED");
                        fragment = new FeedFragment();
                        break;
                    case R.id.action_Compose:
                        // do something here
                        Toast.makeText(MainActivity.this, "Upload", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onNavigationItemSelected: clicked UPLOAD");
                        fragment = new UploadFragment();
                        break;
                    case R.id.action_Profile:
                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onNavigationItemSelected: clicked PROFILE");
                        fragment = new ProfileFragment();
                        break;
                    // do something here
                    default:
                        fragment = new FeedFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_Profile);
    }



}