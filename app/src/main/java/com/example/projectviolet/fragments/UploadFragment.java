package com.example.projectviolet.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectviolet.AttachGameTagActivity;
import com.example.projectviolet.models.Post;
import com.example.projectviolet.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class UploadFragment extends Fragment {

    public static final String TAG = "UploadFragment.java: ";

    private static final int LOCATION_REQUEST = 222;
    boolean gotPicture = false;
    boolean gotGameTag = false;
    private String realPath;
    private ParseFile videoFile = null;
    private String selectedGame;

    private Button btnUploadFromGallery;
    private Button btnChooseGame;
    private ProgressBar progressBar;
    private ImageButton ibUploadPost;
    private EditText etYoutubeLink;
    private EditText etCaption;
    private ImageView ivPreviewThumbnail;


    public UploadFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.pbUploadPost);
        ivPreviewThumbnail = view.findViewById(R.id.ivThumbnailPreview);
        etYoutubeLink = view.findViewById(R.id.etYoutubeLink);
        etCaption = view.findViewById(R.id.etCaption);
        btnUploadFromGallery = view.findViewById(R.id.btnUploadFromGallery);
        btnChooseGame = view.findViewById(R.id.btnApplyTag);
        ibUploadPost = view.findViewById(R.id.ibUploadClip);

        // Check user permissions
        checkLocationRequest();

        btnChooseGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameTagIntent = new Intent(getContext(), AttachGameTagActivity.class);
                final int ACTIVITY_SELECT_GAMETAG = 3;
                startActivityForResult(gameTagIntent, ACTIVITY_SELECT_GAMETAG);
            }
        });

        btnUploadFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
            }
        });

        ibUploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean YouTubeLinkEmpty = etYoutubeLink.getText().toString().isEmpty();

                if(gotPicture && gotGameTag) {
                    uploadMediaVideo(videoFile, realPath, selectedGame);
                }
                else if (!YouTubeLinkEmpty && gotGameTag){

                    String caption = etCaption.getText().toString();
                    String youtubeUrl = etYoutubeLink.getText().toString();
                    boolean validYoutubeUrl = isYoutubeUrl(youtubeUrl);
                    if(!validYoutubeUrl){
                        Toast.makeText(getContext(), "Not a valid YouTube link!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String youtubeThumbnailUrl = getYoutubeThumbnail(youtubeUrl);
                    extractYoutubeUrl(youtubeUrl, caption, selectedGame, youtubeThumbnailUrl);
                }
                else
                    Toast.makeText(getContext(), "You must upload a video and choose a tag", Toast.LENGTH_SHORT).show();
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

    private void uploadYoutubeVideo(String YoutubeUrl, String caption, String gameTag, String youtubeThumbnailUrl){

        if(etCaption.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Your post must have a caption", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(ProgressBar.VISIBLE);

        Post newPost = new Post();
        newPost.setGameTag(gameTag);
        newPost.setCaption(caption);
        newPost.setYoutubeLink(YoutubeUrl);
        newPost.put("videoThumbnailYoutube", youtubeThumbnailUrl);
        ParseUser user = ParseUser.getCurrentUser();
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.e(TAG, "error while saving post");
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(ProgressBar.GONE);
                Log.e(TAG, "Post was successful!!");
                resetFragment();
            }
        });

    }

    private void extractYoutubeUrl(String youtubeLink, String caption, String gameTag, String youtubeThumbnailUrl) {
        @SuppressLint("StaticFieldLeak") YouTubeExtractor mExtractor = new YouTubeExtractor(getContext()) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> sparseArray, VideoMeta videoMeta) {
                
                if (sparseArray != null) {
                    uploadYoutubeVideo(sparseArray.get(22).getUrl(), caption, gameTag, youtubeThumbnailUrl);
                }
            }
    };
    mExtractor.extract(youtubeLink, true, true);
}

    private String getYoutubeThumbnail(String youtubeLink){

        final String skeleton = "https://img.youtube.com/vi/%d/0.jpg";
        int startIndex = youtubeLink.lastIndexOf("=") + 1;
        String properID = youtubeLink.substring(startIndex);
        String finalString = skeleton.replace("%d", properID);
        return finalString;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        checkLocationRequest();

        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            Uri uri = data.getData();
            realPath = getRealPathFromUri(getContext(), uri);

            File inputFile = new File(realPath);

            try {

                FileInputStream fis = new FileInputStream(inputFile);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[(int) inputFile.length()];
                for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                    bos.write(buf, 0, readNum);
                }
                byte[] bytes = bos.toByteArray();
                // file name does not matter, caption is used to detail video
                videoFile = new ParseFile("video.mp4", bytes);

                Glide.with(getContext())
                        .asBitmap()
                        .load(Uri.fromFile(new File(realPath)))
                        .into(ivPreviewThumbnail);

                etYoutubeLink.setFocusable(false);
                etYoutubeLink.setText("");

                gotPicture = true;

            } catch (Exception IOException) {
                Log.e(TAG, "onActivityResult: ERROR UPLOADING VIDEO", IOException);
            }
        }
        else if (requestCode == 3 && resultCode == Activity.RESULT_OK){
            selectedGame = data.getStringExtra("chosenGame");
            Log.e(TAG, "onActivityResult: " + selectedGame );
            gotGameTag = true;
        }
        else
            Toast.makeText(getContext(), "Image not selected", Toast.LENGTH_SHORT).show();
    }

    public void uploadMediaVideo(ParseFile video, String realPath, String gameTag){

        progressBar.setVisibility(ProgressBar.VISIBLE);

        Post newPost = new Post();
        newPost.setGameTag(gameTag);
        newPost.setUser(ParseUser.getCurrentUser());
        newPost.setVideo(video);
        newPost.setCaption(etCaption.getText().toString());

        ParseFile videoThumbnail = getVideoThumb(realPath);
        newPost.setThumbnail(videoThumbnail);

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
                progressBar.setVisibility(ProgressBar.GONE);
                Log.e(TAG, "Post was successful!!");
                resetFragment();

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



    private void resetFragment(){
        // reset the fragments edit text and thumbnail
        gotPicture = false;
        gotGameTag = false;
        videoFile = null;
        etCaption.setText("");
        etYoutubeLink.setText("");
        etYoutubeLink.setFocusable(true);
        selectedGame = "";
        Glide.with(requireContext()).load(R.drawable.video_player_placeholder).into(ivPreviewThumbnail);
    }

    public ParseFile getVideoThumb(String path) {

        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        Bitmap thumbnail = media.getFrameAtTime();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        thumbnail.recycle();

        return new ParseFile("picture_1.png", byteArray);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(LOCATION_REQUEST)
    private void checkLocationRequest() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_MEDIA_LOCATION};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions((Activity) getContext(),"Please grant permission to access your gallery.",
                    LOCATION_REQUEST, perms);
        }
    }


}