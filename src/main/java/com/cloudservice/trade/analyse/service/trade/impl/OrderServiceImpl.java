package com.cloudservice.trade.analyse.service.trade.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.BaseService;
import com.cloudservice.base.Result;
import com.cloudservice.trade.huobi.enums.*;
import com.cloudservice.trade.analyse.context.AnalyseContext;
import com.cloudservice.trade.huobi.model.contract.Account;
import com.cloudservice.trade.huobi.model.contract.Position;
import com.cloudservice.trade.huobi.model.spot.Kline;
import com.cloudservice.trade.analyse.model.trade.Analyse;
import com.cloudservice.trade.huobi.service.contract.ContractAccountService;
import com.cloudservice.trade.huobi.service.contract.ContractTradeService;
import com.cloudservice.trade.huobi.service.spot.SpotMarketService;
import com.cloudservice.trade.analyse.service.trade.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

/**
 * 计划下单
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/10
 */
@Service
public class OrderServiceImpl extends BaseService implements OrderService {

    @Autowired
    private SpotMarketService spotMarketService;
    @Autowired
    private ContractAccountService contractAccountService;
    @Autowired
    private ContractTradeService contractTradeService;

    @Override
    public Kline getKline(SymbolEnum symbol) {
        List<Kline> klineList = spotMarketService.getKline(SymbolUSDTEnum.getUSDT(symbol.getValue()), PeriodEnum.MIN_1, 1);
        if (CollectionUtils.isEmpty(klineList)) {
            return null;
        }
        Kline kline = klineList.get(0);
        logger.info("[{}] symbol={}, kline={}, 获取当前USD价格", LOG_MARK, symbol, kline);
        return kline;
    }

    @Override
    public int getAvailableVolume(String access, String secret, SymbolEnum symbol, ContractLeverRateEnum leverRate) {
        return this.getAvailableVolume(access, secret, symbol, leverRate, this.getKline(symbol));
    }

    /**
     * @description 获取可用张数
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/10 23:44
     * @param access, secret, symbol, leverRate, kline
     **/
    private int getAvailableVolume(String access, String secret, SymbolEnum symbol, ContractLeverRateEnum leverRate
            , Kline kline) {
        if (kline == null) {
            return 0;
        }
        // 获取账户信息
        Account account = contractAccountService.getAccountInfo(access, secret, symbol);
        if (account == null) {
            return 0;
        }
        logger.info("[{}] access={}， symbol={}, leverRate={}, account={}, 获取账户信息"
                , LOG_MARK, access, symbol, leverRate, account);
        // 计算张数
        return account.getMarginAvailable()
                .multiply(new BigDecimal(kline.getClose() + ""))
                .multiply(new BigDecimal(leverRate.getValue()))
                .divide(VOLUME, new MathContext(2))
                .intValue();
    }

    @Override
    public Result limitOpen(String access, String secret, SymbolEnum symbol, ContractLeverRateEnum leverRate
            , Analyse analyse, int volume) {
        // 限价委托下单
        Result result = contractTradeService.order(access, secret, symbol, ContractTypeEnum.THIS_WEEK, analyse.getPrice(), volume
                , analyse.getDirection(), ContractOffsetEnum.OPEN, leverRate, ContractOrderPriceTypeEnum.LIMIT);
        logger.info("[{}] access={}， symbol={}, leverRate={}, analyse={}, volume={}, result={}, 限价委托下单"
                , LOG_MARK, access, symbol, leverRate, analyse, volume, result);
        return result;
    }

    @Override
    public Result opponentOpen(String access, String secret, SymbolEnum symbol, ContractLeverRateEnum leverRate
            , Analyse analyse, int volume) {
        // 限价委托下单
        Result result = contractTradeService.order(access, secret, symbol, ContractTypeEnum.THIS_WEEK, null, volume
                , analyse.getDirection(), ContractOffsetEnum.OPEN, leverRate, ContractOrderPriceTypeEnum.OPPONENT);
        logger.info("[{}] access={}， symbol={}, leverRate={}, analyse={}, volume={}, result={}, 对手价委托下单"
                , LOG_MARK, access, symbol, leverRate, analyse, volume, result);
        // 记录下单信息
        if (result != null && result.success()) {
            String orderId = JSONObject.parseObject(result.getData().toString()).getLong("order_id") + "";
            AnalyseContext.getTrack(access).setLastOrderId(orderId);
            AnalyseContext.getTrack(access).setLastOpenTime(System.currentTimeMillis());
        }
        return result;
    }

    @Override
    public Result opponentClose(String access, String secret, Position position) {
        if (position == null) {
            return Result.buildFail("position is empty");
        }
        return contractTradeService.order(access, secret, SymbolEnum.get(position.getSymbol()), ContractTypeEnum.get(position.getContractType())
                , null, position.getVolume().longValue()
                , ContractDirectionEnum.get(position.getDirection()).getNegate(), ContractOffsetEnum.CLOSE
                , ContractLeverRateEnum.get(position.getLeverRate() + ""), ContractOrderPriceTypeEnum.OPPONENT);
    }

    @Override
    public Result lightningClose(String access, String secret, Position position) {
        if (position == null) {
            return Result.buildFail("position is empty");
        }
        return contractTradeService.lightningClose(access, secret, SymbolEnum.get(position.getSymbol()), ContractTypeEnum.get(position.getContractType())
                , position.getVolume().longValue(), ContractDirectionEnum.get(position.getDirection()).getNegate());
    }
}


