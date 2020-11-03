package com.cloudservice.trade.strategy.service.impl;

import com.cloudservice.base.BaseService;
import com.cloudservice.base.Result;
import com.cloudservice.trade.huobi.enums.ContractDirectionEnum;
import com.cloudservice.trade.huobi.enums.ContractLeverRateEnum;
import com.cloudservice.trade.huobi.model.contract.Position;
import com.cloudservice.trade.huobi.service.contract.ContractAccountService;
import com.cloudservice.trade.huobi.service.contract.ContractMarketService;
import com.cloudservice.trade.huobi.service.contract.ContractTradeService;
import com.cloudservice.trade.strategy.model.Entrust;
import com.cloudservice.trade.strategy.service.StrategyAPI;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * 马丁策略实现
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/10/22
 */
public class MartingleStrategyImpl extends BaseService implements StrategyAPI {

    @Autowired
    private ContractMarketService contractMarketService;
    @Autowired
    private ContractAccountService contractAccountService;
    @Autowired
    private ContractTradeService contractTradeService;

    @Override
    public Result positionCheck(Entrust entrust) {
        // 持仓检查
        List<Position> positionList = contractAccountService.getPositionList(entrust.getAccess(), entrust.getSecret(), entrust.getSymbol());
        Position buy = this.getPosition(positionList, ContractDirectionEnum.BUY);
        Position sell = this.getPosition(positionList, ContractDirectionEnum.SELL);
        logger.info("[{}] entrust={}, buy={}, sell={}, 持仓检查", LOG_MARK, entrust, buy, sell);
        // 0: 多, 1: 空
        return Result.buildSuccess(buy, sell);
    }

    /**
     * @description 获取指定持仓信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/26 14:43
     * @param positionList, direction
     **/
    private Position getPosition(List<Position> positionList, ContractDirectionEnum direction) {
        for (Position position : positionList) {
            if (position == null) {
                continue;
            }
            if (direction.getValue().equals(position.getDirection())) {
                return position;
            }
        }
        return null;
    }

    @Override
    public Result profit(Entrust entrust, Position position) {
        return null;
    }

    @Override
    public Result loss(Entrust entrust, Position position) {
        return null;
    }

    @Override
    public BigDecimal getLossDiffPrice(Entrust entrust, Position position) {
        return null;
    }

    @Override
    public BigDecimal getLossChaseVolume(Entrust entrust, Position position) {
        return null;
    }

    @Override
    public boolean closeTrade(Entrust entrust) {
        return false;
    }

}


