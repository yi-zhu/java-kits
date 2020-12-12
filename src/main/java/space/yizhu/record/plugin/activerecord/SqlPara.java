

package space.yizhu.record.plugin.activerecord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SqlPara implements Serializable {

    private static final long serialVersionUID = -8586448059592782381L;

    String sql;
    List<Object> paraList;

    public SqlPara setSql(String sql) {
        this.sql = sql;
        return this;
    }

    public SqlPara addPara(Object para) {
        if (paraList == null) {
            paraList = new ArrayList<Object>();
        }
        paraList.add(para);
        return this;
    }

    public String getSql() {
        return sql;
    }

    public Object[] getPara() {
        if (paraList == null || paraList.size() == 0) {
            return DbConfig.NULL_PARA_ARRAY;
        } else {
            return paraList.toArray(new Object[paraList.size()]);
        }
    }

    public SqlPara clear() {
        sql = null;
        if (paraList != null) {
            paraList.clear();
        }
        return this;
    }

    public String toString() {
        return "Sql: " + sql + "\nPara: " + paraList;
    }
}
