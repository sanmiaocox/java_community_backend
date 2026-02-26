# 电影交流社区后端API文档

> **最后更新**: 2026-02-26  
> **基础URL**: `http://localhost:7070`  
> **API版本**: v1.0

---

## 📋 目录

1. [接口规范](#接口规范)
2. [认证说明](#认证说明)
3. [测试接口](#测试接口)
4. [TMDB电影接口](#tmdb电影接口)
5. [用户接口](#用户接口-待开发)
6. [动态接口](#动态接口-待开发)
7. [评论接口](#评论接口-待开发)
8. [点赞接口](#点赞接口-待开发)
9. [收藏接口](#收藏接口-待开发)
10. [活动接口](#活动接口-待开发)

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

需要分页的接口统一使用以下参数：

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 1 | 页码（从1开始） |
| size | int | 否 | 20 | 每页数量 |

### 分页响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [],
    "totalElements": 100,
    "totalPages": 5,
    "size": 20,
    "number": 0
  }
}
```

---

## 认证说明

### JWT Token认证（待实现）

登录成功后，服务器返回JWT Token，后续请求需在Header中携带：

```
Authorization: Bearer {token}
```

### 当前状态

目前测试接口和TMDB接口已放行，无需认证。用户相关接口待实现JWT认证。

---

## 测试接口

### GET /hello

测试接口，验证服务器是否正常运行。

**请求示例**:
```bash
curl http://localhost:7070/hello
```

**响应示例**:
```
后端项目启动成功！你好，电影社区！
```

---

## TMDB电影接口

### GET /api/tmdb/popular

获取热门电影列表。

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 1 | 页码 |

**请求示例**:
```bash
curl "http://localhost:7070/api/tmdb/popular?page=1"
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
        "id": 278,
        "title": "肖申克的救赎",
        "poster_path": "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
        "vote_average": 8.7,
        "release_date": "1994-09-23"
      }
    ],
    "total_pages": 500,
    "total_results": 10000
  }
}
```

---

### GET /api/tmdb/movie/{movieId}

获取电影详细信息。

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| movieId | int | 是 | TMDB电影ID |

**请求示例**:
```bash
curl "http://localhost:7070/api/tmdb/movie/278"
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 278,
    "title": "肖申克的救赎",
    "original_title": "The Shawshank Redemption",
    "overview": "电影简介...",
    "poster_path": "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
    "backdrop_path": "/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg",
    "vote_average": 8.7,
    "release_date": "1994-09-23",
    "runtime": 142,
    "genres": [
      {"id": 18, "name": "剧情"},
      {"id": 80, "name": "犯罪"}
    ]
  }
}
```

---

### GET /api/tmdb/search

搜索电影。

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| keyword | string | 是 | - | 搜索关键词 |
| page | int | 否 | 1 | 页码 |

**请求示例**:
```bash
curl "http://localhost:7070/api/tmdb/search?keyword=星际穿越&page=1"
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
        "poster_path": "/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
        "vote_average": 8.4
      }
    ]
  }
}
```

---

### GET /api/tmdb/movie/{movieId}/credits

获取电影演职人员信息。

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| movieId | int | 是 | TMDB电影ID |

**请求示例**:
```bash
curl "http://localhost:7070/api/tmdb/movie/278/credits"
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "cast": [
      {
        "name": "蒂姆·罗宾斯",
        "character": "Andy Dufresne",
        "profile_path": "/path.jpg"
      }
    ],
    "crew": [
      {
        "name": "弗兰克·德拉邦特",
        "job": "Director"
      }
    ]
  }
}
```

---

### GET /api/tmdb/now-playing

获取正在上映的电影。

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 1 | 页码 |

**请求示例**:
```bash
curl "http://localhost:7070/api/tmdb/now-playing?page=1"
```

---

### GET /api/tmdb/top-rated

获取高分电影。

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 1 | 页码 |

**请求示例**:
```bash
curl "http://localhost:7070/api/tmdb/top-rated?page=1"
```

---

## 用户接口（待开发）

### POST /api/auth/register

用户注册。

**请求体**:
```json
{
  "username": "testuser",
  "phone": "13800138000",
  "password": "password123"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "phone": "13800138000",
    "avatar": null
  }
}
```

---

### POST /api/auth/login

用户登录。

**请求体**:
```json
{
  "phone": "13800138000",
  "password": "password123"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "username": "testuser",
      "phone": "13800138000",
      "avatar": "http://example.com/avatar.jpg"
    }
  }
}
```

---

### GET /api/user/profile

获取当前用户信息。

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "testuser",
    "phone": "13800138000",
    "avatar": "http://example.com/avatar.jpg",
    "createdAt": "2026-02-26T10:00:00"
  }
}
```

