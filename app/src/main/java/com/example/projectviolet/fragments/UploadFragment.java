package com.example.projectviolet.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.projectviolet.Post;
import com.example.projectviolet.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment {

    public static final String TAG = "UploadFragment.java: ";

    private Button btnUploadGallery;
    private ImageButton ibUpload;
    private EditText etYoutubeLink;
    private EditText etCaption;
    private Uri video;
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
        btnUploadGallery = view.findViewById(R.id.btnUploadFromGallery);

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


        btnUploadGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1234;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


                //handle video
                Uri uri = data.getData();
                Post newPost = new Post();
                newPost.setCaption("testing uploading files rectly");
                newPost.setUser(ParseUser.getCurrentUser());
                newPost.setVideo(uri);

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

}