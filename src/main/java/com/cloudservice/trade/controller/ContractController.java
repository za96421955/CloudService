package com.cloudservice.trade.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.BaseController;
import com.cloudservice.base.Result;
import com.cloudservice.trade.huobi.enums.*;
import com.cloudservice.trade.huobi.model.contract.Account;
import com.cloudservice.trade.huobi.model.contract.Order;
import com.cloudservice.trade.huobi.model.contract.Position;
import com.cloudservice.trade.huobi.model.contract.TriggerOrder;
import com.cloudservice.trade.huobi.service.contract.ContractAccountService;
import com.cloudservice.trade.huobi.service.contract.ContractTradeService;
import com.cloudservice.utils.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 控制器：交割合约
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/9
 */
@RestController
@RequestMapping("/contract")
public class ContractController extends BaseController {

    @Autowired
    private ContractAccountService contractAccountService;
    @Autowired
    private ContractTradeService contractTradeService;

    @PostMapping("/account/{symbol}")
    @Description("获取账户信息")
    public Account account(String access, String secret, @PathVariable String symbol) {
        try {
            return contractAccountService.getAccountInfo(access, secret, SymbolEnum.get(symbol));
        } catch (Exception e) {
            logger.error("[交割合约] symbol={}, 获取账户信息异常, {}", symbol, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/position/{symbol}")
    @Description("获取用户持仓信息")
    public List<Position> position(String access, String secret, @PathVariable String symbol) {
        try {
            return contractAccountService.getPositionList(access, secret, SymbolEnum.get(symbol));
        } catch (Exception e) {
            logger.error("[交割合约] symbol={}, 获取用户持仓信息异常, {}", symbol, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/accountPosition/{symbol}")
    @Description("查询用户账户和持仓信息")
    public Account accountPosition(String access, String secret, @PathVariable String symbol) {
        try {
            return contractAccountService.getAccountPositionInfo(access, secret, SymbolEnum.get(symbol));
        } catch (Exception e) {
            logger.error("[交割合约] symbol={}, 查询用户账户和持仓信息异常, {}", symbol, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/fee/{symbol}")
    @Description("查询用户当前的手续费费率")
    public JSONObject fee(String access, String secret, @PathVariable String symbol) {
        try {
            return contractAccountService.getFee(access, secret, SymbolEnum.get(symbol));
        } catch (Exception e) {
            logger.error("[交割合约] symbol={}, 查询用户当前的手续费费率异常, {}", symbol, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/order/{symbol}/{type}")
    @Description("下单")
    public Result order(String access, String secret, @PathVariable String symbol, @PathVariable String type, BigDecimal price, long volume
            , String direction, String offset, String leverRate, String orderPriceType) {
        try {
            return contractTradeService.order(access, secret, SymbolEnum.get(symbol), ContractTypeEnum.get(type), price, volume
                    , ContractDirectionEnum.get(direction), ContractOffsetEnum.get(offset), ContractLeverRateEnum.get(leverRate)
                    , ContractOrderPriceTypeEnum.get(orderPriceType));
        } catch (Exception e) {
            logger.error("[交割合约] symbol={}, type={}, price={}, volume={}, direction={}, offset={}, leverRate={}, orderPriceType={}, 下单异常, {}"
                    , symbol, type, price, volume, direction, offset, leverRate, orderPriceType, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/cancel/{symbol}")
    @Description("撤销订单")
    public Result cancel(String access, String secret, @PathVariable String symbol, String orderId) {
        try {
            if (StringUtils.isBlank(orderId)) {
                return contractTradeService.cancelAll(access, secret, SymbolEnum.get(symbol));
            } else {
                return contractTradeService.cancel(access, secret, SymbolEnum.get(symbol), orderId);
            }
        } catch (Exception e) {
            logger.error("[交割合约] symbol={}, orderId={}, 撤销订单异常, {}", symbol, orderId, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/orderInfo/{symbol}")
    @Description("获取指定订单信息")
    public Order orderInfo(String access, String secret, @PathVariable String symbol, String orderId) {
        try {
            return contractTradeService.getOrderInfo(access, secret, SymbolEnum.get(symbol), orderId);
        } catch (Exception e) {
            logger.error("[交割合约] symbol={}, orderId={}, 获取当前开仓订单信息异常, {}"
                    , symbol, orderId, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/history/{symbol}/{tradeType}")
    @Description("获取历史成交记录")
    public List<Order> orderHistory(String access, String secret, @PathVariable String symbol, @PathVariable String tradeType
            , String beginTime, String endTime) {
        try {
            if (StringUtils.isBlank(beginTime)) {
                return contractTradeService.getOrderHistory(access, secret, SymbolEnum.get(symbol), ContractTradeTypeHistoryEnum.get(tradeType));
            } else {
                return contractTradeService.getOrderHistory(access, secret, SymbolEnum.get(symbol), ContractTradeTypeHistoryEnum.get(tradeType)
                        , 1, 50
                        , DateUtil.parse(beginTime, DateUtil.DATE_LONG)
                        , DateUtil.parse(endTime, DateUtil.DATE_LONG));
            }
        } catch (Exception e) {
            logger.error("[交割合约] symbol={}, tradeType={}, 获取当前开仓订单信息异常, {}"
                    , symbol, tradeType, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/openOrder/{symbol}")
    @Description("获取未成交委托")
    public List<Order> openOrder(String access, String secret, @PathVariable String symbol
            , int page, int pageSize) {
        try {
            return contractTradeService.getOpenOrders(access, secret, SymbolEnum.get(symbol), page, pageSize);
        } catch (Exception e) {
            logger.error("[交割合约] symbol={}, page={}, pageSize={}, 获取未成交委托异常, {}"
                    , symbol, page, pageSize, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/lightningClose/{symbol}/{type}")
    @Description("闪电平仓")
    public Result lightningClose(String access, String secret, @PathVariable String symbol, @PathVariable String type
            , long volume, String direction) {
        try {
            return contractTradeService.lightningClose(access, secret, SymbolEnum.get(symbol), ContractTypeEnum.get(type)
                    , volume, ContractDirectionEnum.get(direction));
        } catch (Exception e) {
            logger.error("[交割合约] symbol={}, type={}, volume={}, direction={}, 闪电平仓异常, {}"
                    , symbol, type, volume, direction, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/triggerOrder/{symbol}/{type}")
    @Description("计划委托")
    public Result triggerOrder(String access, String secret, @PathVariable String symbol, @PathVariable String type
            , String triggerType, BigDecimal triggerPrice, BigDecimal orderPrice
            , String orderPriceType, long volume
            , String direction, String offset, String leverRate) {
        try {
            return contractTradeService.triggerOrder(access, secret, SymbolEnum.get(symbol), ContractTypeEnum.get(type)
                    , ContractTriggerTypeEnum.get(triggerType), triggerPrice, orderPrice
                    , ContractOrderPriceTypeEnum.get(orderPriceType), volume
                    , ContractDirectionEnum.get(direction), ContractOffsetEnum.get(offset), ContractLeverRateEnum.get(leverRate));
        } catch (Exception e) {
            logger.error("[交割合约] symbol={}, type={}, triggerType={}, triggerPrice={}, orderPrice={}, orderPriceType={}, volume={}" +
                            ", direction={}, offset={}, leverRate={}, 计划委托异常, {}"
                    , symbol, type, triggerType, triggerPrice, orderPrice, orderPriceType, volume
                    , direction, offset, leverRate, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/triggerCancel/{symbol}/{type}")
    @Description("计划委托撤单")
    public Result triggerCancel(String access, String secret, @PathVariable String symbol, @PathVariable String type, String orderId) {
        try {
            if (StringUtils.isBlank(orderId)) {
                return contractTradeService.triggerCancelAll(access, secret, SymbolEnum.get(symbol), ContractTypeEnum.get(type));
            } else {
                return contractTradeService.triggerCancel(access, secret, SymbolEnum.get(symbol), orderId);
            }
        } catch (Exception e) {
            logger.error("[交割合约] symbol={}, type={}, orderId={}, 计划委托撤单异常, {}"
                    , symbol, type, orderId, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/triggerOrderInfo/{symbol}")
    @Description("获取当前计划委托订单")
    public List<TriggerOrder> triggerOrderInfo(String access, String secret, @PathVariable String symbol, int page, int pageSize) {
        try {
            return contractTradeService.getTriggerOrders(access, secret, SymbolEnum.get(symbol), page, pageSize);
        } catch (Exception e) {
            logger.error("[交割合约] symbol={}, page={}, pageSize={}, 获取当前计划委托订单异常, {}"
                    , symbol, page, pageSize, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

}


