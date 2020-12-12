

package space.yizhu.record.plugin.activerecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;


@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class CPI {

    
    public static final Map<String, Object> getAttrs(Model model) {
        return model._getAttrs();
    }

    public static final Set<String> getModifyFlag(Model model) {
        return model._getModifyFlag();
    }

    public static final Table getTable(Model model) {
        return model._getTable();
    }

    public static final Config getConfig(Model model) {
        return model._getConfig();
    }

    public static final Class<? extends Model> getUsefulClass(Model model) {
        return model._getUsefulClass();
    }

    public static <T> List<T> query(Connection conn, String sql, Object... paras) throws SQLException {
        return Db.query(DbConfig.config, conn, sql, paras);
    }

    public static <T> List<T> query(String configName, Connection conn, String sql, Object... paras) throws SQLException {
        return Db.query(DbConfig.getConfig(configName), conn, sql, paras);
    }

    

    public static void setColumnsMap(Record record, Map<String, Object> columns) {
        record.setColumnsMap(columns);
    }

    public static List<Record> find(Connection conn, String sql, Object... paras) throws SQLException {
        return Db.find(DbConfig.config, conn, sql, paras);
    }

    public static List<Record> find(String configName, Connection conn, String sql, Object... paras) throws SQLException {
        return Db.find(DbConfig.getConfig(configName), conn, sql, paras);
    }

    public static Page<Record> paginate(Connection conn, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) throws SQLException {
        return Db.paginate(DbConfig.config, conn, pageNumber, pageSize, select, sqlExceptSelect, paras);
    }

    public static Page<Record> paginate(String configName, Connection conn, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) throws SQLException {
        return Db.paginate(DbConfig.getConfig(configName), conn, pageNumber, pageSize, select, sqlExceptSelect, paras);
    }

    public static int update(Connection conn, String sql, Object... paras) throws SQLException {
        return Db.update(DbConfig.config, conn, sql, paras);
    }

    public static int update(String configName, Connection conn, String sql, Object... paras) throws SQLException {
        return Db.update(DbConfig.getConfig(configName), conn, sql, paras);
    }

    public static void setTablePrimaryKey(Table table, String primaryKey) {
        table.setPrimaryKey(primaryKey);
    }
}

