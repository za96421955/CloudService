package com.cloudservice.trade.huobi.service.contract.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.BaseService;
import com.cloudservice.base.Result;
import com.cloudservice.trade.huobi.context.ContractAPI;
import com.cloudservice.trade.huobi.context.Host;
import com.cloudservice.trade.huobi.enums.*;
import com.cloudservice.trade.huobi.model.contract.Order;
import com.cloudservice.trade.huobi.model.contract.TriggerOrder;
import com.cloudservice.trade.huobi.service.HuobiHttpRequest;
import com.cloudservice.trade.huobi.service.contract.ContractTradeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 交割合约：交易接口
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
@Service
public class ContractTradeServiceImpl extends BaseService implements ContractTradeService {

    @Override
    public Result order(String access, String secret, SymbolEnum symbol, ContractTypeEnum type, BigDecimal price, long volume
            , ContractDirectionEnum direction, ContractOffsetEnum offset, ContractLeverRateEnum leverRate, ContractOrderPriceTypeEnum orderPriceType) {
        JSONObject data = new JSONObject();
        data.put("symbol", symbol.getValue());
        data.put("contract_type", type.getValue());
        data.put("volume", volume);
        data.put("direction", direction.getValue());
        data.put("offset", offset.getValue());
        data.put("lever_rate", leverRate.getValue());
        data.put("order_price_type", orderPriceType.getValue());
        if (price != null) {
            data.put("price", price);
        }
        String result = HuobiHttpRequest.post(access, secret, Host.CONTRACT, ContractAPI.Trade.ORDER.getApi(), data.toString());
        return Result.parse(result);
    }

    @Override
    public Result cancel(String access, String secret, SymbolEnum symbol, String orderId) {
        JSONObject data = new JSONObject();
        data.put("symbol", symbol.getValue());
        data.put("order_id", orderId);
        String result = HuobiHttpRequest.post(access, secret, Host.CONTRACT, ContractAPI.Trade.CANCEL.getApi(), data.toString());
        return Result.parse(result);
    }

    @Override
    public Result cancelAll(String access, String secret, SymbolEnum symbol) {
        JSONObject data = new JSONObject();
        data.put("symbol", symbol.getValue());
        String result = HuobiHttpRequest.post(access, secret, Host.CONTRACT, ContractAPI.Trade.CANCELALL.getApi(), data.toString());
        return Result.parse(result);
    }

    @Override
    public Order getOrderInfo(String access, String secret, SymbolEnum symbol, String orderId) {
        if (StringUtils.isBlank(orderId)) {
            return null;
        }
        JSONObject data = new JSONObject();
        data.put("symbol", symbol.getValue());
        data.put("order_id", orderId);
        String result = HuobiHttpRequest.post(access, secret, Host.CONTRACT, ContractAPI.Trade.ORDER_INFO.getApi(), data.toString());
        return Order.parseList(this.getDataArray(result)).get(0);
    }

    @Override
    public List<Order> getOrderHistory(String access, String secret, SymbolEnum symbol, ContractTradeTypeHistoryEnum tradeType) {
        return this.getOrderHistory(access, secret, symbol, tradeType, 1, 20, null, null);
    }

    @Override
    public List<Order> getOrderHistory(String access, String secret, SymbolEnum symbol, ContractTradeTypeHistoryEnum tradeType
            , int page, int pageSize, Date beginTime, Date endTime) {
        List<Order> orderList = this.getOrderHistoryAll(access, secret, symbol, tradeType, page, pageSize, beginTime);
        // 移除不在查询时间范围内的订单
        if (beginTime != null || endTime != null) {
            for (int i = orderList.size() - 1; i >= 0; i--) {
                Order order = orderList.get(i);
                if (order == null) {
                    continue;
                }
                if (beginTime != null && order.getCreateDate() < beginTime.getTime()) {
                    orderList.remove(i);
                }
                if (endTime != null && order.getCreateDate() > endTime.getTime()) {
                    orderList.remove(i);
                }
            }
        }
        return orderList;
    }

