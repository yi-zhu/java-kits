

package space.yizhu.record.plugin.activerecord.builder;

import java.sql.ResultSet;
import java.sql.SQLException;


public class BuilderKit {

    public static Byte getByte(ResultSet rs, int i) throws SQLException {
        Object value = rs.getObject(i);
        if (value != null) {
            value = Byte.parseByte(value + "");
            return (Byte) value;
        } else {
            return null;
        }
    }

    public static Short getShort(ResultSet rs, int i) throws SQLException {
        Object value = rs.getObject(i);
        if (value != null) {
            value = Short.parseShort(value + "");
            return (Short) value;
        } else {
            return null;
        }
    }
}

