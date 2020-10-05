package com.cloudservice.trade.hedge.model;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.Jsonable;
import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
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
public class HedgeConfig implements Serializable, Jsonable<HedgeConfig> {
    private static final long serialVersionUID = -4336975265204074693L;
    public static final BigDecimal USD_CNY_RATE = new BigDecimal("6.9");

    private static final int INDEX_RADICAL = 4;
    private static final int INDEX_NORMAL = 5;
    private static final int INDEX_STEADY = 6;
    private static final int INDEX_STEADY_PLUS = 7;

    public static final int TYPE_RADICAL = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_STEADY = 2;
    public static final int TYPE_STEADY_PLUS = 3;

    // 基础配置
    /** 策略类型 */
    private StrategyTypeEnum strategyType;

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
    /** 75X持仓所需CNY */
    private Map<Integer, BigDecimal> position75XCNYMap;
    /** 建议资产 */
    private Map<Integer, BigDecimal> recommendAssetsMap;

    // 收益配置
    /** 计划收益价格 */
    private BigDecimal incomePricePlan;
    /** 止盈倍率 */
    private BigDecimal profitMultiple;
    /** 追仓止盈倍率 */
    private Map<Integer, BigDecimal> chaseProfitMultipleMap;
    /** 追仓止盈差价 */
    private Map<Integer, BigDecimal> chasePositionDiffMap;

    // 时间配置
    /** 止盈追踪间隔时间, 毫秒 */
    private long profitTrackIntervalTime;
    /** 超时时间, 秒 */
    private int timeout;

    public HedgeConfig() {
        this.chaseMultipleMap = new TreeMap<>();
        this.chaseVolumeMap = new TreeMap<>();
        this.positionUSDMap = new TreeMap<>();
        this.position75XCNYMap = new TreeMap<>();
        this.recommendAssetsMap = new TreeMap<>();
        this.chaseProfitMultipleMap = new TreeMap<>();
        this.chasePositionDiffMap = new TreeMap<>();
    }

    public HedgeConfig(StrategyTypeEnum strategyType) {
        this();
        this.strategyType = strategyType;
    }

    public boolean equals(HedgeConfig hedgeConfig) {
        if (hedgeConfig == null) {
            return false;
        }
        return this.strategyType.equals(hedgeConfig.getStrategyType())
                && this.leverRate.equals(hedgeConfig.getLeverRate())
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(strategyType.getValue());
        sb.append("@").append(incomePricePlan).append("-").append(profitMultiple);
        sb.append("@").append(basisVolume).append("-");
        for (BigDecimal multiple : chaseMultipleMap.values()) {
            sb.append(multiple);
        }
        return sb.toString();
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
        long currVolume = basisVolume;
        for (Map.Entry<Integer, BigDecimal> multiple : chaseMultipleMap.entrySet()) {
            long chaseVolume = currVolume * (multiple.getValue().subtract(BigDecimal.ONE)).longValue();
            chaseVolumeMap.put(multiple.getKey(), chaseVolume);
            currVolume += chaseVolume;
            positionUSDMap.put(multiple.getKey(), BigDecimal.valueOf(currVolume * 10));
            position75XCNYMap.put(multiple.getKey(),
                    this.calculateCNYByUSD(positionUSDMap.get(multiple.getKey()))
                            .divide(BigDecimal.valueOf(75), new MathContext(2)));
            recommendAssetsMap.put(multiple.getKey(), position75XCNYMap.get(multiple.getKey()).multiply(BigDecimal.valueOf(2)));
            chaseProfitMultipleMap.put(multiple.getKey(), profitMultiple.pow(multiple.getKey() - 1));
            chasePositionDiffMap.put(multiple.getKey(), incomePricePlan.multiply(chaseProfitMultipleMap.get(multiple.getKey())));
        }
    }

