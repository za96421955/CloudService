package com.cloudservice.trade.hedge.model;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.Jsonable;
import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
    /** 稳健持仓金额(USD) */
    private BigDecimal steadyAmountUSD;
    /** 中庸持仓金额(USD) */
    private BigDecimal mediocreAmountUSD;
    /** 激进持仓金额(USD) */
    private BigDecimal radicalAmountUSD;

    // 开仓配置
    /** 倍数 */
    private ContractLeverRateEnum leverRate;
    /** 基础张数 */
    private long basisVolume;
    /**
     * 区间倍率
     * 前区间(1-2)倍数
     * 中区间(3-4)倍数
     * 后区间(5-7)倍数
     */
    private Map<Integer, BigDecimal> intervalMultipleMap;

    // 收益配置
    /** 计划收益价格 */
    private BigDecimal incomePricePlan;
    /** 追仓止盈倍率 */
    private BigDecimal chaseProfitMultiple;

    // 时间配置
    /** 止盈追踪间隔时间, 毫秒 */
    private long profitTrackIntervalTime;
    /** 超时时间, 秒 */
    private int timeout;

    public HedgeConfig() {
        this.intervalMultipleMap = new HashMap<>();
    }

    public HedgeConfig(StrategyTypeEnum strategyType) {
        this();
        this.strategyType = strategyType;
    }

    /**
     * @description 获取RMB金额
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/29 17:20
     **/
    private BigDecimal getAmountRMB(BigDecimal usd) {
        if (usd == null) {
            return BigDecimal.ZERO;
        }
        return usd.multiply(new BigDecimal("6.9"));
    }

    public boolean equals(HedgeConfig hedgeConfig) {
        if (hedgeConfig == null) {
            return false;
        }
        return this.steadyAmountUSD.equals(hedgeConfig.getSteadyAmountUSD())
                && this.mediocreAmountUSD.equals(hedgeConfig.getMediocreAmountUSD())
                && this.radicalAmountUSD.equals(hedgeConfig.getRadicalAmountUSD())
                && this.leverRate.equals(hedgeConfig.getLeverRate())
                && this.basisVolume == hedgeConfig.getBasisVolume()
                && this.getIntervalMultipleMap().get(1).equals(hedgeConfig.getIntervalMultipleMap().get(1))
                && this.getIntervalMultipleMap().get(2).equals(hedgeConfig.getIntervalMultipleMap().get(2))
                && this.getIntervalMultipleMap().get(3).equals(hedgeConfig.getIntervalMultipleMap().get(3))
                && this.getIntervalMultipleMap().get(4).equals(hedgeConfig.getIntervalMultipleMap().get(4))
                && this.getIntervalMultipleMap().get(5).equals(hedgeConfig.getIntervalMultipleMap().get(5))
                && this.getIntervalMultipleMap().get(6).equals(hedgeConfig.getIntervalMultipleMap().get(6))
                && this.getIntervalMultipleMap().get(7).equals(hedgeConfig.getIntervalMultipleMap().get(7))
                && this.incomePricePlan.equals(hedgeConfig.getIncomePricePlan())
                && this.chaseProfitMultiple.equals(hedgeConfig.getChaseProfitMultiple())
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


