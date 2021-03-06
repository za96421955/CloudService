package com.cloudservice.trade.huobi.service.contract;

import com.cloudservice.trade.huobi.enums.PeriodEnum;
import com.cloudservice.trade.huobi.enums.SymbolContractEnum;
import com.cloudservice.trade.huobi.model.spot.Kline;

import java.util.List;

/**
 * 交割合约：市场接口
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/5
 */
public interface ContractMarketService {

    /**
     * @description 获取K线数据
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 14:52
     * @param symbol, period, size
     *
     * @param symbol
     * @return*/
    List<Kline> getKline(SymbolContractEnum symbol, PeriodEnum period, int size);

    /**
     * @description 获取当前K线信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/18 14:18
     * @param symbol
     **/
    Kline getKlineCurr(SymbolContractEnum symbol);

}


