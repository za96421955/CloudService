package com.cloudservice.trade.hedge.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.BaseService;
import com.cloudservice.base.Result;
import com.cloudservice.plat.context.PlatContext;
import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.hedge.service.HedgeService;
import com.cloudservice.trade.huobi.enums.ContractDirectionEnum;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import com.cloudservice.trade.huobi.enums.ContractTypeEnum;
import com.cloudservice.trade.huobi.enums.SymbolEnum;
import com.cloudservice.trade.huobi.model.contract.Account;
import com.cloudservice.trade.huobi.model.contract.Order;
import com.cloudservice.trade.huobi.model.contract.Position;
import com.cloudservice.trade.huobi.model.spot.Kline;
import com.cloudservice.trade.huobi.service.spot.SpotMarketService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * 对冲抽象服务
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/24
 */
public abstract class AbstractHedgeService extends BaseService implements HedgeService {

    @Autowired
    protected SpotMarketService spotMarketService;

    @Override
    public void setStrategy(Track track) {
        List<HedgeConfig> cfgList = PlatContext.getHedgeStrategyList(track.getStrategyType());

        // 固定策略
        if (cfgList.size() == 1) {
            HedgeConfig fixedCfg = cfgList.get(0);
            if (track.getHedgeConfig() == null || !track.getHedgeConfig().getStrategyType().equals(fixedCfg.getStrategyType())) {
                track.setHedgeConfig(fixedCfg);
            }
            return;
        }

        // 复利策略
        // 获取账户、K线信息
        Account account = this.getAccount(track);
        Kline kline = this.getKlineCurr(track.getSymbol(), ContractTypeEnum.THIS_WEEK);
        if (account == null || kline == null) {
            track.setHedgeConfig(PlatContext.getHedgeStrategyList(StrategyTypeEnum.FIXED_BASIS).get(0));
            return;
        }
        // TODO 计算参考价格CNY
        BigDecimal price = account.getMarginBalance().multiply(kline.getClose()).multiply(HedgeConfig.USD_CNY_RATE);
        track.setHedgeConfig(HedgeConfig.getFitConfigByPrice(price, cfgList, track.getRiskType()));
    }

    /**
     * @description 获取允许交易倍率
     * <p>〈功能详细描述〉</p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/1 23:26
     * @param   track, buy, sell
     */
    private ContractLeverRateEnum getLeverRate(Track track, Position buy, Position sell) {
        if (buy != null) {
            return ContractLeverRateEnum.get(buy.getLeverRate() + "");
        }
        if (sell != null) {
            return ContractLeverRateEnum.get(sell.getLeverRate() + "");
        }
        return track.getHedgeConfig().getLeverRate();
    }

    @Override
    public Result positionCheck(Track track) {
        // 持仓检查
        List<Position> positionList = this.getPositionList(track);
        Position buy = this.getPosition(positionList, ContractDirectionEnum.BUY);
        Position sell = this.getPosition(positionList, ContractDirectionEnum.SELL);
        logger.info("[{}] track={}, buy={}, sell={}, 持仓检查", LOG_MARK, track, buy, sell);

        // <=> 配置倍率替换
        ContractLeverRateEnum cfgLeverRate = track.getHedgeConfig().getLeverRate();
        track.getHedgeConfig().setLeverRate(this.getLeverRate(track, buy, sell));

        // 开多下单
        if (!track.isStopTrade() && buy == null) {
            Result result = this.open(track, ContractDirectionEnum.BUY, track.getHedgeConfig().getBasisVolume());
            logger.info("[{}] track={}, result={}, 开多下单", LOG_MARK, track, result);
        }
        // 开空下单
        if (!track.isStopTrade() && sell == null) {
            Result result = this.open(track, ContractDirectionEnum.SELL, track.getHedgeConfig().getBasisVolume());
            logger.info("[{}] track={}, result={}, 开空下单", LOG_MARK, track, result);
        }
        if (!track.isStopTrade() && (buy == null || sell == null)) {
            return Result.buildFail("交易开启 & (开多 || 开空)无持仓, 持仓检查不通过");
        }

        // <=> 配置倍率还原
        track.getHedgeConfig().setLeverRate(cfgLeverRate);
        // 0: 多, 1: 空
        return Result.buildSuccess(buy, sell);
    }

