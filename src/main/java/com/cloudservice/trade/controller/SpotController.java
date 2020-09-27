package com.cloudservice.trade.controller;

import com.cloudservice.trade.huobi.enums.*;
import com.cloudservice.trade.huobi.model.spot.Depth;
import com.cloudservice.trade.huobi.model.spot.Kline;
import com.cloudservice.trade.huobi.model.spot.Symbol;
import com.cloudservice.trade.huobi.model.spot.Ticker;
import com.cloudservice.trade.huobi.service.spot.SpotBasicService;
import com.cloudservice.trade.huobi.service.spot.SpotMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 控制器：现货
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/9
 */
@RestController
@RequestMapping("/spot")
public class SpotController extends BaseController {

    @Autowired
    private SpotBasicService spotBasicService;
    @Autowired
    private SpotMarketService spotMarketService;

    @GetMapping("/symbols")
    @Description("获取所有交易对")
    public List<Symbol> symbols() {
        try {
            return spotBasicService.getSymbols();
        } catch (Exception e) {
            logger.error("[现货] 获取所有交易对异常, {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/kline/{symbol}/{period}/{size}")
    @Description("获取K线数据")
    public List<Kline> kline(@PathVariable String symbol, @PathVariable String period, @PathVariable int size) {
        try {
            return spotMarketService.getKline(SymbolUSDTEnum.get(symbol), PeriodEnum.get(period), size);
        } catch (Exception e) {
            logger.error("[现货] symbol={}, period={}, size={}, 获取K线数据异常, {}"
                    , symbol, period, size, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/merged/{symbol}")
    @Description("获取最近24小时聚合行情")
    public Ticker merged(@PathVariable String symbol) {
        try {
            return spotMarketService.getMerged(SymbolEnum.get(symbol));
        } catch (Exception e) {
            logger.error("[现货] symbol={}, 获取最近24小时聚合行情异常, {}", symbol, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/depth/{symbol}/{depth}/{depthType}")
    @Description("获取当前市场深度数据")
    public Depth depth(@PathVariable String symbol, @PathVariable String depth, @PathVariable String depthType) {
        try {
            return spotMarketService.getDepth(SymbolUSDTEnum.get(symbol), DepthEnum.get(depth), DepthTypeEnum.get(depthType));
        } catch (Exception e) {
            logger.error("[现货] symbol={}, depth={}, depthType={}, 获取当前市场深度数据异常, {}"
                    , symbol, depth, depthType, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

}


