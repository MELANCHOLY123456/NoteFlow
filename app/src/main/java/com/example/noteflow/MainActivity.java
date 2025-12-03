package com.example.noteflow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RecyclerView notesRecyclerView;
    private NoteAdapter noteAdapter;
    private TextView dateNumberTextView;
    private TextView dateInfoTextView;
    private TextView quoteTextView;
    private NoteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化数据库
        database = NoteDatabase.getInstance(this);

        // 初始化视图
        drawerLayout = findViewById(R.id.drawer_layout);
        notesRecyclerView = findViewById(R.id.notes_recycler_view);
        dateNumberTextView = findViewById(R.id.date_number);
        dateInfoTextView = findViewById(R.id.date_info);
        quoteTextView = findViewById(R.id.quote_text);
        ImageView menuButton = findViewById(R.id.menu_button);
        ImageView addArticleButton = findViewById(R.id.add_article_button);
        
        // 设置日期和名言
        updateDateDisplay();

        // 设置 RecyclerView
        setupNotesList();

        // 设置菜单按钮点击事件
        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(findViewById(R.id.drawer_menu)));

        // 设置新建文章按钮点击事件
        addArticleButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
            startActivity(intent);
        });

        
    }

    private void setupNotesList() {
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // 初始化适配器
        noteAdapter = new NoteAdapter(new java.util.ArrayList<>(), this);
        notesRecyclerView.setAdapter(noteAdapter);
        
        // 从数据库加载笔记
        loadNotesFromDatabase();
    }

    private void loadNotesFromDatabase() {
        // 在后台线程中执行数据库操作
        new Thread(() -> {
            List<NoteWithTags> notesWithTags = database.noteDao().getAllNotesWithTags();
            runOnUiThread(() -> noteAdapter.updateNotes(notesWithTags));
        }).start();
    }

    
    
    private void updateDateDisplay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // 月份从0开始，需要+1
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        
        // 设置日期数字
        dateNumberTextView.setText(String.valueOf(day));
        
        // 设置月份和星期信息
        String[] monthNames = {"", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
        String[] weekDays = {"", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        String monthName = monthNames[month];
        String weekDay = weekDays[dayOfWeek];
        String dateInfo = monthName + "月 | " + weekDay;
        dateInfoTextView.setText(dateInfo);
        
        // 设置名言
        String[] quotes = {
            getString(R.string.quote_of_the_day),
            "书山有路勤为径",
            "学而时习之，不亦说乎",
            "温故而知新",
            "知之为知之，不知为不知",
            "读书破万卷，下笔如有神",
            "宝剑锋从磨砺出，梅花香自苦寒来",
            "黑发不知勤学早，白首方悔读书迟",
            "少壮不努力，老大徒伤悲"
        };
        
        // 根据日期选择不同的名言
        int quoteIndex = (day - 1) % quotes.length;
        quoteTextView.setText(quotes[quoteIndex]);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadNotesFromDatabase(); // 每次返回主界面时重新加载笔记
    }
}