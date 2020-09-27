package com.cloudservice.trade.huobi.context;

/**
 * 服务URL
 * <p>
 *      您可以自行比较使用api.huobi.pro和api-aws.huobi.pro两个域名的延迟情况，选择延迟低的进行使用。
 *      其中，api-aws.huobi.pro域名对使用aws云服务的用户做了一定的链路延迟优化。
 *
 *      ！请使用中国大陆以外的 IP 访问火币 API。
 *      ！鉴于延迟高和稳定性差等原因，不建议通过代理的方式访问火币API。
 *      ！为保证API服务的稳定性，建议使用日本AWS云服务器进行访问。如使用中国大陆境内的客户端服务器，连接的稳定性将难以保证。
 * </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/8/14
 */
public interface Host {

    // REST API
    String REST_API = "https://api.huobi.pro";
    String REST_API_AWS = "https://api-aws.huobi.pro";

    // Websocket Feed（行情，不包含MBP增量行情）
    String WS_API = "wss://api.huobi.pro/ws";
    String WS_API_AWS = "wss://api-aws.huobi.pro/ws";

    // Websocket Feed（行情，仅MBP增量行情）
    String WS_FEED_API = "wss://api.huobi.pro/feed";
    String WS_FEED_API_AWS = "wss://api-aws.huobi.pro/feed";

    // Websocket Feed（资产和订单）
    String WS_V1_API = "wss://api.huobi.pro/ws/v1";
    String WS_V1_API_AWS = "wss://api-aws.huobi.pro/ws/v1";

    /** 合约访问地址 */
    String CONTRACT = "https://api.hbdm.com";
    /** 合约访问地址, 备用 */
    String CONTRACT_PRO = "https://api.btcgateway.pro";

}


