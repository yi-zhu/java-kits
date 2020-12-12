

package space.yizhu.record.plugin.activerecord;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


public class Record implements Serializable {

    private static final long serialVersionUID = 905784513600884082L;

    private Map<String, Object> columns;    

    
    public Record setContainerFactoryByConfigName(String configName) {
        Config config = DbConfig.getConfig(configName);
        if (config == null)
            throw new IllegalArgumentException("Config not found: " + configName);

        processColumnsMap(config);
        return this;
    }

    
    void setColumnsMap(Map<String, Object> columns) {
        this.columns = columns;
    }

    @SuppressWarnings("unchecked")
    private void processColumnsMap(Config config) {
        if (columns == null || columns.size() == 0) {
            columns = config.containerFactory.getColumnsMap();
        } else {
            Map<String, Object> columnsOld = columns;
            columns = config.containerFactory.getColumnsMap();
            columns.putAll(columnsOld);
        }
    }

    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getColumns() {
        if (columns == null) {
            if (DbConfig.config == null)
                columns = DbConfig.brokenConfig.containerFactory.getColumnsMap();
            else
                columns = DbConfig.config.containerFactory.getColumnsMap();
        }
        return columns;
    }

    
    public Record setColumns(Map<String, Object> columns) {
        this.getColumns().putAll(columns);
        return this;
    }

    
    public Record setColumns(Record record) {
        getColumns().putAll(record.getColumns());
        return this;
    }

    
    public Record setColumns(Model<?> model) {
        getColumns().putAll(model._getAttrs());
        return this;
    }

    
    public Record remove(String column) {
        getColumns().remove(column);
        return this;
    }

    
    public Record remove(String... columns) {
        if (columns != null)
            for (String c : columns)
                this.getColumns().remove(c);
        return this;
    }

    
    public Record removeNullValueColumns() {
        for (java.util.Iterator<Entry<String, Object>> it = getColumns().entrySet().iterator(); it.hasNext(); ) {
            Entry<String, Object> e = it.next();
            if (e.getValue() == null) {
                it.remove();
            }
        }
        return this;
    }

    
    public Record keep(String... columns) {
        if (columns != null && columns.length > 0) {
            Map<String, Object> newColumns = new HashMap<String, Object>(columns.length);    
            for (String c : columns)
                if (this.getColumns().containsKey(c))    
                    newColumns.put(c, this.getColumns().get(c));

            this.getColumns().clear();
            this.getColumns().putAll(newColumns);
        } else
            this.getColumns().clear();
        return this;
    }

    
    public Record keep(String column) {
        if (getColumns().containsKey(column)) {    
            Object keepIt = getColumns().get(column);
            getColumns().clear();
            getColumns().put(column, keepIt);
        } else
            getColumns().clear();
        return this;
    }

    
    public Record clear() {
        getColumns().clear();
        return this;
    }

    
    public Record set(String column, Object value) {
        getColumns().put(column, value);
        return this;
    }

    
    @SuppressWarnings("unchecked")
    public <T> T get(String column) {
        return (T) getColumns().get(column);
    }

    
    @SuppressWarnings("unchecked")
    public <T> T get(String column, Object defaultValue) {
        Object result = getColumns().get(column);
        return (T) (result != null ? result : defaultValue);
    }

    public Object getObject(String column) {
        return getColumns().get(column);
    }

    public Object getObject(String column, Object defaultValue) {
        Object result = getColumns().get(column);
        return result != null ? result : defaultValue;
    }

    
    public String getStr(String column) {
        
        Object s = getColumns().get(column);
        return s != null ? s.toString() : null;
    }

    
    public Integer getInt(String column) {
        Number n = getNumber(column);
        return n != null ? n.intValue() : null;
    }

    
    public Long getLong(String column) {
        Number n = getNumber(column);
        return n != null ? n.longValue() : null;
    }

    
    public java.math.BigInteger getBigInteger(String column) {
        return (java.math.BigInteger) getColumns().get(column);
    }

    
    public java.util.Date getDate(String column) {
        return (java.util.Date) getColumns().get(column);
    }

    
    public java.sql.Time getTime(String column) {
        return (java.sql.Time) getColumns().get(column);
    }

    
    public java.sql.Timestamp getTimestamp(String column) {
        return (java.sql.Timestamp) getColumns().get(column);
    }

    
    public Double getDouble(String column) {
        Number n = getNumber(column);
        return n != null ? n.doubleValue() : null;
    }

    
    public Float getFloat(String column) {
        Number n = getNumber(column);
        return n != null ? n.floatValue() : null;
    }

    public Short getShort(String column) {
        Number n = getNumber(column);
        return n != null ? n.shortValue() : null;
    }

    public Byte getByte(String column) {
        Number n = getNumber(column);
        return n != null ? n.byteValue() : null;
    }

    
    public Boolean getBoolean(String column) {
        return (Boolean) getColumns().get(column);
    }

    
    public java.math.BigDecimal getBigDecimal(String column) {
        return (java.math.BigDecimal) getColumns().get(column);
    }

    
    public byte[] getBytes(String column) {
        return (byte[]) getColumns().get(column);
    }

    
    public Number getNumber(String column) {
        return (Number) getColumns().get(column);
    }

    public String toString() {
        if (columns == null) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean first = true;
        for (Entry<String, Object> e : getColumns().entrySet()) {
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
        if (!(o instanceof Record))
            return false;
        if (o == this)
            return true;
        return getColumns().equals(((Record) o).getColumns());
    }

    public int hashCode() {
        return getColumns().hashCode();
    }

    
    public String[] getColumnNames() {
        Set<String> attrNameSet = getColumns().keySet();
        return attrNameSet.toArray(new String[attrNameSet.size()]);
    }

    
    public Object[] getColumnValues() {
        java.util.Collection<Object> attrValueCollection = getColumns().values();
        return attrValueCollection.toArray(new Object[attrValueCollection.size()]);
    }

    
    public String toJson() {
        return new Gson().toJson(getColumns());
    }
}




