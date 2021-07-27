package com.example.projectviolet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameTagsAdapter extends RecyclerView.Adapter<GameTagsAdapter.ViewHolderTags> {

    public static final String TAG = "GameTagsAdapter";
    private ArrayList<GameTag> gameTagsList;
    private Context context;

    public GameTagsAdapter(Context context, ArrayList<GameTag> gameTagsList){
        this.context = context;
        this.gameTagsList = gameTagsList;
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

    public class ViewHolderTags extends RecyclerView.ViewHolder {

        private TextView tvGameName;
        private ImageView ivGameIcon;

        public ViewHolderTags(@NonNull View itemView) {
            super(itemView);

            tvGameName = itemView.findViewById(R.id.tvGameTagName);
            ivGameIcon = itemView.findViewById(R.id.ivGameTagIcon);
        }

        public void bind(GameTag gameTag) {
            tvGameName.setText(gameTag.getGameName());
            ParseFile gameIcon = gameTag.getGameIcon();
            Glide.with(context).load(gameIcon.getUrl()).transform(new RoundedCorners(20)).into(ivGameIcon);
        }

    }
}
