package space.yizhu.kits;/* Created by xiuxi on 2018/11/2.*/

import com.alibaba.druid.support.json.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

public class HttpKit {


    private static CloseableHttpClient httpclient = null;
    public static String load(String url){
        return load(url, null, "GET");
    }
    public static String load(String url,Object params){
        return load(url, params, "POST");
    }
    //    同步调用
  /*  public static String load(String url, Object params,String method) {
//        System.setSecurityManager(new RMISecurityManager());
        StringBuilder resultStr = new StringBuilder();
        try {

            URL restURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
            if (null==method||method.isEmpty())
                method = "POST";
            conn.setRequestMethod(method.toUpperCase());

            conn.setDoOutput(true);
            conn.setAllowUserInteraction(false);
        conn.setUseCaches(true);
        if (params!=null){

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
        }

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
    }*/
    public static String load(String url, Object params,String method) {

        StringBuilder resultStr = new StringBuilder();
        try {

            URL restURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
            if (null==method||method.isEmpty())
                method = "POST";
            conn.setRequestMethod(method.toUpperCase());
            conn.setDoOutput(true);
            conn.setAllowUserInteraction(false);
            conn.setUseCaches(true);
            if (params!=null) {
                OutputStream os = conn.getOutputStream();
                byte[] bytes;
                if (params != null)
                    if (params instanceof byte[])
                        bytes = (byte[]) params;
                    else if (params instanceof String)
                        bytes = params.toString().getBytes();
                    else
                        bytes = JSONUtils.toJSONString(params).getBytes();
                else
                    bytes = new byte[0];
                os.write(bytes, 0, bytes.length);
                os.flush();
                os.close();
            }
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
    private static CloseableHttpClient getHttpsClient() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);
        // 指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            // 信任任何链接
            TrustStrategy anyTrustStrategy = new TrustStrategy() {

                @Override
                public boolean isTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
                    // TODO Auto-generated method stub
                    return true;
                }
            };
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        // 设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        // 构建客户端
        return HttpClientBuilder.create().setConnectionManager(connManager).build();
    }
    public static String httpGet(String url,Map<String,Object> head ) throws UnsupportedEncodingException, NoSuchAlgorithmException, KeyManagementException {
        //1.获得一个httpclient对象
        CloseableHttpClient httpclient;
        url = url.replaceAll(" ", "");
        if (url.startsWith("https")){

            httpclient=getHttpsClient();
        }else {
            httpclient = HttpClients.createDefault();
        }
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()
                // 设置连接超时时间(单位毫秒)
                .setConnectTimeout(7000)
                // 设置请求超时时间(单位毫秒)
                .setConnectionRequestTimeout(7000)
                // socket读写超时时间(单位毫秒)
                .setSocketTimeout(7000)
                // 设置是否允许重定向(默认为true)
                .setRedirectsEnabled(true).build();
        HttpGet httpget = new HttpGet(URLEncoder.encode(url,"utf-8"));

        // 将上面的配置信息 运用到这个Get请求里
        httpget.setConfig(requestConfig);


        //2.生成一个get请求
        CloseableHttpResponse response = null;
        if (null!=head&&head.size()>0){
            for (Map.Entry<String, Object> st:head.entrySet()) {
                if (null!=st.getValue()&&null!=st.getKey())
                httpget.addHeader(st.getKey(),st.getValue().toString());

            }
        }
        try {
            //3.执行get请求并返回结果
            response = httpclient.execute(httpget);
        } catch (IOException e1) {
         SysKit.print(e1);
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


    public static void main(String[] args) throws UnsupportedEncodingException {

       SysKit.print( load("http://yizhu.space:22333/whkfb/callback/gethtxx",new HashMap<String,String
               >(){
           {
               put("htbh","201800037408");
               put("vcode","1234");
           }
       }));
if (true)
    return;
        load("http://117.184.199.40:8877/authenticationService/getLicencePdf.do?uniscid=ZZJGD1609314154606&pdfcode=231003Fabcd31110200000001061511eykuENfwF1A4B38D0",null,"get");
        String url = " https://www.baidu.com";

        Map header = new HashMap(){
            {
                put("cookie", "deviceid=wdi10.3c09f00bee14c4c5d3bad86b3ee434b5c75fe59afcc33d3c4dbdc464f73b3a44; appidstack=101; XLA_CI=571d3a3b9706ae8c1b31a6bd5c40d924; xl_fp_rt=1609291477909; isvip=1; order=; score=119288; usrname=xiuxingzhe2; userid=72828577; upgrade=; logintype=; creditkey=ck0.Q9O-6tfW4LAsLsflhmXfz7zvpVRfo-6c5KfJDv8Rq8WCOuSKU3ghHK4rDao357Fx9jMVsnfXLypI8lBTZ6ja1sNk1P1o8HFIhYlFN_wWNMQWtS5va45KWqLV0jvC90xr; sessionid=ws001.E9380E1D30AEF0B39C67F380DAE367DF; usernewno=680732606; state=0; version=1; usernick=%E5%80%9A%E7%AB%B9; usertype=0; klb_pc_pcc=0; verify_type=; _x_t_=0; flowid=4c3b2539-d6bd-4201-84b3-8e0f9589024e; aidfrom=xl_personal_self_btn2; referfrom=pc_xl_personal_center; XLA_RFundefined=pc_xl_personal_center; error=appid_notmatch; error_description=\"Application name does not match business tracking number\"; errdesc=5bqU55So56iL5bqP5ZCN5ZKM5Lia5Yqh6Lef6Liq56CB5LiN5Yy56YWNWzExXQ==; VERIFY_KEY=2D4074F15221F5234B90D6CF3759DF64848604D15BAF1C21E7786BFE19A9E93E");
                put("method", "GET");


            }
        };

        String ret = null;
        try {
            ret = httpGet(url, header);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        SysKit.print(ret);
    }

}
