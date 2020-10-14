package com.cloudservice.plat.thread;

import com.cloudservice.base.BaseService;
import com.cloudservice.base.Result;
import com.cloudservice.plat.context.PlatContext;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.hedge.service.HedgeService;
import com.cloudservice.trade.hedge.service.HedgeServiceFactory;
import com.cloudservice.trade.huobi.model.contract.Position;
import com.cloudservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 停止交易检查JOB
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
public class CheckStopTradeScheduler extends BaseService {

    @Scheduled(cron = "0 0/1 * * * ?")
    public void run() {
        /**
         * 周4 16:30:00 - 周五 16:30:00
         * 交割合约停止交易
         * 现有持仓缓冲清仓
         */
        boolean isStopTrade = isStopTrade();
        for (Track track : PlatContext.getTrackList()) {
            if (track == null || track.getDiyConfig().getStopTrade() != null) {
                continue;
            }
            track.setStopTrade(isStopTrade);
        }
    }

    /**
     * @description 获取交易是否停止
     * <p>〈功能详细描述〉</p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/1 23:10
     */
    public static boolean isStopTrade() {
        int week = DateUtil.week();
        if (!(week == 4 || week == 5)) {
            return false;
        }
        // 计算停止交易起止时间
        Date beginTime = DateUtil.parse(
                DateUtil.format(DateUtil.modDay(DateUtil.now()), DateUtil.DATE) + " 16:30:00"
                , DateUtil.DATE_LONG);
        Date endTime = new Date(beginTime.getTime());
        if (week == 4) {
            endTime = DateUtil.addDay(beginTime, 1);
        } else {
            beginTime = DateUtil.addDay(beginTime, -1);
        }
        return System.currentTimeMillis() >= beginTime.getTime()
                && System.currentTimeMillis() <= endTime.getTime();
    }

    public static void main(String[] args) {
        System.out.println(isStopTrade());
    }

}


