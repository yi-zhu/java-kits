

package space.yizhu.record.plugin.activerecord.dialect;

import space.yizhu.record.plugin.activerecord.*;
import space.yizhu.record.plugin.activerecord.*;
import space.yizhu.record.plugin.activerecord.builder.KeepByteAndShortModelBuilder;
import space.yizhu.record.plugin.activerecord.builder.KeepByteAndShortRecordBuilder;
import org.postgresql.util.PGobject;
import space.yizhu.kits.DateKit;

import java.math.BigInteger;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


public abstract class Dialect {

    
    protected boolean keepByteAndShort = false;
    protected ModelBuilder modelBuilder = ModelBuilder.me;
    protected RecordBuilder recordBuilder = RecordBuilder.me;

    
    public abstract String forTableBuilderDoBuild(String tableName);

    public abstract String forPaginate(int pageNumber, int pageSize, StringBuilder findSql);

    
    public abstract String forModelFindById(Table table, String columns);

    public abstract String forModelDeleteById(Table table);

    public abstract void forModelSave(Table table, Map<String, Object> attrs, StringBuilder sql, List<Object> paras);

    public abstract void forModelUpdate(Table table, Map<String, Object> attrs, Set<String> modifyFlag, StringBuilder sql, List<Object> paras);

    
    public abstract String forDbFindById(String tableName, String[] pKeys);

    public abstract String forDbDeleteById(String tableName, String[] pKeys);

    public abstract void forDbSave(String tableName, String[] pKeys, Record record, StringBuilder sql, List<Object> paras);

    public abstract void forDbUpdate(String tableName, String[] pKeys, Object[] ids, Record record, StringBuilder sql, List<Object> paras);

    public String forFindAll(String tableName) {
        return "select * from " + tableName;
    }

    
    public boolean isKeepByteAndShort() {
        return keepByteAndShort;
    }

    
    public Dialect setKeepByteAndShort(boolean keepByteAndShort) {
        this.keepByteAndShort = keepByteAndShort;
        
        if (keepByteAndShort) {
            if (modelBuilder.getClass() == ModelBuilder.class) {
                modelBuilder = KeepByteAndShortModelBuilder.me;
            }
            if (recordBuilder.getClass() == RecordBuilder.class) {
                recordBuilder = KeepByteAndShortRecordBuilder.me;
            }
        } else {
            if (modelBuilder.getClass() == KeepByteAndShortModelBuilder.class) {
                modelBuilder = ModelBuilder.me;
            }
            if (recordBuilder.getClass() == KeepByteAndShortRecordBuilder.class) {
                recordBuilder = RecordBuilder.me;
            }
        }
        return this;
    }

    
    public Dialect setModelBuilder(ModelBuilder modelBuilder) {
        this.modelBuilder = modelBuilder;
        return this;
    }

    
    public Dialect setRecordBuilder(RecordBuilder recordBuilder) {
        this.recordBuilder = recordBuilder;
        return this;
    }

    @SuppressWarnings("rawtypes")
    public <T> List<T> buildModelList(ResultSet rs, Class<? extends Model> modelClass) throws SQLException, ReflectiveOperationException {
        return modelBuilder.build(rs, modelClass);
    }

    public List<Record> buildRecordList(Config config, ResultSet rs) throws SQLException {
        return recordBuilder.build(config, rs);
    }

    
    public void getModelGeneratedKey(Model<?> model, PreparedStatement pst, Table table) throws SQLException {
        String[] pKeys = table.getPrimaryKey();
        ResultSet rs = pst.getGeneratedKeys();
        for (String pKey : pKeys) {
            if (model.get(pKey) == null || isOracle()) {
                if (rs.next()) {
                    Class<?> colType = table.getColumnType(pKey);
                    if (colType != null) {    
                        if (colType == Integer.class || colType == int.class) {
                            model.set(pKey, rs.getInt(1));
                        } else if (colType == Long.class || colType == long.class) {
                            model.set(pKey, rs.getLong(1));
                        } else if (colType == BigInteger.class) {
                            processGeneratedBigIntegerKey(model, pKey, rs.getObject(1));
                        } else {
                            model.set(pKey, rs.getObject(1));    
                        }
                    }
                }
            }
        }
        rs.close();
    }

    
    protected void processGeneratedBigIntegerKey(Model<?> model, String pKey, Object v) {
        if (v instanceof BigInteger) {
            model.set(pKey, (BigInteger) v);
        } else if (v instanceof Number) {
            Number n = (Number) v;
            model.set(pKey, BigInteger.valueOf(n.longValue()));
        } else {
            model.set(pKey, v);
        }
    }

    
    public void getRecordGeneratedKey(PreparedStatement pst, Record record, String[] pKeys) throws SQLException {
        ResultSet rs = pst.getGeneratedKeys();
        for (String pKey : pKeys) {
            if (record.get(pKey) == null || isOracle()) {
                if (rs.next()) {
                    record.set(pKey, rs.getObject(1));    
                }
            }
        }
        rs.close();
    }

