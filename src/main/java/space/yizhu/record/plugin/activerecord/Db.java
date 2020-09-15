

package space.yizhu.record.plugin.activerecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import space.yizhu.kits.SyncWriteMap;


@SuppressWarnings("rawtypes")
public class Db {

    private static DbPro MAIN = null;
    private static final Map<String, DbPro> map = new SyncWriteMap<String, DbPro>(32, 0.25F);

    
    static void init(String configName) {
        MAIN = DbKit.getConfig(configName).dbProFactory.getDbPro(configName); 
        map.put(configName, MAIN);
    }

    
    static void removeDbProWithConfig(String configName) {
        if (MAIN != null && MAIN.config.getName().equals(configName)) {
            MAIN = null;
        }
        map.remove(configName);
    }

    public static DbPro use(String configName) {
        DbPro result = map.get(configName);
        if (result == null) {
            Config config = DbKit.getConfig(configName);
            if (config == null) {
                throw new IllegalArgumentException("Config not found by configName: " + configName);
            }
            result = config.dbProFactory.getDbPro(configName);    
            map.put(configName, result);
        }
        return result;
    }

    public static DbPro use() {
        return MAIN;
    }

    static <T> List<T> query(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        return MAIN.query(config, conn, sql, paras);
    }

    
    public static <T> List<T> query(String sql, Object... paras) {
        return MAIN.query(sql, paras);
    }

    
    public static <T> List<T> query(String sql) {
        return MAIN.query(sql);
    }

    
    public static <T> T queryFirst(String sql, Object... paras) {
        return MAIN.queryFirst(sql, paras);
    }

    
    public static <T> T queryFirst(String sql) {
        return MAIN.queryFirst(sql);
    }

    

    
    public static <T> T queryColumn(String sql, Object... paras) {
        return MAIN.queryColumn(sql, paras);
    }

    public static <T> T queryColumn(String sql) {
        return MAIN.queryColumn(sql);
    }

    public static String queryStr(String sql, Object... paras) {
        return MAIN.queryStr(sql, paras);
    }

    public static String queryStr(String sql) {
        return MAIN.queryStr(sql);
    }

    public static Integer queryInt(String sql, Object... paras) {
        return MAIN.queryInt(sql, paras);
    }

    public static Integer queryInt(String sql) {
        return MAIN.queryInt(sql);
    }

    public static Long queryLong(String sql, Object... paras) {
        return MAIN.queryLong(sql, paras);
    }

    public static Long queryLong(String sql) {
        return MAIN.queryLong(sql);
    }

    public static Double queryDouble(String sql, Object... paras) {
        return MAIN.queryDouble(sql, paras);
    }

    public static Double queryDouble(String sql) {
        return MAIN.queryDouble(sql);
    }

    public static Float queryFloat(String sql, Object... paras) {
        return MAIN.queryFloat(sql, paras);
    }

    public static Float queryFloat(String sql) {
        return MAIN.queryFloat(sql);
    }

    public static java.math.BigDecimal queryBigDecimal(String sql, Object... paras) {
        return MAIN.queryBigDecimal(sql, paras);
    }

    public static java.math.BigDecimal queryBigDecimal(String sql) {
        return MAIN.queryBigDecimal(sql);
    }

    public static byte[] queryBytes(String sql, Object... paras) {
        return MAIN.queryBytes(sql, paras);
    }

    public static byte[] queryBytes(String sql) {
        return MAIN.queryBytes(sql);
    }

    public static java.util.Date queryDate(String sql, Object... paras) {
        return MAIN.queryDate(sql, paras);
    }

    public static java.util.Date queryDate(String sql) {
        return MAIN.queryDate(sql);
    }

    public static java.sql.Time queryTime(String sql, Object... paras) {
        return MAIN.queryTime(sql, paras);
    }

    public static java.sql.Time queryTime(String sql) {
        return MAIN.queryTime(sql);
    }

    public static java.sql.Timestamp queryTimestamp(String sql, Object... paras) {
        return MAIN.queryTimestamp(sql, paras);
    }

    public static java.sql.Timestamp queryTimestamp(String sql) {
        return MAIN.queryTimestamp(sql);
    }

    public static Boolean queryBoolean(String sql, Object... paras) {
        return MAIN.queryBoolean(sql, paras);
    }

    public static Boolean queryBoolean(String sql) {
        return MAIN.queryBoolean(sql);
    }

    public static Short queryShort(String sql, Object... paras) {
        return MAIN.queryShort(sql, paras);
    }

    public static Short queryShort(String sql) {
        return MAIN.queryShort(sql);
    }

