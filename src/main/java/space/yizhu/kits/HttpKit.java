package space.yizhu.kits;/* Created by xiuxi on 2018/11/2.*/

import com.alibaba.druid.support.json.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

public class HttpKit {


    private static CloseableHttpClient httpclient = null;

    //    同步调用
    public static String load(String url, Object params) {
//        System.setSecurityManager(new RMISecurityManager());
        StringBuilder resultStr = new StringBuilder();
        try {

            URL restURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
            conn.setRequestMethod("POST");

            conn.setDoOutput(true);
            conn.setAllowUserInteraction(false);
//        conn.setUseCaches(true);
            OutputStream os = conn.getOutputStream();
            byte[] bytes;
            if (params != null)
                if (params instanceof byte[])
                    bytes = (byte[]) params;
                else
                    bytes = params.toString().getBytes();
            else
                bytes = new byte[0];
            os.write(bytes, 0, bytes.length);
            os.flush();
            os.close();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));

            String line;

            while (null != (line = bReader.readLine())) {
                resultStr.append(line);
            }
            bReader.close();
        } catch (IOException e) {
             SysKit.print(e);
        }
        return resultStr.toString();
    }


    public static String httpPost(String url, Map<String, Object> map,String pubkey) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        Map<String, String> req = new HashMap<>();
        req.put("param", RSAKit.encryption(new Gson().toJson(map),pubkey));

        JSONObject jsonObject = new JSONObject();

        for (Map.Entry<String, String> entry : req.entrySet()) {
            //给参数赋值
            jsonObject.put(entry.getKey(), entry.getValue());
        }
        HttpPost httpPost = new HttpPost(url);
//        SysKit.print("开始发送请求["+url+"]:"+jsonObject.toString());
        //        json方式
        StringEntity entity = new StringEntity(jsonObject.toString(), "utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
        } catch (IOException e) {
            SysKit.print(e);
        }
        HttpEntity entity1 = response.getEntity();
        String result = null;
        try {
            result = EntityUtils.toString(entity1);
        } catch (ParseException | IOException e) {
            SysKit.print(e);
        }
        return result;
    }

    public static String httpPost(String url, String json) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(json, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
        } catch (IOException e) {
            SysKit.print(e);
        }
        HttpEntity entity1 = response.getEntity();
        String result = null;
        try {
            result = EntityUtils.toString(entity1);
        } catch (ParseException | IOException e) {
            SysKit.print(e);
        }
        return result;
    }

    public static String httpPostNoRSA(String url, String json) throws KeyManagementException {
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(json, "utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
             SysKit.print(e);
        }
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(ssf).setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
        } catch (IOException e) {
            SysKit.print(e);
        }
        HttpEntity httpEntity = response.getEntity();
        String result = null;
        if (httpEntity != null)
            try {
                result = EntityUtils.toString(httpEntity);
            } catch (ParseException | IOException e) {
                SysKit.print(e);
            }
        else {
            result = String.valueOf(response.getStatusLine().toString());
        }
        return result;
    }

    public static String httpPostNoRSA(String url, Map<String, Object> map) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            //给参数赋值
            jsonObject.put(entry.getKey(), entry.getValue());
        }
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(jsonObject.toString(), "utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
        } catch (IOException e) {
            SysKit.print(e);
        }
        HttpEntity entity1 = response.getEntity();
        String result = null;
        try {
            result = EntityUtils.toString(entity1);
        } catch (ParseException | IOException e) {
             SysKit.print(e);
        }
        return result;
    }

    public static String httpPostLocalTest(String url, Map<String, Object> map) throws KeyManagementException, NoSuchAlgorithmException {

        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(ssf).setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            //给参数赋值
            jsonObject.put(entry.getKey(), entry.getValue());
        }
        HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeader("Accept", "application/json");
        StringEntity entity = new StringEntity(jsonObject.toString(), "utf-8");//解决中文乱码问题
//        entity.setContentEncoding("UTF-8");
//        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
        } catch (IOException e) {
            SysKit.print(e);
        }
        HttpEntity entity1 = response.getEntity();
        String result = null;
        try {
            result = EntityUtils.toString(entity1);
        } catch (ParseException | IOException e) {
             SysKit.print(e);
        }
        return result;
    }

    public static String httpGet(String url) {
        //1.获得一个httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //2.生成一个get请求
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            //3.执行get请求并返回结果
            response = httpclient.execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String result = null;
        try {
            //4.处理结果，这里将结果返回为字符串
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        } catch (ParseException | IOException e) {
             SysKit.print(e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                 SysKit.print(e);
            }
        }
        return result;
    }


    public static void main(String[] args) {
        String str = "{\"busitype\":\"01\",\"actiontype\":\"01\",\"data\":{\"zwfwid\":\"20200930104307420900\",\"opertype\":\"zw03\",\"isfinish\":\"N\",\"operman\":\"\",\"opertime\":\"2020-09-30 10:44:25\",\"remark\":\"\",\"busino\":\"0110GG289494\",\"docinfo\":[{\"doc_id\":\"\",\"type\":\"pdf\",\"cllx\":\"00004\"},{\"doc_id\":\"\",\"type\":\"pdf\",\"cllx\":\"00005\"},{\"doc_id\":\"\",\"type\":\"pdf\",\"cllx\":\"00014\"},{\"doc_id\":\"\",\"type\":\"pdf\",\"cllx\":\"00015\"},{\"doc_id\":\"\",\"type\":\"pdf\",\"cllx\":\"00016\"},{\"doc_id\":\"\",\"type\":\"pdf\",\"cllx\":\"00017\"},{\"doc_id\":\"\",\"type\":\"pdf\",\"cllx\":\"00020\"},{\"doc_id\":\"\",\"type\":\"pdf\",\"cllx\":\"00043\"}]}}";
      JsonObject jsonObject=  new JsonObject();
        jsonObject.getAsJsonObject(str);
        JSONUtils.parse(str);
        SysKit.print(httpPost("http://221.214.107.228:8080/bvpmis/zwfw/uniteJsonInterface.bv",str));
    }

}
