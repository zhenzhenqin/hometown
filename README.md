# 🏠 衢州印象 - 家乡展示网站

> 一个基于 Spring Boot + Vue 3 的沉浸式家乡信息展示系统

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.x-brightgreen)](https://vuejs.org/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

## 📖 项目简介

本项目是一个面向衢州（浙江省下属地级市）的家乡信息展示平台，采用前后端分离架构。系统提供景点介绍、文化展示、特产推荐、AI智能问答导游等功能，并创新性地加入了**数据结构实验室**，为用户提供算法可视化的学习体验。

## 🏗️ 项目架构

```
┌─────────────────────────────────────────────────────────────────┐
│                        前端展示层                                │
├─────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────┐    ┌─────────────────────────────────┐│
│  │   vue-showhometown  │    │  vue-hometown-management        ││
│  │   游客展示前台      │    │  数据管理后台                    ││
│  │   端口: 5173        │    │  端口: 8081                     ││
│  └─────────────────────┘    └─────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        后端服务层                                │
│                      hometown-backend                            │
│                      端口: 8080                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 📦 模块说明

| 模块 | 技术栈 | 说明 | 仓库 |
|------|--------|------|------|
| **后端核心服务** | Spring Boot + MyBatis-Plus + Redis + OSHI | 提供API接口、鉴权、日志、监控及定时任务 | hometown-backend |
| **数据管理后台** | Vue 3 + Element Plus + ECharts | 管理员控制台，包含数据驾驶舱、监控与内容管理 | vue-hometown-management |
| **沉浸式前台** | Vue 3 + BaiduMap GL + CSS Animation | 面向游客的展示端，集成3D地图与创意动画 | vue-showhometown |

## ✨ 核心功能

### 🌐 游客展示前台 (vue-showhometown)

| 功能 | 路径 | 说明 |
|------|------|------|
| 首页 | `/index` | 城市介绍 + 轮播图 + 亮点展示 |
| 文化展示 | `/culture` | 衢州历史文化介绍 |
| 特产美食 | `/specialties` | 特色美食与产品推荐 |
| 景区导览 | `/attraction` | 旅游景点信息与评价 |
| 交互地图 | `/map` | BaiduMap 3D地图集成 |
| AI 问答 | `/ai` | SiliconFlow DeepSeek-V3 智能导游 |
| 数据结构实验室 | `/ds-lab` | 算法可视化学习平台 |

### 🤖 AI 智能问答

- 基于 SiliconFlow 免费 API (DeepSeek-V3 模型)
- 支持流式响应 (SSE)
- 聊天历史持久化存储
- 知识库检索增强

### 🔬 数据结构实验室

创新的**算法可视化学习平台**，采用策略模式设计：

- **冒泡排序** - 数组排序可视化
- **KMP字符串匹配** - 模式匹配算法演示
- **链表插入** - 数据结构操作可视化

特点：
- SVG 手绘风格可视化
- 逐行代码高亮同步
- 交互式播放控制
- 学习笔记导出

### 📊 数据管理后台 (vue-hometown-management)

- 数据驾驶舱 - 数据可视化概览
- 内容管理 - CRUD 操作
- 用户管理
- 实时监控

## 🚀 快速开始

### 环境要求

- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+

### 1. 克隆项目

```bash
git clone https://github.com/your-username/hometown-backend.git
git clone https://github.com/your-username/vue-showhometown.git
git clone https://github.com/your-username/vue-hometown-management.git
```

### 2. 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE hometown DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 导入数据
source hometown_db_xxx.sql;

-- AI功能建表
source hometown/qu-server/src/main/resources/ai_knowledge.sql;
```

### 3. 配置后端

编辑 `hometown/qu-server/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hometown?useUnicode=true&characterEncoding=utf-8
    username: your_username
    password: your_password
```

### 4. 启动后端

```bash
cd hometown
mvn spring-boot:run
```

后端地址: `http://localhost:8080`

### 5. 启动展示前台

```bash
cd vue-showhometown
npm install
npm run dev
```

访问: `http://localhost:5173`

### 6. 启动管理后台

```bash
cd vue-hometown-management
npm install
npm run dev
```

访问: `http://localhost:8081`

## 📁 项目结构

```
Hometown/
├── hometown/                          # 后端核心服务
│   ├── qu-server/
│   │   └── src/main/java/com/mjc/
│   │       ├── controller/            # REST API 控制器
│   │       ├── service/              # 业务逻辑层
│   │       ├── mapper/               # 数据访问层
│   │       ├── entity/               # 实体类
│   │       ├── config/               # 配置类
│   │       ├── interceptor/          # 拦截器
│   │       └── algorithm/            # 算法可视化框架
│   │           ├── impl/             # 算法实现
│   │           └── AlgorithmScene.java
│   └── src/main/resources/
│       └── application.yml
│
├── showhometown/                     # 游客展示前台
│   └── src/
│       ├── api/                      # API 请求封装
│       ├── components/               # 公共组件
│       │   ├── AlgoStepPlayer.vue    # 算法播放器
│       │   └── FloatingAI.vue        # AI悬浮球
│       ├── views/
│       │   ├── ds-lab/               # 数据结构实验室
│       │   └── ai/                   # AI问答页面
│       └── router/
│
├── vue-hometown-management/          # 数据管理后台
│   └── src/
│       ├── api/
│       ├── views/
│       └── router/
│
├── images/                           # 静态资源
├── README.md
└── AI功能开发文档.md
```

## 🎨 技术亮点

### 前后端分离架构
- RESTful API 设计
- JWT 无状态认证
- CORS 跨域处理

### 前端技术特色
- Vue 3 Composition API
- Element Plus 组件库
- CSS Animation 创意动画
- BaiduMap GL 3D地图
- SVG 算法可视化

### 后端技术特色
- Spring Boot 3.x
- MyBatis-Plus 快速 CRUD
- Redis 缓存优化
- OSHI 系统监控
- 策略模式算法框架

### AI 集成
- SiliconFlow API
- SSE 流式响应
- RAG 知识库检索

## 📝 API 文档

### 公开接口 (无需认证)

| 接口 | 方法 | 说明 |
|------|------|------|
| `/attractions/all` | GET | 获取景点列表 |
| `/cultures/all` | GET | 获取文化列表 |
| `/specialties/all` | GET | 获取特产列表 |
| `/articles/*` | GET | 获取文章 |
| `/ai/chat` | POST | AI 问答 |
| `/algorithm/{name}` | GET | 算法执行 |

### 管理接口 (需 JWT 认证)

- `/admin/**` - 管理员操作
- 内容管理、用户管理等

## 🔧 开发指南

### 添加新算法

1. 在 `algorithm/impl/` 下创建新的算法类
2. 继承 `BaseArrayAlgorithm`
3. 实现 `getName()`, `getDescription()`, `getCodeSnippet()`
4. 在 `run()` 中添加场景

```java
@Component
public class QuickSortAlgorithm extends BaseArrayAlgorithm {
    @Override
    public String getName() {
        return "quickSort";
    }
    // ...
}
```

### 前端代理配置

`vue-showhometown/vite.config.js`:

```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true,
    rewrite: (path) => path.replace(/^\/api/, '')
  }
}
```

## 📄 许可证

本项目仅供学习交流使用。

## 🙏 致谢

- [SiliconFlow](https://siliconflow.cn/) - AI API 支持
- [Element Plus](https://element-plus.org/) - UI 组件库
- [Baidu Map](https://lbsyun.baidu.com/) - 地图服务
