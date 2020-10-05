package com.cloudservice.plat.service.impl;

import com.cloudservice.base.BaseService;
import com.cloudservice.plat.service.FileService;
import com.cloudservice.trade.hedge.model.HedgeConfig;
import com.cloudservice.trade.hedge.model.Track;
import com.cloudservice.utils.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 文件服务实现
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
@Service
public class FileServiceImpl extends BaseService implements FileService {
    private static final String CHARSET = "UTF-8";
    private static final String PATH_TRACK = "track.info";
    private static final String PATH_HEDGE_CONFIG = "hedgeConfig.info";

    @Override
    public void flushAll() {

    }

    @Override
    public void writeTrack(Track track) {
        if (track == null) {
            return;
        }
        // 读取文件
        List<Track> trackList = this.readTrackList();
        // 删除文件
        File file = new File(PATH_TRACK);
        if (file.exists()) {
            file.delete();
        }
        // 重新全部写入
        boolean isWrite = false;
        for (Track writeTrack : trackList) {
            if (writeTrack == null) {
                continue;
            }
            if (writeTrack.equals(track)) {
                writeTrack = track;
                isWrite = true;
            }
            FileUtil.append(writeTrack.toJson().toString() + "\n", PATH_TRACK, CHARSET);
        }
        if (!isWrite) {
            FileUtil.append(track.toJson().toString() + "\n", PATH_TRACK, CHARSET);
        }
    }

    @Override
    public List<Track> readTrackList() {
        File file = new File(PATH_TRACK);
        if (!file.exists()) {
            return Collections.emptyList();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), CHARSET));
            String line;
            List<Track> trackList = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                trackList.add(new Track().fromJson(line));
            }
            reader.close();
            return trackList;
        } catch (Exception e) {
            logger.error("[{}] path={}, 文件读取异常, {}", LOG_MARK, PATH_TRACK, e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public void writeHedgeConfig(HedgeConfig hedgeConfig) {
        if (hedgeConfig == null) {
            return;
        }
        FileUtil.append(hedgeConfig.toJson().toString() + "\n", PATH_HEDGE_CONFIG, CHARSET);
//        // 读取文件
//        List<HedgeConfig> cfgList = this.readHedgeConfigList();
//        // 删除文件
//        File file = new File(PATH_HEDGE_CONFIG);
//        if (file.exists()) {
//            file.delete();
//        }
//        // 重新全部写入
//        boolean isWrite = false;
//        for (HedgeConfig writeCfg : cfgList) {
//            if (writeCfg == null) {
//                continue;
//            }
//            if (writeCfg.equals(hedgeConfig)) {
//                writeCfg = hedgeConfig;
//                isWrite = true;
//            }
//            FileUtil.append(writeCfg.toJson().toString() + "\n", PATH_HEDGE_CONFIG, CHARSET);
//        }
//        if (!isWrite) {
//            FileUtil.append(hedgeConfig.toJson().toString() + "\n", PATH_HEDGE_CONFIG, CHARSET);
//        }
    }

    @Override
    public List<HedgeConfig> readHedgeConfigList() {
        File file = new File(PATH_HEDGE_CONFIG);
        if (!file.exists()) {
            return Collections.emptyList();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), CHARSET));
            String line;
            List<HedgeConfig> cfgList = new ArrayList<>();
            HedgeConfig tool = new HedgeConfig();
            while ((line = reader.readLine()) != null) {
                cfgList.add(tool.fromJson(line));
            }
            reader.close();
            return cfgList;
        } catch (Exception e) {
            logger.error("[{}] path={}, 文件读取异常, {}", LOG_MARK, PATH_HEDGE_CONFIG, e.getMessage(), e);
            // 删除文件
            file.delete();
        }
        return Collections.emptyList();
    }

}


