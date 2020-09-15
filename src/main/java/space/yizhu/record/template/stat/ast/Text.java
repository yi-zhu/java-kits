

package space.yizhu.record.template.stat.ast;

import java.io.IOException;
import java.nio.charset.Charset;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.io.IWritable;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Scope;


public class Text extends Stat implements IWritable {

    
    
    private StringBuilder content;
    private Charset charset;
    private byte[] bytes;
    private char[] chars;

    
    public Text(StringBuilder content, String encoding) {
        this.content = content;
        this.charset = Charset.forName(encoding);
        this.bytes = null;
        this.chars = null;
    }

    public void exec(Env env, Scope scope, Writer writer) {
        try {
            writer.write(this);
        } catch (IOException e) {
            throw new TemplateException(e.getMessage(), location, e);
        }
    }

    public byte[] getBytes() {
        if (bytes != null) {
            return bytes;
        }

        synchronized (this) {
            if (bytes != null) {
                return bytes;
            }

            if (content != null) {
                bytes = content.toString().getBytes(charset);
                content = null;
                return bytes;
            } else {
                bytes = new String(chars).getBytes(charset);
                return bytes;
            }
        }
    }

    public char[] getChars() {
        if (chars != null) {
            return chars;
        }

        synchronized (this) {
            if (chars != null) {
                return chars;
            }

            if (content != null) {
                char[] charsTemp = new char[content.length()];
                content.getChars(0, content.length(), charsTemp, 0);
                chars = charsTemp;
                content = null;
                return chars;
            } else {
                String strTemp = new String(bytes, charset);
                char[] charsTemp = new char[strTemp.length()];
                strTemp.getChars(0, strTemp.length(), charsTemp, 0);
                chars = charsTemp;
                return chars;
            }
        }
    }

    public boolean isEmpty() {
        if (content != null) {
            return content.length() == 0;
        } else if (bytes != null) {
            return bytes.length == 0;
        } else {
            return chars.length == 0;
        }
    }





    public String toString() {
        if (bytes != null) {
            return new String(bytes, charset);
        } else if (chars != null) {
            return new String(chars);
        } else {
            return content.toString();
        }
    }
}



