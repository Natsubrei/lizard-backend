package com.lizard.lizardbackend.service;

import com.lizard.lizardbackend.pojo.dto.TradeCreateDTO;
import com.lizard.lizardbackend.pojo.dto.TradeDeleteDTO;
import com.lizard.lizardbackend.pojo.vo.TradeVO;

public interface TradeService {
    /**
     * 创建交易记录
     * @param tradeCreateDTO 创建交易DTO
     * @return 交易记录VO
     */
    TradeVO createTrade(TradeCreateDTO tradeCreateDTO);

    /**
     * 删除交易记录
     * @param tradeDeleteDTO 删除交易DTO
     */
    void deleteTrade(TradeDeleteDTO tradeDeleteDTO);
}