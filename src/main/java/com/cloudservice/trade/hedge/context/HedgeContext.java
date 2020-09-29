package com.cloudservice.trade.hedge.context;

import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.trade.huobi.enums.SymbolEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上下文
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/11
 */
public final class HedgeContext {

    private HedgeContext() {}

    /** 订单追踪 */
    private static final Map<String, Track> trackMap = new HashMap<>();
    public synchronized static Track getTrack(String access, SymbolEnum symbol, String hedgeType) {
        String key = access + "-" + symbol.getValue() + "-" + hedgeType;
        Track track = trackMap.get(key);
        if (track == null) {
            trackMap.put(key, new Track(access, null));
            track = trackMap.get(key);
            track.setSymbol(symbol);
            track.setHedgeType(hedgeType);
        }
        return track;
    }
    public static List<Track> getTrackList() {
        return new ArrayList<>(trackMap.values());
    }

}


