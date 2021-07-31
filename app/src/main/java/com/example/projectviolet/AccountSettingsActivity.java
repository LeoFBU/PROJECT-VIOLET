package com.example.projectviolet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.parse.ParseUser;

public class AccountSettingsActivity extends AppCompatActivity {

    CardView cvManageTags;
    CardView cvSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        cvManageTags = findViewById(R.id.cvManageTags);
        cvSignOut = findViewById(R.id.cvSignOut);

        cvManageTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSettingsActivity.this, SelectGameTagsActivity.class);
                startActivity(intent);
            }
        });

        cvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(AccountSettingsActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }
}