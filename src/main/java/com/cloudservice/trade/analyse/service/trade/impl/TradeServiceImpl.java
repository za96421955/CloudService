package com.cloudservice.trade.analyse.service.trade.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.BaseService;
import com.cloudservice.base.Result;
import com.cloudservice.trade.analyse.context.AnalyseContext;
import com.cloudservice.trade.analyse.model.trade.Analyse;
import com.cloudservice.trade.analyse.service.trade.OrderService;
import com.cloudservice.trade.analyse.service.trade.TradeService;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.huobi.enums.*;
import com.cloudservice.trade.huobi.model.contract.Order;
import com.cloudservice.trade.huobi.model.contract.Position;
import com.cloudservice.trade.huobi.model.contract.TriggerOrder;
import com.cloudservice.trade.huobi.model.spot.Kline;
import com.cloudservice.trade.huobi.service.contract.ContractAccountService;
import com.cloudservice.trade.huobi.service.contract.ContractTradeService;
import com.cloudservice.trade.huobi.service.spot.SpotMarketService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 交易服务
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/14
 */
@Service
public class TradeServiceImpl extends BaseService implements TradeService {

    @Autowired
    private SpotMarketService spotMarketService;
    @Autowired
    private ContractAccountService contractAccountService;
    @Autowired
    private ContractTradeService contractTradeService;
    @Autowired
    private OrderService orderService;

    @Override
    public Result checkOpen(Track track) {
        // 1, 获取持仓信息
        Position position = contractAccountService.getPositionInfo(track.getAccess(), track.getSecret(), track.getSymbol());
        if (position != null) {
            // 设置开仓订单号
            this.setLastOrderId(track);
            return Result.buildFail("有持仓信息, 不可以开仓", position);
        }
        // 2, 当前下单未全部成交, 不可以开仓
        Order order = contractTradeService.getOrderInfo(track.getAccess(), track.getSecret(), track.getSymbol(), track.getLastOrderId());
        if (order != null && order.getStatus() != 6) {
            return Result.buildFail("当前下单未全部成交, 不可以开仓", order);
        }
        // 3, 撤销所有计划委托
        contractTradeService.triggerCancelAll(track.getAccess(), track.getSecret(), track.getSymbol(), ContractTypeEnum.THIS_WEEK);
        track.clearOpen();
        return Result.buildSuccess();
    }

    /**
     * @description 设置开仓订单号
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/17 14:23
     **/
    private void setLastOrderId(Track track) {
        if (track == null || StringUtils.isNotBlank(track.getLastOrderId())) {
            return;
        }
        List<Order> orderList = contractTradeService.getOrderHistory(
                track.getAccess(), track.getSecret(), track.getSymbol(), ContractTradeTypeHistoryEnum.ALL);
        for (Order order : orderList) {
            if (order == null || !ContractOffsetEnum.OPEN.getValue().equals(order.getOffset())) {
                continue;
            }
            track.setLastOrderId(order.getOrderIdStr());
            return;
        }
    }

    @Override
    public Result orderOpen(Track track) {
        // 获取现价分析信息
        Analyse analyse = AnalyseContext.getAnalyse();
        if (analyse == null) {
            return Result.buildFail("当前分析数据获取失败, 不建议买入");
        }
        logger.info("[{}] track{}, analyse={}, 获取当前分析数据", LOG_MARK, track, analyse);

        // 禁止反向开仓检查
        if (track.cancelDisable(analyse.getDirection())) {
            return Result.buildFail("近期撤单, 禁止反向开仓");
        }
        track.clearCancel();

        // 4, 禁止追仓检查
        Order lastCloseOrder = this.getLastProfitOpenDirection(track);
        if (track.profitDisable(lastCloseOrder, analyse)) {
            return Result.buildFail("近期止盈平仓, 禁止同向追仓");
        }
        if (track.lossDisable(lastCloseOrder, analyse)) {
            return Result.buildFail("近期止损平仓, 禁止逆向追仓");
        }

        // 计算可用张数, 下单张数
        int volume = orderService.getAvailableVolume(track.getAccess(), track.getSecret(), track.getSymbol(), track.getHedgeConfig().getLeverRate());
        if (volume <= 0) {
            return Result.buildFail("可用张数 <= 0");
        }
        int orderVolume = volume / 3;
        // TODO 测试使用1张
        orderVolume = 1;
        logger.info("[{}] track{}, volume={}, orderVolume={}, 计算可用张数, 下单张数", LOG_MARK, track, volume, orderVolume);

        // 下单
        Result result = orderService.limitOpen(track.getAccess(), track.getSecret(), track.getSymbol(), track.getHedgeConfig().getLeverRate()
                , analyse, orderVolume);
        if (result != null && result.success()) {
            String orderId = JSONObject.parseObject(result.getData().toString()).getLong("order_id") + "";
            track.setLastAnalyse(analyse);
            track.setLastOrderId(orderId);
            track.setLastOpenTime(System.currentTimeMillis());
        }
        return result;
    }

