/*
 * Copyright (C), 2002-2014, 苏宁易购电子商务有限公司
 * FileName: ArraySortUtil.java
 * Author:  15050768/纪重天
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.cloudservice.utils;

import java.util.UUID;

/**
 * 生成GUID
 */
public final class GuidUtil {

    private GuidUtil() {}

    /**
     * 生成器32位UUID
     */
    public static synchronized String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8)
            + uuid.substring(9, 13)
            + uuid.substring(14, 18)
            + uuid.substring(19, 23)
            + uuid.substring(24, 36);
    }

}