    public static Byte queryByte(String sql, Object... paras) {
        return MAIN.queryByte(sql, paras);
    }

    public static Byte queryByte(String sql) {
        return MAIN.queryByte(sql);
    }

    public static Number queryNumber(String sql, Object... paras) {
        return MAIN.queryNumber(sql, paras);
    }

    public static Number queryNumber(String sql) {
        return MAIN.queryNumber(sql);
    }
    

    
    static int update(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        return MAIN.update(config, conn, sql, paras);
    }

    
    public static int update(String sql, Object... paras) {
        return MAIN.update(sql, paras);
    }

    
    public static int update(String sql) {
        return MAIN.update(sql);
    }

    static List<Record> find(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        return MAIN.find(config, conn, sql, paras);
    }

    
    public static List<Record> find(String sql, Object... paras) {
        return MAIN.find(sql, paras);
    }

    
    public static List<Record> find(String sql) {
        return MAIN.find(sql);
    }

    public static List<Record> findAll(String tableName) {
        return MAIN.findAll(tableName);
    }

    
    public static Record findFirst(String sql, Object... paras) {
        return MAIN.findFirst(sql, paras);
    }

    
    public static Record findFirst(String sql) {
        return MAIN.findFirst(sql);
    }

    
    public static Record findById(String tableName, Object idValue) {
        return MAIN.findById(tableName, idValue);
    }

    public static Record findById(String tableName, String primaryKey, Object idValue) {
        return MAIN.findById(tableName, primaryKey, idValue);
    }

    
    public static Record findByIds(String tableName, String primaryKey, Object... idValues) {
        return MAIN.findByIds(tableName, primaryKey, idValues);
    }

    
    public static boolean deleteById(String tableName, Object idValue) {
        return MAIN.deleteById(tableName, idValue);
    }

    public static boolean deleteById(String tableName, String primaryKey, Object idValue) {
        return MAIN.deleteById(tableName, primaryKey, idValue);
    }

    
    public static boolean deleteByIds(String tableName, String primaryKey, Object... idValues) {
        return MAIN.deleteByIds(tableName, primaryKey, idValues);
    }

    
    public static boolean delete(String tableName, String primaryKey, Record record) {
        return MAIN.delete(tableName, primaryKey, record);
    }

    
    public static boolean delete(String tableName, Record record) {
        return MAIN.delete(tableName, record);
    }

    
    public static int delete(String sql, Object... paras) {
        return MAIN.delete(sql, paras);
    }

    
    public static int delete(String sql) {
        return MAIN.delete(sql);
    }

