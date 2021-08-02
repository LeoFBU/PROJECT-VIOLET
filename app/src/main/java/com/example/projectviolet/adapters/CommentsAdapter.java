package com.example.projectviolet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectviolet.models.Comment;
import com.example.projectviolet.R;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolderComments> {
    
    public static final String TAG = "CommentsAdapter";
    private ArrayList<Comment> commentList;
    private Context context;

    public CommentsAdapter(Context context, ArrayList<Comment> commentList){
        this.context = context;
        this.commentList = commentList;
    } 
    
    @NotNull
    @Override
    public ViewHolderComments onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        return new ViewHolderComments(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderComments holder, int position) {
        Comment comment = commentList.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
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
            tvCommentUsername.setText(comment.getCommentCreatorUsername());
            tvCommentTimestamp.setText(comment.getCommentTimestamp());
            ParseFile commentProfileImage = comment.getCommentProfilePic();
            Glide.with(context).load(commentProfileImage.getUrl()).circleCrop().into(ivCommentUserPFP);

        }
    }
}
