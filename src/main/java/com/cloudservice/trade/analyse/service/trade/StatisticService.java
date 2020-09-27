package com.cloudservice.trade.analyse.service.trade;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.trade.analyse.model.statistic.KlineRange;
import com.cloudservice.trade.analyse.model.statistic.PullTrack;
import com.cloudservice.trade.huobi.enums.KlineFluctuationEnum;
import com.cloudservice.trade.huobi.enums.PeriodEnum;
import com.cloudservice.trade.huobi.enums.SymbolUSDTEnum;

import java.util.List;
import java.util.Map;

/**
 * 统计服务
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/16
 */
public interface StatisticService {

    /**
     * @description 成交订单盈亏统计
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/16 14:25
     * @param access, secret, symbol, tradeType, beginTime, endTime
     **/
    JSONObject transactionOrder(String access, String secret, String symbol, String tradeType
            , String beginTime, String endTime);

    /**
     * @description 获取K线区间信息
     * <p>时间粒度综合</p>
     *
     * @author 陈晨
     * @date 2020/9/16 16:04
     * @param symbol
     **/
    Map<Integer, KlineRange> getKlineRange(SymbolUSDTEnum symbol);

    /**
     * @description 获取K线区间信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/16 14:27
     * @param symbol
     **/
    Map<Integer, KlineRange> getKlineRange(SymbolUSDTEnum symbol, PeriodEnum period, int size);

    /**
     * @description 获取近期K线波动趋势
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/16 17:56
     * @param symbol
     **/
    KlineFluctuationEnum getFluctuation(SymbolUSDTEnum symbol);

    /**
     * @description 获取拉盘追踪信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/21 15:50
     * @param symbol, period, size
     **/
    List<PullTrack> getPullTrack(SymbolUSDTEnum symbol, PeriodEnum period, int size);

}