---

## 动态接口（待开发）

### GET /api/feeds

获取动态列表。

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 1 | 页码 |
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
          "username": "电影爱好者",
          "avatar": "http://example.com/avatar.jpg"
        },
        "movie": {
          "id": 1,
          "title": "星际穿越",
          "posterUrl": "http://example.com/poster.jpg"
        },
        "content": "刚看完这部科幻大片，视觉效果太震撼了！",
        "rating": 9.3,
        "likeCount": 234,
        "shareCount": 23,
        "commentCount": 45,
        "createdAt": "2026-02-26T10:00:00"
      }
    ],
    "totalElements": 100,
    "totalPages": 5
  }
}
```

---

### POST /api/feeds

发布动态。

**请求头**:
```
Authorization: Bearer {token}
```

**请求体**:
```json
{
  "movieId": 1,
  "content": "这部电影太棒了！",
  "rating": 9.5
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "发布成功",
  "data": {
    "id": 1,
    "content": "这部电影太棒了！",
    "rating": 9.5,
    "createdAt": "2026-02-26T10:00:00"
  }
}
```

---

### GET /api/feeds/{feedId}

获取动态详情。

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| feedId | long | 是 | 动态ID |

---

### DELETE /api/feeds/{feedId}

删除动态。

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| feedId | long | 是 | 动态ID |

---

## 评论接口（待开发）

### GET /api/feeds/{feedId}/comments

获取动态的评论列表。

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| feedId | long | 是 | 动态ID |

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
        "username": "影迷小李",
        "avatar": "http://example.com/avatar.jpg"
      },
      "content": "我也超级喜欢这部电影！",
      "likeCount": 23,
      "createdAt": "2026-02-26T11:00:00"
    }
  ]
}
```

---

### POST /api/feeds/{feedId}/comments

发表评论。

**请求头**:
```
Authorization: Bearer {token}
```

**请求体**:
```json
{
  "content": "我也超级喜欢这部电影！"
}
```

---

### DELETE /api/comments/{commentId}

删除评论。

**请求头**:
```
Authorization: Bearer {token}
```

---

## 点赞接口（待开发）

### POST /api/feeds/{feedId}/like

点赞动态。

**请求头**:
```
Authorization: Bearer {token}
```

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

### DELETE /api/feeds/{feedId}/like

取消点赞。

**请求头**:
```
Authorization: Bearer {token}
```

---

## 收藏接口（待开发）

### GET /api/favorites

获取我的收藏列表。

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 1 | 页码 |
| size | int | 否 | 20 | 每页数量 |

---

### POST /api/movies/{movieId}/favorite

收藏电影。

**请求头**:
```
Authorization: Bearer {token}
```

---

### DELETE /api/movies/{movieId}/favorite

取消收藏。

**请求头**:
```
Authorization: Bearer {token}
```

---

## 活动接口（待开发）

### GET /api/events

获取活动列表。

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 1 | 页码 |
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
        "title": "《星际穿越》IMAX重映观影团",
        "imageUrl": "http://example.com/event.jpg",
        "eventDate": "2026-03-15T19:30:00",
        "location": "北京国际影城IMAX厅",
        "participants": 58,
        "maxParticipants": 80,
        "type": "观影团"
      }
    ]
  }
}
```

---

### GET /api/events/{eventId}

获取活动详情。

---

### POST /api/events/{eventId}/join

参加活动。

**请求头**:
```
Authorization: Bearer {token}
```

---

### DELETE /api/events/{eventId}/join

取消参加。

**请求头**:
```
Authorization: Bearer {token}
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 1001 | 用户名已存在 |
| 1002 | 手机号已存在 |
| 1003 | 用户名或密码错误 |
| 1004 | Token无效或已过期 |
| 2001 | 动态不存在 |
| 2002 | 无权限操作 |
| 3001 | 电影不存在 |
| 4001 | 活动不存在 |
| 4002 | 活动已满员 |

---

**文档维护**: 本文档将随接口开发自动更新

