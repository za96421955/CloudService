package com.cloudservice.trade.controller;

import com.cloudservice.base.BaseController;
import com.cloudservice.base.Result;
import com.cloudservice.trade.analyse.context.TradeContext;
import com.cloudservice.trade.analyse.service.trade.OrderService;
import com.cloudservice.trade.analyse.service.trade.TradeService;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import com.cloudservice.trade.huobi.enums.SymbolEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 控制器：交易
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/9
 */
@RestController
@RequestMapping("/trade")
public class TradeController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private TradeService tradeService;

    @GetMapping("/analyse/{symbol}")
    @Description("获取实时分析数据")
    public Result analyse(@PathVariable String symbol) {
        try {
            return Result.buildSuccess(TradeContext.getAnalyse());
        } catch (Exception e) {
            logger.error("[交易] symbol={}, 获取实时分析数据异常, {}", symbol, e.getMessage(), e);
            return Result.buildFail(e.getMessage());
        }
    }

    @PostMapping("/zhang/{symbol}/{leverRate}")
    @Description("获取可用张数")
    public Result zhang(String access, String secret, @PathVariable String symbol, @PathVariable String leverRate) {
        try {
            int volume = orderService.getAvailableVolume(access, secret, SymbolEnum.get(symbol), ContractLeverRateEnum.get(leverRate));
            return Result.buildSuccess(volume);
        } catch (Exception e) {
            logger.error("[交易] symbol={}, leverRate={}, 获取可用张数异常, {}", symbol, leverRate, e.getMessage(), e);
            return Result.buildFail(e.getMessage());
        }
    }

    @PostMapping("/order/{symbol}")
    @Description("委托交易")
    public Track order(String access, String secret, @PathVariable String symbol
            , String hedgeType, String leverRate, long basisVolume, BigDecimal incomePricePlan
            , BigDecimal profitBasisMultiple, Long profitTrackIntervalTime, Integer timeout) {
        Track track = TradeContext.getTrack(access);
        track.setSecret(secret);
        track.setSymbol(SymbolEnum.get(symbol));
        track.getHedgeConfig().setHedgeType(hedgeType);
        track.getHedgeConfig().setLeverRate(ContractLeverRateEnum.get(leverRate));
        track.getHedgeConfig().setBasisVolume(basisVolume);
        track.getHedgeConfig().setIncomePricePlan(incomePricePlan);
        if (profitBasisMultiple != null) {
            track.getHedgeConfig().setProfitBasisMultiple(profitBasisMultiple);
        }
        if (profitTrackIntervalTime != null) {
            track.getHedgeConfig().setProfitTrackIntervalTime(profitTrackIntervalTime);
        }
        if (timeout != null) {
            track.getHedgeConfig().setTimeout(timeout);
        }
        return track;
    }

    @GetMapping("/changeTrade/{access}")
    @Description("交易切换")
    public String changeTrade(@PathVariable String access) {
        Track track = TradeContext.getTrack(access);
        track.getHedgeConfig().setStopTrade(!track.getHedgeConfig().isStopTrade());
        return "isStopTrade: " + track.getHedgeConfig().isStopTrade();
    }

}


