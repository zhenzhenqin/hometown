# Hometown 家乡文化展示系统

## 项目简介

这是一个基于 Spring Boot 的家乡文化展示系统后端服务，提供完整的 RESTful API 接口，用于管理家乡的文化景点、特产、用户信息等。

系统采用现代化的技术栈构建，具有良好的扩展性和维护性：

- 🏛️ 景点信息管理
- 🎭 文化信息管理
- 🍎 特产信息管理
- 👤 用户信息管理
- 📁 文件上传功能
- 📋 分页查询功能
- 🔐 数据安全防护

> 项目状态: 后端开发完成 ✅ | 前端开发完成 🚀

## 技术栈

### 后端技术

- 核心框架: Spring Boot 3.5.5
- Web框架: Spring MVC
- 数据库: MySQL 8.0+
- 持久层: MyBatis + PageHelper 分页插件
- 构建工具: Maven 3.9+
- Java版本: Java 17
- 数据库连接池: HikariCP
- JSON处理: Jackson

### 前端技术

- 核心框架: Vue 3
- UI组件库: Element Plus
- 样式: HTML5 + CSS3 + JavaScript
- HTTP客户端: Axios

## 环境要求

- JDK 17 或更高版本
- MySQL 8.0 或更高版本
- Maven 3.9 或更高版本
- Node.js 16+ (前端开发)

## 快速开始

### 1. 克隆项目

### 2. 数据库配置

创建数据库并执行初始化脚本：

### 3. 修改配置文件

编辑 `src/main/resources/application.yml`：

### 4. 编译运行

## API 接口文档

**完整的 API 接口文档请参考：[飞书接口文档](https://acnhk3go2b65.feishu.cn/wiki/QqEcwrNuGiiKfPkIBolcEMz6n1b)**

### 主要接口模块

#### 用户管理

- GET `/user` - 查询用户信息
- GET `/user/{id}` - 根据ID查询用户
- PUT `/user` - 更新用户信息

#### 文化管理

- GET `/cultures` - 分页查询文化列表
- POST `/cultures` - 添加文化
- GET `/cultures/{id}` - 根据ID查询文化
- PUT `/cultures` - 更新文化
- DELETE `/cultures/{ids}` - 批量删除文化

#### 特产管理

- GET `/specialties` - 分页查询特产列表
- POST `/specialties` - 添加特产
- GET `/specialties/{id}` - 根据ID查询特产
- PUT `/specialties` - 更新特产
- DELETE `/specialties/{ids}` - 批量删除特产

#### 景点管理

- GET `/attractions` - 分页查询景点列表
- POST `/attractions` - 添加景点
- GET `/attractions/{id}` - 根据ID查询景点
- PUT `/attractions` - 更新景点
- DELETE `/attractions/{ids}` - 批量删除景点

## 项目结构

src/main/java/com/mjc/
├── HometownApplication.java # Spring Boot 启动类
├── bean/ # 实体类包
│ ├── Attraction.java # 景点实体
│ ├── AttractionQueryParam.java # 景点查询参数
│ ├── Culture.java # 文化实体
│ ├── CultureQueryParam.java # 文化查询参数
│ ├── PageResult.java # 分页结果封装
│ ├── Result.java # 统一响应封装
│ ├── Specialties.java # 特产实体
│ ├── SpecialtiesQueryParam.java # 特产查询参数
│ └── User.java # 用户实体
├── controller/ # 控制器层
│ ├── AttractionController.java # 景点控制器
│ ├── CultureController.java # 文化控制器
│ ├── SpecialtiesController.java # 特产控制器
│ ├── UploadController.java # 文件上传控制器
│ └── UserController.java # 用户控制器
├── mapper/ # 数据访问层
│ ├── AttractionMapper.java # 景点Mapper接口
│ ├── CultureMapper.java # 文化Mapper接口
│ ├── SpecialtiesMapper.java # 特产Mapper接口
│ └── UserMapper.java # 用户Mapper接口
├── service/ # 业务逻辑层
│ ├── AttractionService.java # 景点服务接口
│ ├── CultureService.java # 文化服务接口
│ ├── SpecialtiesService.java # 特产服务接口
│ ├── UserService.java # 用户服务接口
│ └── Impl/ # 服务实现类
│ ├── AttractionServiceImpl.java
│ ├── CultureServiceImpl.java
│ ├── SpecialtiesServiceImpl.java
│ └── UserServiceImpl.java
├── utils/ # 工具类包
│ └── AliyunOSSOperator.java # 阿里云OSS操作工具类
└── config/ # 配置类包

src/main/resources/
├── com/mjc/mapper/ # MyBatis映射文件
│ ├── AttractionMapper.xml
│ ├── CultureMapper.xml
│ ├── SpecialtiesMapper.xml
│ └── UserMapper.xml
├── application.yml # 应用配置文件
└── logback.xml # 日志配置文件

src/test/java/com/mjc/
└── HometownApplicationTests.java # Spring Boot测试类


## 数据库设计

主要数据表：
- `user` - 用户表
- `culture` - 文化信息表
- `specialties` - 特产信息表
- `attraction` - 景点信息表

## 部署说明

### 生产环境部署

## 前端开发

### 技术选型

- Vue 3 - 渐进式JavaScript框架
- Element Plus - Vue 3组件库
- Vue Router - 路由管理
- Axios - HTTP客户端
- ES6+ - 现代JavaScript语法

### 主要页面

- 首页 - 展示家乡概览
- 文化页面 - 文化信息展示
- 特产页面 - 特产信息展示
- 景点页面 - 景点信息展示
- 管理后台 - 内容管理系统

### 开发环境搭建

## 安全考虑

- SQL注入防护：使用MyBatis参数绑定
- XSS防护：前端数据转义
- 文件上传安全：文件类型和大小限制
- 接口安全：后续可集成JWT认证

## 贡献指南

1. Fork 项目到个人仓库
2. 创建功能分支 `git checkout -b feature/your-feature`
3. 提交更改 `git commit -m 'Add some feature'`
4. 推送到分支 `git push origin feature/your-feature`
5. 创建 Pull Request

## 项目维护

- 定期更新依赖版本
- 监控系统性能
- 备份数据库
- 日志分析和监控

## 许可证

MIT License

## 联系方式

- Gitee: https://gitee.com/mzz6666/hometown
- 接口文档: [飞书文档](https://gitee.com/mzz6666/hometown?source=header_my_projects)
