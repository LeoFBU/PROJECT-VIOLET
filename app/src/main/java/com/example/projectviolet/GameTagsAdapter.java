package com.example.projectviolet;

import android.content.Context;
import android.util.Log;
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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GameTagsAdapter extends RecyclerView.Adapter<GameTagsAdapter.ViewHolderTags> {

    public static final String TAG = "GameTagsAdapter";
    private ArrayList<GameTag> gameTagsList;
    private Context context;
    private ArrayList<String> myRealList;



    public GameTagsAdapter(Context context, ArrayList<GameTag> gameTagsList, ArrayList<String> testlol){
        this.context = context;
        this.gameTagsList = gameTagsList;
        this.myRealList = testlol;
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

    public ArrayList<String> getArrayList(){
        return myRealList;
    }

    public class ViewHolderTags extends RecyclerView.ViewHolder {

        private TextView tvGameName;
        private ImageView ivGameIcon;
        private CheckBox cbSubscribe;

        public ViewHolderTags(@NonNull View itemView) {
            super(itemView);

            tvGameName = itemView.findViewById(R.id.tvGameTagName);
            ivGameIcon = itemView.findViewById(R.id.ivGameTagIcon);
            cbSubscribe = itemView.findViewById(R.id.cbSubscribe);
        }

        public void bind(GameTag gameTag) {

            if(!gameTag.isChecked()){
                cbSubscribe.setChecked(false);
            }
            else
                cbSubscribe.setChecked(true);

            tvGameName.setText(gameTag.getGameName());
            ParseFile gameIcon = gameTag.getGameIcon();
            Glide.with(context).load(gameIcon.getUrl()).transform(new RoundedCorners(20)).into(ivGameIcon);

            ParseUser user = ParseUser.getCurrentUser();
            user.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {



                    cbSubscribe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(cbSubscribe.isChecked()){
                                cbSubscribe.setChecked(true);
                                gameTag.setChecked(true);
                                myRealList.add(gameTag.getGameName());
                                //Log.e(TAG, "onCheck: " + gameTag.getGameName());

//                                user.add("prefferedTags", gameTag.getGameName());
//                                user.saveInBackground(new SaveCallback() {
//                                    @Override
//                                    public void done(ParseException e) {
//                                        if(e!=null){
//                                            Log.e(TAG, "ERROR SAVING " + gameTag.getGameName(), e);
//                                        }
//
//                                        Log.e(TAG, "done saving: "+ gameTag.getGameName(),e);
//                                    }
//
//                                });

                            }


                        }
                    });

                }
            });




        }


    }
}
