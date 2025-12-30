# 🌏 衢州地区信息管理系统 (Quzhou Hometown System)

> **南孔圣地 · 衢州有礼**
> 一款融合了**传统文化与现代技术**的全栈数字化平台。不仅提供文化、特产、旅游资源的展示，更具备**流量分析、地域感知、服务器监控**等企业级后台能力。项目包含**创意交互（烂柯棋局）**与**3D地图导览**，致力于打造沉浸式的“云游衢州”体验。

![Java](https://img.shields.io/badge/Java-17%2B-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7%2B-green) ![Vue](https://img.shields.io/badge/Vue-3.x-42b883) ![ECharts](https://img.shields.io/badge/ECharts-5.x-red) ![Redis](https://img.shields.io/badge/Redis-Latest-red)

---

## 🔗 项目源码仓库

本项目采用前后端分离架构，各模块独立维护：

| 模块名称 | 技术栈 | 说明 | 仓库地址 |
| :--- | :--- | :--- | :--- |
| **后端核心服务** | SpringBoot + MyBatis-Plus + Redis + OSHI | 提供API接口、鉴权、日志、监控及定时任务 | [hometown-backend](https://github.com/zhenzhenqin/hometown) |
| **数据管理后台** | Vue 3 + Element Plus + ECharts | 管理员控制台，包含数据驾驶舱、监控与内容管理 | [vue-hometown-management](https://github.com/zhenzhenqin/vue_hometown_management) |
| **沉浸式前台** | Vue 3 + BaiduMap GL + CSS Animation | 面向游客的展示端，集成3D地图与创意动画 | [vue-showhometown](https://github.com/zhenzhenqin/vue_showhometown) |

---

## ✨ 核心技术亮点

### 1. 🎨 创意交互：烂柯 · 岁月棋局
> *代码位置：展示端 `views/website`*

打破传统枯燥的系统运行时间展示，**以“棋局”喻“时光”**，致敬衢州烂柯文化。
* **纯 CSS 构建**：无图渲染水墨山水背景，极致轻量，零素材依赖。
* **动态对弈算法**：前端模拟围棋自动落子，每一手棋代表系统稳定运行的一个“心跳”。
* **环境隐喻**：将服务器 **CPU 负载** 映射为背景的 **云雾浓度**（粒子雾气效果），实现技术指标的艺术化表达。

### 2. 📊 数据中心与地域感知
> *代码位置：管理端 `views/report` & 后端 `ReportService`*

从“被动记录”转向“主动分析”，构建全方位的流量监控体系。
* **高并发统计**：利用 **Redis HyperLogLog/Set** 实现 UV（独立访客）与 PV（访问量）的秒级去重统计。
* **地域雷达**：集成 **Ip2Region** 离线库与 **ECharts 地图**，精准解析用户 IP 属地，生成可视化的用户来源热力分布图。支持“内网IP/未知地域”的智能归类。

### 3. 🗺️ 3D 空间可视化导览
> *代码位置：展示端 `views/map`*

* 集成 **百度地图 GL (WebGL)** 引擎，支持 3D 倾斜视角查看景点分布。
* 实现平滑的 **FlyTo 飞行视角** 切换，提供沉浸式的地理位置漫游体验。
* **智能打点**：支持经纬度直接定位与地址解析兜底双重策略。

### 4. 🛡️ 硬件级服务监控
> *代码位置：后端 `MonitorController` & `OSHI`*

* 引入 **OSHI (Operating System and Hardware Information)** 库。
* 实时采集服务器的 CPU 核心负载、JVM 堆内存使用率、磁盘 I/O 等底层指标，并在后台以仪表盘形式直观展示。

---

## 🧩 功能模块概览

### 🖥️ 后台管理端 (Admin Console)

1.  **数据驾驶舱 (Dashboard)**
  * **核心指标**：用户总量、文章收录数、全站互动量概览。
  * **可视化图表**：特产价格分布饼图、热门景点柱状图、实时数据大屏。
2.  **流量与地域分析**
  * **地域感知雷达**：中国地图热力分布，直观展示用户来源省份与城市 Top 榜单。
  * **流量报表**：近七日 UV/PV 折线趋势图，支持动态切换维度。
3.  **系统监控**
  * **服务监控**：CPU/内存/JVM 实时仪表盘，服务器环境参数（IP、OS、架构）。
  * **日志审计**：基于 AOP 全链路记录敏感操作（操作人、IP、耗时、方法名）。
4.  **内容管理**
  * **景点/特产/文化**：富文本编辑、多图上传（接入阿里云 OSS 防盗链）。
  * **用户管理**：账号状态控制（封禁/解封）、重置密码。

### 🌏 用户展示端 (User Portal)

1.  **运行中枢 (Website Status)**
  * 展示“烂柯岁月”创意动画，将枯燥的服务器状态转化为水墨棋局。
2.  **全域导览 (Map)**
  * 基于地理位置的景点聚合展示，点击标记支持一键导航与详情跳转。
3.  **文化与资源**
  * **南孔文化**：图文并茂的非遗文化展示。
  * **特产美食**：瀑布流布局展示衢州特色美食，支持按热度排序。
4.  **互动功能**
  * **高性能交互**：利用 Redis 缓存承载高频点赞与收藏。
  * **用户中心**：个人资料管理、历史足迹查看。

---

## 🛠️ 技术栈清单

### 后端 (Backend)
-   **核心框架**: Spring Boot 2.7.x
-   **数据库**: MySQL 8.0 + MyBatis-Plus
-   **缓存/统计**: Redis (Template & HyperLogLog)
-   **硬件监控**: OSHI (System Info)
-   **IP解析**: Ip2Region (离线库)
-   **安全鉴权**: JWT + Interceptor
-   **工具**: Swagger/Knife4j, Lombok, Hutool, FastJSON
-   **云存储**: 阿里云 OSS (配置 Referer 防盗链)

### 前端 (Frontend)
-   **核心**: Vue 3 (Composition API) + Vite 4
-   **UI库**: Element Plus (管理端) / 自定义 CSS (展示端)
-   **可视化**: ECharts 5.x + 百度地图 API GL
-   **网络**: Axios (封装拦截器，统一错误处理)
-   **样式**: SCSS + Pure CSS3 Animations (无图水墨渲染)

---

## 🚀 快速启动 (Quick Start)

### 1. 环境准备
* JDK 17+
* Node.js 16+
* MySQL 8.0 & Redis 5.0+

### 2. 数据库初始化
导入 `qu-server/src/main/resources/init.sql` 到 MySQL 数据库 `hometown_db`。

### 3. 后端启动
修改 `application.yml` 或挂载外部配置文件：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hometown_db
  redis:
    host: localhost
# 配置阿里云 OSS 和百度地图 AK