

package space.yizhu.record.template.io;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;


public class JdkEncoder extends Encoder {

    private CharsetEncoder ce;

    public JdkEncoder(Charset charset) {
        this.ce = charset.newEncoder();
    }

    public float maxBytesPerChar() {
        return ce.maxBytesPerChar();
    }

    public int encode(char[] chars, int offset, int len, byte[] bytes) {
        ce.reset();
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        CharBuffer cb = CharBuffer.wrap(chars, offset, len);
        try {
            CoderResult cr = ce.encode(cb, bb, true);
            if (!cr.isUnderflow())
                cr.throwException();
            cr = ce.flush(bb);
            if (!cr.isUnderflow())
                cr.throwException();
            return bb.position();
        } catch (CharacterCodingException x) {
            
            
            throw new RuntimeException("Encode error: " + x.getMessage(), x);
        }
    }
}






