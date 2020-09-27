package com.cloudservice.trade.hedge.service;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 对冲服务工厂
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/26
 */
@Service
public class HedgeServiceFactory {
    /** 交割合约 */
    public static final String CONTRACT = "contract";
    /** 永续合约 */
    public static final String SWAP = "swap";

    @Resource(name = "contractHedgeServiceImpl")
    private HedgeService contractHedgeService;
    @Resource(name = "swapHedgeServiceImpl")
    private HedgeService swapHedgeService;

    /**
     * @description 获取对冲服务
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/26 15:23
     * @param type
     **/
    public HedgeService getHedgeService(String type) {
        if (CONTRACT.equals(type)) {
            return contractHedgeService;
        }
        if (SWAP.equals(type)) {
            return swapHedgeService;
        }
        return null;
    }

}


