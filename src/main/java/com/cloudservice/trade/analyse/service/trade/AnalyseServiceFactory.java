package com.cloudservice.trade.analyse.service.trade;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 分析服务工厂
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/10
 */
@Service
public final class AnalyseServiceFactory {
    public static final String TYPE_DEPTH = "depth";
    public static final String TYPE_CYCLE = "cycle";

    @Resource(name = "depthAnalyseServiceImpl")
    private AnalyseService depthAnalyseService;
    @Resource(name = "cycleAnalyseServiceImpl")
    private AnalyseService cycleAnalyseService;

    /**
     * @description 获取指定类型分析服务
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/15 17:53
     * @param type
     **/
    public AnalyseService getService(String type) {
        if (TYPE_DEPTH.equals(type)) {
            return depthAnalyseService;
        }
        if (TYPE_CYCLE.equals(type)) {
            return cycleAnalyseService;
        }
        throw new RuntimeException("service type[" + type + "] not found");
    }

    /**
     * @description 获取当前分析服务
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/15 17:53
     **/
    public AnalyseService getCurr() {
        return this.getService(TYPE_CYCLE);
    }

}


