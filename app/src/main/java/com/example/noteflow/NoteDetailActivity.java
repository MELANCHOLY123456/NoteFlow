package com.example.noteflow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NoteDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        // 初始化视图
        ImageButton backButton = findViewById(R.id.back_button);
        TextView articleTitle = findViewById(R.id.article_title);
        TextView articleTimestamp = findViewById(R.id.article_timestamp);
        TextView articleContent = findViewById(R.id.article_content);

        // 从 Intent 获取数据
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String timestamp = intent.getStringExtra("timestamp");
        String content = intent.getStringExtra("content");

        // 设置数据到视图
        if (title != null) {
            articleTitle.setText(title);
        }
        if (timestamp != null) {
            articleTimestamp.setText(timestamp);
        }
        if (content != null) {
            articleContent.setText(content);
        } else {
            // 如果没有传入内容，提供一个默认内容
            articleContent.setText("这里是 " + (title != null ? title : "这篇文章") + " 的详细内容。");
        }

        // 设置返回按钮点击事件
        backButton.setOnClickListener(v -> finish());
    }
}