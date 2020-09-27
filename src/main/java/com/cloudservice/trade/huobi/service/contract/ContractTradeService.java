package com.cloudservice.trade.huobi.service.contract;

import com.cloudservice.base.Result;
import com.cloudservice.trade.huobi.enums.*;
import com.cloudservice.trade.huobi.model.contract.Order;
import com.cloudservice.trade.huobi.model.contract.TriggerOrder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 交割合约：交易接口
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/5
 */
public interface ContractTradeService {

    /**
     * @description 下单
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 17:55
     * @param access
     * @param secret
     * @param symbol
     * @param type              合约类型
     * @param price             价格
     * @param volume            委托数量(张)
     * @param direction         "buy":买 "sell":卖
     * @param offset            "open":开 "close":平
     * @param leverRate         杠杆倍数
     * @param orderPriceType    订单报价类型
     *
     * @return*/
    Result order(String access, String secret, SymbolEnum symbol, ContractTypeEnum type, BigDecimal price, long volume
            , ContractDirectionEnum direction, ContractOffsetEnum offset, ContractLeverRateEnum leverRate, ContractOrderPriceTypeEnum orderPriceType);

    /**
     * @description 撤销订单
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/9 10:40
     * @param access, secret, symbol, orderId
     *
     * @return*/
    Result cancel(String access, String secret, SymbolEnum symbol, String orderId);

    /**
     * @description 撤销全部订单
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/9 10:40
     * @param access, secret, symbol
     *
     * @return*/
    Result cancelAll(String access, String secret, SymbolEnum symbol);

    /**
     * @description 获取指定订单信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/9 10:40
     * @param access, secret, symbol, orderId
     *
     * @return*/
    Order getOrderInfo(String access, String secret, SymbolEnum symbol, String orderId);

    /**
     * @description 获取历史成交记录
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/9 13:55
     * @param access, secret, symbol, tradeType
     */
    List<Order> getOrderHistory(String access, String secret, SymbolEnum symbol, ContractTradeTypeHistoryEnum tradeType);

    /**
     * @description 获取历史成交记录
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/15 16:12
     * @param access, secret, symbol, tradeType, page, pageSize, beginTime, endTime
     **/
    List<Order> getOrderHistory(String access, String secret, SymbolEnum symbol, ContractTradeTypeHistoryEnum tradeType
            , int page, int pageSize, Date beginTime, Date endTime);

    /**
    * @description 获取未成交委托
    * <p>〈功能详细描述〉</p>
    *
    * @author 陈晨
    * @date 2020/9/11 16:41
    * @param access, secret, symbol, page, pageSize
    **/
    @Deprecated
    List<Order> getOpenOrders(String access, String secret, SymbolEnum symbol, int page, int pageSize);

    /**
     * @description 闪电平仓
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/9 10:47
     * @param access, secret, symbol, type, volume, direction
     *
     * @return*/
    Result lightningClose(String access, String secret, SymbolEnum symbol, ContractTypeEnum type, long volume, ContractDirectionEnum direction);

    /**
     * @description 计划委托
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 17:55
     * @param access
     * @param secret
     * @param symbol
     * @param type              合约类型
     * @param triggerType       触发类型
     * @param triggerPrice      触发价
     * @param orderPrice        委托价
     * @param orderPriceType    委托类型
     * @param volume            委托数量(张)
     * @param direction         "buy":买 "sell":卖
     * @param offset            "open":开 "close":平
     * @param leverRate         杠杆倍数
     *
     * @return*/
    Result triggerOrder(String access, String secret, SymbolEnum symbol, ContractTypeEnum type
            , ContractTriggerTypeEnum triggerType, BigDecimal triggerPrice, BigDecimal orderPrice
            , ContractOrderPriceTypeEnum orderPriceType, long volume
            , ContractDirectionEnum direction, ContractOffsetEnum offset, ContractLeverRateEnum leverRate);

    /**
     * @description 计划委托撤单
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/9 10:40
     * @param access, secret, symbol, orderId
     *
     * @return*/
    Result triggerCancel(String access, String secret, SymbolEnum symbol, String orderId);

    /**
     * @description 计划委托全部撤单
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/9 10:40
     * @param access, secret, symbol, type
     *
     * @return*/
    Result triggerCancelAll(String access, String secret, SymbolEnum symbol, ContractTypeEnum type);

    /**
     * @description 获取当前计划委托订单
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/9 11:07
     * @param access, secret, symbol, page, pageSize
     *
     * @return*/
    List<TriggerOrder> getTriggerOrders(String access, String secret, SymbolEnum symbol, int page, int pageSize);

}


