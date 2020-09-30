package com.cloudservice.plat.service.strategys.x20;

import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.plat.service.strategys.StrategyAPI;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 基础固定策略
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
@Service
public class X20HedgeStrategyFixedBasis implements StrategyAPI<HedgeConfig> {

    @Override
    public HedgeConfig getStrategy() {
        HedgeConfig cfg = new HedgeConfig(StrategyTypeEnum.FIXED_BASIS_20X);
        cfg.setSteadyAmountUSD(new BigDecimal("128"));
        cfg.setMediocreAmountUSD(new BigDecimal("64"));
        cfg.setRadicalAmountUSD(new BigDecimal("32"));
        cfg.setLeverRate(ContractLeverRateEnum.LEVER_20);
        cfg.setBasisVolume(2);
        cfg.getIntervalMultipleMap().put(1, BigDecimal.valueOf(2));
        cfg.getIntervalMultipleMap().put(2, BigDecimal.valueOf(2));
        cfg.getIntervalMultipleMap().put(3, BigDecimal.valueOf(2));
        cfg.getIntervalMultipleMap().put(4, BigDecimal.valueOf(2));
        cfg.getIntervalMultipleMap().put(5, BigDecimal.valueOf(2));
        cfg.getIntervalMultipleMap().put(6, BigDecimal.valueOf(2));
        cfg.getIntervalMultipleMap().put(7, BigDecimal.valueOf(2));
        cfg.setIncomePricePlan(new BigDecimal("0.6"));
        cfg.setChaseProfitMultiple(BigDecimal.valueOf(2));
        cfg.setProfitTrackIntervalTime(1000);
        cfg.setTimeout(30);
        return cfg;
    }

}


