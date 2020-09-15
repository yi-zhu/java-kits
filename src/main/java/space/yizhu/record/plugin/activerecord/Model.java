

package space.yizhu.record.plugin.activerecord;

import com.google.gson.Gson;
import space.yizhu.record.plugin.activerecord.cache.ICache;
import space.yizhu.kits.DateKit;
import space.yizhu.kits.SysKit;

import java.io.Serializable;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;


@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class Model<M extends Model> implements Serializable {

    public static final int FILTER_BY_SAVE = 0;
    public static final int FILTER_BY_UPDATE = 1;
    private static final long serialVersionUID = -990334519496260591L;
    private String configName;

    
    private Set<String> modifyFlag;

    
    private Map<String, Object> attrs = createAttrsMap();    

    private Map<String, Object> createAttrsMap() {
        Config config = _getConfig();
        if (config == null)
            return DbKit.brokenConfig.containerFactory.getAttrsMap();
        return config.containerFactory.getAttrsMap();
    }

    
    public M dao() {
        attrs = DaoContainerFactory.daoMap;
        modifyFlag = DaoContainerFactory.daoSet;
        return (M) this;
    }

    
    protected void filter(int filterBy) {

    }

    
    protected Map<String, Object> _getAttrs() {
        return attrs;
    }

    
    public Set<Entry<String, Object>> _getAttrsEntrySet() {
        return attrs.entrySet();
    }

    
    public String[] _getAttrNames() {
        Set<String> attrNameSet = attrs.keySet();
        return attrNameSet.toArray(new String[attrNameSet.size()]);
    }

    
    public Object[] _getAttrValues() {
        java.util.Collection<Object> attrValueCollection = attrs.values();
        return attrValueCollection.toArray(new Object[attrValueCollection.size()]);
    }

    
    public M _setAttrs(M model) {
        return (M) _setAttrs(model._getAttrs());
    }

    
    public M _setAttrs(Map<String, Object> attrs) {
        for (Entry<String, Object> e : attrs.entrySet())
            set(e.getKey(), e.getValue());
        return (M) this;
    }
	
	

    protected Set<String> _getModifyFlag() {
        if (modifyFlag == null) {
            Config config = _getConfig();
            if (config == null)
                modifyFlag = DbKit.brokenConfig.containerFactory.getModifyFlagSet();
            else
                modifyFlag = config.containerFactory.getModifyFlagSet();
        }
        return modifyFlag;
    }

    protected Config _getConfig() {
        if (configName != null)
            return DbKit.getConfig(configName);
        return DbKit.getConfig(_getUsefulClass());
    }
	
	

    protected Table _getTable() {
        return TableMapping.me().getTable(_getUsefulClass());
    }

    protected Class<? extends Model> _getUsefulClass() {
        Class c = getClass();
        
        
        
        return c.getName().indexOf("$$EnhancerBy") == -1 ? c : c.getSuperclass();
    }

    
    public M use(String configName) {
        if (attrs == DaoContainerFactory.daoMap) {
            throw new RuntimeException("dao 只允许调用查询方法");
        }

        this.configName = configName;
        return (M) this;
    }

    
    public M set(String attr, Object value) {
        Table table = _getTable();    
        if (table != null && !table.hasColumnLabel(attr)) {
            throw new ActiveRecordException("The attribute name does not exist: \"" + attr + "\"");
        }

        attrs.put(attr, value);
        _getModifyFlag().add(attr);    
        return (M) this;
    }

    

    
    public M put(String key, Object value) {
		
        attrs.put(key, value);
        return (M) this;
    }

    
    public M setOrPut(String attrOrNot, Object value) {
        Table table = _getTable();
        if (table != null && table.hasColumnLabel(attrOrNot)) {
            _getModifyFlag().add(attrOrNot);    
        }

        attrs.put(attrOrNot, value);
        return (M) this;
    }

    public M _setOrPut(Map<String, Object> map) {
        for (Entry<String, Object> e : map.entrySet()) {
            setOrPut(e.getKey(), e.getValue());
        }
        return (M) this;
    }

    public M _setOrPut(Model model) {
        return (M) _setOrPut(model._getAttrs());
    }

    
    public M put(Map<String, Object> map) {
        attrs.putAll(map);
        return (M) this;
    }

    
    public M put(Model model) {
        attrs.putAll(model._getAttrs());
        return (M) this;
    }

    
    public M put(Record record) {
        attrs.putAll(record.getColumns());
        return (M) this;
    }

    
    public Record toRecord() {
        return new Record().setColumns(_getAttrs());
    }

    
    public <T> T get(String attr) {
        return (T) (attrs.get(attr));
    }

    
    public <T> T get(String attr, Object defaultValue) {
        Object result = attrs.get(attr);
        return (T) (result != null ? result : defaultValue);
    }

    
    public String getStr(String attr) {
        
        Object s = attrs.get(attr);
        return s != null ? s.toString() : null;
    }

    
    public Integer getInt(String attr) {
        Number n = (Number) attrs.get(attr);
        return n != null ? n.intValue() : null;
    }

    
    public Long getLong(String attr) {
        Number n = (Number) attrs.get(attr);
        return n != null ? n.longValue() : null;
    }

    
    public java.math.BigInteger getBigInteger(String attr) {
        return (java.math.BigInteger) attrs.get(attr);
    }

    
    public java.util.Date getDate(String attr) {
        return (java.util.Date) attrs.get(attr);
    }

    
    public java.sql.Time getTime(String attr) {
        return (java.sql.Time) attrs.get(attr);
    }

    
    public java.sql.Timestamp getTimestamp(String attr) {
        return (java.sql.Timestamp) attrs.get(attr);
    }

    
    public Double getDouble(String attr) {
        Number n = (Number) attrs.get(attr);
        return n != null ? n.doubleValue() : null;
    }

    
    public Float getFloat(String attr) {
        Number n = (Number) attrs.get(attr);
        return n != null ? n.floatValue() : null;
    }

    public Short getShort(String attr) {
        Number n = (Number) attrs.get(attr);
        return n != null ? n.shortValue() : null;
    }

    public Byte getByte(String attr) {
        Number n = (Number) attrs.get(attr);
        return n != null ? n.byteValue() : null;
    }

    
    public Boolean getBoolean(String attr) {
        return (Boolean) attrs.get(attr);
    }

    
    public java.math.BigDecimal getBigDecimal(String attr) {
        return (java.math.BigDecimal) attrs.get(attr);
    }

    
    public byte[] getBytes(String attr) {
        return (byte[]) attrs.get(attr);
    }

    
    public Number getNumber(String attr) {
        return (Number) attrs.get(attr);
    }

    
    public Page<M> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
        return doPaginate(pageNumber, pageSize, null, select, sqlExceptSelect, paras);
    }

    
    public Page<M> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect) {
        return doPaginate(pageNumber, pageSize, null, select, sqlExceptSelect, DbKit.NULL_PARA_ARRAY);
    }

    
    public Page<M> paginate(int pageNumber, int pageSize, boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras) {
        return doPaginate(pageNumber, pageSize, isGroupBySql, select, sqlExceptSelect, paras);
    }

    private Page<M> doPaginate(int pageNumber, int pageSize, Boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras) {
        Config config = _getConfig();
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

    private Page<M> doPaginateByFullSql(Config config, Connection conn, int pageNumber, int pageSize, Boolean isGroupBySql, String totalRowSql, StringBuilder findSql, Object... paras) throws Exception {
        if (pageNumber < 1 || pageSize < 1) {
            throw new ActiveRecordException("pageNumber and pageSize must more than 0");
        }
        if (config.dialect.isTakeOverModelPaginate()) {
            return config.dialect.takeOverModelPaginate(conn, _getUsefulClass(), pageNumber, pageSize, isGroupBySql, totalRowSql, findSql, paras);
        }

        List result = Db.query(config, conn, totalRowSql, paras);
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
            return new Page<M>(new ArrayList<M>(0), pageNumber, pageSize, 0, 0);    
        }

        int totalPage = (int) (totalRow / pageSize);
        if (totalRow % pageSize != 0) {
            totalPage++;
        }

        if (pageNumber > totalPage) {
            return new Page<M>(new ArrayList<M>(0), pageNumber, pageSize, totalPage, (int) totalRow);
        }

        
        String sql = config.dialect.forPaginate(pageNumber, pageSize, findSql);
        List<M> list = find(config, conn, sql, paras);
        return new Page<M>(list, pageNumber, pageSize, totalPage, (int) totalRow);
    }

    private Page<M> doPaginateByFullSql(int pageNumber, int pageSize, Boolean isGroupBySql, String totalRowSql, String findSql, Object... paras) {
        Config config = _getConfig();
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

    public Page<M> paginateByFullSql(int pageNumber, int pageSize, String totalRowSql, String findSql, Object... paras) {
        return doPaginateByFullSql(pageNumber, pageSize, null, totalRowSql, findSql, paras);
    }

    public Page<M> paginateByFullSql(int pageNumber, int pageSize, boolean isGroupBySql, String totalRowSql, String findSql, Object... paras) {
        return doPaginateByFullSql(pageNumber, pageSize, isGroupBySql, totalRowSql, findSql, paras);
    }

    
    public boolean save() {
        try {
            filter(FILTER_BY_SAVE);

            Config config = _getConfig();
            Table table = _getTable();

            StringBuilder sql = new StringBuilder();
            List<Object> paras = new ArrayList<Object>();
            List<Object> parasT = new ArrayList<Object>();
            for (Object para : paras) {
                if (para.toString().contains("-"))
                    parasT.add((java.sql.Timestamp) para);
                else
                    parasT.add(para);
            }

            for (Entry<String, Object> set : attrs.entrySet()) {
                if (set.getValue() != null)

                    if (table.getColumnType(set.getKey()) == Long.class) {
                        set.setValue(Long.parseLong(String.valueOf(set.getValue())));
                    } else if (table.getColumnType(set.getKey()) == Integer.class) {
                        if (!set.getValue().toString().equals(""))
                            set.setValue(Integer.parseInt(String.valueOf(set.getValue())));
                    } else if (table.getColumnType(set.getKey()) == Timestamp.class) {
                        set.setValue(DateKit.string2Timestamp(set.getValue().toString()));
                    }
            }
            config.dialect.forModelSave(table, attrs, sql, parasT);
            

            
            Connection conn = null;
            PreparedStatement pst = null;
            int result = 0;
            try {
                conn = config.getConnection();
                if (config.dialect.isOracle()) {
                    pst = conn.prepareStatement(sql.toString(), table.getPrimaryKey());
                } else {
                    pst = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
                }
                config.dialect.fillStatement(pst, parasT);
                result = pst.executeUpdate();
                config.dialect.getModelGeneratedKey(this, pst, table);
                _getModifyFlag().clear();
                return result >= 1;
            } catch (Exception e) {
                throw new ActiveRecordException(e);
            } finally {
                config.close(pst, conn);
            }
        } catch (ActiveRecordException e) {
            if (e.toString().contains("Key (id)=")) { 
                SysKit.print("postgres自增主键异常.重新设定主键.如依旧报错,请手动修复");
                find("select setval('" + _getTable().getName() + "_id_seq', max(id)) from " + _getTable().getName());

                return save();
            }
            SysKit.print(e);
            return false;
        }
    }

    
    public boolean delete() {
        Table table = _getTable();
        String[] pKeys = table.getPrimaryKey();
        if (pKeys.length == 1) {    
            Object id = attrs.get(pKeys[0]);
            if (id == null)
                throw new ActiveRecordException("Primary key " + pKeys[0] + " can not be null");
            return deleteById(table, id);
        }

        Object[] ids = new Object[pKeys.length];
        for (int i = 0; i < pKeys.length; i++) {
            ids[i] = attrs.get(pKeys[i]);
            if (ids[i] == null)
                throw new ActiveRecordException("Primary key " + pKeys[i] + " can not be null");
        }
        return deleteById(table, ids);
    }

    
    public boolean deleteById(Object idValue) {
        if (idValue == null)
            throw new IllegalArgumentException("idValue can not be null");
        return deleteById(_getTable(), idValue);
    }

    
    public boolean deleteByIds(Object... idValues) {
        Table table = _getTable();
        if (idValues == null || idValues.length != table.getPrimaryKey().length)
            throw new IllegalArgumentException("Primary key nubmer must equals id value number and can not be null");

        return deleteById(table, idValues);
    }

    private boolean deleteById(Table table, Object... idValues) {
        Config config = _getConfig();
        Connection conn = null;
        try {
            conn = config.getConnection();
            String sql = config.dialect.forModelDeleteById(table);
            return Db.update(config, conn, sql, idValues) >= 1;
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
    }

    
    public boolean update() {
        try {
            filter(FILTER_BY_UPDATE);

            if (_getModifyFlag().isEmpty()) {
                return false;
            }


            Table table = _getTable();
            String[] pKeys = table.getPrimaryKey();
            for (String pKey : pKeys) {
                Object id = attrs.get(pKey);
                if (id == null)
                    throw new ActiveRecordException("You can't update model without Primary Key, " + pKey + " can not be null.");
            }
            for (Entry<String, Object> set : attrs.entrySet()) {
                if (set.getValue() != null)
                    if (table.getColumnType(set.getKey()) == Long.class) {
                        set.setValue(Long.parseLong(String.valueOf(set.getValue())));
                    } else if (table.getColumnType(set.getKey()) == Integer.class) {
                        set.setValue(Integer.parseInt(String.valueOf(set.getValue())));
                    } else if (table.getColumnType(set.getKey()) == Timestamp.class) {
                        set.setValue(DateKit.string2Timestamp(set.getValue().toString()));
                    }
            }
            Config config = _getConfig();
            StringBuilder sql = new StringBuilder();
            List<Object> paras = new ArrayList<Object>();
            config.dialect.forModelUpdate(table, attrs, _getModifyFlag(), sql, paras);

            if (paras.size() <= 1) {    
                return false;
            }

            
            Connection conn = null;
            try {
                conn = config.getConnection();
                int result = Db.update(config, conn, sql.toString(), paras.toArray());
                if (result >= 1) {
                    _getModifyFlag().clear();
                    return true;
                }
                return false;
            } catch (Exception e) {
                throw new ActiveRecordException(e);
            } finally {
                config.close(conn);
            }
        } catch (ActiveRecordException e) {
            if (e.toString().contains("Key (id)=")) { 
                find("select setval('" + _getTable().getName() + "_id_seq', max(id)) from " + _getTable().getName());

                return update();
            }
        }
        return false;
    }

    
    private List<M> find(Config config, Connection conn, String sql, Object... paras) throws Exception {
        PreparedStatement pst = conn.prepareStatement(sql);
        config.dialect.fillStatement(pst, paras);
        ResultSet rs = pst.executeQuery();
        List<M> result = config.dialect.buildModelList(rs, _getUsefulClass());    
        DbKit.close(rs, pst);
        return result;
    }

    protected List<M> find(Config config, String sql, Object... paras) {
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

    
    public List<M> find(String sql, Object... paras) {
        return find(_getConfig(), sql, paras);
    }

    
    public List<M> find(String sql) {
        return find(sql, DbKit.NULL_PARA_ARRAY);
    }

    public List<M> findAll() {
        Config config = _getConfig();
        String sql = config.dialect.forFindAll(_getTable().getName());
        return find(config, sql, DbKit.NULL_PARA_ARRAY);
    }

    
    public M findFirst(String sql, Object... paras) {
        List<M> result = find(sql, paras);
        return result.size() > 0 ? result.get(0) : null;
    }

    
    public M findFirst(String sql) {
        return findFirst(sql, DbKit.NULL_PARA_ARRAY);
    }

    
    public M findById(Object idValue) {
        return findByIdLoadColumns(new Object[]{idValue}, "*");
    }

    
    public M findByIds(Object... idValues) {
        return findByIdLoadColumns(idValues, "*");
    }

    
    public M findByIdLoadColumns(Object idValue, String columns) {
        return findByIdLoadColumns(new Object[]{idValue}, columns);
    }

    
    public M findByIdLoadColumns(Object[] idValues, String columns) {
        Table table = _getTable();
        if (table.getPrimaryKey().length != idValues.length)
            throw new IllegalArgumentException("id values error, need " + table.getPrimaryKey().length + " id value");

        Config config = _getConfig();
        String sql = config.dialect.forModelFindById(table, columns);
        List<M> result = find(config, sql, idValues);
        return result.size() > 0 ? result.get(0) : null;
    }

    
    public M remove(String attr) {
        attrs.remove(attr);
        _getModifyFlag().remove(attr);
        return (M) this;
    }

    
    public M remove(String... attrs) {
        if (attrs != null)
            for (String a : attrs) {
                this.attrs.remove(a);
                this._getModifyFlag().remove(a);
            }
        return (M) this;
    }

    
    public M removeNullValueAttrs() {
        for (Iterator<Entry<String, Object>> it = attrs.entrySet().iterator(); it.hasNext(); ) {
            Entry<String, Object> e = it.next();
            if (e.getValue() == null) {
                it.remove();
                _getModifyFlag().remove(e.getKey());
            }
        }
        return (M) this;
    }

    
    public M keep(String... attrs) {
        if (attrs != null && attrs.length > 0) {
            Config config = _getConfig();
            if (config == null) {    
                config = DbKit.brokenConfig;
            }
            Map<String, Object> newAttrs = config.containerFactory.getAttrsMap();    
            Set<String> newModifyFlag = config.containerFactory.getModifyFlagSet();    
            for (String a : attrs) {
                if (this.attrs.containsKey(a))    
                    newAttrs.put(a, this.attrs.get(a));
                if (this._getModifyFlag().contains(a))
                    newModifyFlag.add(a);
            }
            this.attrs = newAttrs;
            this.modifyFlag = newModifyFlag;
        } else {
            this.attrs.clear();
            this._getModifyFlag().clear();
        }
        return (M) this;
    }

    
    public M keep(String attr) {
        if (attrs.containsKey(attr)) {    
            Object keepIt = attrs.get(attr);
            boolean keepFlag = _getModifyFlag().contains(attr);
            attrs.clear();
            _getModifyFlag().clear();
            attrs.put(attr, keepIt);
            if (keepFlag)
                _getModifyFlag().add(attr);
        } else {
            attrs.clear();
            _getModifyFlag().clear();
        }
        return (M) this;
    }

    
    public M clear() {
        attrs.clear();
        _getModifyFlag().clear();
        return (M) this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean first = true;
        for (Entry<String, Object> e : attrs.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            Object value = e.getValue();
            if (value != null) {
                value = value.toString();
            }
            sb.append(e.getKey()).append(':').append(value);
        }
        sb.append('}');
        return sb.toString();
    }

    
    public boolean equals(Object o) {
        if (!(o instanceof Model))
            return false;
        if (o == this)
            return true;
        Model mo = (Model) o;
        if (getClass() != mo.getClass())
            return false;
        return attrs.equals(mo.attrs);
    }

    
    public int hashCode() {
        
        return attrs.hashCode();
    }

    
    public List<M> findByCache(String cacheName, Object key, String sql, Object... paras) {
        Config config = _getConfig();
        ICache cache = config.getCache();
        List<M> result = cache.get(cacheName, key);
        if (result == null) {
            result = find(config, sql, paras);
            cache.put(cacheName, key, result);
        }
        return result;
    }

    
    public List<M> findByCache(String cacheName, Object key, String sql) {
        return findByCache(cacheName, key, sql, DbKit.NULL_PARA_ARRAY);
    }

    
    public M findFirstByCache(String cacheName, Object key, String sql, Object... paras) {
        ICache cache = _getConfig().getCache();
        M result = cache.get(cacheName, key);
        if (result == null) {
            result = findFirst(sql, paras);
            cache.put(cacheName, key, result);
        }
        return result;
    }

    
    public M findFirstByCache(String cacheName, Object key, String sql) {
        return findFirstByCache(cacheName, key, sql, DbKit.NULL_PARA_ARRAY);
    }

    
    public Page<M> paginateByCache(String cacheName, Object key, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
        return doPaginateByCache(cacheName, key, pageNumber, pageSize, null, select, sqlExceptSelect, paras);
    }

    
    public Page<M> paginateByCache(String cacheName, Object key, int pageNumber, int pageSize, String select, String sqlExceptSelect) {
        return doPaginateByCache(cacheName, key, pageNumber, pageSize, null, select, sqlExceptSelect, DbKit.NULL_PARA_ARRAY);
    }

    public Page<M> paginateByCache(String cacheName, Object key, int pageNumber, int pageSize, boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras) {
        return doPaginateByCache(cacheName, key, pageNumber, pageSize, isGroupBySql, select, sqlExceptSelect, paras);
    }

    private Page<M> doPaginateByCache(String cacheName, Object key, int pageNumber, int pageSize, Boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras) {
        ICache cache = _getConfig().getCache();
        Page<M> result = cache.get(cacheName, key);
        if (result == null) {
            result = doPaginate(pageNumber, pageSize, isGroupBySql, select, sqlExceptSelect, paras);
            cache.put(cacheName, key, result);
        }
        return result;
    }

    
    public String toJson() {
        return new Gson().toJson(attrs);
    }

    public String getSql(String key) {
        return _getConfig().getSqlKit().getSql(key);
    }

    

    public SqlPara getSqlPara(String key, Model model) {
        return getSqlPara(key, model.attrs);
    }

    public SqlPara getSqlPara(String key, Map data) {
        return _getConfig().getSqlKit().getSqlPara(key, data);
    }

    public SqlPara getSqlPara(String key, Object... paras) {
        return _getConfig().getSqlKit().getSqlPara(key, paras);
    }

    public List<M> find(SqlPara sqlPara) {
        return find(sqlPara.getSql(), sqlPara.getPara());
    }

    public M findFirst(SqlPara sqlPara) {
        return findFirst(sqlPara.getSql(), sqlPara.getPara());
    }

    public Page<M> paginate(int pageNumber, int pageSize, SqlPara sqlPara) {
        String[] sqls = PageSqlKit.parsePageSql(sqlPara.getSql());
        return doPaginate(pageNumber, pageSize, null, sqls[0], sqls[1], sqlPara.getPara());
    }
}


