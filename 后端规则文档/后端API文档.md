# 电影交流社区后端API文档

> **最后更新**: 2026-02-28  
> **基础URL**: 本机url`http://localhost:7070`  安卓虚拟机url‘http://10.0.2.2:7070’
> **API版本**: v1.0

---

## 📋 目录

1. [接口规范](#接口规范)
2. [认证说明](#认证说明)
3. [已实现接口](#已实现接口)
   - [测试接口](#1-测试接口)
   - [用户认证接口](#2-用户认证接口)
   - [用户信息接口](#3-用户信息接口)
   - [关注/粉丝/好友接口](#4-关注粉丝好友接口)
   - [收藏夹管理接口](#5-收藏夹管理接口)
   - [收藏项管理接口](#6-收藏项管理接口)
   - [看过记录接口](#7-看过记录接口)
   - [TMDB电影数据接口](#8-tmdb电影数据接口)
   - [文件上传接口](#9-文件上传接口)
   - [活动管理接口](#10-活动管理接口)
4. [待实现接口](#待实现接口)
5. [错误码说明](#错误码说明)

---

## 接口规范

### 统一响应格式

所有接口返回统一的JSON格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未授权（未登录） |
| 403 | 禁止访问（无权限） |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

### 分页参数

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 0 | 页码（从0开始） |
| size | int | 否 | 20 | 每页数量 |

---

## 认证说明

### JWT Token认证

**重要规则**：除了 `/hello` 和 `/api/auth/**` 接口外，所有接口都需要Token认证！

登录成功后，服务器返回JWT Token，后续请求需在Header中携带：

```
Authorization: Bearer {token}
```

### Token格式示例

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0dXNlciIsImlhdCI6MTcwOTAxMDAwMCwiZXhwIjoxNzA5NjE0ODAwfQ.xxxxx
```

Token包含：
- 用户ID（subject）
- 用户名（username）
- 签发时间（iat）
- 过期时间（exp，默认7天）

---

## 已实现接口

### 1. 测试接口

#### GET /hello

测试接口，验证服务器是否正常运行。

**是否需要Token**: ❌ 否（公开接口）

**请求示例**:
```bash
curl http://localhost:7070/hello
```

**响应示例**:
```
后端项目启动成功！你好，电影社区！
```

---

### 2. 用户认证接口

#### POST /api/auth/register

用户注册。

**是否需要Token**: ❌ 否（公开接口）

**请求头**:
```
Content-Type: application/json
```

**请求体**:
```json
{
  "username": "testuser",
  "phone": "13800138000",
  "password": "123456"
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 | 校验规则 |
|------|------|------|------|---------|
| username | string | 是 | 用户名 | 2-50个字符 |
| phone | string | 是 | 手机号 | 11位数字 |
| password | string | 是 | 密码 | 6-20个字符 |

**响应示例**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "id": 1,
    "userCode": "0001",
    "username": "testuser",
    "phone": "13800138000",
    "avatar": null,
    "bio": null,
    "createdAt": "2026-02-28T10:30:00"
  }
}
```

**错误响应**:
```json
{
  "code": 1001,
  "message": "用户名已存在",
  "data": null
}
```

---

#### POST /api/auth/login

用户登录。

**是否需要Token**: ❌ 否（公开接口）

**请求头**:
```
Content-Type: application/json
```

**请求体**:
```json
{
  "phone": "13800138000",
  "password": "123456"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "user": {
      "id": 1,
      "userCode": "0001",
      "username": "testuser",
      "phone": "13800138000",
      "avatar": null,
      "bio": null,
      "createdAt": "2026-02-28T10:30:00"
    }
  }
}
```

---

### 3. 用户信息接口

#### GET /api/users/profile

获取当前用户信息。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
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
    "createdAt": "2026-02-28T10:00:00"
  }
}
```

---

#### PUT /api/users/profile

更新个人资料。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**:
```json
{
  "username": "newusername",
  "avatar": "https://example.com/new-avatar.jpg",
  "bio": "这是我的新个人简介"
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 | 校验规则 |
|------|------|------|------|---------|
| username | string | 否 | 用户名 | 2-50个字符 |
| avatar | string | 否 | 头像URL | 最多500字符 |
| bio | string | 否 | 个人简介 | 最多500字符 |

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "userCode": "0001",
    "username": "newusername",
    "phone": "13800138000",
    "avatar": "https://example.com/new-avatar.jpg",
    "bio": "这是我的新个人简介",
    "createdAt": "2026-02-28T10:00:00"
  }
}
```

---

#### GET /api/users/{userId}

获取指定用户信息。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | long | 是 | 用户ID |

**响应示例**:
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
    "createdAt": "2026-02-28T10:00:00"
  }
}
```

---

### 4. 关注/粉丝/好友接口

#### POST /api/users/{userId}/follow

关注用户。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | long | 是 | 要关注的用户ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "关注成功",
  "data": null
}
```

**错误响应**:
```json
{
  "code": 6001,
  "message": "不能关注自己",
  "data": null
}
```

---

#### DELETE /api/users/{userId}/follow

取消关注。

**是否需要Token**: ✅ 是

**响应示例**:
```json
{
  "code": 200,
  "message": "取消关注成功",
  "data": null
}
```

---

#### GET /api/users/{userId}/following

获取关注列表（我关注的人）。

**是否需要Token**: ✅ 是

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 0 | 页码 |
| size | int | 否 | 20 | 每页数量 |

**响应示例**:
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
        "createdAt": "2026-02-28T10:00:00"
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

#### GET /api/users/{userId}/followers

获取粉丝列表（关注我的人）。

**是否需要Token**: ✅ 是

**请求参数**: 同关注列表

**响应示例**: 同关注列表

---

#### GET /api/users/{userId}/friends

获取好友列表（互相关注）。

**是否需要Token**: ✅ 是

**响应示例**:
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
      "createdAt": "2026-02-28T10:00:00"
    }
  ]
}
```

---

#### GET /api/users/{userId}/follow/status

获取关注状态。

**是否需要Token**: ✅ 是

**响应示例**:
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

**字段说明**:
- `isFollowing`: 我是否关注了对方
- `isFollower`: 对方是否关注了我
- `isFriend`: 是否为好友（互相关注）

---

#### GET /api/users/{userId}/stats

获取用户统计信息。

**是否需要Token**: ✅ 是

**响应示例**:
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

### 5. 收藏夹管理接口

#### POST /api/collections

创建收藏夹。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**:
```json
{
  "name": "我的科幻片单",
  "description": "收藏的科幻电影",
  "type": "MOVIE",
  "isPublic": true,
  "coverImage": "abc123-def456-789.jpg"
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 | 可选值 |
|------|------|------|------|--------|
| name | string | 是 | 收藏夹名称 | 最多100字符 |
| description | string | 否 | 收藏夹描述 | 最多500字符 |
| type | string | 是 | 收藏夹类型 | MOVIE/EVENT |
| isPublic | boolean | 否 | 是否公开 | 默认true |
| coverImage | string | 否 | 封面图片文件名 | 只传文件名，不是完整URL |

**注意**: `coverImage` 字段只需传入文件名（如 `abc123.jpg`），不需要传完整URL。前端显示时自己拼接：`http://10.0.2.2:7070/uploads/{coverImage}`

**收藏夹类型说明**:
- `MOVIE`: 电影收藏夹，只能收藏电影
- `EVENT`: 活动收藏夹，只能收藏活动

**注意**: 看过功能使用独立的 watched_movies 表，不属于收藏夹系统

**响应示例**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 1,
    "userId": 1,
    "name": "我的科幻片单",
    "description": "收藏的科幻电影",
    "type": "MOVIE",
    "isSystem": false,
    "isPublic": true,
    "coverImage": "abc123-def456-789.jpg",
    "itemCount": 0,
    "createdAt": "2026-03-01T10:00:00",
    "updatedAt": "2026-03-01T10:00:00"
  }
}
```

**注意**: 响应中的 `coverImage` 字段只包含文件名，前端需要拼接完整URL：`http://10.0.2.2:7070/uploads/{coverImage}`

---

#### GET /api/collections

获取用户的所有收藏夹。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
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
      "createdAt": "2026-03-01T10:00:00",
      "updatedAt": "2026-03-01T10:00:00"
    },
    {
      "id": 2,
      "userId": 1,
      "name": "默认活动收藏夹",
      "description": null,
      "type": "EVENT",
      "isSystem": true,
      "isPublic": true,
      "coverImage": null,
      "itemCount": 10,
      "createdAt": "2026-03-01T10:00:00",
      "updatedAt": "2026-03-01T10:00:00"
    }
  ]
}
```

---

#### GET /api/collections/type/{type}

获取用户指定类型的收藏夹。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 | 可选值 |
|------|------|------|------|--------|
| type | string | 是 | 收藏夹类型 | MOVIE/EVENT |

**响应示例**: 同获取所有收藏夹

---

#### GET /api/collections/{id}

获取收藏夹详情。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | long | 是 | 收藏夹ID |

**响应示例**: 同创建收藏夹

---

#### PUT /api/collections/{id}

更新收藏夹。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | long | 是 | 收藏夹ID |

**请求体**:
```json
{
  "name": "更新后的名称",
  "description": "更新后的描述",
  "isPublic": false,
  "coverImage": "new-cover-abc123.jpg"
}
```

**注意**: 
- 系统收藏夹不允许修改名称和类型
- `coverImage` 字段只需传入文件名，不需要传完整URL

**响应示例**: 同创建收藏夹

---

#### DELETE /api/collections/{id}

删除收藏夹。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | long | 是 | 收藏夹ID |

**注意**: 系统收藏夹不允许删除

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

### 6. 收藏项管理接口

#### POST /api/favorites

添加收藏项。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**:
```json
{
  "collectionId": 1,
  "itemType": "MOVIE",
  "tmdbId": 157336,
  "note": "非常喜欢这部电影"
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 | 可选值 |
|------|------|------|------|--------|
| collectionId | long | 是 | 收藏夹ID | - |
| itemType | string | 是 | 收藏项类型 | MOVIE/EVENT |
| tmdbId | long | 是（MOVIE类型） | TMDB电影ID | - |
| itemId | long | 是（EVENT类型） | 活动ID | - |
| note | string | 否 | 用户备注 | - |

**功能说明**:
- 收藏电影时使用`tmdbId`，如果电影不在本地数据库会自动从TMDB获取并保存
- 收藏活动时使用`itemId`（活动ID）

**响应示例**:
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
    "createdAt": "2026-03-01T10:00:00",
    "itemDetail": {
      "id": 100,
      "tmdbId": 157336,
      "title": "星际穿越",
      "posterUrl": "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
      "rating": 8.4,
      "year": "2014"
    }
  }
}
```

---

#### GET /api/favorites/collection/{collectionId}

获取收藏夹中的所有收藏项。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| collectionId | long | 是 | 收藏夹ID |

**响应示例**:
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
      "createdAt": "2026-03-01T10:00:00",
      "itemDetail": {
        "id": 100,
        "title": "盗梦空间",
        "posterUrl": "https://example.com/poster.jpg",
        "rating": 9.3,
        "year": "2010"
      }
    }
  ]
}
```

---

#### GET /api/favorites/collection/{collectionId}/type/{itemType}

获取收藏夹中指定类型的收藏项。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 | 可选值 |
|------|------|------|------|--------|
| collectionId | long | 是 | 收藏夹ID | - |
| itemType | string | 是 | 收藏项类型 | MOVIE/EVENT |

**响应示例**: 同获取所有收藏项

---

#### DELETE /api/favorites/collection/{collectionId}/item/{itemType}/{itemId}

移除收藏项。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| collectionId | long | 是 | 收藏夹ID |
| itemType | string | 是 | 收藏项类型 |
| itemId | long | 是 | 收藏项ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "移除成功",
  "data": null
}
```

---

#### GET /api/favorites/check/{itemType}/{itemId}

检查用户是否收藏了某个项目。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| itemType | string | 是 | 收藏项类型 |
| itemId | long | 是 | 收藏项ID |

**响应示例**:
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

### 7. 看过记录接口

#### POST /api/watched

标记电影为看过。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**:
```json
{
  "tmdbId": 157336,
  "rating": 9.5,
  "note": "非常精彩的电影"
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 | 校验规则 |
|------|------|------|------|---------|
| tmdbId | long | 是 | TMDB电影ID | - |
| rating | double | 否 | 用户评分 | 0.0-10.0 |
| note | string | 否 | 观影笔记 | - |

**功能说明**:
- 如果电影不在本地数据库，会自动从TMDB获取电影信息并保存
- 使用TMDB ID而非本地电影ID，方便前端直接使用TMDB数据

**响应示例**:
```json
{
  "code": 200,
  "message": "标记成功",
  "data": {
    "id": 1,
    "userId": 1,
    "movieId": 100,
    "watchedAt": "2026-03-01T10:00:00",
    "rating": 9.5,
    "note": "非常精彩的电影",
    "createdAt": "2026-03-01T10:00:00",
    "updatedAt": "2026-03-01T10:00:00",
    "movieInfo": {
      "id": 100,
      "tmdbId": 157336,
      "title": "星际穿越",
      "posterUrl": "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
      "rating": 8.4,
      "year": "2014"
    }
  }
}
```

---

#### DELETE /api/watched/{movieId}

取消看过标记。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| movieId | long | 是 | 电影ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "取消成功",
  "data": null
}
```

---

#### PUT /api/watched/{movieId}

更新看过记录。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| movieId | long | 是 | 电影ID |

**请求体**:
```json
{
  "rating": 9.0,
  "note": "更新后的笔记"
}
```

**响应示例**: 同标记看过

---

#### GET /api/watched

获取用户看过的所有电影。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "movieId": 100,
      "watchedAt": "2026-03-01T10:00:00",
      "rating": 9.5,
      "note": "非常精彩的电影",
      "createdAt": "2026-03-01T10:00:00",
      "updatedAt": "2026-03-01T10:00:00",
      "movieInfo": {
        "id": 100,
        "title": "盗梦空间",
        "posterUrl": "https://example.com/poster.jpg",
        "rating": 9.3,
        "year": "2010"
      }
    }
  ]
}
```

---

#### GET /api/watched/check/{movieId}

检查用户是否看过某部电影。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| movieId | long | 是 | 电影ID |

**响应示例**:
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

#### GET /api/watched/count

获取用户看过的电影数量。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
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

### 8. TMDB电影数据接口

#### GET /api/tmdb/search

搜索电影。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| keyword | string | 是 | - | 搜索关键词 |
| page | int | 否 | 1 | 页码 |

**请求示例**:
```bash
GET /api/tmdb/search?keyword=星际穿越&page=1
```

**响应示例**:
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
        "vote_count": 32000,
        "popularity": 150.5
      }
    ],
    "total_pages": 5,
    "total_results": 100
  }
}
```

---

#### GET /api/tmdb/popular

获取热门电影。热门 / 流行 / 人气榜

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 1 | 页码 |

**请求示例**:
```bash
GET /api/tmdb/popular?page=1
```

**响应示例**: 同搜索电影接口

---

#### GET /api/tmdb/now-playing

获取正在上映的电影。正在热映 / 影院热映

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 1 | 页码 |

**请求示例**:
```bash
GET /api/tmdb/now-playing?page=1
```

**响应示例**: 同搜索电影接口

---

#### GET /api/tmdb/upcoming

获取即将上映的电影。即将上映 / 待上映

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 1 | 页码 |

**请求示例**:
```bash
GET /api/tmdb/upcoming?page=1
```

**响应示例**: 同搜索电影接口

---

#### GET /api/tmdb/top-rated

获取高分电影。高分好评 / 最佳评分

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 1 | 页码 |

**请求示例**:
```bash
GET /api/tmdb/top-rated?page=1
```

**响应示例**: 同搜索电影接口

---

#### GET /api/tmdb/movie/{tmdbId}

获取电影详情。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| tmdbId | int | 是 | TMDB电影ID |

**请求示例**:
```bash
GET /api/tmdb/movie/157336
```

**响应示例**:
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
    "popularity": 150.5,
    "budget": 165000000,
    "revenue": 677463813,
    "genres": [
      {
        "id": 12,
        "name": "冒险"
      },
      {
        "id": 18,
        "name": "剧情"
      },
      {
        "id": 878,
        "name": "科幻"
      }
    ],
    "production_companies": [
      {
        "id": 923,
        "name": "Legendary Pictures"
      }
    ],
    "production_countries": [
      {
        "iso_3166_1": "US",
        "name": "United States of America"
      }
    ],
    "spoken_languages": [
      {
        "iso_639_1": "en",
        "name": "English"
      }
    ]
  }
}
```

