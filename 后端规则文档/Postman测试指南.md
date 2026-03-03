# Postman接口测试指南

> **最后更新**: 2026-03-02  
> **基础URL**: `http://localhost:7070`  
> **测试工具**: Postman

---

## 📋 目录

1. [环境准备](#环境准备)
2. [测试接口列表](#测试接口列表)
3. [认证接口测试](#认证接口测试)
4. [用户信息接口测试](#用户信息接口测试)
5. [关注系统接口测试](#关注系统接口测试)
6. [收藏夹管理接口测试](#收藏夹管理接口测试)
7. [收藏项管理接口测试](#收藏项管理接口测试)
8. [看过记录接口测试](#看过记录接口测试)
9. [TMDB电影数据接口测试](#tmdb电影数据接口测试)
10. [常见问题](#常见问题)

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

### 认证接口（无需Token）
| 接口名称 | 请求方式 | 接口路径 | 说明 |
|---------|---------|---------|------|
| 测试接口 | GET | /hello | 测试服务是否启动 |
| 用户注册 | POST | /api/auth/register | 注册新用户 |
| 用户登录 | POST | /api/auth/login | 用户登录获取Token |

### 用户信息接口（需要Token）
| 接口名称 | 请求方式 | 接口路径 | 说明 |
|---------|---------|---------|------|
| 获取当前用户信息 | GET | /api/users/profile | 获取当前登录用户信息 |
| 更新个人资料 | PUT | /api/users/profile | 更新用户名、头像、简介 |
| 获取指定用户信息 | GET | /api/users/{userId} | 获取其他用户信息 |

### 关注系统接口（需要Token）
| 接口名称 | 请求方式 | 接口路径 | 说明 |
|---------|---------|---------|------|
| 关注用户 | POST | /api/users/{userId}/follow | 关注指定用户 |
| 取消关注 | DELETE | /api/users/{userId}/follow | 取消关注指定用户 |
| 获取关注列表 | GET | /api/users/{userId}/following | 获取用户关注的人 |
| 获取粉丝列表 | GET | /api/users/{userId}/followers | 获取用户的粉丝 |
| 获取好友列表 | GET | /api/users/{userId}/friends | 获取互相关注的好友 |
| 获取关注状态 | GET | /api/users/{userId}/follow/status | 查询与某用户的关注关系 |
| 获取用户统计 | GET | /api/users/{userId}/stats | 获取关注/粉丝/好友数量 |

### 收藏夹管理接口（需要Token）
| 接口名称 | 请求方式 | 接口路径 | 说明 |
|---------|---------|---------|------|
| 创建收藏夹 | POST | /api/collections | 创建新收藏夹 |
| 获取所有收藏夹 | GET | /api/collections | 获取当前用户的所有收藏夹 |
| 获取指定类型收藏夹 | GET | /api/collections/type/{type} | 获取MOVIE或EVENT类型收藏夹 |
| 获取收藏夹详情 | GET | /api/collections/{id} | 获取单个收藏夹详情 |
| 更新收藏夹 | PUT | /api/collections/{id} | 更新收藏夹信息 |
| 删除收藏夹 | DELETE | /api/collections/{id} | 删除收藏夹 |

### 收藏项管理接口（需要Token）
| 接口名称 | 请求方式 | 接口路径 | 说明 |
|---------|---------|---------|------|
| 添加收藏项 | POST | /api/favorites | 添加电影或活动到收藏夹 |
| 获取收藏夹内容 | GET | /api/favorites/collection/{collectionId} | 获取收藏夹中的所有项目 |
| 获取指定类型收藏项 | GET | /api/favorites/collection/{collectionId}/type/{itemType} | 获取收藏夹中指定类型的项目 |
| 移除收藏项 | DELETE | /api/favorites/collection/{collectionId}/item/{itemType}/{itemId} | 从收藏夹移除项目 |
| 检查收藏状态 | GET | /api/favorites/check/{itemType}/{itemId} | 检查是否已收藏某项目 |

### 看过记录接口（需要Token）
| 接口名称 | 请求方式 | 接口路径 | 说明 |
|---------|---------|---------|------|
| 标记看过 | POST | /api/watched | 标记电影为看过 |
| 取消看过 | DELETE | /api/watched/{movieId} | 取消看过标记 |
| 更新看过记录 | PUT | /api/watched/{movieId} | 更新评分和笔记 |
| 获取看过列表 | GET | /api/watched | 获取所有看过的电影 |
| 检查看过状态 | GET | /api/watched/check/{movieId} | 检查是否看过某电影 |
| 获取看过数量 | GET | /api/watched/count | 获取看过的电影总数 |

### TMDB电影数据接口（需要Token）
| 接口名称 | 请求方式 | 接口路径 | 说明 |
|---------|---------|---------|------|
| 搜索电影 | GET | /api/tmdb/search | 根据关键词搜索电影 |
| 获取热门电影 | GET | /api/tmdb/popular | 获取热门电影列表 |
| 获取正在上映 | GET | /api/tmdb/now-playing | 获取正在上映的电影 |
| 获取即将上映 | GET | /api/tmdb/upcoming | 获取即将上映的电影 |
| 获取高分电影 | GET | /api/tmdb/top-rated | 获取高分电影列表 |
| 获取电影详情 | GET | /api/tmdb/movie/{tmdbId} | 获取电影详细信息 |
| 获取演职人员 | GET | /api/tmdb/movie/{tmdbId}/credits | 获取电影演员和导演信息 |
| 获取电影图片 | GET | /api/tmdb/movie/{tmdbId}/images | 获取电影海报和剧照 |

---

## 认证接口测试

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

### 2. 用户注册测试

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

### 3. 用户登录测试

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

## 用户信息接口测试

### 1. 获取当前用户信息

**接口**: `GET /api/users/profile`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/users/profile`
3. 在 `Headers` 中添加：
   - Key: `Authorization`
   - Value: `Bearer {你的token}`
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "userCode": "0001",
    "username": "testuser",
    "phone": "13800138000",
    "avatar": "https://example.com/avatar.jpg",
    "bio": "这是我的个人简介",
    "createdAt": "2026-03-02T10:00:00"
  }
}
```

---

### 2. 更新个人资料

**接口**: `PUT /api/users/profile`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `PUT` 方法
2. 输入URL: `http://localhost:7070/api/users/profile`
3. 在 `Headers` 中添加：
   - Key: `Authorization`
   - Value: `Bearer {你的token}`
   - Key: `Content-Type`
   - Value: `application/json`
4. 在 `Body` 中选择 `raw` 和 `JSON`，输入：

```json
{
  "username": "新用户名",
  "avatar": "https://example.com/new-avatar.jpg",
  "bio": "这是我的新个人简介"
}
```

5. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "userCode": "0001",
    "username": "新用户名",
    "phone": "13800138000",
    "avatar": "https://example.com/new-avatar.jpg",
    "bio": "这是我的新个人简介",
    "createdAt": "2026-03-02T10:00:00"
  }
}
```

---

### 3. 获取指定用户信息

**接口**: `GET /api/users/{userId}`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/users/2`（2是用户ID）
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**: 同获取当前用户信息

---

## 关注系统接口测试

### 1. 关注用户

**接口**: `POST /api/users/{userId}/follow`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `POST` 方法
2. 输入URL: `http://localhost:7070/api/users/2/follow`（关注ID为2的用户）
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "关注成功",
  "data": null
}
```

**错误响应（不能关注自己）**:
```json
{
  "code": 6001,
  "message": "不能关注自己",
  "data": null
}
```

---

### 2. 取消关注

**接口**: `DELETE /api/users/{userId}/follow`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `DELETE` 方法
2. 输入URL: `http://localhost:7070/api/users/2/follow`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "取消关注成功",
  "data": null
}
```

---

### 3. 获取关注列表

**接口**: `GET /api/users/{userId}/following`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/users/1/following?page=0&size=20`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "content": [
      {
        "id": 2,
        "userCode": "0002",
        "username": "用户B",
        "avatar": "https://example.com/avatar.jpg",
        "bio": "这是用户B的简介",
        "createdAt": "2026-03-02T10:00:00"
      }
    ],
    "totalElements": 50,
    "totalPages": 3,
    "size": 20,
    "number": 0
  }
}
```

---

### 4. 获取粉丝列表

**接口**: `GET /api/users/{userId}/followers`

**需要Token**: ✅ 是

**步骤**: 同获取关注列表，只需将URL改为 `/followers`

---

### 5. 获取好友列表

**接口**: `GET /api/users/{userId}/friends`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/users/1/friends`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 2,
      "userCode": "0002",
      "username": "用户B",
      "avatar": "https://example.com/avatar.jpg",
      "bio": "这是用户B的简介",
      "createdAt": "2026-03-02T10:00:00"
    }
  ]
}
```

---

### 6. 获取关注状态

**接口**: `GET /api/users/{userId}/follow/status`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/users/2/follow/status`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "isFollowing": true,
    "isFollower": true,
    "isFriend": true
  }
}
```

---

### 7. 获取用户统计

**接口**: `GET /api/users/{userId}/stats`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/users/1/stats`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "followingCount": 123,
    "followerCount": 456,
    "friendCount": 89
  }
}
```

---

## 收藏夹管理接口测试

### 1. 创建收藏夹

**接口**: `POST /api/collections`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `POST` 方法
2. 输入URL: `http://localhost:7070/api/collections`
3. 在 `Headers` 中添加：
   - Authorization
   - Content-Type: application/json
