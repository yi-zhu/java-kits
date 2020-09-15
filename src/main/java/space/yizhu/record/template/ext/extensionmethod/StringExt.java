

package space.yizhu.record.template.ext.extensionmethod;

import space.yizhu.kits.StrKit;


public class StringExt {

    
    public Boolean toBoolean(String self) {
        if (StrKit.isBlank(self)) {
            return null;    
        }

        String value = self.trim().toLowerCase();
        if ("true".equals(value) || "1".equals(value)) {    
            return Boolean.TRUE;
        } else if ("false".equals(value) || "0".equals(value)) {
            return Boolean.FALSE;
        } else {
            throw new RuntimeException("Can not parse to boolean type of value: \"" + self + "\"");
        }
    }

    public Integer toInt(String self) {
        return StrKit.isBlank(self) ? null : Integer.parseInt(self);
    }

    public Long toLong(String self) {
        return StrKit.isBlank(self) ? null : Long.parseLong(self);
    }

    public Float toFloat(String self) {
        return StrKit.isBlank(self) ? null : Float.parseFloat(self);
    }

    public Double toDouble(String self) {
        return StrKit.isBlank(self) ? null : Double.parseDouble(self);
    }

    public Short toShort(String self) {
        return StrKit.isBlank(self) ? null : Short.parseShort(self);
    }

    public Byte toByte(String self) {
        return StrKit.isBlank(self) ? null : Byte.parseByte(self);
    }
}




