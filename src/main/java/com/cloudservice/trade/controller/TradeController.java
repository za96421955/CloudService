package com.cloudservice.trade.controller;

import com.cloudservice.base.BaseController;
import com.cloudservice.base.Result;
import com.cloudservice.plat.context.PlatContext;
import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.trade.analyse.context.AnalyseContext;
import com.cloudservice.trade.analyse.service.trade.OrderService;
import com.cloudservice.trade.analyse.service.trade.TradeService;
import com.cloudservice.trade.hedge.context.HedgeContext;
import com.cloudservice.trade.hedge.model.DIYConfig;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import com.cloudservice.trade.huobi.enums.ContractTypeEnum;
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
            , String hedgeType, String contractType, String strategyType, Integer riskType
            , DIYConfig diyConfig) {
        Track track = PlatContext.getTrack(access, SymbolEnum.get(symbol), hedgeType);
        if (track == null) {
            track = new Track(access, secret);
        }
        track.setSymbol(SymbolEnum.get(symbol));
        track.setContractType(ContractTypeEnum.get(contractType));
        track.setHedgeType(hedgeType);
        track.setStrategyType(StrategyTypeEnum.get(strategyType));
        track.setRiskType(riskType);
        // 自定义配置
        track.setDiyConfig(diyConfig);
        // 记录上下文
        PlatContext.setTrack(track);
        return PlatContext.getTrack(track.getAccess(), track.getSymbol(), track.getHedgeType());
    }

    @GetMapping("/changeTrade/{symbol}/{hedgeType}/{access}/{isStop:-1|0|1}")
    @Description("交易切换")
    public String changeTrade(@PathVariable String access, @PathVariable String symbol
            , @PathVariable String hedgeType, @PathVariable int isStop) {
        Track track = PlatContext.getTrack(access, SymbolEnum.get(symbol), hedgeType);
        if (track == null) {
            return null;
        }
        if (isStop == -1) {
            track.getDiyConfig().setStopTrade(null);
        } else {
            track.getDiyConfig().setStopTrade(isStop == 1);
        }
        return "isStopTrade: " + track.getDiyConfig().getStopTrade();
    }

}


