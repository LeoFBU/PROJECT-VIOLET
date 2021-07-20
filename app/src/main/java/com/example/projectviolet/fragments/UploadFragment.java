package com.example.projectviolet.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectviolet.Post;
import com.example.projectviolet.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
    private String realPaths;
    private ImageView ivPreviewThumbnail;
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
        ivPreviewThumbnail = view.findViewById(R.id.ivThumbnailPreview);


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

                if(youtubeUrl.isEmpty()){

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
                final int ACTIVITY_SELECT_IMAGE = 1;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
                Parcelable e = i.getParcelableExtra("video_key");

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

        //TODO: Add request permissions to read/write on storage, right now this only works
        // if the user goes to their own settings and explicitly allows the app on their own.

        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            Uri uri = data.getData();
            String realPath = getRealPathFromUri(getContext(), uri);
            realPaths = getRealPathFromUri(getContext(), uri);

            File inputFile = new File(realPath);

            try {

                FileInputStream fis = new FileInputStream(inputFile);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[(int) inputFile.length()];
                for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                    bos.write(buf, 0, readNum);
                }
                byte[] bytes = bos.toByteArray();
                ParseFile file = new ParseFile("testVideo1.mp4", bytes);

                Glide.with(getContext())
                        .asBitmap()
                        .load(Uri.fromFile(new File(realPath)))
                        .into(ivPreviewThumbnail);

                etYoutubeLink.setKeyListener(null);
                etYoutubeLink.setFocusable(false);

                ibUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadMediaVideo(file, realPath);
                    }
                });

            } catch (Exception IOException) {
                Log.e(TAG, "onActivityResult: ERROR UPLOADING VIDEO", IOException);
            }
        }
        else
            Toast.makeText(getContext(), "Image not selected", Toast.LENGTH_SHORT).show();
    }

    public void uploadMediaVideo(ParseFile video, String realPath){

        Post newPost = new Post();
        newPost.setUser(ParseUser.getCurrentUser());
        newPost.setVideo(video);
        newPost.setCaption(etCaption.getText().toString());
        if(etCaption.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Your post must have a caption", Toast.LENGTH_SHORT).show();
            return;
        }

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.e(TAG, "error while saving post");
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }

                Log.e(TAG, "Post was successful!!");

                etCaption.setText("");
                etYoutubeLink.setFocusable(true);
                Glide.with(getContext())
                        .load(R.drawable.video_player_placeholder)
                        .into(ivPreviewThumbnail);

            }
        });

    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}