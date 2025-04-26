package com.lizard.lizardbackend.mapper;

import com.lizard.lizardbackend.pojo.entity.Trade;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TradeMapper {
    void insert(Trade trade);

    @Select("SELECT * FROM trade WHERE trade_id = #{tradeId}")
    Trade getById(Long tradeId);

    @Update("UPDATE trade SET is_deleted = 1 WHERE trade_id = #{tradeId} AND (payer_id = #{userId} OR payee_id = #{userId})")
    int logicalDelete(@Param("tradeId") Long tradeId, @Param("userId") Long userId);

    @Delete("DELETE FROM trade WHERE trade_id = #{tradeId} AND (payer_id = #{userId} OR payee_id = #{userId})")
    int physicalDelete(@Param("tradeId") Long tradeId, @Param("userId") Long userId);

    // 移除复杂的动态 SQL 注解，改用 XML 配置
    int updateDeleteStatus(@Param("tradeId") Long tradeId, @Param("userId") Long userId, @Param("isPayer") boolean isPayer);
}