package com.cloudservice.plat.service.strategys.fixed;

import com.cloudservice.plat.enums.StrategyTypeEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 4张固定策略
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
@Service
public class HedgeStrategyFixedVolume4 extends AbstractHedgeStrategyFixed {

    @Override
    protected StrategyTypeEnum getStrategyType() {
        return StrategyTypeEnum.FIXED_VOLUME_4;
    }

    @Override
    protected long getBasisVolume() {
        return 4;
    }

    @Override
    protected BigDecimal getChaseMultiple_1() {
        return BigDecimal.valueOf(2);
    }

    @Override
    protected BigDecimal getChaseMultiple_2() {
        return BigDecimal.valueOf(2);
    }

    @Override
    protected BigDecimal getChaseMultiple_3() {
        return BigDecimal.valueOf(2);
    }

    @Override
    protected BigDecimal getChaseMultiple_4() {
        return BigDecimal.valueOf(2);
    }

    @Override
    protected BigDecimal getChaseMultiple_5() {
        return BigDecimal.valueOf(2);
    }

    @Override
    protected BigDecimal getChaseMultiple_6() {
        return BigDecimal.valueOf(2);
    }

    @Override
    protected BigDecimal getChaseMultiple_7() {
        return BigDecimal.valueOf(2);
    }

}


