package com.lizard.lizardbackend.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lizard.lizardbackend.constant.MessageConstant;
import com.lizard.lizardbackend.constant.PostConstant;
import com.lizard.lizardbackend.constant.TradeConstant;
import com.lizard.lizardbackend.exception.*;
import com.lizard.lizardbackend.mapper.PostMapper;
import com.lizard.lizardbackend.mapper.TradeMapper;
import com.lizard.lizardbackend.mapper.UserMapper;
import com.lizard.lizardbackend.pojo.entity.Post;
import com.lizard.lizardbackend.pojo.entity.Trade;
import com.lizard.lizardbackend.pojo.entity.User;
import com.lizard.lizardbackend.pojo.vo.TradeQueryVO;
import com.lizard.lizardbackend.result.PageResult;
import com.lizard.lizardbackend.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
        if (Objects.equals(post.getStatus(), PostConstant.STATUS_HAS_TRADED)) {
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
                .title(post.getTitle())
                .imageUrl(post.getImageUrl())
                .type(post.getType())
                .build();

        tradeMapper.insert(newTrade);

        return newTrade.getId();
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

        // 检查交易记录是否已被双方中删除
        Integer payerDeleted = trade.getPayerDeleted();
        Integer payeeDeleted = trade.getPayeeDeleted();
        if (payeeDeleted == 1 || payerDeleted == 1) {
            throw new TradeCancelException(MessageConstant.TRADE_DELETED_OVER);
        }

        // 检查用户是否为交易双方
        Long payerId = trade.getPayerId();
        Long payeeId = trade.getPayeeId();
        if(!Objects.equals(userId, payerId) && !Objects.equals(userId, payeeId)){
            throw new TradeCancelException(MessageConstant.TRADE_PAY_MISMATCH_ERROR);
        }

        // 检查交易是否结束（完成或失败）
        Integer status = trade.getStatus();
        if (status == TradeConstant.STATUS_SUCCESS || status == TradeConstant.STATUS_FAILURE) {
            throw new TradeCancelException(MessageConstant.TRADE_OVERED);
        }

        Trade tradeUpdate = Trade.builder()
                .id(tradeId)
                .status(TradeConstant.STATUS_FAILURE)
                .build();

        tradeMapper.update(tradeUpdate);
    }

    @Override
    public void establishTrade(Long userId, Long tradeId) {
        // 检查交易记录是否存在
        Trade trade = tradeMapper.getById(tradeId);
        if (trade == null) {
            throw new TradeEstablishException(MessageConstant.TRADE_NOT_EXISTS);
        }

        // 检查交易记录是否已被双方中删除
        Integer payerDeleted = trade.getPayerDeleted();
        Integer payeeDeleted = trade.getPayeeDeleted();
        if (payeeDeleted == 1 || payerDeleted == 1) {
            throw new TradeEstablishException(MessageConstant.TRADE_DELETED_OVER);
        }

        // 检查用户是否为收款方
        Long payeeId = trade.getPayeeId();
        if(!Objects.equals(userId, payeeId)) {
            throw new TradeEstablishException(MessageConstant.TRADE_PAYEE_MISMATCH_ERROR);
        }

        // 检查交易是否已建立
        Integer status = trade.getStatus();
        if (status == TradeConstant.STATUS_ESTABLISHED) {
            throw new TradeEstablishException(MessageConstant.TRADE_ESTABLISH);
        }

        // 检查交易是否结束（完成或失败）
        if (status == TradeConstant.STATUS_SUCCESS || status == TradeConstant.STATUS_FAILURE) {
            throw new TradeEstablishException(MessageConstant.TRADE_OVER);
        }

        Trade tradeUpdate = Trade.builder()
                .id(tradeId)
                .status(TradeConstant.STATUS_ESTABLISHED)
                .build();

        tradeMapper.update(tradeUpdate);
    }

    @Override
    public void successTrade(Long userId, Long tradeId) {
        // 检查交易记录是否存在
        Trade trade = tradeMapper.getById(tradeId);
        if (trade == null) {
            throw new TradeSuccessException(MessageConstant.TRADE_NOT_EXISTS);
        }

        // 检查交易记录是否已被双方中删除
        Integer payerDeleted = trade.getPayerDeleted();
        Integer payeeDeleted = trade.getPayeeDeleted();
        if (payeeDeleted == 1 || payerDeleted == 1) {
            throw new TradeSuccessException(MessageConstant.TRADE_DELETED_OVER);
        }

        // 检查用户是否为付款方
        Long payerId = trade.getPayerId();
        if(!Objects.equals(userId, payerId)) {
            throw new TradeSuccessException(MessageConstant.TRADE_PAYER_MISMATCH_ERROR);
        }

        // 检查交易是否已建立
        Integer status = trade.getStatus();
        if (status == TradeConstant.STATUS_IN_PROGRESS) {
            throw new TradeSuccessException(MessageConstant.TRADE_NOY_ESTABLISH);
        }

        // 检查交易是否结束（完成或失败）
        if (status == TradeConstant.STATUS_SUCCESS || status == TradeConstant.STATUS_FAILURE) {
            throw new TradeSuccessException(MessageConstant.TRADE_OVER);
        }

        Trade tradeUpdate = Trade.builder()
                .id(tradeId)
                .status(TradeConstant.STATUS_SUCCESS)
                .build();

        tradeMapper.update(tradeUpdate);
        tradeMapper.traded(trade.getPayeeId(), trade.getPayerId(), trade.getPostId());

        postMapper.traded(trade.getPostId());
    }

    @Override
    public PageResult tradePageQueryByUserId(Long userId, Integer pageNum, Integer pageSize) {
        // 使用PageHelper进行分页查询
        PageHelper.startPage(pageNum, pageSize);
        Page<TradeQueryVO> page = tradeMapper.tradePageQueryByUserId(userId);

        // 查询失败则抛出异常
        if (page == null) {
            throw new TradeQueryException(MessageConstant.PAGE_QUERY_ERROR);
        }

        // 返回帖子总数以及此次查询的结果
        return new PageResult(page.getTotal(), page);
    }

    @Override
    public TradeQueryVO queryTrade(Long userId, Long payerId, Long payeeId, Long postId) {
        // 检查用户是否为交易双方
        if(!Objects.equals(userId, payerId) && !Objects.equals(userId, payeeId)){
            throw new TradeQueryException(MessageConstant.TRADE_PAY_MISMATCH_ERROR);
        }

        Trade trade = tradeMapper.getByThreeId(payerId, payeeId, postId);
        if (trade == null) {
            return null;
        }

        TradeQueryVO tradeQueryVO = new TradeQueryVO();
        BeanUtils.copyProperties(trade, tradeQueryVO);

        return tradeQueryVO;
    }
}