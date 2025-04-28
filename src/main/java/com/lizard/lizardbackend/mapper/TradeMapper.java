package com.lizard.lizardbackend.mapper;

import com.lizard.lizardbackend.pojo.entity.Trade;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TradeMapper {
    void insert(Trade trade);

    @Select("SELECT * FROM trade WHERE id = #{id}")
    Trade getById(Long id);

    @Select("SELECT * FROM trade " +
            "WHERE payer_id = #{payerId} AND payee_id = #{payeeId} AND post_id = #{postId} AND payer_deleted = 0")
    Trade getByThreeId(Long payerId, Long payeeId, Long postId);


    void update(Trade trade);
}