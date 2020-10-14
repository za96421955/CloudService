package com.cloudservice.plat.context;

import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.plat.thread.CheckStopTradeScheduler;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.huobi.enums.SymbolEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 平台上下文
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
public final class PlatContext {
    private static final Logger logger = LoggerFactory.getLogger(PlatContext.class);

    private PlatContext() {}

    /** 现价追踪 */
    private static final Map<Track, BigDecimal> lePriceTrack = new HashMap<>();
    private static final Map<Track, BigDecimal> gePriceTrack = new HashMap<>();
    public static void setLePriceTrack(Track track, BigDecimal price) {
        if (price == null) {
            lePriceTrack.remove(track);
        } else {
            lePriceTrack.put(track, price);
        }
    }
    public static void setGePriceTrack(Track track, BigDecimal price) {
        if (price == null) {
            gePriceTrack.remove(track);
        } else {
            gePriceTrack.put(track, price);
        }
    }
    public static Set<Track> getTriggerTrack(BigDecimal price) {
        Set<Track> triggerSet = new HashSet<>();
        for (Map.Entry<Track, BigDecimal> le : lePriceTrack.entrySet()) {
            if (le == null || le.getKey() == null || le.getValue() == null) {
                continue;
            }
            if (price.compareTo(le.getValue()) <= 0) {
                triggerSet.add(le.getKey());
            }
        }
        for (Map.Entry<Track, BigDecimal> ge : gePriceTrack.entrySet()) {
            if (ge == null || ge.getKey() == null || ge.getValue() == null) {
                continue;
            }
            if (price.compareTo(ge.getValue()) >= 0) {
                triggerSet.add(ge.getKey());
            }
        }
        if (triggerSet.size() > 0) {
            logger.info("[现价追踪] LE={}, GE={}, trigger={}, 获取被触发追踪"
                    , lePriceTrack.values(), gePriceTrack.values(), triggerSet);
        } else {
            logger.debug("[现价追踪] LE={}, GE={}, trigger={}, 获取被触发追踪"
                    , lePriceTrack.values(), gePriceTrack.values(), triggerSet);
        }
        return triggerSet;
    }

    /** 委托交易 */
    private static final Map<String, Track> trackMap = new HashMap<>();
    public static List<Track> getTrackList() {
        return new ArrayList<>(trackMap.values());
    }
    public static Track getTrack(String access, SymbolEnum symbol, String hedgeType) {
        String key = access + "-" + symbol.getValue() + "-" + hedgeType;
        return trackMap.get(key);
    }
    public synchronized static void setTrack(Track track) {
        if (track == null) {
            return;
        }
        // 设置交易是否停止
        track.setStopTrade(CheckStopTradeScheduler.isStopTrade());
        // 记录缓存
        String key = track.getAccess() + "-" + track.getSymbol().getValue() + "-" + track.getHedgeType();
        trackMap.put(key, track);
    }

    /** 策略配置 */
    private static final Map<StrategyTypeEnum, List<HedgeConfig>> hedgeStrategyMap = new HashMap<>();
    public static Map<StrategyTypeEnum, List<HedgeConfig>> getHedgeStrategyMap() {
        return hedgeStrategyMap;
    }
    public static List<HedgeConfig> getHedgeStrategyList(StrategyTypeEnum strategyType) {
        List<HedgeConfig> cfgList = hedgeStrategyMap.get(strategyType);
        if (CollectionUtils.isEmpty(cfgList)) {
            hedgeStrategyMap.put(strategyType, new ArrayList<>());
            cfgList = hedgeStrategyMap.get(strategyType);
        }
        return cfgList;
    }
    public static HedgeConfig getHedgeStrategy(StrategyTypeEnum strategyType) {
        List<HedgeConfig> cfgList = getHedgeStrategyList(strategyType);
        if (CollectionUtils.isEmpty(cfgList)) {
            return null;
        }
        return cfgList.get(0);
    }
    public synchronized static void setHedgeStrategy(HedgeConfig hedgeConfig) {
        if (hedgeConfig == null) {
            return;
        }
        getHedgeStrategyList(hedgeConfig.getStrategyType()).add(hedgeConfig);
    }

}


