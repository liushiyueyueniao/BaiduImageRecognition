package com.tony.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author jiangwj20966 2018/9/14
 */
public class BaiduRecognitionUtil {

    private static String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";
    private static String GENERATE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
    private static String appKey = "";
    private static String appSecret = "";

    public static String getPicContent(File image) throws Exception {

        FileInputStream fileInputStream = new FileInputStream(image);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int l = -1;
        while ((l = fileInputStream.read(buff)) > 0) {
            outputStream.write(buff, 0, l);
        }
        String base64Str = Base64.encodeBase64String(outputStream.toByteArray());
        System.out.println(base64Str);

        String accessToken = getAccessToken();

        return getResult(base64Str, accessToken);
    }


    private static String getResult(String imgBase64, String accessToken) throws IOException {
        if (accessToken==null) {
            return null;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("image", imgBase64)
//                .add("grant_type", "client_credentials")
                .add("access_token", accessToken).build();
        Request request = new Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded").url(GENERATE_URL).post(formBody).build();
        Response response = okHttpClient.newCall(request).execute();
        if (response != null && response.body() != null) {
            JSONObject result = (JSONObject) JSONObject.parse(response.body().string());
            System.out.println(result.toJSONString());
            return result.toJSONString();
        }
        return null;
    }

    private static String getAccessToken() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("grant_type", "client_credentials")
                .add("client_id", appKey)
                .add("client_secret", appSecret).build();
        Request request = new Request.Builder().url(ACCESS_TOKEN_URL).post(formBody).build();
        Response response = okHttpClient.newCall(request).execute();
        if (response != null && response.body() != null) {
            JSONObject result = (JSONObject) JSONObject.parse(response.body().string());
            String accessKey = result.getString("access_token");
            return accessKey;
        }
        return null;
    }








    public static String getAccessTokenUrl() {
        return ACCESS_TOKEN_URL;
    }

    public static void setAccessTokenUrl(String accessTokenUrl) {
        ACCESS_TOKEN_URL = accessTokenUrl;
    }

    public static String getGenerateUrl() {
        return GENERATE_URL;
    }

    public static void setGenerateUrl(String generateUrl) {
        GENERATE_URL = generateUrl;
    }

    public static String getAppKey() {
        return appKey;
    }

    public static void setAppKey(String appKey) {
        BaiduRecognitionUtil.appKey = appKey;
    }

    public static String getAppSecret() {
        return appSecret;
    }

    public static void setAppSecret(String appSecret) {
        BaiduRecognitionUtil.appSecret = appSecret;
    }
}