package com.cloudservice.plat.service.strategys.compound;

import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 低资产复利策略
 * <p>
 *     < 1000
 * </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
@Service
public class HedgeStrategyCompoundLow extends AbstractHedgeStrategyCompound {

    @Override
    protected StrategyTypeEnum getStrategyType() {
        return StrategyTypeEnum.COMPOUND_LOW;
    }

    @Override
    public List<HedgeConfig> getStrategyList() {
        return this.getConfigList(2, 5, 2, 3);
    }

    @Override
    public BigDecimal getMinAssets() {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getMaxAssets() {
        return BigDecimal.valueOf(1000);
    }

}


