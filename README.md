# 西易二手交易平台后端

### 用户API

| 接口名称             | 请求类型 | 接口路径         |
| -------------------- | -------- | ---------------- |
| 用户注册             | `POST`   | /user/register   |
| 用户登录             | `POST`   | /user/login      |
| 查询当前登录用户信息 | `GET`    | /user/current    |
| 根据用户id查询       | `GET`    | /user/{userid}   |
| 修改用户信息         | `PUT`    | /user/info       |
| 修改用户密码         | `PUT`    | /user/password   |
| 注销账号             | `DELETE` | /user/deactivate |

### 帖子API

| 接口名称               | 请求类型 | 接口路径       |
| ---------------------- | -------- | -------------- |
| 创建帖子               | `POST`   | /post/create   |
| 向帖子中添加图片       | `POST`   | /post/image    |
| 根据帖子id删除帖子     | `DELETE` | /post/{postId} |
| 根据帖子id查询帖子详情 | `GET`    | /post/{postId} |
| 根据用户id查询帖子     | `GET`    | /post/list     |
| 根据帖子标签查询       | `GET`    | /post/type     |
| 根据时间顺序查询       | `GET`    | /post/time     |
| 根据关键词查询         | `GET`    | /post/search   |

### 交易API

| 接口名称                 | 请求类型 | 接口路径                   |
| ------------------------ | -------- | -------------------------- |
| 创建交易记录             | `POST`   | /trade/create              |
| 删除交易记录             | `DELETE` | /trade/{tradeId}           |
| 取消交易                 | `PUT`    | /trade/cancel/{tradeId}    |
| 收款方建立交易           | `PUT`    | /trade/establish/{tradeId} |
| 付款方确认交易           | `PUT`    | /trade/success/{tradeId}   |
| 根据用户id查询           | `GET`    | /trade/list                |
| 根据双方id以及帖子id查询 | `GET`    | /trade                     |
