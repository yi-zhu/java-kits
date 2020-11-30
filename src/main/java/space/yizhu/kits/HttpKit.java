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
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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

    public static String httpGet(String url,Map<String,Object> head ) throws UnsupportedEncodingException {
        //1.获得一个httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //2.生成一个get请求
        HttpGet httpget = new HttpGet(URLEncoder.encode(url,"utf-8"));
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
        String url = " https://wq.jd.com/deal/msubmit/confirm?paytype=0&paychannel=1&action=1&reg=1&type=0&token2=EF1C95CA274CE44243C283C6C94143E4&dpid=&skulist=100016046842&scan_orig=&gpolicy=&platprice=0&ship=0|65|4|||0||1||2020-11-28|09:00-15:00|{%221%22:%221%22,%22161%22:%220%22,%22237%22:%221%22,%22278%22:%220%22,%2230%22:%221%22,%2235%22:%221%22}|1|||||0||0||0|2,-404966545,889164203320598528,889164203358347264,101612_889164233934766081,101613_889164234014457856||&pick=&savepayship=0&valuableskus=100016046842,1,409900,678&commlist=100016046842,,1,100016046842,1,0,0&sceneval=2&setdefcoupon=0&r=0.826552690411303&callback=confirmCbF&traceid=1058799336827598629";

        Map header = new HashMap(){
            {
                put("authority", "wq.jd.com");
                put("method", "GET");
                put("path", "/deal/msubmit/confirm?paytype=0&paychannel=1&action=1&reg=1&type=0&token2=EF1C95CA274CE44243C283C6C94143E4&dpid=&skulist=100016046842&scan_orig=&gpolicy=&platprice=0&ship=0|65|4|||0||1||2020-11-28|09:00-15:00|{%221%22:%221%22,%22161%22:%220%22,%22237%22:%221%22,%22278%22:%220%22,%2230%22:%221%22,%2235%22:%221%22}|1|||||0||0||0|2,-404966545,889164203320598528,889164203358347264,101612_889164233934766081,101613_889164234014457856||&pick=&savepayship=0&valuableskus=100016046842,1,409900,678&commlist=100016046842,,1,100016046842,1,0,0&sceneval=2&setdefcoupon=0&r=0.826552690411303&callback=confirmCbF&traceid=1058799336827598629");
                put("scheme", "https");
                put("accept", "*/*");
                put("accept-encoding", "gzip, deflate, br");
                put("accept-language", "zh-CN,zh;q=0.9");
                put("cookie", "shshshfpa=90cf3218-af8d-9aa4-711d-22a537d85cae-1554279661; shshshfpb=coeIv8hADM%2FsczHzR4yKkFw%3D%3D; pinId=v9GcfTThBvGUps4Gaq4KtA; unick=%E5%80%9A%E7%AB%B9; _tp=mm1ult5xzJeudO3lJ5b9Dg%3D%3D; _pst=13406371157_p; __jdu=1775236543; pin=13406371157_p; user-key=bab3ee3e-66e6-498c-8314-0c0644096ebb; areaId=13; ipLocation=%u5c71%u4e1c; ipLoc-djd=13-1000-40488-54434.138125605; mt_xid=V2_52007VwMUVFlbUF0bTBtsAjULG1ZeUVpGHBwbDBliCxVQQQtaWhZVTQhSZQITU1xbWl0eeRpdBmYfElJBWFtLH0gSXQFsAhFiX2hRahxIH1QAYjMQWl9Y; cn=39; PCSYCityID=CN_370000_370100_370102; TrackID=1-HvrvFQwpS2pthIfNJz7O_7XDpRncDST9MMLs5TeWB2o1A8eK1KnKwg3p0h2ZBetFHYUkZsV6EqHbt0Qm_6FMhsVvSUV9MiGZI6HgeQcIho; ceshi3.com=201; thor=9ADEF46F1E1A23E6570733F20C328450637749E5D62783D56886A74AAEF11A73BC77FBE10B2F6B7F1F5F46FBE8B080F67126D3352D4F3CA1A96ABE811BBAAC5C5C05533BB366BC9BF818635657A816EC4B267CB7A16ABF348810AA923688D7E499145CFE09C5A4E426E02ABDF43CF6F51031CA9CC084DFFBA8CBD555B0E68730E9163880270F29BEBEBA15E8BDA0604D; CCC_SE=ADC_efQ7APEFfj01po1InD1tJw5Y7lQXPuq2TDD1xY9xHWZoRyKan5YtlYLL9UAFwB2n%2bbwrycqIaGE7f4F0YvVqh3657HJrpzwIYPdOYADrOc8z3%2fppazwes15kfLGtMRzVg43vkBo2zwzAO4eXHN3KaCQYiB4pIBR%2finY4M2jKw1PpdyGHJ03zMqVTeYk4MoZ%2bckVSRYFScVbzOFWVw7lpnLxokNBv3KGszaGldOBXqS0YihoXMGOW4sR9U0fTOQmzkUrTrR4v4HZ2jhAXqxe6yyhUkOlQB8GFLyHqnO73ntLoF23PEOH5z1XG5RowWfah76pcjfmBlHOev5qko3oNIZvakhTeHMmVP78vPCe2g4%2bAbAnvhv8KzlUROGZuFRFWee0dGRSAeaMgjdetsS1iEuil9cCdfH1yCuvZdQMgrwFC9bqYRwnlN6dkBABD5pAi6mOInoyZVs996DlWplpcnmx9ROrLclNNkZSwcLAIAXUcjs42vfT%2b7a39NRWDAGI8Knn0YjZmuqNMi9HtC%2bAc%2fZGmNvwGsPwnd0DqrINOmoBd2oXKu5xrNwgJUoMiuWMCcXEf8kpsqKrkxAefqqzFc3rr4haMQRpgkuhkblecWlt8vnKg958wCXhXV1wYz61V; unpl=V2_ZzNtbRUAQh0nDUBReBALDWJTEVRKBRBAdwxAACgRCAJkA0FdclRCFnQURlRnGloUZwcZXENcRhZFCEVkexhdBGYLGlxFUnMlRQtGZHopXANhBxFfRVNGF0U4QVRyKVwEZgISWkNSRBxwC3ZUFUUOR1cBG19DUHMURQhBVnIaXQJkCxBtFDlDFHQJR1V6H10CKgMUW0ZUQRJxDURkeilf; __jda=122270672.1775236543.1554279659.1606459965.1606460374.155; __jdc=122270672; __jdv=122270672|kong|t_1000099064_|tuiguang|ff09c47428f94a289cbd357eb9e621b1|1606460442128; mba_muid=1775236543; visitkey=33445805293610014; 3AB9D23F7A4B3C9B=N77MZGWDDBPAJQM4YLIU67BJJ2TREI6RAHU34RQGEZOJ7LKYCVDVAHNEG3MJAH6ASGVHQBINML6QHWJRKH6CRBHMAI; TrackerID=ZplZZ6UmUxQXkkqkK64k-sE-NkFgCnvwPcGvXH704DM8iyZm3loGWmzzcrv4qtySdIJ4hxIfVCgUcbKFvgcBz0TLBCsbPkhr7-dg-TLhNdw; pt_key=AAJfwKQwADA7xWADIqQB67j_XXvLFH-1DDmwNLU9XN9uBIQ7AE-7Jb3KElQ2FbK9koDFF3KGEGg; pt_pin=13406371157_p; pt_token=oxcr55d2; pwdt_id=13406371157_p; sfstoken=tk01mc7571ce4a8sMXgxKzJWejR1fsn/yOCHmmC36IagMzvnJ/Ri7F3G5NTBbrU5hxvqFRn9ziy4GxfpmGBHvMv1YVYf; cartLastOpTime=1606460466; wq_logid=1606460465_1804289383; wxa_level=1; retina=0; cid=9; wqmnx1=MDEyNjM1MGg6bWNvL3JpYT0xOGFtbnJ0bD0wMzA0MzU5MHovKG9UMG54QVd0LkssZWtoLy4uU2kuNWY3bjI0MllPT1UhSCU%3D; jxsid=16064604645176557141; wq_addr=138125605%7C13_1000_40488_54434%7C%u5C71%u4E1C_%u6D4E%u5357%u5E02_%u5386%u57CE%u533A_%u738B%u820D%u4EBA%u8857%u9053%7C%u5C71%u4E1C%u6D4E%u5357%u5E02%u5386%u57CE%u533A%u738B%u820D%u4EBA%u8857%u9053%u5DE5%u4E1A%u5317%u8DEF%u7965%u6CF0%u57CE%u9633%u5149%u5C1A%u4E1C10%u53F7%u697C1%u5355%u5143302%7C117099945%2C36719627; addrId_1=138125605; mitemAddrId=13_1000_40488_54434; mitemAddrName=%u5C71%u4E1C%u6D4E%u5357%u5E02%u5386%u57CE%u533A%u738B%u820D%u4EBA%u8857%u9053%u5DE5%u4E1A%u5317%u8DEF%u7965%u6CF0%u57CE%u9633%u5149%u5C1A%u4E1C10%u53F7%u697C1%u5355%u5143302; webp=1; __jdb=122270672.10.1775236543|155.1606460374; mba_sid=16064604442686611122431237564.2; __wga=1606460464951.1606460464951.1606460464951.1606460464951.1.1; PPRD_P=UUID.1775236543; jxsid_s_t=1606460465029; jxsid_s_u=https%3A//p.m.jd.com/norder/order.action; sc_width=2560; shshshfp=6d8040b6b212cbf65283dfbb3640ec6c; shshshsID=d7222605edffbe672bffc38900f2ff82_12_1606460465376; cd_eid=N77MZGWDDBPAJQM4YLIU67BJJ2TREI6RAHU34RQGEZOJ7LKYCVDVAHNEG3MJAH6ASGVHQBINML6QHWJRKH6CRBHMAI; __jd_ref_cls=MNeworder_COnlinePay");
                put("dnt", "1");
                put("referer", "https://p.m.jd.com/norder/order.action?wareId=100016046842&wareNum=1&enterOrder=true&login=1&r=0.9643570808029407");
                put("sec-fetch-mode", "no-cors");
                put("sec-fetch-site", "same-site");
                put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.131 Safari/537.36");

            }
        };

        String ret = httpGet(url, header);
        SysKit.print(ret);
    }

}
