package com.example.projectviolet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.projectviolet.R;
import com.example.projectviolet.models.GameTag;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GameTagsAdapter extends RecyclerView.Adapter<GameTagsAdapter.ViewHolderTags> {

    public static final String TAG = "GameTagsAdapter";
    private ArrayList<GameTag> gameTagsList;
    private Context context;
    private List<String> preferredGames;

    public GameTagsAdapter(Context context, ArrayList<GameTag> gameTagsList, List<String> preferredGames){
        this.context = context;
        this.gameTagsList = gameTagsList;
        this.preferredGames = preferredGames;
    }

    @NotNull
    @Override
    public GameTagsAdapter.ViewHolderTags onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gametag_layout, parent, false);
        return new ViewHolderTags(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameTagsAdapter.ViewHolderTags holder, int position) {
        GameTag gameTag = gameTagsList.get(position);
        holder.bind(gameTag);
    }

    @Override
    public int getItemCount() {
        return gameTagsList.size();
    }

    public List<String> getArrayList(){
        return preferredGames;
    }

    public class ViewHolderTags extends RecyclerView.ViewHolder {

        private TextView tvGameName;
        private ImageView ivGameIcon;
        private CheckBox cbSubscribe;
        private CardView cvCardGame;

        public ViewHolderTags(@NonNull View itemView) {
            super(itemView);

            tvGameName = itemView.findViewById(R.id.tvGameTagName);
            ivGameIcon = itemView.findViewById(R.id.ivGameTagIcon);
            cbSubscribe = itemView.findViewById(R.id.cbSubscribe);
            cvCardGame = itemView.findViewById(R.id.cvGameTag);
        }

        public void bind(GameTag gameTag) {

            if(!gameTag.isChecked() && !(preferredGames.contains(gameTag.getGameName()))){
                cbSubscribe.setChecked(false);
            }
            else {
                cbSubscribe.setChecked(true);
            }

            tvGameName.setText(gameTag.getGameName());
            ParseFile gameIcon = gameTag.getGameIcon();
            Glide.with(context).load(gameIcon.getUrl()).transform(new RoundedCorners(20)).into(ivGameIcon);

            ParseUser user = ParseUser.getCurrentUser();
            user.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    cvCardGame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cbSubscribe.setSoundEffectsEnabled(false);
                            cbSubscribe.performClick();
                            if(cbSubscribe.isChecked()){
                                cbSubscribe.setChecked(true);
                                gameTag.setChecked(true);
                                preferredGames.add(gameTag.getGameName());
                            }
                            else
                            {
                                gameTag.setChecked(false);
                                preferredGames.remove(gameTag.getGameName());
                            }
                        }
                    });
                }
            });

        }
    }
}
