package com.lizard.lizardbackend.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除交易记录DTO
 */
@Data
public class TradeDeleteDTO implements Serializable {
    /**
     * 交易记录id
     */
    private Long tradeId;

    /**
     * 用户id（用于验证权限）
     */
    private Long userId;

    /**
     * 删除类型（0-逻辑删除，1-物理删除）
     */
    private Integer deleteType;
}