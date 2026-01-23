package com.nhom1.kttstoreapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.TaskDetailActivity;
import com.nhom1.kttstoreapp.adapter.ColumnAdapter;
import com.nhom1.kttstoreapp.adapter.TaskAdapter;
import com.nhom1.kttstoreapp.model.Board;
import com.nhom1.kttstoreapp.model.Comment;
import com.nhom1.kttstoreapp.model.Label;
import com.nhom1.kttstoreapp.model.Member;
import com.nhom1.kttstoreapp.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BoardFragment extends Fragment {

    private RecyclerView rvBoardColumns;
    private ColumnAdapter columnAdapter;
    private Board board;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        rvBoardColumns = view.findViewById(R.id.rvBoardColumns);
        rvBoardColumns.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Initialize board with sample data
        initializeBoard();

        columnAdapter = new ColumnAdapter(getContext(), board);
        columnAdapter.setTaskClickListener(task -> {
            // Open task detail activity
            Intent intent = new Intent(getContext(), TaskDetailActivity.class);
            intent.putExtra("task_id", task.getId());
            startActivity(intent);
        });

        rvBoardColumns.setAdapter(columnAdapter);

        return view;
    }

    private void initializeBoard() {
        board = new Board("board1", "Project Board");

        // Create sample members
        Member member1 = new Member("member1", "Đỗ Duy Mạn", "man@example.com");
        Member member2 = new Member("member2", "Dũng", "dung@example.com");

        // Create sample labels
        Label label1 = new Label("label1", "Frontend", "#2196F3");
        Label label2 = new Label("label2", "Backend", "#4CAF50");

        // Create sample tasks
        Task task1 = new Task("task1", "User View Home Page", 
                "• Hiển thị banner\n• Hiển thị danh mục sản phẩm\n• Hiển thị sản phẩm nổi bật\n• Giao diện thân thiện người dùng\n• Người làm: Dũng", 
                "Cần làm");
        task1.getMembers().add(member2);
        task1.getLabels().add(label1);

        Comment comment1 = new Comment("comment1", "Đã thêm task này vào cần làm", member1);
        comment1.setStatus("Cần làm");
        task1.getComments().add(comment1);

        Task task2 = new Task("task2", "User Manage Cart", 
                "• Thêm sản phẩm vào giỏ\n• Xóa sản phẩm\n• Cập nhật số lượng", 
                "Cần làm");
        task2.getMembers().add(member1);
        task2.getLabels().add(label1);
        task2.getLabels().add(label2);

        Task task3 = new Task("task3", "API Authentication", 
                "• JWT token\n• Refresh token\n• Login/Logout", 
                "Đang làm");
        task3.getMembers().add(member2);
        task3.getLabels().add(label2);

        board.getTasks().add(task1);
        board.getTasks().add(task2);
        board.getTasks().add(task3);
    }
}

