package com.example.noteflow;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
        
        // 初始化适配器
        noteAdapter = new NoteAdapter(new java.util.ArrayList<>(), this);
        notesRecyclerView.setAdapter(noteAdapter);
        
        // 检查数据库是否为空，如果为空则添加示例数据
        checkAndAddSampleData();
        
        // 从数据库加载笔记
        loadNotesFromDatabase();
    }

    private void checkAndAddSampleData() {
        new Thread(() -> {
            // 删除所有现有笔记并添加新的示例数据
            // 这样可以确保数据格式是最新的
            database.noteDao().deleteAllNotes(); // 我们需要在DAO中添加这个方法
            addSampleNotesToDatabase();
        }).start();
    }

    private void addSampleNotesToDatabase() {
        // 创建计算机主题的示例笔记
        Note note1 = new Note(
            getString(R.string.first_article_title),
            generateFullContent(getString(R.string.first_article_preview)),
            "2025-01-15"
        );

        Note note2 = new Note(
            getString(R.string.learning_notes_title),
            generateFullContent(getString(R.string.learning_notes_preview)),
            "2025-01-14"
        );

        Note note3 = new Note(
            getString(R.string.life_reflection_title),
            generateFullContent(getString(R.string.life_reflection_preview)),
            "2025-01-13"
        );

        Note note4 = new Note(
            getString(R.string.tech_share_title),
            generateFullContent(getString(R.string.tech_share_preview)),
            "2025-01-12"
        );

        Note note5 = new Note(
            getString(R.string.travel_diary_title),
            generateFullContent(getString(R.string.travel_diary_preview)),
            "2025-01-11"
        );

        new Thread(() -> {
            database.noteDao().insertNote(note1);
            database.noteDao().insertNote(note2);
            database.noteDao().insertNote(note3);
            database.noteDao().insertNote(note4);
            database.noteDao().insertNote(note5);
            
            // 重新加载数据
            runOnUiThread(this::loadNotesFromDatabase);
        }).start();
    }

    // 生成完整的文章内容
    private String generateFullContent(String preview) {
        if (preview.contains("编译器")) {
            return "编译器是将高级语言程序转换为机器语言程序的系统软件。编译器的设计是计算机科学中的重要课题，其核心任务是将源程序转换为等价的目标程序。\n\n" +
            "编译器通常分为前端和后端两个部分。前端主要负责词法分析、语法分析和语义分析，检查源程序的正确性并生成中间表示。后端则负责代码优化和目标代码生成，将中间表示转换为特定目标机器的代码。\n\n" +
            "词法分析是编译过程的第一步，它将源程序的字符序列转换为有意义的记号序列。语法分析则根据语言的语法规则，将记号序列组织成语法结构，通常表示为抽象语法树（AST）。语义分析负责检查程序的静态语义，如类型检查、作用域分析等。\n\n" +
            "现代编译器还包含复杂的优化技术，包括局部优化、全局优化、循环优化等，以提高生成代码的执行效率。编译器优化技术是计算机系统性能提升的关键因素之一。";
        } else if (preview.contains("MIPS")) {
            return "MIPS（Microprocessor without Interlocked Pipeline Stages）是一种经典的精简指令集计算机（RISC）架构。它采用流水线技术，通过将指令执行分解为多个阶段来提高处理器性能。\n\n" +
            "MIPS架构的特点包括：固定长度的32位指令格式、大量的寄存器（32个通用寄存器）、简单的寻址方式以及load-store架构（只有load和store指令可以访问内存）。这种设计使得MIPS处理器结构简单，易于流水线化。\n\n" +
            "MIPS指令分为三类：R型（寄存器型）、I型（立即数型）和J型（跳转型）。R型指令用于寄存器间的运算，I型指令用于立即数操作和内存访问，J型指令用于无条件跳转。\n\n" +
            "MIPS架构在计算机体系结构教学和研究中被广泛使用，因为它清晰地展示了计算机组成原理和流水线技术的核心概念。";
        } else if (preview.contains("并行")) {
            return "指令级并行（Instruction-Level Parallelism, ILP）是指在程序执行过程中，同时执行多条指令的技术。ILP是现代高性能处理器提高性能的主要手段之一。\n\n" +
            "实现ILP的主要技术包括超标量（Superscalar）和超流水线（Superpipelining）。超标量技术允许处理器在单个时钟周期内发射多条指令到不同的功能单元执行，而超流水线技术则通过增加流水线级数来提高时钟频率。\n\n" +
            "分支预测是ILP实现中的关键技术，因为分支指令会破坏指令流的连续性。现代处理器使用各种分支预测算法，如静态预测、动态预测、分支目标缓冲等，以减少分支惩罚。\n\n" +
            "乱序执行（Out-of-Order Execution）是另一种重要的ILP技术，它允许处理器在满足数据依赖关系的前提下，以不同于程序顺序的方式执行指令，从而提高指令执行的并行度。";
        } else if (preview.contains("内存")) {
            return "操作系统的内存管理是计算机系统中的核心功能之一，负责管理计算机的主存储器资源，为进程提供虚拟内存空间。\n\n" +
            "现代操作系统的内存管理主要包括分页和分段两种机制。分页将内存和进程地址空间都划分为固定大小的页，通过页表实现虚拟地址到物理地址的映射。分段则根据程序的逻辑结构将地址空间划分为不同大小的段。\n\n" +
            "虚拟内存技术允许进程使用比物理内存更大的地址空间。当物理内存不足时，操作系统会将不常用的页面换出到磁盘，并在需要时再换入内存。这种技术大大提高了内存利用率。\n\n" +
            "页面置换算法是内存管理的关键，常见的算法包括先进先出（FIFO）、最近最少使用（LRU）、时钟算法等。这些算法的目标是尽可能减少页面错误率，提高系统性能。";
        } else if (preview.contains("协议栈")) {
            return "计算机网络协议栈是实现网络通信的基础，TCP/IP协议栈是目前最广泛使用的网络通信模型。它将复杂的网络通信过程分解为不同层次，每一层负责特定的功能。\n\n" +
            "网络层负责数据包的路由和转发，IP协议是网络层的核心协议。传输层提供端到端的通信服务，TCP协议提供可靠的面向连接的服务，UDP协议提供不可靠的无连接服务。应用层包含各种应用协议，如HTTP、FTP、SMTP等。\n\n" +
            "网络协议栈的设计遵循分层原则，每一层只与相邻层交互，这种设计提高了系统的模块化程度和可维护性。下层为上层提供服务，上层无需关心下层的具体实现。\n\n" +
            "网络协议分析是网络性能优化和故障诊断的重要手段。通过分析各层协议的性能指标和数据包内容，可以识别网络瓶颈和优化方案。";
        } else {
            return "这是文章的完整内容：\n\n" +
                   preview + "\n\n" +
                   "在实际应用中，这里将显示文章的全部内容，" +
                   "包括更多的段落和详细的描述。您可以在这里添加" +
                   "任意长度的文本内容，支持段落格式和换行。" +
                   "\n\n文章的更多内容可以继续扩展，添加更多的细节描述。";
        }
    }

    private void loadNotesFromDatabase() {
        // 在后台线程中执行数据库操作
        new Thread(() -> {
            List<Note> notes = database.noteDao().getAllNotes();
            runOnUiThread(() -> {
                noteAdapter.updateNotes(notes);
            });
        }).start();
    }

    private void setupDrawerButtons() {
        Button btnCategory = findViewById(R.id.btn_category);
        Button btnDeleteArticle = findViewById(R.id.btn_delete_article);

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "文章分类", Toast.LENGTH_SHORT).show();
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
}