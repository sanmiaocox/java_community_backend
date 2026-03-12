# 电影交流社区后端API文档

> **最后更新**: 2026-03-12  
> **基础URL**: 本机url`http://localhost:7070`  安卓虚拟机url‘http://10.0.2.2:7070’
> **API版本**: v1.0

---

### 12. 动态管理接口

#### POST /api/feeds

发布动态。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**:
```json
{
  "content": "刚看完这部科幻大片，视觉效果太震撼了！",
  "images": ["img1.jpg", "img2.jpg"],
  "movieId": 100,
  "eventId": null
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| content | string | 是 | 动态内容，最多2000字符 |
| images | array | 否 | 图片文件名数组，最多4张 |
| movieId | long | 否 | 关联电影ID（本地数据库ID，不是tmdbId） |
| eventId | long | 否 | 关联活动ID |

**注意**：
- movieId是本地数据库的电影ID，不是tmdbId
- 前端需要先调用 POST /api/movies/save 保存电影到本地数据库
- 然后使用返回的本地ID发布动态

**响应示例**:
```json
{
  "code": 200,
  "message": "发布成功",
  "data": {
    "id": 1,
    "user": {
      "id": 1,
      "userCode": "0001",
      "username": "电影爱好者",
      "avatar": "avatar.jpg"
    },
    "movie": {
      "id": 100,
      "tmdbId": 157336,
      "title": "星际穿越",
      "posterUrl": "poster.jpg",
      "rating": 8.4
    },
    "event": null,
    "content": "刚看完这部科幻大片，视觉效果太震撼了！",
    "images": ["img1.jpg", "img2.jpg"],
    "likeCount": 0,
    "commentCount": 0,
    "isLiked": false,
    "createdAt": "2026-03-09T10:00:00",
    "updatedAt": "2026-03-09T10:00:00"
  }
}
```

---

#### GET /api/feeds

获取动态列表（关注人的动态）。

**是否需要Token**: ✅ 是

**功能说明**：
- 显示当前用户关注的人发布的动态
- 如果用户没有关注任何人，返回空列表
- 按发布时间倒序排列

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 0 | 页码 |
| size | int | 否 | 20 | 每页数量 |

**请求示例**:
```bash
GET /api/feeds?page=0&size=20
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
        "user": {
          "id": 2,
          "userCode": "0002",
          "username": "影迷小李",
          "avatar": "avatar.jpg"
        },
        "movie": {
          "id": 100,
          "tmdbId": 157336,
          "title": "星际穿越",
          "posterUrl": "poster.jpg",
          "rating": 8.4
        },
        "event": null,
        "content": "刚看完这部科幻大片，视觉效果太震撼了！",
        "images": ["img1.jpg", "img2.jpg"],
        "likeCount": 234,
        "commentCount": 45,
        "isLiked": true,
        "createdAt": "2026-03-09T10:00:00",
        "updatedAt": "2026-03-09T10:00:00"
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

#### GET /api/feeds/{feedId}

获取动态详情。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| feedId | long | 是 | 动态ID |

**响应示例**: 同发布动态

---

#### DELETE /api/messages/groups/{groupId}

解散群聊（仅群主可操作，删除后群聊消息全部清除）。

**是否需要Token**: ✅ 是

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | long | 是 | 群组ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 待实现接口

暂无待实现接口，核心功能已全部完成。

---

/api/feeds/{feedId}

删除动态。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| feedId | long | 是 | 动态ID |

**注意**：
- 只能删除自己的动态
- 删除动态会级联删除所有评论和点赞

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

#### GET /api/users/{userId}/feeds

获取用户动态列表。

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

**响应示例**: 同获取动态列表

---

#### GET /api/movies/{movieId}/feeds

获取电影相关动态。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| movieId | long | 是 | 电影ID（本地数据库ID） |

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 0 | 页码 |
| size | int | 否 | 20 | 每页数量 |

**响应示例**: 同获取动态列表

---

### 13. 点赞功能接口

#### POST /api/feeds/{feedId}/like

点赞动态。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| feedId | long | 是 | 动态ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "点赞成功",
  "data": {
    "isLiked": true,
    "likeCount": 235
  }
}
```

---

#### DELETE /api/feeds/{feedId}/like

取消点赞动态。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| feedId | long | 是 | 动态ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "取消点赞成功",
  "data": {
    "isLiked": false,
    "likeCount": 234
  }
}
```

---

#### GET /api/feeds/{feedId}/like/status

检查是否点赞动态。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| feedId | long | 是 | 动态ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "isLiked": true,
    "likeCount": 235
  }
}
```

---

#### POST /api/comments/{commentId}/like

点赞评论。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| commentId | long | 是 | 评论ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "点赞成功",
  "data": {
    "isLiked": true,
    "likeCount": 24
  }
}
```

---

#### DELETE /api/comments/{commentId}/like

取消点赞评论。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| commentId | long | 是 | 评论ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "取消点赞成功",
  "data": {
    "isLiked": false,
    "likeCount": 23
  }
}
```

---

#### GET /api/comments/{commentId}/like/status

检查是否点赞评论。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| commentId | long | 是 | 评论ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "isLiked": true,
    "likeCount": 24
  }
}
```

---

### 14. 评论功能接口

#### POST /api/feeds/{feedId}/comments

发表评论。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| feedId | long | 是 | 动态ID |

