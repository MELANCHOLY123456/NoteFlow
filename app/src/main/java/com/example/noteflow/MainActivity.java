package com.example.noteflow;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RecyclerView notesRecyclerView;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        drawerLayout = findViewById(R.id.drawer_layout);
        notesRecyclerView = findViewById(R.id.notes_recycler_view);
        ImageView menuButton = findViewById(R.id.menu_button);
        ImageView addArticleButton = findViewById(R.id.add_article_button);

        // 设置 RecyclerView
        setupNotesList();

        // 设置菜单按钮点击事件
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(findViewById(R.id.drawer_menu));
            }
        });

        // 设置新建文章按钮点击事件
        addArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "新建文章", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置侧滑菜单按钮点击事件
        setupDrawerButtons();
    }

    private void setupNotesList() {
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 创建一些示例数据
        noteAdapter = new NoteAdapter(NoteAdapter.createSampleNotes(this));
        notesRecyclerView.setAdapter(noteAdapter);
    }

    private void setupDrawerButtons() {
        Button btnCategory = findViewById(R.id.btn_category);
        Button btnNewArticle = findViewById(R.id.btn_new_article);
        Button btnDeleteArticle = findViewById(R.id.btn_delete_article);

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "文章分类", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
            }
        });

        btnNewArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "新建文章", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
            }
        });

        btnDeleteArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "删除文章", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
            }
        });
    }
}