4. 在 `Body` 中输入：

```json
{
  "name": "我的科幻片单",
  "description": "收藏的科幻电影",
  "type": "MOVIE",
  "isPublic": true,
  "coverImage": "https://example.com/cover.jpg"
}
```

5. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 3,
    "userId": 1,
    "name": "我的科幻片单",
    "description": "收藏的科幻电影",
    "type": "MOVIE",
    "isSystem": false,
    "isPublic": true,
    "coverImage": "https://example.com/cover.jpg",
    "itemCount": 0,
    "createdAt": "2026-03-02T10:00:00",
    "updatedAt": "2026-03-02T10:00:00"
  }
}
```

---

### 2. 获取所有收藏夹

**接口**: `GET /api/collections`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/collections`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "name": "默认电影收藏夹",
      "description": null,
      "type": "MOVIE",
      "isSystem": true,
      "isPublic": true,
      "coverImage": null,
      "itemCount": 5,
      "createdAt": "2026-03-02T10:00:00",
      "updatedAt": "2026-03-02T10:00:00"
    }
  ]
}
```

---

### 3. 获取指定类型收藏夹

**接口**: `GET /api/collections/type/{type}`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/collections/type/MOVIE`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**type参数**: `MOVIE` 或 `EVENT`

---

### 4. 更新收藏夹

