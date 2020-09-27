package com.cloudservice.trade.huobi.service.spot;

import com.cloudservice.trade.huobi.model.spot.Symbol;

import java.util.List;

/**
 * 现货：基础接口
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/8
 */
public interface SpotBasicService {

    /**
     * @description 获取所有交易对
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 15:23
     *
     * @return*/
    List<Symbol> getSymbols();

}