**请求体**:
```json
{
  "content": "我也超级喜欢这部电影！"
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| content | string | 是 | 评论内容，最多500字符 |

**响应示例**:
```json
{
  "code": 200,
  "message": "评论成功",
  "data": {
    "id": 1,
    "user": {
      "id": 2,
      "userCode": "0002",
      "username": "影迷小李",
      "avatar": "avatar.jpg"
    },
    "content": "我也超级喜欢这部电影！",
    "likeCount": 0,
    "isLiked": false,
    "createdAt": "2026-03-09T11:00:00",
    "updatedAt": "2026-03-09T11:00:00"
  }
}
```

---

#### GET /api/feeds/{feedId}/comments

获取动态的评论列表。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| feedId | long | 是 | 动态ID |

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
          "id": 2,
          "userCode": "0002",
          "username": "影迷小李",
          "avatar": "avatar.jpg"
        },
        "content": "我也超级喜欢这部电影！",
        "likeCount": 23,
        "isLiked": true,
        "createdAt": "2026-03-09T11:00:00",
        "updatedAt": "2026-03-09T11:00:00"
      }
    ],
    "totalElements": 45,
    "totalPages": 3,
    "size": 20,
    "number": 0
  }
}
```

---

#### DELETE /api/comments/{commentId}

删除评论。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| commentId | long | 是 | 评论ID |

**注意**：
- 只能删除自己的评论
- 删除评论会同时删除该评论的所有点赞

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

## 📋 目录