---

#### GET /api/tmdb/movie/{tmdbId}/credits

获取电影演职人员。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| tmdbId | int | 是 | TMDB电影ID |

**请求示例**:
```bash
GET /api/tmdb/movie/157336/credits
```

**响应示例**:
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
        "original_name": "Matthew McConaughey",
        "character": "Cooper",
        "profile_path": "/sY2mwpafcwqyYS1sOySu1MENDse.jpg",
        "order": 0
      },
      {
        "id": 1813,
        "name": "安妮·海瑟薇",
        "original_name": "Anne Hathaway",
        "character": "Brand",
        "profile_path": "/tLelKoPx5lG6OhJJTAZz2cB9Fqr.jpg",
        "order": 1
      }
    ],
    "crew": [
      {
        "id": 525,
        "name": "克里斯托弗·诺兰",
        "original_name": "Christopher Nolan",
        "job": "Director",
        "department": "Directing",
        "profile_path": "/xuAIuYSmsUzKlUMBFGVZaWsY3DZ.jpg"
      }
    ]
  }
}
```

---

#### GET /api/tmdb/movie/{tmdbId}/images

获取电影图片。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| tmdbId | int | 是 | TMDB电影ID |

**请求示例**:
```bash
GET /api/tmdb/movie/157336/images
```

**响应示例**:
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
        "height": 1080,
        "vote_average": 5.384,
        "vote_count": 4
      }
    ],
    "posters": [
      {
        "file_path": "/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
        "width": 2000,
        "height": 3000,
        "vote_average": 5.318,
        "vote_count": 3
      }
    ]
  }
}
```

