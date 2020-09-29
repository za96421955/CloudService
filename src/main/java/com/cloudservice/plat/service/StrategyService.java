package com.cloudservice.plat.service;

import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.trade.hedge.model.HedgeConfig;

/**
 * 策略服务
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
public interface StrategyService {

    /**
     * @description 初始化对冲策略
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/29 16:30
     **/
    void initHedgeStrategy();

    /**
     * @description 记录对冲策略
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/29 16:29
     * @param hedgeConfig
     **/
    void recordHedgeStrategy(HedgeConfig hedgeConfig);

    /**
     * @description 加载对冲策略
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/29 16:27
     **/
    void loadHedgeStrategy();

}


