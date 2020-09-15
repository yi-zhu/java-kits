

package space.yizhu.record.plugin.activerecord.builder;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import space.yizhu.record.plugin.activerecord.CPI;
import space.yizhu.record.plugin.activerecord.Model;
import space.yizhu.record.plugin.activerecord.ModelBuilder;


public class KeepByteAndShortModelBuilder extends ModelBuilder {

    public static final KeepByteAndShortModelBuilder me = new KeepByteAndShortModelBuilder();

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> List<T> build(ResultSet rs, Class<? extends Model> modelClass) throws SQLException, ReflectiveOperationException {
        List<T> result = new ArrayList<T>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String[] labelNames = new String[columnCount + 1];
        int[] types = new int[columnCount + 1];
        buildLabelNamesAndTypes(rsmd, labelNames, types);
        while (rs.next()) {
            Model<?> ar = modelClass.newInstance();
            Map<String, Object> attrs = CPI.getAttrs(ar);
            for (int i = 1; i <= columnCount; i++) {
                Object value;
                int t = types[i];
                if (t < Types.DATE) {
                    if (t == Types.TINYINT) {
                        value = BuilderKit.getByte(rs, i);
                    } else if (t == Types.SMALLINT) {
                        value = BuilderKit.getShort(rs, i);
                    } else {
                        value = rs.getObject(i);
                    }
                } else {
                    if (t == Types.TIMESTAMP) {
                        value = rs.getTimestamp(i);
                    } else if (t == Types.DATE) {
                        value = rs.getDate(i);
                    } else if (t == Types.CLOB) {
                        value = handleClob(rs.getClob(i));
                    } else if (t == Types.NCLOB) {
                        value = handleClob(rs.getNClob(i));
                    } else if (t == Types.BLOB) {
                        value = handleBlob(rs.getBlob(i));
                    } else {
                        value = rs.getObject(i);
                    }
                }

                attrs.put(labelNames[i], value);
            }
            result.add((T) ar);
        }
        return result;
    }
}



