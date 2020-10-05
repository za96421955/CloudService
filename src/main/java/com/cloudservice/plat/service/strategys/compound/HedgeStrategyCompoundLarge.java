package com.cloudservice.plat.service.strategys.compound;

import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 大额资产复利策略
 * <p>
 *     > 100000
 * </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
@Service
public class HedgeStrategyCompoundLarge extends AbstractHedgeStrategyCompound {

    @Override
    protected StrategyTypeEnum getStrategyType() {
        return StrategyTypeEnum.COMPOUND_LARGE;
    }

    @Override
    public List<HedgeConfig> getStrategyList() {
        return this.getConfigList(10, 20, 4, 10);
    }

    @Override
    public BigDecimal getMinAssets() {
        return BigDecimal.valueOf(100000);
    }

    @Override
    public BigDecimal getMaxAssets() {
        return null;
    }

}


