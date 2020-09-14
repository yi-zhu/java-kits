package space.yizhu.kits;/* Created by xiuxi on 2018/10/10.*/

public class NumberKit {

    public static int getInt(String str) {
        if (str == null)
            return 0;
        if (str.contains(".")) {
            str = str.substring(0, str.indexOf("."));
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int getInt(Object obj) {
        if (obj == null)
            return 0;
        String str = String.valueOf(obj);
        if (str.contains(".")) {
            str = str.substring(0, str.indexOf("."));
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    public static String decimalToHex(int decimal) {
        String hex = "";
        while (decimal != 0) {
            int hexValue = decimal % 16;
            hex = toHexChar(hexValue) + hex;
            decimal = decimal / 16;
        }
        return hex;
    }

    //将0~15的十进制数转换成0~F的十六进制数
    public static char toHexChar(int hexValue) {
        if (hexValue <= 9 && hexValue >= 0)
            return (char) (hexValue + '0');
        else
            return (char) (hexValue - 10 + 'A');
    }
}
