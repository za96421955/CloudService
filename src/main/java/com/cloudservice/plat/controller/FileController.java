package com.cloudservice.plat.controller;

import com.cloudservice.base.BaseController;
import com.cloudservice.base.Result;
import com.cloudservice.plat.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器：文件
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/9
 */
@RestController
@RequestMapping("/file")
public class FileController extends BaseController {

    @Autowired
    private FileService fileService;

    @PostMapping("/track/write")
    @Description("记录追踪信息")
    public Result writeTrack() {
        try {
//            Track track = new Track();
//            track.setAccess("access");
//            track.setSymbol(SymbolEnum.ETH);
//            track.setHedgeType(HedgeServiceFactory.CONTRACT);
//            track.setHedgeConfig(PlatContext.getHedgeStrategyList(StrategyTypeEnum.FIXED_BASIS).get(0));
//            fileService.writeTrack(track);
            return Result.buildSuccess();
        } catch (Exception e) {
            logger.error("[文件] 记录追踪信息异常, {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/track/read")
    @Description("读取追踪信息集合")
    public Result readTrackList() {
        try {
            return Result.buildSuccess(fileService.readTrackList());
        } catch (Exception e) {
            logger.error("[文件] 读取追踪信息集合异常, {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/cfg/write")
    @Description("记录配置信息")
    public Result writeHedgeConfig() {
        try {
//            HedgeConfig hedgeConfig = PlatContext.getHedgeStrategyList(StrategyTypeEnum.FIXED_BASIS).get(0);
//            fileService.writeHedgeConfig(hedgeConfig);
            return Result.buildSuccess();
        } catch (Exception e) {
            logger.error("[文件] 记录配置信息异常, {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/cfg/read")
    @Description("读取配置信息集合")
    public Result readHedgeConfigList() {
        try {
            return Result.buildSuccess(fileService.readHedgeConfigList());
        } catch (Exception e) {
            logger.error("[文件] 读取配置信息集合异常, {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

}


