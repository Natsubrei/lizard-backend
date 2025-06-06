package com.lizard.lizardbackend.mapper;

import com.github.pagehelper.Page;
import com.lizard.lizardbackend.pojo.entity.Trade;
import com.lizard.lizardbackend.pojo.vo.TradeQueryVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TradeMapper {
    /**
     * 新增一条交易记录
     * @param trade 交易记录对象
     */
    void insert(Trade trade);

    /**
     * 更新交易记录
     * @param trade 包含更新内容的交易记录对象
     */
    void update(Trade trade);

    /**
     * 将当前交易的相关的其他交易的状态设置为交易失败（status = 3）
     * @param payeeId 收款方id
     * @param payerId 付款方id
     * @param postId 交易成功的帖子id
     */
    @Update("UPDATE trade SET status = 3 " +
            "WHERE payee_id = #{payeeId} AND post_id = #{postId} AND payer_id != #{payerId}")
    void traded(Long payeeId, Long payerId, Long postId);

    /**
     * 根据交易id查询对应的交易记录
     * @param id 交易记录id
     * @return 交易记录对象
     */
    @Select("SELECT * FROM trade WHERE id = #{id}")
    Trade getById(Long id);

    /**
     * 根据双方id和帖子id查询交易记录
     * @param payerId 付款方id
     * @param payeeId 收款方id
     * @param postId 帖子id
     * @return 交易对象
     */
    @Select("SELECT * FROM trade " +
            "WHERE payer_id = #{payerId} AND payee_id = #{payeeId} AND post_id = #{postId}")
    Trade getByThreeId(Long payerId, Long payeeId, Long postId);

    /**
     * 分页查询与指定用户相关的所有交易记录（排除被该用户逻辑删除的记录）
     * @param userId 用户id
     * @return 分页查询结果
     */
    @Select("SELECT * FROM trade " +
            "WHERE (payer_id = #{userId} AND payer_deleted = 0 ) OR (payee_id = #{userId} AND payee_deleted = 0) " +
            "ORDER BY create_time DESC")
    Page<TradeQueryVO> pageQueryByUserId(Long userId);
}