**图片URL拼接说明**:
- 完整图片URL = `https://image.tmdb.org/t/p/{size}{file_path}`
- 常用尺寸：
  - 海报：`w185`, `w342`, `w500`, `w780`, `original`
  - 背景图：`w300`, `w780`, `w1280`, `original`
- 示例：`https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg`

---

#### GET /api/tmdb/movie/{tmdbId}/recommendations

获取推荐电影（根据指定电影推荐相似电影）。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| tmdbId | int | 是 | TMDB电影ID |

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 1 | 页码 |

**请求示例**:
```bash
GET /api/tmdb/movie/157336/recommendations?page=1
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "page": 1,
    "results": [
      {
        "id": 27205,
        "title": "盗梦空间",
        "original_title": "Inception",
        "overview": "道姆·柯布是一个经验老道的窃贼...",
        "poster_path": "/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg",
        "backdrop_path": "/s3TBrRGB1iav7gFOCNx3H31MoES.jpg",
        "release_date": "2010-07-15",
        "vote_average": 8.4,
        "vote_count": 35000,
        "popularity": 120.5
      },
      {
        "id": 550,
        "title": "搏击俱乐部",
        "original_title": "Fight Club",
        "overview": "杰克是一个大汽车公司的职员...",
        "poster_path": "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
        "backdrop_path": "/fCayJrkfRaCRCTh8GqN30f8oyQF.jpg",
        "release_date": "1999-10-15",
        "vote_average": 8.4,
        "vote_count": 28000,
        "popularity": 95.3
      }
    ],
    "total_pages": 10,
    "total_results": 200
  }
}
```

