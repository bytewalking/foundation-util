package com.evan.foundation.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author evan
 * @since 2021-03-08 11:24
*/
@Slf4j
public class DingTalkUtil {


    private static int SUCCESS = 200;
    private static final String JSON_TYPE = "application/json";

    /**
     * 通过Post请求发送简单钉钉通知
     *
     * @param robotUrl
     * @param content
     */
    @SneakyThrows
    public static boolean sendDingTalkNotification(String robotUrl, String content) {

        AssertUtil.notBlank(robotUrl, () -> "robotUrl empty");
        AssertUtil.notBlank(content, () -> "content empty");

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(robotUrl);
            httpPost.addHeader("Content-Type", "application/json; charset=utf-8");

            StringEntity se = new StringEntity(content, "utf-8");
            httpPost.setEntity(se);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (checkResponse(httpResponse)) {
                return true;
            }
            LogUtil.warn(log, "钉钉通知发送失败: {} {} ", robotUrl, content);
            return false;
        }
    }

    /**
     * 发送文本消息
     *
     * @param txtContent
     * @param robotUrl
     */
    public static void sendTxt(String txtContent, String robotUrl) {
        if (StringUtils.isBlank(txtContent) || StringUtils.isBlank(robotUrl)) {
            return;
        }
        Map<String, Object> dintTalkMap = new HashMap<>();
        dintTalkMap.put("msgtype", "text");
        Map<String, Object> dintTalkContentMap = new HashMap<>();
        dintTalkContentMap.put("content", txtContent);
        dintTalkMap.put("text", dintTalkContentMap);
        DingTalkUtil.sendDingTalkNotification(robotUrl, JSON.toJSONString(dintTalkMap));
    }

    /**
     * markdown 消息
     *
     * @param title
     * @param text
     * @param imageUrl
     * @param robotUrl
     */
    public static void sendMarkdown(String title, String text, String imageUrl, String robotUrl) {
        if (StringUtils.isBlank(title)
                || StringUtils.isBlank(text)
                || StringUtils.isBlank(imageUrl)
                || StringUtils.isBlank(robotUrl)) {
            return;
        }
        Map<String, Object> dintTalkMap = new HashMap<>();
        dintTalkMap.put("msgtype", "markdown");
        Map<String, Object> dintTalkContentMap = new HashMap<>();
        dintTalkContentMap.put("title", title);
        dintTalkContentMap.put("text", "#### " + text + " ![](" + imageUrl + ")");
        dintTalkMap.put("markdown", dintTalkContentMap);
        DingTalkUtil.sendDingTalkNotification(robotUrl, JSON.toJSONString(dintTalkMap));
    }

    /**
     * actionCard 消息
     *
     * @param title
     * @param text
     * @param detailUrl    跳转链接
     * @param imageUrlList 图片
     * @param robotUrl     机器人地址
     */
    public static void sendActionCard(String title, String text, String detailUrl, List<String> imageUrlList, String robotUrl) {
        if (StringUtils.isBlank(title)
                || StringUtils.isBlank(text)
                || StringUtils.isBlank(detailUrl)
                || StringUtils.isBlank(robotUrl)) {
            return;
        }
        Map<String, Object> dintTalkMap = new HashMap<>();
        Map<String, Object> dintTalkContentMap = new HashMap<>();
        dintTalkContentMap.put("title", title);
        StringBuffer sb = new StringBuffer();
        sb.append("#### ").append(text);

        if (!CollectionUtils.isEmpty(imageUrlList)) {
            for (String imageUrl : imageUrlList) {
                sb.append(" ![](" + imageUrl + ")");
            }
        }

        dintTalkContentMap.put("text", sb.toString());
        dintTalkContentMap.put("btnOrientation", "0");
        dintTalkContentMap.put("singleTitle", "查看详情");
        dintTalkContentMap.put("singleURL", detailUrl);

        dintTalkMap.put("msgtype", "actionCard");
        dintTalkMap.put("actionCard", dintTalkContentMap);
        DingTalkUtil.sendDingTalkNotification(robotUrl, JSON.toJSONString(dintTalkMap));
    }

    private static boolean checkResponse(HttpResponse httpResponse) {
        if (httpResponse.getStatusLine().getStatusCode() != SUCCESS) {
            return false;
        }
        if (!JSON_TYPE.equalsIgnoreCase(httpResponse.getEntity().getContentType().getValue())) {
            return false;
        }
        try {
            JSONObject responseJsonObj = JSON.parseObject(EntityUtils.toString(httpResponse.getEntity(), "UTF-8"));
            return "ok".equalsIgnoreCase(responseJsonObj.getString("errmsg"));
        } catch (IOException e) {
            LogUtil.warn(log, "dingtalk parse response ", e);
            return false;
        }
    }
}

