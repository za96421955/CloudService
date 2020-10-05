package com.cloudservice.plat.service.strategys.compound;

import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 高资产复利策略
 * <p>
 *     20000 ~ 100000
 * </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
@Service
public class HedgeStrategyCompoundHigh extends AbstractHedgeStrategyCompound {

    @Override
    protected StrategyTypeEnum getStrategyType() {
        return StrategyTypeEnum.COMPOUND_HIGH;
    }

    @Override
    public List<HedgeConfig> getStrategyList() {
        return this.getConfigList(4, 6, 3, 8);
    }

}


