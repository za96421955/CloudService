package com.cloudservice.trade.hedge.model;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.Jsonable;
import com.cloudservice.plat.enums.StrategyTypeEnum;
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
public class HedgeConfig implements Serializable, Jsonable<HedgeConfig> {
    private static final long serialVersionUID = -4336975265204074693L;

    // 基础配置
    /** 策略类型 */
    private StrategyTypeEnum strategyType;
    /** 持仓金额(USD) */
    private BigDecimal positionAmountUSD;

    // 开仓配置
    /** 倍数 */
    private ContractLeverRateEnum leverRate;
    /** 基础张数 */
    private long basisVolume;
    /** 前区间(1-2)倍数 */
    private BigDecimal beforeIntervalMultiple;
    /** 中区间(3-4)倍数 */
    private BigDecimal midIntervalMultiple;
    /** 后区间(5-7)倍数 */
    private BigDecimal afterIntervalMultiple;

    // 收益配置
    /** 计划收益价格 */
    private BigDecimal incomePricePlan;
    /** 止盈基础倍率 */
    private BigDecimal profitBasisMultiple;

    // 时间配置
    /** 止盈追踪间隔时间, 毫秒 */
    private long profitTrackIntervalTime;
    /** 超时时间, 秒 */
    private int timeout;

    public HedgeConfig(StrategyTypeEnum strategyType) {
        this.strategyType = strategyType;
    }

    /**
     * @description 获取持仓金额(RMB)
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/29 17:20
     **/
    private BigDecimal getPositionAmountRMB() {
        return this.getPositionAmountUSD().multiply(new BigDecimal("6.9"));
    }

    public boolean equals(HedgeConfig hedgeConfig) {
        if (hedgeConfig == null) {
            return false;
        }
        return this.positionAmountUSD.equals(hedgeConfig.getPositionAmountUSD())
                && this.leverRate.equals(hedgeConfig.getLeverRate())
                && this.basisVolume == hedgeConfig.getBasisVolume()
                && this.beforeIntervalMultiple.equals(hedgeConfig.getBeforeIntervalMultiple())
                && this.midIntervalMultiple.equals(hedgeConfig.getMidIntervalMultiple())
                && this.afterIntervalMultiple.equals(hedgeConfig.getAfterIntervalMultiple())
                && this.incomePricePlan.equals(hedgeConfig.getIncomePricePlan())
                && this.profitBasisMultiple.equals(hedgeConfig.getProfitBasisMultiple())
                && this.profitTrackIntervalTime == hedgeConfig.getProfitTrackIntervalTime()
                && this.timeout == hedgeConfig.getTimeout();
    }

    @Override
    public JSONObject toJson() {
        return JSONObject.parseObject(JSONObject.toJSONString(this));
    }

    @Override
    public HedgeConfig fromJson(String json) {
        return JSONObject.parseObject(json, this.getClass());
    }

}


