package com.cloudservice.plat.service.strategys;

import java.util.List;

/**
 * 策略API
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/29
 */
public interface StrategyAPI<T> {

    /**
     * @description 获取策略
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/29 17:26
     **/
    List<T> getStrategyList();

}
