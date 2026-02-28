# 电影交流社区后端API文档

> **最后更新**: 2026-02-28  
> **基础URL**: 本机url`http://localhost:7070`  安卓虚拟机url‘http://10.0.2.2:7070’
> **API版本**: v1.0

---

## 📋 目录

1. [接口规范](#接口规范)
2. [认证说明](#认证说明)
3. [已实现接口](#已实现接口)
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

## 待实现接口

### 1. 动态接口

#### GET /api/feeds

获取动态列表（分页）。

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

### 5. 活动接口

#### GET /api/events

获取活动列表（分页）。

**是否需要Token**: ✅ 是

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 0 | 页码 |
| size | int | 否 | 20 | 每页数量 |
| type | string | 否 | - | 活动类型（观影团/影评征集/线下活动） |

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
        "description": "一起去看IMAX版星际穿越！",
        "imageUrl": "https://example.com/event.jpg",
        "eventDate": "2026-03-15T19:30:00",
        "location": "北京国际影城IMAX厅",
        "participants": 58,
        "maxParticipants": 80,
        "type": "观影团",
        "createdAt": "2026-02-28T10:00:00"
      }
    ],
    "totalElements": 20,
    "totalPages": 1
  }
}
```

---

#### GET /api/events/{eventId}

获取活动详情。

**是否需要Token**: ✅ 是

---

#### POST /api/events

创建活动。

**是否需要Token**: ✅ 是

**请求体**:
```json
{
  "title": "《星际穿越》IMAX重映观影团",
  "description": "一起去看IMAX版星际穿越！",
  "imageUrl": "https://example.com/event.jpg",
  "eventDate": "2026-03-15T19:30:00",
  "location": "北京国际影城IMAX厅",
  "maxParticipants": 80,
  "type": "观影团"
}
```

---

#### PUT /api/events/{eventId}

更新活动（只能更新自己创建的）。

**是否需要Token**: ✅ 是

---

#### DELETE /api/events/{eventId}

删除活动（只能删除自己创建的）。

**是否需要Token**: ✅ 是

---

#### POST /api/events/{eventId}/join

参加活动。

**是否需要Token**: ✅ 是

**响应示例**:
```json
{
  "code": 200,
  "message": "参加成功",
  "data": {
    "joined": true,
    "participants": 59
  }
}
```

---

#### DELETE /api/events/{eventId}/join

取消参加。

**是否需要Token**: ✅ 是

---

#### GET /api/events/{eventId}/participants

获取活动参与者列表。

**是否需要Token**: ✅ 是

---

### 6. 电影接口

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
| 5001 | 评论不存在 | 评论相关接口 |
| 5002 | 无权限操作此评论 | 评论相关接口 |

---

## 接口实现优先级

### 高优先级（核心功能）⭐⭐⭐
1. ✅ 用户注册/登录
2. ✅ 获取/更新个人资料
3. ⏳ 发布动态
4. ⏳ 获取动态列表
5. ⏳ 点赞动态
6. ⏳ 评论动态
7. ⏳ 收藏电影

### 中优先级（重要功能）⭐⭐
8. ⏳ 获取动态详情
9. ⏳ 删除动态
10. ⏳ 删除评论
11. ⏳ 获取用户动态列表
12. ⏳ 获取收藏列表

### 低优先级（辅助功能）⭐
13. ⏳ 活动相关接口
14. ⏳ 点赞评论
15. ⏳ 获取电影动态列表

---

## 更新日志

### 2026-02-28
- ✅ 实现用户个人资料接口（获取、更新）
- ✅ 添加用户唯一标识（userCode）
- ✅ 添加个人简介字段（bio）
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