**使用场景**:
- 电影详情页的"相关推荐"模块
- "猜你喜欢"功能
- 根据用户看过的电影推荐新电影

---

### 9. 文件上传接口

#### POST /api/upload/image

上传图片文件。

**是否需要Token**: ❌ 否（公开接口）

**请求头**:
```
Content-Type: multipart/form-data
```

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| file | File | 是 | 图片文件 |

**支持的图片格式**:
- JPEG/JPG
- PNG
- GIF
- WEBP

**文件大小限制**: 最大10MB

**请求示例**:
```bash
curl -X POST http://localhost:7070/api/upload/image \
  -F "file=@/path/to/image.jpg"
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "filename": "abc123-def456-789.jpg"
  }
}
```

**注意**: 
- 响应只返回文件名，不返回完整URL
- 前端需要自己拼接完整URL：`http://10.0.2.2:7070/uploads/{filename}`
- 这样设计的好处：
  - 数据库只存储文件名，不存储域名
  - 方便后期更换域名或CDN
  - 减少数据库存储空间

**错误响应**:
```json
{
  "code": 400,
  "message": "只能上传图片文件",
  "data": null
}
```

**使用场景**:
- 用户头像上传
- 收藏夹封面图上传
- 动态图片上传
- 活动海报上传

**使用流程**:
1. 前端选择图片文件
2. 调用上传接口 `POST /api/upload/image`
3. 获取返回的图片URL
4. 在创建/更新其他资源时，将图片URL作为参数传递

