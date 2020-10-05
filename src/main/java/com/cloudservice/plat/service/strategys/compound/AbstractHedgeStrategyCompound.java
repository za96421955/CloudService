package com.cloudservice.plat.service.strategys.compound;

import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.plat.service.strategys.StrategyAPI;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 复利策略
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
public abstract class AbstractHedgeStrategyCompound implements StrategyAPI<HedgeConfig> {

    /**
     * @description 获取策略类型
     * <p>〈功能详细描述〉</p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/5 19:10
     */
    protected abstract StrategyTypeEnum getStrategyType();

    /**
     * @description 获取配置集合
     * <p>〈功能详细描述〉</p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/5 19:11
     * @param   minVolume, maxVolume, minMultiple, maxMultiple
     */
    protected List<HedgeConfig> getConfigList(long minVolume, long maxVolume, int minMultiple, int maxMultiple) {
        // 小幅波动：贪婪, 后层倍数 <= 前层倍数
        // 大幅震荡：保本, 6,7层倍数固定2
        List<HedgeConfig> cfgList = new ArrayList<>();
        for (long volume = minVolume; volume <= maxVolume; volume++) {
            for (int m1 = minMultiple; m1 <= maxMultiple; m1++) {
                for (int m2 = minMultiple; m2 <= m1; m2++) {
                    for (int m3 = minMultiple; m3 <= m2; m3++) {
                        for (int m4 = minMultiple; m4 <= m3; m4++) {
                            for (int m5 = minMultiple; m5 <= m4; m5++) {
                                HedgeConfig cfg = new HedgeConfig(this.getStrategyType());
                                cfg.setLeverRate(ContractLeverRateEnum.LEVER_75);
                                cfg.setBasisVolume(volume);
                                cfg.getChaseMultipleMap().put(1, BigDecimal.valueOf(m1));
                                cfg.getChaseMultipleMap().put(2, BigDecimal.valueOf(m2));
                                cfg.getChaseMultipleMap().put(3, BigDecimal.valueOf(m3));
                                cfg.getChaseMultipleMap().put(4, BigDecimal.valueOf(m4));
                                cfg.getChaseMultipleMap().put(5, BigDecimal.valueOf(m5));
                                cfg.getChaseMultipleMap().put(6, BigDecimal.valueOf(2));
                                cfg.getChaseMultipleMap().put(7, BigDecimal.valueOf(2));
                                cfg.setIncomePricePlan(new BigDecimal("0.6"));
                                cfg.setProfitMultiple(BigDecimal.valueOf(2));
                                cfg.setProfitTrackIntervalTime(1000);
                                cfg.setTimeout(30);
                                cfgList.add(cfg);
                            }
                        }
                    }
                }
            }
        }
        return cfgList;
    }

    public static void main(String[] args) {
        HedgeStrategyCompoundLow low = new HedgeStrategyCompoundLow();
        HedgeStrategyCompoundIn in = new HedgeStrategyCompoundIn();
        HedgeStrategyCompoundHigh high = new HedgeStrategyCompoundHigh();
        HedgeStrategyCompoundLarge large = new HedgeStrategyCompoundLarge();

        Map<Integer, HedgeConfig> fitCfgMap = HedgeConfig.getFitConfigMapByPrice(
                BigDecimal.valueOf(1000), low.getStrategyList());
        System.out.println("radical: " + fitCfgMap.get(HedgeConfig.TYPE_RADICAL));
        System.out.println("normal: " + fitCfgMap.get(HedgeConfig.TYPE_NORMAL));
        System.out.println("steady: " + fitCfgMap.get(HedgeConfig.TYPE_STEADY));
        System.out.println("steadyPlus: " + fitCfgMap.get(HedgeConfig.TYPE_STEADY_PLUS));
        System.out.println();

        fitCfgMap = HedgeConfig.getFitConfigMapByPrice(
                BigDecimal.valueOf(20000), in.getStrategyList());
        System.out.println("radical: " + fitCfgMap.get(HedgeConfig.TYPE_RADICAL));
        System.out.println("normal: " + fitCfgMap.get(HedgeConfig.TYPE_NORMAL));
        System.out.println("steady: " + fitCfgMap.get(HedgeConfig.TYPE_STEADY));
        System.out.println("steadyPlus: " + fitCfgMap.get(HedgeConfig.TYPE_STEADY_PLUS));
        System.out.println();

        fitCfgMap = HedgeConfig.getFitConfigMapByPrice(
                BigDecimal.valueOf(100000), high.getStrategyList());
        System.out.println("radical: " + fitCfgMap.get(HedgeConfig.TYPE_RADICAL));
        System.out.println("normal: " + fitCfgMap.get(HedgeConfig.TYPE_NORMAL));
        System.out.println("steady: " + fitCfgMap.get(HedgeConfig.TYPE_STEADY));
        System.out.println("steadyPlus: " + fitCfgMap.get(HedgeConfig.TYPE_STEADY_PLUS));
        System.out.println();

        fitCfgMap = HedgeConfig.getFitConfigMapByPrice(
                BigDecimal.valueOf(1000000), large.getStrategyList());
        System.out.println("radical: " + fitCfgMap.get(HedgeConfig.TYPE_RADICAL));
        System.out.println("normal: " + fitCfgMap.get(HedgeConfig.TYPE_NORMAL));
        System.out.println("steady: " + fitCfgMap.get(HedgeConfig.TYPE_STEADY));
        System.out.println("steadyPlus: " + fitCfgMap.get(HedgeConfig.TYPE_STEADY_PLUS));
        System.out.println();
    }

}


