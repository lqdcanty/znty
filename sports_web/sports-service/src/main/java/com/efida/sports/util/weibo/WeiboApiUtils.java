package com.efida.sports.util.weibo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 微博接口调用
 * 
 * @author lijiaqi
 * @date 2015年7月14日
 */
public class WeiboApiUtils {

    public static final String USERS_SHOW_URL       = "https://api.weibo.com/2/users/show.json";

    public static final String ACCESS_TOKEN_URL     = "https://api.weibo.com/oauth2/access_token";

    public static final String GET_TOKEN_INFOURL    = "https://api.weibo.com/oauth2/get_token_info";

    public static final String EMAIL_URL            = "https://api.weibo.com/2/account/profile/email.json";

    public static final String SHORT_URL_EXPAND_URL = "https://api.weibo.com/2/short_url/expand.json";

    public static final String SHOW_URL             = "https://api.weibo.com/2/statuses/show.json";

    public static final String GEO_TO_ADDRESS_URL   = "https://api.weibo.com/2/location/geo/geo_to_address.json";

    public static final String IP_TO_GEO_URL        = "https://api.weibo.com/2/location/geo/ip_to_geo.json";

    public static final String IPLOOKUP_URL         = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json";

    public static final String USER_TIMELINE_URL    = "https://api.weibo.com/2/statuses/user_timeline.json";

    public static final String LONGWEIBO_ULR        = "https://api.weibo.com/2/proxy/vme/open/longshow.json";

    public static final String SENDALL_URL          = "https://m.api.weibo.com/2/messages/sendall.json";

    public static final String TIMELINE_BATCH_URL   = "https://api.weibo.com/2/statuses/timeline_batch.json";

    public static final String WEIBO_CLIENT_ID      = "1358758795";

    public static final String WEIBO_CLIENT_SECRET  = "84a7e9a9b08cc29fdbb8415de72ff877";

    public static final String WEIBO_GRANT_TYPE     = "authorization_code";

    public static final String WEIBO_REDIRECT_URI   = "http://bqj.cn/weibo/authcallback";

