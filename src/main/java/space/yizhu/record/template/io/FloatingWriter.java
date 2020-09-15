

package space.yizhu.record.template.io;

import java.io.IOException;


public class FloatingWriter {

    public static void write(ByteWriter byteWriter, double doubleValue) throws IOException {
        FloatingDecimal fd = new FloatingDecimal(doubleValue);
        char[] chars = byteWriter.chars;
        byte[] bytes = byteWriter.bytes;
        int len = fd.getChars(chars);
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) chars[i];
        }
        byteWriter.out.write(bytes, 0, len);
    }

    public static void write(ByteWriter byteWriter, float floatValue) throws IOException {
        FloatingDecimal fd = new FloatingDecimal(floatValue);
        char[] chars = byteWriter.chars;
        byte[] bytes = byteWriter.bytes;
        int len = fd.getChars(chars);
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) chars[i];
        }
        byteWriter.out.write(bytes, 0, len);
    }

    public static void write(CharWriter charWriter, double doubleValue) throws IOException {
        FloatingDecimal fd = new FloatingDecimal(doubleValue);
        char[] chars = charWriter.chars;
        int len = fd.getChars(chars);
        charWriter.out.write(chars, 0, len);
    }

    public static void write(CharWriter charWriter, float floatValue) throws IOException {
        FloatingDecimal fd = new FloatingDecimal(floatValue);
        char[] chars = charWriter.chars;
        int len = fd.getChars(chars);
        charWriter.out.write(chars, 0, len);
    }
}






