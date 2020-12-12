package space.yizhu.kits;/* Created by xiuxi on 2018/10/9.*/

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Double.NaN;
import static space.yizhu.kits.ToolKit.encodeHex;

public class CharKit {

    public static Base64.Encoder encode = Base64.getEncoder();
    public static Base64.Decoder decode = Base64.getDecoder();

    public static String stringToHex(String str) {

        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        return hex.toString();
    }

    public static String convertHexToString(String hex) {


        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }

        return sb.toString();
    }

    //将字符串转换成二进制字符串，以空格相隔
    public static String StrToBinstr(String str) {
        char[] strChar = str.toCharArray();
        String result = "";
        for (int i = 0; i < strChar.length; i++) {
            result += Integer.toBinaryString(strChar[i]) + " ";
        }

        return result;
    }

    //将二进制字符串转换为char
    public static String BinstrToChar(String binStr) {
        int[] temp = BinstrToIntArray(binStr);
        int sum = 0;
        for (int i = 0; i < temp.length; i++) {
            sum += temp[temp.length - 1 - i] << i;
        }
        return String.valueOf((char) sum);
    }

    //将二进制字符串转换成int数组
    public static int[] BinstrToIntArray(String binStr) {
        char[] temp = binStr.toCharArray();
        int[] result = new int[temp.length];
        for (int i = 0; i < temp.length; i++) {
            result[i] = temp[i] - 48;
        }
        return result;
    }

    public static String hexToBase64Str(Integer hex) {
        return (new String(encode.encode(BinstrToChar(Integer.toBinaryString(hex)).getBytes(StandardCharsets.ISO_8859_1)), StandardCharsets.UTF_8));
    }

    public static String hexToBase64Str(String hexs) {
        String base = "";
        for (int i = 0; i < Math.round(hexs.length() / 2); i++) {
            base += BinstrToChar(Integer.toBinaryString(Integer.parseInt(hexs.substring(i * 2, i * 2 + 2), 16)));
        }

        return (new String(encode.encode(base.getBytes(StandardCharsets.ISO_8859_1)), StandardCharsets.UTF_8));
    }

    public static String hexToStr(String hexs) {
        String base = "";
        for (int i = 0; i < Math.round(hexs.length() / 2); i++) {
            base += BinstrToChar(Integer.toBinaryString(Integer.parseInt(hexs.substring(i * 2, i * 2 + 2), 16)));
        }

        return (new String(base.getBytes(StandardCharsets.ISO_8859_1)));
    }

    public static String base64ToHexStr(String base64) {

        byte[] bytes = decode.decode(base64.getBytes(StandardCharsets.ISO_8859_1));
        return ByteKit.byte2str(bytes);

    }


    public static byte[] hexToBase64Byte(Integer hex) {
        return
                encode.encode(BinstrToChar(Integer.toBinaryString(hex)).getBytes(StandardCharsets.ISO_8859_1));
    }

    public static String hexStringToUByte(String str) {
        int m = 0, n = 0;
        int l = str.length() / 2;
        String ret = "";
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            int temp = Integer.decode("0x" + str.substring(i * 2, m) + str.substring(m, n));
            if (temp > 127) ret += (temp - 256);
            else ret += temp;
        }
        return ret;
    }

    //double 保留两位小数
    public static String doubleKeepDecimal(Double dol) {
        try {
            if (dol.equals(NaN))
                return "-";
            java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
            return df.format(dol);
        } catch (Exception e) {
            return "0.0";
        }
    }

    public static boolean isSpecialString(String str) {
        String regEx = "[ _`~!@#$%^&*()+\\-=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static boolean isSpecialString(String str, String exclude) {
        String regEx = "[ _`~!@#$%^&*()+\\-=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t".replace(exclude, "");
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static String md5(String text) {
        MessageDigest msgDigest = null;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            SysKit.print(e);
        }
        if (msgDigest != null) {
            msgDigest.update(text.getBytes(StandardCharsets.UTF_8));
        }
        byte[] bytes = msgDigest.digest();
        return new String(encodeHex(bytes));
    }
    public static boolean isNotNull(String str){
        if (null==str||str.length()==0||str.equals("null")||str.equals("undefined")){
            return false;
        } else return true;
    }
    public static String catTail(String str) {
        return catTail(str, 1);
    }
    public static String catTail(String str, int len) {
        if (null == str) {
            return null;
        }
        if (len < 0) {
            return str;
        }

        if (len > str.length())
            return "";
        else
            return str.substring(0, str.length() - len);
    }
    public static String catHead(String str) {
        return catHead(str, 1);
    }
    public static String catHead(String str, int len) {
        if (null == str) {
            return null;
        }
        if (len < 0) {
            return str;
        }

        if (len > str.length())
            return "";
        else
            return str.substring(len);
    }



    public String toStrings(String a) {
        String[] arr = a.split("\\s+");
        String sss = "";
        for (String ss : arr) {
            sss = sss + BinstrToChar(ss);
        }
        return sss;
    }

}
