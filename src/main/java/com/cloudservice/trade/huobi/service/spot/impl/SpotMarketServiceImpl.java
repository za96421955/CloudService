package com.cloudservice.trade.huobi.service.spot.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.BaseService;
import com.cloudservice.trade.analyse.model.statistic.KlineRange;
import com.cloudservice.trade.huobi.context.Host;
import com.cloudservice.trade.huobi.context.SpotAPI;
import com.cloudservice.trade.huobi.enums.*;
import com.cloudservice.trade.huobi.model.spot.Depth;
import com.cloudservice.trade.huobi.model.spot.Kline;
import com.cloudservice.trade.huobi.model.spot.Ticker;
import com.cloudservice.trade.huobi.service.HuobiHttpRequest;
import com.cloudservice.trade.huobi.service.spot.SpotMarketService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

/**
 * 现货：基础接口
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
@Service
public class SpotMarketServiceImpl extends BaseService implements SpotMarketService {

    @Override
    public List<Kline> getKline(SymbolUSDTEnum symbol, PeriodEnum period, int size) {
        StringBuilder data = new StringBuilder();
        data.append("symbol=").append(symbol.getValue());
        data.append("&period=").append(period.getValue());
        data.append("&size=").append(size);
        String result = HuobiHttpRequest.get(Host.REST_API, SpotAPI.Market.KLINE.getApi(), data.toString());
        return Kline.parseList(JSONObject.parseObject(result).getJSONArray("data"));
    }

    @Override
    public Kline getKlineCurr(SymbolUSDTEnum symbol) {
        List<Kline> klineList = this.getKline(symbol, PeriodEnum.MIN_1, 1);
        if (CollectionUtils.isEmpty(klineList)) {
            return null;
        }
        return klineList.get(0);
    }

    @Override
    public Integer getRange(Kline kline, BigDecimal price) {
        if (kline == null) {
            return null;
        }
        BigDecimal diffRange = kline.getHigh().subtract(kline.getLow())
                .divide(BigDecimal.valueOf(RANGE_COUNT), new MathContext(8));
        if (diffRange.compareTo(BigDecimal.ZERO) <= 0) {
            return 1;
        }
        BigDecimal closeDiff = price.subtract(kline.getLow());
        if (closeDiff.compareTo(BigDecimal.ZERO) < 0) {
            closeDiff = BigDecimal.ZERO;
        }
        int range = closeDiff.divide(diffRange, new MathContext(2)).add(BigDecimal.ONE).intValue();
        return Math.min(range, RANGE_COUNT);
    }

    @Override
    public ContractDirectionEnum getDirection(KlineRange klineRange) {
        if (klineRange == null) {
            return null;
        }
        if (klineRange.getHighRate().compareTo(klineRange.getLowRate()) < 0) {
            return ContractDirectionEnum.SELL;
        }
        return ContractDirectionEnum.BUY;
    }

    @Override
    public Ticker getMerged(SymbolEnum symbol) {
        StringBuilder data = new StringBuilder();
        data.append("symbol=").append(symbol.getValue());
        String result = HuobiHttpRequest.get(Host.REST_API, SpotAPI.Market.MERGED.getApi(), data.toString());
        return Ticker.parse(JSONObject.parseObject(result).getJSONObject("tick"));
    }

    @Override
    public Depth getDepth(SymbolUSDTEnum symbol, DepthEnum depth, DepthTypeEnum depthType) {
        StringBuilder data = new StringBuilder();
        data.append("symbol=").append(symbol.getValue());
        data.append("&depth=").append(depth.getValue());
        data.append("&type=").append(depthType.getValue());
        String result = HuobiHttpRequest.get(Host.REST_API, SpotAPI.Market.DEPTH.getApi(), data.toString());
        return Depth.parse(JSONObject.parseObject(result).getJSONObject("tick"));
    }

}