    public boolean isOracle() {
        return false;
    }

    public boolean isTakeOverDbPaginate() {
        return false;
    }

    public Page<Record> takeOverDbPaginate(Connection conn, int pageNumber, int pageSize, Boolean isGroupBySql, String totalRowSql, StringBuilder findSql, Object... paras) throws SQLException {
        throw new RuntimeException("You should implements this method in " + getClass().getName());
    }

    public boolean isTakeOverModelPaginate() {
        return false;
    }

    @SuppressWarnings("rawtypes")
    public Page takeOverModelPaginate(Connection conn, Class<? extends Model> modelClass, int pageNumber, int pageSize, Boolean isGroupBySql, String totalRowSql, StringBuilder findSql, Object... paras) throws Exception {
        throw new RuntimeException("You should implements this method in " + getClass().getName());
    }

    public void fillStatement(PreparedStatement pst, List<Object> paras) throws SQLException {
        for (int i = 0, size = paras.size(); i < size; i++) {
            pst.setObject(i + 1, paras.get(i));
        }
    }

    public void fillStatement(PreparedStatement pst, Object... paras) throws SQLException {
        for (int i = 0; i < paras.length; i++) {
            pst.setObject(i + 1, paras[i]);
        }
    }

    public String getDefaultPrimaryKey() {
        return "id";
    }

    public boolean isPrimaryKey(String colName, String[] pKeys) {
        for (String pKey : pKeys) {
            if (colName.equalsIgnoreCase(pKey)) {
                return true;
            }
        }
        return false;
    }

    
    public void trimPrimaryKeys(String[] pKeys) {
        for (int i = 0; i < pKeys.length; i++) {
            pKeys[i] = pKeys[i].trim();
        }
    }

    public String replaceOrderBy(String sql) {
        return Holder.ORDER_BY_PATTERN.matcher(sql).replaceAll("");
    }

    
    protected void fillStatementHandleDateType(PreparedStatement pst, List<Object> paras) throws SQLException {
        for (int i = 0, size = paras.size(); i < size; i++) {
            Object value = paras.get(i);
            doStatementType(pst, i, value);
        }
    }

    private void doStatementType(PreparedStatement pst, int i, Object value) throws SQLException {
        if (value instanceof java.util.Date) {
            if (value instanceof Date) {
                pst.setDate(i + 1, (Date) value);
            } else if (value instanceof Timestamp) {
                pst.setTimestamp(i + 1, (Timestamp) value);
            } else {
                
                java.util.Date d = (java.util.Date) value;
                pst.setTimestamp(i + 1, new Timestamp(d.getTime()));
            }
        } else if (value != null &&
                value.toString().length() - value.toString().replaceAll("-", "").length() == 2
                &&
                value.toString().length() - value.toString().replaceAll(" ", "").length() == 1
                &&
                value.toString().length() - value.toString().replaceAll(":", "").length() == 2) {
            
            pst.setTimestamp(i + 1, DateKit.string2Timestamp(value.toString()));
        } else if (value != null && ((value.toString().startsWith("[") && value.toString().endsWith("]")) || (value.toString().startsWith("{") && value.toString().endsWith("}")))) {
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(String.valueOf(value));
            pst.setObject(i + 1, jsonObject);
        } else {
            pst.setObject(i + 1, value);
        }
    }

    
    protected void fillStatementHandleDateType(PreparedStatement pst, Object... paras) throws SQLException {
        for (int i = 0; i < paras.length; i++) {
            Object value = paras[i];

            doStatementType(pst, i, value);
        }
    }

    protected static class Holder {
        
        private static final Pattern ORDER_BY_PATTERN = Pattern.compile(
                "order\\s+by\\s+[^,\\s]+(\\s+asc|\\s+desc)?(\\s*,\\s*[^,\\s]+(\\s+asc|\\s+desc)?)*",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    }
}