**前端示例（Flutter）**:
```dart
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'dart:io';

// 上传图片并返回文件名
Future<String?> uploadImage(File imageFile) async {
  var request = http.MultipartRequest(
    'POST',
    Uri.parse('http://10.0.2.2:7070/api/upload/image'),
  );
  
  request.files.add(
    await http.MultipartFile.fromPath('file', imageFile.path),
  );
  
  var response = await request.send();
  if (response.statusCode == 200) {
    var responseData = await response.stream.bytesToString();
    var jsonData = jsonDecode(responseData);
    return jsonData['data']['filename'];  // 只返回文件名
  }
  return null;
}

// 拼接完整的图片URL
String getImageUrl(String filename) {
  return 'http://10.0.2.2:7070/uploads/$filename';
}

// 使用示例：创建收藏夹时上传封面
Future<void> createCollectionWithCover(File? coverImage) async {
  String? coverFilename;
  
  // 1. 先上传图片，获取文件名
  if (coverImage != null) {
    coverFilename = await uploadImage(coverImage);
  }
  
  // 2. 创建收藏夹（只传文件名）
  final response = await http.post(
    Uri.parse('http://10.0.2.2:7070/api/collections'),
    headers: {
      'Authorization': 'Bearer $token',
      'Content-Type': 'application/json',
    },
    body: jsonEncode({
      'name': '我的收藏夹',
      'type': 'MOVIE',
      'isPublic': true,
      'coverImage': coverFilename,  // 只传文件名，不是完整URL
    }),
  );
  
  // 3. 显示图片时，前端拼接完整URL
  if (coverFilename != null) {
    String imageUrl = getImageUrl(coverFilename);
    // 使用 imageUrl 显示图片
  }
}
```

**注意事项**:
- 上传的图片会保存在服务器的项目根目录 `uploads/` 文件夹
- 文件名会自动生成UUID，避免重复
- **响应只返回文件名，不返回完整URL**
- **前端需要自己拼接完整URL**: `http://10.0.2.2:7070/uploads/{filename}`
- **数据库中只存储文件名**，不存储完整URL
- 这样设计的好处：
  - 方便后期更换域名或CDN
  - 减少数据库存储空间
  - 统一管理图片访问路径

---

#### GET /uploads/{filename}

访问已上传的图片。

**是否需要Token**: ❌ 否（公开接口）

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| filename | string | 是 | 图片文件名 |

**请求示例**:
```bash
GET http://localhost:7070/uploads/abc123-def456-789.jpg
```

**响应**: 直接返回图片文件

**使用场景**:
- 在前端显示上传的图片
- 作为 `<img>` 标签的 `src` 属性
- 作为头像、封面等图片的URL

---

### 10. 活动管理接口

#### POST /api/events

