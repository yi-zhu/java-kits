

package space.yizhu.record.plugin.activerecord;

import com.google.gson.Gson;
import space.yizhu.bean.LogModel;
import space.yizhu.kits.*;
import space.yizhu.record.plugin.activerecord.cache.ICache;

import java.io.Serializable;
import java.sql.*;
import java.util.*;
import java.util.Date;
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
        if (config == null) {
            return DbConfig.brokenConfig.containerFactory.getAttrsMap();
        }
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
        for (Entry<String, Object> e : attrs.entrySet()) {
            set(e.getKey(), e.getValue());
        }
        return (M) this;
    }
	
	

    protected Set<String> _getModifyFlag() {
        if (modifyFlag == null) {
            Config config = _getConfig();
            if (config == null) {
                modifyFlag = DbConfig.brokenConfig.containerFactory.getModifyFlagSet();
            } else {
                modifyFlag = config.containerFactory.getModifyFlagSet();
            }
        }
        return modifyFlag;
    }

    protected Config _getConfig() {
        if (configName != null) {
            return DbConfig.getConfig(configName);
        }
        return DbConfig.getConfig(_getUsefulClass());
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
        if (null == s) {
            s = attrs.get(attr.toLowerCase());
            if (null == s) {
                return "";
            } else {
                return s.toString();
            }
        } else {
            return s.toString();
        }
    }

    
    public Integer getInt(String attr) {
        Object n =attrs.get(attr);
        if (n instanceof Number) {
            return n != null ? ((Number)n).intValue() : -1;
        } else if(n instanceof String) {
            return n != null ? Integer.parseInt(n.toString()) : -1;
        } else {
            return -1;
        }
    }

    
    public Long getLong(String attr) {
        Object n =attrs.get(attr);
        if (n instanceof Number) {
            return n != null ? ((Number)n).longValue() : -1;
        } else if(n instanceof String) {
            return n != null ? Long.parseLong(n.toString()) : -1;
        } else {
            return -1L;
        }

    }

    
    public java.math.BigInteger getBigInteger(String attr) {
        return (java.math.BigInteger) attrs.get(attr);
    }

    
    public java.util.Date getDate(String attr) {
       Object obj=attrs.get(attr);
       if (obj instanceof  Date){
           return (Date) obj;
       }else if ( obj instanceof  String){
           return DateKit.parseDate(((String) obj));
       }else if ( obj instanceof  Number){
           return new Date(((Number) obj).longValue());
       }else {
           return null;
       }
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
        return doPaginate(pageNumber, pageSize, null, select, sqlExceptSelect, DbConfig.NULL_PARA_ARRAY);
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
        StringBuilder sql = new StringBuilder();

        try {
            filter(FILTER_BY_SAVE);

            Config config = _getConfig();
            Table table = _getTable();

            List<Object> paras = new ArrayList<Object>();
            List<Object> parasT = new ArrayList<Object>();
            for (Object para : paras) {
                if (para.toString().contains("-")) {
                    parasT.add((Timestamp) para);
                } else {
                    parasT.add(para);
                }
            }

            for (Entry<String, Object> set : attrs.entrySet()) {
                if (set.getValue() != null) {
                    if (table.getColumnType(set.getKey()) == Long.class) {
                        set.setValue(Long.parseLong(String.valueOf(set.getValue())));
                    } else if (table.getColumnType(set.getKey()) == Integer.class) {
                        if (!set.getValue().toString().equals("")) {
                            set.setValue(Integer.parseInt(String.valueOf(set.getValue())));
                        }
                    } else if (table.getColumnType(set.getKey()) == Timestamp.class) {
                        if (set.getValue() instanceof Date) {
                            set.setValue(new Timestamp(((Date) set.getValue()).getTime()));
                        } else if (set.getValue() instanceof String ){
                            set.setValue(new Timestamp(DateKit.parseDate(set.getValue().toString()).getTime()));
                        }
                        else if (set.getValue() instanceof Number){
                            set.setValue(new Timestamp((Long) set.getValue()));
                        }
                    }
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
            } catch (Exception e) {
                throw new ActiveRecordException(e);
            } finally {
                config.close(pst, conn);
            }
            try {
                if (DbKit.isSaveDblog()&&CharKit.isNotNull(DbKit.logTableName)){
                    if (!table.getName().contains(DbKit.logTableName)){
                        new LogModel().setHeads(sql.toString()).setReturned(result + "")
                                .setParams(ToolKit.listToJson(paras))
                                .setType(1).setCode(table.getName()).save();
                    }
                }
            } catch (Exception e) {
                SysKit.print(e,"日志记录-创建出问题了");
            }
            return result >= 1;

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
            if (id == null) {
                throw new ActiveRecordException("Primary key " + pKeys[0] + " can not be null");
            }
            return deleteById(table, id);
        }

        Object[] ids = new Object[pKeys.length];
        for (int i = 0; i < pKeys.length; i++) {
            ids[i] = attrs.get(pKeys[i]);
            if (ids[i] == null) {
                throw new ActiveRecordException("Primary key " + pKeys[i] + " can not be null");
            }
        }
        return deleteById(table, ids);
    }

    
    public boolean deleteById(Object idValue) {
        if (idValue == null) {
            throw new IllegalArgumentException("idValue can not be null");
        }
        return deleteById(_getTable(), idValue);
    }

    
    public boolean deleteByIds(Object... idValues) {
        Table table = _getTable();
        if (idValues == null || idValues.length != table.getPrimaryKey().length) {
            throw new IllegalArgumentException("Primary key nubmer must equals id value number and can not be null");
        }

        return deleteById(table, idValues);
    }

    private boolean deleteById(Table table, Object... idValues) {
        Config config = _getConfig();
        Connection conn = null;
        String sql;
        int result = 0;
        try {
            conn = config.getConnection();
             sql = config.dialect.forModelDeleteById(table);
            result= Db.update(config, conn, sql, idValues) ;
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
        try {
            if (DbKit.isSaveDblog()&&CharKit.isNotNull(DbKit.logTableName)){
                if (!table.getName().contains(DbKit.logTableName)){
                    new LogModel().setHeads(String.valueOf(sql)).setReturned(result + "")
                            .setParams(ToolKit.listToJson(Arrays.asList(idValues))).setType(3)
                            .setCode(table.getName())
                            .save();
                }
            }
        } catch (Exception e) {
            SysKit.print(e,"日志记录-更新出问题了");
        }
        return result >= 1;
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
                if (id == null) {
                    throw new ActiveRecordException( pKey + " 主键不能为空.");
                }
            }
            for (Entry<String, Object> set : attrs.entrySet()) {
                if (set.getValue() != null) {
                    if (table.getColumnType(set.getKey()) == Long.class) {
                        set.setValue(Long.parseLong(String.valueOf(set.getValue())));
                    } else if (table.getColumnType(set.getKey()) == Integer.class) {
                        set.setValue(Integer.parseInt(String.valueOf(set.getValue())));
                    } else if (table.getColumnType(set.getKey()) == Timestamp.class) {
                        if (set.getKey().equals("modify_time")){
                            set.setValue(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        }else
                        set.setValue(DateKit.string2Timestamp(set.getValue().toString()));
                    }
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
            int result=0;
            try {
                conn = config.getConnection();
                 result = Db.update(config, conn, sql.toString(), paras.toArray());

            } catch (Exception e) {
                throw new ActiveRecordException(e);
            } finally {
                config.close(conn);
            }

            try {
                if (DbKit.isSaveDblog()&&CharKit.isNotNull(DbKit.logTableName)){
                    if (!table.getName().contains(DbKit.logTableName)){
                        new LogModel().setHeads(sql.toString()).setReturned(result + "")
                                .setParams(ToolKit.listToJson(paras)).setType(2)
                                .setCode(table.getName())
                                .save();
                    }
                }
            } catch (Exception e) {
                SysKit.print(e,"日志记录-更新出问题了");
            }
            if (result >= 1) {
                _getModifyFlag().clear();
                return true;
            }
            return false;
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
        DbConfig.close(rs, pst);
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
        return find(sql, DbConfig.NULL_PARA_ARRAY);
    }

    public List<M> findAll() {
        Config config = _getConfig();
        String sql = config.dialect.forFindAll(_getTable().getName());
        return find(config, sql, DbConfig.NULL_PARA_ARRAY);
    }

    
    public M findFirst(String sql, Object... paras) {
        List<M> result = find(sql, paras);
        return result.size() > 0 ? result.get(0) : null;
    }

    
    public M findFirst(String sql) {
        return findFirst(sql, DbConfig.NULL_PARA_ARRAY);
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
        if (table.getPrimaryKey().length != idValues.length) {
            throw new IllegalArgumentException("id values error, need " + table.getPrimaryKey().length + " id value");
        }

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
        if (attrs != null) {
            for (String a : attrs) {
                this.attrs.remove(a);
                this._getModifyFlag().remove(a);
            }
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
                config = DbConfig.brokenConfig;
            }
            Map<String, Object> newAttrs = config.containerFactory.getAttrsMap();    
            Set<String> newModifyFlag = config.containerFactory.getModifyFlagSet();    
            for (String a : attrs) {
                if (this.attrs.containsKey(a)) {
                    newAttrs.put(a, this.attrs.get(a));
                }
                if (this._getModifyFlag().contains(a)) {
                    newModifyFlag.add(a);
                }
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
            if (keepFlag) {
                _getModifyFlag().add(attr);
            }
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

    @Override
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Model)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        Model mo = (Model) o;
        if (getClass() != mo.getClass()) {
            return false;
        }
        return attrs.equals(mo.attrs);
    }

    @Override
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
        return findByCache(cacheName, key, sql, DbConfig.NULL_PARA_ARRAY);
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
        return findFirstByCache(cacheName, key, sql, DbConfig.NULL_PARA_ARRAY);
    }

    
    public Page<M> paginateByCache(String cacheName, Object key, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
        return doPaginateByCache(cacheName, key, pageNumber, pageSize, null, select, sqlExceptSelect, paras);
    }

    
    public Page<M> paginateByCache(String cacheName, Object key, int pageNumber, int pageSize, String select, String sqlExceptSelect) {
        return doPaginateByCache(cacheName, key, pageNumber, pageSize, null, select, sqlExceptSelect, DbConfig.NULL_PARA_ARRAY);
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




    private int pageSize = 30;
    private  final String SPACE=" ";



    public String[] getField() {
        return getFields().toArray(new String[]{});
    }

    public List<String> getFields() {
        return new ArrayList<String>(this.getTable().getColumnTypeMap().keySet());
    }


    /**
     * Getter for property 'pageSize'.
     *
     * @return Value for property 'pageSize'.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Setter for property 'pageSize'.
     *
     * @param pageSize Value to set for property 'pageSize'.
     */
    public Model<M> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public List<M> findBy(Map<String, String> map) {
        String keys = "", values = "";
        for (Map.Entry<String, String> set : map.entrySet()) {
            if (!set.getValue().equals("undefined")) {
                keys += set.getKey() + ",";
                values += set.getValue() + ",";
            }

        }
        if (keys.endsWith(",")) {
            keys = keys.substring(0, keys.length() - 1);
            values = values.substring(0, values.length() - 1);
            return findBy(keys, values);

        } else {
            return null;
        }
    }
    public M findByFirst(Map<String, String> map) {
        String keys = "", values = "";
        for (Map.Entry<String, String> set : map.entrySet()) {
            if (!set.getValue().equals("undefined")) {
                keys += set.getKey() + ",";
                values += set.getValue() + ",";
            }

        }
        if (keys.endsWith(",")) {
            keys = keys.substring(0, keys.length() - 1);
            values = values.substring(0, values.length() - 1);
            return findByFirst(keys, values);

        } else {
            return null;
        }
    }
    public List<M> findBy(String key, String value){
        return findBy(key, value, true);
    }
    //仿写于byId
    public List<M> findBy(String key, String value,boolean isDesc) {
        Table table = this.getTable();
        String[] keys, values;
        if (key == null || value == null) {
            return null;
        }
        values = value.split(",");
        keys = key.split(",");
        int page = 1, limit = 1000;
        StringBuilder sql = new StringBuilder("select * from " + table.getName() + " where ");
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].equals("page")) {
                page = Integer.parseInt(values[i]);
            } else if (keys[i].equals("limit")) {
                limit = Integer.parseInt(values[i]);
            } else if (i < keys.length - 1 && table.getColumnTypeMap().containsKey(keys[i])) {
                sql.append(keys[i]).append(" = '").append(values[i]).append("' and ");
            } else if (table.getColumnTypeMap().containsKey(keys[i])) {
                sql.append(keys[i]).append(" = '").append(values[i]).append("' ");
            }
        }
        if (sql.toString().endsWith("and ")) {
            sql.delete(sql.length() - 4, sql.length());
        }
        String order;
        if (isDesc)
            order= "order by id desc";
        else
            order= "order by id ";

        sql.append(order);
//        page--;
//        sql.append(" LIMIT ").append(limit).append(" OFFSET ").append(page * limit);

        List<M> result = null;
        try {
            result = this.find(sql.toString());
        } catch (Exception e) {
            result = this.find(sql.toString().replace(order, ""));
        }
        return result.size() > 0 ? result : null;
    }

    public M findByFirst(String key, String value) {

        List<M> result = findBy(key, value);
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    //仿写于byId
    public List<M> findValue(String value, int limit, int page) {
        Table table = this.getTable();
        List<String> keys = new ArrayList<>(table.getColumnTypeMap().keySet());
        StringBuilder sql = new StringBuilder("select * from " + table.getName() + " where ");
        for (int i = 0; i < keys.size(); i++) {
            if (i < keys.size() - 1) {
                sql.append(keys.get(i)).append(" = '").append(value).append("' and ");
            } else {
                sql.append(keys.get(i)).append(" = '").append(value).append("' ");
            }
        }
        String order = "order by id desc";
        sql.append(order);
        List<M> result = null;
        try {
            result = this.find(sql.toString());
        } catch (Exception e) {
            result = this.find(sql.toString().replace(order, ""));
        }
        return result.size() > 0 ? result : null;
    }

    //仿写于byId
    public List<M> findOneKey(String key, String... values) {
        Table table = this.getTable();

        //language=SQL
        String sql = "select * from " + table.getName() + " where " + key + " in (";
        for (Object str : values) {
            sql += "'" + str + "',";
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += ")";
        List<M> result = new ArrayList<>();
        try {
            result = this.find(sql);
        } catch (Exception e) {
        }
        return result.size() > 0 ? result : null;
    }

    public Page<M> find() {
        return findByKeys(null, null, 1, pageSize);
    }

    /**
     * 按id正序查询
     *
     * @param pageNum 第几页
     * @return page
     */
    public Page<M> find(int pageNum) {
        return findByKeys(null, null, pageNum, pageSize);
    }

    /**
     * 按id正序查询
     *
     * @param pageNum  第几页
     * @param pageSize 每页行数
     * @return page
     */
    public Page<M> find(int pageNum, int pageSize) {
        setPageSize(pageSize);
        return findByKeys(null, null, pageNum, pageSize);
    }

    /**
     * 按id正序查询
     *
     * @param key   查询关键字
     * @param value 值
     * @return 分页数据
     */
    public Page<M> find(String key, Object value) {
        return findByKeys(new HashMap<String, Object>() {{
            put(key, value);
        }}, null, 1, pageSize);
    }

    /**
     * 按id正序查询
     *
     * @param key      查询关键字
     * @param value    值
     * @param pageNum  第几页
     * @param pageSize 每页行数
     * @return 分页数据
     */
    public Page<M> find(String key, Object value, int pageNum, int pageSize) {
        return findByKeys(new HashMap<String, Object>() {{
            put(key, value);
        }}, null, pageNum, pageSize);
    }

    /**
     * 按id倒序查询
     *
     * @param key   查询关键字
     * @param value 值
     * @return 分页数据
     */
    public Page<M> findNews(String key, Object value) {
        return findByKeys(new HashMap<String, Object>() {{
                              put(key, value);
                          }},
                new HashMap<String, Boolean>() {{
                    put("id", false);
                }}, 1, pageSize);
    }

    /**
     * 按id倒序查询
     *
     * @param key     查询关键字
     * @param value   值
     * @param pageNum 第几页
     * @return 分页数据
     */
    public Page<M> findNews(String key, Object value, int pageNum) {
        return findByKeys(new HashMap<String, Object>() {{
                              put(key, value);
                          }},
                new HashMap<String, Boolean>() {{
                    put("id", false);
                }}, pageNum, pageSize);
    }

    /**
     * 按id倒序查询
     *
     * @param key      查询关键字
     * @param value    值
     * @param pageNum  第几页
     * @param pageSize 每页行数
     * @return 分页数据
     */
    public Page<M> findNews(String key, Object value, int pageNum, int pageSize) {
        return findByKeys(new HashMap<String, Object>() {{
                              put(key, value);
                          }},
                new HashMap<String, Boolean>() {{
                    put("id", false);
                }}, pageNum, pageSize);
    }


    /**
     * @param kv       查询条件
     * @param orderBys 排序条件，true为asc，false为desc
     * @param pageNum  起始页
     * @param pageSize 要查询数据
     * @return 分页数据
     */
    public Page<M> findByKeys(Map<String, Object> kv, Map<String, Boolean> orderBys, int pageNum, int pageSize) {
        Table table = this.getTable();
        //language=SQL
        String sql = " from " + table.getName() + " ";
        String orderStr = " order by ", whereStr = " where ";

        if (null != kv) {
            for (Map.Entry<String, Object> set : kv.entrySet()) {
                if (null == set.getKey()) {
                    continue;
                }
                //时间字段特殊处理
                if (set.getKey().endsWith("_time") || set.getKey().endsWith("_date")) {
                    if (set.getValue() instanceof List && ((List) set.getValue()).size() >= 2) {
                        if (((List) set.getValue()).size() == 3) {
                            whereStr += SPACE+set.getKey() + " not between  '" + ((List) set.getValue()).get(0) + "' and '" + ((List) set.getValue()).get(1) + "' and";
                        } else {
                            whereStr += SPACE+set.getKey() + " between  '" + ((List) set.getValue()).get(0) + "' and '" + ((List) set.getValue()).get(1) + "' and";
                        }
                    } else if (set.getValue() instanceof String && set.getValue().toString().contains(",")) {
                        String[] vls = set.getValue().toString().split(",");
                        if (vls.length == 2) {
                            whereStr +=SPACE+ set.getKey() + " between  '" + vls[0] + "' and '" + vls[1] + "' and";
                        } else if (vls.length == 3) {
                            whereStr += SPACE+set.getKey() + " not between  '" + vls[0] + "' and '" + vls[1] + "' and";

                        }
                    }
                } else if (set.getValue() instanceof List) {


                    String tempIn;
                    tempIn = SPACE + set.getKey() + " in (";

                    for (Object obj : (List) set.getValue()) {
                        tempIn += "'" + obj + "',";
                    }
                    if (tempIn.endsWith(",")) {
                        tempIn = tempIn.substring(0, tempIn.length() - 1);
                    }
                    if (tempIn.endsWith("(")) {
                        tempIn = null;
                    } else {
                        tempIn += ") and";
                    }
                    if (null != tempIn) {
                        whereStr += tempIn;
                    }
                } else {

                    whereStr += SPACE + set.getKey() + " = '" + set.getValue() + "' and";
                }
            }

        } else {
            whereStr = SPACE;
        }

        if (whereStr.endsWith("and")) {
            whereStr = whereStr.substring(0, whereStr.length() - 3);
        }
        if (whereStr.length() > 10) {
            sql += whereStr + SPACE;
        }

        if (null != orderBys) {
            for (Map.Entry<String, Boolean> est : orderBys.entrySet()) {
                if (est.getValue()) {
                    orderStr += est.getKey() + " asc ,";
                } else {
                    orderStr += est.getKey() + " desc ,";
                }
            }
        } else {
            orderStr += " id asc  ";
        }
        if (orderStr.endsWith(",")) {
            orderStr = CharKit.catTail(orderStr);
        }
        if (orderStr.length() > 13) {
            sql += orderStr + SPACE;
        }
        return paginate(pageNum, pageSize, "select *", sql);


    }

    //仿写于byId
    @Deprecated
    public boolean delBy(String key, String... values) {
        if (values.length == 0) {
            return false;
        }
        Table table = this.getTable();
        //language=SQL
        String sql = "delete  from " + table.getName() + " where " + key;
        if (values.length == 1) {
            sql += " = '" + values[0] + "'";
        } else {
            StringBuilder inVl = new StringBuilder();
            for (String v : values) {
                inVl.append("'").append(v).append("',");
            }
            inVl = new StringBuilder(inVl.substring(0, inVl.length() - 1));
            sql += "in (" + inVl + ")";
        }

        int result = 0;
        try {
            result = Db.update(sql);
        } catch (Exception e) {
            SysKit.print(e, "delby");
        }
        return result > -1;
    }


    //排序
    public List<M> findBy(String key, String value, boolean isAsc, String orderKey) {
        String[] keys, values;
        Table table = this.getTable();
        StringBuilder sql = new StringBuilder("select * from " + table.getName() + " ");
        if (!(value == null || value.length() == 0)) {
            sql.append(" where ");
            keys = key.split(",");
            values = value.split(",");
            for (int i = 0; i < keys.length; i++) {
                if (i == keys.length - 1) {
                    sql.append(keys[i]).append(" = '").append(values[i]).append("' ");
                } else {
                    sql.append(keys[i]).append(" = '").append(values[i]).append("' and ");
                }

            }
        }
        if (orderKey != null) {
            if (isAsc) {
                sql.append("order by ").append(orderKey).append(" asc");
            } else {
                sql.append("order by ").append(orderKey).append(" desc");
            }
        }
        List<M> result = this.find(sql.toString());
        return result.size() > 0 ? result : null;
    }


    public boolean saveOrUpdate() {
        try {
            if (this._getAttrs().get("id") != null) {
                this._getAttrs().put("id", Long.parseLong(String.valueOf(this._getAttrs().get("id"))));
                return update();

            } else {
                return save();
            }
        } catch (Exception e) {
            if (e.toString().contains("Key (id)=")) { //postsql 主键自增错误
                SysKit.print("postgres自增主键异常.重新设定主键.如依旧报错,请手动修复");
                find("select setval('" + this.getTableName() + "_id_seq', max(id)) from " + this.getTableName());
                if (this._getAttrs().get("id") != null) {
                    this._getAttrs().put("id", Long.parseLong(String.valueOf(this._getAttrs().get("id"))));
                    return update();

                } else {
                    return save();
                }
            }
            SysKit.print(e);
            return false;
        }
    }


    public M findFirstBy(String key, String value) {
        try {
            String[] keys, values;
            Table table = this.getTable();
            keys = key.split(",");
            values = value.split(",");
            String sql = "select * from " + table.getName() + " where ";
            for (int i = 0; i < keys.length; i++) {
                if (i == keys.length - 1) {
                    sql += keys[i] + " = '" + values[i] + "' ";
                } else {
                    sql += keys[i] + " = '" + values[i] + "' and ";
                }

            }
            List<M> result = this.find(sql);
            return result.size() > 0 ? result.get(0) : null;
        } catch (Exception e) {
            SysKit.print(e);
            return null;
        }
    }

    public List<M> getAll() {
        return getAllSort("id ");
    }

    public List<M> getAllSort(String sort) { //language=SQL
        Table table = this.getTable();
        List<M> result = this.find("select * from  " + table.getName() + " order by   " + sort + " ");
        return result.size() > 0 ? result : null;
    }


    public Table getTable() {
        return TableMapping.me().getTable(this.getUsefulClass());
    }

    private Class<? extends Model> getUsefulClass() {
        Class c = this.getClass();
        return !c.getName().contains("EnhancerByCGLIB") ? c : c.getSuperclass();
    }

    public String getTableName() {
        return this.getTable().getName();
    }


    /* ------------标准表----------- */

    private long id;
    private String code;
    private String name;
    private String creator;
    private String mender;
    private boolean is_del;
    private Date create_time;
    private Date modify_time;
    public long id() {
        return getLong("id");
    }

    public String code() {
        return getStr("code");
    }

    public String name() {
        return getStr("name");
    }

    public String creator() {
        return getStr("creator");
    }

    public String mender() {
        return getStr("mender");
    }

    public Integer is_del() {
        return getInt("is_del");
    }

    public String create_Time() {
        return getStr("create_time");
    }

    public String modify_time() {
        return getStr("modify_time");
    }


    public long getId() {
        id = getLong("id");
        return id;
    }
    /**
     * Getter for property 'code'.
     *
     * @return Value for property 'code'.
     */
    public String getCode() {
        return  code=getStr("code");
    }

    /**
     * Setter for property 'code'.
     *
     * @param v Value to set for property 'code'.
     */
    public Model<M> setCode(String v) {
        code = v;
        set("code",v);
        return this;
    }


    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {
        return name= getStr("name");
    }

    /**
     * Setter for property 'name'.
     *
     * @param v Value to set for property 'name'.
     */
    public Model<M> setName(String v) {
        name = v;
        set("name", v);
        return this;
    }

    /**
     * Getter for property 'creator'.
     *
     * @return Value for property 'creator'.
     */
    public String getCreator() {
        return creator= getStr("creator");
    }

    /**
     * Setter for property 'creator'.
     *
     * @param v Value to set for property 'creator'.
     */
    public Model<M> setCreator(String v) {
        creator = v;
        set("creator",v);
        return this;
    }

    /**
     * Getter for property 'mender'.
     *
     * @return Value for property 'mender'.
     */
    public String getMender()  {
        return mender= get("mender");
    }

    /**
     * Setter for property 'mender'.
     *
     * @param mender Value to set for property 'mender'.
     */
    public Model<M> setMender(String mender) {
        this.mender = mender;
        set("mender", mender);
        return this;
    }

    /**
     * Getter for property 'createTime'.
     *
     * @return Value for property 'createTime'.
     */
    public Date getCreateTime() {
        return create_time= getDate("create_time");
    }

    /**
     * Setter for property 'createTime'.
     *
     * @param createTime Value to set for property 'createTime'.
     */
    public Model<M> setCreateTime(Date createTime) {
        this.create_time = createTime;
        set("create_time", createTime);
        return this;
    }

    /**
     * Getter for property 'modifyTime'.
     *
     * @return Value for property 'modifyTime'.
     */
    public Date getModifyTime() {
        return modify_time= get("modify_time");
    }

    /**
     * Setter for property 'modifyTime'.
     *
     * @param modifyTime Value to set for property 'modifyTime'.
     */
    public Model<M> setModifyTime(Date modifyTime) {
        modify_time = modifyTime;
        set("modify_time",modifyTime);
        return this;
    }

    /**
     * Getter for property 'isDel'.
     *
     * @return Value for property 'isDel'.
     */
    public boolean isDel() {
        return is_del=getStr("is_del").equals("1");
    }

    /**
     * Setter for property 'isDel'.
     *
     * @param isDel Value to set for property 'isDel'.
     * @return
     */
    public Model<M> setDel(boolean isDel) {
        is_del = isDel;
        set("is_del", isDel ? 1 : 0);
        return this;
    }


    public  boolean hasValue(String val){
        for (Object v : _getAttrValues()) {
            if (String.valueOf(v).contains(val))
                return true;
        }
        return false;

    };


}


