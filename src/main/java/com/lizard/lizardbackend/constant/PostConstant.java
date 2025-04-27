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
    Integer CONTENT_MAX_LENGTH = 1000;

    /**
     * 想买入
     */
    Integer STATUS_BUY = 0;

    /**
     * 想卖出
     */
    Integer STATUS_SELL = 1;

    /**
     * 想租入
     */
    Integer STATUS_RENT = 2;

    /**
     * 想借出
     */
    Integer STATUS_LEND = 3;
}
