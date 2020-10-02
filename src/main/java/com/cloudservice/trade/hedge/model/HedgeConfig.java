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
import java.math.MathContext;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

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
    private static final BigDecimal USD_CNY_RATE = new BigDecimal("7");
    private static final int RADICAL_INDEX = 5;
    private static final int MEDIOCRE_INDEX = 6;
    private static final int STEADY_INDEX = 7;

    // 基础配置
    /** 策略类型 */
    private StrategyTypeEnum strategyType;
//    /** 稳健持仓金额(USD) */
//    private BigDecimal steadyAmountUSD;
//    /** 中庸持仓金额(USD) */
//    private BigDecimal mediocreAmountUSD;
//    /** 激进持仓金额(USD) */
//    private BigDecimal radicalAmountUSD;

    // 开仓配置
    /** 倍数 */
    private ContractLeverRateEnum leverRate;
    /** 基础张数 */
    private long basisVolume;
    /** 追仓倍率 */
    private Map<Integer, BigDecimal> chaseMultipleMap;
    /** 追仓张数 */
    private Map<Integer, Long> chaseVolumeMap;
    /** 持仓所需USD */
    private Map<Integer, BigDecimal> positionUSDMap;
    /** 持仓所需CNY */
    private Map<Integer, BigDecimal> positionCNYMap;

    // 收益配置
    /** 计划收益价格 */
    private BigDecimal incomePricePlan;
    /** 止盈倍率 */
    private BigDecimal profitMultiple;
    /** 追仓止盈倍率 */
    private Map<Integer, BigDecimal> chaseProfitMultipleMap;
    /** 追仓止盈差价 */
    private Map<Integer, BigDecimal> chasePositionDiffMap;
    /** 预估常平差价 */
    private Map<Integer, BigDecimal> liquidationDiffMap;

    // 时间配置
    /** 止盈追踪间隔时间, 毫秒 */
    private long profitTrackIntervalTime;
    /** 超时时间, 秒 */
    private int timeout;

    public HedgeConfig() {
        this.chaseMultipleMap = new TreeMap<>();
        this.chaseVolumeMap = new HashMap<>();
        this.positionUSDMap = new HashMap<>();
        this.positionCNYMap = new HashMap<>();
        this.chaseProfitMultipleMap = new HashMap<>();
        this.chasePositionDiffMap = new HashMap<>();
        this.liquidationDiffMap = new HashMap<>();
    }

    public HedgeConfig(StrategyTypeEnum strategyType) {
        this();
        this.strategyType = strategyType;
    }

    public boolean equals(HedgeConfig hedgeConfig) {
        if (hedgeConfig == null) {
            return false;
        }
        return this.leverRate.equals(hedgeConfig.getLeverRate())
                && this.basisVolume == hedgeConfig.getBasisVolume()
                && this.getChaseMultipleMap().get(1).equals(hedgeConfig.getChaseMultipleMap().get(1))
                && this.getChaseMultipleMap().get(2).equals(hedgeConfig.getChaseMultipleMap().get(2))
                && this.getChaseMultipleMap().get(3).equals(hedgeConfig.getChaseMultipleMap().get(3))
                && this.getChaseMultipleMap().get(4).equals(hedgeConfig.getChaseMultipleMap().get(4))
                && this.getChaseMultipleMap().get(5).equals(hedgeConfig.getChaseMultipleMap().get(5))
                && this.getChaseMultipleMap().get(6).equals(hedgeConfig.getChaseMultipleMap().get(6))
                && this.getChaseMultipleMap().get(7).equals(hedgeConfig.getChaseMultipleMap().get(7))
                && this.incomePricePlan.equals(hedgeConfig.getIncomePricePlan())
                && this.profitMultiple.equals(hedgeConfig.getProfitMultiple())
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

    /**
     * @description 计算追仓信息
     * <p>〈功能详细描述〉</p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/2 0:44
     */
    public void calculateChaseInfo() {
        // full 8 - 10
//        for (int i = 8; i <= 10; i++) {
//            if (chaseMultipleMap.get(i) == null) {
//                chaseMultipleMap.put(i, chaseMultipleMap.get(STEADY_INDEX));
//            }
//        }
        // calculate info
        long currVolume = basisVolume;
        for (Map.Entry<Integer, BigDecimal> multiple : chaseMultipleMap.entrySet()) {
            long chaseVolume = currVolume * (multiple.getValue().subtract(BigDecimal.ONE)).longValue();
            chaseVolumeMap.put(multiple.getKey(), chaseVolume);
            currVolume += chaseVolume;
            positionUSDMap.put(multiple.getKey(), BigDecimal.valueOf(currVolume * 10));
            positionCNYMap.put(multiple.getKey(), this.calculateCNYByUSD(positionUSDMap.get(multiple.getKey())));
            chaseProfitMultipleMap.put(multiple.getKey(), profitMultiple.pow(multiple.getKey()));
            chasePositionDiffMap.put(multiple.getKey(), incomePricePlan.multiply(chaseProfitMultipleMap.get(multiple.getKey())));
            BigDecimal lastMultiple = chaseMultipleMap.get(multiple.getKey() - 1);
            if (lastMultiple == null) {
                liquidationDiffMap.put(multiple.getKey(), chasePositionDiffMap.get(multiple.getKey()));
            } else {
                liquidationDiffMap.put(multiple.getKey(), chasePositionDiffMap.get(multiple.getKey()).divide(lastMultiple, new MathContext(2)));
            }
        }
    }

    /**
     * @description USD计算CNY金额
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/29 17:20
     **/
    private BigDecimal calculateCNYByUSD(BigDecimal usd) {
        if (usd == null) {
            return BigDecimal.ZERO;
        }
        return usd.multiply(USD_CNY_RATE);
    }

    public static void main(String[] args) {
        HedgeConfig cfg = new HedgeConfig(StrategyTypeEnum.FIXED_BASIS_20X);
        cfg.setLeverRate(ContractLeverRateEnum.LEVER_20);
        cfg.setBasisVolume(2);
        cfg.getChaseMultipleMap().put(1, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(2, BigDecimal.valueOf(3));
        cfg.getChaseMultipleMap().put(3, BigDecimal.valueOf(3));
        cfg.getChaseMultipleMap().put(4, BigDecimal.valueOf(3));
        cfg.getChaseMultipleMap().put(5, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(6, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(7, BigDecimal.valueOf(2));
        cfg.setIncomePricePlan(new BigDecimal("0.6"));
        cfg.setProfitMultiple(BigDecimal.valueOf(2));
        cfg.setProfitTrackIntervalTime(1000);
        cfg.setTimeout(30);
        cfg.calculateChaseInfo();

        System.out.println("getChaseMultipleMap: " + cfg.getChaseMultipleMap());
        System.out.println("getChaseVolumeMap: " + cfg.getChaseVolumeMap());
        System.out.println("getPositionUSDMap: " + cfg.getPositionUSDMap());
        System.out.println("getPositionCNYMap: " + cfg.getPositionCNYMap());
        System.out.println("getChaseProfitMultipleMap: " + cfg.getChaseProfitMultipleMap());
        System.out.println("getChasePositionDiffMap: " + cfg.getChasePositionDiffMap());
        System.out.println("getLiquidationDiffMap: " + cfg.getLiquidationDiffMap());
    }

}


