package com.cloudservice.trade.hedge.thread;

import com.cloudservice.base.BaseService;
import com.cloudservice.base.Result;
import com.cloudservice.trade.analyse.context.TradeContext;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.hedge.service.HedgeService;
import com.cloudservice.trade.hedge.service.HedgeServiceFactory;
import com.cloudservice.trade.huobi.model.contract.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 对冲追踪
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
public class HedgeTrackScheduler extends BaseService {

    @Autowired
    private HedgeServiceFactory hedgeServiceFactory;

//    @Scheduled(cron = "0/5 * * * * ?")
    public void run() {
        for (Track track : TradeContext.getTrackList()) {
            if (track == null) {
                continue;
            }
            try {
                // 获取对冲服务
                HedgeService service = hedgeServiceFactory.getHedgeService(track.getHedgeConfig().getHedgeType());
                // 1, 持仓检查
                Result result = service.positionCheck(track);
                if (result.success()) {
                    Object[] positions = result.getData();
                    Position buy = (Position) positions[0];
                    Position sell = (Position) positions[1];
                    // 2, 双向平仓检查
                    service.closeCheck(track, buy, sell);
                } else {
                    logger.info("[对冲追踪] track={}, result={}, 持仓检查未通过, 无持仓信息", track, result);
                }
            } catch (Exception e) {
                logger.error("[对冲追踪] track={}, 异常, {}", track, e.getMessage(), e);
            }
        }
    }

}


