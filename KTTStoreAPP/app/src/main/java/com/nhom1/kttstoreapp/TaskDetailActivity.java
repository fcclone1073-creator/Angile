package com.nhom1.kttstoreapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nhom1.kttstoreapp.adapter.CommentAdapter;
import com.nhom1.kttstoreapp.model.Comment;
import com.nhom1.kttstoreapp.model.Member;
import com.nhom1.kttstoreapp.model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TaskDetailActivity extends AppCompatActivity {

    private MaterialButton btnStatus;
    private TextView tvTaskTitle;
    private MaterialButton btnEditDescription;
    private TextView tvDescription;
    private TextInputLayout tilDescriptionEdit;
    private TextInputEditText etDescription;
    private TextInputEditText etComment;
    private MaterialButton btnPostComment;
    private RecyclerView rvComments;

    private Task currentTask;
    private CommentAdapter commentAdapter;
    private List<String> statusOptions;
    private int currentStatusIndex = 0;
    private boolean isEditingDescription = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initializeViews();
        initializeStatusOptions();
        loadTaskData();
        setupListeners();
    }

    private void initializeViews() {
        btnStatus = findViewById(R.id.btnStatus);
        tvTaskTitle = findViewById(R.id.tvTaskTitle);
        btnEditDescription = findViewById(R.id.btnEditDescription);
        tvDescription = findViewById(R.id.tvDescription);
        tilDescriptionEdit = findViewById(R.id.tilDescriptionEdit);
        etDescription = findViewById(R.id.etDescription);
        etComment = findViewById(R.id.etComment);
        btnPostComment = findViewById(R.id.btnPostComment);
        rvComments = findViewById(R.id.rvComments);

        rvComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(this, new ArrayList<>());
        rvComments.setAdapter(commentAdapter);
    }

    private void initializeStatusOptions() {
        statusOptions = Arrays.asList("Cần làm", "Đang làm", "Hoàn thành");
    }

    private void loadTaskData() {
        // In a real app, load task from database/API using task_id from intent
        String taskId = getIntent().getStringExtra("task_id");
        
        // Sample task data (matching the image)
        currentTask = new Task("task1", "User View Home Page", 
                "• Hiển thị banner\n• Hiển thị danh mục sản phẩm\n• Hiển thị sản phẩm nổi bật\n• Giao diện thân thiện người dùng\n• Người làm: Dũng", 
                "Cần làm");

        Member member1 = new Member("member1", "Đỗ Duy Mạn", "man@example.com");
        Comment comment1 = new Comment("comment1", "Đã thêm task này vào cần làm", member1);
        comment1.setStatus("Cần làm");
        comment1.setCreatedAt(new Date(System.currentTimeMillis() - 86400000)); // Yesterday
        currentTask.getComments().add(comment1);

        displayTask();
    }

    private void displayTask() {
        tvTaskTitle.setText(currentTask.getTitle());
        tvDescription.setText(currentTask.getDescription());
        etDescription.setText(currentTask.getDescription());
        
        btnStatus.setText(currentTask.getStatus());
        currentStatusIndex = statusOptions.indexOf(currentTask.getStatus());
        if (currentStatusIndex == -1) currentStatusIndex = 0;

        updateStatusButtonColor(currentTask.getStatus());
        commentAdapter.updateComments(currentTask.getComments());
    }

    private void setupListeners() {
        btnStatus.setOnClickListener(v -> {
            currentStatusIndex = (currentStatusIndex + 1) % statusOptions.size();
            String newStatus = statusOptions.get(currentStatusIndex);
            btnStatus.setText(newStatus);
            currentTask.setStatus(newStatus);
            
            // Update status color
            updateStatusButtonColor(newStatus);
        });

        btnEditDescription.setOnClickListener(v -> {
            if (isEditingDescription) {
                // Save description
                String newDescription = etDescription.getText().toString();
                currentTask.setDescription(newDescription);
                tvDescription.setText(newDescription);
                tvDescription.setVisibility(View.VISIBLE);
                tilDescriptionEdit.setVisibility(View.GONE);
                btnEditDescription.setText("Chỉnh sửa");
                isEditingDescription = false;
            } else {
                // Edit description
                tvDescription.setVisibility(View.GONE);
                tilDescriptionEdit.setVisibility(View.VISIBLE);
                etDescription.setText(currentTask.getDescription());
                btnEditDescription.setText("Lưu");
                isEditingDescription = true;
            }
        });

        btnPostComment.setOnClickListener(v -> {
            String commentText = etComment.getText().toString().trim();
            if (!commentText.isEmpty()) {
                // Create new comment
                Member currentUser = new Member("current_user", "Người dùng hiện tại", "user@example.com");
                Comment newComment = new Comment();
                newComment.setContent(commentText);
                newComment.setAuthor(currentUser);
                newComment.setCreatedAt(new Date());

                currentTask.getComments().add(newComment);
                commentAdapter.updateComments(currentTask.getComments());
                
                etComment.setText("");
                Toast.makeText(this, "Đã thêm bình luận", Toast.LENGTH_SHORT).show();
            }
        });

        // Action buttons (Add, Label, Date, Checklist, Members)
        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng thêm", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnLabel).setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng nhãn", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnDate).setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng ngày", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnChecklist).setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng việc cần làm", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnMembers).setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng thành viên", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateStatusButtonColor(String status) {
        int color;
        switch (status) {
            case "Cần làm":
                color = 0xFF4CAF50; // Green
                break;
            case "Đang làm":
                color = 0xFFFF9800; // Orange
                break;
            case "Hoàn thành":
                color = 0xFF2196F3; // Blue
                break;
            default:
                color = 0xFF4CAF50;
        }
        btnStatus.setBackgroundColor(color);
    }
}