    /**
     * @description 获取指定持仓信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/26 14:43
     * @param positionList, direction
     **/
    private Position getPosition(List<Position> positionList, ContractDirectionEnum direction) {
        for (Position position : positionList) {
            if (position == null) {
                continue;
            }
            if (direction.getValue().equals(position.getDirection())) {
                return position;
            }
        }
        return null;
    }

    @Override
    public void closeCheck(Track track, Position buy, Position sell) {
        if (track == null) {
            return;
        }

        // <=> 配置倍率替换
        ContractLeverRateEnum cfgLeverRate = track.getHedgeConfig().getLeverRate();
        track.getHedgeConfig().setLeverRate(this.getLeverRate(track, buy, sell));

        /**
         * 单项止损开仓, 存在极高爆仓风险
         * 因为basis张止损而承担如此风险, 非明智之举
         */
        // 1.1, 开多止盈平仓
        Result result = this.profitClose(track, buy
                , this.calculateIncomeMultiple(track, sell)
                , this.calculateCloseLossVolume(track, sell));
        logger.debug("[{}] track={}, result={}, Buy - 开多止盈平仓", LOG_MARK, track, result);
        // 1.2, 停止交易, 开空止损平仓
        if (buy == null) {
            result = this.stopTradeLittleLossClose(track, sell);
            logger.debug("[{}] track={}, result={}, Buy - 停止交易, 开空止损平仓", LOG_MARK, track, result);
        }

        // 2.1, 开空止盈平仓
        result = this.profitClose(track, sell
                , this.calculateIncomeMultiple(track, buy)
                , this.calculateCloseLossVolume(track, buy));
        logger.debug("[{}] track={}, result={}, Sell - 开空止盈平仓", LOG_MARK, track, result);
        // 2.2, 停止交易, 开多止损平仓
        if (sell == null) {
            result = this.stopTradeLittleLossClose(track, buy);
            logger.debug("[{}] track={}, result={}, Sell - 停止交易, 开多止损平仓", LOG_MARK, track, result);
        }

        // <=> 配置倍率还原
        track.getHedgeConfig().setLeverRate(cfgLeverRate);
    }

    /**
     * @description 计算止盈收益倍数
     * <p>
     *     1，最小1倍
     * </p>
     *
     * @author 陈晨
     * @date 2020/9/26 15:00
     * @param track, reversePosition
     **/
    private BigDecimal calculateIncomeMultiple(Track track, Position reversePosition) {
        if (reversePosition == null) {
            return BigDecimal.ONE;
        }
        BigDecimal incomeMultiple = track.getProfitMultiple(reversePosition.getVolume().longValue());
        if (incomeMultiple.compareTo(BigDecimal.ONE) < 0) {
            incomeMultiple = BigDecimal.ONE;
        }
        return incomeMultiple;
    }

    /**
     * @description 计算止损追仓张数
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/26 15:03
     * @param position
     **/
    private long calculateCloseLossVolume(Track track, Position position) {
        if (position == null) {
            return 0;
        }
        // 获取追仓倍率
        BigDecimal multiple = track.getChaseMultiple(position.getVolume().longValue());
        // 当前持仓 * 追仓倍率 - 当前持仓 = 止损追仓数
        return position.getVolume().multiply(multiple).subtract(position.getVolume()).longValue();
    }