创建活动。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**:
```json
{
  "title": "《星际穿越》IMAX重映观影团",
  "imageUrl": "event-cover-abc123.jpg",
  "eventDate": "2026-03-15T19:30:00",
  "location": "北京国际影城IMAX厅",
  "maxParticipants": 80,
  "type": "观影团",
  "description": "一起去看IMAX版星际穿越！",
  "movieId": 000000
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| title | string | 是 | 活动标题，最多200字符 |
| imageUrl | string | 否 | 活动封面图片文件名 |
| eventDate | datetime | 是 | 活动时间，必须是未来时间 |
| location | string | 是 | 活动地点，最多200字符 |
| maxParticipants | int | 是 | 最大参与人数，默认100 |
| type | string | 是 | 活动类型，如"观影团"、"影评征集"、"线下活动" |
| description | string | 否 | 活动描述，最多2000字符 |
| movieId | long | 是 | 关联的电影ID（必填） |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "《星际穿越》IMAX重映观影团",
    "imageUrl": "event-cover-abc123.jpg",
    "eventDate": "2026-03-15T19:30:00",
    "location": "北京国际影城IMAX厅",
    "participants": 0,
    "maxParticipants": 80,
    "type": "观影团",
    "description": "一起去看IMAX版星际穿越！",
    "movieId": 100,
    "movieTitle": "星际穿越",
    "moviePosterUrl": "poster-abc123.jpg",
    "createdAt": "2026-03-04T10:00:00",
    "updatedAt": "2026-03-04T10:00:00",
    "isParticipant": false
  }
}
```

**注意**: 
- 活动必须关联一个电影
- 响应中会包含关联电影的基本信息（标题、海报）

---

#### GET /api/events

获取活动列表（分页，支持筛选和搜索）。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 0 | 页码 |
| size | int | 否 | 20 | 每页数量 |
| type | string | 否 | - | 按类型筛选 |
| movieId | long | 否 | - | 按电影ID筛选 |
| keyword | string | 否 | - | 搜索关键词（标题或描述） |

**请求示例**:
```bash
# 获取所有活动
GET /api/events?page=0&size=20

# 按类型筛选
GET /api/events?type=观影团

# 按电影ID筛选
GET /api/events?movieId=100

# 搜索活动
GET /api/events?keyword=星际穿越
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "title": "《星际穿越》IMAX重映观影团",
        "imageUrl": "event-cover-abc123.jpg",
        "eventDate": "2026-03-15T19:30:00",
        "location": "北京国际影城IMAX厅",
        "participants": 58,
        "maxParticipants": 80,
        "type": "观影团",
        "description": "一起去看IMAX版星际穿越！",
        "movieId": 100,
        "movieTitle": "星际穿越",
        "moviePosterUrl": "poster-abc123.jpg",
        "createdAt": "2026-03-04T10:00:00",
        "updatedAt": "2026-03-04T10:00:00",
        "isParticipant": true
      }
    ],
    "totalElements": 100,
    "totalPages": 5,
    "size": 20,
    "number": 0
  }
}
```

---

#### GET /api/events/{eventId}

获取活动详情。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| eventId | long | 是 | 活动ID |

**响应示例**: 同创建活动

---

#### PUT /api/events/{eventId}

更新活动（只能更新自己创建的活动）。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| eventId | long | 是 | 活动ID |

**请求体**:
```json
{
  "title": "更新后的标题",
  "imageUrl": "new-cover-abc123.jpg",
  "eventDate": "2026-03-16T19:30:00",
  "location": "新的地点",
  "maxParticipants": 100,
  "type": "观影团",
  "description": "更新后的描述",
  "movieId": 101
}
```

**注意**: 所有字段都是可选的，只更新提供的字段

**响应示例**: 同创建活动

---

#### DELETE /api/events/{eventId}

删除活动（只能删除自己创建的活动）。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| eventId | long | 是 | 活动ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

#### POST /api/events/{eventId}/join

参加活动。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| eventId | long | 是 | 活动ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "joined": true,
    "participants": 59
  }
}
```

**错误响应**:
```json
{
  "code": 400,
  "message": "活动已满员",
  "data": null
}
```

---

#### DELETE /api/events/{eventId}/join

取消参加活动。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| eventId | long | 是 | 活动ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "joined": false,
    "participants": 58
  }
}
```

---

#### GET /api/events/{eventId}/participants

获取活动参与者列表。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| eventId | long | 是 | 活动ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "userId": 2,
      "username": "影迷小李",
      "userCode": "0002",
      "avatar": "avatar-def456.jpg",
      "joinedAt": "2026-03-04T11:00:00"
    },
    {
      "id": 2,
      "userId": 3,
      "username": "电影达人",
      "userCode": "0003",
      "avatar": "avatar-ghi789.jpg",
      "joinedAt": "2026-03-04T12:00:00"
    }
  ]
}
```

---

#### GET /api/events/{eventId}/joined

检查当前用户是否已参加活动。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| eventId | long | 是 | 活动ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "isJoined": true
  }
}
```

---

#### GET /api/events/user/{userId}/joined

获取用户参加的活动列表。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | long | 是 | 用户ID |

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 0 | 页码 |
| size | int | 否 | 20 | 每页数量 |

**响应示例**: 同获取活动列表

---

## 待实现接口

### 1. 动态接口

#### GET /api/feeds

获取动态列表（分页）。

