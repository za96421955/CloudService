package com.cloudservice.trade.hedge.service;

import com.cloudservice.base.Result;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.huobi.model.contract.Position;

/**
 * 对冲服务
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/24
 */
public interface HedgeService {
    String LOG_MARK = "对冲服务";

    /**
     * @description 设置对冲策略
     * <p>〈功能详细描述〉</p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/5 21:23
     * @param   track
     */
    void setStrategy(Track track);

    /**
     * @description 持仓检查
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/24 15:05
     * @param track
     **/
    Result positionCheck(Track track);

    /**
     * @description 双向平仓检查
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/25 14:13
     * @param track, buy, sell
     **/
    void closeCheck(Track track, Position buy, Position sell);

}


