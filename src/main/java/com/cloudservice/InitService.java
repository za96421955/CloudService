package com.cloudservice;

import com.cloudservice.plat.service.StrategyService;
import com.cloudservice.plat.thread.PriceTrackScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 初始化服务
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
@Component
public class InitService {

    @Autowired
    private StrategyService strategyService;
    @Autowired
    private PriceTrackScheduler priceTrackScheduler;

    @PostConstruct
    private void init() {
        // 初始化对冲策略
        strategyService.initHedgeStrategy();
        // 现价追踪
        priceTrackScheduler.run();
    }

}


