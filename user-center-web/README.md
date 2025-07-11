# 耄耋书籍商城系统

## 项目介绍
耄耋书籍商城是一个基于Vue 3 + TypeScript开发的现代化图书购物平台，提供完整的图书浏览、购买、订单管理等功能，并包含后台管理系统。

## 技术栈
- 前端框架：Vue 3.2.13
- 类型支持：TypeScript
- 状态管理：Pinia 2.0.14
- UI组件库：Ant Design Vue 4.2.6
- 路由管理：Vue Router 4.0.3
- HTTP客户端：Axios 1.9.0
- 构建工具：Vue CLI

## 功能特性

### 用户功能
- 用户注册与登录
- 图书浏览和搜索
- 图书详情查看
- 购物车管理
- 订单管理
- 消息通知

### 管理员功能
- 用户管理
- 图书管理
- 订单管理
- 系统消息管理

## 项目结构
```
src/
├── api/                # API接口定义
│   ├── book.ts        # 图书相关接口
│   ├── cart.ts        # 购物车接口
│   ├── message.ts     # 消息接口
│   ├── order.ts       # 订单接口
│   └── user.ts        # 用户接口
├── assets/            # 静态资源
├── components/        # 公共组件
├── layouts/           # 布局组件
├── pages/            # 页面组件
├── router/           # 路由配置
├── store/            # 状态管理
├── views/            # 视图组件
├── App.vue           # 根组件
├── main.ts           # 入口文件
└── request.ts        # Axios配置
```

## 快速开始

### 环境要求
- Node.js >= 14.0.0
- npm >= 6.0.0 或 yarn >= 1.22.0

### 安装依赖
```bash
# 使用npm
npm install

# 或使用yarn
yarn install
```

### 开发环境运行
```bash
# 使用npm
npm run serve

# 或使用yarn
yarn serve
```

### 生产环境构建
```bash
# 使用npm
npm run build

# 或使用yarn
yarn build
```

## 路由说明
- `/` - 首页
- `/user/login` - 用户登录页
- `/user/register` - 用户注册页
- `/admin/userManage` - 用户管理页面
- `/book/bookManage` - 图书管理页面
- `/message` - 消息中心
- `/buy` - 图书购买页面
- `/cart` - 购物车页面
- `/order` - 订单页面
- `/order/orderManage` - 订单管理页面
- `/book/detail` - 图书详情页面

## 开发规范
- 使用TypeScript进行开发，确保类型安全
- 遵循Vue 3组合式API的开发方式
- 使用Ant Design Vue组件库构建用户界面
- 采用模块化的开发方式，保持代码的可维护性和可扩展性

## 项目特点
1. 使用Vue 3最新特性，包括Composition API
2. TypeScript支持，提供完整的类型检查
3. 基于Ant Design Vue的现代化UI设计
4. 完整的用户权限管理
5. 响应式设计，支持多种设备访问