    static Page<Record> paginate(Config config, Connection conn, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) throws SQLException {
        return MAIN.paginate(config, conn, pageNumber, pageSize, select, sqlExceptSelect, paras);
    }

    
    public static Page<Record> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
        return MAIN.paginate(pageNumber, pageSize, select, sqlExceptSelect, paras);
    }

    public static Page<Record> paginate(int pageNumber, int pageSize, boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras) {
        return MAIN.paginate(pageNumber, pageSize, isGroupBySql, select, sqlExceptSelect, paras);
    }

    
    public static Page<Record> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect) {
        return MAIN.paginate(pageNumber, pageSize, select, sqlExceptSelect);
    }

    public static Page<Record> paginateByFullSql(int pageNumber, int pageSize, String totalRowSql, String findSql, Object... paras) {
        return MAIN.paginateByFullSql(pageNumber, pageSize, totalRowSql, findSql, paras);
    }

    public static Page<Record> paginateByFullSql(int pageNumber, int pageSize, boolean isGroupBySql, String totalRowSql, String findSql, Object... paras) {
        return MAIN.paginateByFullSql(pageNumber, pageSize, isGroupBySql, totalRowSql, findSql, paras);
    }

    static boolean save(Config config, Connection conn, String tableName, String primaryKey, Record record) throws SQLException {
        return MAIN.save(config, conn, tableName, primaryKey, record);
    }

    
    public static boolean save(String tableName, String primaryKey, Record record) {
        return MAIN.save(tableName, primaryKey, record);
    }

    
    public static boolean save(String tableName, Record record) {
        return MAIN.save(tableName, record);
    }

    static boolean update(Config config, Connection conn, String tableName, String primaryKey, Record record) throws SQLException {
        return MAIN.update(config, conn, tableName, primaryKey, record);
    }

    
    public static boolean update(String tableName, String primaryKey, Record record) {
        return MAIN.update(tableName, primaryKey, record);
    }

    
    public static boolean update(String tableName, Record record) {
        return MAIN.update(tableName, record);
    }

    
    public static Object execute(ICallback callback) {
        return MAIN.execute(callback);
    }

    
    static Object execute(Config config, ICallback callback) {
        return MAIN.execute(config, callback);
    }

    
    static boolean tx(Config config, int transactionLevel, IAtom atom) {
        return MAIN.tx(config, transactionLevel, atom);
    }

    public static boolean tx(int transactionLevel, IAtom atom) {
        return MAIN.tx(transactionLevel, atom);
    }

    
    public static boolean tx(IAtom atom) {
        return MAIN.tx(atom);
    }

    
    public static List<Record> findByCache(String cacheName, Object key, String sql, Object... paras) {
        return MAIN.findByCache(cacheName, key, sql, paras);
    }

    
    public static List<Record> findByCache(String cacheName, Object key, String sql) {
        return MAIN.findByCache(cacheName, key, sql);
    }

    
    public static Record findFirstByCache(String cacheName, Object key, String sql, Object... paras) {
        return MAIN.findFirstByCache(cacheName, key, sql, paras);
    }

    
    public static Record findFirstByCache(String cacheName, Object key, String sql) {
        return MAIN.findFirstByCache(cacheName, key, sql);
    }

    
    public static Page<Record> paginateByCache(String cacheName, Object key, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
        return MAIN.paginateByCache(cacheName, key, pageNumber, pageSize, select, sqlExceptSelect, paras);
    }

    public static Page<Record> paginateByCache(String cacheName, Object key, int pageNumber, int pageSize, boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras) {
        return MAIN.paginateByCache(cacheName, key, pageNumber, pageSize, isGroupBySql, select, sqlExceptSelect, paras);
    }

    
    public static Page<Record> paginateByCache(String cacheName, Object key, int pageNumber, int pageSize, String select, String sqlExceptSelect) {
        return MAIN.paginateByCache(cacheName, key, pageNumber, pageSize, select, sqlExceptSelect);
    }

    
    public static int[] batch(String sql, Object[][] paras, int batchSize) {
        return MAIN.batch(sql, paras, batchSize);
    }

    
    public static int[] batch(String sql, String columns, List modelOrRecordList, int batchSize) {
        return MAIN.batch(sql, columns, modelOrRecordList, batchSize);
    }

    
    public static int[] batch(List<String> sqlList, int batchSize) {
        return MAIN.batch(sqlList, batchSize);
    }

    
    public static int[] batchSave(List<? extends Model> modelList, int batchSize) {
        return MAIN.batchSave(modelList, batchSize);
    }

    
    public static int[] batchSave(String tableName, List<Record> recordList, int batchSize) {
        return MAIN.batchSave(tableName, recordList, batchSize);
    }

    
    public static int[] batchUpdate(List<? extends Model> modelList, int batchSize) {
        return MAIN.batchUpdate(modelList, batchSize);
    }

    
    public static int[] batchUpdate(String tableName, String primaryKey, List<Record> recordList, int batchSize) {
        return MAIN.batchUpdate(tableName, primaryKey, recordList, batchSize);
    }

    
    public static int[] batchUpdate(String tableName, List<Record> recordList, int batchSize) {
        return MAIN.batchUpdate(tableName, recordList, batchSize);
    }

    public static String getSql(String key) {
        return MAIN.getSql(key);
    }

    public static SqlPara getSqlPara(String key, Record record) {
        return MAIN.getSqlPara(key, record);
    }

    public static SqlPara getSqlPara(String key, Model model) {
        return MAIN.getSqlPara(key, model);
    }

    public static SqlPara getSqlPara(String key, Map data) {
        return MAIN.getSqlPara(key, data);
    }

    public static SqlPara getSqlPara(String key, Object... paras) {
        return MAIN.getSqlPara(key, paras);
    }

    public static SqlPara getSqlParaByString(String content, Map data) {
        return MAIN.getSqlParaByString(content, data);
    }

    public static SqlPara getSqlParaByString(String content, Object... paras) {
        return MAIN.getSqlParaByString(content, paras);
    }

    public static List<Record> find(SqlPara sqlPara) {
        return MAIN.find(sqlPara);
    }

    public static Record findFirst(SqlPara sqlPara) {
        return MAIN.findFirst(sqlPara);
    }

    public static int update(SqlPara sqlPara) {
        return MAIN.update(sqlPara);
    }

    public static Page<Record> paginate(int pageNumber, int pageSize, SqlPara sqlPara) {
        return MAIN.paginate(pageNumber, pageSize, sqlPara);
    }

    public static Page<Record> paginate(int pageNumber, int pageSize, boolean isGroupBySql, SqlPara sqlPara) {
        return MAIN.paginate(pageNumber, pageSize, isGroupBySql, sqlPara);
    }
}



