package com.cloudservice.trade.hedge.service.impl;

import com.cloudservice.base.Result;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.huobi.enums.*;
import com.cloudservice.trade.huobi.model.contract.Order;
import com.cloudservice.trade.huobi.model.contract.Position;
import com.cloudservice.trade.huobi.model.spot.Kline;
import com.cloudservice.trade.huobi.service.swap.SwapAccountService;
import com.cloudservice.trade.huobi.service.swap.SwapMarketService;
import com.cloudservice.trade.huobi.service.swap.SwapTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    protected boolean isStopTrade(Track track, Position position) {
        // 停止交易, 无持仓 || 平仓张数 > basis, 则不再向下追仓
        if (!track.isStopTrade()) {
            return false;
        }
        Position positionCheck = swapAccountService.getPositionInfo(track.getAccess(), track.getSecret(), track.getContractCode());
        return positionCheck == null || position.getVolume().compareTo(BigDecimal.valueOf(track.getHedgeConfig().getBasisVolume())) > 0;
    }

    @Override
    protected Order getOrderInfo(Track track, String orderId) {
        return swapTradeService.getOrderInfo(track.getAccess(), track.getSecret(), track.getContractCode(), orderId);
    }

}


