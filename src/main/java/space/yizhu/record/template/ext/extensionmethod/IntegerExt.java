

package space.yizhu.record.template.ext.extensionmethod;


public class IntegerExt {

    public Boolean toBoolean(Integer self) {
        return self != 0;
    }

    public Integer toInt(Integer self) {
        return self;
    }

    public Long toLong(Integer self) {
        return self.longValue();
    }

    public Float toFloat(Integer self) {
        return self.floatValue();
    }

    public Double toDouble(Integer self) {
        return self.doubleValue();
    }

    public Short toShort(Integer self) {
        return self.shortValue();
    }

    public Byte toByte(Integer self) {
        return self.byteValue();
    }
}



