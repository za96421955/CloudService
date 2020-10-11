package com.cloudservice.plat.service.strategys.fixed;

import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.plat.service.strategys.StrategyAPI;
import com.cloudservice.plat.service.strategys.compound.HedgeStrategyCompoundHigh;
import com.cloudservice.plat.service.strategys.compound.HedgeStrategyCompoundIn;
import com.cloudservice.plat.service.strategys.compound.HedgeStrategyCompoundLarge;
import com.cloudservice.plat.service.strategys.compound.HedgeStrategyCompoundLow;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 固定策略
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
public abstract class AbstractHedgeStrategyFixed implements StrategyAPI<HedgeConfig> {

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
     * @description 获取基础张数
     * <p>〈功能详细描述〉</p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/11 17:07
     */
    protected abstract long getBasisVolume();

    @Override
    public List<HedgeConfig> getStrategyList() {
        HedgeConfig cfg = new HedgeConfig(this.getStrategyType());
        cfg.setLeverRate(ContractLeverRateEnum.LEVER_75);
        cfg.setBasisVolume(this.getBasisVolume());
        cfg.getChaseMultipleMap().put(1, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(2, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(3, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(4, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(5, BigDecimal.valueOf(3));
        cfg.getChaseMultipleMap().put(6, BigDecimal.valueOf(3));
        cfg.getChaseMultipleMap().put(7, BigDecimal.valueOf(3));
        cfg.setIncomePricePlan(new BigDecimal("0.6"));
        cfg.setProfitMultiple(BigDecimal.valueOf(2));
        cfg.setProfitTrackIntervalTime(1000);
        cfg.setTimeout(30);
        cfg.calculateChaseInfo();

        List<HedgeConfig> cfgList = new ArrayList<>();
        cfgList.add(cfg);
        return cfgList;
    }

    @Override
    public BigDecimal getMinAssets() {
        return null;
    }

    @Override
    public BigDecimal getMaxAssets() {
        return null;
    }

}


