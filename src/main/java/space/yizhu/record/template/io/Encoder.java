

package space.yizhu.record.template.io;


public abstract class Encoder {

    public abstract float maxBytesPerChar();

    public abstract int encode(char[] chars, int offset, int len, byte[] bytes);
}

