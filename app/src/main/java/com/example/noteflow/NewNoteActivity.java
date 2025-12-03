package com.example.noteflow;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewNoteActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText contentEditText;
    private ImageButton saveButton;
    private ImageButton backButton;
    private ImageButton addTagButton; // 添加标签按钮
    private LinearLayout tagsContainer; // 标签容器
    private LinearLayout tagsSection; // 标签区域

    private boolean isEditMode = false;
    private int noteId = -1;
    private NoteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        // 初始化数据库
        database = NoteDatabase.getInstance(this);

        initializeViews();
        getNoteDataFromIntent();
        setupViewsForEditMode();
        setupClickListeners();
        
        // 如果是编辑模式，加载现有标签
        if (isEditMode) {
            loadNoteTags();
        }
    }

    private void initializeViews() {
        titleEditText = findViewById(R.id.note_title);
        contentEditText = findViewById(R.id.note_content);
        saveButton = findViewById(R.id.save_note_button);
        backButton = findViewById(R.id.back_button);
        addTagButton = findViewById(R.id.add_tag_button); // 添加标签按钮
        tagsContainer = findViewById(R.id.tags_container); // 标签容器
        tagsSection = findViewById(R.id.tags_section); // 标签区域
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
        if (addTagButton != null) {
            addTagButton.setOnClickListener(v -> showAddTagDialog());
        }
    }

    private void loadNoteTags() {
        if (noteId == -1) return;
        
        new Thread(() -> {
            List<Tag> tags = database.noteDao().getTagsForNote(noteId);
            runOnUiThread(() -> {
                updateTagsView(tags);
            });
        }).start();
    }

    private void updateTagsView(List<Tag> tags) {
        tagsContainer.removeAllViews();
        
        if (tags != null && !tags.isEmpty()) {
            for (Tag tag : tags) {
                // 创建标签视图
                TextView tagView = new TextView(this);
                tagView.setText(tag.getTagName());
                tagView.setTextSize(12);
                tagView.setPadding(16, 8, 16, 8);
                
                // 设置背景为圆角矩形
                tagView.setBackground(createTagBackground());
                
                // 添加边距
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 8, 8);
                tagView.setLayoutParams(params);
                
                // 添加删除功能
                final Tag tagToRemove = tag;
                tagView.setOnClickListener(v -> showRemoveTagDialog(tagToRemove));
                
                // 添加到容器
                tagsContainer.addView(tagView);
            }
            // 显示标签区域
            tagsSection.setVisibility(View.VISIBLE);
        } else {
            // 隐藏标签区域
            tagsSection.setVisibility(View.GONE);
        }
    }
    
    // 创建标签背景
    private android.graphics.drawable.Drawable createTagBackground() {
        android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
        drawable.setColor(getResources().getColor(android.R.color.holo_blue_light));
        drawable.setCornerRadius(20f); // 设置圆角半径
        return drawable;
    }

    private void showAddTagDialog() {
        if (noteId == -1) {
            Toast.makeText(this, "请先保存文章", Toast.LENGTH_SHORT).show();
            return;
        }

        // 创建输入框
        EditText input = new EditText(this);
        input.setHint("输入标签名称");

        // 创建对话框
        new AlertDialog.Builder(this)
                .setTitle("添加标签")
                .setView(input)
                .setPositiveButton("确定", (dialog, which) -> {
                    String tagName = input.getText().toString().trim();
                    if (!TextUtils.isEmpty(tagName)) {
                        addTagToNote(tagName);
                    } else {
                        Toast.makeText(NewNoteActivity.this, "标签名称不能为空", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void addTagToNote(String tagName) {
        new Thread(() -> {
            try {
                // 检查标签是否已存在
                Tag existingTag = database.noteDao().getTagByName(tagName);
                Tag tag;
                if (existingTag == null) {
                    // 如果标签不存在，创建新标签
                    tag = new Tag(tagName);
                    long tagId = database.noteDao().insertTag(tag);
                    tag.setId((int) tagId);
                } else {
                    tag = existingTag;
                }

                // 检查笔记-标签关联是否已存在
                List<Tag> existingTags = database.noteDao().getTagsForNote(noteId);
                boolean tagAlreadyExists = false;
                for (Tag t : existingTags) {
                    if (t.getTagName().equals(tagName)) {
                        tagAlreadyExists = true;
                        break;
                    }
                }

                if (!tagAlreadyExists) {
                    // 创建笔记-标签关联
                    NoteTag noteTag = new NoteTag(noteId, tag.getId());
                    database.noteDao().insertNoteTag(noteTag);

                    // 更新UI
                    runOnUiThread(() -> {
                        Toast.makeText(NewNoteActivity.this, "标签添加成功", Toast.LENGTH_SHORT).show();
                        loadNoteTags(); // 重新加载标签
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(NewNoteActivity.this, "标签已存在", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(NewNoteActivity.this, "添加标签失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void showRemoveTagDialog(Tag tagToRemove) {
        new AlertDialog.Builder(this)
                .setTitle("删除标签")
                .setMessage("确定要删除标签 \"" + tagToRemove.getTagName() + "\" 吗？")
                .setPositiveButton("确定", (dialog, which) -> removeTagFromNote(tagToRemove))
                .setNegativeButton("取消", null)
                .show();
    }

    private void removeTagFromNote(Tag tag) {
        new Thread(() -> {
            try {
                // 使用新添加的方法直接删除特定的note-tag关联
                database.noteDao().deleteNoteTag(noteId, tag.getId());
                
                // 更新UI
                runOnUiThread(() -> {
                    Toast.makeText(NewNoteActivity.this, "标签删除成功", Toast.LENGTH_SHORT).show();
                    loadNoteTags(); // 重新加载标签
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(NewNoteActivity.this, "删除标签失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
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
            NoteDatabase db = NoteDatabase.getInstance(this);
            if (isEditMode) {
                db.noteDao().updateNote(note); // 更新笔记
                runOnUiThread(() -> {
                    Toast.makeText(NewNoteActivity.this, "文章修改成功", Toast.LENGTH_SHORT).show();
                    finish(); // 修改成功后返回
                });
            } else {
                db.noteDao().insertNote(note); // 插入新笔记
                runOnUiThread(() -> {
                    Toast.makeText(NewNoteActivity.this, "文章保存成功", Toast.LENGTH_SHORT).show();
                    finish(); // 保存成功后返回主页面
                });
            }
        }).start();
    }
}