package space.yizhu.bean;/* Created by xiuxi on 2017.6.20.*/

import space.yizhu.kits.SysKit;
import space.yizhu.record.plugin.activerecord.Db;
import space.yizhu.record.plugin.activerecord.Model;
import space.yizhu.record.plugin.activerecord.Table;
import space.yizhu.record.plugin.activerecord.TableMapping;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BaseModel<M extends BaseModel<M>> extends Model<M> {
    public static final Model me = new BaseModel().dao();

    public String[] getField() {
        return getFields().toArray(new String[]{});
    }

    public List<String> getFields() {
        return new ArrayList<String>(this.getTable().getColumnTypeMap().keySet());
    }


    @Override
    public Date getDate(String attr) {
        try {
            return super.getDate(attr);
        } catch (Exception e) {
            try {
                return DateUtils.parseDate(getStr(attr), new String[]{"yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy/mm/dd HH:mm:ss", "yyyy-mm-dd HH:mm:ss", "yyyy-mm-dd", "yyyy/mm/dd"});
            } catch (ParseException e1) {
                e1.printStackTrace();
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
        ;

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
    public List<M> findBy(String key, List values) {
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

    //仿写于byId
    public boolean delBy(String key, String value) {
        Table table = this.getTable();

        //language=SQL
        String sql = "delete  from " + table.getName() + " where " + key + " = '" + value + "' ";
        int result = 0;
        try {
            result = Db.update(sql);
        } catch (Exception e) {
            SysKit.print(e, "delby");
        }
        return result > -1;
    }

    //排序
    public List<M> findBy(String key, String value, String order) {
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

        sql.append("order by ").append(order).append(" desc");
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


    //------------标准表-----------
    public String id() {
        return getStr("id");
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
        return getStr("updator");
    }

    public Integer is_del() {
        return getInt("is_del");
    }

    public String create_date() {
        return getStr("create_date");
    }

    public String modify_time() {
        return getStr("modify_time");
    }

}
