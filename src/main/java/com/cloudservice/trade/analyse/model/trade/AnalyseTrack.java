package com.cloudservice.trade.analyse.model.trade;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.Jsonable;
import com.cloudservice.trade.analyse.context.AnalyseContext;
import com.cloudservice.trade.analyse.model.trade.Analyse;
import com.cloudservice.trade.analyse.service.trade.TradeService;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.huobi.enums.*;
import com.cloudservice.trade.huobi.model.contract.Order;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单追踪
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/17
 */
@Getter
@Setter
public class AnalyseTrack extends Track {
    private static final long serialVersionUID = 1952131344683637395L;

    /** 是否允许开仓 */
    private boolean isOpenAllow;
    /** 最后分析数据 */
    private Analyse lastAnalyse;
    /** 最后订单ID */
    private String lastOrderId;
    /** 最后开仓时间 */
    private Long lastOpenTime;
    /** 击穿次数 */
    private int breakdownNum;

    /** 最后撤销开仓方向 */
    private ContractDirectionEnum lastCancelDirection;
    /** 最后撤销时间 */
    private Long lastCancelTime;

    public AnalyseTrack() {}

    public AnalyseTrack(String access, String secret) {
        this.setAccess(access);
        this.setSecret(secret);
    }

    public Analyse getLastAnalyse() {
        if (lastAnalyse == null) {
            lastAnalyse = AnalyseContext.getAnalyse();
        }
        return lastAnalyse;
    }

    public ContractCodeEnum getContractCode() {
        if (this.getSymbol() == null) {
            return null;
        }
        return ContractCodeEnum.getUSD(this.getSymbol().getValue());
    }

    /**
     * @description 清除开仓缓存信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/18 13:58
     **/
    public void clearOpen() {
        this.lastAnalyse = null;
        this.lastOrderId = null;
        this.lastOpenTime = null;
        this.breakdownNum = 0;
    }

    /**
     * @description 清澈撤单缓存
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/22 15:52
     **/
    public void clearCancel() {
        this.lastCancelDirection = null;
        this.lastCancelTime = null;
    }

    /**
     * @description 近期撤单, 禁止反向开仓检查
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/22 16:04
     **/
    public boolean cancelDisable(ContractDirectionEnum direction) {
        if (this.lastCancelDirection == null) {
            return false;
        }
        return !direction.equals(this.lastCancelDirection)
                && ((System.currentTimeMillis() - this.lastCancelTime) < TradeService.CANCEL_DISABLE_TIME);
    }

    /**
     * @description 近期止盈平仓, 禁止同向追仓检查
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/23 13:46
     **/
    public boolean profitDisable(Order order, Analyse analyse) {
        if (order == null || analyse == null
                || !ContractOffsetEnum.CLOSE.getValue().equals(order.getOffset())
                || order.getOffsetProfitloss().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        return !order.getDirection().equals(analyse.getDirection().getValue())
                && ((System.currentTimeMillis() - order.getCreateDate()) < TradeService.CHASE_DISABLE_TIME);
    }

    /**
     * @description 近期止损平仓, 禁止逆向追仓检查
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/24 10:58
     * @param order, analyse
     **/
    public boolean lossDisable(Order order, Analyse analyse) {
        if (order == null || analyse == null
                || !ContractOffsetEnum.CLOSE.getValue().equals(order.getOffset())
                || order.getOffsetProfitloss().compareTo(BigDecimal.ZERO) >= 0) {
            return false;
        }
        return order.getDirection().equals(analyse.getDirection().getValue())
                && ((System.currentTimeMillis() - order.getCreateDate()) < TradeService.CHASE_DISABLE_TIME);
    }

    public boolean equals(Track track) {
        if (track == null) {
            return false;
        }
        return this.getAccess().equals(track.getAccess())
                && this.getSymbol().equals(track.getSymbol())
                && this.getHedgeType().equals(track.getHedgeType());
    }

    @Override
    public String toString() {
        return "Track{" +
                "access='" + this.getAccess() + '\'' +
                ", symbol=" + this.getSymbol() +
                ", stopTrade=" + this.isStopTrade() +
                '}';
    }

    @Override
    public JSONObject toJson() {
        return JSONObject.parseObject(JSONObject.toJSONString(this));
    }

    @Override
    public Track fromJson(String json) {
        return JSONObject.parseObject(json, this.getClass());
    }

}


