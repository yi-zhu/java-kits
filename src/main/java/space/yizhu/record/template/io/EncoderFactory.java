

package space.yizhu.record.template.io;

import java.nio.charset.Charset;

import space.yizhu.record.template.EngineConfig;


public class EncoderFactory {

    protected Charset charset = Charset.forName(EngineConfig.DEFAULT_ENCODING);

    void setEncoding(String encoding) {
        charset = Charset.forName(encoding);
    }

    public Encoder getEncoder() {
        if (Charset.forName("UTF-8").equals(charset)) {
            return Utf8Encoder.me;
        } else {
            return new JdkEncoder(charset);
        }
    }
}




