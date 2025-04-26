package com.lizard.lizardbackend.service.impl;

import com.lizard.lizardbackend.constant.TradeConstant;
import com.lizard.lizardbackend.mapper.TradeMapper;
import com.lizard.lizardbackend.pojo.dto.TradeCreateDTO;
import com.lizard.lizardbackend.pojo.dto.TradeDeleteDTO;
import com.lizard.lizardbackend.pojo.entity.Trade;
import com.lizard.lizardbackend.pojo.vo.TradeVO;
import com.lizard.lizardbackend.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class TradeServiceImpl implements TradeService {
    private final TradeMapper tradeMapper;

    public TradeServiceImpl(TradeMapper tradeMapper) {
        this.tradeMapper = tradeMapper;
    }

    @Override
    public TradeVO createTrade(TradeCreateDTO tradeCreateDTO) {
        // 创建交易实体
        Trade trade = new Trade();
        trade.setTradeId(System.currentTimeMillis()); // 简单使用时间戳作为ID
        trade.setPayerId(tradeCreateDTO.getPayerId());
        trade.setPayeeId(tradeCreateDTO.getPayeeId());
        trade.setPostId(tradeCreateDTO.getPostId());
        trade.setStatus(TradeConstant.STATUS_IN_PROGRESS);
        trade.setCreateTime(LocalDateTime.now());

        // 保存交易记录
        tradeMapper.insert(trade);
        log.info("创建交易记录成功，交易ID：{}", trade.getTradeId());

        // 转换为VO返回
        TradeVO tradeVO = new TradeVO();
        BeanUtils.copyProperties(trade, tradeVO);
        return tradeVO;
    }

    @Override
    public void deleteTrade(TradeDeleteDTO tradeDeleteDTO) {
        Long tradeId = tradeDeleteDTO.getTradeId();
        Long userId = tradeDeleteDTO.getUserId();
        Integer deleteType = tradeDeleteDTO.getDeleteType();

        // 根据删除类型执行不同的删除操作
        int affectedRows;
        if (deleteType == TradeConstant.DELETE_TYPE_PHYSICAL) {
            // 物理删除
            affectedRows = tradeMapper.physicalDelete(tradeId, userId);
        } else {
            // 逻辑删除
            Trade trade = tradeMapper.getById(tradeId);
            boolean isPayer = trade != null && trade.getPayerId().equals(userId);
            affectedRows = tradeMapper.updateDeleteStatus(tradeId, userId, isPayer);
        }

        if (affectedRows == 0) {
            log.warn("删除交易记录失败，tradeId: {}, userId: {}", tradeId, userId);
        } else {
            log.info("成功删除交易记录，tradeId: {}, userId: {}, deleteType: {}", tradeId, userId, deleteType == TradeConstant.DELETE_TYPE_PHYSICAL ? "物理删除" : "逻辑删除");
        }
    }
}