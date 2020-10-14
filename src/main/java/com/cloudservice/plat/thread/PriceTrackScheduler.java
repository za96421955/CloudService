package com.cloudservice.plat.thread;

import com.cloudservice.base.BaseService;
import com.cloudservice.plat.context.PlatContext;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.huobi.enums.ContractTypeEnum;
import com.cloudservice.trade.huobi.enums.SymbolContractEnum;
import com.cloudservice.trade.huobi.enums.SymbolEnum;
import com.cloudservice.trade.huobi.model.spot.Kline;
import com.cloudservice.trade.huobi.service.contract.ContractMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * 现价追踪
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
public class PriceTrackScheduler extends BaseService {
    private static Thread thread;

    @Autowired
    private ContractMarketService contractMarketService;
    @Autowired
    private HedgeTrackScheduler hedgeTrackScheduler;

    public void run() {
        if (thread != null) {
            return;
        }
        thread = new Thread(() -> {
            while (true) {
                try {
                    // 追踪
                    this.triggerTrack();
                    // 休眠, 1秒5次
                    this.sleep(200);
                } catch (Exception e) {
                    logger.error("[现价追踪] 现价追踪异常, {}", e.getMessage(), e);
                }
            }
        });
        thread.start();
    }

    /**
     * @description 触发追踪
     * <p>〈功能详细描述〉</p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/5 23:25
     */
    private void triggerTrack() {
        // 当季
        Kline kline = contractMarketService.getKlineCurr(SymbolContractEnum.get(SymbolEnum.ETH, ContractTypeEnum.QUARTER));
        if (kline == null) {
            return;
        }
        Set<Track> triggerSet = PlatContext.getTriggerTrack(kline.getClose());
        if (CollectionUtils.isEmpty(triggerSet)) {
            return;
        }
        for (Track track : triggerSet) {
            if (track == null || !ContractTypeEnum.QUARTER.equals(track.getContractType())) {
                continue;
            }
            hedgeTrackScheduler.runTrack(track);
        }

        // 当周
        kline = contractMarketService.getKlineCurr(SymbolContractEnum.get(SymbolEnum.ETH, ContractTypeEnum.THIS_WEEK));
        if (kline == null) {
            return;
        }
        triggerSet = PlatContext.getTriggerTrack(kline.getClose());
        if (CollectionUtils.isEmpty(triggerSet)) {
            return;
        }
        for (Track track : triggerSet) {
            if (track == null || !ContractTypeEnum.THIS_WEEK.equals(track.getContractType())) {
                continue;
            }
            hedgeTrackScheduler.runTrack(track);
        }
    }

}


