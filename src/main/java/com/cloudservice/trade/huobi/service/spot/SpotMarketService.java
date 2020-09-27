package com.cloudservice.trade.huobi.service.spot;

import com.cloudservice.trade.analyse.model.statistic.KlineRange;
import com.cloudservice.trade.huobi.enums.*;
import com.cloudservice.trade.huobi.model.spot.Depth;
import com.cloudservice.trade.huobi.model.spot.Kline;
import com.cloudservice.trade.huobi.model.spot.Ticker;

import java.math.BigDecimal;
import java.util.List;


/**
 * 现货：市场接口
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public interface SpotMarketService {

    int RANGE_COUNT = 10;

    /**
     * @description 获取K线数据
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 14:52
     * @param symbol, period, size
     *
     * @param symbol
     * @return*/
    List<Kline> getKline(SymbolUSDTEnum symbol, PeriodEnum period, int size);

    /**
     * @description 获取当前K线信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/18 14:18
     * @param symbol
     **/
    Kline getKlineCurr(SymbolUSDTEnum symbol);

    /**
     * @description 获取差价区间
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/16 15:21
     * @param kline, price
     **/
    Integer getRange(Kline kline, BigDecimal price);

    /**
     * @description 获取K线涨跌
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/16 15:24
     * @param kline
     **/
    ContractDirectionEnum getDirection(KlineRange kline);

    /**
     * @description 获取最近24小时聚合行情
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 14:53
     * @param symbol
     *
     * @return*/
    Ticker getMerged(SymbolEnum symbol);

    /**
     * @description 获取当前市场深度数据
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 15:03
     * @param symbol, depth, depthType
     *
     * @param symbol
     * @return*/
    Depth getDepth(SymbolUSDTEnum symbol, DepthEnum depth, DepthTypeEnum depthType);

}

