package com.lizard.lizardbackend.service.impl;

import com.lizard.lizardbackend.constant.MessageConstant;
import com.lizard.lizardbackend.constant.TradeConstant;
import com.lizard.lizardbackend.exception.TradeCancelException;
import com.lizard.lizardbackend.exception.TradeCreateException;
import com.lizard.lizardbackend.exception.TradeDeleteException;
import com.lizard.lizardbackend.mapper.PostMapper;
import com.lizard.lizardbackend.mapper.TradeMapper;
import com.lizard.lizardbackend.mapper.UserMapper;
import com.lizard.lizardbackend.pojo.entity.Post;
import com.lizard.lizardbackend.pojo.entity.Trade;
import com.lizard.lizardbackend.pojo.entity.User;
import com.lizard.lizardbackend.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class TradeServiceImpl implements TradeService {
    private final TradeMapper tradeMapper;
    private final UserMapper userMapper;
    private final PostMapper postMapper;

    public TradeServiceImpl(TradeMapper tradeMapper, UserMapper userMapper, PostMapper postMapper) {
        this.tradeMapper = tradeMapper;
        this.userMapper = userMapper;
        this.postMapper = postMapper;
    }

    @Override
    public Long createTrade(Long userId, Long payerId, Long payeeId, Long postId) {
        // 检查各字段是否为空
        if (payerId == null || payeeId == null || postId == null) {
            throw new TradeCreateException(MessageConstant.ALL_FIELDS_REQUIRED);
        }

        // 创建交易的用户应为付款方，且付款方与收款方不能为同一人
        if (!Objects.equals(userId, payerId) || payerId.equals(payeeId)) {
            throw new TradeCreateException(MessageConstant.TRADE_ERROR);
        }

        // 检查用户是否存在
        User payer = userMapper.getById(payerId);
        User payee = userMapper.getById(payeeId);
        if (payer == null || payee == null || payer.getIsDeleted() == 1 || payee.getIsDeleted() == 1) {
            throw new TradeCreateException(MessageConstant.TRADING_USER_NOT_EXISTS);
        }

        // 检查帖子是否存在
        Post post = postMapper.getByPostId(postId);
        if (post == null || post.getIsDeleted() == 1) {
            throw new TradeCreateException(MessageConstant.POST_NOT_EXISTS);
        }

        // 检查帖子是否已经被交易
        if (post.getStatus() == 1) {
            throw new TradeCreateException(MessageConstant.ITEM_HAS_BEEN_TRADED);
        }

        // 帖子发布者和收款方应为同一人
        if (!Objects.equals(post.getUserId(), payeeId)) {
            throw new TradeCreateException(MessageConstant.TRADE_ERROR);
        }

        // 检查交易记录是否已经存在
        Trade trade = tradeMapper.getByThreeId(payerId, payeeId, postId);
        if (trade != null) {
            throw new TradeCreateException(MessageConstant.TRADE_EXISTS);
        }

        Trade newTrade = Trade.builder()
                .payerId(payerId)
                .payeeId(payeeId)
                .postId(postId)
                .build();

        return tradeMapper.insert(newTrade);
    }

    @Override
    public void deleteTrade(Long userId, Long tradeId) {
        // 检查交易记录是否存在
        Trade trade = tradeMapper.getById(tradeId);
        if (trade == null) {
            throw new TradeDeleteException(MessageConstant.TRADE_NOT_EXISTS);
        }

        // 交易进行中将无法删除交易记录
        Integer status = trade.getStatus();
        if (status == TradeConstant.STATUS_IN_PROGRESS || status == TradeConstant.STATUS_ESTABLISHED) {
            throw new TradeDeleteException(MessageConstant.TRADE_IN_PROGRESS);
        }

        boolean isPayer;
        Long payerId = trade.getPayerId();
        Long payeeId = trade.getPayeeId();
        if (Objects.equals(userId, payerId)) {
            isPayer = true;
        } else if (Objects.equals(userId, payeeId)) {
            isPayer = false;
        } else {
            throw new TradeDeleteException(MessageConstant.TRADE_OWNER_MISMATCH_ERROR);
        }

        // 检查交易记录是否已经被删除
        if ((isPayer && trade.getPayerDeleted() == 1)
            || (!isPayer && trade.getPayeeDeleted() == 1)) {
            throw new TradeDeleteException(MessageConstant.TRADE_NOT_EXISTS);
        }

        Trade tradeUpdate = Trade.builder()
                .id(tradeId)
                .payerDeleted(isPayer ? 1 : null)
                .payeeDeleted(isPayer ? null : 1)
                .build();

        tradeMapper.update(tradeUpdate);
    }

    @Override
    public void cancelTrade(Long userId, Long tradeId){
        // 检查交易记录是否存在
        Trade trade = tradeMapper.getById(tradeId);
        if (trade == null) {
            throw new TradeCancelException(MessageConstant.TRADE_NOT_EXISTS);
        }

        // 检查交易是否结束（完成或失败）
        Integer status = trade.getStatus();
        if (status == TradeConstant.STATUS_SUCCESS || status == TradeConstant.STATUS_FAILURE) {
            throw new TradeCancelException(MessageConstant.TRADE_OVERED);
        }

        // 检查用户是否为交易双方
        Long payerId = trade.getPayerId();
        Long payeeId = trade.getPayeeId();
        if(!Objects.equals(userId, payerId) && !Objects.equals(userId, payeeId)){
            throw new TradeCancelException(MessageConstant.TRADE_PAYER_MISMATCH_ERROR);
        }

        Trade tradeUpdate = Trade.builder()
                .id(tradeId)
                .status(TradeConstant.STATUS_FAILURE)
                .build();

        tradeMapper.update(tradeUpdate);
    }
}