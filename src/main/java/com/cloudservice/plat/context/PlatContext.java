package com.cloudservice.plat.context;

import com.cloudservice.plat.enums.StrategyTypeEnum;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.huobi.enums.SymbolEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平台上下文
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
public final class PlatContext {

    private PlatContext() {}

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
        String key = track.getAccess() + "-" + track.getSymbol().getValue() + "-" + track.getHedgeConfig().getHedgeType();
        trackMap.put(key, track);
    }

    /** 策略配置 */
    private static final Map<StrategyTypeEnum, HedgeConfig> hedgeStrategyMap = new HashMap<>();
    public static List<HedgeConfig> getHedgeStrategyList() {
        return new ArrayList<>(hedgeStrategyMap.values());
    }
    public static HedgeConfig getHedgeStrategy(StrategyTypeEnum strategyType) {
        return hedgeStrategyMap.get(strategyType);
    }
    public synchronized static void setHedgeStrategy(HedgeConfig hedgeConfig) {
        if (hedgeConfig == null) {
            return;
        }
        hedgeStrategyMap.put(hedgeConfig.getStrategyType(), hedgeConfig);
    }

}

