package com.example.projectviolet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.projectviolet.GameTag;
import com.example.projectviolet.R;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SelectGameTagsAdapter extends RecyclerView.Adapter<SelectGameTagsAdapter.ViewHolderSelectTag>{

    public static final String TAG = "SelectGameTagsAdapter";
    private boolean userHasSelected = false;
    private String selectedGame;
    private ArrayList<GameTag> gamesList;
    private Context context;

    public SelectGameTagsAdapter(Context context, String selectedGame, ArrayList<GameTag> gamesList){
        this.context = context;
        this.selectedGame = selectedGame;
        this.gamesList = gamesList;
    }

    @NotNull
    @Override
    public SelectGameTagsAdapter.ViewHolderSelectTag onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gametag_layout, parent, false);
        return new SelectGameTagsAdapter.ViewHolderSelectTag(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SelectGameTagsAdapter.ViewHolderSelectTag holder, int position) {
        GameTag game = gamesList.get(position);
        holder.bind(game);
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }

    public String getGameTag(){
        return selectedGame;
    }

    public class ViewHolderSelectTag extends RecyclerView.ViewHolder {

        private TextView tvGameName;
        private ImageView ivGameIcon;
        private CheckBox cbSubscribe;

        public ViewHolderSelectTag(@NonNull @NotNull View itemView) {
            super(itemView);

            tvGameName = itemView.findViewById(R.id.tvGameTagName);
            ivGameIcon = itemView.findViewById(R.id.ivGameTagIcon);
            cbSubscribe = itemView.findViewById(R.id.cbSubscribe);

        }

        public void bind(GameTag game) {

            if(!game.isChecked()){
                cbSubscribe.setChecked(false);
            }
            else
                cbSubscribe.setChecked(true);

            tvGameName.setText(game.getGameName());
            ParseFile gameIcon = game.getGameIcon();
            Glide.with(context).load(gameIcon.getUrl()).transform(new RoundedCorners(20)).into(ivGameIcon);

            cbSubscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(cbSubscribe.isChecked() && userHasSelected == false){
                        selectedGame = game.getGameName();
                        cbSubscribe.setChecked(true);
                        game.setChecked(true);
                        userHasSelected = true;
                    }
                    else if (!cbSubscribe.isChecked() && userHasSelected){
                        selectedGame = "";
                        cbSubscribe.setChecked(false);
                        game.setChecked(false);
                        userHasSelected = false;
                    }
                    else{
                        cbSubscribe.setChecked(false);
                    }


                }
            });

        }
    }
}
