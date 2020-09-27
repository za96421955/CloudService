package com.cloudservice.trade.analyse.service.trade.impl.analyse;

import com.cloudservice.base.BaseService;
import com.cloudservice.trade.analyse.service.trade.AnalyseService;
import com.cloudservice.trade.huobi.enums.ContractDirectionEnum;
import com.cloudservice.trade.huobi.model.spot.DepthDetail;

/**
 * 现价分析: 抽象服务
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/10
 */
public abstract class AbstractAnalyseServiceImpl extends BaseService implements AnalyseService {

    /**
     * @description 获取买/卖
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/14 17:25
     **/
    protected ContractDirectionEnum getDirection(DepthDetail buy, DepthDetail sell, DepthDetail compare) {
        if (buy == null || sell == null || compare == null) {
            return null;
        }
        if (compare.equals(buy)) {
            return ContractDirectionEnum.BUY;
        } else {
            return ContractDirectionEnum.SELL;
        }
    }

}


