package com.cloudservice.trade.analyse.service.trade;

import com.cloudservice.base.Result;
import com.cloudservice.trade.analyse.model.trade.Analyse;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import com.cloudservice.trade.huobi.enums.SymbolEnum;
import com.cloudservice.trade.huobi.model.contract.Position;
import com.cloudservice.trade.huobi.model.spot.Kline;

import java.math.BigDecimal;

/**
 * 计划下单
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/10
 */
public interface OrderService {
    String LOG_MARK = "计划下单";

    // 1张 = 10USD
    BigDecimal VOLUME = BigDecimal.valueOf(10);

    /**
     * @description 获取当前K线数据
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/10 23:40
     * @param symbol
     **/
    Kline getKline(SymbolEnum symbol);

    /**
     * @description 获取可用张数
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/10 20:32
     * @param access, secret, leverRate
     **/
    int getAvailableVolume(String access, String secret, SymbolEnum symbol, ContractLeverRateEnum leverRate);

    /**
     * @description 限价委托下单
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/10 20:33
     * @param access, secret, symbol, leverRate, analyse, volume
     */
    Result limitOpen(String access, String secret, SymbolEnum symbol, ContractLeverRateEnum leverRate
            , Analyse analyse, int volume);

     /**
      * @description 对手价开仓
      * <p>〈功能详细描述〉</p>
      *
      * @author 陈晨
      * @date 2020/9/14 17:31
      **/
    Result opponentOpen(String access, String secret, SymbolEnum symbol, ContractLeverRateEnum leverRate
            , Analyse analyse, int volume);

    /**
     * @description 对手价平仓
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/14 13:59
     * @param access, secret, position
     **/
    Result opponentClose(String access, String secret, Position position);

    /**
     * @description 闪电平仓
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/18 14:39
     * @param access, secret, position
     **/
    Result lightningClose(String access, String secret, Position position);

}


