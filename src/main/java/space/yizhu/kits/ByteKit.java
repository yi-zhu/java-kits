package space.yizhu.kits;/* Created by xiuxi on 2018/9/29.*/


import java.nio.charset.StandardCharsets;

public class ByteKit {
    /*    public static String bytesToHexFun2(byte[] bytes) {
            char[] buf = new char[bytes.length * 2];
            int index = 0;
            for(byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
                buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
                buf[index++] = HEX_CHAR[b & 0xf];
            }

            return new String(buf);
        }*/
    public static String bytesToHex(byte[] bytes) {

        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }

        return buf.toString();
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    private static char szbyte2str[][] = {
            {'0', '0'}, {'0', '1'}, {'0', '2'}, {'0', '3'}, {'0', '4'}, {'0', '5'}, {'0', '6'}, {'0', '7'},
            {'0', '8'}, {'0', '9'}, {'0', 'A'}, {'0', 'B'}, {'0', 'C'}, {'0', 'D'}, {'0', 'E'}, {'0', 'F'},

            {'1', '0'}, {'1', '1'}, {'1', '2'}, {'1', '3'}, {'1', '4'}, {'1', '5'}, {'1', '6'}, {'1', '7'},
            {'1', '8'}, {'1', '9'}, {'1', 'A'}, {'1', 'B'}, {'1', 'C'}, {'1', 'D'}, {'1', 'E'}, {'1', 'F'},

            {'2', '0'}, {'2', '1'}, {'2', '2'}, {'2', '3'}, {'2', '4'}, {'2', '5'}, {'2', '6'}, {'2', '7'},
            {'2', '8'}, {'2', '9'}, {'2', 'A'}, {'2', 'B'}, {'2', 'C'}, {'2', 'D'}, {'2', 'E'}, {'2', 'F'},

            {'3', '0'}, {'3', '1'}, {'3', '2'}, {'3', '3'}, {'3', '4'}, {'3', '5'}, {'3', '6'}, {'3', '7'},
            {'3', '8'}, {'3', '9'}, {'3', 'A'}, {'3', 'B'}, {'3', 'C'}, {'3', 'D'}, {'3', 'E'}, {'3', 'F'},

            {'4', '0'}, {'4', '1'}, {'4', '2'}, {'4', '3'}, {'4', '4'}, {'4', '5'}, {'4', '6'}, {'4', '7'},
            {'4', '8'}, {'4', '9'}, {'4', 'A'}, {'4', 'B'}, {'4', 'C'}, {'4', 'D'}, {'4', 'E'}, {'4', 'F'},

            {'5', '0'}, {'5', '1'}, {'5', '2'}, {'5', '3'}, {'5', '4'}, {'5', '5'}, {'5', '6'}, {'5', '7'},
            {'5', '8'}, {'5', '9'}, {'5', 'A'}, {'5', 'B'}, {'5', 'C'}, {'5', 'D'}, {'5', 'E'}, {'5', 'F'},

            {'6', '0'}, {'6', '1'}, {'6', '2'}, {'6', '3'}, {'6', '4'}, {'6', '5'}, {'6', '6'}, {'6', '7'},
            {'6', '8'}, {'6', '9'}, {'6', 'A'}, {'6', 'B'}, {'6', 'C'}, {'6', 'D'}, {'6', 'E'}, {'6', 'F'},

            {'7', '0'}, {'7', '1'}, {'7', '2'}, {'7', '3'}, {'7', '4'}, {'7', '5'}, {'7', '6'}, {'7', '7'},
            {'7', '8'}, {'7', '9'}, {'7', 'A'}, {'7', 'B'}, {'7', 'C'}, {'7', 'D'}, {'7', 'E'}, {'7', 'F'},

            {'8', '0'}, {'8', '1'}, {'8', '2'}, {'8', '3'}, {'8', '4'}, {'8', '5'}, {'8', '6'}, {'8', '7'},
            {'8', '8'}, {'8', '9'}, {'8', 'A'}, {'8', 'B'}, {'8', 'C'}, {'8', 'D'}, {'8', 'E'}, {'8', 'F'},

            {'9', '0'}, {'9', '1'}, {'9', '2'}, {'9', '3'}, {'9', '4'}, {'9', '5'}, {'9', '6'}, {'9', '7'},
            {'9', '8'}, {'9', '9'}, {'9', 'A'}, {'9', 'B'}, {'9', 'C'}, {'9', 'D'}, {'9', 'E'}, {'9', 'F'},

            {'A', '0'}, {'A', '1'}, {'A', '2'}, {'A', '3'}, {'A', '4'}, {'A', '5'}, {'A', '6'}, {'A', '7'},
            {'A', '8'}, {'A', '9'}, {'A', 'A'}, {'A', 'B'}, {'A', 'C'}, {'A', 'D'}, {'A', 'E'}, {'A', 'F'},

            {'B', '0'}, {'B', '1'}, {'B', '2'}, {'B', '3'}, {'B', '4'}, {'B', '5'}, {'B', '6'}, {'B', '7'},
            {'B', '8'}, {'B', '9'}, {'B', 'A'}, {'B', 'B'}, {'B', 'C'}, {'B', 'D'}, {'B', 'E'}, {'B', 'F'},

            {'C', '0'}, {'C', '1'}, {'C', '2'}, {'C', '3'}, {'C', '4'}, {'C', '5'}, {'C', '6'}, {'C', '7'},
            {'C', '8'}, {'C', '9'}, {'C', 'A'}, {'C', 'B'}, {'C', 'C'}, {'C', 'D'}, {'C', 'E'}, {'C', 'F'},

            {'D', '0'}, {'D', '1'}, {'D', '2'}, {'D', '3'}, {'D', '4'}, {'D', '5'}, {'D', '6'}, {'D', '7'},
            {'D', '8'}, {'D', '9'}, {'D', 'A'}, {'D', 'B'}, {'D', 'C'}, {'D', 'D'}, {'D', 'E'}, {'D', 'F'},

            {'E', '0'}, {'E', '1'}, {'E', '2'}, {'E', '3'}, {'E', '4'}, {'E', '5'}, {'E', '6'}, {'E', '7'},
            {'E', '8'}, {'E', '9'}, {'E', 'A'}, {'E', 'B'}, {'E', 'C'}, {'E', 'D'}, {'E', 'E'}, {'E', 'F'},

            {'F', '0'}, {'F', '1'}, {'F', '2'}, {'F', '3'}, {'F', '4'}, {'F', '5'}, {'F', '6'}, {'F', '7'},
            {'F', '8'}, {'F', '9'}, {'F', 'A'}, {'F', 'B'}, {'F', 'C'}, {'F', 'D'}, {'F', 'E'}, {'F', 'F'}
    };

    public static String byte2str(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            sb.append(szbyte2str[buf[i] & 0xFF][0]);
            sb.append(szbyte2str[buf[i] & 0xFF][1]);
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    private static char szstr2bin[] = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 0, 0, 0, 0, 0,
            0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    public static byte[] str2byte(String str) {
        int length = str.length();
        if (length < 1) return null;
        if (length % 2 != 0) return null;
        byte[] result = new byte[str.length() / 2];
        for (int i = 0; i < length; ) {
            char H = szstr2bin[str.charAt(i++) & 0xFF];
            char L = szstr2bin[str.charAt(i++) & 0xFF];
            result[(i / 2) - 1] = (byte) (H * 16 + L);
        }
        return result;
    }

    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }


    public static byte[] hexToByte(Integer hex) {
        return CharKit.BinstrToChar(Integer.toBinaryString(hex)).getBytes(StandardCharsets.ISO_8859_1);
    }

}
