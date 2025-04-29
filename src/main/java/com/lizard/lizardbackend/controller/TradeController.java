package com.lizard.lizardbackend.controller;

import com.lizard.lizardbackend.constant.UserConstant;
import com.lizard.lizardbackend.pojo.dto.TradeCreateDTO;
import com.lizard.lizardbackend.result.PageResult;
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

        log.info("删除交易记录：{}，{}", userId, tradeId);
        tradeService.deleteTrade(userId, tradeId);

        return Result.success();
    }
    /**
     * 取消交易
     * @param tradeId  交易id
     * @param request http请求
     * @return 交易记录id
     */
    @PutMapping("/cancel/{tradeId}")
    public Result<?> cancelTrade(@PathVariable Long tradeId, HttpServletRequest request){
        Long userId = (Long) request.getAttribute(UserConstant.USER_ID);

        log.info("取消交易： {}，{}",userId,tradeId);
        tradeService.cancelTrade(userId, tradeId);

        return Result.success();
    }

    /**
     * 收款方建立交易
     * @param tradeId  交易id
     * @param request http请求
     * @return 交易记录id
     */
    @PutMapping("/establish/{tradeId}")
    public Result<?> establishTrade(@PathVariable Long tradeId, HttpServletRequest request){
        Long userId = (Long) request.getAttribute(UserConstant.USER_ID);

        log.info("建立交易： {}，{}",userId,tradeId);
        tradeService.establishTrade(userId, tradeId);

        return Result.success();
    }

    /**
     * 收款方建立交易
     * @param tradeId  交易id
     * @param request http请求
     * @return 交易记录id
     */
    @PutMapping("/success/{tradeId}")
    public Result<?> successTrade(@PathVariable Long tradeId, HttpServletRequest request){
        Long userId = (Long) request.getAttribute(UserConstant.USER_ID);

        log.info("确定交易： {}，{}",userId,tradeId);
        tradeService.successTrade(userId, tradeId);

        return Result.success();
    }

    /**
     * @param pageNum 分页查询页号
     * @param pageSize 分页查询每页大小
     * @param request http请求
     * @return 分页查询结果
     */
    @GetMapping("/list")
    public Result<PageResult> listByUserId(Integer pageNum, Integer pageSize, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(UserConstant.USER_ID);

        log.info("根据用户id查询交易记录：{}, {}, {}", userId, pageNum, pageSize);
        PageResult pageResult =tradeService.tradePageQueryByUserId(userId, pageNum, pageSize);

        return  Result.success(pageResult);
    }
}