1. [接口规范](#接口规范)
2. [认证说明](#认证说明)
3. [已实现接口](#已实现接口)
   - [测试接口](#1-测试接口)
   - [用户认证接口](#2-用户认证接口)
   - [用户信息接口](#3-用户信息接口)
     - 获取当前用户信息
     - 更新个人资料
     - 获取指定用户信息
     - 修改密码
     - 修改手机号
   - [关注/粉丝/好友接口](#4-关注粉丝好友接口)
   - [收藏夹管理接口](#5-收藏夹管理接口)
   - [收藏项管理接口](#6-收藏项管理接口)
   - [看过记录接口](#7-看过记录接口)
   - [TMDB电影数据接口](#8-tmdb电影数据接口)
   - [文件上传接口](#9-文件上传接口)
   - [电影管理接口](#10-电影管理接口)
   - [活动管理接口](#11-活动管理接口)
   - [动态管理接口](#12-动态管理接口)
   - [点赞功能接口](#13-点赞功能接口)
   - [评论功能接口](#14-评论功能接口)
   - [通知接口](#15-通知接口)
   - [消息接口（私聊+群聊）](#16-消息接口私聊群聊)
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

#### PUT /api/users/password

修改密码。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**:
```json
{
  "oldPassword": "123456",
  "newPassword": "654321"
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 | 校验规则 |
|------|------|------|------|---------|
| oldPassword | string | 是 | 旧密码 | - |
| newPassword | string | 是 | 新密码 | 6-20个字符 |

**响应示例**:
```json
{
  "code": 200,
  "message": "密码修改成功",
  "data": null
}
```

**错误响应**:
```json
{
  "code": 1005,
  "message": "旧密码错误",
  "data": null
}
```

```json
{
  "code": 1006,
  "message": "新密码不能与旧密码相同",
  "data": null
}
```

**注意事项**:
- 修改密码后，旧Token仍然有效，直到过期
- 建议前端在修改密码成功后，提示用户重新登录
- 新密码不能与旧密码相同

---

#### PUT /api/users/phone

修改手机号。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**:
```json
{
  "newPhone": "13900139000",
  "password": "123456"
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 | 校验规则 |
|------|------|------|------|---------|
| newPhone | string | 是 | 新手机号 | 11位数字，1开头 |
| password | string | 是 | 当前密码（用于验证身份） | - |

**响应示例**:
```json
{
  "code": 200,
  "message": "手机号修改成功",
  "data": null
}
```

**错误响应**:
```json
{
  "code": 1003,
  "message": "密码错误",
  "data": null
}
```

```json
{
  "code": 1002,
  "message": "手机号已被使用",
  "data": null
}
```

```json
{
  "code": 1007,
  "message": "新手机号不能与当前手机号相同",
  "data": null
}
```

**注意事项**:
- 需要提供当前密码进行身份验证
- 新手机号不能与当前手机号相同
- 新手机号不能已被其他用户使用
- 修改成功后，下次登录需使用新手机号

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

检查用户是否看过某部电影（根据本地ID）。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| movieId | long | 是 | 本地数据库电影ID |

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

**使用场景**:
- 当已知本地电影ID时使用
- 用于收藏夹、活动等场景

---

#### GET /api/watched/check/tmdb/{tmdbId}

检查用户是否看过某部电影（根据TMDB ID）。

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
GET /api/watched/check/tmdb/20982
```

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

**使用场景**:
- 前端从TMDB获取电影信息后，使用TMDB ID检查看过状态
- 避免需要先查询本地电影ID的额外步骤
- **推荐使用此接口**，因为前端通常只有TMDB ID

**重要说明**:
- 如果电影不存在于本地数据库，返回 `isWatched: false`
- 只有当电影已保存到本地且用户标记为看过时，才返回 `isWatched: true`

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

#### GET /api/tmdb/discover/movie

发现电影（高级筛选）。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| language | string | 否 | zh-CN | 语言代码 |
| sort_by | string | 否 | - | 排序方式，如 popularity.desc, vote_average.desc, release_date.desc |
| with_genres | string | 否 | - | 类型ID，多个用逗号分隔，如 28,12（动作+冒险） |
| primary_release_year | int | 否 | - | 上映年份 |
| vote_average.gte | double | 否 | - | 最低评分（0.0-10.0） |
| vote_average.lte | double | 否 | - | 最高评分（0.0-10.0） |
| page | int | 否 | 1 | 页码 |

**常用排序方式**:
- `popularity.desc` - 按人气降序
- `popularity.asc` - 按人气升序
- `vote_average.desc` - 按评分降序
- `vote_average.asc` - 按评分升序
- `release_date.desc` - 按上映日期降序
- `release_date.asc` - 按上映日期升序

**请求示例**:
```bash
# 筛选2024年上映的动作片，评分7分以上，按人气排序
GET /api/tmdb/discover/movie?sort_by=popularity.desc&with_genres=28&primary_release_year=2024&vote_average.gte=7.0&page=1

# 筛选科幻+冒险类型，评分8-10分
GET /api/tmdb/discover/movie?with_genres=878,12&vote_average.gte=8.0&vote_average.lte=10.0&page=1
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
        "popularity": 150.5,
        "genre_ids": [878, 18, 12]
      }
    ],
    "total_pages": 50,
    "total_results": 1000
  }
}
```

**使用场景**:
- 高级筛选功能
- 按类型、年份、评分等条件筛选电影
- 发现符合特定条件的电影

---

#### GET /api/tmdb/genre/movie/list

获取电影类型列表。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| language | string | 否 | zh-CN | 语言代码 |

**请求示例**:
```bash
GET /api/tmdb/genre/movie/list?language=zh-CN
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "genres": [
      {
        "id": 28,
        "name": "动作"
      },
      {
        "id": 12,
        "name": "冒险"
      },
      {
        "id": 16,
        "name": "动画"
      },
      {
        "id": 35,
        "name": "喜剧"
      },
      {
        "id": 80,
        "name": "犯罪"
      },
      {
        "id": 99,
        "name": "纪录"
      },
      {
        "id": 18,
        "name": "剧情"
      },
      {
        "id": 10751,
        "name": "家庭"
      },
      {
        "id": 14,
        "name": "奇幻"
      },
      {
        "id": 36,
        "name": "历史"
      },
      {
        "id": 27,
        "name": "恐怖"
      },
      {
        "id": 10402,
        "name": "音乐"
      },
      {
        "id": 9648,
        "name": "悬疑"
      },
      {
        "id": 10749,
        "name": "爱情"
      },
      {
        "id": 878,
        "name": "科幻"
      },
      {
        "id": 10770,
        "name": "电视电影"
      },
      {
        "id": 53,
        "name": "惊悚"
      },
      {
        "id": 10752,
        "name": "战争"
      },
      {
        "id": 37,
        "name": "西部"
      }
    ]
  }
}
```

**使用场景**:
- 筛选器的类型选择下拉框
- 显示电影类型标签
- 配合 discover 接口进行类型筛选

**常用类型ID**:
- 28 - 动作
- 12 - 冒险
- 16 - 动画
- 35 - 喜剧
- 80 - 犯罪
- 18 - 剧情
- 14 - 奇幻
- 27 - 恐怖
- 10749 - 爱情
- 878 - 科幻
- 53 - 惊悚

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

### 10. 电影管理接口

#### POST /api/movies/save

保存电影到数据库。

从TMDB获取电影信息并保存到本地数据库。如果电影已存在（根据tmdbId判断），则直接返回已存在的电影信息，不会重复写入。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**:
```json
{
  "tmdbId": 157336
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| tmdbId | int | 是 | TMDB电影ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "电影保存成功",
  "data": {
    "id": 1,
    "tmdbId": 157336,
    "title": "星际穿越",
    "originalTitle": "Interstellar",
    "posterUrl": "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
    "rating": 8.4,
    "ratingSource": "TMDB",
    "releaseDate": "2014-11-07",
    "year": "2014",
    "genres": "科幻,剧情,冒险",
    "genre": "科幻",
    "region": "美国,英国,加拿大",
    "languages": "英语",
    "directors": null,
    "actors": null,
    "synopsis": "随着地球自然环境的恶化，人类面临着无法生存的威胁...",
    "tmdbUrl": "https://www.themoviedb.org/movie/157336",
    "createdAt": "2026-03-05T10:00:00",
    "updatedAt": "2026-03-05T10:00:00"
  }
}
```

**如果电影已存在**:
```json
{
  "code": 200,
  "message": "电影已存在",
  "data": {
    "id": 1,
    "tmdbId": 157336,
    "title": "星际穿越",
    ...
  }
}
```

**错误响应**:
```json
{
  "code": 500,
  "message": "无法从TMDB获取电影信息: tmdbId=999999",
  "data": null
}
```

**使用场景**:
- 用户在创建活动前，需要先保存电影到数据库
- 用户收藏电影时，自动调用此接口保存电影
- 用户标记看过电影时，自动调用此接口保存电影
- 前端从TMDB搜索到电影后，需要保存到本地数据库才能使用

**注意事项**:
- 接口会自动检查电影是否已存在（根据tmdbId），避免重复写入
- 如果TMDB API调用失败，会返回500错误
- 保存的电影信息包括标题、海报、评分、类型、地区等基本信息
- 导演和演员信息需要单独调用TMDB演职人员接口获取

---

#### GET /api/movies/{movieId}

获取本地电影详情（根据本地ID）。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| movieId | long | 是 | 本地电影ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "tmdbId": 157336,
    "title": "星际穿越",
    "originalTitle": "Interstellar",
    "posterUrl": "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
    "rating": 8.4,
    "ratingSource": "TMDB",
    "releaseDate": "2014-11-07",
    "year": "2014",
    "genres": "科幻,剧情,冒险",
    "genre": "科幻",
    "region": "美国,英国,加拿大",
    "languages": "英语",
    "directors": null,
    "actors": null,
    "synopsis": "随着地球自然环境的恶化，人类面临着无法生存的威胁...",
    "tmdbUrl": "https://www.themoviedb.org/movie/157336",
    "createdAt": "2026-03-05T10:00:00",
    "updatedAt": "2026-03-05T10:00:00"
  }
}
```

---

#### GET /api/movies/tmdb/{tmdbId}

获取本地电影详情（根据TMDB ID）。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| tmdbId | int | 是 | TMDB电影ID |

**响应示例**: 同上

**使用场景**:
- 前端已知TMDB ID，需要获取本地电影详情
- 检查电影是否已保存到本地数据库

---

#### GET /api/movies

获取本地电影列表（分页）。

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

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "tmdbId": 157336,
        "title": "星际穿越",
        "originalTitle": "Interstellar",
        "posterUrl": "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
        "rating": 8.4,
        "ratingSource": "TMDB",
        "releaseDate": "2014-11-07",
        "year": "2014",
        "genres": "科幻,剧情,冒险",
        "genre": "科幻",
        "region": "美国,英国,加拿大",
        "languages": "英语",
        "directors": null,
        "actors": null,
        "synopsis": "随着地球自然环境的恶化，人类面临着无法生存的威胁...",
        "tmdbUrl": "https://www.themoviedb.org/movie/157336",
        "createdAt": "2026-03-05T10:00:00",
        "updatedAt": "2026-03-05T10:00:00"
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

#### GET /api/movies/search

搜索本地电影。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| keyword | string | 是 | - | 搜索关键词（标题、导演、演员） |
| page | int | 否 | 0 | 页码 |
| size | int | 否 | 20 | 每页数量 |

**请求示例**:
```bash
GET /api/movies/search?keyword=星际穿越&page=0&size=20
```

**响应示例**: 同获取电影列表

**使用场景**:
- 在本地数据库中搜索已保存的电影
- 支持按标题、导演、演员搜索

---

### 11. 活动管理接口

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
  "registrationDeadline": "2026-03-14T23:59:59",
  "endTime": "2026-03-15T22:00:00",
  "location": "北京国际影城IMAX厅",
  "maxParticipants": 80,
  "type": "观影团",
  "description": "一起去看IMAX版星际穿越！",
  "registrationNotice": "请提前15分钟到场，携带有效身份证件。",
  "movieId": 100
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| title | string | 是 | 活动标题，最多200字符 |
| imageUrl | string | 否 | 活动封面图片文件名 |
| eventDate | datetime | 是 | 活动开始时间，必须是未来时间 |
| registrationDeadline | datetime | 否 | 报名截止时间，过期后不能再参加 |
| endTime | datetime | 否 | 活动结束时间 |
| location | string | 是 | 活动地点，最多200字符 |
| maxParticipants | int | 是 | 最大参与人数，默认100 |
| type | string | 是 | 活动类型，如"观影团"、"影评征集"、"线下活动" |
| description | string | 否 | 活动描述，最多2000字符 |
| registrationNotice | string | 否 | 报名须知，最多2000字符 |
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
    "registrationDeadline": "2026-03-14T23:59:59",
    "endTime": "2026-03-15T22:00:00",
    "location": "北京国际影城IMAX厅",
    "participants": 0,
    "maxParticipants": 80,
    "type": "观影团",
    "description": "一起去看IMAX版星际穿越！",
    "registrationNotice": "请提前15分钟到场，携带有效身份证件。",
    "movieId": 100,
    "movieTmdbId": 157336,
    "movieTitle": "星际穿越",
    "moviePosterUrl": "poster-abc123.jpg",
    "creatorId": 1,
    "creatorUsername": "电影爱好者",
    "creatorAvatar": "avatar-abc123.jpg",
    "createdAt": "2026-03-04T10:00:00",
    "updatedAt": "2026-03-04T10:00:00",
    "isParticipant": false,
    "isCreator": true
  }
}
```

**注意**: 
- 活动必须关联一个电影
- 响应中会包含关联电影的基本信息（标题、海报）
- 响应中会包含创建人信息（ID、用户名、头像）
- `isCreator` 表示当前用户是否是活动创建人
- `isParticipant` 表示当前用户是否已参加活动

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
        "registrationDeadline": "2026-03-14T23:59:59",
        "endTime": "2026-03-15T22:00:00",
        "location": "北京国际影城IMAX厅",
        "participants": 58,
        "maxParticipants": 80,
        "type": "观影团",
        "description": "一起去看IMAX版星际穿越！",
        "registrationNotice": "请提前15分钟到场，携带有效身份证件。",
        "movieId": 100,
        "movieTmdbId": 157336,
        "movieTitle": "星际穿越",
        "moviePosterUrl": "poster-abc123.jpg",
        "creatorId": 1,
        "creatorUsername": "电影爱好者",
        "creatorAvatar": "avatar-abc123.jpg",
        "createdAt": "2026-03-04T10:00:00",
        "updatedAt": "2026-03-04T10:00:00",
        "isParticipant": true,
        "isCreator": false
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

更新活动（只有创建人可以修改）。

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
  "registrationDeadline": "2026-03-15T23:59:59",
  "endTime": "2026-03-16T22:00:00",
  "location": "新的地点",
  "maxParticipants": 100,
  "type": "观影团",
  "description": "更新后的描述",
  "registrationNotice": "更新后的报名须知",
  "movieId": 101
}
```

**注意**: 
- 所有字段都是可选的，只更新提供的字段
- 只有活动创建人可以修改活动
- 如果不是创建人，会返回错误：`只有创建人可以修改活动`
}
```

**注意**: 所有字段都是可选的，只更新提供的字段

**响应示例**: 同创建活动

---

#### DELETE /api/events/{eventId}

删除活动（只有创建人可以删除）。

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

**注意**: 
- 只有活动创建人可以删除活动
- 如果不是创建人，会返回错误：`只有创建人可以删除活动`

---

#### POST /api/events/{eventId}/join

参加活动。

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
  "participantPhone": "13800138001",
  "participantNickname": "张三",
  "participantWechat": "zhangsan_wx",
  "participantQQ": "123456789"
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| participantPhone | string | 是 | 参与人手机号（11位） |
| participantNickname | string | 是 | 参与人昵称，最多50字符 |
| participantWechat | string | 否 | 参与人微信号，最多50字符 |
| participantQq | string | 否 | 参与人QQ号，最多20字符 |

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

```json
{
  "code": 400,
  "message": "报名已截止",
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
      "participantPhone": "13800138001",
      "participantNickname": "小李",
      "participantWechat": "xiaoli_wx",
      "participantQq": "123456789",
      "joinedAt": "2026-03-04T11:00:00"
    },
    {
      "id": 2,
      "userId": 3,
      "username": "电影达人",
      "userCode": "0003",
      "avatar": "avatar-ghi789.jpg",
      "participantPhone": "13900139001",
      "participantNickname": "达人",
      "participantWechat": "daren_wx",
      "participantQq": null,
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

#### GET /api/events/user/{userId}/created

获取用户创建的活动列表。

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

**使用场景**:
- 查看某个用户创建的所有活动
- 在个人主页展示"我创建的活动"
- 区分"我创建的"和"我参与的"活动

---

### 15. 通知接口

#### GET /api/notifications

查询当前用户所有通知（分页，含已读未读）。

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

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "type": "LIKE_FEED",
        "sender": {
          "id": 2,
          "username": "影迷小李",
          "avatar": "avatar.jpg"
        },
        "targetType": "FEED",
        "targetId": 10,
        "content": null,
        "isRead": false,
        "createdAt": "2026-03-11T10:00:00"
      },
      {
        "id": 2,
        "type": "COMMENT_FEED",
        "sender": {
          "id": 3,
          "username": "电影达人",
          "avatar": "avatar2.jpg"
        },
        "targetType": "FEED",
        "targetId": 10,
        "content": "我也超级喜欢这部电影！...",
        "isRead": true,
        "createdAt": "2026-03-11T09:00:00"
      }
    ],
    "totalElements": 50,
    "totalPages": 3,
    "size": 20,
    "number": 0
  }
}
```

**通知类型说明**:
| type | 含义 | targetType | targetId |
|------|------|-----------|----------|
| LIKE_FEED | 点赞了你的动态 | FEED | 动态ID |
| LIKE_COMMENT | 点赞了你的评论 | COMMENT | 评论ID |
| COMMENT_FEED | 评论了你的动态 | FEED | 动态ID |
| FOLLOW | 关注了你 | USER | 用户ID |
| EVENT_JOIN | 有人报名了你的活动 | EVENT | 活动ID |
| EVENT_QUIT | 有人退出了你的活动 | EVENT | 活动ID |
| SYSTEM | 系统公告 | null | null |

---

#### GET /api/notifications/unread

查询当前用户未读通知（分页）。

**是否需要Token**: ✅ 是

**请求参数**: 同查询所有通知

**响应示例**: 同查询所有通知（只返回未读的）

---

#### GET /api/notifications/unread/count

获取未读通知数量（用于显示角标）。

**是否需要Token**: ✅ 是

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": 5
}
```

---

#### PUT /api/notifications/{id}/read

将单条通知标记为已读。

**是否需要Token**: ✅ 是

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | long | 是 | 通知ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

#### PUT /api/notifications/read-all

将当前用户所有通知全部标记为已读。

**是否需要Token**: ✅ 是

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 16. 消息接口（私聊+群聊）

---

#### POST /api/messages/send

发送消息（私聊或群聊统一入口，由 `chatType` 区分）。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体（私聊）**:
```json
{
  "chatType": "PRIVATE",
  "targetUserId": 2,
  "content": "你好，一起去看电影吗？",
  "type": "TEXT"
}
```

**请求体（群聊）**:
```json
{
  "chatType": "GROUP",
  "groupId": 1,
  "content": "大家好！",
  "type": "TEXT"
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| chatType | string | 是 | PRIVATE=私聊，GROUP=群聊 |
| targetUserId | long | PRIVATE时必填 | 对方用户ID |
| groupId | long | GROUP时必填 | 群组ID |
| content | string | 是 | 消息内容 |
| type | string | 否 | TEXT（默认）/ IMAGE |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "chatType": "PRIVATE",
    "sender": {
      "id": 1,
      "username": "我",
      "avatar": "avatar.jpg"
    },
    "content": "你好，一起去看电影吗？",
    "type": "TEXT",
    "isRecalled": false,
    "createdAt": "2026-03-11T10:00:00"
  }
}
```

---

#### DELETE /api/messages/{messageId}/recall

撤回消息（仅发送者本人可撤回）。

**是否需要Token**: ✅ 是

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| messageId | long | 是 | 消息ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

#### GET /api/messages/conversations

查询当前用户的私信会话列表（按最后消息时间倒序）。

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
        "conversationId": 1,
        "otherUser": {
          "id": 2,
          "username": "影迷小李",
          "avatar": "avatar.jpg",
          "userCode": "0002"
        },
        "lastMessage": "你好，一起去看电影吗？",
        "lastMessageAt": "2026-03-11T10:00:00",
        "unreadCount": 2
      }
    ],
    "totalElements": 10,
    "totalPages": 1,
    "size": 20,
    "number": 0
  }
}
```

---

#### GET /api/messages/conversations/{conversationId}

查询私信会话的消息列表（分页，最新消息在前；同时自动清零当前用户未读数）。

**是否需要Token**: ✅ 是

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| conversationId | long | 是 | 会话ID |

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 0 | 页码 |
| size | int | 否 | 30 | 每页数量 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 5,
        "chatType": "PRIVATE",
        "sender": {
          "id": 2,
          "username": "影迷小李",
          "avatar": "avatar.jpg"
        },
        "content": "好啊！什么时候？",
        "type": "TEXT",
        "isRecalled": false,
        "createdAt": "2026-03-11T10:05:00"
      },
      {
        "id": 4,
        "chatType": "PRIVATE",
        "sender": {
          "id": 1,
          "username": "我",
          "avatar": "my_avatar.jpg"
        },
        "content": "消息已撤回",
        "type": "TEXT",
        "isRecalled": true,
        "createdAt": "2026-03-11T10:02:00"
      }
    ],
    "totalElements": 20,
    "totalPages": 1,
    "size": 30,
    "number": 0
  }
}
```

---

#### POST /api/messages/groups

创建群聊。

**是否需要Token**: ✅ 是

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**:
```json
{
  "name": "星际穿越观影团",
  "avatar": "group_avatar.jpg",
  "memberIds": [2, 3, 4],
  "maxMembers": 100
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | string | 是 | 群名称，最多100字符 |
| avatar | string | 否 | 群头像文件名 |
| memberIds | array | 是 | 初始邀请的成员ID列表（不含创建者自己） |
| maxMembers | int | 否 | 最大成员数，默认100 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "星际穿越观影团",
    "avatar": "group_avatar.jpg",
    "owner": {
      "id": 1,
      "username": "电影爱好者",
      "avatar": "avatar.jpg"
    },
    "maxMembers": 100,
    "memberCount": 4,
    "unreadCount": 0,
    "myRole": "OWNER",
    "createdAt": "2026-03-11T10:00:00",
    "members": [
      {
        "userId": 1,
        "username": "电影爱好者",
        "avatar": "avatar.jpg",
        "userCode": "0001",
        "role": "OWNER",
        "joinedAt": "2026-03-11T10:00:00"
      },
      {
        "userId": 2,
        "username": "影迷小李",
        "avatar": "avatar2.jpg",
        "userCode": "0002",
        "role": "MEMBER",
        "joinedAt": "2026-03-11T10:00:00"
      }
    ]
  }
}
```

---

#### GET /api/messages/groups

查询当前用户加入的所有群聊。

**是否需要Token**: ✅ 是

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "星际穿越观影团",
      "avatar": "group_avatar.jpg",
      "owner": {
        "id": 1,
        "username": "电影爱好者",
        "avatar": "avatar.jpg"
      },
      "maxMembers": 100,
      "memberCount": 4,
      "unreadCount": 3,
      "myRole": "OWNER",
      "createdAt": "2026-03-11T10:00:00",
      "members": null
    }
  ]
}
```

---

#### GET /api/messages/groups/event/{eventId}

通过活动ID查询对应群聊。

**是否需要Token**: ✅ 是

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| eventId | long | 是 | 活动ID |

**响应示例**: 同创建群聊（含 members 列表）

**使用场景**:
- 用户参加活动后，通过活动ID直接进入对应群聊
- 活动详情页「进入群聊」按钮

---

#### GET /api/messages/groups/{groupId}

查询群聊详情（含成员列表）。

**是否需要Token**: ✅ 是

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | long | 是 | 群组ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "星际穿越观影团",
    "avatar": "group_avatar.jpg",
    "owner": {
      "id": 1,
      "username": "电影爱好者",
      "avatar": "avatar.jpg"
    },
    "maxMembers": 100,
    "memberCount": 4,
    "unreadCount": 0,
    "myRole": "OWNER",
    "eventId": 5,
    "createdAt": "2026-03-11T10:00:00",
    "members": [
      {
        "userId": 1,
        "username": "电影爱好者",
        "avatar": "avatar.jpg",
        "userCode": "0001",
        "role": "OWNER",
        "joinedAt": "2026-03-11T10:00:00"
      },
      {
        "userId": 2,
        "username": "影迷小李",
        "avatar": "avatar2.jpg",
        "userCode": "0002",
        "role": "MEMBER",
        "joinedAt": "2026-03-11T10:05:00"
      },
      {
        "userId": 3,
        "username": "电影达人",
        "avatar": "avatar3.jpg",
        "userCode": "0003",
        "role": "MEMBER",
        "joinedAt": "2026-03-11T10:10:00"
      },
      {
        "userId": 4,
        "username": "观影爱好者",
        "avatar": "avatar4.jpg",
        "userCode": "0004",
        "role": "MEMBER",
        "joinedAt": "2026-03-11T10:15:00"
      }
    ]
  }
}
```

**响应字段说明**:
| 字段 | 类型 | 说明 |
|------|------|------|
| id | long | 群组ID |
| name | string | 群名称 |
| avatar | string | 群头像文件名 |
| owner | object | 群主信息（包含id、username、avatar） |
| maxMembers | int | 最大成员数 |
| memberCount | int | 当前成员数 |
| unreadCount | int | 当前用户未读消息数 |
| myRole | string | 当前用户在群中的角色（OWNER/MEMBER） |
| eventId | long | 关联的活动ID（如果有） |
| createdAt | datetime | 群创建时间 |
| members | array | 成员列表（包含所有成员信息） |

**成员对象字段说明**:
| 字段 | 类型 | 说明 |
|------|------|------|
| userId | long | 用户ID |
| username | string | 用户名 |
| avatar | string | 用户头像文件名 |
| userCode | string | 用户编码 |
| role | string | 成员角色（OWNER/MEMBER） |
| joinedAt | datetime | 加入时间 |

**使用场景**:
- 进入群聊详情页面时调用
- 显示群信息和成员列表
- 检查当前用户的角色（判断是否有管理权限）

---

#### GET /api/messages/groups/{groupId}/messages

查询群聊消息列表（分页，最新消息在前；同时自动清零当前用户未读数）。

**是否需要Token**: ✅ 是

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | long | 是 | 群组ID |

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | int | 否 | 0 | 页码 |
| size | int | 否 | 30 | 每页数量 |

**响应示例**: 同查询私信消息列表，`chatType` 值为 `GROUP`

---

#### GET /api/messages/groups/{groupId}/members

获取群成员列表（分页）。

**是否需要Token**: ✅ 是

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | long | 是 | 群组ID |

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
        "userId": 1,
        "username": "电影爱好者",
        "avatar": "avatar.jpg",
        "userCode": "0001",
        "role": "OWNER",
        "joinedAt": "2026-03-11T10:00:00"
      },
      {
        "userId": 2,
        "username": "影迷小李",
        "avatar": "avatar2.jpg",
        "userCode": "0002",
        "role": "MEMBER",
        "joinedAt": "2026-03-11T10:05:00"
      }
    ],
    "totalElements": 4,
    "totalPages": 1,
    "size": 20,
    "number": 0
  }
}
```

**使用场景**:
- 群聊成员列表页面（支持分页加载）
- 大群成员数量多时，避免一次性加载所有成员

**功能说明**:
- 返回分页的群成员列表
- 每个成员包含：用户ID、用户名、头像、用户编码、角色、加入时间
- 按加入时间升序排列
- 只有群成员才能查看成员列表
---

#### DELETE /api/messages/groups/{groupId}/members/{userId}

踢出群成员（仅群主或管理员）。

**是否需要Token**: ✅ 是

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | long | 是 | 群组ID |
| userId | long | 是 | 要踢出的用户ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

**错误响应**:
```json
{
  "code": 403,
  "message": "只有群主或管理员可以踢出成员",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "不能踢出群主",
  "data": null
}
```

**使用场景**:
- 群主或管理员移除违规成员
- 如果群聊关联了活动，同时移除该用户的活动参与记录

**特殊处理**:
- 如果群聊关联了活动（eventId 不为空），踢出成员时会同时调用 `DELETE /api/events/{eventId}/participants/{userId}` 移除活动参与者
- 不能踢出群主

---

#### POST /api/messages/groups/{groupId}/invite

邀请成员加入群聊（群主或管理员可操作）。

**是否需要Token**: ✅ 是

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | long | 是 | 群组ID |

**请求体**:
```json
[5, 6, 7]
```

（用户ID数组）

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

#### DELETE /api/messages/groups/{groupId}/leave

退出群聊（群主不能直接退出，需先转让或解散）。

**是否需要Token**: ✅ 是

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | long | 是 | 群组ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

#### POST /api/messages/groups/{groupId}/join

主动加入群聊（无需邀请）。

**是否需要Token**: ✅ 是

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | long | 是 | 群组ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

#### DELETE /api/messages/groups/{groupId}

解散群聊（仅群主可操作，删除后群聊消息全部清除）。

**是否需要Token**: ✅ 是

**路径参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| groupId | long | 是 | 群组ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

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
| 1001 | 用户名已存在 | 注册接口、更新资料接口 |
| 1002 | 手机号已存在 | 注册接口、修改手机号接口 |
| 1003 | 手机号或密码错误 | 登录接口、修改手机号接口 |
| 1004 | Token无效或已过期 | 需要认证的接口 |
| 1005 | 旧密码错误 | 修改密码接口 |
| 1006 | 新密码不能与旧密码相同 | 修改密码接口 |
| 1007 | 新手机号不能与当前手机号相同 | 修改手机号接口 |
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
| 7001 | 通知不存在或无权限 | 通知接口 |
| 8001 | 不能与自己发起会话 | 消息接口 |
| 8002 | 目标用户不存在 | 消息接口 |
| 8003 | 你不是该群成员 | 群聊接口 |
| 8004 | 消息不存在 | 消息接口 |
| 8005 | 只能撤回自己发送的消息 | 消息接口 |
| 8006 | 消息已撤回 | 消息接口 |
| 8007 | 只有群主或管理员可以邀请成员 | 群聊接口 |
| 8008 | 超过群人数上限 | 群聊接口 |
| 8009 | 群主请先转让群主后再退出 | 群聊接口 |
| 8010 | 只有群主可以解散群聊 | 群聊接口 |
| 8011 | 只有群主或管理员可以踢出成员 | 群聊接口 |
| 8012 | 不能踢出群主 | 群聊接口 |

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

### 2026-03-12
- 🐛 修复消息已读功能不生效的 Bug
  - **根本原因**: `MessageService.getPrivateMessages` 标注了 `@Transactional(readOnly = true)`，只读事务中写操作（clearUnread）不会提交，导致未读数永远无法清零
  - **修复**: 将 `getPrivateMessages` 改为普通 `@Transactional`，确保清零操作在可写事务中执行
  - **修复**: `GroupMemberRepository` 中 `@Modifying` 方法补充 `@Transactional` 注解，确保批量更新/清零未读数可在 Repository 层独立执行
- 📝 补充文档缺失接口
  - 新增 `GET /api/messages/groups/event/{eventId}` — 通过活动ID查询对应群聊
  - 新增 `POST /api/messages/groups/{groupId}/join` — 主动加入群聊
  - 补全 `DELETE /api/messages/groups/{groupId}` — 解散群聊（原文档条目不完整）
- ✅ 实现群成员管理功能（2个新接口）
  - **GET /api/messages/groups/{groupId}/members** — 获取群成员列表（分页）
    - 支持分页加载，避免大群一次性加载所有成员
    - 返回成员信息：用户ID、用户名、头像、用户编码、角色、加入时间
  - **DELETE /api/messages/groups/{groupId}/members/{userId}** — 踢出群成员（仅群主或管理员）
    - 只有群主或管理员可以踢出成员
    - 不能踢出群主
    - 如果群聊关联了活动，同时移除该用户的活动参与记录
- ✅ 新增错误码 8011-8012（群成员管理相关）

### 2026-03-11
- ✅ 实现消息模块完整功能
  - **通知系统**（5个接口）
    - GET /api/notifications - 查询所有通知（分页）
    - GET /api/notifications/unread - 查询未读通知（分页）
    - GET /api/notifications/unread/count - 获取未读数量（角标）
    - PUT /api/notifications/{id}/read - 单条标记已读
    - PUT /api/notifications/read-all - 全部标记已读
  - **私信**（2个接口）
    - GET /api/messages/conversations - 私信会话列表
    - GET /api/messages/conversations/{id} - 私信消息列表（含自动清零未读）
  - **群聊**（7个接口）
    - POST /api/messages/groups - 创建群聊
    - GET /api/messages/groups - 我的群聊列表
    - GET /api/messages/groups/{id} - 群聊详情+成员列表
    - GET /api/messages/groups/{id}/messages - 群聊消息列表（含自动清零未读）
    - POST /api/messages/groups/{id}/invite - 邀请成员
    - DELETE /api/messages/groups/{id}/leave - 退出群聊
    - DELETE /api/messages/groups/{id} - 解散群聊（群主）
  - **消息收发**（2个接口）
    - POST /api/messages/send - 发送消息（私聊/群聊统一入口）
    - DELETE /api/messages/{id}/recall - 撤回消息
- ✅ 通知自动触发点
    - 点赞动态 → LIKE_FEED 通知
    - 点赞评论 → LIKE_COMMENT 通知
    - 发表评论 → COMMENT_FEED 通知（携带评论摘要前50字）
    - 关注用户 → FOLLOW 通知
    - 报名活动 → EVENT_JOIN 通知
    - 退出活动 → EVENT_QUIT 通知
- ✅ 新增错误码 8001-8010（消息/群聊相关）

### 2026-03-10
- ✅ 实现用户账号安全接口（2个接口）
  - PUT /api/users/password - 修改密码
  - PUT /api/users/phone - 修改手机号
- ✅ 新增错误码
  - 1005 - 旧密码错误
  - 1006 - 新密码不能与旧密码相同
  - 1007 - 新手机号不能与当前手机号相同
- ✅ 安全特性
  - 修改密码需要验证旧密码
  - 修改手机号需要验证当前密码
  - 新手机号不能已被其他用户使用
  - 新密码不能与旧密码相同

### 2026-03-09
- ✅ 实现动态管理完整功能（6个接口）
  - POST /api/feeds - 发布动态
  - GET /api/feeds - 获取动态列表（关注人的动态）
  - GET /api/feeds/{feedId} - 获取动态详情
  - DELETE /api/feeds/{feedId} - 删除动态
  - GET /api/users/{userId}/feeds - 获取用户动态列表
  - GET /api/movies/{movieId}/feeds - 获取电影相关动态
- ✅ 实现点赞功能（6个接口）
  - POST /api/feeds/{feedId}/like - 点赞动态
  - DELETE /api/feeds/{feedId}/like - 取消点赞动态
  - GET /api/feeds/{feedId}/like/status - 检查是否点赞动态
  - POST /api/comments/{commentId}/like - 点赞评论
  - DELETE /api/comments/{commentId}/like - 取消点赞评论
  - GET /api/comments/{commentId}/like/status - 检查是否点赞评论
- ✅ 实现评论功能（3个接口）
  - POST /api/feeds/{feedId}/comments - 发表评论
  - GET /api/feeds/{feedId}/comments - 获取评论列表
  - DELETE /api/comments/{commentId} - 删除评论
- ✅ 数据库改造
  - 动态表添加images字段（支持最多4张图片）
  - 重建点赞表（支持动态和评论点赞）
- ✅ 核心功能特性
  - 首页动态流显示关注人的动态
  - 支持关联电影和活动
  - 批量查询点赞状态优化性能
  - 级联删除保证数据一致性

### 2026-03-08
- ✅ 实现电影搜索高级筛选接口
  - GET /api/tmdb/discover/movie - 发现电影（支持按类型、年份、评分等多条件筛选）
  - GET /api/tmdb/genre/movie/list - 获取电影类型列表
  - 支持按人气、评分、上映日期等多种方式排序
  - 支持多类型组合筛选
  - 支持评分区间筛选

### 2026-03-06
- ✅ 修复看过记录检查接口的TMDB ID问题
  - 新增 GET /api/watched/check/tmdb/{tmdbId} - 根据TMDB ID检查看过状态
  - 保留 GET /api/watched/check/{movieId} - 根据本地ID检查看过状态
  - 解决前端使用TMDB ID检查时返回错误结果的问题
- ✅ 实现电影管理接口
  - GET /api/movies/{movieId} - 获取本地电影详情（根据本地ID）
  - GET /api/movies/tmdb/{tmdbId} - 获取本地电影详情（根据TMDB ID）
  - GET /api/movies - 获取本地电影列表（分页）
  - GET /api/movies/search - 搜索本地电影
- ✅ 活动参与表添加联系信息字段
  - participant_phone - 参与人手机号（必填）
  - participant_nickname - 参与人昵称（必填）
  - participant_wechat - 参与人微信号（选填）
  - participant_qq - 参与人QQ号（选填）
  - 修改参加活动接口，需要提供联系信息

### 2026-03-05
- ✅ 实现电影保存接口
  - POST /api/movies/save - 保存电影到数据库
  - 自动检查tmdbId是否重复，避免重复写入
  - 从TMDB获取电影详细信息并保存到本地
  - 返回完整的电影信息（包括本地ID）
- ✅ 活动表添加新字段
  - creator_id - 创建人ID（外键关联users表）
  - registration_deadline - 报名截止时间
  - end_time - 活动结束时间
  - movie_tmdb_id - 电影TMDB ID
  - registration_notice - 报名须知
- ✅ 实现活动创建人权限控制
  - 只有创建人可以修改和删除活动
  - 响应中包含isCreator字段，标识当前用户是否是创建人
- ✅ 实现报名截止时间控制
  - 过期后不能再参加活动

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

