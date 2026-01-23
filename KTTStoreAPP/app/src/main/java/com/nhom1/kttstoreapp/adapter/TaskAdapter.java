package com.nhom1.kttstoreapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.model.Label;
import com.nhom1.kttstoreapp.model.Member;
import com.nhom1.kttstoreapp.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> tasks;
    private OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    public void setOnTaskClickListener(OnTaskClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task_card, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks != null ? tasks.size() : 0;
    }

    public void updateTasks(List<Task> newTasks) {
        this.tasks = newTasks;
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTaskTitle;
        private LinearLayout llLabels;
        private TextView tvMemberCount;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            llLabels = itemView.findViewById(R.id.llLabels);
            tvMemberCount = itemView.findViewById(R.id.tvMemberCount);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onTaskClick(tasks.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Task task) {
            tvTaskTitle.setText(task.getTitle());

            // Clear labels
            llLabels.removeAllViews();

            // Add labels
            if (task.getLabels() != null && !task.getLabels().isEmpty()) {
                for (Label label : task.getLabels()) {
                    TextView labelView = new TextView(context);
                    labelView.setText(label.getName());
                    labelView.setPadding(8, 4, 8, 4);
                    labelView.setTextSize(10);
                    labelView.setTextColor(0xFFFFFFFF);
                    try {
                        labelView.setBackgroundColor(android.graphics.Color.parseColor(label.getColor()));
                    } catch (Exception e) {
                        labelView.setBackgroundColor(0xFF2196F3);
                    }
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, 8, 0);
                    labelView.setLayoutParams(params);
                    llLabels.addView(labelView);
                }
            }

            // Show member count
            if (task.getMembers() != null && !task.getMembers().isEmpty()) {
                tvMemberCount.setText("👤 " + task.getMembers().size());
                tvMemberCount.setVisibility(View.VISIBLE);
            } else {
                tvMemberCount.setVisibility(View.GONE);
            }
        }
    }
}

