package com.lizard.lizardbackend.controller;

import com.lizard.lizardbackend.constant.UserConstant;
import com.lizard.lizardbackend.pojo.dto.TradeCreateDTO;
import com.lizard.lizardbackend.result.Result;
import com.lizard.lizardbackend.service.TradeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 交易相关接口
 */
@Slf4j
@RestController
@RequestMapping("/trade")
public class TradeController {
    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    /**
     * 创建交易记录
     * @param tradeCreateDTO 创建交易DTO
     * @param request http请求
     * @return 交易记录id
     */
    @PostMapping("/create")
    public Result<Long> createTrade(@RequestBody TradeCreateDTO tradeCreateDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(UserConstant.USER_ID);
        Long payerId = tradeCreateDTO.getPayerId();
        Long payeeId = tradeCreateDTO.getPayeeId();
        Long postId = tradeCreateDTO.getPostId();

        log.info("创建交易记录：{}", tradeCreateDTO);
        Long tradeId = tradeService.createTrade(userId, payerId, payeeId, postId);

        return Result.success(tradeId);
    }

    /**
     * 删除交易记录
     * @param tradeId 交易id
     * @param request http请求
     * @return 删除成功则返回成功Result
     */
    @DeleteMapping("/{tradeId}")
    public Result<?> deleteTrade(@PathVariable Long tradeId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(UserConstant.USER_ID);

        log.info("删除交易记录：userId：{}，tradeId：{}", userId, tradeId);
        tradeService.deleteTrade(userId, tradeId);

        return Result.success();
    }
}