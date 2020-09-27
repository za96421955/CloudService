package com.cloudservice.trade.analyse.service.trade.impl.analyse;

import com.cloudservice.trade.analyse.context.TradeContext;
import com.cloudservice.trade.analyse.model.trade.Analyse;
import com.cloudservice.trade.analyse.model.trade.Prophecy;
import com.cloudservice.trade.analyse.thread.ProphecyMachine;
import com.cloudservice.trade.huobi.enums.ContractDirectionEnum;
import com.cloudservice.trade.huobi.enums.PeriodEnum;
import com.cloudservice.trade.huobi.enums.SymbolUSDTEnum;
import com.cloudservice.trade.huobi.model.spot.Kline;
import com.cloudservice.trade.huobi.service.spot.SpotMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 现价分析: 周期分析
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/10
 */
@Service
public class CycleAnalyseServiceImpl extends AbstractAnalyseServiceImpl {

    @Autowired
    private SpotMarketService spotMarketService;
    @Autowired
    private ProphecyMachine prophecyMachine;

    @Override
    public ContractDirectionEnum getDirection(SymbolUSDTEnum symbol) {
        return this.getDirection(this.getProphecy(this.getKline(symbol)));
    }

    private ContractDirectionEnum getDirection(Prophecy prophecy) {
        if (prophecy == null || prophecy.getKlineRange() == null) {
            return null;
        }
        return prophecy.getKlineRange().getHighRate().compareTo(prophecy.getKlineRange().getLowRate()) > 0
                ? ContractDirectionEnum.BUY : ContractDirectionEnum.SELL;
    }

    /**
     * 当前分析数据
     * 正向分析：适合开空
     * 逆向分析：适合开多
     */
    @Override
    public Analyse getAnalyse(SymbolUSDTEnum symbol) {
        Kline kline = this.getKline(symbol);
        Prophecy prophecy = this.getProphecy(kline);
        ContractDirectionEnum localDirection = this.getDirection(prophecy);
        if (localDirection == null) {
            return null;
        }
        // 设置分析信息
        Analyse analyse = new Analyse();
        analyse.setPrice(kline.getClose());
        analyse.setDirection(localDirection);
        if (ContractDirectionEnum.BUY.equals(analyse.getDirection())) {
            analyse.setProfit(analyse.getPrice().add(analyse.getPrice().multiply(prophecy.getKlineRange().getHighRate())));
            analyse.setTriggerProfit(analyse.getProfit().subtract(prophecy.getPeriod().getAdjustmentPrice()));
            analyse.setLoss(analyse.getPrice().subtract(analyse.getPrice().multiply(prophecy.getKlineRange().getLowRate()).multiply(BigDecimal.valueOf(2))));
            analyse.setTriggerLoss(analyse.getLoss().subtract(prophecy.getPeriod().getAdjustmentPrice().multiply(BigDecimal.valueOf(3))));
        } else {
            analyse.setProfit(analyse.getPrice().subtract(analyse.getPrice().multiply(prophecy.getKlineRange().getLowRate())));
            analyse.setTriggerProfit(analyse.getProfit().add(prophecy.getPeriod().getAdjustmentPrice()));
            analyse.setLoss(analyse.getPrice().add(analyse.getPrice().multiply(prophecy.getKlineRange().getHighRate()).multiply(BigDecimal.valueOf(2))));
            analyse.setTriggerLoss(analyse.getLoss().add(prophecy.getPeriod().getAdjustmentPrice().multiply(BigDecimal.valueOf(3))));
        }
        // 逆向, 则购买方向取反
        if (!TradeContext.getDirectionSwitch()) {
            analyse.negate();
        }
        return analyse;
    }

    /**
     * @description 获取当前k线
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/17 10:59
     **/
    private Kline getKline(SymbolUSDTEnum symbol) {
        List<Kline> klineList = spotMarketService.getKline(symbol, PeriodEnum.MIN_1, 1);
        if (CollectionUtils.isEmpty(klineList)) {
            return null;
        }
        return klineList.get(0);
    }

    /**
     * @description 获取预言信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/16 16:35
     **/
    private Prophecy getProphecy(Kline kline) {
        if (kline == null) {
            return null;
        }
        Integer range = spotMarketService.getRange(kline, kline.getClose());
        if (range == null) {
            return null;
        }
        return prophecyMachine.getProphecy(range);
    }

}


