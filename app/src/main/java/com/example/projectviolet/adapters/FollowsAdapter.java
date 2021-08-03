package com.example.projectviolet.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectviolet.OtherUserProfileActivity;
import com.example.projectviolet.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FollowsAdapter extends RecyclerView.Adapter<FollowsAdapter.ViewHolderFollows> {

    public static final String TAG = "FollowsAdapter";
    private ArrayList<ParseUser> userList;
    private Context context;
    private ParseUser mainUser;

    public FollowsAdapter(Context context, ArrayList<ParseUser> userList, ParseUser mainUser){
        this.context = context;
        this.userList = userList;
        this.mainUser = mainUser;
    }

    @NotNull
    @Override
    public FollowsAdapter.ViewHolderFollows onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_layout, parent, false);
        return new ViewHolderFollows(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FollowsAdapter.ViewHolderFollows holder, int position) {
        ParseUser user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolderFollows extends RecyclerView.ViewHolder {

        private TextView followUsername;
        private ImageView followProfilePicture;
        private CardView cvFollows;
        private Button btnFollowState;
        List<ParseUser> list = mainUser.getList("following");
        boolean isFollowing;

        public ViewHolderFollows(@NonNull @NotNull View itemView) {
            super(itemView);

            followProfilePicture = itemView.findViewById(R.id.ivFollowProfilePic);
            followUsername = itemView.findViewById(R.id.tvFollowUsername);
            cvFollows = itemView.findViewById(R.id.cvFolllowCard);
            btnFollowState = itemView.findViewById(R.id.btnFollow);
        }

        public void bind(ParseUser user) {

            List<ParseUser> followingList = mainUser.getList("followers");


            followUsername.setText(user.getUsername());

            ParseFile profilePic = (ParseFile) user.get("profileImage");
            Glide.with(context).load(profilePic.getUrl()).circleCrop().into(followProfilePicture);

            if(list.contains(user.getObjectId())){
                btnFollowState.setText("unfollow");
                isFollowing = true;
            }
            else {
                btnFollowState.setText("follow");
                isFollowing = false;
            }

            cvFollows.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isFollowing) {
                        Intent intent = new Intent(context, OtherUserProfileActivity.class);
                        intent.putExtra("user", user);
                        context.startActivity(intent);
                    }

                }
            });

            btnFollowState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!isFollowing) {
                        btnFollowState.setText("unfollow");
                        isFollowing = true;
                        mainUser.add("following", user.getObjectId());
                        mainUser.saveInBackground();
                    }
                    else {
                        btnFollowState.setText("follow");
                        isFollowing = false;
                        mainUser.removeAll("following", Collections.singleton(user.getObjectId()));
                        mainUser.saveInBackground();

                    }

                }
            });


        }

    }
}
