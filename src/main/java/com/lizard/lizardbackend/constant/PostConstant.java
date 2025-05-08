package com.lizard.lizardbackend.constant;

/**
 * 帖子相关常量类
 */
public interface PostConstant {
    /**
     * 帖子图片存放目录
     */
    String IMAGE_DIRECTORY = "image";

    /**
     * 标题最大长度
     */
    Integer TITLE_MAX_LENGTH = 32;

    /**
     * 正文最大长度
     */
    Integer CONTENT_MAX_LENGTH = 10000;

    /**
     * 想买入
     */
    Integer TYPE_BUY = 0;

    /**
     * 想卖出
     */
    Integer TYPE_SELL = 1;

    /**
     * 想租入
     */
    Integer TYPE_RENT = 2;

    /**
     * 想借出
     */
    Integer TYPE_LEND = 3;

    /**
     * 未交易
     */
    Integer STATUS_NOT_TRADED = 0;

    /**
     * 已交易
     */
    Integer STATUS_HAS_TRADED = 1;
}
