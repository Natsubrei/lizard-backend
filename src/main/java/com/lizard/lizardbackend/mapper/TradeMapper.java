package com.lizard.lizardbackend.mapper;

import com.github.pagehelper.Page;
import com.lizard.lizardbackend.pojo.entity.Trade;
import com.lizard.lizardbackend.pojo.vo.TradeQueryVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TradeMapper {
    void insert(Trade trade);

    @Select("SELECT * FROM trade WHERE id = #{id}")
    Trade getById(Long id);

    @Select("SELECT * FROM trade " +
            "WHERE payer_id = #{payerId} AND payee_id = #{payeeId} AND post_id = #{postId} AND payer_deleted = 0")
    Trade getByThreeId(Long payerId, Long payeeId, Long postId);

    @Select("SELECT * FROM trade WHERE (payer_id = #{userId} AND payer_deleted = 0 ) OR (payee_id = #{userId} AND payee_deleted = 0 ) ORDER BY create_time DESC")
    Page<TradeQueryVO> tradePageQueryByUserId(Long userId);

    void update(Trade trade);
}