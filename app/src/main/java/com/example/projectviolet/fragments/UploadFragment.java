package com.example.projectviolet.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.projectviolet.Post;
import com.example.projectviolet.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment {

    public static final String TAG = "UploadFragment.java: ";

    private ImageButton ibUpload;
    private EditText etYoutubeLink;
    private EditText etCaption;

    public UploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ibUpload = view.findViewById(R.id.ibUploadClip);
        etYoutubeLink = view.findViewById(R.id.etYoutubeLink);
        etCaption = view.findViewById(R.id.etCaption);

        ibUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption = etCaption.getText().toString();
                String youtubeUrl = etYoutubeLink.getText().toString();

                if(caption.isEmpty()){
                    Toast.makeText(getContext(), "Your post must have a caption", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean validYoutubeUrl = isYoutubeUrl(youtubeUrl);
                if(!validYoutubeUrl){
                    Toast.makeText(getContext(), "Not a valid YouTube link!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                //savePost(youtubeUrl, caption, currentUser);
                extractYoutubeUrl(youtubeUrl, caption, currentUser);



            }
        });

    }

    public static boolean isYoutubeUrl(String youTubeURl)
    {
        boolean success;
        String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        if (!youTubeURl.isEmpty() && youTubeURl.matches(pattern))
        {
            success = true;
        }
        else
        {
            // Not Valid youtube URL
            success = false;
        }
        return success;
    }

    private void savePost(String YoutubeUrl, String caption, ParseUser currentUser){


        Post newPost = new Post();
        newPost.setCaption(caption);
        newPost.setYoutubeLink(YoutubeUrl);
        newPost.setUser(currentUser);


        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.e(TAG, "error while saving post");
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.e(TAG, "Post was successful!!");
                etCaption.setText("");
            }
        });

    }


//    @SuppressLint("StaticFieldLeak")
//    private void extractYoutubeUrl(String youtubeLink, String caption, ParseUser currentUser) {
//        new YouTubeExtractor(getContext()) {
//            @Override
//            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
//                if (ytFiles != null) {
//                    //playVideo(ytFiles.get(17).getUrl());
//                    savePost(ytFiles.get(17).getUrl(), caption, currentUser);
//                }
//            }
//        }.extract(youtubeLink, true, true);
//    }
    private void extractYoutubeUrl(String youtubeLink, String caption, ParseUser user) {
        @SuppressLint("StaticFieldLeak") YouTubeExtractor mExtractor = new YouTubeExtractor(getContext()) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> sparseArray, VideoMeta videoMeta) {
                
                if (sparseArray != null) {
                    savePost(sparseArray.get(22).getUrl(), caption, user);
                }
            }
    };
    mExtractor.extract(youtubeLink, true, true);
}

}