**是否需要Token**: ✅ 是

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "user": {
          "id": 1,
          "userCode": "0001",
          "username": "电影爱好者",
          "avatar": "https://example.com/avatar.jpg"
        },
        "movie": {
          "id": 1,
          "title": "星际穿越",
          "posterUrl": "https://example.com/poster.jpg"
        },
        "event": null,
        "content": "刚看完这部科幻大片，视觉效果太震撼了！",
        "images": ["https://example.com/img1.jpg"],
        "rating": 9.3,
        "likeCount": 234,
        "shareCount": 23,
        "commentCount": 45,
        "createdAt": "2026-02-28T10:00:00"
      }
    ],
    "totalElements": 100,
    "totalPages": 5,
    "size": 20,
    "number": 0
  }
}
```

---

#### POST /api/feeds

发布动态。

**是否需要Token**: ✅ 是

**请求体**:
```json
{
  "movieId": 1,
  "eventId": null,
  "content": "这部电影太棒了！",
  "images": ["https://example.com/img1.jpg"],
  "rating": 9.5
}
```

**说明**：movieId和eventId至少填一个，也可以都不填（纯文字动态）

---

#### GET /api/feeds/{feedId}

获取动态详情。

**是否需要Token**: ✅ 是

---

#### PUT /api/feeds/{feedId}

更新动态。

**是否需要Token**: ✅ 是

---

#### DELETE /api/feeds/{feedId}

删除动态（只能删除自己的）。

**是否需要Token**: ✅ 是

---

#### GET /api/users/{userId}/feeds

获取指定用户的动态列表。

**是否需要Token**: ✅ 是

---

### 2. 评论接口

#### GET /api/feeds/{feedId}/comments

获取动态的评论列表。

**是否需要Token**: ✅ 是

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "user": {
        "id": 2,
        "userCode": "0002",
        "username": "影迷小李",
        "avatar": "https://example.com/avatar.jpg"
      },
      "content": "我也超级喜欢这部电影！",
      "likeCount": 23,
      "createdAt": "2026-02-28T11:00:00"
    }
  ]
}
```

---

#### POST /api/feeds/{feedId}/comments

发表评论。

**是否需要Token**: ✅ 是

**请求体**:
```json
{
  "content": "我也超级喜欢这部电影！"
}
```

---

#### DELETE /api/comments/{commentId}

删除评论（只能删除自己的）。

**是否需要Token**: ✅ 是

---

### 3. 点赞接口

#### POST /api/feeds/{feedId}/like

点赞动态。

**是否需要Token**: ✅ 是

**响应示例**:
```json
{
  "code": 200,
  "message": "点赞成功",
  "data": {
    "liked": true,
    "likeCount": 235
  }
}
```

---

#### DELETE /api/feeds/{feedId}/like

取消点赞。

**是否需要Token**: ✅ 是

---

#### POST /api/comments/{commentId}/like

点赞评论。

**是否需要Token**: ✅ 是

---

#### DELETE /api/comments/{commentId}/like

取消点赞评论。

**是否需要Token**: ✅ 是

---

### 4. 收藏接口

#### GET /api/favorites

获取我的收藏列表（分页）。

**是否需要Token**: ✅ 是

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 0 | 页码 |
| size | int | 否 | 20 | 每页数量 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "movie": {
          "id": 1,
          "title": "星际穿越",
          "posterUrl": "https://example.com/poster.jpg",
          "rating": 9.3
        },
        "createdAt": "2026-02-28T10:00:00"
      }
    ],
    "totalElements": 50,
    "totalPages": 3
  }
}
```

---

#### POST /api/movies/{movieId}/favorite

收藏电影。

**是否需要Token**: ✅ 是

---

#### DELETE /api/movies/{movieId}/favorite

取消收藏。

**是否需要Token**: ✅ 是

---

#### GET /api/movies/{movieId}/favorite/status

检查是否已收藏。

**是否需要Token**: ✅ 是

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "favorited": true
  }
}
```

---

### 2. 评论接口

#### GET /api/movies

获取本地电影列表（分页）。

**是否需要Token**: ✅ 是

**说明**：返回数据库中已保存的电影（用户发布过动态的电影）

---

#### GET /api/movies/{movieId}

获取本地电影详情。

**是否需要Token**: ✅ 是

---

#### GET /api/movies/{movieId}/feeds

获取某电影的所有动态。

**是否需要Token**: ✅ 是

---

## 错误码说明

| 错误码 | 说明 | 接口 |
|--------|------|------|
| 200 | 成功 | 所有接口 |
| 400 | 参数校验失败 | 所有接口 |
| 401 | 未授权（未登录） | 需要认证的接口 |
| 403 | 禁止访问（无权限） | 需要权限的接口 |
| 404 | 资源不存在 | 所有接口 |
| 500 | 服务器内部错误 | 所有接口 |
| 1001 | 用户名已存在 | 注册接口 |
| 1002 | 手机号已存在 | 注册接口 |
| 1003 | 手机号或密码错误 | 登录接口 |
| 1004 | Token无效或已过期 | 需要认证的接口 |
| 2001 | 动态不存在 | 动态相关接口 |
| 2002 | 无权限操作此动态 | 动态相关接口 |
| 2003 | 已经点赞过了 | 点赞接口 |
| 2004 | 还未点赞 | 取消点赞接口 |
| 3001 | 电影不存在 | 电影相关接口 |
| 3002 | 已经收藏过了 | 收藏接口 |
| 3003 | 还未收藏 | 取消收藏接口 |
| 4001 | 活动不存在 | 活动相关接口 |
| 4002 | 活动已满员 | 参加活动接口 |
| 4003 | 已经参加过了 | 参加活动接口 |
| 4004 | 还未参加 | 取消参加接口 |
| 4005 | 无权限操作此活动 | 活动相关接口 |
| 4006 | 活动已满员 | 参加活动接口 |
| 4007 | 已经参加过该活动 | 参加活动接口 |
| 5001 | 评论不存在 | 评论相关接口 |
| 5002 | 无权限操作此评论 | 评论相关接口 |
| 6001 | 不能关注自己 | 关注接口 |
| 6002 | 已经关注过了 | 关注接口 |
| 6003 | 还未关注 | 取消关注接口 |

