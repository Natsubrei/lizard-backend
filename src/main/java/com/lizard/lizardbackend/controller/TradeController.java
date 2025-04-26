package com.lizard.lizardbackend.controller;

import com.lizard.lizardbackend.constant.UserConstant;
import com.lizard.lizardbackend.pojo.dto.TradeCreateDTO;
import com.lizard.lizardbackend.pojo.dto.TradeDeleteDTO;
import com.lizard.lizardbackend.pojo.vo.TradeVO;
import com.lizard.lizardbackend.result.Result;
import com.lizard.lizardbackend.service.TradeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
     * @return 交易记录VO
     */
    @PostMapping("/create")
    public Result<TradeVO> createTrade(@RequestBody TradeCreateDTO tradeCreateDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(UserConstant.USER_ID);
        tradeCreateDTO.setPayerId(userId); // 设置付款方为当前用户

        log.info("创建交易记录：{}", tradeCreateDTO);
        TradeVO tradeVO = tradeService.createTrade(tradeCreateDTO);
        return Result.success(tradeVO);
    }

    /**
     * 删除交易记录
     * @param tradeDeleteDTO 删除交易DTO
     * @param request http请求
     * @return 操作结果
     */
    @DeleteMapping("/delete")
    public Result<?> deleteTrade(@RequestBody TradeDeleteDTO tradeDeleteDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(UserConstant.USER_ID);
        tradeDeleteDTO.setUserId(userId);

        log.info("删除交易记录：{}", tradeDeleteDTO);
        tradeService.deleteTrade(tradeDeleteDTO);
        return Result.success();
    }
}