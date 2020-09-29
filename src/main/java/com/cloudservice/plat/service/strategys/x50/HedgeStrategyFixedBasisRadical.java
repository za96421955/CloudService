package com.cloudservice.plat.service.strategys.x50;

import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.plat.service.strategys.HedgeStrategyAPI;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 基础固定策略 - 激进
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
@Service
public class HedgeStrategyFixedBasisRadical implements HedgeStrategyAPI {

    @Override
    public HedgeConfig getStrategy() {
        HedgeConfig cfg = new HedgeConfig(StrategyTypeEnum.FIXED_BASIS_RADICAL_50X);
        cfg.setPositionAmountUSD(new BigDecimal("12.8"));
        cfg.setLeverRate(ContractLeverRateEnum.LEVER_50);
        cfg.setBasisVolume(2);
        cfg.setBeforeIntervalMultiple(BigDecimal.valueOf(2));
        cfg.setMidIntervalMultiple(BigDecimal.valueOf(2));
        cfg.setAfterIntervalMultiple(BigDecimal.valueOf(2));
        cfg.setIncomePricePlan(new BigDecimal("0.6"));
        cfg.setProfitBasisMultiple(BigDecimal.ONE);
        cfg.setProfitTrackIntervalTime(1000);
        cfg.setTimeout(30);
        return cfg;
    }

}

