package com.cloudservice.plat.service.strategys.compound;

import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 中资产复利策略
 * <p>
 *     1000 ~ 20000
 * </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
@Service
public class HedgeStrategyCompoundIn extends AbstractHedgeStrategyCompound {

    @Override
    protected StrategyTypeEnum getStrategyType() {
        return StrategyTypeEnum.COMPOUND_IN;
    }

    @Override
    public List<HedgeConfig> getStrategyList() {
        return this.getConfigList(3, 5, 2, 6);
    }

}


