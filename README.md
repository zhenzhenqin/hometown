**Hometown 家乡文化展示系统**
**项目简介**
这是一个基于 Spring Boot 的家乡文化展示系统后端服务，提供完整的 RESTful API 接口，用于管理家乡的文化景点、特产、用户信息等。
系统采用现代化的技术栈构建，具有良好的扩展性和维护性
🏛️ 景点信息管理
🎭 文化信息管理
🍎 特产信息管理
👤 用户信息管理
📁 文件上传功能
📋 分页查询功能
🔐 数据安全防护




**技术栈**

**后端技术**
核心框架: Spring Boot 3.5.5
Web框架: Spring MVC
数据库: MySQL 8.0+
持久层: MyBatis + PageHelper 分页插件
构建工具: Maven 3.9+
Java版本: Java 17
数据库连接池: HikariCP
JSON处理: Jackson
**前端技术（计划）**
核心框架: Vue 3
UI组件库: Element Plus
样式: HTML5 + CSS3 + JavaScript
HTTP客户端: Axios
**环境要求:**
JDK 17 或更高版本
MySQL 8.0 或更高版本  
Maven 3.9 或更高版本
Node.js 16+ (前端开发)




**快速开始**

1. 克隆项目
   git clone https://gitee.com/mzz6666/hometown.git
   cd hometown
2. 数据库配置
   创建数据库并执行初始化脚本：
   CREATE DATABASE hometown DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
3. 修改配置文件
   编辑 src/main/resources/application.yml：
   spring:
   datasource:
   url: jdbc:mysql://localhost:3306/hometown?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
   username: your_username
   password: your_password

    # 文件上传配置(可使用阿里云OSS,作者囊中羞涩)
    file:
    upload:
    path: D:/code/images/
4. 编译运行
   # 清理并编译项目
    mvn clean compile

    # 运行项目
   mvn spring-boot:run

   # 或者打包后运行
   mvn clean package
   java -jar target/hometown-1.0.0.jar




**API 接口文档**
**完整的 API 接口文档请参考：飞书接口文档**
完整链接在:https://gitee.com/mzz6666/hometown?source=header_my_projects
**主要接口模块**
_用户管理_
GET /user - 查询用户信息
GET /user/{id} - 根据ID查询用户
PUT /user - 更新用户信息
_文化管理_
GET /cultures - 分页查询文化列表
POST /cultures - 添加文化
GET /cultures/{id} - 根据ID查询文化
PUT /cultures - 更新文化
DELETE /cultures/{ids} - 批量删除文化
_特产管理_
GET /specialties - 分页查询特产列表
POST /specialties - 添加特产
GET /specialties/{id} - 根据ID查询特产
PUT /specialties - 更新特产
DELETE /specialties/{ids} - 批量删除特产
_景点管理_
GET /attractions - 分页查询景点列表
POST /attractions - 添加景点
GET /attractions/{id} - 根据ID查询景点
PUT /attractions - 更新景点
DELETE /attractions/{ids} - 批量删除景点




**项目结构**
src/main/java/com/mjc/
├── HometownApplication.java     # Spring Boot 启动类
├── bean/                        # 实体类包
│   ├── User.java               # 用户实体
│   ├── Culture.java            # 文化实体
│   ├── Specialties.java        # 特产实体
│   ├── Attraction.java         # 景点实体
│   ├── PageResult.java         # 分页结果封装
│   └── Result.java             # 统一响应封装
├── controller/                  # 控制器层
│   ├── UserController.java     # 用户控制器
│   ├── CultureController.java  # 文化控制器
│   ├── SpecialtiesController.java # 特产控制器
│   └── AttractionController.java # 景点控制器
├── service/                     # 业务逻辑层
│   ├── UserService.java        # 用户服务接口
│   ├── CultureService.java     # 文化服务接口
│   ├── SpecialtiesService.java # 特产服务接口
│   ├── AttractionService.java  # 景点服务接口
│   └── Impl/                   # 服务实现类
├── mapper/                      # 数据访问层
│   ├── UserMapper.java         # 用户Mapper接口
│   ├── CultureMapper.java      # 文化Mapper接口
│   ├── SpecialtiesMapper.java  # 特产Mapper接口
│   └── AttractionMapper.java   # 景点Mapper接口
├── utils/                       # 工具类包
│   └── FileUploadUtil.java     # 文件上传工具类
└── config/                      # 配置类包



**数据库设计**
主要数据表
 user - 用户表
 culture - 文化信息表
 specialties - 特产信息表
 attraction - 景点信息表




**部署说明**

**生产环境部署**
# 打包项目
mvn clean package -Pprod

# 运行应用
java -jar target/hometown-1.0.0.jar --spring.profiles.active=prod



**Docker 部署（可选）**
   FROM openjdk:17-jdk-alpine
   COPY target/hometown-1.0.0.jar app.jar
   EXPOSE 8080
   ENTRYPOINT ["java", "-jar", "/app.jar"]


**前端开发计划**
_技术选型_
Vue 3 - 渐进式JavaScript框架
Element Plus - Vue 3组件库
Vue Router - 路由管理
Axios - HTTP客户端
ES6+ - 现代JavaScript语法
_主要页面_
首页 - 展示家乡概览
文化页面 - 文化信息展示
特产页面 - 特产信息展示
景点页面 - 景点信息展示
管理后台 - 内容管理系统



_开发环境搭建_
# 创建Vue项目
npm create vue@latest hometown-frontend
cd hometown-frontend

# 安装依赖
npm install

# 安装Element Plus
npm install element-plus

# 安装其他依赖
npm install axios vue-router

# 启动开发服务器
npm run dev


**配置说明**
_应用配置_

server:
   port: 8080
   
spring:
   datasource:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/hometown
      username: root
      password: password

mybatis:
   mapper-locations: classpath:com/mjc/mapper/*.xml
   type-aliases-package: com.mjc.bean

pagehelper:
   helper-dialect: mysql
   reasonable: true
   support-methods-arguments: true

**文件上传配置**
file:
   upload:
      path: /var/www/uploads/


**错误处理**
_系统采用统一的错误响应格式:(json格式)_

{
"code": 0,
"msg": "错误信息",
"data": null
}

**安全考虑**
SQL注入防护：使用MyBatis参数绑定
XSS防护：前端数据转义
文件上传安全：文件类型和大小限制
接口安全：后续可集成JWT认证


**贡献指南**
Fork 项目到个人仓库
创建功能分支 git checkout -b feature/your-feature
提交更改 git commit -m 'Add some feature'
推送到分支 git push origin feature/your-feature
创建 Pull Request


**项目维护**
定期更新依赖版本
监控系统性能
备份数据库
日志分析和监控

**许可证**

_MIT License_

**联系方式**
Gitee: https://gitee.com/mzz6666/hometown
接口文档: 飞书文档 
链接:https://gitee.com/mzz6666/hometown?source=header_my_projects


项目状态: 后端开发完成 ✅ | 前端开发待开始 🚀