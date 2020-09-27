package com.cloudservice.trade.analyse.service.trade;

import com.cloudservice.trade.analyse.model.trade.Analyse;
import com.cloudservice.trade.huobi.enums.ContractDirectionEnum;
import com.cloudservice.trade.huobi.enums.SymbolUSDTEnum;

/**
 * 现价分析
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/10
 */
public interface AnalyseService {
    String LOG_MARK = "现价分析";

    /**
     * @description 趋势分析
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/15 17:43
     * @param symbol
     **/
    ContractDirectionEnum getDirection(SymbolUSDTEnum symbol);

    /**
     * @description 现价分析
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/15 17:29
     * @param symbol
     **/
    Analyse getAnalyse(SymbolUSDTEnum symbol);

}


