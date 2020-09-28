package com.cloudservice.trade.huobi.service.swap.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloudservice.base.BaseService;
import com.cloudservice.trade.huobi.context.Host;
import com.cloudservice.trade.huobi.context.SwapAPI;
import com.cloudservice.trade.huobi.enums.ContractCodeEnum;
import com.cloudservice.trade.huobi.enums.PeriodEnum;
import com.cloudservice.trade.huobi.model.spot.Kline;
import com.cloudservice.trade.huobi.service.HuobiHttpRequest;
import com.cloudservice.trade.huobi.service.swap.SwapMarketService;
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
public class SwapMarketServiceImpl extends BaseService implements SwapMarketService {

    @Override
    public List<Kline> getKline(ContractCodeEnum contractCode, PeriodEnum period, int size) {
        StringBuilder data = new StringBuilder();
        data.append("symbol=").append(contractCode.getValue());
        data.append("&period=").append(period.getValue());
        data.append("&size=").append(size);
        String result = HuobiHttpRequest.get(Host.CONTRACT, SwapAPI.Market.KLINE.getApi(), data.toString());
        return Kline.parseList(JSONObject.parseObject(result).getJSONArray("data"));
    }

    @Override
    public Kline getKlineCurr(ContractCodeEnum contractCode) {
        List<Kline> klineList = this.getKline(contractCode, PeriodEnum.MIN_1, 1);
        if (CollectionUtils.isEmpty(klineList)) {
            return null;
        }
        return klineList.get(0);
    }

}


