

package space.yizhu.record.template.io;

import java.io.IOException;
import java.io.OutputStream;


public class ByteWriter extends Writer {

    OutputStream out;
    Encoder encoder;

    char[] chars;
    byte[] bytes;

    public ByteWriter(Encoder encoder, int bufferSize) {
        this.encoder = encoder;
        this.chars = new char[bufferSize];
        this.bytes = new byte[bufferSize * ((int) encoder.maxBytesPerChar())];
    }

    public ByteWriter init(OutputStream outputStream) {
        this.out = outputStream;
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
        int byteLen = encoder.encode(chars, 0, len, bytes);
        out.write(bytes, 0, byteLen);
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
        int byteLen = encoder.encode(chars, 0, len, bytes);
        out.write(bytes, 0, byteLen);
    }

    public void write(StringBuilder stringBuilder) throws IOException {
        write(stringBuilder, 0, stringBuilder.length());
    }

    public void write(IWritable writable) throws IOException {
        byte[] data = writable.getBytes();
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

    private static final byte[] TRUE_BYTES = "true".getBytes();
    private static final byte[] FALSE_BYTES = "false".getBytes();

    public void write(boolean booleanValue) throws IOException {
        out.write(booleanValue ? TRUE_BYTES : FALSE_BYTES);
    }
}