    /**
     * @description 获取最后止盈开仓方向
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/23 11:27
     * @param track
     **/
    private Order getLastProfitOpenDirection(Track track) {
        List<Order> orderList = contractTradeService.getOrderHistory(track.getAccess(), track.getSecret(), track.getSymbol()
                , ContractTradeTypeHistoryEnum.ALL);
        for (Order order : orderList) {
            if (order == null) {
                continue;
            }
            if (ContractOffsetEnum.CLOSE.getValue().equals(order.getOffset())) {
                if (order.getOffsetProfitloss().compareTo(BigDecimal.ZERO) > 0) {
                    return order;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public Result checkCancel(Track track) {
        // 1, 获取OrderId
        if (StringUtils.isBlank(track.getLastOrderId())) {
            return Result.buildFail("orderId不存在");
        }
        try {
            // 2, 限价委托成交检查
            Order order = contractTradeService.getOrderInfo(track.getAccess(), track.getSecret(), track.getSymbol(), track.getLastOrderId());
            if (order == null) {
                return Result.buildSuccess();
            }
            if (order.getStatus() == 6) {
                logger.info("[{}] track={}, order={}, 限价委托已全部成交, 发布计划委托", LOG_MARK, track, order);
                return Result.buildSuccess(order);
            }

            // 3, 委托超时未成交, 撤销
            boolean isTimeout = this.checkTimeout(track);
            if (isTimeout) {
                Result result = contractTradeService.cancel(track.getAccess(), track.getSecret(), track.getSymbol(), track.getLastOrderId());
                logger.info("[{}] track={}, result={}, 限价委托超时未成交, 已撤销", LOG_MARK, track, result);
                track.clearOpen();
                // 撤销后5分钟内禁止反向开仓
                track.setLastCancelDirection(ContractDirectionEnum.get(order.getDirection()));
                track.setLastCancelTime(System.currentTimeMillis());
                return Result.buildFail("限价委托超时未成交, 已撤销");
            }
            // 4, 未成交，重新检查
            return Result.buildFail("未成交，重新检查");
        } catch (Exception e) {
            logger.error("[{}] track={}, 限价委托成功, 获取订单异常, {}", LOG_MARK, track, e.getMessage(), e);
            return Result.buildFail(e.getMessage());
        }
    }

    /**
     * @description 超时检查
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/14 15:00
     **/
    private boolean checkTimeout(Track track) {
        if (track.getLastOpenTime() == null) {
            return false;
        }
        return (System.currentTimeMillis() - track.getLastOpenTime()) > OPEN_TIMEOUT;
    }

    @Override
    public Result closeTrack(Track track) {
        // 获取持仓信息
        Position position = contractAccountService.getPositionInfo(track.getAccess(), track.getSecret(), track.getSymbol());
        if (position == null) {
            return Result.buildFail("无持仓信息");
        }

        // 限价击穿检查
        if (this.checkBreakdown(track)) {
            // 撤销所有限价委托
            Result result = contractTradeService.cancelAll(track.getAccess(), track.getSecret(), track.getSymbol());
            logger.info("[{}] track={}, result={}, 撤销所有限价委托", LOG_MARK, track, result);
            track.setBreakdownNum(track.getBreakdownNum() + 1);
            if (track.getBreakdownNum() <= 2) {
                // 第一次击穿, 对手价平仓
                result = orderService.opponentClose(track.getAccess(), track.getSecret(), position);
                logger.info("[{}] track={}, result={}, 第{}次击穿, 对手价平仓", LOG_MARK, track, result, track.getBreakdownNum());
            } else {
                // 第二次击穿, 闪电平仓
                result = orderService.lightningClose(track.getAccess(), track.getSecret(), position);
                logger.info("[{}] track={}, result={}, 第{}次击穿, 闪电平仓", LOG_MARK, track, result, track.getBreakdownNum());
            }
            return Result.buildFail("限价击穿, 平仓");
        }

        /**
         * 止盈、止损计划检查
         */
        // 获取计划委托运算符
        ContractDirectionEnum direction = ContractDirectionEnum.get(position.getDirection());
        ContractTriggerTypeEnum triggerType;
        if (ContractDirectionEnum.BUY.equals(direction)) {
            triggerType = ContractTriggerTypeEnum.GE;
        } else {
            triggerType = ContractTriggerTypeEnum.LE;
        }

        // 止盈、止损计划检查
        List<TriggerOrder> triggerOrderList = contractTradeService.getTriggerOrders(track.getAccess(), track.getSecret(), track.getSymbol(), 1, 20);
        boolean isProfit = false;
        boolean isLoss = false;
        for (TriggerOrder order : triggerOrderList) {
            isProfit |= this.isProfit(position, order);
            isLoss |= this.isProfit(position, order);
        }
        // 计划委托, 止盈
        if (!isProfit) {
            Result result = contractTradeService.triggerOrder(track.getAccess(), track.getSecret(), track.getSymbol(), ContractTypeEnum.THIS_WEEK
                    , triggerType, track.getLastAnalyse().getTriggerProfit(), track.getLastAnalyse().getProfit(), ContractOrderPriceTypeEnum.LIMIT
                    , position.getVolume().intValue(), direction.getNegate(), ContractOffsetEnum.CLOSE
                    , ContractLeverRateEnum.get(position.getLeverRate() + ""));
            logger.info("[{}] track={}, result={}, 计划委托, 止盈", LOG_MARK, track, result);
        }
        // 计划委托, 止损
        if (!isLoss) {
            Result result = contractTradeService.triggerOrder(track.getAccess(), track.getSecret(), track.getSymbol(), ContractTypeEnum.THIS_WEEK
                    , triggerType.getNegate(), track.getLastAnalyse().getTriggerLoss(), track.getLastAnalyse().getLoss(), ContractOrderPriceTypeEnum.LIMIT
                    , position.getVolume().intValue(), direction.getNegate(), ContractOffsetEnum.CLOSE
                    , ContractLeverRateEnum.get(position.getLeverRate() + ""));
            logger.info("[{}] track={}, result={}, 计划委托, 止损", LOG_MARK, track, result);
        }
        return Result.buildSuccess();
    }

    /**
     * @description 击穿检查
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/18 14:33
     * @param track
     **/
    private boolean checkBreakdown(Track track) {
        Kline kline = spotMarketService.getKlineCurr(SymbolUSDTEnum.getUSDT(track.getSymbol().getValue()));
        if (kline == null) {
            return false;
        }
        // 无现价分析, 则直接表示击穿
        if (track.getLastAnalyse() == null) {
            return true;
        }
        Analyse analyse = track.getLastAnalyse();
        BigDecimal max = analyse.getProfit();
        BigDecimal min = analyse.getProfit();
        if (analyse.getProfit().compareTo(analyse.getLoss()) > 0) {
            min = analyse.getLoss();
        } else {
            max = analyse.getLoss();
        }
        return kline.getClose().compareTo(max) > 0 || kline.getClose().compareTo(min) < 0;
    }

    /**
     * @description 是否止盈计划委托
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/18 14:12
     **/
    private boolean isProfit(Position position, TriggerOrder order) {
        Boolean check = this.checkPrevent(position, order);
        return check != null && check;
    }

    /**
     * @description 是否止损计划委托
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/18 14:12
     **/
    private boolean isLoss(Position position, TriggerOrder order) {
        Boolean check = this.checkPrevent(position, order);
        return check != null && !check;
    }

    /**
     * @description 止盈止损检查
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/18 14:10
     * @return null: 无, true: 止盈, false: 止损
     **/
    private Boolean checkPrevent(Position position, TriggerOrder order) {
        if (position == null || order == null
                || !ContractOffsetEnum.CLOSE.getValue().equals(order.getOffset())
                || order.getTriggerPrice().compareTo(position.getCostOpen()) == 0) {
            return null;
        }
        // 止盈: 1, 开多, 触发价 > 持仓价; 2, 开空, 触发价 < 持仓价
        // 止损: 1, 开多, 触发价 < 持仓价; 2, 开空, 触发价 > 持仓价
        if (ContractDirectionEnum.BUY.getValue().equals(position.getDirection())) {
            return order.getTriggerPrice().compareTo(position.getCostOpen()) > 0;
        } else {
            return order.getTriggerPrice().compareTo(position.getCostOpen()) < 0;
        }
    }

}


