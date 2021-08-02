package com.example.projectviolet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.projectviolet.adapters.SelectGameTagsAdapter;
import com.example.projectviolet.util.verticalSpacingItem;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class AttachGameTagActivity extends AppCompatActivity {

    public static final String TAG = "AttachGameTag";

    protected SelectGameTagsAdapter adapter;
    private String chosenGame;
    private ArrayList<GameTag> gameList;
    private RecyclerView rvAttachGameTag;
    private Button btnSubmitTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_game_tag);

        rvAttachGameTag = findViewById(R.id.rvChooseGameTag);
        btnSubmitTag = findViewById(R.id.btnChooseTag);

        gameList = new ArrayList<>();
        adapter = new SelectGameTagsAdapter(this, chosenGame, gameList);
        rvAttachGameTag.setAdapter(adapter);

        LinearLayoutManager Manager = new LinearLayoutManager(this);
        rvAttachGameTag.setLayoutManager(Manager);

        verticalSpacingItem.VerticalSpacingItemDecorator itemDecorator = new verticalSpacingItem.VerticalSpacingItemDecorator(5);
        rvAttachGameTag.addItemDecoration(itemDecorator);

        queryTags(0);

        Intent returnTagIntent = new Intent();
        btnSubmitTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedGame = adapter.getGameTag();
                if(selectedGame.isEmpty()){
                    selectedGame = null;
                }
                returnTagIntent.putExtra("chosenGame", selectedGame);
                setResult(RESULT_OK, returnTagIntent);
                finish();

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
                gameList.addAll(tags);
                adapter.notifyDataSetChanged();
            }
        });
    }

}