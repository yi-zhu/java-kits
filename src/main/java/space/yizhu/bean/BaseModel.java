package space.yizhu.bean;/* Created by xiuxi on 2017.6.20.*/

import space.yizhu.record.plugin.activerecord.Model;

/**
 * @author yi
 */
public class BaseModel<M extends BaseModel<M>> extends Model<M> {

    private int pageSize = 30;
    private  final String SPACE=" ";
/*

    public BaseModel() {
        BaseModel<M> me=  new BaseModel<M>().dao();
    }

    public String[] getField() {
        return getFields().toArray(new String[]{});
    }

    public List<String> getFields() {
        return new ArrayList<String>(this.getTable().getColumnTypeMap().keySet());
    }


    */
/**
     * Getter for property 'pageSize'.
     *
     * @return Value for property 'pageSize'.
     *//*

    public int getPageSize() {
        return pageSize;
    }

    */
/**
     * Setter for property 'pageSize'.
     *
     * @param pageSize Value to set for property 'pageSize'.
     *//*

    public BaseModel<M> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public Date getDate(String attr) {
        try {
            return super.getDate(attr);
        } catch (Exception e) {
            try {
                return DateUtils.parseDate(getStr(attr), "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy/mm/dd HH:mm:ss", "yyyy-mm-dd HH:mm:ss", "yyyy-mm-dd", "yyyy/mm/dd");
            } catch (ParseException e1) {
                SysKit.print(e1, "base,getDate");
                return null;
            }

        }
    }

    @Override
    public String getStr(String attr) {
        try {
            Object s = super.getStr(attr);
            if (null == s) {
                s = super.getStr(attr.toLowerCase());
                if (null == s)
                    return "";
                else return s.toString();
            } else
                return s.toString();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public Double getDouble(String attr) {
        try {
            Object n = super.getStr(attr);
            return n != null ? Double.parseDouble(n.toString()) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    @Override
    public Integer getInt(String attr) {
        try {
            Object n = super.getStr(attr);
            return n != null ? Integer.parseInt(n.toString()) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
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

        } else return null;


    }

    //仿写于byId
    public List<M> findBy(String key, String value) {
        Table table = this.getTable();
        String[] keys, values;
        if (key == null || value == null)
            return null;
        values = value.split(",");
        keys = key.split(",");
        int page = 1, limit = 1000;
        StringBuilder sql = new StringBuilder("select * from " + table.getName() + " where ");
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].equals("page"))
                page = Integer.parseInt(values[i]);
            else if (keys[i].equals("limit"))
                limit = Integer.parseInt(values[i]);
            else if (i < keys.length - 1 && table.getColumnTypeMap().containsKey(keys[i]))
                sql.append(keys[i]).append(" = '").append(values[i]).append("' and ");
            else if (table.getColumnTypeMap().containsKey(keys[i]))
                sql.append(keys[i]).append(" = '").append(values[i]).append("' ");
        }
        if (sql.toString().endsWith("and "))
            sql.delete(sql.length() - 4, sql.length());
        String order = "order by id desc";
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
            if (i < keys.size() - 1)
                sql.append(keys.get(i)).append(" = '").append(value).append("' and ");
            else
                sql.append(keys.get(i)).append(" = '").append(value).append("' ");
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

    */
/**
     * 按id正序查询
     *
     * @param pageNum 第几页
     * @return page
     *//*

    public Page<M> find(int pageNum) {
        return findByKeys(null, null, pageNum, pageSize);
    }

    */
/**
     * 按id正序查询
     *
     * @param pageNum  第几页
     * @param pageSize 每页行数
     * @return page
     *//*

    public Page<M> find(int pageNum, int pageSize) {
        setPageSize(pageSize);
        return findByKeys(null, null, pageNum, pageSize);
    }

    */
/**
     * 按id正序查询
     *
     * @param key   查询关键字
     * @param value 值
     * @return 分页数据
     *//*

    public Page<M> find(String key, Object value) {
        return findByKeys(new HashMap<String, Object>() {{
            put(key, value);
        }}, null, 1, pageSize);
    }

    */
/**
     * 按id正序查询
     *
     * @param key      查询关键字
     * @param value    值
     * @param pageNum  第几页
     * @param pageSize 每页行数
     * @return 分页数据
     *//*

    public Page<M> find(String key, Object value, int pageNum, int pageSize) {
        return findByKeys(new HashMap<String, Object>() {{
            put(key, value);
        }}, null, pageNum, pageSize);
    }

    */
/**
     * 按id倒序查询
     *
     * @param key   查询关键字
     * @param value 值
     * @return 分页数据
     *//*

    public Page<M> findNews(String key, Object value) {
        return findByKeys(new HashMap<String, Object>() {{
                              put(key, value);
                          }},
                new HashMap<String, Boolean>() {{
                    put("id", false);
                }}, 1, pageSize);
    }

    */
/**
     * 按id倒序查询
     *
     * @param key     查询关键字
     * @param value   值
     * @param pageNum 第几页
     * @return 分页数据
     *//*

    public Page<M> findNews(String key, Object value, int pageNum) {
        return findByKeys(new HashMap<String, Object>() {{
                              put(key, value);
                          }},
                new HashMap<String, Boolean>() {{
                    put("id", false);
                }}, pageNum, pageSize);
    }

    */
/**
     * 按id倒序查询
     *
     * @param key      查询关键字
     * @param value    值
     * @param pageNum  第几页
     * @param pageSize 每页行数
     * @return 分页数据
     *//*

    public Page<M> findNews(String key, Object value, int pageNum, int pageSize) {
        return findByKeys(new HashMap<String, Object>() {{
                              put(key, value);
                          }},
                new HashMap<String, Boolean>() {{
                    put("id", false);
                }}, pageNum, pageSize);
    }


    */
/**
     * @param kv       查询条件
     * @param orderBys 排序条件，true为asc，false为desc
     * @param pageNum  起始页
     * @param pageSize 要查询数据
     * @return 分页数据
     *//*

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
                            whereStr += set.getKey() + " not between  " + ((List) set.getValue()).get(0) + " and " + ((List) set.getValue()).get(1) + " and";
                        } else {
                            whereStr += set.getKey() + " between  " + ((List) set.getValue()).get(0) + " and " + ((List) set.getValue()).get(1) + " and";
                        }
                    } else if (set.getValue() instanceof String && set.getValue().toString().contains(",")) {
                        String[] vls = set.getValue().toString().split(",");
                        if (vls.length == 2) {
                            whereStr += set.getKey() + " between  " + vls[0] + " and " + vls[1] + " and";
                        } else if (vls.length == 3) {
                            whereStr += set.getKey() + " not between  " + vls[0] + " and " + vls[1] + " and";

                        }
                    }
                } else if (set.getValue() instanceof List) {


                    String tempIn;
                    tempIn = SPACE + set.getKey() + " in (";

                    for (Object obj : (List) set.getValue()) {
                        tempIn += "'" + obj + "',";
                    }
                    if (tempIn.endsWith(","))
                        tempIn = tempIn.substring(0, tempIn.length() - 1);
                    if (tempIn.endsWith("(")) {
                        tempIn = null;
                    } else {
                        tempIn += ") and";
                    }
                    if (null != tempIn)
                        whereStr += tempIn;
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
                    orderStr += est.getKey() + "asc ,";
                } else {
                    orderStr += est.getKey() + "desc ,";
                }
            }
        } else {
            orderStr += " id asc  ";
        }
        if (orderStr.endsWith(","))
            orderStr = CharKit.catTail(orderStr);
        if (orderStr.length() > 13)
            sql += orderStr + SPACE;
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
                if (i == keys.length - 1)
                    sql.append(keys[i]).append(" = '").append(values[i]).append("' ");
                else sql.append(keys[i]).append(" = '").append(values[i]).append("' and ");

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

            } else return save();
        } catch (Exception e) {
            if (e.toString().contains("Key (id)=")) { //postsql 主键自增错误
                SysKit.print("postgres自增主键异常.重新设定主键.如依旧报错,请手动修复");
                find("select setval('" + this.getTableName() + "_id_seq', max(id)) from " + this.getTableName());
                if (this._getAttrs().get("id") != null) {
                    this._getAttrs().put("id", Long.parseLong(String.valueOf(this._getAttrs().get("id"))));
                    return update();

                } else return save();
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
                if (i == keys.length - 1)
                    sql += keys[i] + " = '" + values[i] + "' ";
                else sql += keys[i] + " = '" + values[i] + "' and ";

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


    */
/* ------------标准表----------- *//*


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
    */
/**
     * Getter for property 'code'.
     *
     * @return Value for property 'code'.
     *//*

    public String getCode() {
        return  code=getStr("code");
    }

    */
/**
     * Setter for property 'code'.
     *
     * @param v Value to set for property 'code'.
     *//*

    public BaseModel<M> setCode(String v) {
        code = v;
        set("code",v);
        return this;
    }


    */
/**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     *//*

    public String getName() {
        return name= getStr("name");
    }

    */
/**
     * Setter for property 'name'.
     *
     * @param v Value to set for property 'name'.
     *//*

    public BaseModel<M> setName(String v) {
        name = v;
        set("name", v);
        return this;
    }

    */
/**
     * Getter for property 'creator'.
     *
     * @return Value for property 'creator'.
     *//*

    public String getCreator() {
        return creator= getStr("creator");
    }

    */
/**
     * Setter for property 'creator'.
     *
     * @param v Value to set for property 'creator'.
     *//*

    public BaseModel<M> setCreator(String v) {
        creator = v;
        set("creator",v);
        return this;
    }

    */
/**
     * Getter for property 'mender'.
     *
     * @return Value for property 'mender'.
     *//*

    public String getMender()  {
        return mender= get("mender");
    }

    */
/**
     * Setter for property 'mender'.
     *
     * @param mender Value to set for property 'mender'.
     *//*

    public BaseModel<M> setMender(String mender) {
        this.mender = mender;
        set("mender", mender);
        return this;
    }

    */
/**
     * Getter for property 'createTime'.
     *
     * @return Value for property 'createTime'.
     *//*

    public Date getCreateTime() {
        return create_time= getDate("create_time");
    }

    */
/**
     * Setter for property 'createTime'.
     *
     * @param createTime Value to set for property 'createTime'.
     *//*

    public BaseModel<M> setCreateTime(Date createTime) {
        this.create_time = createTime;
        set("create_time", createTime);
        return this;
    }

    */
/**
     * Getter for property 'modifyTime'.
     *
     * @return Value for property 'modifyTime'.
     *//*

    public Date getModifyTime() {
        return modify_time= get("modify_time");
    }

    */
/**
     * Setter for property 'modifyTime'.
     *
     * @param modifyTime Value to set for property 'modifyTime'.
     *//*

    public BaseModel<M> setModifyTime(Date modifyTime) {
        modify_time = modifyTime;
        set("modify_time",modifyTime);
        return this;
    }

    */
/**
     * Getter for property 'isDel'.
     *
     * @return Value for property 'isDel'.
     *//*

    public boolean getIsDel() {
        return is_del=getStr("is_del").equals("1");
    }

    */
/**
     * Setter for property 'isDel'.
     *
     * @param isDel Value to set for property 'isDel'.
     * @return
     *//*

    public BaseModel<M> setIsDel(boolean isDel) {
        is_del = isDel;
        set("is_del", isDel ? 1 : 0);
        return this;
    }


*/

}   
