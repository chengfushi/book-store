# 耄耋书籍商城

## 项目介绍
耄耋书籍商城是一个基于Spring Boot开发的在线书籍销售平台，提供书籍浏览、购物车管理、订单处理等功能。

## 技术栈
- 后端框架：Spring Boot
- 持久层框架：MyBatis-Plus
- 数据库：MySQL
- 项目管理工具：Maven

## 主要功能模块

### 1. 用户管理
- 用户注册/登录
- 用户信息管理（包括基本信息、头像、联系方式等）
- 用户状态管理（正常/封号）
- 用户角色管理

### 2. 书籍管理
- 书籍信息维护（ISBN、名称、价格、描述等）
- 书籍图片管理
- 书籍上架/下架
- 书籍分类管理

### 3. 购物车功能
- 添加商品到购物车
- 购物车商品管理
- 购物车结算

### 4. 订单管理
- 订单创建和处理
- 订单状态跟踪
- 订单明细管理

### 5. 消息通知
- 用户消息管理
- 系统通知

## 项目结构
```
src/main/java/com/chengfu/usercenterapi/
├── common/          # 通用组件（异常处理、响应对象等）
├── config/          # 配置类
├── constant/        # 常量定义
├── controller/      # 控制器层
├── mapper/          # MyBatis Mapper接口
├── model/          # 数据模型
│   ├── cto/        # 组合传输对象
│   ├── domain/     # 领域模型
│   └── request/    # 请求对象
├── service/        # 服务层
│   └── impl/      # 服务实现
└── utils/          # 工具类
```

## 快速开始

### 环境要求
- JDK 1.8+
- MySQL 5.7+
- Maven 3.6+

### 配置说明
1. 在 `application.yml` 中配置数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/book_store
    username: your_username
    password: your_password
```

2. 创建数据库：
```sql
CREATE DATABASE book_store;
```

3. 运行项目：
```bash
mvn spring-boot:run
```

## API文档
项目启动后，可通过以下路径访问API接口：
- 接口前缀：`/api`
- 端口：8080

### 主要接口
- 用户相关：`/api/user/*`
- 书籍相关：`/api/book/*`
- 购物车：`/api/cart/*`
- 订单相关：`/api/order/*`
- 消息通知：`/api/message/*`

## 数据库设计
主要包含以下数据表：
- user：用户信息表
- book_list：书籍信息表
- cart：购物车表
- order_main：订单主表
- order_item：订单明细表
- user_message：用户消息表

## 特性
- 基于MyBatis-Plus的CRUD操作
- 统一的异常处理机制
- 逻辑删除支持
- 跨域配置
- 全局响应处理

## 开发团队
- 开发者：[施成福]
- 联系方式：[shichengfu123@outlook.com]

## 版权信息
本软件使用 [MIT](https://opensource.org/licenses/MIT) 开源协议，详细内容请查看 [LICENSE](LICENSE) 文件。

