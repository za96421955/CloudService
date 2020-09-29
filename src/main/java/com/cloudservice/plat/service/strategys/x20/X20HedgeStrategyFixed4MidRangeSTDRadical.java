package com.cloudservice.plat.service.strategys.x20;

import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.plat.service.strategys.StrategyAPI;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 4张中区间拉近(2233222)20X固定策略 - 激进
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
@Service
public class X20HedgeStrategyFixed4MidRangeSTDRadical implements StrategyAPI<HedgeConfig> {

    @Override
    public HedgeConfig getStrategy() {
        HedgeConfig cfg = new HedgeConfig(StrategyTypeEnum.FIXED_4_MIDRANGE_STD_RADICAL_20X);
        cfg.setPositionAmountUSD(new BigDecimal("144"));
        cfg.setLeverRate(ContractLeverRateEnum.LEVER_20);
        cfg.setBasisVolume(4);
        cfg.setBeforeIntervalMultiple(BigDecimal.valueOf(2));
        cfg.setMidIntervalMultiple(BigDecimal.valueOf(3));
        cfg.setAfterIntervalMultiple(BigDecimal.valueOf(2));
        cfg.setIncomePricePlan(new BigDecimal("0.6"));
        cfg.setProfitBasisMultiple(BigDecimal.ONE);
        cfg.setProfitTrackIntervalTime(1000);
        cfg.setTimeout(30);
        return cfg;
    }

}


