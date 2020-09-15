

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


public class TimestampProcessedModelBuilder extends ModelBuilder {

    public static final TimestampProcessedModelBuilder me = new TimestampProcessedModelBuilder();

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
                if (types[i] < Types.DATE) {
                    value = rs.getObject(i);
                } else {
                    if (types[i] == Types.TIMESTAMP) {
                        value = rs.getTimestamp(i);
                    } else if (types[i] == Types.DATE) {
                        value = rs.getDate(i);
                    } else if (types[i] == Types.CLOB) {
                        value = handleClob(rs.getClob(i));
                    } else if (types[i] == Types.NCLOB) {
                        value = handleClob(rs.getNClob(i));
                    } else if (types[i] == Types.BLOB) {
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



