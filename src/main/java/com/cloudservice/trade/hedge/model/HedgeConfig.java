package com.cloudservice.trade.hedge.model;

import com.cloudservice.trade.hedge.service.HedgeServiceFactory;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 对冲配置
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/17
 */
@Getter
@Setter
@ToString
public class HedgeConfig implements Serializable {
    private static final long serialVersionUID = -4336975265204074693L;

    /** 对冲合约类型 */
    private String hedgeType;
    /** 倍数 */
    private ContractLeverRateEnum leverRate;
    /** 基础张数 */
    private long basisVolume;
    /** 计划收益价格 */
    private BigDecimal incomePricePlan;
    /** 止盈基础倍率 */
    private BigDecimal profitBasisMultiple = BigDecimal.ONE;
    /** 止盈追踪间隔时间, 毫秒 */
    private long profitTrackIntervalTime = 1200;
    /** 超时时间, 秒 */
    private int timeout = 30;
    /** 停止交易 */
    private boolean stopTrade = false;

    /**
     * @description 默认配置初始化
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/26 17:02
     **/
    public static HedgeConfig initDefault() {
        return new HedgeConfig(HedgeServiceFactory.CONTRACT, ContractLeverRateEnum.LEVER_10, 1, BigDecimal.ONE);
    }

    public HedgeConfig(String hedgeType, ContractLeverRateEnum leverRate, long basisVolume, BigDecimal incomePricePlan) {
        this.hedgeType = hedgeType;
        this.leverRate = leverRate;
        this.basisVolume = basisVolume;
        this.incomePricePlan = incomePricePlan;
    }

    public HedgeConfig setHedgeType(String hedgeType) {
        this.hedgeType = hedgeType;
        return this;
    }

    public HedgeConfig setLeverRate(ContractLeverRateEnum leverRate) {
        this.leverRate = leverRate;
        return this;
    }

    public HedgeConfig setBasisVolume(long basisVolume) {
        this.basisVolume = basisVolume;
        return this;
    }

    public HedgeConfig setIncomePricePlan(BigDecimal incomePricePlan) {
        this.incomePricePlan = incomePricePlan;
        return this;
    }

    public HedgeConfig setProfitTrackIntervalTime(long profitTrackIntervalTime) {
        this.profitTrackIntervalTime = profitTrackIntervalTime;
        return this;
    }

    public HedgeConfig setProfitBasisMultiple(BigDecimal profitBasisMultiple) {
        this.profitBasisMultiple = profitBasisMultiple;
        return this;
    }

    public HedgeConfig setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public HedgeConfig setStopTrade(boolean stopTrade) {
        this.stopTrade = stopTrade;
        return this;
    }

}