**接口**: `PUT /api/collections/{id}`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `PUT` 方法
2. 输入URL: `http://localhost:7070/api/collections/3`
3. 在 `Headers` 中添加 Authorization 和 Content-Type
4. 在 `Body` 中输入：

```json
{
  "name": "更新后的名称",
  "description": "更新后的描述",
  "isPublic": false,
  "coverImage": "https://example.com/new-cover.jpg"
}
```

5. 点击 `Send`

**注意**: 系统收藏夹不允许修改名称和类型

---

### 5. 删除收藏夹

**接口**: `DELETE /api/collections/{id}`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `DELETE` 方法
2. 输入URL: `http://localhost:7070/api/collections/3`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**注意**: 系统收藏夹不允许删除

**成功响应**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

## 收藏项管理接口测试

### 1. 添加收藏项（电影）

**接口**: `POST /api/favorites`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `POST` 方法
2. 输入URL: `http://localhost:7070/api/favorites`
3. 在 `Headers` 中添加 Authorization 和 Content-Type
4. 在 `Body` 中输入：

```json
{
  "collectionId": 1,
  "itemType": "MOVIE",
  "tmdbId": 157336,
  "note": "非常喜欢这部电影"
}
```

5. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "添加成功",
  "data": {
    "id": 1,
    "collectionId": 1,
    "itemType": "MOVIE",
    "itemId": 100,
    "note": "非常喜欢这部电影",
    "createdAt": "2026-03-02T10:00:00",
    "itemDetail": {
      "id": 100,
      "tmdbId": 157336,
      "title": "星际穿越",
      "posterUrl": "https://image.tmdb.org/t/p/w500/xxx.jpg",
      "rating": 8.4,
      "year": "2014"
    }
  }
}
```

**说明**: 
- 收藏电影时使用 `tmdbId`（TMDB电影ID）
- 如果电影不在本地数据库，会自动从TMDB获取并保存

---

### 2. 获取收藏夹内容

**接口**: `GET /api/favorites/collection/{collectionId}`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/favorites/collection/1`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "collectionId": 1,
      "itemType": "MOVIE",
      "itemId": 100,
      "note": "非常喜欢这部电影",
      "createdAt": "2026-03-02T10:00:00",
      "itemDetail": {
        "id": 100,
        "title": "星际穿越",
        "posterUrl": "https://image.tmdb.org/t/p/w500/xxx.jpg",
        "rating": 8.4,
        "year": "2014"
      }
    }
  ]
}
```

---

### 3. 移除收藏项

**接口**: `DELETE /api/favorites/collection/{collectionId}/item/{itemType}/{itemId}`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `DELETE` 方法
2. 输入URL: `http://localhost:7070/api/favorites/collection/1/item/MOVIE/100`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "移除成功",
  "data": null
}
```

---

### 4. 检查收藏状态

**接口**: `GET /api/favorites/check/{itemType}/{itemId}`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/favorites/check/MOVIE/100`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "isFavorited": true
  }
}
```

---

## 看过记录接口测试

### 1. 标记电影为看过

**接口**: `POST /api/watched`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `POST` 方法
2. 输入URL: `http://localhost:7070/api/watched`
3. 在 `Headers` 中添加 Authorization 和 Content-Type
4. 在 `Body` 中输入：

