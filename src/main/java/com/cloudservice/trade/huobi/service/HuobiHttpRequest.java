package com.cloudservice.trade.huobi.service;

import com.cloudservice.utils.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 火币HTTP请求
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/9/3
 */
public final class HuobiHttpRequest {
    protected static final Logger logger = LoggerFactory.getLogger(HuobiHttpRequest.class);

    private static final String SIGNATURE_METHOD = "HmacSHA256";
    private static final String SIGNATURE_VERSION = "2";

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String CONTENT_TYPE_JSON = "application/json";

    private HuobiHttpRequest() {}

    /**
     * @description 发送Get请求
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 14:07
     * @param host, api, data
     **/
    public static String get(String host, String api, String data) {
        try {
            return request(null, null, GET, host, api, data, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description 发送加密Get请求
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/3 17:54
     * @param host, api, data
     **/
    public static String get(String access, String secret, String host, String api) {
        try {
            return request(access, secret, GET, host, api, null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description 发送Post请求
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/3 17:54
     * @param host, api, data
     **/
    public static String post(String host, String api, String data) {
        try {
            return request(null, null, POST, host, api, data, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description 发送加密Post请求
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/3 17:54
     * @param host, api, data
     **/
    public static String post(String access, String secret, String host, String api, String data) {
        try {
            return request(access, secret, POST, host, api, data, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description 发送HTTP请求
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/3 17:52
     * @param access, secret, method, host, api, data, sign
     **/
    public static String request(String access, String secret, String method, String host, String api, String data, boolean sign) throws Exception {
//        logger.info("[HTTP] access={}, secret={}, method={}, host={}, api={}, data={}, sign={}", access, secret, method, host, api, data, sign);
        // 获取请求地址
        String url = getRequestUrl(host, api, data);
        if (sign) {
            url = getSignRequestUrl(access, secret, method, host, api);
        }

        // 打印curl请求信息
        StringBuilder curl = new StringBuilder("curl");
        if (POST.equals(method)) {
            curl.append(" -H ").append("\"Content-Type: ").append(CONTENT_TYPE_JSON).append("\"");
            curl.append(" -X ").append("POST");
            curl.append(" -d '").append(data).append("'");
        }
        curl.append(" \"").append(url).append("\"");
        logger.debug("[HTTP] Request: {}", curl);

        // 构建请求对象
        HttpUriRequest uriRequest;
        if (GET.equals(method)) {
            uriRequest = new HttpGet(url);
        } else {
            HttpPost post = new HttpPost(url);
            if (StringUtils.isNotBlank(data)) {
                post.addHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_JSON);
                post.setEntity(new StringEntity(data));
            }
            uriRequest = post;
        }

        // 发送请求, 获取响应信息
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse resp = client.execute(uriRequest);
        HttpEntity entity = resp.getEntity();
        String result = EntityUtils.toString(entity);
//        logger.info("[HTTP] result={}", result);
        return result;
    }

    /**
     * @description 获取请求地址
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/8 14:00
     * @param host, api, data
     **/
    public static String getRequestUrl(String host, String api, String data) {
        if (StringUtils.isBlank(data)) {
            return host + api;
        }
        return host + api + "?" + data;
    }

    /**
     * @description 获取秘钥请求地址
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/3 17:49
     * @param method, host, api
     **/
    public static String getSignRequestUrl(String access, String secret, String method, String host, String api) {
        try {
            // 设置请求参数
            StringBuilder params = new StringBuilder();
            params.append("AccessKeyId=").append(access);
            params.append("&SignatureMethod=").append(SIGNATURE_METHOD);
            params.append("&SignatureVersion=").append(SIGNATURE_VERSION);
            params.append("&Timestamp=").append(URLEncoder.encode(getTimestamp(), "UTF-8"));
            // 计算秘钥
            StringBuilder requestInfo = new StringBuilder();
            requestInfo.append(method).append("\n");
            requestInfo.append(host.replaceFirst("http[s]?://", "")).append("\n");
            requestInfo.append(api).append("\n");
            requestInfo.append(params.toString());
            String signature = getSignature(secret, requestInfo.toString());
            // 请求参数添加秘钥
            params.append("&Signature=").append(URLEncoder.encode(signature, "UTF-8"));
            // 返回请求url
            return getRequestUrl(host, api, params.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description 获取时间戳
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/3 17:47
     **/
    public static String getTimestamp() {
        return DateUtil.format(DateUtil.addHour(DateUtil.now(), -8), DateUtil.DATE_LONG).replace(" ", "T");
    }

    /**
     * @description 获取密文
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/9/3 17:27
     **/
    public static String getSignature(String secret, String requestInfo) {
        Mac hmacSha256;
        try {
            hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSha256.init(secKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("[Signature] No such algorithm: " + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new RuntimeException("[Signature] Invalid key: " + e.getMessage());
        }
        byte[] hash = hmacSha256.doFinal(requestInfo.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

}


