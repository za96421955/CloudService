package com.cloudservice.trade.controller;

import com.cloudservice.base.BaseController;
import com.cloudservice.base.Result;
import com.cloudservice.plat.context.PlatContext;
import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.trade.analyse.context.AnalyseContext;
import com.cloudservice.trade.analyse.service.trade.OrderService;
import com.cloudservice.trade.analyse.service.trade.TradeService;
import com.cloudservice.trade.hedge.context.HedgeContext;
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
            return Result.buildSuccess(AnalyseContext.getAnalyse());
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
            , String hedgeType, String strategyType, BigDecimal incomePricePlan
            , Long profitTrackIntervalTime, Integer timeout) {
        Track track = new Track(access, secret);
        track.setSymbol(SymbolEnum.get(symbol));
        track.setHedgeType(hedgeType);
        track.setHedgeConfig(PlatContext.getHedgeStrategy(StrategyTypeEnum.get(strategyType)));
        track.getHedgeConfig().setIncomePricePlan(incomePricePlan);
        if (profitTrackIntervalTime != null) {
            track.getHedgeConfig().setProfitTrackIntervalTime(profitTrackIntervalTime);
        }
        if (timeout != null) {
            track.getHedgeConfig().setTimeout(timeout);
        }
        PlatContext.setTrack(track);
        return track;
    }

    @GetMapping("/changeTrade/{symbol}/{hedgeType}/{access}")
    @Description("交易切换")
    public String changeTrade(@PathVariable String access, @PathVariable String symbol
            , @PathVariable String hedgeType) {
        Track track = PlatContext.getTrack(access, SymbolEnum.get(symbol), hedgeType);
        if (track == null) {
            return null;
        }
        track.setStopTrade(!track.isStopTrade());
        return "isStopTrade: " + track.isStopTrade();
    }

}


