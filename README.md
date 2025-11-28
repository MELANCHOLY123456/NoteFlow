# NoteFlow - 笔记应用

NoteFlow 是一款简洁美观的 Android 笔记应用，专为高效记录和管理个人笔记而设计。应用采用现代化的 UI 设计，提供流畅的用户体验。

## 功能特性

- **笔记管理**：创建、查看、编辑和删除笔记
- **时间戳记录**：自动为每条笔记添加时间戳
- **侧滑菜单**：便捷的分类和删除功能
- **每日名言**：主页面显示每日名言，增加使用乐趣
- **本地数据库**：使用 Room 数据库存储笔记，保证数据安全
- **响应式界面**：适配不同屏幕尺寸的设备

## 技术栈

- **编程语言**：Java
- **数据库**：Room (SQLite ORM)
- **UI 框架**：Android SDK
- **架构模式**：MVC (Model-View-Controller)
- **构建工具**：Gradle
- **依赖注入**：使用 AndroidX 库

## 项目结构

```
NoteFlow/
├── app/                    # 应用主模块
│   ├── src/main/java/      # Java 源代码
│   │   └── com/example/noteflow/
│   │       ├── MainActivity.java      # 主页面
│   │       ├── NoteDetailActivity.java # 笔记详情页
│   │       ├── Note.java              # 笔记数据模型
│   │       ├── NoteDatabase.java      # Room 数据库
│   │       ├── NoteDao.java           # 数据访问对象
│   │       └── NoteAdapter.java       # RecyclerView 适配器
│   ├── src/main/res/       # 资源文件
│   │   ├── layout/         # 布局文件
│   │   └── values/         # 字符串等资源
│   └── build.gradle.kts    # 模块构建配置
├── build.gradle.kts        # 项目级构建配置
└── settings.gradle.kts     # 模块配置
```

## 部署说明

### 环境要求

- Android Studio (建议使用最新版本)
- Android SDK (API 级别 24 及以上)
- Java 11 或更高版本
- Gradle 8.0 或更高版本

### 本地部署步骤

1. **克隆项目**

   ```bash
   git clone https://github.com/your-username/NoteFlow.git
   cd NoteFlow
   ```

2. **打开项目**

   启动 Android Studio，选择 "Open an existing project"，然后选择项目根目录。

3. **同步项目**

   Android Studio 会自动检测到 Gradle 配置并提示同步项目。点击 "Sync Now" 或使用菜单中的 "Sync Project with Gradle Files"。

4. **配置设备**

   - 使用物理 Android 设备（通过 USB 调试）或 Android 模拟器
   - 确保设备/模拟器系统版本为 Android 7.0 (API 24) 或更高

5. **构建和运行**

   - 点击 Android Studio 工具栏中的 "Run" 按钮（绿色三角形）
   - 或使用快捷键 `Shift + F10`
   - 选择目标设备并等待应用安装完成

### 构建 APK

如果您需要构建 APK 文件进行分发：

1. 在 Android Studio 中，选择 "Build" -> "Build Bundle(s) / APK(s)" -> "Build APK(s)"
2. 构建完成后，点击通知中的 "locate" 链接找到生成的 APK 文件

## 使用说明

1. **主页面**：
   - 显示当前日期和每日名言
   - 展示已保存的笔记列表
   - 可通过顶部的菜单按钮打开侧滑菜单

2. **创建笔记**：
   - 点击顶部的 "+" 按钮（新建文章）
   - 输入标题和内容（示例中在代码中预设了笔记内容）

3. **查看笔记**：
   - 点击笔记列表中的任意一项进入详情页
   - 详情页显示完整的笔记内容

4. **侧滑菜单**：
   - 包含"文章分类"和"删除文章"功能
   - 可通过左滑屏幕或点击菜单按钮打开

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进这个项目。

## 许可证

此项目遵循 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件。

## 作者

NoteFlow 由 [您的名字] 开发维护。