```json
{
  "tmdbId": 157336,
  "rating": 9.5,
  "note": "非常精彩的电影"
}
```

5. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "标记成功",
  "data": {
    "id": 1,
    "userId": 1,
    "movieId": 100,
    "watchedAt": "2026-03-02T10:00:00",
    "rating": 9.5,
    "note": "非常精彩的电影",
    "createdAt": "2026-03-02T10:00:00",
    "updatedAt": "2026-03-02T10:00:00",
    "movieInfo": {
      "id": 100,
      "tmdbId": 157336,
      "title": "星际穿越",
      "posterUrl": "https://image.tmdb.org/t/p/w500/xxx.jpg",
      "rating": 8.4,
      "year": "2014"
    }
  }
}
```

**说明**: 使用 `tmdbId`（TMDB电影ID），如果电影不在本地数据库会自动保存

---

### 2. 取消看过标记

**接口**: `DELETE /api/watched/{movieId}`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `DELETE` 方法
2. 输入URL: `http://localhost:7070/api/watched/100`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "取消成功",
  "data": null
}
```

---

### 3. 更新看过记录

**接口**: `PUT /api/watched/{movieId}`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `PUT` 方法
2. 输入URL: `http://localhost:7070/api/watched/100`
3. 在 `Headers` 中添加 Authorization 和 Content-Type
4. 在 `Body` 中输入：

```json
{
  "rating": 9.0,
  "note": "更新后的笔记"
}
```

5. 点击 `Send`

---

### 4. 获取看过列表

**接口**: `GET /api/watched`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/watched`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "movieId": 100,
      "watchedAt": "2026-03-02T10:00:00",
      "rating": 9.5,
      "note": "非常精彩的电影",
      "createdAt": "2026-03-02T10:00:00",
      "updatedAt": "2026-03-02T10:00:00",
      "movieInfo": {
        "id": 100,
        "title": "星际穿越",
        "posterUrl": "https://image.tmdb.org/t/p/w500/xxx.jpg",
        "rating": 8.4,
        "year": "2014"
      }
    }
  ]
}
```

---

### 5. 检查看过状态

**接口**: `GET /api/watched/check/{movieId}`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/watched/check/100`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "isWatched": true
  }
}
```

---

### 6. 获取看过数量

**接口**: `GET /api/watched/count`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/watched/count`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "count": 42
  }
}
```

---