    /**
     * @description 查询全部历史成交订单信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/15 16:05
     * @param access, secret, symbol, tradeType, page, pageSize, beginTime
     **/
    private List<Order> getOrderHistoryAll(String access, String secret, SymbolEnum symbol, ContractTradeTypeHistoryEnum tradeType
            , int page, int pageSize, Date beginTime) {
        // 查询当前页成交订单集合
        JSONObject data = new JSONObject();
        data.put("symbol", symbol.getValue());
        data.put("trade_type", tradeType.getValue());
        data.put("create_date", 7);
        data.put("page_index", page);
        data.put("page_size", pageSize);
        JSONObject result = JSONObject.parseObject(HuobiHttpRequest.post(access, secret, Host.CONTRACT, ContractAPI.Trade.MATCHRESULTS.getApi(), data.toString()));
        JSONObject resultData = result.getJSONObject("data");
        List<Order> orderList = Order.parseList(resultData.getJSONArray("trades"));
        if (beginTime == null) {
            return orderList;
        }
        // 若设置了开始时间, 且最后一条记录成交时间 > 开始时间, 则继续查下一页
        Order lastOrder = orderList.get(orderList.size() - 1);
        if (lastOrder.getCreateDate() > beginTime.getTime()) {
            orderList.addAll(this.getOrderHistoryAll(access, secret, symbol, tradeType
                    , resultData.getInteger("current_page") + 1, pageSize, beginTime));
        }
        return orderList;
    }

    @Override
    public List<Order> getOpenOrders(String access, String secret, SymbolEnum symbol, int page, int pageSize) {
        JSONObject data = new JSONObject();
        data.put("symbol", symbol.getValue());
        data.put("page_index", page);
        data.put("page_size", pageSize);
        String result = HuobiHttpRequest.post(access, secret, Host.CONTRACT, ContractAPI.Trade.OPENORDERS.getApi(), data.toString());
        return Order.parseList(this.getData(result).getJSONArray("trades"));
    }

    @Override
    public Result lightningClose(String access, String secret, SymbolEnum symbol, ContractTypeEnum type, long volume, ContractDirectionEnum direction) {
        JSONObject data = new JSONObject();
        data.put("symbol", symbol.getValue());
        data.put("contract_type", type.getValue());
        data.put("volume", volume);
        data.put("direction", direction.getValue());
        String result = HuobiHttpRequest.post(access, secret, Host.CONTRACT, ContractAPI.Trade.LIGHTNING_CLOSE_POSITION.getApi(), data.toString());
        return Result.parse(result);
    }

    @Override
    public Result triggerOrder(String access, String secret, SymbolEnum symbol, ContractTypeEnum type
            , ContractTriggerTypeEnum triggerType, BigDecimal triggerPrice, BigDecimal orderPrice
            , ContractOrderPriceTypeEnum orderPriceType, long volume
            , ContractDirectionEnum direction, ContractOffsetEnum offset, ContractLeverRateEnum leverRate) {
        JSONObject data = new JSONObject();
        data.put("symbol", symbol.getValue());
        data.put("contract_type", type.getValue());
        data.put("trigger_type", triggerType.getValue());
        data.put("trigger_price", triggerPrice);
        data.put("order_price", orderPrice);
        data.put("order_price_type", orderPriceType.getValue());
        data.put("volume", volume);
        data.put("direction", direction.getValue());
        data.put("offset", offset.getValue());
        data.put("lever_rate", leverRate.getValue());
        String result = HuobiHttpRequest.post(access, secret, Host.CONTRACT, ContractAPI.Trade.TRIGGER_ORDER.getApi(), data.toString());
        return Result.parse(result);
    }

    @Override
    public Result triggerCancel(String access, String secret, SymbolEnum symbol, String orderId) {
        JSONObject data = new JSONObject();
        data.put("symbol", symbol.getValue());
        data.put("order_id", orderId);
        String result = HuobiHttpRequest.post(access, secret, Host.CONTRACT, ContractAPI.Trade.TRIGGER_CANCEL.getApi(), data.toString());
        return Result.parse(result);
    }

    @Override
    public Result triggerCancelAll(String access, String secret, SymbolEnum symbol, ContractTypeEnum type) {
        JSONObject data = new JSONObject();
        data.put("symbol", symbol.getValue());
        data.put("contract_type", type.getValue());
        String result = HuobiHttpRequest.post(access, secret, Host.CONTRACT, ContractAPI.Trade.TRIGGER_CANCELALL.getApi(), data.toString());
        return Result.parse(result);
    }

    @Override
    public List<TriggerOrder> getTriggerOrders(String access, String secret, SymbolEnum symbol, int page, int pageSize) {
        JSONObject data = new JSONObject();
        data.put("symbol", symbol.getValue());
        data.put("page_index", page);
        data.put("page_size", pageSize);
        String result = HuobiHttpRequest.post(access, secret, Host.CONTRACT, ContractAPI.Trade.TRIGGER_OPENORDERS.getApi(), data.toString());
        return TriggerOrder.parseList(this.getData(result).getJSONArray("orders"));
    }
}


