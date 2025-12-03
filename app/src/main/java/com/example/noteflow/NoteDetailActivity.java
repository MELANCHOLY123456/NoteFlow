package com.example.noteflow;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Objects;

public class NoteDetailActivity extends AppCompatActivity {

    private NoteDatabase database;
    private int noteId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        // 初始化数据库
        database = NoteDatabase.getInstance(this);

        // 初始化视图
        ImageButton backButton = findViewById(R.id.back_button);
        ImageButton editButton = findViewById(R.id.edit_note_button);
        ImageButton addTagButton = findViewById(R.id.add_tag_button); // 添加标签按钮
        ImageButton deleteButton = findViewById(R.id.delete_note_button); // 删除按钮
        TextView articleTitle = findViewById(R.id.article_title);
        TextView articleTimestamp = findViewById(R.id.article_timestamp);
        TextView articleContent = findViewById(R.id.article_content);
        LinearLayout tagsContainer = findViewById(R.id.tags_container); // 标签容器

        // 从 Intent 获取数据
        android.content.Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String timestamp = intent.getStringExtra("timestamp");
        String content = intent.getStringExtra("content");
        noteId = intent.getIntExtra("id", -1); // 获取笔记ID

        // 设置数据到视图
        if (title != null) {
            articleTitle.setText(title);
        }
        if (timestamp != null) {
            articleTimestamp.setText(timestamp);
        }

        // 如果没有传入内容，提供一个默认内容
        articleContent.setText(Objects.requireNonNullElseGet(content, () -> "这里是 " + (title != null ? title : "这篇文章") + " 的详细内容。"));

        // 加载并显示标签
        if (tagsContainer != null) {
            loadAndDisplayTags(tagsContainer);
        }

        // 设置返回按钮点击事件
        backButton.setOnClickListener(v -> finish());

        // 设置编辑按钮点击事件
        editButton.setOnClickListener(v -> {
            // 跳转到编辑笔记界面
            android.content.Intent editIntent = new android.content.Intent(NoteDetailActivity.this, NewNoteActivity.class);
            editIntent.putExtra("id", noteId); // 传递笔记ID用于更新
            editIntent.putExtra("title", title);
            editIntent.putExtra("content", content);
            editIntent.putExtra("editMode", true); // 标识是编辑模式
            startActivity(editIntent);
            // 不调用finish()，保持详情页在任务栈中，以便编辑完成后返回到详情页
        });

        // 设置添加标签按钮点击事件
        if (addTagButton != null) {
            addTagButton.setOnClickListener(v -> showAddTagDialog());
        }

        // 设置删除按钮点击事件
        if (deleteButton != null) {
            deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());
        }
    }

    private void loadAndDisplayTags(LinearLayout tagsContainer) {
        if (noteId == -1) return;

        new Thread(() -> {
            List<Tag> tags = database.noteDao().getTagsForNote(noteId);
            runOnUiThread(() -> {
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
                        
                        // 不添加点击事件，只用于展示标签
                        // 添加到容器
                        tagsContainer.addView(tagView);
                    }
                    
                    // 显示标签容器
                    tagsContainer.setVisibility(View.VISIBLE);
                } else {
                    // 隐藏标签容器
                    tagsContainer.setVisibility(View.GONE);
                }
            });
        }).start();
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
            Toast.makeText(this, "笔记ID无效", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(NoteDetailActivity.this, "标签名称不能为空", Toast.LENGTH_SHORT).show();
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

                    runOnUiThread(() -> {
                        Toast.makeText(NoteDetailActivity.this, "标签添加成功", Toast.LENGTH_SHORT).show();
                        // 重新加载并显示标签
                        LinearLayout tagsContainer = findViewById(R.id.tags_container);
                        if (tagsContainer != null) {
                            loadAndDisplayTags(tagsContainer);
                        }
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(NoteDetailActivity.this, "标签已存在", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(NoteDetailActivity.this, "添加标签失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    
    // 显示删除确认对话框
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("删除文章")
                .setMessage("确定要删除这篇文章吗？此操作不可撤销。")
                .setPositiveButton("确定", (dialog, which) -> deleteNote())
                .setNegativeButton("取消", null)
                .show();
    }
    
    // 删除笔记
    private void deleteNote() {
        if (noteId == -1) {
            Toast.makeText(this, "笔记ID无效", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                // 删除笔记与标签的关联
                database.noteDao().deleteNoteTagsForNote(noteId);
                
                // 删除笔记本身
                Note note = database.noteDao().getNoteById(noteId);
                if (note != null) {
                    database.noteDao().deleteNote(note);
                    
                    runOnUiThread(() -> {
                        Toast.makeText(NoteDetailActivity.this, "文章删除成功", Toast.LENGTH_SHORT).show();
                        finish(); // 返回到主页面
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(NoteDetailActivity.this, "笔记不存在", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(NoteDetailActivity.this, "删除失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // 每次回到详情页时都刷新数据，以反映可能的更改
        refreshNoteData();
    }
    
    // 刷新笔记数据
    private void refreshNoteData() {
        if (noteId != -1) {
            new Thread(() -> {
                Note note = database.noteDao().getNoteById(noteId);
                if (note != null) {
                    runOnUiThread(() -> {
                        // 更新UI
                        TextView articleTitle = findViewById(R.id.article_title);
                        TextView articleTimestamp = findViewById(R.id.article_timestamp);
                        TextView articleContent = findViewById(R.id.article_content);
                        
                        if (articleTitle != null) {
                            articleTitle.setText(note.getTitle());
                        }
                        if (articleTimestamp != null) {
                            articleTimestamp.setText(note.getTimestamp());
                        }
                        if (articleContent != null) {
                            articleContent.setText(note.getContent());
                        }
                        
                        // 重新加载标签
                        LinearLayout tagsContainer = findViewById(R.id.tags_container);
                        if (tagsContainer != null) {
                            loadAndDisplayTags(tagsContainer);
                        }
                    });
                }
            }).start();
        }
    }
}