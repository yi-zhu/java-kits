

package space.yizhu.record.template.ext.extensionmethod;


public class DoubleExt {

    public Boolean toBoolean(Double self) {
        return self != 0;
    }

    public Integer toInt(Double self) {
        return self.intValue();
    }

    public Long toLong(Double self) {
        return self.longValue();
    }

    public Float toFloat(Double self) {
        return self.floatValue();
    }

    public Double toDouble(Double self) {
        return self;
    }

    public Short toShort(Double self) {
        return self.shortValue();
    }

    public Byte toByte(Double self) {
        return self.byteValue();
    }
}



