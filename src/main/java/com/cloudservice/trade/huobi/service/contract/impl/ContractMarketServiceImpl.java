package com.cloudservice.trade.huobi.service.contract.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.BaseService;
import com.cloudservice.trade.huobi.context.ContractAPI;
import com.cloudservice.trade.huobi.context.Host;
import com.cloudservice.trade.huobi.enums.PeriodEnum;
import com.cloudservice.trade.huobi.enums.SymbolContractEnum;
import com.cloudservice.trade.huobi.model.spot.Kline;
import com.cloudservice.trade.huobi.service.HuobiHttpRequest;
import com.cloudservice.trade.huobi.service.contract.ContractMarketService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 交割合约：市场接口实现
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/28
 */
@Service
public class ContractMarketServiceImpl extends BaseService implements ContractMarketService {

    @Override
    public List<Kline> getKline(SymbolContractEnum symbol, PeriodEnum period, int size) {
        StringBuilder data = new StringBuilder();
        data.append("symbol=").append(symbol.getValue());
        data.append("&period=").append(period.getValue());
        data.append("&size=").append(size);
        String result = HuobiHttpRequest.get(Host.CONTRACT, ContractAPI.Market.KLINE.getApi(), data.toString());
        return Kline.parseList(JSONObject.parseObject(result).getJSONArray("data"));
    }

    @Override
    public Kline getKlineCurr(SymbolContractEnum symbol) {
        List<Kline> klineList = this.getKline(symbol, PeriodEnum.MIN_1, 1);
        if (CollectionUtils.isEmpty(klineList)) {
            return null;
        }
        return klineList.get(0);
    }

}


