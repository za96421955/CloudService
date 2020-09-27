package com.cloudservice.trade.huobi.demo;

import com.cloudservice.trade.huobi.service.contract.ContractAccountService;
import com.cloudservice.trade.huobi.service.contract.ContractTradeService;
import com.cloudservice.trade.huobi.service.contract.impl.ContractAccountServiceImpl;
import com.cloudservice.trade.huobi.service.contract.impl.ContractTradeServiceImpl;
import com.cloudservice.trade.huobi.service.spot.SpotBasicService;
import com.cloudservice.trade.huobi.service.spot.SpotMarketService;
import com.cloudservice.trade.huobi.service.spot.impl.SpotBasicServiceImpl;
import com.cloudservice.trade.huobi.service.spot.impl.SpotMarketServiceImpl;

/**
 * TODO
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/8/14
 */
public class RequestDemo {

    public static void main(String[] args) throws Exception {
        SpotBasicService spotBasic = new SpotBasicServiceImpl();

        SpotMarketService spotMarket = new SpotMarketServiceImpl();
//        spotMarket.getDepth(SymbolEnum.ETH_USDT, DepthEnum.DEPTH_20, DepthTypeEnum.STEP_0_01);

        ContractAccountService contractAccount = new ContractAccountServiceImpl();
//        contractAccount.getAccountPositionInfo(Key.ACCESS_KEY_XU, Key.SECRET_KEY_XU, SymbolEnum.ETH);

        ContractTradeService contractTrade = new ContractTradeServiceImpl();
    }

}


