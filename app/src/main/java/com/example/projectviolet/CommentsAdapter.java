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
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolderComments> {
    
    public static final String TAG = "CommentsAdapter";
    private ArrayList<Comment> comments;
    private Context context;

    public CommentsAdapter(Context context, ArrayList<Comment> comments){
        this.context = context;
        this.comments = comments;
    } 
    
    @NotNull
    @Override
    public ViewHolderComments onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        return new ViewHolderComments(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderComments holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolderComments extends RecyclerView.ViewHolder {

        TextView tvCommentContent;
        TextView tvCommentTimestamp;
        TextView tvCommentUsername;
        ImageView ivCommentUserPFP;

        public ViewHolderComments( @NotNull View itemView) {
            super(itemView);

            tvCommentContent = itemView.findViewById(R.id.tvUserCommentContent);
            tvCommentTimestamp = itemView.findViewById(R.id.tvUserCommentTimestamp);
            tvCommentUsername = itemView.findViewById(R.id.tvUserCommentUsername);
            ivCommentUserPFP = itemView.findViewById(R.id.ivUserCommentPFP);

        }

        public void bind(Comment comment) {

            tvCommentContent.setText(comment.getCommentContent());
            tvCommentUsername.setText(comment.getCommentUser().getUsername());
            tvCommentTimestamp.setText(comment.getCommentTimestamp(comment.getCreatedAt()));
            ParseFile commentProfileImage = comment.getCommentUser().getParseFile("profileImage");
            Glide.with(context).load(commentProfileImage.getUrl()).circleCrop().into(ivCommentUserPFP);


        }
    }
}
