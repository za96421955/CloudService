package com.cloudservice.plat.thread;

import com.cloudservice.base.BaseService;
import com.cloudservice.base.Result;
import com.cloudservice.plat.context.PlatContext;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.hedge.service.HedgeService;
import com.cloudservice.trade.hedge.service.HedgeServiceFactory;
import com.cloudservice.trade.huobi.model.contract.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对冲追踪JOB
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
    private static Map<String, Thread> threadPool = new HashMap<>();

    @Autowired
    private HedgeServiceFactory hedgeServiceFactory;

    @Scheduled(cron = "0/5 * * * * ?")
    public void run() {
        for (Track track : PlatContext.getTrackList()) {
            if (track == null) {
                continue;
            }
            // 获取线程, 若线程正在运行, 则跳过
            Thread thread = threadPool.get(track.getAccess());
            if (thread != null) {
                continue;
            }
            // 生成工作线程
            threadPool.put(track.getAccess(), new Thread(() -> {
                try {
                    // 1, 获取对冲服务
                    HedgeService service = hedgeServiceFactory.getHedgeService(track.getHedgeType());
                    // 2, 设置对冲策略
                    service.setStrategy(track);
                    // 3, 持仓检查
                    Result result = service.positionCheck(track);
                    if (result.success()) {
                        Object[] positions = result.getData();
                        Position buy = (Position) positions[0];
                        Position sell = (Position) positions[1];
                        // 4, 双向平仓检查
                        service.closeCheck(track, buy, sell);
                    } else {
                        logger.info("[对冲追踪] track={}, result={}, 持仓检查未通过, 无持仓信息", track, result);
                    }
                } catch (Exception e) {
                    logger.error("[对冲追踪] track={}, 异常, {}", track, e.getMessage(), e);
                }
                // 清除工作线程
                threadPool.put(track.getAccess(), null);
            }));
            // 执行工作线程
            threadPool.get(track.getAccess()).start();
        }
    }

}