    /**
     * @description 止盈平仓
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/26 15:10
     * @param track             追踪信息
     * @param position          持仓信息
     * @param incomeMultiple    止盈收益倍数
     * @param lossVolume        止损追仓张数
     **/
    private Result profitClose(Track track, Position position, BigDecimal incomeMultiple, long lossVolume) {
        if (track == null || position == null) {
            return Result.buildFail("args empty");
        }
        // 判断是否可以平仓
        if (!this.isClose(track, position, incomeMultiple, BigDecimal.ZERO)) {
            return Result.buildFail("not allowed close");
        }
        // 平仓下单
        Result result = this.closeOrder(track, position, lossVolume);
        if (result.success()) {
            return result;
        }
        // 平仓下单失败, 则全部撤单, 重新下单
        result = this.cancel(track);
        logger.info("[{}] track={}, position={}, result={}, 平仓下单失败, 则全部撤单, 重新下单"
                , LOG_MARK, track, position, result);
        return this.closeOrder(track, position, lossVolume);
    }

    /**
     * @description 判断是否可以平仓
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/24 15:45
     **/
    private boolean isClose(Track track, Position position, BigDecimal incomeMultiple, BigDecimal lastIncomePrice) {
        // 获取现价信息
        Kline kline = this.getKlineCurr(track.getSymbol(), ContractTypeEnum.THIS_WEEK);
        if (kline == null) {
            return false;
        }
        // 获取当前收益价格
        BigDecimal incomePrice = this.getIncomePrice(position, kline.getClose());
        // 若未达到计划收益, 则不平仓
        if (incomePrice.compareTo(track.getHedgeConfig().getIncomePricePlan().multiply(incomeMultiple)) < 0) {
            return false;
        }
        logger.info("[{}] direction={}, price={}, curr={}, income={}, 达到计划收益条件, 平仓准备"
                , LOG_MARK, position.getDirection(), position.getCostHold(), kline.getClose(), incomePrice);

        // 若已收益, 则持续追踪收益价格, 直至收益出现回落, 则平仓
        if (lastIncomePrice.compareTo(incomePrice) > 0) {
            return true;
        }
        // Nms检查一次
        this.sleep(track.getHedgeConfig().getProfitTrackIntervalTime());
        return this.isClose(track, position, incomeMultiple, incomePrice);
    }

    /**
     * @description 获取收益价格
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/24 15:35
     **/
    private BigDecimal getIncomePrice(Position position, BigDecimal currPrice) {
        if (ContractDirectionEnum.BUY.getValue().equals(position.getDirection())) {
            // 开多收益: 当前价 > 持仓价 + 收益价
            return currPrice.subtract(position.getCostHold());
        } else {
            // 开空收益: 当前价 < 持仓价 - 收益价
            return position.getCostHold().subtract(currPrice);
        }
    }

    /**
     * @description 平仓下单
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/24 16:46
     **/
    private Result closeOrder(Track track, Position position, long lossVolume) {
        // 1, 止盈平仓（所有张）
        Result result = this.close(track, position);
        logger.info("[{}] track={}, direction={}, price={}, result={}, 止盈平仓（所有, {}张）"
                , LOG_MARK, track, position.getDirection(), position.getCostHold()
                , result, position.getVolume());
        if (!result.success()) {
            return result;
        }
        // 订单完成检查
        if (!this.orderCompleteCheck(track, result, 0)) {
            logger.info("[{}] track={}, direction={}, 止盈平仓检查, 超时", LOG_MARK, track, position.getDirection());
            return Result.buildFail("止盈平仓检查, 超时");
        }

        // 2, 停止交易, 则停止追仓
        if (this.isStopTrade(track, position)) {
            return Result.buildSuccess();
        }

        // 3, 同向开仓（basis张）
        result = this.open(track, ContractDirectionEnum.get(position.getDirection()), track.getHedgeConfig().getBasisVolume());
        logger.info("[{}] track={}, direction={}, result={}, 同向开仓（{}}张）"
                , LOG_MARK, track, position.getDirection(), result, track.getHedgeConfig().getBasisVolume());
        if (!result.success()) {
            return result;
        }
        // 订单完成检查
        if (!this.orderCompleteCheck(track, result, 0)) {
            logger.info("[{}] track={}, direction={}, 同向开仓检查, 超时", LOG_MARK, track, position.getDirection());
            return Result.buildFail("同向开仓检查, 超时");
        }

        // 4, 逆向止损加仓（lossVolume张）
        if (lossVolume > 0) {
            result = this.open(track, ContractDirectionEnum.get(position.getDirection()).getNegate(), lossVolume);
            logger.info("[{}] track={}, direction={}, result={}, 逆向止损加仓（{}张）"
                    , LOG_MARK, track, ContractDirectionEnum.get(position.getDirection()).getNegate()
                    , result, lossVolume);
        }
        return Result.buildSuccess();
    }

