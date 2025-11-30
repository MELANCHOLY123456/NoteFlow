package com.example.noteflow;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewNoteActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText contentEditText;
    private ImageButton saveButton;
    private ImageButton backButton;

    private boolean isEditMode = false;
    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        initializeViews();
        getNoteDataFromIntent();
        setupViewsForEditMode();
        setupClickListeners();
    }

    private void initializeViews() {
        titleEditText = findViewById(R.id.note_title);
        contentEditText = findViewById(R.id.note_content);
        saveButton = findViewById(R.id.save_note_button);
        backButton = findViewById(R.id.back_button);
    }

    private void getNoteDataFromIntent() {
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("editMode", false);
        noteId = intent.getIntExtra("id", -1);
        
        if (isEditMode) {
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            
            titleEditText.setText(title);
            contentEditText.setText(content);
        }
    }

    private void setupViewsForEditMode() {
        if (isEditMode) {
            // 更改保存按钮图标为保存图标
            saveButton.setImageResource(R.drawable.save);
        }
    }

    private void setupClickListeners() {
        saveButton.setOnClickListener(v -> saveNote());
        backButton.setOnClickListener(v -> finish());
    }

    private void saveNote() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "请输入文章标题", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入文章内容", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取当前时间戳
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // 创建笔记对象
        Note note = new Note(title, content, timestamp);
        
        if (isEditMode) {
            note.setId(noteId); // 设置ID以便更新
        }

        // 在后台线程中保存到数据库
        new Thread(() -> {
            NoteDatabase database = NoteDatabase.getInstance(this);
            if (isEditMode) {
                database.noteDao().updateNote(note); // 更新笔记
                runOnUiThread(() -> {
                    Toast.makeText(NewNoteActivity.this, "文章修改成功", Toast.LENGTH_SHORT).show();
                    finish(); // 修改成功后返回
                });
            } else {
                database.noteDao().insertNote(note); // 插入新笔记
                runOnUiThread(() -> {
                    Toast.makeText(NewNoteActivity.this, "文章保存成功", Toast.LENGTH_SHORT).show();
                    finish(); // 保存成功后返回主页面
                });
            }
        }).start();
    }
}