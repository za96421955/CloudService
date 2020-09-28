package com.cloudservice.plat.service;

import com.cloudservice.trade.hedge.model.HedgeConfig;
import com.cloudservice.trade.hedge.model.Track;

import java.util.List;

/**
 * 文件服务
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/28
 */
public interface FileService {

    /**
     * @description 记录追踪信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/28 21:30
     * @param track
     **/
    void writeTrack(Track track);

    /**
     * @description 读取追踪信息集合
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/28 21:30
     **/
    List<Track> readTrackList();

    /**
     * @description 记录配置信息
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/28 21:30
     * @param hedgeConfig
     **/
    void writeHedgeConfig(HedgeConfig hedgeConfig);

    /**
     * @description 读取配置信息集合
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/28 21:30
     **/
    List<HedgeConfig> readHedgeConfigList();

}


