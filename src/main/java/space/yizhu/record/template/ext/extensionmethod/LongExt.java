

package space.yizhu.record.template.ext.extensionmethod;


public class LongExt {

    public Boolean toBoolean(Long self) {
        return self != 0;
    }

    public Integer toInt(Long self) {
        return self.intValue();
    }

    public Long toLong(Long self) {
        return self;
    }

    public Float toFloat(Long self) {
        return self.floatValue();
    }

    public Double toDouble(Long self) {
        return self.doubleValue();
    }

    public Short toShort(Long self) {
        return self.shortValue();
    }

    public Byte toByte(Long self) {
        return self.byteValue();
    }
}