    /**
     * @title 获取tokenInfo
     * @methodName
     * @author lijiaqi
     * @description
     * @param accessToken
     * @return
     */
    public static JSONObject getTokenInfo(String accessToken) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", accessToken);
        return invokePOSTWeiboApi(GET_TOKEN_INFOURL, params);
    }

    /**
     * 
     * @Title
     * @author lijiaqi
     * @description
     * @param uids
     * @param accessToken
     * @param count
     * @param page
     * @return
     */
    public static JSONObject getTimeLineBatch(String uids, String accessToken, Integer page,
                                              Integer count, Integer feature) {
        StringBuffer urlBuffer = new StringBuffer();
        urlBuffer.append(TIMELINE_BATCH_URL).append("?").append("uids=").append(uids)
            .append("&access_token=").append(accessToken);
        if (count != null) {
            urlBuffer.append("&count=").append(count);
        }
        if (page != null) {
            urlBuffer.append("&page=").append(page);
        }
        if (feature != null) {
            urlBuffer.append("&feature=").append(feature);
        }
        String apiUrl = urlBuffer.toString();
        System.out.println(apiUrl);
        return invokeWeiboApi(apiUrl);
    }

    /**
     * 获取当前机器的地址
     * 
     * @param ip
     * @return
     */
    public static JSONObject ipLookup(String ip) {

        String apiurl = IPLOOKUP_URL;
        if (StringUtils.isNotBlank(ip)) {
            apiurl += "&ip=" + ip;
        }

        JSONObject resultJsonObject = invokeWeiboApi(apiurl);
        return resultJsonObject;
    }

    /**
     * 获取当前机器的地址
     * 
     * @param ip
     * @return
     */
    public static JSONObject ipLookup() {
        return ipLookup(null);
    }

    /**
     * 将经纬度坐标转换成地址坐标
     * 
     * @param longitude
     *            经度
     * @param latitude
     *            纬度
     * @param accessToken
     * @return
     */
    public static JSONObject geoToAddress(String longitude, String latitude, String accessToken) {
        String coordinate = UrlEnCodeAndDecodeUtil.encodeurl(longitude + "," + latitude);
        String apiurl = String.format("%s?access_token=%s&coordinate=%s", GEO_TO_ADDRESS_URL,
            accessToken, coordinate);
        return invokeWeiboApi(apiurl);
    }

    /**
     * 
     * @param ip
     * @param accessToken
     * @return
     */
    public static JSONObject ipToGeo(String ip, String accessToken) {
        String apiurl = String.format("%s?access_token=%s&ip=%s", IP_TO_GEO_URL, accessToken, ip);
        return invokeWeiboApi(apiurl);
    }

    /**
     * 获取用户的信息
     * 
     * @param uid
     * @param accessToken
     * @return
     */
    public static JSONObject queryUserInfo(String uid, String accessToken, String weiboClientId) {
        String apiurl = String.format("%s?access_token=%s&uid=%s&source=%s", USERS_SHOW_URL,
            accessToken, uid, weiboClientId);

        return invokeWeiboApi(apiurl);
    }

    /**
     * 获取用户的信息
     * 
     * @param uid
     * @param accessToken
     * @return
     */
    public static JSONObject getLocalUserInfo(String uid, String accessToken) {

        return queryUserInfo(uid, accessToken, WEIBO_CLIENT_ID);
    }

    /**
     * 根据短地址获取长地址
     * 
     * @param shortUrl
     * @param accessToken
     * @return
     */
    public static String getLongUrl(String shortUrl, String accessToken) {
        String apiurl = String.format("%s?access_token=%s&url_short=%s", SHORT_URL_EXPAND_URL,
            accessToken, shortUrl);
        JSONObject resulJsonObject = invokeWeiboApi(apiurl);

        JSONArray ulrs = resulJsonObject.getJSONArray("urls");
        String longUlr = "";
        for (Object object : ulrs) {
            JSONObject url = (JSONObject) object;
            if (url.getString("url_short").trim().equals(shortUrl.trim())) {
                longUlr = url.getString("url_long");
            }
        }
        return longUlr;
    }

    /**
     * 获取指定用户微博列表
     * 
     * @param shortUrl
     * @param accessToken
     * @return
     */
    public static JSONObject getUserTimeLine(String accessToken, String count) {
        String apiurl = String.format("%s?access_token=%s&count=%s", USER_TIMELINE_URL, accessToken,
            count);
        return invokeWeiboApi(apiurl);
    }

    /**
     * 根据Id获取新浪微博的信息
     * 
     * @param id
     * @param accessToken
     * @return
     */
    public static JSONObject getWeiboById(String id, String accessToken) {
        String apiurl = String.format("%s?access_token=%s&id=%s", SHOW_URL, accessToken, id);
        return invokeWeiboApi(apiurl);
    }

    /**
     * 获取长微博数据
     * 
     * @param oid
     * @param accessToken
     * @return
     */
    public static JSONObject getLongWeibo(String oid, String accessToken) {
        String oids = "1022:" + oid;
        String apiurl = String.format("%s?access_token=%s&oids=%s", LONGWEIBO_ULR, accessToken,
            oids);
        System.out.println(apiurl);
        JSONObject result = null;
        try {
            result = JSON.parseObject(HttpsClientUtil.doGet(apiurl));

        } catch (Exception e) {
            throw new RuntimeException(e);

        }
        if (result.getIntValue("code") != 100000) {
            throw new RuntimeException("获取长微博数据失败 " + result.toJSONString());
        }
        JSONObject longWeiboData = result.getJSONObject("data");
        if (longWeiboData == null) {
            throw new RuntimeException("获取长微博数据失败" + result.toJSONString());
        }
        JSONObject article = longWeiboData.getJSONObject("1022:" + oid);
        if (article == null) {
            throw new RuntimeException("获取长微博数据失败" + result.toJSONString());
        }

        return article;
    }

    /**
     * 获取用户的邮箱
     * 
     * @param uid
     * @param accessToken
     * @return
     */
    public static JSONObject getUserEmail(String uid, String accessToken) {

        String apiurl = String.format("%s?access_token=%s&source=%s", EMAIL_URL, accessToken,
            WEIBO_CLIENT_ID);
        return invokeWeiboApi(apiurl);

    }

    /**
     * 获取用户的uid和access_token
     * 
     * @param code
     * @param weiboClientId
     * @param weiboClientSecrect
     * @param weiboGrantType
     * @param weiboRedirectUrl
     * @return
     */
    public static JSONObject queryWeiboOpenIdAndToken(String code, String weiboClientId,
                                                      String weiboClientSecrect,
                                                      String weiboGrantType,
                                                      String weiboRedirectUrl) {

        String result = null;
        Map<String, String> map = new HashMap<String, String>();
        map.put("client_id", weiboClientId);
        map.put("client_secret", weiboClientSecrect);
        map.put("grant_type", weiboGrantType);
        map.put("redirect_uri", weiboRedirectUrl);
        map.put("code", code);
        try {
            result = HttpsClientUtil.doPost(ACCESS_TOKEN_URL, map, "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return JSON.parseObject(result);
    }

    /**
     * 获取用户的uid和access_token
     * 
     * @param code
     * @return
     */
    public static JSONObject getWeiboOpenIdAndToken(String code) {
        // String apiurl =
        // String.format("%s?client_id=%s&client_secret=%s&grant_type=%s&code=%s",
        // ACCESS_TOKEN_URL,
        // ConfigUtil.getPropertyValue("weibo_client_id"),
        // ConfigUtil.getPropertyValue("weibo_client_secret"),
        // ConfigUtil.getPropertyValue("weibo_code"),
        // code);
        return queryWeiboOpenIdAndToken(code, WEIBO_CLIENT_ID, WEIBO_CLIENT_SECRET,
            WEIBO_GRANT_TYPE, WEIBO_REDIRECT_URI);

    }

    public static JSONObject sendTextToAll(List<String> uids, String text, String accessToken) {
        JSONObject jsonObject = new JSONObject();
        JSONArray touser = new JSONArray();
        for (String uid : uids) {
            touser.add(uid);
        }
        JSONObject textObject = new JSONObject();
        textObject.put("content", text);
        jsonObject.put("touser", touser);
        jsonObject.put("text", textObject);
        jsonObject.put("msgtype", "text");
        String postStr = jsonObject.toJSONString();
        System.out.println("postStr:" + postStr);
        String result = "";
        try {
            result = HttpsClientUtil
                .executeJSONHttpMethod(SENDALL_URL + "?access_token=" + accessToken, postStr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (StringUtils.isBlank(result)) {
            throw new RuntimeException("调用新浪微博接口 " + SENDALL_URL + " 结果为空");
        }
        JSONObject resultObject = JSON.parseObject(result);
        if (resultObject.containsKey("error_code")) {
            throw new RuntimeException("调用新浪微博接口 " + SENDALL_URL + " 失败 result:" + result);
        }
        return resultObject;
    }

    /**
     * 调用新浪微博
     * 
     * @title
     * @methodName
     * @author lijiaqi
     * @description
     * @param invokeUrl
     * @return
     */
    private static JSONObject invokeWeiboApi(String invokeUrl) {
        String result = "";
        try {
            result = HttpsClientUtil.doGet(invokeUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (StringUtils.isBlank(result)) {
            throw new RuntimeException("调用新浪微博接口 " + invokeUrl + " 结果为空");
        }
        JSONObject resultObject = null;
        try {
            resultObject = JSON.parseObject(result);
        } catch (Exception e) {
            throw new RuntimeException("调用新浪微博接口 " + invokeUrl + " 异常 result:" + result);
        }
        if (resultObject.containsKey("error_code")) {
            throw new RuntimeException("调用新浪微博接口 " + invokeUrl + " 失败 result:" + result);
        }
        return resultObject;
    }

    private static JSONObject invokePOSTWeiboApi(String invokeUrl, Map<String, String> params) {
        String result = "";
        try {
            result = HttpsClientUtil.doPost(invokeUrl, params, "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (StringUtils.isBlank(result)) {
            throw new RuntimeException("调用新浪微博接口 " + invokeUrl + " 结果为空");
        }
        JSONObject resultObject = null;
        try {
            resultObject = JSON.parseObject(result);
        } catch (Exception e) {
            throw new RuntimeException("调用新浪微博接口 " + invokeUrl + " 异常 result:" + result);
        }
        /*
         * if (resultObject.containsKey("error_code")) { throw new
         * RuntimeException("调用新浪微博接口 " + invokeUrl + " 失败 result:" + result); }
         */
        return resultObject;
    }

    /*public static void main(String[] args) {
    	System.out.println(sendTextToAll(Arrays.asList("2342864382"), "这是测试哟，亲", "2.001BFNHGUukzUD872102aa490LYu1B"));
    }*/

}
