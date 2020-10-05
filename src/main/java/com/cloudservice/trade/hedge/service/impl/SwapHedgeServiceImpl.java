package com.cloudservice.trade.hedge.service.impl;

import com.cloudservice.base.Result;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.huobi.enums.*;
import com.cloudservice.trade.huobi.model.contract.Account;
import com.cloudservice.trade.huobi.model.contract.Order;
import com.cloudservice.trade.huobi.model.contract.Position;
import com.cloudservice.trade.huobi.model.spot.Kline;
import com.cloudservice.trade.huobi.service.swap.SwapAccountService;
import com.cloudservice.trade.huobi.service.swap.SwapMarketService;
import com.cloudservice.trade.huobi.service.swap.SwapTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 对冲服务：永续合约实现
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/24
 */
@Service
public class SwapHedgeServiceImpl extends AbstractHedgeService {

    @Autowired
    private SwapMarketService swapMarketService;
    @Autowired
    private SwapAccountService swapAccountService;
    @Autowired
    private SwapTradeService swapTradeService;

    @Override
    protected Account getAccount(Track track) {
        return swapAccountService.getAccountInfo(track.getAccess(), track.getSecret(), track.getContractCode());
    }

    @Override
    protected List<Position> getPositionList(Track track) {
        return swapAccountService.getPositionList(track.getAccess(), track.getSecret(), track.getContractCode());
    }

    @Override
    protected Kline getKlineCurr(SymbolEnum symbol, ContractTypeEnum contractType) {
        Kline kline = swapMarketService.getKlineCurr(ContractCodeEnum.getUSD(symbol.getValue()));
        if (kline == null) {
            kline = spotMarketService.getKlineCurr(SymbolUSDTEnum.getUSDT(symbol.getValue()));
        }
        return kline;
    }

    @Override
    protected Result open(Track track, ContractDirectionEnum direction, long volume) {
        return swapTradeService.order(track.getAccess(), track.getSecret(), track.getContractCode()
                , null, volume, direction, ContractOffsetEnum.OPEN
                , track.getHedgeConfig().getLeverRate(), ContractOrderPriceTypeEnum.OPTIMAL_5);
    }

    @Override
    protected Result close(Track track, Position position) {
        return swapTradeService.order(track.getAccess(), track.getSecret(), track.getContractCode()
                , null, position.getVolume().longValue()
                , ContractDirectionEnum.get(position.getDirection()).getNegate(), ContractOffsetEnum.CLOSE
                , track.getHedgeConfig().getLeverRate(), ContractOrderPriceTypeEnum.OPTIMAL_5);
    }

    @Override
    protected Result cancel(Track track) {
        return swapTradeService.cancelAll(track.getAccess(), track.getSecret(), track.getContractCode());
    }

    @Override
    protected Order getOrderInfo(Track track, String orderId) {
        return swapTradeService.getOrderInfo(track.getAccess(), track.getSecret(), track.getContractCode(), orderId);
    }

}


