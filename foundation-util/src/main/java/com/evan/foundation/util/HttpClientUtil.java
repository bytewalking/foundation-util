package com.evan.foundation.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author evan
 * @since 2021-03-08 11:24
 */
public class HttpClientUtil {

    private static final int TIME_OUT = 1000 * 10;

    public static String httpGet(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(TIME_OUT)
                .setConnectTimeout(TIME_OUT)
                .setConnectionRequestTimeout(TIME_OUT)
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
        httpGet.setHeader("Accept-Language", "zh,zh-CN;q=0.9,zh-TW;q=0.8,en-US;q=0.7,en;q=0.6");
        httpGet.setHeader("Cache-Control", "no-cache");
        httpGet.setHeader("Connection", "keep-alive");
        try {
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            ExceptionUtil.logError(e, () -> "httpGet error " + url);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                ExceptionUtil.logError(e, () -> "httpGet error " + url);
            }
        }
        return null;
    }


    public static String httpGet(String url,Map<String, String> header) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(TIME_OUT)
                .setConnectTimeout(TIME_OUT)
                .setConnectionRequestTimeout(TIME_OUT)
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
        httpGet.setHeader("Accept-Language", "zh,zh-CN;q=0.9,zh-TW;q=0.8,en-US;q=0.7,en;q=0.6");
        httpGet.setHeader("Cache-Control", "no-cache");
        httpGet.setHeader("Connection", "keep-alive");
        if(Objects.nonNull(header)) {
            Iterator headerIt = header.entrySet().iterator();
            while (headerIt.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) headerIt.next();
                httpGet.addHeader(elem.getKey(), elem.getValue());
            }
        }
        try {
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            ExceptionUtil.logError(e, () -> "httpGet error " + url);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                ExceptionUtil.logError(e, () -> "httpGet error " + url);
            }
        }
        return null;
    }


    /**
     * post form
     * @param url
     * @param header
     * @param body
     * @return
     */
    public static String httpPostByForm(String url,Map<String, String> header, Map<String, String> body) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(TIME_OUT)
                .setConnectTimeout(TIME_OUT)
                .setConnectionRequestTimeout(TIME_OUT)
                .build();
        HttpEntity httpEntity = null;
        try {
            List<NameValuePair> list = new ArrayList<>();
            Iterator bodyIt = body.entrySet().iterator();
            while (bodyIt.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) bodyIt.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate");
            httpPost.setHeader("Upgrade-Insecure-Requests", "1");
            httpPost.setHeader("Accept-Language", "zh,zh-CN;q=0.9,zh-TW;q=0.8,en-US;q=0.7,en;q=0.6");
            httpPost.setHeader("Cache-Control", "no-cache");
            httpPost.setHeader("Connection", "keep-alive");
            if(Objects.nonNull(header)) {
                Iterator headerIt = header.entrySet().iterator();
                while (headerIt.hasNext()) {
                    Map.Entry<String, String> elem = (Map.Entry<String, String>) headerIt.next();
                    httpPost.addHeader(elem.getKey(), elem.getValue());
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            HttpResponse httpResponse = httpclient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            return EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            ExceptionUtil.logError(e, () -> "httpPostByForm error " + url);
        } finally {
            try {
                httpclient.close();
                EntityUtils.consume(httpEntity);
            } catch (IOException e) {
                ExceptionUtil.logError(e, () -> "httpPostByForm close error " + url);
            }
        }
        return null;
    }

    /**
     * post json
     * @param url
     * @param header
     * @param bodyJson
     * @return
     */
    public static String httpPost(String url, Map<String, Object> header,String bodyJson) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(TIME_OUT)
                .setConnectTimeout(TIME_OUT)
                .setConnectionRequestTimeout(TIME_OUT)
                .build();
        HttpEntity httpEntity = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate");
            httpPost.setHeader("Upgrade-Insecure-Requests", "1");
            httpPost.setHeader("Accept-Language", "zh,zh-CN;q=0.9,zh-TW;q=0.8,en-US;q=0.7,en;q=0.6");
            httpPost.setHeader("Cache-Control", "no-cache");
            httpPost.setHeader("Connection", "keep-alive");
            if(Objects.nonNull(header)) {
                Iterator headerIt = header.entrySet().iterator();
                while (headerIt.hasNext()) {
                    Map.Entry<String, String> elem = (Map.Entry<String, String>) headerIt.next();
                    httpPost.addHeader(elem.getKey(), elem.getValue());
                }
            }
            httpPost.setEntity(new StringEntity(bodyJson, "utf-8"));
            HttpResponse httpResponse = httpclient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            return EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            ExceptionUtil.logError(e, () -> "httpPostByJson error " + url);
        } finally {
            try {
                httpclient.close();
                EntityUtils.consume(httpEntity);
            } catch (IOException e) {
                ExceptionUtil.logError(e, () -> "httpPostByJson close error " + url);
            }
        }
        return null;
    }

    /**
     * post json
     * @param url
     * @param header
     * @param body
     * @return
     */
    public static String httpPostByJson(String url, Map<String, Object> header,Map<String, Object> body) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(TIME_OUT)
                .setConnectTimeout(TIME_OUT)
                .setConnectionRequestTimeout(TIME_OUT)
                .build();
        HttpEntity httpEntity = null;
        try {
            List<NameValuePair> list = new ArrayList<>();
            Iterator bodyIt = body.entrySet().iterator();
            while (bodyIt.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) bodyIt.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate");
            httpPost.setHeader("Upgrade-Insecure-Requests", "1");
            httpPost.setHeader("Accept-Language", "zh,zh-CN;q=0.9,zh-TW;q=0.8,en-US;q=0.7,en;q=0.6");
            httpPost.setHeader("Cache-Control", "no-cache");
            httpPost.setHeader("Connection", "keep-alive");
            if(Objects.nonNull(header)) {
                Iterator headerIt = header.entrySet().iterator();
                while (headerIt.hasNext()) {
                    Map.Entry<String, String> elem = (Map.Entry<String, String>) headerIt.next();
                    httpPost.addHeader(elem.getKey(), elem.getValue());
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            HttpResponse httpResponse = httpclient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            return EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            ExceptionUtil.logError(e, () -> "httpPostByJson error " + url);
        } finally {
            try {
                httpclient.close();
                EntityUtils.consume(httpEntity);
            } catch (IOException e) {
                ExceptionUtil.logError(e, () -> "httpPostByJson close error " + url);
            }
        }
        return null;
    }

    /**
     * HTTP GET请求操作
     * @param url
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T httpGet(String url,Class<T> clazz) {
        String result = httpGet(url);
        if(StringUtils.isBlank(result)){
            return null;
        }
        return JSON.parseObject(result, clazz);
    }

}

