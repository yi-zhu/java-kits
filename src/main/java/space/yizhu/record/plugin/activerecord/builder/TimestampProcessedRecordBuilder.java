

package space.yizhu.record.plugin.activerecord.builder;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import space.yizhu.record.plugin.activerecord.CPI;
import space.yizhu.record.plugin.activerecord.Config;
import space.yizhu.record.plugin.activerecord.ModelBuilder;
import space.yizhu.record.plugin.activerecord.Record;
import space.yizhu.record.plugin.activerecord.RecordBuilder;


public class TimestampProcessedRecordBuilder extends RecordBuilder {

    public static final TimestampProcessedRecordBuilder me = new TimestampProcessedRecordBuilder();

    @SuppressWarnings("unchecked")
    public List<Record> build(Config config, ResultSet rs) throws SQLException {
        List<Record> result = new ArrayList<Record>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String[] labelNames = new String[columnCount + 1];
        int[] types = new int[columnCount + 1];
        buildLabelNamesAndTypes(rsmd, labelNames, types);
        while (rs.next()) {
            Record record = new Record();
            CPI.setColumnsMap(record, config.getContainerFactory().getColumnsMap());
            Map<String, Object> columns = record.getColumns();
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
                        value = ModelBuilder.me.handleClob(rs.getClob(i));
                    } else if (types[i] == Types.NCLOB) {
                        value = ModelBuilder.me.handleClob(rs.getNClob(i));
                    } else if (types[i] == Types.BLOB) {
                        value = ModelBuilder.me.handleBlob(rs.getBlob(i));
                    } else {
                        value = rs.getObject(i);
                    }
                }

                columns.put(labelNames[i], value);
            }
            result.add(record);
        }
        return result;
    }
}




