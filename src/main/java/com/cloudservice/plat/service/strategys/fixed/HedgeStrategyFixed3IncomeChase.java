package com.cloudservice.plat.service.strategys.fixed;

import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.plat.service.strategys.StrategyAPI;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 3次收益追逐固定策略
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
@Service
public class HedgeStrategyFixed3IncomeChase implements StrategyAPI<HedgeConfig> {

    @Override
    public List<HedgeConfig> getStrategyList() {
        HedgeConfig cfg = new HedgeConfig(StrategyTypeEnum.FIXED_3_INCOME_CHASE);
        cfg.setLeverRate(ContractLeverRateEnum.LEVER_75);
        cfg.setBasisVolume(2);
        cfg.getChaseMultipleMap().put(1, BigDecimal.valueOf(3));
        cfg.getChaseMultipleMap().put(2, BigDecimal.valueOf(3));
        cfg.getChaseMultipleMap().put(3, BigDecimal.valueOf(3));
        cfg.getChaseMultipleMap().put(4, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(5, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(6, BigDecimal.valueOf(2));
        cfg.getChaseMultipleMap().put(7, BigDecimal.valueOf(2));
        cfg.setIncomePricePlan(new BigDecimal("0.6"));
        cfg.setProfitMultiple(BigDecimal.valueOf(2));
        cfg.setProfitTrackIntervalTime(500);
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


