package com.cloudservice.trade.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.BaseController;
import com.cloudservice.base.Result;
import com.cloudservice.trade.huobi.enums.*;
import com.cloudservice.trade.huobi.model.contract.Account;
import com.cloudservice.trade.huobi.model.contract.Order;
import com.cloudservice.trade.huobi.model.contract.Position;
import com.cloudservice.trade.huobi.model.contract.TriggerOrder;
import com.cloudservice.trade.huobi.service.swap.SwapAccountService;
import com.cloudservice.trade.huobi.service.swap.SwapTradeService;
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
 * 控制器：永续合约
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/9
 */
@RestController
@RequestMapping("/swap")
public class SwapController extends BaseController {

    @Autowired
    private SwapAccountService swapAccountService;
    @Autowired
    private SwapTradeService swapTradeService;

    @PostMapping("/account/{contractCode}")
    @Description("获取账户信息")
    public Account account(String access, String secret, @PathVariable String contractCode) {
        try {
            return swapAccountService.getAccountInfo(access, secret, ContractCodeEnum.get(contractCode));
        } catch (Exception e) {
            logger.error("[永续合约] contractCode={}, 获取账户信息异常, {}", contractCode, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/position/{contractCode}")
    @Description("获取用户持仓信息")
    public List<Position> position(String access, String secret, @PathVariable String contractCode) {
        try {
            return swapAccountService.getPositionList(access, secret, ContractCodeEnum.get(contractCode));
        } catch (Exception e) {
            logger.error("[永续合约] contractCode={}, 获取用户持仓信息异常, {}", contractCode, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/fee/{contractCode}")
    @Description("查询用户当前的手续费费率")
    public JSONObject fee(String access, String secret, @PathVariable String contractCode) {
        try {
            return swapAccountService.getFee(access, secret, ContractCodeEnum.get(contractCode));
        } catch (Exception e) {
            logger.error("[永续合约] contractCode={}, 查询用户当前的手续费费率异常, {}", contractCode, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/order/{contractCode}")
    @Description("下单")
    public Result order(String access, String secret, @PathVariable String contractCode, BigDecimal price, long volume
            , String direction, String offset, String leverRate, String orderPriceType) {
        try {
            return swapTradeService.order(access, secret, ContractCodeEnum.get(contractCode), price, volume
                    , ContractDirectionEnum.get(direction), ContractOffsetEnum.get(offset), ContractLeverRateEnum.get(leverRate)
                    , ContractOrderPriceTypeEnum.get(orderPriceType));
        } catch (Exception e) {
            logger.error("[永续合约] contractCode={}, price={}, volume={}, direction={}, offset={}, leverRate={}, orderPriceType={}, 下单异常, {}"
                    , contractCode, price, volume, direction, offset, leverRate, orderPriceType, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/cancel/{contractCode}")
    @Description("撤销订单")
    public Result cancel(String access, String secret, @PathVariable String contractCode, String orderId) {
        try {
            if (StringUtils.isBlank(orderId)) {
                return swapTradeService.cancelAll(access, secret, ContractCodeEnum.get(contractCode));
            } else {
                return swapTradeService.cancel(access, secret, ContractCodeEnum.get(contractCode), orderId);
            }
        } catch (Exception e) {
            logger.error("[永续合约] contractCode={}, orderId={}, 撤销订单异常, {}", contractCode, orderId, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/orderInfo/{contractCode}")
    @Description("获取指定订单信息")
    public Order orderInfo(String access, String secret, @PathVariable String contractCode, String orderId) {
        try {
            return swapTradeService.getOrderInfo(access, secret, ContractCodeEnum.get(contractCode), orderId);
        } catch (Exception e) {
            logger.error("[永续合约] contractCode={}, orderId={}, 获取当前开仓订单信息异常, {}"
                    , contractCode, orderId, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/history/{contractCode}/{tradeType}")
    @Description("获取历史成交记录")
    public List<Order> orderHistory(String access, String secret, @PathVariable String contractCode, @PathVariable String tradeType
            , String beginTime, String endTime) {
        try {
            if (StringUtils.isBlank(beginTime)) {
                return swapTradeService.getOrderHistory(access, secret, ContractCodeEnum.get(contractCode), ContractTradeTypeHistoryEnum.get(tradeType));
            } else {
                return swapTradeService.getOrderHistory(access, secret, ContractCodeEnum.get(contractCode), ContractTradeTypeHistoryEnum.get(tradeType)
                        , 1, 50
                        , DateUtil.parse(beginTime, DateUtil.DATE_LONG)
                        , DateUtil.parse(endTime, DateUtil.DATE_LONG));
            }
        } catch (Exception e) {
            logger.error("[永续合约] contractCode={}, tradeType={}, 获取当前开仓订单信息异常, {}"
                    , contractCode, tradeType, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/openOrder/{contractCode}")
    @Description("获取未成交委托")
    public List<Order> openOrder(String access, String secret, @PathVariable String contractCode
            , int page, int pageSize) {
        try {
            return swapTradeService.getOpenOrders(access, secret, ContractCodeEnum.get(contractCode), page, pageSize);
        } catch (Exception e) {
            logger.error("[永续合约] contractCode={}, page={}, pageSize={}, 获取未成交委托异常, {}"
                    , contractCode, page, pageSize, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/lightningClose/{contractCode}")
    @Description("闪电平仓")
    public Result lightningClose(String access, String secret, @PathVariable String contractCode
            , long volume, String direction) {
        try {
            return swapTradeService.lightningClose(access, secret, ContractCodeEnum.get(contractCode)
                    , volume, ContractDirectionEnum.get(direction));
        } catch (Exception e) {
            logger.error("[永续合约] contractCode={}, volume={}, direction={}, 闪电平仓异常, {}"
                    , contractCode, volume, direction, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/triggerOrder/{contractCode}")
    @Description("计划委托")
    public Result triggerOrder(String access, String secret, @PathVariable String contractCode
            , String triggerType, BigDecimal triggerPrice, BigDecimal orderPrice
            , String orderPriceType, long volume
            , String direction, String offset, String leverRate) {
        try {
            return swapTradeService.triggerOrder(access, secret, ContractCodeEnum.get(contractCode)
                    , ContractTriggerTypeEnum.get(triggerType), triggerPrice, orderPrice
                    , ContractOrderPriceTypeEnum.get(orderPriceType), volume
                    , ContractDirectionEnum.get(direction), ContractOffsetEnum.get(offset), ContractLeverRateEnum.get(leverRate));
        } catch (Exception e) {
            logger.error("[永续合约] contractCode={}, triggerType={}, triggerPrice={}, orderPrice={}, orderPriceType={}, volume={}" +
                            ", direction={}, offset={}, leverRate={}, 计划委托异常, {}"
                    , contractCode, triggerType, triggerPrice, orderPrice, orderPriceType, volume
                    , direction, offset, leverRate, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/triggerCancel/{contractCode}")
    @Description("计划委托撤单")
    public Result triggerCancel(String access, String secret, @PathVariable String contractCode, String orderId) {
        try {
            if (StringUtils.isBlank(orderId)) {
                return swapTradeService.triggerCancelAll(access, secret, ContractCodeEnum.get(contractCode));
            } else {
                return swapTradeService.triggerCancel(access, secret, ContractCodeEnum.get(contractCode), orderId);
            }
        } catch (Exception e) {
            logger.error("[永续合约] contractCode={}, orderId={}, 计划委托撤单异常, {}"
                    , contractCode, orderId, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/triggerOrderInfo/{contractCode}")
    @Description("获取当前计划委托订单")
    public List<TriggerOrder> triggerOrderInfo(String access, String secret, @PathVariable String contractCode, int page, int pageSize) {
        try {
            return swapTradeService.getTriggerOrders(access, secret, ContractCodeEnum.get(contractCode), page, pageSize);
        } catch (Exception e) {
            logger.error("[永续合约] contractCode={}, page={}, pageSize={}, 获取当前计划委托订单异常, {}"
                    , contractCode, page, pageSize, e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

}


