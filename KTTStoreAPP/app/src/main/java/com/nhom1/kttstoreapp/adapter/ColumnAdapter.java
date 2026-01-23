package com.nhom1.kttstoreapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.model.Board;
import com.nhom1.kttstoreapp.model.Task;

import java.util.List;

public class ColumnAdapter extends RecyclerView.Adapter<ColumnAdapter.ColumnViewHolder> {

    private Context context;
    private Board board;
    private TaskAdapter.OnTaskClickListener taskClickListener;

    public ColumnAdapter(Context context, Board board) {
        this.context = context;
        this.board = board;
    }

    public void setTaskClickListener(TaskAdapter.OnTaskClickListener listener) {
        this.taskClickListener = listener;
    }

    @NonNull
    @Override
    public ColumnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_column, parent, false);
        return new ColumnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColumnViewHolder holder, int position) {
        String columnStatus = board.getColumns().get(position);
        List<Task> tasksForColumn = board.getTasksByStatus(columnStatus);
        holder.bind(columnStatus, tasksForColumn);
    }

    @Override
    public int getItemCount() {
        return board != null && board.getColumns() != null ? board.getColumns().size() : 0;
    }

    public void updateBoard(Board newBoard) {
        this.board = newBoard;
        notifyDataSetChanged();
    }

    class ColumnViewHolder extends RecyclerView.ViewHolder {
        private TextView tvColumnTitle;
        private RecyclerView rvTasks;
        private TaskAdapter taskAdapter;

        public ColumnViewHolder(@NonNull View itemView) {
            super(itemView);
            tvColumnTitle = itemView.findViewById(R.id.tvColumnTitle);
            rvTasks = itemView.findViewById(R.id.rvTasks);
            rvTasks.setLayoutManager(new LinearLayoutManager(context));
            taskAdapter = new TaskAdapter(context, null);
            rvTasks.setAdapter(taskAdapter);
        }

        public void bind(String columnStatus, List<Task> tasks) {
            tvColumnTitle.setText(columnStatus);
            taskAdapter.updateTasks(tasks);
            taskAdapter.setOnTaskClickListener(taskClickListener);
        }
    }
}

