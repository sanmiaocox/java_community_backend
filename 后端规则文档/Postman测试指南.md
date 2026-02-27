# Postman接口测试指南

> **最后更新**: 2026-02-27  
> **基础URL**: `http://localhost:7070`  
> **测试工具**: Postman

---

## 📋 目录

1. [环境准备](#环境准备)
2. [测试接口列表](#测试接口列表)
3. [用户注册测试](#用户注册测试)
4. [用户登录测试](#用户登录测试)
5. [常见问题](#常见问题)

---

## 环境准备

### 1. 启动后端服务

确保Spring Boot项目已启动，默认端口为 `7070`

### 2. 确认数据库

确保MySQL数据库 `final` 已创建，并且 `users` 表已创建

### 3. 安装Postman

如果还没有安装Postman，请访问 [Postman官网](https://www.postman.com/downloads/) 下载安装

---

## 测试接口列表

| 接口名称 | 请求方式 | 接口路径 | 说明 |
|---------|---------|---------|------|
| 测试接口 | GET | /hello | 测试服务是否启动 |
| 用户注册 | POST | /api/auth/register | 注册新用户 |
| 用户登录 | POST | /api/auth/login | 用户登录获取Token |

---

## 测试接口

### 1. 测试服务是否启动

**接口**: `GET http://localhost:7070/hello`

**步骤**:
1. 打开Postman
2. 选择 `GET` 方法
3. 输入URL: `http://localhost:7070/hello`
4. 点击 `Send` 按钮

**预期响应**:
```
后端项目启动成功！你好，电影社区！
```

**截图说明**:
- Status: `200 OK`
- Body: 显示欢迎信息

---

## 用户注册测试

### 接口信息

- **请求方式**: `POST`
- **接口路径**: `/api/auth/register`
- **Content-Type**: `application/json`

### 测试步骤

#### 1. 创建请求

1. 打开Postman
2. 点击 `New` → `HTTP Request`
3. 选择 `POST` 方法
4. 输入URL: `http://localhost:7070/api/auth/register`

#### 2. 设置请求头

点击 `Headers` 标签，添加：

| Key | Value |
|-----|-------|
| Content-Type | application/json |

#### 3. 设置请求体

点击 `Body` 标签，选择 `raw` 和 `JSON`，输入：

```json
{
  "username": "testuser",
  "phone": "13800138000",
  "password": "123456"
}
```

#### 4. 发送请求

点击 `Send` 按钮

### 成功响应示例

**Status**: `200 OK`

```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "phone": "13800138000",
    "avatar": null,
    "createdAt": "2026-02-27T10:30:00"
  }
}
```

### 测试用例

#### 用例1：正常注册

**请求体**:
```json
{
  "username": "张三",
  "phone": "13912345678",
  "password": "123456"
}
```

**预期结果**: 注册成功，返回用户信息

---

#### 用例2：用户名已存在

**请求体**:
```json
{
  "username": "testuser",
  "phone": "13900000001",
  "password": "123456"
}
```

**预期结果**:
```json
{
  "code": 1001,
  "message": "用户名已存在",
  "data": null
}
```

---

#### 用例3：手机号已注册

**请求体**:
```json
{
  "username": "newuser",
  "phone": "13800138000",
  "password": "123456"
}
```

**预期结果**:
```json
{
  "code": 1002,
  "message": "手机号已被注册",
  "data": null
}
```

---

#### 用例4：参数校验失败（用户名为空）

**请求体**:
```json
{
  "username": "",
  "phone": "13800138000",
  "password": "123456"
}
```

**预期结果**:
```json
{
  "code": 400,
  "message": "参数校验失败",
  "data": null
}
```

---

#### 用例5：参数校验失败（手机号格式错误）

**请求体**:
```json
{
  "username": "testuser2",
  "phone": "12345678901",
  "password": "123456"
}
```

**预期结果**:
```json
{
  "code": 400,
  "message": "参数校验失败",
  "data": null
}
```

---

#### 用例6：参数校验失败（密码太短）

**请求体**:
```json
{
  "username": "testuser3",
  "phone": "13800138001",
  "password": "123"
}
```

**预期结果**:
```json
{
  "code": 400,
  "message": "参数校验失败",
  "data": null
}
```

---

## 用户登录测试

### 接口信息

- **请求方式**: `POST`
- **接口路径**: `/api/auth/login`
- **Content-Type**: `application/json`

### 测试步骤

#### 1. 创建请求

1. 打开Postman
2. 点击 `New` → `HTTP Request`
3. 选择 `POST` 方法
4. 输入URL: `http://localhost:7070/api/auth/login`

#### 2. 设置请求头

点击 `Headers` 标签，添加：

| Key | Value |
|-----|-------|
| Content-Type | application/json |

#### 3. 设置请求体

点击 `Body` 标签，选择 `raw` 和 `JSON`，输入：

```json
{
  "phone": "13800138000",
  "password": "123456"
}
```

#### 4. 发送请求

点击 `Send` 按钮

### 成功响应示例

**Status**: `200 OK`

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0dXNlciIsImlhdCI6MTcwOTAxMDAwMCwiZXhwIjoxNzA5NjE0ODAwfQ.xxxxx",
    "user": {
      "id": 1,
      "username": "testuser",
      "phone": "13800138000",
      "avatar": null,
      "createdAt": "2026-02-27T10:30:00"
    }
  }
}
```

**重要**: 复制返回的 `token` 值，后续需要认证的接口都需要携带这个Token

### 测试用例

#### 用例1：正常登录

**请求体**:
```json
{
  "phone": "13800138000",
  "password": "123456"
}
```

**预期结果**: 登录成功，返回Token和用户信息

---

#### 用例2：手机号不存在

**请求体**:
```json
{
  "phone": "13999999999",
  "password": "123456"
}
```

**预期结果**:
```json
{
  "code": 1003,
  "message": "手机号或密码错误",
  "data": null
}
```

---

#### 用例3：密码错误

**请求体**:
```json
{
  "phone": "13800138000",
  "password": "wrongpassword"
}
```

**预期结果**:
```json
{
  "code": 1003,
  "message": "手机号或密码错误",
  "data": null
}
```

---

#### 用例4：参数为空

**请求体**:
```json
{
  "phone": "",
  "password": ""
}
```

**预期结果**:
```json
{
  "code": 400,
  "message": "参数校验失败",
  "data": null
}
```

---

## 使用Token访问需要认证的接口（待实现）

### 设置Authorization Header

当后续接口需要认证时，需要在请求头中携带Token：

1. 点击 `Headers` 标签
2. 添加新的Header：

| Key | Value |
|-----|-------|
| Authorization | Bearer {你的token} |

**示例**:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0dXNlciIsImlhdCI6MTcwOTAxMDAwMCwiZXhwIjoxNzA5NjE0ODAwfQ.xxxxx
```

**注意**: `Bearer` 和 `token` 之间有一个空格

---

## Postman Collection导入

### 创建Collection

为了方便测试，可以创建一个Postman Collection：

1. 点击左侧 `Collections`
2. 点击 `+` 创建新Collection
3. 命名为 `电影社区API`
4. 添加以下请求：
   - 测试接口
   - 用户注册
   - 用户登录

### 设置环境变量

1. 点击右上角齿轮图标 → `Manage Environments`
2. 点击 `Add` 创建新环境
3. 命名为 `本地开发`
4. 添加变量：

| Variable | Initial Value | Current Value |
|----------|---------------|---------------|
| baseUrl | http://localhost:7070 | http://localhost:7070 |
| token | | (登录后自动设置) |

5. 在请求中使用变量：`{{baseUrl}}/api/auth/login`

### 自动设置Token

在登录接口的 `Tests` 标签中添加脚本：

```javascript
// 解析响应
var jsonData = pm.response.json();

// 如果登录成功，保存token到环境变量
if (jsonData.code === 200 && jsonData.data.token) {
    pm.environment.set("token", jsonData.data.token);
    console.log("Token已保存:", jsonData.data.token);
}
```

这样登录成功后，Token会自动保存到环境变量中。

---

## 常见问题

### Q1: 连接被拒绝（Connection refused）

**原因**: 后端服务未启动或端口错误

**解决方案**:
1. 确认Spring Boot项目已启动
2. 检查端口是否为7070
3. 查看控制台是否有错误信息

---

### Q2: 404 Not Found

**原因**: 接口路径错误

**解决方案**:
1. 检查URL是否正确
2. 确认Controller的@RequestMapping路径
3. 查看后端日志

---

### Q3: 500 Internal Server Error

**原因**: 服务器内部错误

**解决方案**:
1. 查看后端控制台的错误堆栈
2. 检查数据库连接是否正常
3. 确认数据库表是否已创建

---

### Q4: 参数校验失败但不知道哪个字段错误

**解决方案**:
查看响应的详细信息，通常会包含具体的错误字段

---

### Q5: 数据库连接失败

**错误信息**: `Communications link failure`

**解决方案**:
1. 确认MySQL服务已启动
2. 检查application.properties中的数据库配置
3. 确认数据库用户名和密码正确

---

## 测试检查清单

### 注册功能测试

- [ ] 正常注册成功
- [ ] 用户名已存在时提示错误
- [ ] 手机号已注册时提示错误
- [ ] 用户名为空时提示错误
- [ ] 手机号格式错误时提示错误
- [ ] 密码太短时提示错误
- [ ] 注册后数据库中有对应记录
- [ ] 密码已加密存储

### 登录功能测试

- [ ] 正常登录成功并返回Token
- [ ] 手机号不存在时提示错误
- [ ] 密码错误时提示错误
- [ ] 参数为空时提示错误
- [ ] Token格式正确（JWT格式）
- [ ] Token可以正常解析

---

## 数据库验证

### 验证注册是否成功

在Navicat中执行：

```sql
-- 查询所有用户
SELECT id, username, phone, avatar, created_at FROM users;

-- 查询指定手机号的用户
SELECT * FROM users WHERE phone = '13800138000';
```

### 验证密码是否加密

```sql
-- 查看密码字段（应该是加密后的字符串）
SELECT username, password FROM users;
```

密码应该类似这样：
```
$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi
```

---

## 下一步测试

当实现更多功能后，可以测试：

1. **获取用户信息**: `GET /api/user/profile`（需要Token）
2. **发布动态**: `POST /api/feeds`（需要Token）
3. **获取动态列表**: `GET /api/feeds`
4. **点赞动态**: `POST /api/feeds/{id}/like`（需要Token）
5. **评论动态**: `POST /api/feeds/{id}/comments`（需要Token）

---

**文档维护**: 本文档将随接口开发持续更新

