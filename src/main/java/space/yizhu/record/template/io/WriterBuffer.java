

package space.yizhu.record.template.io;


public class WriterBuffer {

    private static final int MIN_BUFFER_SIZE = 64;                    
    private static final int MAX_BUFFER_SIZE = 1024 * 1024 * 10;        

    private int bufferSize = 2048;                                    

    private EncoderFactory encoderFactory = new EncoderFactory();

    private final ThreadLocal<ByteWriter> byteWriters = new ThreadLocal<ByteWriter>() {
        protected ByteWriter initialValue() {
            return new ByteWriter(encoderFactory.getEncoder(), bufferSize);
        }
    };

    private final ThreadLocal<CharWriter> charWriters = new ThreadLocal<CharWriter>() {
        protected CharWriter initialValue() {
            return new CharWriter(bufferSize);
        }
    };

    private final ThreadLocal<FastStringWriter> fastStringWriters = new ThreadLocal<FastStringWriter>() {
        protected FastStringWriter initialValue() {
            return new FastStringWriter();
        }
    };

    public ByteWriter getByteWriter(java.io.OutputStream outputStream) {
        return byteWriters.get().init(outputStream);
    }

    public CharWriter getCharWriter(java.io.Writer writer) {
        return charWriters.get().init(writer);
    }

    public FastStringWriter getFastStringWriter() {
        return fastStringWriters.get();
    }

    public void setBufferSize(int bufferSize) {
        if (bufferSize < MIN_BUFFER_SIZE || bufferSize > MAX_BUFFER_SIZE) {
            throw new IllegalArgumentException("bufferSize must between " + (MIN_BUFFER_SIZE - 1) + " and " + (MAX_BUFFER_SIZE + 1));
        }
        this.bufferSize = bufferSize;
    }

    public void setEncoderFactory(EncoderFactory encoderFactory) {
        if (encoderFactory == null) {
            throw new IllegalArgumentException("encoderFactory can not be null");
        }
        this.encoderFactory = encoderFactory;
    }

    public void setEncoding(String encoding) {
        encoderFactory.setEncoding(encoding);
    }
}








