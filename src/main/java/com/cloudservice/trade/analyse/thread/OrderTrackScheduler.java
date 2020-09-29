package com.cloudservice.trade.analyse.thread;

import com.cloudservice.base.BaseService;
import com.cloudservice.base.Result;
import com.cloudservice.trade.analyse.context.AnalyseContext;
import com.cloudservice.trade.analyse.model.trade.AnalyseTrack;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.analyse.service.trade.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单追踪
 * <p>
 *     cron: * * * * * * *
 *     cron: 秒 分 时 日 月 周 年
 * </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/16
 */
@Component
public class OrderTrackScheduler extends BaseService {

    @Autowired
    private TradeService tradeService;

    //    @Scheduled(cron = "0/5 * * * * ?")
    public void run() {
//        logger.info("[订单追踪] ===============================");
        for (AnalyseTrack track : AnalyseContext.getTrackList()) {
            if (track == null) {
                continue;
            }
            try {
                // 1, 开仓检查
                Result result = tradeService.checkOpen(track);
                logger.info("[订单追踪] track={}, result={}, 开仓检查 End", track, result);
                if (result.success()) {
                    // 2, 允许下单
                    track.setOpenAllow(true);
                    logger.info("[订单追踪] track={}, 允许下单", track);
                } else {
                    // 3, 撤单检查
                    result = tradeService.checkCancel(track);
                    logger.info("[订单追踪] track={}, result={}, 撤单检查 End", track, result);
                    // 4, 平仓追踪
                    result = tradeService.closeTrack(track);
                    logger.info("[订单追踪] track={}, result={}, 平仓追踪 End", track, result);
                }
            } catch (Exception e) {
                logger.error("[{}] track={}, 异常, {}", track, e.getMessage(), e);
            }
        }
    }

}


