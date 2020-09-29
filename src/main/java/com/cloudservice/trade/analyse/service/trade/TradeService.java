package com.cloudservice.trade.analyse.service.trade;

import com.cloudservice.base.Result;
import com.cloudservice.trade.analyse.model.trade.AnalyseTrack;
import com.cloudservice.trade.hedge.model.Track;


/**
 * 交易服务
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/14
 */
public interface TradeService {
    String LOG_MARK = "交易服务";

    /** 开仓限价委托, 成交超时时间, 3分钟 */
    long OPEN_TIMEOUT = 3 * 60 * 1000;
    /** 撤单后, 禁止反向开仓时间 */
    long CANCEL_DISABLE_TIME = 10 * 60 * 1000;
    /** 平仓止盈后, 禁止同向追仓时间 */
    long CHASE_DISABLE_TIME = 10 * 60 * 1000;

    /**
     * @description 开仓检查
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/14 10:32
     * @param track
     **/
    Result checkOpen(AnalyseTrack track);

    /**
     * @description 委托下单
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/14 10:32
     * @param track
     */
    Result orderOpen(AnalyseTrack track);

    /**
     * @description 撤单检查
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/14 10:32
     * @param track
     **/
    Result checkCancel(AnalyseTrack track);

    /**
     * @description 平仓追踪
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/14 10:32
     * @param track
     **/
    Result closeTrack(AnalyseTrack track);

}

