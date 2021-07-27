package com.example.projectviolet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.projectviolet.util.verticalSpacingItem;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SelectGameTagsActivity extends AppCompatActivity {

    public static final String TAG = "SelectGameTagsActivity";

    RecyclerView rvGameTags;
    private ArrayList<GameTag> GameTagsList;
    protected GameTagsAdapter adapter;
    private Button btnSubmitTags;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game_tags);

        context = this;

        rvGameTags = findViewById(R.id.rvGameTags);
        btnSubmitTags = findViewById(R.id.btnSubmitTags);

        GameTagsList = new ArrayList<>();
        adapter = new GameTagsAdapter(context, GameTagsList);
        rvGameTags.setAdapter(adapter);

        LinearLayoutManager Manager = new LinearLayoutManager(context);
        rvGameTags.setLayoutManager(Manager);

        verticalSpacingItem.VerticalSpacingItemDecorator itemDecorator = new verticalSpacingItem.VerticalSpacingItemDecorator(5);
        rvGameTags.addItemDecoration(itemDecorator);

        queryTags(0);

        btnSubmitTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MainActivity.class);
                finish();
                startActivity(i);
            }
        });

    }

    private void queryTags(int i) {

        ParseQuery<GameTag> query = ParseQuery.getQuery(GameTag.class);
        // might want to create a query limit somehow
        // maybe even add the endless scroll to the specific recyclerview
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<GameTag>() {
            @Override
            public void done(List<GameTag> tags, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts" + e, e);
                    return;
                }
                GameTagsList.addAll(tags);
                adapter.notifyDataSetChanged();
            }
        });
    }

    void StartMainActivity(Context context){
        Intent i = new Intent(context, MainActivity.class);
        finish();
        startActivity(i);
    }




}