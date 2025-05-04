create schema if not exists lizard;

use lizard;

create table user
(
    id          bigint auto_increment comment '用户id'
        primary key,
    username    varchar(32)                        not null comment '用户名',
    password    varchar(64)                       not null comment '密码',
    nickname    varchar(32)                        null comment '昵称',
    avatar      varchar(127)                       null comment '头像URL',
    phone       varchar(16)                        null comment '手机号',
    role        tinyint  default 0                 not null comment '身份',
    status      tinyint  default 0                 not null comment '状态',
    is_deleted  tinyint  default 0                 not null comment '是否被删除',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint user_uk
        unique (username)
)
    comment '用户表';

create table post
(
    id            bigint auto_increment comment '帖子id'
        primary key,
    user_id       bigint                             not null comment '发布者id',
    username      varchar(32)                        not null comment '发布者用户名',
    title         varchar(64)                        not null comment '帖子标题',
    content       text                               not null comment '帖子正文',
    content_brief varchar(127)                       not null comment '正文预览',
    image_url     varchar(127)                       not null comment '帖子第一张图片URL',
    type          tinyint                            not null comment '交易类型',
    status        tinyint  default 0                 not null comment '商品状态',
    price         int                                null comment '预期价格',
    is_deleted    tinyint  default 0                 not null comment '是否被删除',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '帖子表';

create table image
(
    id          bigint auto_increment comment '图片id'
        primary key,
    post_id     bigint                             not null comment '帖子id',
    url         varchar(127)                       not null comment '图片URL',
    is_deleted  tinyint  default 0                 not null comment '是否被删除',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '帖子图片表';

create table trade
(
    id            bigint auto_increment comment '交易记录id'
        primary key,
    payer_id      bigint                             not null comment '付款用户id',
    payee_id      bigint                             not null comment '收款用户id',
    post_id       bigint                             not null comment '帖子id',
    title         varchar(64)                        not null comment '帖子标题',
    image_url     varchar(127)                       not null comment '帖子第一张图片URL',
    type          tinyint                            not null comment '交易类型',
    status        tinyint  default 0                 not null comment '交易状态',
    payer_deleted tinyint  default 0                 not null comment '付款方是否删除',
    payee_deleted tinyint  default 0                 not null comment '收款方是否删除',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '交易表';
