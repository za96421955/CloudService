package com.cloudservice.plat.service.impl;

import com.cloudservice.base.BaseService;
import com.cloudservice.plat.context.PlatContext;
import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.plat.service.FileService;
import com.cloudservice.plat.service.StrategyService;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 策略服务实现
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
@Service
public class StrategyServiceImpl extends BaseService implements StrategyService {

    @Autowired
    private FileService fileService;

    @Override
    public void initHedgeStrategy() {
        // 基础固定策略 - 稳健
        HedgeConfig cfg = new HedgeConfig(StrategyTypeEnum.FIXED_BASIS_STEADY);
        cfg.setLeverRate(ContractLeverRateEnum.LEVER_20);
        cfg.setBasisVolume(2);
        cfg.setBeforeIntervalMultiple(BigDecimal.valueOf(2));
        cfg.setMidIntervalMultiple(BigDecimal.valueOf(2));
        cfg.setAfterIntervalMultiple(BigDecimal.valueOf(2));
        cfg.setIncomePricePlan(new BigDecimal("0.5"));
        cfg.setProfitBasisMultiple(BigDecimal.ONE);
        cfg.setProfitTrackIntervalTime(1000);
        cfg.setTimeout(30);
    }

    @Override
    public void recordHedgeStrategy(HedgeConfig hedgeConfig) {
        // 记录上下文
        PlatContext.setHedgeStrategy(hedgeConfig);
        // 持久化
        fileService.writeHedgeConfig(hedgeConfig);
    }

    @Override
    public void loadHedgeStrategy() {
        // 读取文件, 记录上下文
        List<HedgeConfig> cfgList = fileService.readHedgeConfigList();
        for (HedgeConfig cfg : cfgList) {
            if (cfg == null) {
                continue;
            }
            PlatContext.setHedgeStrategy(cfg);
        }
    }

}