    /**
     * @description 订单完成检查
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/24 20:34
     **/
    private boolean orderCompleteCheck(Track track, Result result, int count) {
        if (count > track.getHedgeConfig().getTimeout()) {
            return false;
        }
        String orderId = JSONObject.parseObject(result.getData().toString()).getLong("order_id") + "";
        Order order = this.getOrderInfo(track, orderId);
        if (order != null && order.getStatus() == 6) {
            return true;
        }
        this.sleep(1000);
        return this.orderCompleteCheck(track, result, ++count);
    }

    /**
     * @description 是否停止交易
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/26 13:30
     * @param track, position
     **/
    private boolean isStopTrade(Track track, Position position) {
        // 停止交易 && 平仓张数 > basis, 则不再向下追仓
        return track.isStopTrade() && position.getVolume().compareTo(BigDecimal.valueOf(track.getHedgeConfig().getBasisVolume())) > 0;
    }

    /**
     * @description 停止交易小仓止损平仓
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/28 17:58
     * @param track, position
     **/
    private Result stopTradeLittleLossClose(Track track, Position position) {
        if (position == null
                || !track.isStopTrade()
                || position.getVolume().compareTo(BigDecimal.valueOf(track.getHedgeConfig().getBasisVolume())) > 0) {
            return Result.buildSuccess();
        }
        // 1, 止损平仓（所有张）
        Result result = this.close(track, position);
        logger.info("[{}] track={}, direction={}, price={}, result={}, 止损平仓（所有, {}张）"
                , LOG_MARK, track, position.getDirection(), position.getCostHold()
                , result, position.getVolume());
        if (!result.success()) {
            return result;
        }
        // 订单完成检查
        if (!this.orderCompleteCheck(track, result, 0)) {
            // 平仓下单失败, 则全部撤单, 重新下单
            result = this.cancel(track);
            logger.info("[{}] track={}, position={}, result={}, 止损平仓检查超时, 则全部撤单, 重新下单"
                    , LOG_MARK, track, position, result);
            return this.stopTradeLittleLossClose(track, position);
        }
        return Result.buildSuccess();
    }

    /**
     * @description 获取账户信息
     * <p>〈功能详细描述〉</p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/5 21:40
     * @param   track
     */
    protected abstract Account getAccount(Track track);

    /**
     * @description 获取持仓信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/26 15:16
     * @param track
     **/
    protected abstract List<Position> getPositionList(Track track);

    /**
     * @description 获取当前K线信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/28 17:46
     * @param symbol, contractType
     **/
    protected abstract Kline getKlineCurr(SymbolEnum symbol, ContractTypeEnum contractType);

    /**
     * @description 开仓
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/24 15:55
     * @param track, direction, volume
     **/
    protected abstract Result open(Track track, ContractDirectionEnum direction, long volume);

    /**
     * @description 平仓
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/24 15:55
     * @param track, direction
     **/
    protected abstract Result close(Track track, Position position);

    /**
     * @description 撤单
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/26 15:07
     * @param track
     **/
    protected abstract Result cancel(Track track);

    /**
     * @description 获取指定订单信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/26 13:29
     * @param track, orderId
     **/
    protected abstract Order getOrderInfo(Track track, String orderId);

}


