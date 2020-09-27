package com.cloudservice.trade.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.trade.analyse.model.statistic.KlineRange;
import com.cloudservice.trade.analyse.model.statistic.PullTrack;
import com.cloudservice.trade.analyse.service.trade.StatisticService;
import com.cloudservice.trade.analyse.thread.ProphecyMachine;
import com.cloudservice.trade.huobi.enums.KlineFluctuationEnum;
import com.cloudservice.trade.huobi.enums.PeriodEnum;
import com.cloudservice.trade.huobi.enums.SymbolUSDTEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制器：统计
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/9
 */
@RestController
@RequestMapping("/statistic")
public class StatisticController extends BaseController {

    @Autowired
    private StatisticService statisticService;
    @Autowired
    private ProphecyMachine prophecyMachine;

    @PostMapping("/{symbol}/{tradeType}")
    @Description("盈亏统计")
    public JSONObject orderHistory(String access, String secret, @PathVariable String symbol, @PathVariable String tradeType
            , String beginTime, String endTime) {
        try {
            return statisticService.transactionOrder(access, secret, symbol, tradeType, beginTime, endTime);
        } catch (Exception e) {
            logger.error("[统计] symbol={}, tradeType={}, 盈亏统计异常, {}"
                    , symbol, tradeType, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/prophecy/{symbol}")
    @Description("获取预言信息")
    public Map<String, Object> getKlineRange(@PathVariable String symbol) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("klineRangeMap", prophecyMachine.getKlineRangeMap());
            result.put("prophecyRangeMap", prophecyMachine.getProphecyRangeMap());
            result.put("prophecyMap", prophecyMachine.getProphecyMap());
            return result;
        } catch (Exception e) {
            logger.error("[统计] symbol={}, 获取预言信息异常, {}", symbol, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/klineRange/{symbol}/{period}/{size}")
    @Description("获取K线区间信息")
    public Map<Integer, KlineRange> getKlineRange(@PathVariable String symbol, @PathVariable String period, @PathVariable int size) {
        try {
            return statisticService.getKlineRange(SymbolUSDTEnum.get(symbol), PeriodEnum.get(period), size);
        } catch (Exception e) {
            logger.error("[统计] symbol={}, period={}, size={}, 获取K线区间信息异常, {}"
                    , symbol, period, size, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/fluctuation/{symbol}")
    @Description("获取近期K线波动趋势")
    public KlineFluctuationEnum getFluctuation(@PathVariable String symbol) {
        try {
            return statisticService.getFluctuation(SymbolUSDTEnum.get(symbol));
        } catch (Exception e) {
            logger.error("[统计] symbol={}, 获取近期K线波动趋势异常, {}", symbol, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/pullTrack/{symbol}/{period}/{size}")
    @Description("获取拉盘追踪信息")
    public List<PullTrack> getPullTrack(@PathVariable String symbol, @PathVariable String period, @PathVariable int size) {
        try {
            return statisticService.getPullTrack(SymbolUSDTEnum.get(symbol), PeriodEnum.get(period), size);
        } catch (Exception e) {
            logger.error("[统计] symbol={}, period={}, size={}, 获取拉盘追踪信息异常, {}"
                    , symbol, period, size, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

}