## TMDB电影数据接口测试

### ⚠️ 重要提示：网络问题

TMDB API 位于国外服务器 (`https://api.themoviedb.org`)，在中国大陆访问可能会遇到以下问题：

**常见错误**:
```
Connection timed out: connect
```

**解决方案**:
1. **使用代理**: 配置系统代理或VPN
2. **修改hosts**: 添加TMDB API的DNS解析（可能需要定期更新）
3. **使用CDN**: 考虑使用国内的TMDB数据镜像服务
4. **前端直接调用**: 让前端直接调用TMDB API，后端只负责数据持久化

**如果无法访问TMDB API**，可以：
- 跳过TMDB接口测试
- 直接测试收藏和看过功能（使用本地已有的电影数据）
- 等待网络环境改善后再测试

---

### 1. 搜索电影

**接口**: `GET /api/tmdb/search`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/tmdb/search?keyword=星际穿越&page=1`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "page": 1,
    "results": [
      {
        "id": 157336,
        "title": "星际穿越",
        "original_title": "Interstellar",
        "overview": "在不远的未来，随着地球自然环境的恶化...",
        "poster_path": "/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
        "backdrop_path": "/xu9zaAevzQ5nnrsXN6JcahLnG4i.jpg",
        "release_date": "2014-11-05",
        "vote_average": 8.4,
        "vote_count": 32000
      }
    ],
    "total_pages": 5,
    "total_results": 100
  }
}
```

---

### 2. 获取热门电影

**接口**: `GET /api/tmdb/popular`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/tmdb/popular?page=1`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

---

### 3. 获取正在上映

**接口**: `GET /api/tmdb/now-playing`

**需要Token**: ✅ 是

**步骤**: 同获取热门电影，URL改为 `/api/tmdb/now-playing?page=1`

---

### 4. 获取即将上映

**接口**: `GET /api/tmdb/upcoming`

**需要Token**: ✅ 是

**步骤**: 同获取热门电影，URL改为 `/api/tmdb/upcoming?page=1`

---

### 5. 获取高分电影

**接口**: `GET /api/tmdb/top-rated`

**需要Token**: ✅ 是

**步骤**: 同获取热门电影，URL改为 `/api/tmdb/top-rated?page=1`

---

### 6. 获取电影详情

**接口**: `GET /api/tmdb/movie/{tmdbId}`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/tmdb/movie/157336`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 157336,
    "title": "星际穿越",
    "original_title": "Interstellar",
    "tagline": "人类的下一步，将迈向宇宙",
    "overview": "在不远的未来，随着地球自然环境的恶化...",
    "poster_path": "/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
    "backdrop_path": "/xu9zaAevzQ5nnrsXN6JcahLnG4i.jpg",
    "release_date": "2014-11-05",
    "runtime": 169,
    "vote_average": 8.4,
    "vote_count": 32000,
    "budget": 165000000,
    "revenue": 677463813,
    "genres": [
      {
        "id": 12,
        "name": "冒险"
      },
      {
        "id": 878,
        "name": "科幻"
      }
    ]
  }
}
```

---

### 7. 获取演职人员

**接口**: `GET /api/tmdb/movie/{tmdbId}/credits`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/tmdb/movie/157336/credits`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 157336,
    "cast": [
      {
        "id": 287,
        "name": "马修·麦康纳",
        "character": "Cooper",
        "profile_path": "/xxx.jpg"
      }
    ],
    "crew": [
      {
        "id": 525,
        "name": "克里斯托弗·诺兰",
        "job": "Director",
        "department": "Directing"
      }
    ]
  }
}
```

---

### 8. 获取电影图片

**接口**: `GET /api/tmdb/movie/{tmdbId}/images`

**需要Token**: ✅ 是

**步骤**:
1. 选择 `GET` 方法
2. 输入URL: `http://localhost:7070/api/tmdb/movie/157336/images`
3. 在 `Headers` 中添加 Authorization
4. 点击 `Send`

