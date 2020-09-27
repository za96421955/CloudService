package com.cloudservice.utils;
/*
 * Copyright (C), 2002-2018, 苏宁易购电子商务有限公司
 * FileName: com.suning.logistics.jwms.utils
 * Author:   陈晨(17061934)
 * Date:     2019/12/30 15:51
 * Description: // 模块目的、功能描述      
 * History:		// 修改记录
 * <author>		<time>		<version>		<desc>
 * 17061934		2019/12/30 15:51		1.0		<描述>
 */

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * @version v1.0
 * @Description: Hash工具
 * <p>〈功能详细描述〉</p>
 *
 * @ClassName HashUtil
 * @author 陈晨(17061934)
 * @date 2019/12/30 15:51
 */
public final class HashUtil {
    private static Logger logger = LoggerFactory.getLogger(HashUtil.class);

    private static final String ASSIGN_HASH_KEY = "@AssignHashKey@";

    private static final int NUM0 = 0;
    private static final int NUM1 = 1;
    private static final int NUM2 = 2;
    private static final int NUM3 = 3;
    private static final int NUM4 = 4;
    private static final int NUM8 = 8;
    private static final int NUM16 = 16;
    private static final int NUM24 = 24;
    private static final int NUM_0XFF = 0xFF;

    private HashUtil() {}

    private static long hash(byte[] digest, int nTime) {
        long rv = ((long) (digest[NUM3 + nTime * NUM4] & NUM_0XFF) << NUM24)
                | ((long) (digest[NUM2 + nTime * NUM4] & NUM_0XFF) << NUM16)
                | ((long) (digest[NUM1 + nTime * NUM4] & NUM_0XFF) << NUM8)
                | (digest[NUM0 + nTime * NUM4] & NUM_0XFF);
        return rv & 0xffffffffL; /* Truncate to 32-bits */
    }

    /**
     * DalClient 内部Hash算法
     * <p>提取源码, 添加捕获异常</p>
     *
     * @param k
     * @param n
     * @return
     * @author 陈晨(17061934)
     */
    public static long hash(String k, int n) {
        if (StringUtils.isBlank(k) || n <= 0) {
            return 0;
        }

        // 指定Hash
        if (k.contains(ASSIGN_HASH_KEY)) {
            return Long.parseLong(k.replaceAll(ASSIGN_HASH_KEY, ""));
        }

        // 正常Hash
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(k.getBytes("UTF-8"));
            byte[] digest = md5.digest();
            return hash(digest, 0) % n;
        } catch (Exception e) {
            logger.error("MD5 exception, {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @description 指定Hash
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨(17061934)
     * @date    2019/11/14 20:57
     * @param   index
     */
    public static String getAssginHashKey(long index) {
        return ASSIGN_HASH_KEY + index;
    }

}


