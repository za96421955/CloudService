package com.cloudservice.trade.analyse.context;

import com.cloudservice.trade.analyse.model.trade.Analyse;
import com.cloudservice.trade.analyse.model.trade.AnalyseTrack;
import com.cloudservice.trade.hedge.model.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易上下文
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/11
 */
public final class AnalyseContext {

    private AnalyseContext() {}

    /** 方向切换, 默认正向 */
    // TODO 逆向
    private static boolean directionSwitch = false;
    public static boolean getDirectionSwitch() {
        return directionSwitch;
    }
    public static void setDirectionSwitch(boolean directionSwitch) {
        AnalyseContext.directionSwitch = directionSwitch;
    }

    /** 限价分析 */
    private static Analyse analyse;
    public static Analyse getAnalyse() {
        return analyse;
    }
    public static void setAnalyse(Analyse analyse) {
        AnalyseContext.analyse = analyse;
    }

    /** 订单追踪 */
    private static final Map<String, AnalyseTrack> trackMap = new HashMap<>();
    public synchronized static AnalyseTrack getTrack(String access) {
        AnalyseTrack track = trackMap.get(access);
        if (track == null) {
            trackMap.put(access, new AnalyseTrack(access, null));
            track = trackMap.get(access);
        }
        return track;
    }
    public static List<AnalyseTrack> getTrackList() {
        return new ArrayList<>(trackMap.values());
    }

}


