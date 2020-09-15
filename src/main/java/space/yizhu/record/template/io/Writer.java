

package space.yizhu.record.template.io;

import java.io.IOException;
import java.util.Date;


public abstract class Writer {

    protected DateFormats formats = new DateFormats();

    public abstract void flush() throws IOException;

    public abstract void close();

    public abstract void write(IWritable writable) throws IOException;

    public abstract void write(String string, int offset, int length) throws IOException;

    public abstract void write(String string) throws IOException;

    public abstract void write(StringBuilder stringBuilder, int offset, int length) throws IOException;

    public abstract void write(StringBuilder stringBuilder) throws IOException;

    public abstract void write(boolean booleanValue) throws IOException;

    public abstract void write(int intValue) throws IOException;

    public abstract void write(long longValue) throws IOException;

    public abstract void write(double doubleValue) throws IOException;

    public abstract void write(float floatValue) throws IOException;

    public void write(short shortValue) throws IOException {
        write((int) shortValue);
    }

    public void write(Date date, String datePattern) throws IOException {
        String str = formats.getDateFormat(datePattern).format(date);
        write(str, 0, str.length());
    }
}







