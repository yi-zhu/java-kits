package space.yizhu.kits;

import com.alibaba.druid.support.json.JSONUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import space.yizhu.bean.LogModel;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolKit {

    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static String numberString = "0123456789";

/*
*暂时废弃

    public static Map<String, Object> getRequestMap(HttpServletRequest request) {
        String str = null;
        try {
            InputStream is = request.getInputStream();
            DataInputStream input = new DataInputStream(is);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            str = bufferedReader.readLine();
        } catch (IOException e) {
            SysKit.print(e, "getInputStream");
        }

        try {
            return new Gson().fromJson(str, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            SysKit.print(e, "请求数据非GSON");
            Map<String, Object> map = new HashMap<>();
            map.put("data", str);
            return map;
        }
    }
    public static Map<String, Object> getRequestMap(HttpServletRequest request, String privkey) {
        String str = null;
        try {
            InputStream is = request.getInputStream();
            DataInputStream input = new DataInputStream(is);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            str = bufferedReader.readLine();
        } catch (IOException e) {
            SysKit.print(e, "getInputStream");
        }
        try {
            if (str.contains(":")) {
                str = str.split(":")[1];
                str = RSAKit.decryption(str.substring(0, str.length() - 1), privkey);
            } else
                str = RSAKit.decryption(str, privkey);

        } catch (Exception e) {
            SysKit.print(e, "RSA错误");
        }
        try {
            return new Gson().fromJson(str, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            SysKit.print(e, "请求数据非GSON");
            Map<String, Object> map = new HashMap<>();
            map.put("data", str);
            return map;
        }
    }

    //key 转小写
    public static Map<String, Object> getRequestData(HttpServletRequest request) {
        Map<String, Object> req;
        req = getRequestMap(request);
        if (req == null) {
            req = getParameterMap(request);
        }

        Map<String, Object> reqT = new HashMap<>();
        for (Map.Entry<String, Object> set : req.entrySet())
            reqT.put(set.getKey().toLowerCase(), set.getValue());

        return reqT;
    }

    public static String getRequestURI(HttpServletRequest request, String uri) {
        int port = request.getServerPort();
        if (port == 80) {
            return "http://" + request.getServerName()
                    + request.getContextPath() + "/" + uri;
        } else {
            return "http://" + request.getServerName() + ":" + port
                    + request.getContextPath() + "/" + uri;
        }
    }
    *
    public static Map<String, Object> getParameterMap(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        Map<String, String[]> tempMap = request.getParameterMap();
        Set<String> keys = tempMap.keySet();
        for (String key : keys) {
//            byte source [] = request.getParameter(key).getBytes("iso8859-1");
//            String modelname = new String (source,"UTF-8");
            resultMap.put(key.toLowerCase(), request.getParameter(key));
        }
        System.out.println(resultMap);

        String auth = String.valueOf(resultMap.get("auth"));
        //权限判断
        //,.....

//        resultMap.remove("auth");
        return resultMap;
    }

    public static Map<String, String> getParameterMapStr(HttpServletRequest request) {
        Map<String, String> resultMap = new HashMap<String, String>();

        Map<String, String[]> tempMap = request.getParameterMap();
        Set<String> keys = tempMap.keySet();
        for (String key : keys) {
            if (request.getParameter(key) != null)
                resultMap.put(key.toLowerCase(), request.getParameter(key));
        }
//        System.out.println(resultMap);
        return resultMap;
    }
*/

    /*生成随机*/
    public static String getRandomCode() {
        Random rand = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            result.append(rand.nextInt(10));
        }
        return result.toString();
    }

    /*生成随机数字*/
    public static int getRandomInt(int max, int min) {
        Random random = new Random();
        if (max-min<2)
            return max;
        if (max > min)
            return random.nextInt(max) % (max - min + 1) + min;
        else
            return random.nextInt(min+1) % (min - max + 1) + max;

    }    /*生成随机数字*/

    public static double getRandomInt(double max, double min) {
        max = max * 100;
        min = min * 100;
        int maxI = (Double.valueOf(max).intValue());
        int minI = (Double.valueOf(min).intValue());
        Random random = new Random();
        if (max > min)
            return (random.nextInt(maxI) % (maxI - minI + 1) + minI) / 100.0;
        else
            return (random.nextInt(minI) % (minI - maxI + 1) + maxI) / 100.0;

    }


    /*生成随机小数*/
    public static double getRandomDouble() {
        Random random = new Random();

        return random.nextDouble();
    }

    /*生成短信验证码*/
    public static String getStringRandom(int length) {

        StringBuilder val = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (random.nextInt(26) + temp));
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val.append(String.valueOf(random.nextInt(10)));
            }
        }
        return val.toString().toLowerCase();
    }

    /*生成令牌*/
    public static String getToken(String device, Integer userid) {
        return toMd5(device + userid + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
    }

    /*生成文件名*/
    public static String getSaveName(Date date, String fileName, int num) {
        return DateFormatUtils.format(date, "yyyyMMddHHmmssSSS") + "_" + num
                + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /*md5加密 */
    public static String toMd5(String text) {
        MessageDigest msgDigest = null;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "System doesn't support MD5 algorithm.");
        }
        try {
            msgDigest.update(text.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(
                    "System doesn't support your  EncodingException.");
        }
        byte[] bytes = msgDigest.digest();
        String md5Str = new String(encodeHex(bytes));
        return md5Str;
    }

    public static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return out;
    }

    /*字节数组*/
    public static byte[] decode2Hex(String nm) {
        int len = nm.length();
        byte[] result = new byte[len / 2];
        for (int i = 0; i < len; i++) {
            char c = nm.charAt(i);
            byte b = Byte.decode("0x" + c);
            c = nm.charAt(++i);
            result[i / 2] = (byte) (b << 4 | Byte.decode("0x" + c));
        }
        return result;
    }

    /* 字节数组转话未16进制 */
    public static String bytes2HexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0)
            return null;
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    /*保留小数 */
    public static float toFixed(float f, Integer len) {
        Long mup = new Double(Math.pow(10, len)).longValue();
        return Math.round(f * mup) / mup.floatValue();
    }

    /*拼接字符串*/
    public static String array2String(String[] ary, String split) {
        if (ary == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ary.length; i++) {
            sb.append(ary[i]);
            if (i != (ary.length - 1))
                sb.append(split);
        }
        return sb.toString();
    }

    public static String uNull(String object) {

        return object == null ? "" : object;
    }

    /* 生成UUID */
    public static String getUUID() {
        UUID id = UUID.randomUUID();
        String idStr = id.toString();
        idStr = idStr.replaceAll("-", "");
        return idStr;
    }

    /*生成验证码 */
    public static String getCaptcha(int len) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < len; i++) {
            int random = new Random().nextInt(numberString.length());
            char chr = numberString.charAt(random);
            str.append(chr);
        }
        return str.toString();
    }

    /*加密手机号*/
    public static String maskingPhone(String phone) {
        int len = phone.length() - 5;
        StringBuilder mask = new StringBuilder();
        for (int i = 0; i < len; i++)
            mask.append("*");
        return phone.substring(0, 3) + mask + phone.substring(phone.length() - 2);
    }

    /* 加密电子邮件 */
    public static String maskingEmail(String email) {
        int len = email.indexOf('@') - 2;
        String mask = "";
        for (int i = 0; i < len; i++)
            mask += "*";
        return email.substring(0, 2) + mask + email.substring(email.indexOf('@'));
    }


    //sql传值时拼接
    public static String addSqlStr(String str) {
        if (str.contains("'"))
            return str;
        String strD = null;
        if (str.contains(",")) {
            if (str.endsWith(",")) {
                str = str.substring(0, str.length() - 1);
                String str1 = str.replace(",", "','");
                strD = "'" + str1 + "'";
                return strD;
            } else {
                String str1 = str.replace(",", "','");
                strD = "'" + str1 + "'";
                return strD;
            }
        } else {
            strD = "'" + str + "'";
            return strD;
        }
    }

    //double 保留两位小数
    public static String doubleKeepDecimal(Double str) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
        return df.format(str);
    }

    //按特殊字符拆分字符@return 特殊字符串 And 非特殊字符串
    public static Map<String, List<String>> splitSpecial(String formala) {
        Map<String, List<String>> varAndSpecial = new HashMap<>();
        List<String> vars = new ArrayList<>();
        List<String> specials = new ArrayList<>();
        int upnext = 0;
        for (int i = 0; i < formala.length(); i++) {
            if (isSpecialString(formala.substring(i, i + 1))) {
                vars.add(formala.substring(upnext, i));
                specials.add(formala.substring(i, i + 1));
                upnext = i + 1;
            }
            if (i == formala.length() - 1) {
                if (upnext <= i) {
                    vars.add(formala.substring(upnext, i + 1));
                }
            }

        }
        varAndSpecial.put("vars", vars);
        varAndSpecial.put("specials", specials);
        return varAndSpecial;
    }

    public static Object getObject(Object obj) {
        return obj == null ? "null" : obj;
    }


    public static boolean isSpecialString(String str) {
        String regEx = "[ _`~!@#$%^&*()+\\-=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static String mapToJson(Map map) {
        String json = "";
        try {
            json= JSONUtils.toJSONString(map);
        } catch (Exception e) {
            System.out.println("mapToJson失败:" + e.getLocalizedMessage());
        }
        return json;
    }

    public static String listToJson(List list) {
        String json = "";
        try {
            json= JSONUtils.toJSONString(list);
        } catch (Exception e) {
            System.out.println("listToJson:" + e.getLocalizedMessage());
        }
        return json;
    }

    public static String toJson(Object obj) {
        String json = "";
        try {
            json= JSONUtils.toJSONString(obj);
        } catch (Exception e) {
            System.out.println("listToJson:" + e.getLocalizedMessage());
        }
        return json;
    }


    //一维数组转化为二维数组
    public static Object[][] to2Array(Object[] oneArry) {
        Object[][] arr = new Object[1][oneArry.length];
        System.arraycopy(oneArry, 0, arr[0], 0, oneArry.length);
        return arr;
    }


    public static Object mapToBean(Map map, Class<?> beanClass) throws Exception {
        if (map == null||map.size()==0)
            return null;

        Object obj = beanClass.newInstance();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }

            field.setAccessible(true);
            Object val = map.get(field.getName());
            if (null!=val){
                if (null!=field.get(obj)){
                    if (field.getType() == String.class) {
                        field.set(obj, String.valueOf(map.get(field.getName())));
                    }else if (field.get(obj) instanceof Number) {
                        if (field.get(obj) instanceof Integer)
                            field.set(obj, Integer.valueOf(String.valueOf(map.get(field.getName()))));
                        else if (field.get(obj) instanceof Double)
                            field.set(obj, Double.valueOf(String.valueOf(map.get(field.getName()))));
                        else if (field.get(obj) instanceof Float)
                            field.set(obj, Float.valueOf(String.valueOf(map.get(field.getName()))));
                        else if (field.get(obj) instanceof Long)
                            field.set(obj, Long.valueOf(String.valueOf(map.get(field.getName()))));
                        else if (field.get(obj) instanceof Short)
                            field.set(obj, Short.valueOf(String.valueOf(map.get(field.getName()))));
                        else if (field.get(obj) instanceof Byte)
                            field.set(obj, Byte.valueOf(String.valueOf(map.get(field.getName()))));
                    } else {

                        field.set(obj, map.get(field.getName()));

                    }
                }else {
                    if (field.getType() == String.class) {
                        field.set(obj, String.valueOf(map.get(field.getName())));
                    }else
                        field.set(obj, map.get(field.getName()));

                }

            }
        }

        return obj;

    }
    public static Map beanToMap(  Object obj ) throws Exception {
        if (obj == null)
            return null;
        Map map = new HashMap();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            if (null!=field.get(obj))
            map.put(field.getName(), field.get(obj));
        }
        return map;

    }



}
