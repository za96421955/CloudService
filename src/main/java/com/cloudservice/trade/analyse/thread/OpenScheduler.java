package com.cloudservice.trade.analyse.thread;

import com.cloudservice.base.BaseService;
import com.cloudservice.base.Result;
import com.cloudservice.trade.analyse.context.AnalyseContext;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.huobi.enums.SymbolUSDTEnum;
import com.cloudservice.trade.analyse.service.trade.AnalyseServiceFactory;
import com.cloudservice.trade.analyse.service.trade.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 开仓
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
public class OpenScheduler extends BaseService {

    @Autowired
    private AnalyseServiceFactory analyseServiceFactory;
    @Autowired
    private TradeService tradeService;

    //    @Scheduled(cron = "59 0/1 * * * ?")
    public void run() {
        logger.info("[现价分析] ===============================");
        try {
            AnalyseContext.setAnalyse(analyseServiceFactory.getCurr().getAnalyse(SymbolUSDTEnum.ETH_USDT));
            logger.info("[现价分析] analyse={}", AnalyseContext.getAnalyse());
        } catch (Exception e) {
            logger.error("[现价分析] 异常, {}", e.getMessage(), e);
        }

        logger.info("[开仓] ===============================");
        for (Track track : AnalyseContext.getTrackList()) {
            if (track == null || !track.isOpenAllow()) {
                continue;
            }
            try {
                Result result = tradeService.orderOpen(track);
                if (result.success()) {
                    track.setOpenAllow(false);
                    logger.info("[开仓] track={}, result={}, 委托下单成功", track, result);
                }
                logger.error("[开仓] track={}, result={}, 委托下单失败", track, result);
            } catch (Exception e) {
                logger.error("[开仓] track={}, 异常, {}", track, e.getMessage(), e);
            }
        }
    }

}


