package space.yizhu.kits;/* Created by xiuxi on 2018/11/2.*/

import com.google.gson.Gson;
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
            e.printStackTrace();
        }
        return resultStr.toString();
    }


    public static String httpPost(String url, Map<String, Object> map) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        Map<String, String> req = new HashMap<>();
        req.put("param", RSAKit.encryption(new Gson().toJson(map)));

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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static void main(String[] args) {
/*//李文峰测试
        String str = "[\n" +
                "  {\n" +
                "    \"historyTroubleInfo\": [\n" +
                "      {\n" +
                "        \"troubleType\": 5,\n" +
                "        \"uid\": \"cd777604149c46bc8ff1c691fd8da752\",\n" +
                "        \"equipmentState\": 4,\n" +
                "        \"troubleTime\": 1550127299355,\n" +
                "        \"placeId\": \"\",\n" +
                "        \"tenantId\": \"\",\n" +
                "        \"equipmentId\": \"\",\n" +
                "        \"projectId\": \"\",\n" +
                "        \"equipmentType\": 1.0\n" +
                "      }\n" +
                "    ],\n" +
                "    \"gateWayNumber\": \"8e4f3f30-ce45-49ab-bea9-ff35849b1f2e\",\n" +
                "    \"imei\": \"864814042953900\",\n" +
                "    \"realTimeDataInfo\": [\n" +
                "      {\n" +
                "        \"value6\": \"8\",\n" +
                "        \"value5\": \"0\",\n" +
                "        \"value7\": \"53906769\",\n" +
                "        \"value2\": \"20\",\n" +
                "        \"value1\": \"108\",\n" +
                "        \"value4\": \"90\",\n" +
                "        \"value3\": \"1\",\n" +
                "        \"placeId\": \"\",\n" +
                "        \"equipmentId\": \"\",\n" +
                "        \"equipmentType\": 1.0,\n" +
                "        \"receiveTime\": 1550127299355,\n" +
                "        \"equipmentState\": 3,\n" +
                "        \"tenantId\": \"\",\n" +
                "        \"projectId\": \"\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"gatewayState\": 1\n" +
                "  }\n" +
                "]";

        try {
            httpPostNoRSA("http://192.168.11.31/auth/api/equipmentApi/synchronousData", str);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
*/



/*        Map map = new HashMap();
        map.put("param", "j6C5vt4WeJbS3hO90+STlAJl9XRTTUvscoUU8oaeFnwYis+XNut+oLUQX\\/YGe+J1fhMPQHe9+MuTWzLMSEh7q\\/Ou2QttaAeVt4wk2BTGbu8+iEe34vn1T6L5e5a37tDDJU1hVv2xXn4g6Npm0Ukj8DvY3G+a8\\/efqi\\/hBV3gNzw=");
        String ret = null;
        try {
            ret = httpPostLocalTest("https://ioicube.com:8891/container/zmkm", map);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SysKit.print(ret);*/
       /* Map ret = new HashMap();

        ret.put("code", 0);
        ret.put("type", 1);
        ret.put("devid", "00000000f561352b");
        ret.put("orderid", "c124997600993306");
        ret.put("adds", new ArrayList<>());
        ret.put("down", new ArrayList<>());
        ret.put("msg", "deviceOpenRes success");
        ret.put("timestamp", new Date().getTime());
        String retStr = HttpKit.httpPost("https://testc.wemall.com.cn/callbackapi/order/order", ret);
        retStr = retStr.split(":")[1];
        retStr = RSAKit.decryption(retStr.substring(0, retStr.length() - 1));
        SysKit.print(retStr);*/
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> noids = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    noids.add("ONE" + ToolKit.getRandomCode());
                }
                for (String str : noids) {
                    String json = "{\"IMEI\":\""+str+"\",\"name\":\"ONE\",\"manufacturerId\":\"ST\",\"manufacturerName\":\"ST\",\"deviceType\":\"Smoke\",\"model\":\"517N01\",\"protocolType\":\"CoAP\"}";
                    SysKit.print(httpPostNoRSA("http://ioicube.com:7890/iotNb/addSmoke", json));
                }
            }
        }).start();        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> noids = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    noids.add("TWO" + ToolKit.getRandomCode());
                }
                for (String str : noids) {
                    String json = "{\"IMEI\":\""+str+"\",\"name\":\"TWO\",\"manufacturerId\":\"ST\",\"manufacturerName\":\"ST\",\"deviceType\":\"Smoke\",\"model\":\"517N01\",\"protocolType\":\"CoAP\"}";
                    SysKit.print(httpPostNoRSA("http://ioicube.com:7890/iotNb/addSmoke", json));
                }
            }
        }).start();        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> noids = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    noids.add("TREE" + ToolKit.getRandomCode());
                }
                for (String str : noids) {
                    String json = "{\"IMEI\":\""+str+"\",\"name\":\"TREE\",\"manufacturerId\":\"ST\",\"manufacturerName\":\"ST\",\"deviceType\":\"Smoke\",\"model\":\"517N01\",\"protocolType\":\"CoAP\"}";
                    SysKit.print(httpPostNoRSA("http://ioicube.com:7890/iotNb/addSmoke", json));
                }
            }
        }).start();


*/

//    }

//                Map ret = new HashMap();
//                map.put("param","\\\"QMtmsSY+FNJ6qFc+wvPUi9z1BByJU9bdSEv0eHW8F1QZLlZTslvTJKRimO3\\\\\\/jwQeEE9XPAJYhHKcLyOKbDXon1GK6Vzckp1bx5o4Lm7J7+PB\\\\\\/kHeSg9nBG+Zy6zQsxCPB2ZJVQo0EZsWYm0iz4\\\\\\/m\\\\\\/wDYr6rGe\\\\\\/S6xPbxcJg20mUjvLkljo2+QCgjUGitHZeJDBwQf7hD54tnv8C0U5ziam4TKlImKV\\\\\\/dC95+8dVH38K7q0i\\\\\\/xAmGvlIj0CsFagzdyVj0n2KgMIX3mZHInTMpEQmYlCDvCTh2h10+9CSpHRNrQX1\\\\\\/78vaRyQQO027y0Dqt6bVm0gd1xAChSx\\\\\\/wHjsyQ==\\\"");
//                String ret = httpPostLocalTest("http://localhost:8890/zmkm/getVedio", map);
//                String ret = httpPostLocalTest("http://localhost:8890/container/deviceOpenS", map);
//                SysKit.print(ret);
//手动结束订单
/*
        Map ret = new HashMap();

        ret.put("code", 0);

        ret.put("type", "1");
        ret.put("devid", "00000000f561352b");
        ret.put("orderid", "c305620243037389");
        ret.put("adds", new ArrayList<>());
        ret.put("down", new ArrayList<>());
        ret.put("msg", "deviceOpenRes success");
        ret.put("timestamp", new Date().getTime());
        SysKit.print(httpPost("https://testc.wemall.com.cn/callbackapi/order/order", ret));
*/


//        try {
//            load("http://ioicube.com:8895/iotNb/synchronousData", "");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Map map = new HashMap();
        map.put("param", "AodjQMSVMdL7z1lBQu9PK654bOaVreSRIl3XQuq6Vg5Nd4kfeUwy2uW6aFhEzq031LZnbBB5oi31wqlY1H\\/ItAWawkioIR6Zzd+AnPBzigyMo+zzvOuYAoVO5Ha0y9lexCoe7amST3PV0lMxrwrn8YWlKcSlFk7j459PIGISPeA=");

        SysKit.print(httpPostNoRSA("https://ioicube.com:8895/container/zmkm", map));
    }

}