**成功响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 157336,
    "backdrops": [
      {
        "file_path": "/xu9zaAevzQ5nnrsXN6JcahLnG4i.jpg",
        "width": 1920,
        "height": 1080
      }
    ],
    "posters": [
      {
        "file_path": "/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
        "width": 2000,
        "height": 3000
      }
    ]
  }
}
```

**图片URL拼接**:
- 完整URL = `https://image.tmdb.org/t/p/{size}{file_path}`
- 示例: `https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg`

---

## 使用Token访问需要认证的接口

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

### Q4: 401 Unauthorized

**原因**: Token无效或未提供

**解决方案**:
1. 确认已在Headers中添加Authorization
2. 检查Token格式：`Bearer {token}`（注意Bearer后有空格）
3. 确认Token未过期（默认7天有效期）
4. 重新登录获取新Token

---

### Q5: TMDB API连接超时

**错误信息**: `Connection timed out: connect`

**原因**: 无法访问TMDB API服务器（国外服务器）

**解决方案**:
1. 使用VPN或代理
2. 配置系统代理
3. 跳过TMDB接口测试，直接测试其他功能
4. 让前端直接调用TMDB API

---

### Q6: 参数校验失败但不知道哪个字段错误

**解决方案**:
查看响应的详细信息，通常会包含具体的错误字段

---

### Q7: 数据库连接失败

**错误信息**: `Communications link failure`

**解决方案**:
1. 确认MySQL服务已启动
2. 检查application.properties中的数据库配置
3. 确认数据库用户名和密码正确
4. 确认数据库`final`已创建

---

### Q8: 收藏或看过功能提示"电影不存在"

**原因**: 使用了本地电影ID而非TMDB ID

**解决方案**:
1. 使用`tmdbId`参数（TMDB电影ID）
2. 系统会自动从TMDB获取电影信息并保存到本地
3. 如果TMDB API无法访问，需要先手动在数据库中添加电影数据

---

### Q9: 无法删除系统收藏夹

**原因**: 系统收藏夹（isSystem=true）不允许删除

**解决方案**:
这是正常的业务逻辑，系统收藏夹是用户注册时自动创建的默认收藏夹，不允许删除

---

### Q10: 不能关注自己

**错误信息**: `不能关注自己`

**原因**: 业务逻辑限制

**解决方案**:
这是正常的业务逻辑，用户不能关注自己

---

## 测试检查清单

### 认证功能测试
- [ ] 测试接口（/hello）正常访问
- [ ] 正常注册成功
- [ ] 用户名已存在时提示错误
- [ ] 手机号已注册时提示错误
- [ ] 参数校验正常工作
- [ ] 正常登录成功并返回Token
- [ ] 手机号不存在时提示错误
- [ ] 密码错误时提示错误
- [ ] Token格式正确（JWT格式）

### 用户信息功能测试
- [ ] 获取当前用户信息成功
- [ ] 更新个人资料成功
- [ ] 获取其他用户信息成功
- [ ] 未登录时返回401错误

### 关注系统功能测试
- [ ] 关注用户成功
- [ ] 不能关注自己
- [ ] 取消关注成功
- [ ] 获取关注列表成功（支持分页）
- [ ] 获取粉丝列表成功（支持分页）
- [ ] 获取好友列表成功
- [ ] 获取关注状态正确
- [ ] 获取用户统计数据正确

### 收藏夹功能测试
- [ ] 创建收藏夹成功
- [ ] 获取所有收藏夹成功
- [ ] 获取指定类型收藏夹成功
- [ ] 更新收藏夹成功
- [ ] 删除收藏夹成功
- [ ] 系统收藏夹不允许删除
- [ ] 用户注册时自动创建默认收藏夹

### 收藏项功能测试
- [ ] 添加电影到收藏夹成功
- [ ] TMDB电影自动保存到本地
- [ ] 获取收藏夹内容成功
- [ ] 移除收藏项成功
- [ ] 检查收藏状态正确
- [ ] 收藏夹itemCount自动更新

### 看过记录功能测试
- [ ] 标记电影为看过成功
- [ ] TMDB电影自动保存到本地
- [ ] 取消看过标记成功
- [ ] 更新看过记录成功
- [ ] 获取看过列表成功
- [ ] 检查看过状态正确
- [ ] 获取看过数量正确

