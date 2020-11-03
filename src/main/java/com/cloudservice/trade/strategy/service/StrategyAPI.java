package com.cloudservice.trade.strategy.service;

import com.cloudservice.base.Result;
import com.cloudservice.trade.huobi.model.contract.Position;
import com.cloudservice.trade.strategy.model.Entrust;

import java.math.BigDecimal;

/**
 * 策略API
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/10/22
 */
public interface StrategyAPI {
    String LOG_MARK = "策略API";

    /**
     * @description 持仓检查
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/24 15:05
     * @param entrust
     **/
    Result positionCheck(Entrust entrust);

    /**
     * @description 止盈
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/25 14:13
     * @param entrust, position
     **/
    Result profit(Entrust entrust, Position position);

    /**
     * @description 止损
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/25 14:13
     * @param entrust, position
     **/
    Result loss(Entrust entrust, Position position);

    /**
     * @description 获取止损差价
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/10/22 13:53
     * @param entrust, position
     **/
    BigDecimal getLossDiffPrice(Entrust entrust, Position position);

    /**
     * @description 获取止损追仓张数
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/10/22 13:53
     * @param entrust, position
     **/
    BigDecimal getLossChaseVolume(Entrust entrust, Position position);

    /**
     * @description 停止交易
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/10/22 14:47
     * @param entrust
     **/
    boolean closeTrade(Entrust entrust);

}


