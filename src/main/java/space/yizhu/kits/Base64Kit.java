

package space.yizhu.kits;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Base64;

public class Base64Kit {

    public static final Charset UTF_8 = Charset.forName("UTF-8");
    private static IBase64 delegate;

    static {
        if (isPresent("java.util.Base64", Base64Kit.class.getClassLoader())) {
            delegate = new Java8Base64();
        }
    }

    private Base64Kit() {
    }

    /**
     * 编码
     *
     * @param value byte数组
     * @return {String}
     */
    public static String encode(byte[] value) {
        return delegate.encode(value);
    }

    /**
     * 编码
     *
     * @param value 字符串
     * @return {String}
     */
    public static String encode(String value) {
        byte[] val = value.getBytes(UTF_8);
        return delegate.encode(val);
    }

    /**
     * 编码
     *
     * @param value       字符串
     * @param charsetName charSet
     * @return {String}
     */
    public static String encode(String value, String charsetName) {
        byte[] val = value.getBytes(Charset.forName(charsetName));
        return delegate.encode(val);
    }

    /**
     * 解码
     *
     * @param value 字符串
     * @return {byte[]}
     */
    public static byte[] decode(String value) {
        return delegate.decode(value);
    }

    /**
     * 解码
     *
     * @param value 字符串
     * @return {String}
     */
    public static String decodeToStr(String value) {
        byte[] decodedValue = delegate.decode(value);
        return new String(decodedValue, UTF_8);
    }

    /**
     * 解码
     *
     * @param value       字符串
     * @param charsetName 字符集
     * @return {String}
     */
    public static String decodeToStr(String value, String charsetName) {
        byte[] decodedValue = delegate.decode(value);
        return new String(decodedValue, Charset.forName(charsetName));
    }

    private static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            Class.forName(className, true, classLoader);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }

    /**
     * 将base64字符解码保存文件
     *
     * @param path
     * @return String
     */
    public static String encodeFile(String path) {
        File file = new File(path);
        FileInputStream inputFile;

        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return new BASE64Encoder().encode(buffer);

        } catch (IOException e) {
            SysKit.print(e);
            return null;
        }

    }

    /**
     * 将base64字符解码保存文件
     *
     * @param base64Code base
     * @param name 名称
     * @param savePath=nul
     * @return boolean
     */

    public static boolean decoder2File(String base64Code, String name, String savePath) {
        if (null == base64Code || base64Code.isEmpty()) {
            return false;
        }
        if (null == savePath || savePath.isEmpty()) {
            savePath = System.getProperty("user.dir");
        }
        base64Code = base64Code.substring(base64Code.indexOf("data:"));
        try {

            String postfix = base64Code.substring(base64Code.indexOf("/"), base64Code.indexOf(";"));
            byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
            FileOutputStream out = new FileOutputStream(savePath + "/" + name + "." + postfix);
            out.write(buffer);
            out.close();
            return true;
        } catch (IOException e) {
            SysKit.print(e);
            return false;
        }
    }

    /**
     * 将base64字符解码保存文件
     *
     * @param base64Code
     * @param name
     * @return boolean
     */

    public static boolean decoder2File(String base64Code, String name) {
        if (null == base64Code || base64Code.isEmpty()) {
            return false;
        }
        String savePath = System.getProperty("user.dir");

        try {
            byte[] buffer;
            FileOutputStream out;
            if (base64Code.startsWith("data:")){
                base64Code = base64Code.substring(base64Code.indexOf("data:"));
                String postfix = base64Code.substring(base64Code.indexOf("/"), base64Code.indexOf(";"));
                 buffer = Base64.getDecoder().decode(base64Code);
                out = new FileOutputStream(savePath + "/" + name + "." + postfix);
            }else {
                StringUtils.isNoneBlank(base64Code);
                if (base64Code.contains("\\n")) {
                    base64Code= base64Code.replaceAll("\\\\n","");
                }
                buffer = new BASE64Decoder().decodeBuffer(base64Code);
                 out = new FileOutputStream(savePath + "/" + name );
            }
            out.write(buffer);
            out.close();
            return true;
        } catch (IOException e) {
            SysKit.print(e);
            return false;
        }
    }



    public static void main(String[] args) {

        String base=FileKit.read(new File("D:/work/unicom/tep/base"));
        decoder2File(base,"电子证照.pdf");
        String data = "";

        try {
            decoder2File(data, "月宫");
        } catch (Exception e) {
             SysKit.print(e);
        }

    }

    static interface IBase64 {
        public String encode(byte[] value);

        public byte[] decode(String value);
    }

    static class Java8Base64 implements IBase64 {
        @Override
        public String encode(byte[] value) {
            return java.util.Base64.getEncoder().encodeToString(value);
        }

        @Override
        public byte[] decode(String value) {
            return java.util.Base64.getDecoder().decode(value);
        }
    }
}
