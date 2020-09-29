package com.cloudservice.plat.controller;

import com.cloudservice.base.BaseController;
import com.cloudservice.base.Result;
import com.cloudservice.plat.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器：策略
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/9
 */
@RestController
@RequestMapping("/strategy")
public class StrategyController extends BaseController {

    @Autowired
    private StrategyService strategyService;

    @PostMapping("/hedge/init")
    @Description("初始化对冲策略")
    public Result initHedgeStrategy() {
        try {
            strategyService.initHedgeStrategy();
            return Result.buildSuccess();
        } catch (Exception e) {
            logger.error("[策略] 初始化对冲策略异常, {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/hedge/load")
    @Description("加载对冲策略")
    public Result loadHedgeStrategy() {
        try {
            strategyService.loadHedgeStrategy();
            return Result.buildSuccess();
        } catch (Exception e) {
            logger.error("[策略] 加载对冲策略异常, {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

}


