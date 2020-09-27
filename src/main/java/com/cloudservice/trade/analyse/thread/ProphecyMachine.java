package com.cloudservice.trade.analyse.thread;

import com.cloudservice.base.BaseService;
import com.cloudservice.trade.analyse.model.statistic.KlineRange;
import com.cloudservice.trade.analyse.model.trade.Prophecy;
import com.cloudservice.trade.analyse.service.trade.StatisticService;
import com.cloudservice.trade.huobi.enums.PeriodEnum;
import com.cloudservice.trade.huobi.enums.SymbolUSDTEnum;
import com.cloudservice.trade.huobi.service.spot.SpotMarketService;
import com.cloudservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预言机加载
 * <p>
 *     cron: * * * * * * *
 *     cron: 秒 分 时 日 月 周 年
 * </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/16
 */
@Component
public class ProphecyMachine extends BaseService {

    @Autowired
    private StatisticService statisticService;

//    @Scheduled(cron = "30 0/1 * * * ?")
    public void runMine() {
        logger.info("[预言机加载] ===============================");
        try {
            // 差价区间数据加载
            this.loadKlineRange(SymbolUSDTEnum.ETH_USDT);
            // 预言分析
            this.prophecyAnalyse();
        } catch (Exception e) {
            logger.error("[预言机加载] 异常, {}", e.getMessage(), e);
        }
    }

    /**
     * @description 获取预言信息
     * <p>
     *     1，高优先级
     *     2，大时间片
     * </p>
     *
     * @author 陈晨
     * @date 2020/9/18 17:02
     * @param range     差价区间
     **/
    public Prophecy getProphecy(int range) {
        return prophecyMap.get(range);
    }

    public Map<PeriodEnum, Map<Integer, KlineRange>> getKlineRangeMap() {
        return klineRangeMap;
    }

    public Map<Integer, Map<PeriodEnum, Prophecy>> getProphecyRangeMap() {
        return prophecyRangeMap;
    }

    public Map<Integer, Prophecy> getProphecyMap() {
        return prophecyMap;
    }

    /**
     * @description 差价区间数据加载
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/18 16:00
     **/
    private Map<PeriodEnum, Map<Integer, KlineRange>> klineRangeMap = new HashMap<>();
    private void loadKlineRange(SymbolUSDTEnum symbol) {
        Map<PeriodEnum, Map<Integer, KlineRange>> klineRangeMap = new HashMap<>();
        klineRangeMap.put(PeriodEnum.MIN_1, statisticService.getKlineRange(symbol));
        klineRangeMap.put(PeriodEnum.MIN_5, statisticService.getKlineRange(symbol, PeriodEnum.MIN_5, 2000));
        klineRangeMap.put(PeriodEnum.MIN_15, statisticService.getKlineRange(symbol, PeriodEnum.MIN_15, 2000));
        klineRangeMap.put(PeriodEnum.MIN_30, statisticService.getKlineRange(symbol, PeriodEnum.MIN_30, 2000));
        klineRangeMap.put(PeriodEnum.MIN_60, statisticService.getKlineRange(symbol, PeriodEnum.MIN_60, 2000));
        this.klineRangeMap = klineRangeMap;
    }

    /**
     * @description 预言分析
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/18 17:00
     **/
    private Map<Integer, Map<PeriodEnum, Prophecy>> prophecyRangeMap = new HashMap<>();
    private Map<Integer, Prophecy> prophecyMap = new HashMap<>();
    private void prophecyAnalyse() {
        // 设置预言信息
        Map<Integer, Map<PeriodEnum, Prophecy>> prophecyRangeMap = new HashMap<>();
        this.setProphecy(PeriodEnum.MIN_1, prophecyRangeMap);
        this.setProphecy(PeriodEnum.MIN_5, prophecyRangeMap);
        this.setProphecy(PeriodEnum.MIN_15, prophecyRangeMap);
        this.setProphecy(PeriodEnum.MIN_30, prophecyRangeMap);
        this.setProphecy(PeriodEnum.MIN_60, prophecyRangeMap);
        this.prophecyRangeMap = prophecyRangeMap;
        // 设置差价区间预言信息
        Map<Integer, Prophecy> prophecyMap = new HashMap<>();
        for (int i = 1; i < SpotMarketService.RANGE_COUNT; i++) {
            prophecyMap.put(i, this.getProphecyCurr(i));
        }
        this.prophecyMap = prophecyMap;
    }

    /**
     * @description 设置预言信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/18 16:31
     **/
    private void setProphecy(PeriodEnum period, Map<Integer, Map<PeriodEnum, Prophecy>> prophecyRangeMap) {
        for (Map.Entry<Integer, KlineRange> entry : klineRangeMap.get(period).entrySet()) {
            Map<PeriodEnum, Prophecy> prophecyMap = prophecyRangeMap.get(entry.getKey());
            if (prophecyMap == null) {
                prophecyRangeMap.put(entry.getKey(), new HashMap<>());
                prophecyMap = prophecyRangeMap.get(entry.getKey());
            }
            Prophecy prophecy = new Prophecy().setPeriod(period)
                            .setKlineRange(entry.getValue())
                            .setPriority(this.getPriority(entry.getValue()));
            if (prophecy.getPriority() <= 0) {
                continue;
            }
            prophecyMap.put(period, prophecy);
        }
    }

    /**
     * @description 获取优先级
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/18 16:25
     **/
    private int getPriority(KlineRange klineRange) {
        BigDecimal rateDiff = klineRange.getBuyRate100().subtract(klineRange.getSellRate100()).abs();
        if (rateDiff.compareTo(BigDecimal.valueOf(10)) < 0) {
            return 0;
        }
        if (rateDiff.compareTo(BigDecimal.valueOf(20)) < 0) {
            return 1;
        }
        if (rateDiff.compareTo(BigDecimal.valueOf(30)) < 0) {
            return 2;
        }
        if (rateDiff.compareTo(BigDecimal.valueOf(40)) < 0) {
            return 3;
        }
        return 4;
    }

    /**
     * @description 获取预言信息
     * <p>
     *     1，高优先级
     *     2，大时间片
     * </p>
     *
     * @author 陈晨
     * @date 2020/9/18 17:02
     * @param range     差价区间
     **/
    public Prophecy getProphecyCurr(int range) {
        Prophecy result = null;
        for (PeriodEnum period : this.getTimeSlice()) {
            Prophecy curr = prophecyRangeMap.get(range).get(period);
            if (curr == null) {
                continue;
            }
            if (result == null) {
                result = curr;
                continue;
            }
            if (result.getPriority() > curr.getPriority()) {
                continue;
            }
            if (result.getPriority() == curr.getPriority()
                    && result.getPeriod().second() > curr.getPeriod().second()) {
                continue;
            }
            result = curr;
        }
        return result;
    }

    /**
     * @description 获取时间片
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/18 17:17
     **/
    private List<PeriodEnum> getTimeSlice() {
        int minute = DateUtil.minute(DateUtil.now());
        List<PeriodEnum> periodList = new ArrayList<>();
        periodList.add(PeriodEnum.MIN_1);
        periodList.add(PeriodEnum.MIN_5);
//        if (minute % 5 == 4) {
//            periodList.add(PeriodEnum.MIN_5);
//        }
        if (minute % 15 == 14) {
            periodList.add(PeriodEnum.MIN_15);
        }
        if (minute % 30 == 29) {
            periodList.add(PeriodEnum.MIN_30);
        }
        if (minute % 60 == 59) {
            periodList.add(PeriodEnum.MIN_60);
        }
        return periodList;
    }

}


