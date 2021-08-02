package com.example.projectviolet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.projectviolet.util.verticalSpacingItem;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SelectGameTagsActivity extends AppCompatActivity {

    public static final String TAG = "SelectGameTagsActivity";

    private ArrayList<GameTag> GameTagsList;
    private List<String> preferredTags;

    protected GameTagsAdapter adapter;
    RecyclerView rvGameTags;
    Context context;

    private Button btnSubmitTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game_tags);

        context = this;
        ParseUser user = ParseUser.getCurrentUser();

        rvGameTags = findViewById(R.id.rvGameTags);
        btnSubmitTags = findViewById(R.id.btnSubmitTags);

        GameTagsList = new ArrayList<>();
        preferredTags = user.getList("prefferedTags");
        //preferredTags = new ArrayList<>();

        adapter = new GameTagsAdapter(context, GameTagsList, preferredTags);
        rvGameTags.setAdapter(adapter);

        LinearLayoutManager Manager = new LinearLayoutManager(context);
        rvGameTags.setLayoutManager(Manager);

        verticalSpacingItem.VerticalSpacingItemDecorator itemDecorator = new verticalSpacingItem.VerticalSpacingItemDecorator(5);
        rvGameTags.addItemDecoration(itemDecorator);

        queryTags(0);

        btnSubmitTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // iterate through gametags list if GameTags object .checked == true, add to new array
                preferredTags = adapter.getArrayList();
                for (int i = 0; i < preferredTags.size(); i++){
                    Log.d(TAG, "testlol:" + preferredTags.get(i));

                }

                ParseUser user = ParseUser.getCurrentUser();
                user.put("prefferedTags", preferredTags);
                user.saveInBackground();

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


        query.addAscendingOrder("gameName");
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