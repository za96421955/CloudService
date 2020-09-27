package com.cloudservice.trade.analyse.service.trade.impl.analyse;

import com.cloudservice.trade.analyse.model.trade.Analyse;
import com.cloudservice.trade.huobi.enums.ContractDirectionEnum;
import com.cloudservice.trade.huobi.enums.DepthEnum;
import com.cloudservice.trade.huobi.enums.DepthTypeEnum;
import com.cloudservice.trade.huobi.enums.SymbolUSDTEnum;
import com.cloudservice.trade.huobi.model.spot.Depth;
import com.cloudservice.trade.huobi.model.spot.DepthDetail;
import com.cloudservice.trade.huobi.service.spot.SpotMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 现价分析: 深度占比分析
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/10
 */
@Service
public class DepthAnalyseServiceImpl extends AbstractAnalyseServiceImpl {

    @Autowired
    private SpotMarketService spotMarketService;

    @Override
    public ContractDirectionEnum getDirection(SymbolUSDTEnum symbol) {
        // 获取1深度数据
        Depth depthInfo_1 = spotMarketService.getDepth(symbol, DepthEnum.DEPTH_5, DepthTypeEnum.STEP_1);
        depthInfo_1.calculateDetailRate();
        // 当前第一条/第二条, 占比 > 2倍
        ContractDirectionEnum direction = this.getDirection(depthInfo_1.getFirstBuy(), depthInfo_1.getFirstSell()
                , depthInfo_1.getFirstBuy().compareMultiple(depthInfo_1.getFirstSell(), BigDecimal.valueOf(2)));
        if (direction == null) {
            direction = this.getDirection(depthInfo_1.getSecondBuy(), depthInfo_1.getSecondSell()
                    , depthInfo_1.getSecondBuy().compareMultiple(depthInfo_1.getSecondSell(), BigDecimal.valueOf(2)));
        }
        return direction;
    }

    @Override
    public Analyse getAnalyse(SymbolUSDTEnum symbol) {
        return this.getAnalyseByDepth01(symbol, this.getDirection(symbol), null);
    }

    /**
     * @description 获取0.1深度分析数据
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/15 17:32
     * @param symbol, direction, openPrice
     **/
    private Analyse getAnalyseByDepth01(SymbolUSDTEnum symbol, ContractDirectionEnum direction, BigDecimal openPrice) {
        // 获取0.1深度数据
        Depth depthInfo_01 = spotMarketService.getDepth(symbol, DepthEnum.DEPTH_20, DepthTypeEnum.STEP_0_1);
        // 累计买入、卖出量, 记录最大买入、卖出价
        DepthDetail buy = depthInfo_01.getMaxBuy(openPrice);
        DepthDetail sell = depthInfo_01.getMaxSell(openPrice);
        if (buy == null || sell == null) {
            return null;
        }
        BigDecimal adjustmentPrice = new BigDecimal("0.01");
        // 设置分析信息
        Analyse analyse = new Analyse();
        analyse.setDirection(direction);
        if (ContractDirectionEnum.BUY.equals(direction)) {
            // 开多止盈, 最大卖价 - price
            analyse.setTriggerProfit(sell.getPrice().subtract(adjustmentPrice));
            analyse.setProfit(analyse.getTriggerProfit());
            // 开多止损, 最大买价 - price * 3
            analyse.setTriggerLoss(buy.getPrice().subtract(adjustmentPrice.multiply(BigDecimal.valueOf(3))));
            analyse.setLoss(analyse.getTriggerLoss());
        } else {
            // 开空止盈, 最大买价 + price
            analyse.setTriggerProfit(buy.getPrice().add(adjustmentPrice));
            analyse.setProfit(analyse.getTriggerProfit());
            // 开空止损, 最大卖价 + price * 3
            analyse.setTriggerLoss(sell.getPrice().add(adjustmentPrice.multiply(BigDecimal.valueOf(3))));
            analyse.setLoss(analyse.getTriggerLoss());
        }
        return analyse;
    }

}


