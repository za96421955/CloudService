package com.cloudservice.plat.service.strategys;

import com.cloudservice.trade.hedge.model.HedgeConfig;

/**
 * 对冲策略API
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
public interface HedgeStrategyAPI {

    /**
     * @description 获取策略
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/29 17:26
     **/
    HedgeConfig getStrategy();

}
