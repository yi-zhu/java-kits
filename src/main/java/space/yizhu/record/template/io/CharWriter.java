

package space.yizhu.record.template.io;

import java.io.IOException;


public class CharWriter extends Writer {

    java.io.Writer out;
    char[] chars;

    public CharWriter(int bufferSize) {
        this.chars = new char[bufferSize];
    }

    public CharWriter init(java.io.Writer writer) {
        this.out = writer;
        return this;
    }

    public void flush() throws IOException {
        out.flush();
    }

    public void close() {
        out = null;
    }

    public void write(String str, int offset, int len) throws IOException {
        while (len > chars.length) {
            write(str, offset, chars.length);
            offset += chars.length;
            len -= chars.length;
        }

        str.getChars(offset, offset + len, chars, 0);
        out.write(chars, 0, len);
    }

    public void write(String str) throws IOException {
        write(str, 0, str.length());
    }

    public void write(StringBuilder stringBuilder, int offset, int len) throws IOException {
        while (len > chars.length) {
            write(stringBuilder, offset, chars.length);
            offset += chars.length;
            len -= chars.length;
        }

        stringBuilder.getChars(offset, offset + len, chars, 0);
        out.write(chars, 0, len);
    }

    public void write(StringBuilder stringBuilder) throws IOException {
        write(stringBuilder, 0, stringBuilder.length());
    }

    public void write(IWritable writable) throws IOException {
        char[] data = writable.getChars();
        out.write(data, 0, data.length);
    }

    public void write(int intValue) throws IOException {
        IntegerWriter.write(this, intValue);
    }

    public void write(long longValue) throws IOException {
        LongWriter.write(this, longValue);
    }

    public void write(double doubleValue) throws IOException {
        FloatingWriter.write(this, doubleValue);
    }

    public void write(float floatValue) throws IOException {
        FloatingWriter.write(this, floatValue);
    }

    private static final char[] TRUE_CHARS = "true".toCharArray();
    private static final char[] FALSE_CHARS = "false".toCharArray();

    public void write(boolean booleanValue) throws IOException {
        out.write(booleanValue ? TRUE_CHARS : FALSE_CHARS);
    }
}






