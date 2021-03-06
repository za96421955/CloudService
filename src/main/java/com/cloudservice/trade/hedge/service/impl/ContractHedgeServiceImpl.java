package com.cloudservice.trade.hedge.service.impl;

import com.cloudservice.base.Result;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.huobi.enums.*;
import com.cloudservice.trade.huobi.model.contract.Account;
import com.cloudservice.trade.huobi.model.contract.Order;
import com.cloudservice.trade.huobi.model.contract.Position;
import com.cloudservice.trade.huobi.model.spot.Kline;
import com.cloudservice.trade.huobi.service.contract.ContractAccountService;
import com.cloudservice.trade.huobi.service.contract.ContractMarketService;
import com.cloudservice.trade.huobi.service.contract.ContractTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 对冲服务：交割合约实现
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/24
 */
@Service
public class ContractHedgeServiceImpl extends AbstractHedgeService {

    @Autowired
    private ContractMarketService contractMarketService;
    @Autowired
    private ContractAccountService contractAccountService;
    @Autowired
    private ContractTradeService contractTradeService;

    @Override
    protected Account getAccount(Track track) {
        return contractAccountService.getAccountInfo(track.getAccess(), track.getSecret(), track.getSymbol());
    }

    @Override
    protected List<Position> getPositionList(Track track) {
        return contractAccountService.getPositionList(track.getAccess(), track.getSecret(), track.getSymbol());
    }

    @Override
    protected Kline getKlineCurr(SymbolEnum symbol, ContractTypeEnum contractType) {
        Kline kline = contractMarketService.getKlineCurr(SymbolContractEnum.get(symbol, contractType));
        if (kline == null) {
            kline = spotMarketService.getKlineCurr(SymbolUSDTEnum.getUSDT(symbol.getValue()));
        }
        return kline;
    }

    @Override
    protected Result open(Track track, ContractDirectionEnum direction, long volume) {
        return contractTradeService.order(track.getAccess(), track.getSecret(), track.getSymbol(), track.getContractType()
                , null, volume, direction, ContractOffsetEnum.OPEN
                , track.getHedgeConfig().getLeverRate(), ContractOrderPriceTypeEnum.OPTIMAL_5);
    }

    @Override
    protected Result close(Track track, Position position) {
        return contractTradeService.order(track.getAccess(), track.getSecret(), track.getSymbol(), track.getContractType()
                , null, position.getVolume().longValue()
                , ContractDirectionEnum.get(position.getDirection()).getNegate(), ContractOffsetEnum.CLOSE
                , track.getHedgeConfig().getLeverRate(), ContractOrderPriceTypeEnum.OPTIMAL_5);
    }

    @Override
    protected Result cancel(Track track) {
        return contractTradeService.cancelAll(track.getAccess(), track.getSecret(), track.getSymbol());
    }

    @Override
    protected Order getOrderInfo(Track track, String orderId) {
        return contractTradeService.getOrderInfo(track.getAccess(), track.getSecret(), track.getSymbol(), orderId);
    }

}