### TMDB接口功能测试（需要网络）
- [ ] 搜索电影成功
- [ ] 获取热门电影成功
- [ ] 获取正在上映电影成功
- [ ] 获取即将上映电影成功
- [ ] 获取高分电影成功
- [ ] 获取电影详情成功
- [ ] 获取演职人员成功
- [ ] 获取电影图片成功

---

## 数据库验证

### 验证注册是否成功

在Navicat中执行：

```sql
-- 查询所有用户
SELECT id, user_code, username, phone, avatar, bio, created_at FROM users;

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

### 验证关注关系

```sql
-- 查询关注关系
SELECT * FROM follows;

-- 查询用户1关注的人
SELECT f.*, u.username 
FROM follows f 
JOIN users u ON f.followed_id = u.id 
WHERE f.follower_id = 1;

-- 查询用户1的粉丝
SELECT f.*, u.username 
FROM follows f 
JOIN users u ON f.follower_id = u.id 
WHERE f.followed_id = 1;
```

### 验证收藏夹

```sql
-- 查询所有收藏夹
SELECT * FROM collections;

-- 查询用户1的收藏夹
SELECT * FROM collections WHERE user_id = 1;

-- 查询收藏夹中的项目
SELECT f.*, m.title 
FROM favorites f 
LEFT JOIN movies m ON f.item_id = m.id AND f.item_type = 'MOVIE'
WHERE f.collection_id = 1;
```

### 验证看过记录

```sql
-- 查询所有看过记录
SELECT * FROM watched_movies;

-- 查询用户1看过的电影
SELECT wm.*, m.title, m.tmdb_id
FROM watched_movies wm
JOIN movies m ON wm.movie_id = m.id
WHERE wm.user_id = 1;
```

### 验证TMDB电影保存

```sql
-- 查询所有电影
SELECT id, tmdb_id, title, original_title, rating, year FROM movies;

-- 查询指定TMDB ID的电影
SELECT * FROM movies WHERE tmdb_id = 157336;
```

---

## Postman Collection 导出/导入

### 导出Collection

1. 在Postman左侧找到你的Collection
2. 点击三个点 `...` → `Export`
3. 选择 `Collection v2.1`
4. 保存为 `电影社区API.postman_collection.json`

### 导入Collection

1. 点击左上角 `Import`
2. 选择导出的JSON文件
3. 点击 `Import`

### 分享给团队

可以将导出的JSON文件分享给团队成员，他们导入后即可使用所有配置好的接口

---

## 测试流程建议

### 完整测试流程

1. **启动服务**: 启动Spring Boot应用和MySQL数据库
2. **测试连接**: 访问 `/hello` 确认服务正常
3. **注册用户**: 注册2-3个测试用户
4. **登录获取Token**: 登录并保存Token到环境变量
5. **测试用户信息**: 获取和更新个人资料
6. **测试关注系统**: 用户之间互相关注
7. **测试收藏夹**: 创建收藏夹、添加收藏项
8. **测试看过记录**: 标记电影为看过
9. **测试TMDB接口**: 搜索和获取电影信息（需要网络）
10. **数据库验证**: 检查数据库中的数据是否正确

### 快速测试流程（跳过TMDB）

如果无法访问TMDB API：

1. 启动服务 → 注册登录 → 获取Token
2. 测试用户信息和关注系统
3. 手动在数据库中插入测试电影数据：

```sql
INSERT INTO movies (tmdb_id, title, original_title, overview, poster_url, backdrop_url, release_date, rating, year, runtime, created_at, updated_at)
VALUES 
(157336, '星际穿越', 'Interstellar', '在不远的未来...', 'https://image.tmdb.org/t/p/w500/xxx.jpg', NULL, '2014-11-05', 8.4, '2014', 169, NOW(), NOW()),
(550, '搏击俱乐部', 'Fight Club', '一个失眠的人...', 'https://image.tmdb.org/t/p/w500/yyy.jpg', NULL, '1999-10-15', 8.8, '1999', 139, NOW(), NOW());
```

4. 使用本地电影ID测试收藏和看过功能

---

**文档维护**: 本文档记录所有已实现接口的测试方法，随功能开发持续更新

