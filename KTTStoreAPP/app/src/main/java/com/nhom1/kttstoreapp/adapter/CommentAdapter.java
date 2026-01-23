package com.nhom1.kttstoreapp.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.model.Comment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    public void updateComments(List<Comment> newComments) {
        this.comments = newComments;
        notifyDataSetChanged();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAuthorName;
        private TextView tvCommentContent;
        private TextView tvStatus;
        private TextView tvTimestamp;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            tvCommentContent = itemView.findViewById(R.id.tvCommentContent);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }

        public void bind(Comment comment) {
            if (comment.getAuthor() != null) {
                tvAuthorName.setText(comment.getAuthor().getName());
            }

            tvCommentContent.setText(comment.getContent());

            if (comment.getStatus() != null && !comment.getStatus().isEmpty()) {
                tvStatus.setText(comment.getStatus());
                tvStatus.setVisibility(View.VISIBLE);
            } else {
                tvStatus.setVisibility(View.GONE);
            }

            // Format timestamp
            if (comment.getCreatedAt() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd 'thg' M, yyyy", new Locale("vi", "VN"));
                tvTimestamp.setText(sdf.format(comment.getCreatedAt()));
            }
        }
    }
}

