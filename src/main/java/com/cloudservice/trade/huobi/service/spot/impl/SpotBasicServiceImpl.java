package com.cloudservice.trade.huobi.service.spot.impl;

import com.cloudservice.base.BaseService;
import com.cloudservice.trade.huobi.context.Host;
import com.cloudservice.trade.huobi.context.SpotAPI;
import com.cloudservice.trade.huobi.model.spot.Symbol;
import com.cloudservice.trade.huobi.service.HuobiHttpRequest;
import com.cloudservice.trade.huobi.service.spot.SpotBasicService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 现货：基础接口
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
@Service
public class SpotBasicServiceImpl extends BaseService implements SpotBasicService {

    @Override
    public List<Symbol> getSymbols() {
        String result = HuobiHttpRequest.get(Host.REST_API, SpotAPI.Basic.SYMBOLS.getApi(), null);
        return Symbol.parseList(this.getDataArray(result));
    }

}