    /**
     * @description 获取追仓次数
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/3 19:20
     * @param   volume
     */
    public int getChaseIndex(long volume) {
        boolean isGe = false;
        int lastIndex = 1;
        for (Map.Entry<Integer, Long> entry : chaseVolumeMap.entrySet()) {
            lastIndex = entry.getKey();
            long currVolume = entry.getValue() / (chaseMultipleMap.get(entry.getKey()).intValue() - 1);
            if (volume == currVolume) {
                return entry.getKey();
            }
            if (volume > currVolume) {
                isGe = true;
            }
            if (isGe && volume < currVolume) {
                return entry.getKey();
            }
        }
        return lastIndex;
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

    /**
     * @description 获取激进价格
     * <p>〈功能详细描述〉</p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/5 16:25
     */
    public BigDecimal getRadicalPrice() {
        return recommendAssetsMap.get(INDEX_RADICAL);
    }

    /**
     * @description 获取常规价格
     * <p>〈功能详细描述〉</p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/5 16:25
     */
    public BigDecimal getNormalPrice() {
        return recommendAssetsMap.get(INDEX_NORMAL);
    }

    /**
     * @description 获取稳健价格
     * <p>〈功能详细描述〉</p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/5 16:26
     */
    public BigDecimal getSteadyPrice() {
        return recommendAssetsMap.get(INDEX_STEADY);
    }

    /**
     * @description 获取超稳健价格
     * <p>〈功能详细描述〉</p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/5 16:26
     */
    public BigDecimal getSteadyPlusPrice() {
        return recommendAssetsMap.get(INDEX_STEADY_PLUS);
    }

    /**
     * @description 获取合适的配置
     * <p>
     *     1，高追少张
     * </p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/5 16:30
     */
    public static HedgeConfig getFitConfig(List<HedgeConfig> cfgList) {
        if (CollectionUtils.isEmpty(cfgList)) {
            return null;
        }
        HedgeConfig fit = null;
        for (HedgeConfig cfg : cfgList) {
            if (cfg == null) {
                continue;
            }
            if (fit == null) {
                fit = cfg;
                continue;
            }
            // 1，高追少张
            boolean isAllEq = true;
            for (int i = 1; i <= 4; i++) {
                BigDecimal cfgMultiple = cfg.getChaseMultipleMap().get(i);
                BigDecimal fitMultiple = fit.getChaseMultipleMap().get(i);
                if (cfgMultiple.compareTo(fitMultiple) == 0) {
                    continue;
                }
                isAllEq = false;
                if (cfgMultiple.compareTo(fitMultiple) > 0) {
                    fit = cfg;
                }
                break;
            }
            // 2, 全部倍数相等, 高张优先
            if (isAllEq && cfg.getBasisVolume() > fit.getBasisVolume()) {
                fit = cfg;
            }
        }
        return fit;
    }

    /**
     * @description 指定资产, 获取配置集合
     * <p>
     *     0, 激进配置
     *     1，常规配置
     *     2，稳健配置
     * </p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/5 16:51
     */
    public static Map<Integer, List<HedgeConfig>> getFitConfigListMapByPrice(
            BigDecimal price, List<HedgeConfig> cfgList) {
        Map<Integer, List<HedgeConfig>> cfgMap = new TreeMap<>();
        cfgMap.put(TYPE_RADICAL, new ArrayList<>());
        cfgMap.put(TYPE_NORMAL, new ArrayList<>());
        cfgMap.put(TYPE_STEADY, new ArrayList<>());
        cfgMap.put(TYPE_STEADY_PLUS, new ArrayList<>());
        for (HedgeConfig cfg : cfgList) {
            if (cfg == null) {
                continue;
            }
            // 1, price >= steadyPlusPrice, add 3
            if (price.compareTo(cfg.getSteadyPlusPrice()) >= 0) {
                cfgMap.get(TYPE_STEADY_PLUS).add(cfg);
                continue;
            }
            // 1, price >= steadyPrice, add 2
            if (price.compareTo(cfg.getSteadyPrice()) >= 0) {
                cfgMap.get(TYPE_STEADY).add(cfg);
                continue;
            }
            // 2, price >= normalPrice, add 1
            if (price.compareTo(cfg.getNormalPrice()) >= 0) {
                cfgMap.get(TYPE_NORMAL).add(cfg);
                continue;
            }
            // 3, price >= radicalPrice, add 0
            if (price.compareTo(cfg.getRadicalPrice()) >= 0) {
                cfgMap.get(TYPE_RADICAL).add(cfg);
            }
            // 4, pass
        }
        // 空集合填充
        if (cfgMap.get(TYPE_STEADY).isEmpty()) {
            cfgMap.get(TYPE_STEADY).addAll(cfgMap.get(TYPE_STEADY_PLUS));
        }
        if (cfgMap.get(TYPE_NORMAL).isEmpty()) {
            cfgMap.get(TYPE_NORMAL).addAll(cfgMap.get(TYPE_STEADY));
        }
        if (cfgMap.get(TYPE_RADICAL).isEmpty()) {
            cfgMap.get(TYPE_RADICAL).addAll(cfgMap.get(TYPE_NORMAL));
        }
        return cfgMap;
    }

    /**
     * @description 指定资产, 获取合适的配置集合
     * <p>
     *     0, 激进配置
     *     1，常规配置
     *     2，稳健配置
     * </p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/5 16:51
     */
    public static Map<Integer, HedgeConfig> getFitConfigMapByPrice(
            BigDecimal price, List<HedgeConfig> cfgList) {
        Map<Integer, List<HedgeConfig>> fitCfgListMap = HedgeConfig.getFitConfigListMapByPrice(
                price, cfgList);
        Map<Integer, HedgeConfig> fitCfgMap = new TreeMap<>();
        fitCfgMap.put(TYPE_RADICAL, HedgeConfig.getFitConfig(fitCfgListMap.get(TYPE_RADICAL)));
        fitCfgMap.put(TYPE_NORMAL, HedgeConfig.getFitConfig(fitCfgListMap.get(TYPE_NORMAL)));
        fitCfgMap.put(TYPE_STEADY, HedgeConfig.getFitConfig(fitCfgListMap.get(TYPE_STEADY)));
        fitCfgMap.put(TYPE_STEADY_PLUS, HedgeConfig.getFitConfig(fitCfgListMap.get(TYPE_STEADY_PLUS)));
        return fitCfgMap;
    }

    /**
     * @description 指定资产, 获取合适的配置
     * <p>
     *     0, 激进配置
     *     1，常规配置
     *     2，稳健配置
     * </p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/5 16:51
     */
    public static HedgeConfig getFitConfigByPrice(
            BigDecimal price, List<HedgeConfig> cfgList, int type) {
        return HedgeConfig.getFitConfigMapByPrice(price, cfgList).get(type);
    }

}