---

## 接口实现优先级

### 高优先级（核心功能）⭐⭐⭐
1. ✅ 用户注册/登录
2. ✅ 获取/更新个人资料
3. ✅ 关注/取消关注用户
4. ✅ 获取关注/粉丝/好友列表
5. ✅ 收藏夹管理（创建/获取/更新/删除）
6. ✅ 收藏项管理（添加/移除/查询）
7. ✅ 看过记录管理（标记/取消/更新/查询）
8. ✅ TMDB电影数据查询（搜索/热门/详情/推荐等9个接口）
9. ✅ 图片上传功能
10. ✅ 活动管理（创建/查看/更新/删除/参加/取消参加等11个接口）
11. ⏳ 发布动态
12. ⏳ 获取动态列表
13. ⏳ 点赞动态
14. ⏳ 评论动态

### 中优先级（重要功能）⭐⭐
15. ⏳ 获取动态详情
16. ⏳ 删除动态
17. ⏳ 删除评论
18. ⏳ 获取用户动态列表
19. ⏳ 获取收藏列表

### 低优先级（辅助功能）⭐
20. ⏳ 点赞评论
21. ⏳ 获取电影动态列表

---

## 更新日志

### 2026-03-04
- ✅ 实现活动管理完整功能（11个接口）
  - POST /api/events - 创建活动
  - GET /api/events - 获取活动列表（支持筛选和搜索）
  - GET /api/events/{eventId} - 获取活动详情
  - PUT /api/events/{eventId} - 更新活动
  - DELETE /api/events/{eventId} - 删除活动
  - POST /api/events/{eventId}/join - 参加活动
  - DELETE /api/events/{eventId}/join - 取消参加
  - GET /api/events/{eventId}/participants - 获取参与者列表
  - GET /api/events/{eventId}/joined - 检查是否已参加
  - GET /api/events/user/{userId}/created - 获取用户创建的活动
  - GET /api/events/user/{userId}/joined - 获取用户参加的活动
- ✅ 创建活动相关实体类（Event、EventParticipant）
- ✅ 实现活动状态管理（UPCOMING/ONGOING/ENDED）
- ✅ 实现活动分类功能
- ✅ 实现活动搜索功能
- ✅ 实现活动参与人数管理
- ✅ 实现图片上传接口
  - POST /api/upload/image - 上传图片
  - GET /uploads/{filename} - 访问图片
  - **重要变更**: 上传接口只返回文件名，不返回完整URL
  - 数据库中只存储文件名，前端负责拼接完整URL
- ✅ 实现TMDB推荐电影接口
  - GET /api/tmdb/movie/{tmdbId}/recommendations
- ✅ 修复收藏夹响应JSON序列化问题
  - 添加 @JsonInclude(JsonInclude.Include.NON_NULL) 注解
- ✅ 配置静态资源访问
- ✅ 更新SecurityConfig，放行上传接口和静态资源
- ✅ 修改文件上传路径为项目根目录的 uploads 文件夹

### 2026-03-02
- ✅ 实现TMDB电影数据接口（8个接口）
  - 搜索电影
  - 获取热门电影
  - 获取正在上映电影
  - 获取即将上映电影
  - 获取高分电影
  - 获取电影详情
  - 获取演职人员
  - 获取电影图片
- ✅ 修改Movie实体，添加tmdbId字段
- ✅ 实现MovieService，处理TMDB电影到本地数据库的转换
- ✅ 修改看过和收藏功能，支持TMDB电影自动保存
- ✅ 修复Jackson依赖缺失问题

### 2026-02-28
- ✅ 实现用户个人资料接口（获取、更新）
- ✅ 实现关注/粉丝/好友系统（7个接口）
- ✅ 添加用户唯一标识（userCode）
- ✅ 添加个人简介字段（bio）
- ✅ 创建关注关系表（follows）
- ✅ 更新SecurityConfig（除hello和auth外都需要Token）
- 📝 完整整理所有接口文档（已实现+待实现）

### 2026-02-27
- ✅ 实现用户注册接口
- ✅ 实现用户登录接口
- ✅ 添加JWT Token认证
- ✅ 添加参数校验
- ✅ 添加全局异常处理

---

**文档维护**: 本文档记录所有接口（已实现和待实现），随开发进度持续更新

