

package space.yizhu.record.plugin.activerecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import space.yizhu.kits.LogKit;
import space.yizhu.kits.StrKit;
import space.yizhu.record.plugin.activerecord.cache.ICache;
import org.postgresql.util.PGobject;
import space.yizhu.kits.DateKit;

import static space.yizhu.record.plugin.activerecord.DbKit.NULL_PARA_ARRAY;


@SuppressWarnings({"rawtypes", "unchecked"})
public class DbPro {

    protected final Config config;

    public DbPro() {
        if (DbKit.config == null) {
            throw new RuntimeException("The main config is null, initialize ActiveRecordPlugin first");
        }
        this.config = DbKit.config;
    }

    public DbPro(String configName) {
        this.config = DbKit.getConfig(configName);
        if (this.config == null) {
            throw new IllegalArgumentException("Config not found by configName: " + configName);
        }
    }

    public Config getConfig() {
        return config;
    }

    <T> List<T> query(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        List result = new ArrayList();
        PreparedStatement pst = conn.prepareStatement(sql);
        config.dialect.fillStatement(pst, paras);
        ResultSet rs = pst.executeQuery();
        int colAmount = rs.getMetaData().getColumnCount();
        if (colAmount > 1) {
            while (rs.next()) {
                Object[] temp = new Object[colAmount];
                for (int i = 0; i < colAmount; i++) {
                    temp[i] = rs.getObject(i + 1);
                }
                result.add(temp);
            }
        } else if (colAmount == 1) {
            while (rs.next()) {
                result.add(rs.getObject(1));
            }
        }
        DbKit.close(rs, pst);
        return result;
    }

    
    public <T> List<T> query(String sql, Object... paras) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return query(config, conn, sql, paras);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
    }

    
    public <T> List<T> query(String sql) {        
        return query(sql, NULL_PARA_ARRAY);
    }

    
    public <T> T queryFirst(String sql, Object... paras) {
        List<T> result = query(sql, paras);
        return (result.size() > 0 ? result.get(0) : null);
    }

    
    public <T> T queryFirst(String sql) {
        
        List<T> result = query(sql, NULL_PARA_ARRAY);
        return (result.size() > 0 ? result.get(0) : null);
    }

    

    
    public <T> T queryColumn(String sql, Object... paras) {
        List<T> result = query(sql, paras);
        if (result.size() > 0) {
            T temp = result.get(0);
            if (temp instanceof Object[])
                throw new ActiveRecordException("Only ONE COLUMN can be queried.");
            return temp;
        }
        return null;
    }

    public <T> T queryColumn(String sql) {
        return (T) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public String queryStr(String sql, Object... paras) {
        Object s = queryColumn(sql, paras);
        return s != null ? s.toString() : null;
    }

    public String queryStr(String sql) {
        return queryStr(sql, NULL_PARA_ARRAY);
    }

    public Integer queryInt(String sql, Object... paras) {
        Number n = queryNumber(sql, paras);
        return n != null ? n.intValue() : null;
    }

    public Integer queryInt(String sql) {
        return queryInt(sql, NULL_PARA_ARRAY);
    }

    public Long queryLong(String sql, Object... paras) {
        Number n = queryNumber(sql, paras);
        return n != null ? n.longValue() : null;
    }

    public Long queryLong(String sql) {
        return queryLong(sql, NULL_PARA_ARRAY);
    }

    public Double queryDouble(String sql, Object... paras) {
        Number n = queryNumber(sql, paras);
        return n != null ? n.doubleValue() : null;
    }

    public Double queryDouble(String sql) {
        return queryDouble(sql, NULL_PARA_ARRAY);
    }

    public Float queryFloat(String sql, Object... paras) {
        Number n = queryNumber(sql, paras);
        return n != null ? n.floatValue() : null;
    }

    public Float queryFloat(String sql) {
        return queryFloat(sql, NULL_PARA_ARRAY);
    }

    public java.math.BigDecimal queryBigDecimal(String sql, Object... paras) {
        return (java.math.BigDecimal) queryColumn(sql, paras);
    }

    public java.math.BigDecimal queryBigDecimal(String sql) {
        return (java.math.BigDecimal) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public byte[] queryBytes(String sql, Object... paras) {
        return (byte[]) queryColumn(sql, paras);
    }

    public byte[] queryBytes(String sql) {
        return (byte[]) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public java.util.Date queryDate(String sql, Object... paras) {
        return (java.util.Date) queryColumn(sql, paras);
    }

    public java.util.Date queryDate(String sql) {
        return (java.util.Date) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public java.sql.Time queryTime(String sql, Object... paras) {
        return (java.sql.Time) queryColumn(sql, paras);
    }

    public java.sql.Time queryTime(String sql) {
        return (java.sql.Time) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public java.sql.Timestamp queryTimestamp(String sql, Object... paras) {
        return (java.sql.Timestamp) queryColumn(sql, paras);
    }

    public java.sql.Timestamp queryTimestamp(String sql) {
        return (java.sql.Timestamp) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public Boolean queryBoolean(String sql, Object... paras) {
        return (Boolean) queryColumn(sql, paras);
    }

    public Boolean queryBoolean(String sql) {
        return (Boolean) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public Short queryShort(String sql, Object... paras) {
        Number n = queryNumber(sql, paras);
        return n != null ? n.shortValue() : null;
    }

    public Short queryShort(String sql) {
        return queryShort(sql, NULL_PARA_ARRAY);
    }

    public Byte queryByte(String sql, Object... paras) {
        Number n = queryNumber(sql, paras);
        return n != null ? n.byteValue() : null;
    }

    public Byte queryByte(String sql) {
        return queryByte(sql, NULL_PARA_ARRAY);
    }

    public Number queryNumber(String sql, Object... paras) {
        return (Number) queryColumn(sql, paras);
    }

    public Number queryNumber(String sql) {
        return (Number) queryColumn(sql, NULL_PARA_ARRAY);
    }
    

    
    int update(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        sql = sql.replace("\"", "");
        PreparedStatement pst = conn.prepareStatement(sql);
        config.dialect.fillStatement(pst, paras);
        int result = pst.executeUpdate();
        DbKit.close(pst);
        return result;
    }

    
    public int update(String sql, Object... paras) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return update(config, conn, sql, paras);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
    }

    
    public int update(String sql) {
        return update(sql, NULL_PARA_ARRAY);
    }

    List<Record> find(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(sql);
        config.dialect.fillStatement(pst, paras);
        ResultSet rs = pst.executeQuery();
        List<Record> result = config.dialect.buildRecordList(config, rs);    
        DbKit.close(rs, pst);
        return result;
    }

    
    public List<Record> find(String sql, Object... paras) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return find(config, conn, sql, paras);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
    }

    
    public List<Record> find(String sql) {
        return find(sql, NULL_PARA_ARRAY);
    }

    public List<Record> findAll(String tableName) {
        String sql = config.dialect.forFindAll(tableName);
        return find(sql, NULL_PARA_ARRAY);
    }

    
    public Record findFirst(String sql, Object... paras) {
        List<Record> result = find(sql, paras);
        return result.size() > 0 ? result.get(0) : null;
    }

    
    public Record findFirst(String sql) {
        return findFirst(sql, NULL_PARA_ARRAY);
    }

    
    public Record findById(String tableName, Object idValue) {
        return findByIds(tableName, config.dialect.getDefaultPrimaryKey(), idValue);
    }

    public Record findById(String tableName, String primaryKey, Object idValue) {
        return findByIds(tableName, primaryKey, idValue);
    }

    
    public Record findByIds(String tableName, String primaryKey, Object... idValues) {
        String[] pKeys = primaryKey.split(",");
        if (pKeys.length != idValues.length)
            throw new IllegalArgumentException("primary key number must equals id value number");

        String sql = config.dialect.forDbFindById(tableName, pKeys);
        List<Record> result = find(sql, idValues);
        return result.size() > 0 ? result.get(0) : null;
    }

    
    public boolean deleteById(String tableName, Object idValue) {
        return deleteByIds(tableName, config.dialect.getDefaultPrimaryKey(), idValue);
    }

    public boolean deleteById(String tableName, String primaryKey, Object idValue) {
        return deleteByIds(tableName, primaryKey, idValue);
    }

    
    public boolean deleteByIds(String tableName, String primaryKey, Object... idValues) {
        String[] pKeys = primaryKey.split(",");
        if (pKeys.length != idValues.length)
            throw new IllegalArgumentException("primary key number must equals id value number");

        String sql = config.dialect.forDbDeleteById(tableName, pKeys);
        return update(sql, idValues) >= 1;
    }

    
    public boolean delete(String tableName, String primaryKey, Record record) {
        String[] pKeys = primaryKey.split(",");
        if (pKeys.length <= 1) {
            Object t = record.get(primaryKey);    
            return deleteByIds(tableName, primaryKey, t);
        }

        config.dialect.trimPrimaryKeys(pKeys);
        Object[] idValue = new Object[pKeys.length];
        for (int i = 0; i < pKeys.length; i++) {
            idValue[i] = record.get(pKeys[i]);
            if (idValue[i] == null)
                throw new IllegalArgumentException("The value of primary key \"" + pKeys[i] + "\" can not be null in record object");
        }
        return deleteByIds(tableName, primaryKey, idValue);
    }

    
    public boolean delete(String tableName, Record record) {
        String defaultPrimaryKey = config.dialect.getDefaultPrimaryKey();
        Object t = record.get(defaultPrimaryKey);    
        return deleteByIds(tableName, defaultPrimaryKey, t);
    }

    
    public int delete(String sql, Object... paras) {
        return update(sql, paras);
    }

    
    public int delete(String sql) {
        return update(sql);
    }

    
    public Page<Record> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
        return doPaginate(pageNumber, pageSize, null, select, sqlExceptSelect, paras);
    }

    
    public Page<Record> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect) {
        return doPaginate(pageNumber, pageSize, null, select, sqlExceptSelect, NULL_PARA_ARRAY);
    }

    public Page<Record> paginate(int pageNumber, int pageSize, boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras) {
        return doPaginate(pageNumber, pageSize, isGroupBySql, select, sqlExceptSelect, paras);
    }

    protected Page<Record> doPaginate(int pageNumber, int pageSize, Boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            String totalRowSql = "select count(*) " + config.dialect.replaceOrderBy(sqlExceptSelect);
            StringBuilder findSql = new StringBuilder();
            findSql.append(select).append(' ').append(sqlExceptSelect);
            return doPaginateByFullSql(config, conn, pageNumber, pageSize, isGroupBySql, totalRowSql, findSql, paras);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
    }

    protected Page<Record> doPaginateByFullSql(Config config, Connection conn, int pageNumber, int pageSize, Boolean isGroupBySql, String totalRowSql, StringBuilder findSql, Object... paras) throws SQLException {
        if (pageNumber < 1 || pageSize < 1) {
            throw new ActiveRecordException("pageNumber and pageSize must more than 0");
        }
        if (config.dialect.isTakeOverDbPaginate()) {
            return config.dialect.takeOverDbPaginate(conn, pageNumber, pageSize, isGroupBySql, totalRowSql, findSql, paras);
        }

        List result = query(config, conn, totalRowSql, paras);
        int size = result.size();
        if (isGroupBySql == null) {
            isGroupBySql = size > 1;
        }

        long totalRow;
        if (isGroupBySql) {
            totalRow = size;
        } else {
            totalRow = (size > 0) ? ((Number) result.get(0)).longValue() : 0;
        }
        if (totalRow == 0) {
            return new Page<Record>(new ArrayList<Record>(0), pageNumber, pageSize, 0, 0);
        }

        int totalPage = (int) (totalRow / pageSize);
        if (totalRow % pageSize != 0) {
            totalPage++;
        }

        if (pageNumber > totalPage) {
            return new Page<Record>(new ArrayList<Record>(0), pageNumber, pageSize, totalPage, (int) totalRow);
        }

        
        String sql = config.dialect.forPaginate(pageNumber, pageSize, findSql);
        List<Record> list = find(config, conn, sql, paras);
        return new Page<Record>(list, pageNumber, pageSize, totalPage, (int) totalRow);
    }

    Page<Record> paginate(Config config, Connection conn, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) throws SQLException {
        String totalRowSql = "select count(*) " + config.dialect.replaceOrderBy(sqlExceptSelect);
        StringBuilder findSql = new StringBuilder();
        findSql.append(select).append(' ').append(sqlExceptSelect);
        return doPaginateByFullSql(config, conn, pageNumber, pageSize, null, totalRowSql, findSql, paras);
    }

    protected Page<Record> doPaginateByFullSql(int pageNumber, int pageSize, Boolean isGroupBySql, String totalRowSql, String findSql, Object... paras) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            StringBuilder findSqlBuf = new StringBuilder().append(findSql);
            return doPaginateByFullSql(config, conn, pageNumber, pageSize, isGroupBySql, totalRowSql, findSqlBuf, paras);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
    }

    public Page<Record> paginateByFullSql(int pageNumber, int pageSize, String totalRowSql, String findSql, Object... paras) {
        return doPaginateByFullSql(pageNumber, pageSize, null, totalRowSql, findSql, paras);
    }

    public Page<Record> paginateByFullSql(int pageNumber, int pageSize, boolean isGroupBySql, String totalRowSql, String findSql, Object... paras) {
        return doPaginateByFullSql(pageNumber, pageSize, isGroupBySql, totalRowSql, findSql, paras);
    }

    boolean save(Config config, Connection conn, String tableName, String primaryKey, Record record) throws SQLException {
        String[] pKeys = primaryKey.split(",");
        List<Object> paras = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder();
        config.dialect.forDbSave(tableName, pKeys, record, sql, paras);

        PreparedStatement pst;
        if (config.dialect.isOracle()) {
            pst = conn.prepareStatement(sql.toString(), pKeys);
        } else {
            pst = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
        }
        config.dialect.fillStatement(pst, paras);
        int result = pst.executeUpdate();
        config.dialect.getRecordGeneratedKey(pst, record, pKeys);
        DbKit.close(pst);
        return result >= 1;
    }

    
    public boolean save(String tableName, String primaryKey, Record record) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return save(config, conn, tableName, primaryKey, record);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
    }

    
    public boolean save(String tableName, Record record) {
        return save(tableName, config.dialect.getDefaultPrimaryKey(), record);
    }

    boolean update(Config config, Connection conn, String tableName, String primaryKey, Record record) throws SQLException {
        String[] pKeys = primaryKey.split(",");
        Object[] ids = new Object[pKeys.length];

        for (int i = 0; i < pKeys.length; i++) {
            ids[i] = record.get(pKeys[i].trim());    
            if (ids[i] == null)
                throw new ActiveRecordException("You can't update record without Primary Key, " + pKeys[i] + " can not be null.");
        }

        StringBuilder sql = new StringBuilder();
        List<Object> paras = new ArrayList<Object>();
        config.dialect.forDbUpdate(tableName, pKeys, ids, record, sql, paras);

        if (paras.size() <= 1) {    
            return false;
        }

        return update(config, conn, sql.toString(), paras.toArray()) >= 1;
    }

    
    public boolean update(String tableName, String primaryKey, Record record) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return update(config, conn, tableName, primaryKey, record);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
    }

    
    public boolean update(String tableName, Record record) {
        return update(tableName, config.dialect.getDefaultPrimaryKey(), record);
    }

    
    public Object execute(ICallback callback) {
        return execute(config, callback);
    }

    
    Object execute(Config config, ICallback callback) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return callback.call(conn);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
    }

    
    boolean tx(Config config, int transactionLevel, IAtom atom) {
        Connection conn = config.getThreadLocalConnection();
        if (conn != null) {    
            try {
                if (conn.getTransactionIsolation() < transactionLevel)
                    conn.setTransactionIsolation(transactionLevel);
                boolean result = atom.run();
                if (result)
                    return true;
                throw new NestedTransactionHelpException("Notice the outer transaction that the nested transaction return false");    
            } catch (SQLException e) {
                throw new ActiveRecordException(e);
            }
        }

        Boolean autoCommit = null;
        try {
            conn = config.getConnection();
            autoCommit = conn.getAutoCommit();
            config.setThreadLocalConnection(conn);
            conn.setTransactionIsolation(transactionLevel);
            conn.setAutoCommit(false);
            boolean result = atom.run();
            if (result)
                conn.commit();
            else
                conn.rollback();
            return result;
        } catch (NestedTransactionHelpException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (Exception e1) {
                LogKit.error(e1.getMessage(), e1);
            }
            LogKit.logNothing(e);
            return false;
        } catch (Throwable t) {
            if (conn != null) try {
                conn.rollback();
            } catch (Exception e1) {
                LogKit.error(e1.getMessage(), e1);
            }
            throw t instanceof RuntimeException ? (RuntimeException) t : new ActiveRecordException(t);
        } finally {
            try {
                if (conn != null) {
                    if (autoCommit != null)
                        conn.setAutoCommit(autoCommit);
                    conn.close();
                }
            } catch (Throwable t) {
                LogKit.error(t.getMessage(), t);    
            } finally {
                config.removeThreadLocalConnection();    
            }
        }
    }

    public boolean tx(int transactionLevel, IAtom atom) {
        return tx(config, transactionLevel, atom);
    }

    
    public boolean tx(IAtom atom) {
        return tx(config, config.getTransactionLevel(), atom);
    }

    
    public List<Record> findByCache(String cacheName, Object key, String sql, Object... paras) {
        ICache cache = config.getCache();
        List<Record> result = cache.get(cacheName, key);
        if (result == null) {
            result = find(sql, paras);
            cache.put(cacheName, key, result);
        }
        return result;
    }

    
    public List<Record> findByCache(String cacheName, Object key, String sql) {
        return findByCache(cacheName, key, sql, NULL_PARA_ARRAY);
    }

    
    public Record findFirstByCache(String cacheName, Object key, String sql, Object... paras) {
        ICache cache = config.getCache();
        Record result = cache.get(cacheName, key);
        if (result == null) {
            result = findFirst(sql, paras);
            cache.put(cacheName, key, result);
        }
        return result;
    }

    
    public Record findFirstByCache(String cacheName, Object key, String sql) {
        return findFirstByCache(cacheName, key, sql, NULL_PARA_ARRAY);
    }

    
    public Page<Record> paginateByCache(String cacheName, Object key, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
        return doPaginateByCache(cacheName, key, pageNumber, pageSize, null, select, sqlExceptSelect, paras);
    }

    
    public Page<Record> paginateByCache(String cacheName, Object key, int pageNumber, int pageSize, String select, String sqlExceptSelect) {
        return doPaginateByCache(cacheName, key, pageNumber, pageSize, null, select, sqlExceptSelect, NULL_PARA_ARRAY);
    }

    public Page<Record> paginateByCache(String cacheName, Object key, int pageNumber, int pageSize, boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras) {
        return doPaginateByCache(cacheName, key, pageNumber, pageSize, isGroupBySql, select, sqlExceptSelect, paras);
    }

    protected Page<Record> doPaginateByCache(String cacheName, Object key, int pageNumber, int pageSize, Boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras) {
        ICache cache = config.getCache();
        Page<Record> result = cache.get(cacheName, key);
        if (result == null) {
            result = doPaginate(pageNumber, pageSize, isGroupBySql, select, sqlExceptSelect, paras);
            cache.put(cacheName, key, result);
        }
        return result;
    }

    protected int[] batch(Config config, Connection conn, String sql, Object[][] paras, int batchSize) throws SQLException {
        if (paras == null || paras.length == 0)
            return new int[0];
        if (batchSize < 1)
            throw new IllegalArgumentException("The batchSize must more than 0.");

        boolean isInTransaction = config.isInTransaction();
        int counter = 0;
        int pointer = 0;
        int[] result = new int[paras.length];
        PreparedStatement pst = conn.prepareStatement(sql);
        for (int i = 0; i < paras.length; i++) {
            for (int j = 0; j < paras[i].length; j++) {
                Object value = paras[i][j];
                if (value instanceof java.util.Date) {
                    if (value instanceof java.sql.Date) {
                        pst.setDate(j + 1, (java.sql.Date) value);
                    } else if (value instanceof java.sql.Timestamp) {
                        pst.setTimestamp(j + 1, (java.sql.Timestamp) value);
                    } else {
                        
                        java.util.Date d = (java.util.Date) value;
                        pst.setTimestamp(j + 1, new java.sql.Timestamp(d.getTime()));
                    }
                } else if (value != null && value.toString().indexOf("-") == 4 &&
                        value.toString().length() - value.toString().replaceAll("-", "").length() == 2
                        &&
                        value.toString().length() - value.toString().replaceAll(" ", "").length() == 1
                        &&
                        value.toString().length() - value.toString().replaceAll(":", "").length() == 2) {
                    
                    pst.setTimestamp(j + 1, DateKit.string2Timestamp(value.toString()));
                } else if (value != null && (value.toString().startsWith("[") || value.toString().startsWith("{"))) {
                    PGobject jsonObject = new PGobject();
                    jsonObject.setType("json");
                    jsonObject.setValue(String.valueOf(value));
                    pst.setObject(j + 1, jsonObject);
                } else {
                    pst.setObject(j + 1, value);
                }

            }
            pst.addBatch();
            if (++counter >= batchSize) {
                counter = 0;
                int[] r = pst.executeBatch();
                if (isInTransaction == false)
                    conn.commit();
                for (int k = 0; k < r.length; k++)
                    result[pointer++] = r[k];
            }
        }
        if (counter != 0) {
            int[] r = pst.executeBatch();
            if (isInTransaction == false)
                conn.commit();
            for (int k = 0; k < r.length; k++)
                result[pointer++] = r[k];
        }
        DbKit.close(pst);
        return result;
    }

    
    public int[] batch(String sql, Object[][] paras, int batchSize) {
        Connection conn = null;
        Boolean autoCommit = null;
        try {
            conn = config.getConnection();
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            return batch(config, conn, sql, paras, batchSize);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            if (autoCommit != null)
                try {
                    conn.setAutoCommit(autoCommit);
                } catch (Exception e) {
                    LogKit.error(e.getMessage(), e);
                }
            config.close(conn);
        }
    }

    protected int[] batch(Config config, Connection conn, String sql, String columns, List list, int batchSize) throws SQLException {
        if (list == null || list.size() == 0)
            return new int[0];
        Object element = list.get(0);
        if (!(element instanceof Record) && !(element instanceof Model))
            throw new IllegalArgumentException("The element in list must be Model or Record.");
        if (batchSize < 1)
            throw new IllegalArgumentException("The batchSize must more than 0.");
        boolean isModel = element instanceof Model;

        String[] columnArray = columns.split(",");
        for (int i = 0; i < columnArray.length; i++)
            columnArray[i] = columnArray[i].trim();

        boolean isInTransaction = config.isInTransaction();
        int counter = 0;
        int pointer = 0;
        int size = list.size();
        int[] result = new int[size];
        PreparedStatement pst = conn.prepareStatement(sql);
        for (int i = 0; i < size; i++) {
            Map map = isModel ? ((Model) list.get(i))._getAttrs() : ((Record) list.get(i)).getColumns();
            for (int j = 0; j < columnArray.length; j++) {
                Object value = map.get(columnArray[j]);
                if (value instanceof java.util.Date) {
                    if (value instanceof java.sql.Date) {
                        pst.setDate(j + 1, (java.sql.Date) value);
                    } else if (value instanceof java.sql.Timestamp) {
                        pst.setTimestamp(j + 1, (java.sql.Timestamp) value);
                    } else {
                        
                        java.util.Date d = (java.util.Date) value;
                        pst.setTimestamp(j + 1, new java.sql.Timestamp(d.getTime()));
                    }
                } else if (value != null && value.toString().indexOf("-") == 4 &&
                        value.toString().length() - value.toString().replaceAll("-", "").length() == 2
                        &&
                        value.toString().length() - value.toString().replaceAll(" ", "").length() == 1
                        &&
                        value.toString().length() - value.toString().replaceAll(":", "").length() == 2) {
                    
                    pst.setTimestamp(j + 1, DateKit.string2Timestamp(value.toString()));
                } else if (value != null && (value.toString().startsWith("[") || value.toString().startsWith("{"))) {
                    PGobject jsonObject = new PGobject();
                    jsonObject.setType("json");
                    jsonObject.setValue(String.valueOf(value));
                    pst.setObject(j + 1, jsonObject);
                } else {
                    pst.setObject(j + 1, value);
                }

            }
            pst.addBatch();
            if (++counter >= batchSize) {
                counter = 0;
                int[] r = pst.executeBatch();
                if (isInTransaction == false)
                    conn.commit();
                for (int k = 0; k < r.length; k++)
                    result[pointer++] = r[k];
            }
        }
        if (counter != 0) {
            int[] r = pst.executeBatch();
            if (isInTransaction == false)
                conn.commit();
            for (int k = 0; k < r.length; k++)
                result[pointer++] = r[k];
        }
        DbKit.close(pst);
        return result;
    }

    
    public int[] batch(String sql, String columns, List modelOrRecordList, int batchSize) {
        Connection conn = null;
        Boolean autoCommit = null;
        try {
            conn = config.getConnection();
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            return batch(config, conn, sql, columns, modelOrRecordList, batchSize);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            if (autoCommit != null)
                try {
                    conn.setAutoCommit(autoCommit);
                } catch (Exception e) {
                    LogKit.error(e.getMessage(), e);
                }
            config.close(conn);
        }
    }

    protected int[] batch(Config config, Connection conn, List<String> sqlList, int batchSize) throws SQLException {
        if (sqlList == null || sqlList.size() == 0)
            return new int[0];
        if (batchSize < 1)
            throw new IllegalArgumentException("The batchSize must more than 0.");

        boolean isInTransaction = config.isInTransaction();
        int counter = 0;
        int pointer = 0;
        int size = sqlList.size();
        int[] result = new int[size];
        Statement st = conn.createStatement();
        for (int i = 0; i < size; i++) {
            st.addBatch(sqlList.get(i));
            if (++counter >= batchSize) {
                counter = 0;
                int[] r = st.executeBatch();
                if (isInTransaction == false)
                    conn.commit();
                for (int k = 0; k < r.length; k++)
                    result[pointer++] = r[k];
            }
        }
        if (counter != 0) {
            int[] r = st.executeBatch();
            if (isInTransaction == false)
                conn.commit();
            for (int k = 0; k < r.length; k++)
                result[pointer++] = r[k];
        }
        DbKit.close(st);
        return result;
    }

    
    public int[] batch(List<String> sqlList, int batchSize) {
        Connection conn = null;
        Boolean autoCommit = null;
        try {
            conn = config.getConnection();
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            return batch(config, conn, sqlList, batchSize);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            if (autoCommit != null)
                try {
                    conn.setAutoCommit(autoCommit);
                } catch (Exception e) {
                    LogKit.error(e.getMessage(), e);
                }
            config.close(conn);
        }
    }

    
    public int[] batchSave(List<? extends Model> modelList, int batchSize) {
        if (modelList == null || modelList.size() == 0)
            return new int[0];

        Model model = modelList.get(0);
        Map<String, Object> attrs = model._getAttrs();
        int index = 0;
        StringBuilder columns = new StringBuilder();
        
        for (Entry<String, Object> e : attrs.entrySet()) {
            if (config.dialect.isOracle()) {    
                Object value = e.getValue();
                if (value instanceof String && ((String) value).endsWith(".nextval")) {
                    continue;
                }
            }

            if (index++ > 0) {
                columns.append(',');
            }
            columns.append(e.getKey());
        }

        StringBuilder sql = new StringBuilder();
        List<Object> parasNoUse = new ArrayList<Object>();
        config.dialect.forModelSave(TableMapping.me().getTable(model.getClass()), attrs, sql, parasNoUse);
        return batch(sql.toString(), columns.toString(), modelList, batchSize);
    }

    
    public int[] batchSave(String tableName, List<Record> recordList, int batchSize) {
        if (recordList == null || recordList.size() == 0)
            return new int[0];

        Record record = recordList.get(0);
        Map<String, Object> cols = record.getColumns();
        int index = 0;
        StringBuilder columns = new StringBuilder();
        
        for (Entry<String, Object> e : cols.entrySet()) {
            if (config.dialect.isOracle()) {    
                Object value = e.getValue();
                if (value instanceof String && ((String) value).endsWith(".nextval")) {
                    continue;
                }
            }

            if (index++ > 0) {
                columns.append(',');
            }
            columns.append(e.getKey());
        }

        String[] pKeysNoUse = new String[0];
        StringBuilder sql = new StringBuilder();
        List<Object> parasNoUse = new ArrayList<Object>();
        config.dialect.forDbSave(tableName, pKeysNoUse, record, sql, parasNoUse);
        return batch(sql.toString(), columns.toString(), recordList, batchSize);
    }

    
    public int[] batchUpdate(List<? extends Model> modelList, int batchSize) {
        if (modelList == null || modelList.size() == 0)
            return new int[0];

        Model model = modelList.get(0);
        Table table = TableMapping.me().getTable(model.getClass());
        String[] pKeys = table.getPrimaryKey();
        Map<String, Object> attrs = model._getAttrs();
        List<String> attrNames = new ArrayList<String>();
        
        for (Entry<String, Object> e : attrs.entrySet()) {
            String attr = e.getKey();
            if (config.dialect.isPrimaryKey(attr, pKeys) == false && table.hasColumnLabel(attr))
                attrNames.add(attr);
        }
        for (String pKey : pKeys)
            attrNames.add(pKey);
        String columns = StrKit.join(attrNames.toArray(new String[attrNames.size()]), ",");

        
        Set<String> modifyFlag = attrs.keySet();    

        StringBuilder sql = new StringBuilder();
        List<Object> parasNoUse = new ArrayList<Object>();
        config.dialect.forModelUpdate(TableMapping.me().getTable(model.getClass()), attrs, modifyFlag, sql, parasNoUse);
        return batch(sql.toString(), columns, modelList, batchSize);
    }

    
    public int[] batchUpdate(String tableName, String primaryKey, List<Record> recordList, int batchSize) {
        if (recordList == null || recordList.size() == 0)
            return new int[0];

        String[] pKeys = primaryKey.split(",");
        config.dialect.trimPrimaryKeys(pKeys);

        Record record = recordList.get(0);
        Map<String, Object> cols = record.getColumns();
        List<String> colNames = new ArrayList<String>();
        
        for (Entry<String, Object> e : cols.entrySet()) {
            String col = e.getKey();
            if (config.dialect.isPrimaryKey(col, pKeys) == false)
                colNames.add(col);
        }
        for (String pKey : pKeys)
            colNames.add(pKey);
        String columns = StrKit.join(colNames.toArray(new String[colNames.size()]), ",");

        Object[] idsNoUse = new Object[pKeys.length];
        StringBuilder sql = new StringBuilder();
        List<Object> parasNoUse = new ArrayList<Object>();
        config.dialect.forDbUpdate(tableName, pKeys, idsNoUse, record, sql, parasNoUse);
        return batch(sql.toString(), columns, recordList, batchSize);
    }

    
    public int[] batchUpdate(String tableName, List<Record> recordList, int batchSize) {
        return batchUpdate(tableName, config.dialect.getDefaultPrimaryKey(), recordList, batchSize);
    }

    public String getSql(String key) {
        return config.getSqlKit().getSql(key);
    }

    public SqlPara getSqlPara(String key, Record record) {
        return getSqlPara(key, record.getColumns());
    }

    public SqlPara getSqlPara(String key, Model model) {
        return getSqlPara(key, model._getAttrs());
    }

    public SqlPara getSqlPara(String key, Map data) {
        return config.getSqlKit().getSqlPara(key, data);
    }

    public SqlPara getSqlPara(String key, Object... paras) {
        return config.getSqlKit().getSqlPara(key, paras);
    }

    public SqlPara getSqlParaByString(String content, Map data) {
        return config.getSqlKit().getSqlParaByString(content, data);
    }

    public SqlPara getSqlParaByString(String content, Object... paras) {
        return config.getSqlKit().getSqlParaByString(content, paras);
    }

    public List<Record> find(SqlPara sqlPara) {
        return find(sqlPara.getSql(), sqlPara.getPara());
    }

    public Record findFirst(SqlPara sqlPara) {
        return findFirst(sqlPara.getSql(), sqlPara.getPara());
    }

    public int update(SqlPara sqlPara) {
        return update(sqlPara.getSql(), sqlPara.getPara());
    }

    public Page<Record> paginate(int pageNumber, int pageSize, SqlPara sqlPara) {
        String[] sqls = PageSqlKit.parsePageSql(sqlPara.getSql());
        return doPaginate(pageNumber, pageSize, null, sqls[0], sqls[1], sqlPara.getPara());
    }

    public Page<Record> paginate(int pageNumber, int pageSize, boolean isGroupBySql, SqlPara sqlPara) {
        String[] sqls = PageSqlKit.parsePageSql(sqlPara.getSql());
        return doPaginate(pageNumber, pageSize, isGroupBySql, sqls[0], sqls[1], sqlPara.getPara());
    }
}



