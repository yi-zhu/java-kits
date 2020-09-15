

package space.yizhu.record.plugin.activerecord.generator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.sql.DataSource;

import space.yizhu.kits.StrKit;
import space.yizhu.record.plugin.activerecord.dialect.Dialect;
import space.yizhu.record.plugin.activerecord.dialect.MysqlDialect;
import space.yizhu.record.plugin.activerecord.dialect.OracleDialect;


public class MetaBuilder {

    protected DataSource dataSource;
    protected Dialect dialect = new MysqlDialect();
    protected Set<String> excludedTables = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

    protected Connection conn = null;
    protected DatabaseMetaData dbMeta = null;

    protected String[] removedTableNamePrefixes = null;

    protected TypeMapping typeMapping = new TypeMapping();

    protected boolean generateRemarks = false;    

    public MetaBuilder(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource can not be null.");
        }
        this.dataSource = dataSource;
    }

    public void setGenerateRemarks(boolean generateRemarks) {
        this.generateRemarks = generateRemarks;
    }

    public void setDialect(Dialect dialect) {
        if (dialect != null) {
            this.dialect = dialect;
        }
    }

    public void addExcludedTable(String... excludedTables) {
        if (excludedTables != null) {
            for (String table : excludedTables) {
                this.excludedTables.add(table);
            }
        }
    }

    
    public void setRemovedTableNamePrefixes(String... removedTableNamePrefixes) {
        this.removedTableNamePrefixes = removedTableNamePrefixes;
    }

    public void setTypeMapping(TypeMapping typeMapping) {
        if (typeMapping != null) {
            this.typeMapping = typeMapping;
        }
    }

    public List<TableMeta> build() {
        System.out.println("Build TableMeta ...");
        try {
            conn = dataSource.getConnection();
            dbMeta = conn.getMetaData();

            List<TableMeta> ret = new ArrayList<TableMeta>();
            buildTableNames(ret);
            for (TableMeta tableMeta : ret) {
                buildPrimaryKey(tableMeta);
                buildColumnMetas(tableMeta);
            }
            removeNoPrimaryKeyTable(ret);
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    
    protected void removeNoPrimaryKeyTable(List<TableMeta> ret) {
        for (java.util.Iterator<TableMeta> it = ret.iterator(); it.hasNext(); ) {
            TableMeta tm = it.next();
            if (StrKit.isBlank(tm.primaryKey)) {
                it.remove();
                System.err.println("Skip table " + tm.name + " because there is no primary key");
            }
        }
    }

    
    protected boolean isSkipTable(String tableName) {
        return false;
    }

    
    protected String buildModelName(String tableName) {
        
        if (removedTableNamePrefixes != null) {
            for (String prefix : removedTableNamePrefixes) {
                if (tableName.startsWith(prefix)) {
                    tableName = tableName.replaceFirst(prefix, "");
                    break;
                }
            }
        }

        
        if (dialect instanceof OracleDialect) {
            tableName = tableName.toLowerCase();
        }

        return StrKit.firstCharToUpperCase(StrKit.toCamelCase(tableName));
    }

    
    protected String buildBaseModelName(String modelName) {
        return "Base" + modelName;
    }

    
    protected ResultSet getTablesResultSet() throws SQLException {
        String schemaPattern = dialect instanceof OracleDialect ? dbMeta.getUserName() : null;
        
        return dbMeta.getTables(conn.getCatalog(), schemaPattern, null, new String[]{"TABLE"});    
    }

    protected void buildTableNames(List<TableMeta> ret) throws SQLException {
        ResultSet rs = getTablesResultSet();
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");

            if (excludedTables.contains(tableName)) {
                System.out.println("Skip table :" + tableName);
                continue;
            }
            if (isSkipTable(tableName)) {
                System.out.println("Skip table :" + tableName);
                continue;
            }

            TableMeta tableMeta = new TableMeta();
            tableMeta.name = tableName;
            tableMeta.remarks = rs.getString("REMARKS");

            tableMeta.modelName = buildModelName(tableName);
            tableMeta.baseModelName = buildBaseModelName(tableMeta.modelName);
            ret.add(tableMeta);
        }
        rs.close();
    }

    protected void buildPrimaryKey(TableMeta tableMeta) throws SQLException {
        ResultSet rs = dbMeta.getPrimaryKeys(conn.getCatalog(), null, tableMeta.name);

        String primaryKey = "";
        int index = 0;
        while (rs.next()) {
            if (index++ > 0) {
                primaryKey += ",";
            }
            primaryKey += rs.getString("COLUMN_NAME");
        }

        
        
        
        

        tableMeta.primaryKey = primaryKey;
        rs.close();
    }

    
    protected void buildColumnMetas(TableMeta tableMeta) throws SQLException {
        String sql = dialect.forTableBuilderDoBuild(tableMeta.name);
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();


        Map<String, ColumnMeta> columnMetaMap = new HashMap<>();
        if (generateRemarks) {
            DatabaseMetaData dbMeta = conn.getMetaData();
            ResultSet colMetaRs = null;
            try {
                colMetaRs = dbMeta.getColumns(null, null, tableMeta.name, null);
                while (colMetaRs.next()) {
                    ColumnMeta columnMeta = new ColumnMeta();
                    columnMeta.name = colMetaRs.getString("COLUMN_NAME");
                    columnMeta.remarks = colMetaRs.getString("REMARKS");
                    columnMetaMap.put(columnMeta.name, columnMeta);
                }
            } catch (Exception e) {
                System.out.println("无法生成 REMARKS");
            } finally {
                if (colMetaRs != null) {
                    colMetaRs.close();
                }
            }
        }


        for (int i = 1; i <= columnCount; i++) {
            ColumnMeta cm = new ColumnMeta();
            cm.name = rsmd.getColumnName(i);

            String typeStr = null;
            if (dialect.isKeepByteAndShort()) {
                int type = rsmd.getColumnType(i);
                if (type == Types.TINYINT) {
                    typeStr = "java.lang.Byte";
                } else if (type == Types.SMALLINT) {
                    typeStr = "java.lang.Short";
                }
            }

            if (typeStr == null) {
                String colClassName = rsmd.getColumnClassName(i);
                typeStr = typeMapping.getType(colClassName);
            }

            if (typeStr == null) {
                int type = rsmd.getColumnType(i);
                if (type == Types.BINARY || type == Types.VARBINARY || type == Types.LONGVARBINARY || type == Types.BLOB) {
                    typeStr = "byte[]";
                } else if (type == Types.CLOB || type == Types.NCLOB) {
                    typeStr = "java.lang.String";
                }
                
                
                else if (type == Types.TIMESTAMP || type == Types.DATE) {
                    typeStr = "java.util.Date";
                }
                
                else if (type == Types.OTHER) {
                    typeStr = "java.lang.Object";
                } else {
                    typeStr = "java.lang.String";
                }
            }

            typeStr = handleJavaType(typeStr, rsmd, i);

            cm.javaType = typeStr;

            
            cm.attrName = buildAttrName(cm.name);

            
            if (generateRemarks && columnMetaMap.containsKey(cm.name)) {
                cm.remarks = columnMetaMap.get(cm.name).remarks;
            }

            tableMeta.columnMetas.add(cm);
        }

        rs.close();
        stm.close();
    }

    
    protected String handleJavaType(String typeStr, ResultSetMetaData rsmd, int column) throws SQLException {
        
        if (!dialect.isOracle()) {
            return typeStr;
        }

        
        if ("java.math.BigDecimal".equals(typeStr)) {
            int scale = rsmd.getScale(column);            
            int precision = rsmd.getPrecision(column);    
            if (scale == 0) {
                if (precision <= 9) {
                    typeStr = "java.lang.Integer";
                } else if (precision <= 18) {
                    typeStr = "java.lang.Long";
                } else {
                    typeStr = "java.math.BigDecimal";
                }
            } else {
                
                typeStr = "java.math.BigDecimal";
            }
        }

        return typeStr;
    }

    
    protected String buildAttrName(String colName) {
        if (dialect instanceof OracleDialect) {
            colName = colName.toLowerCase();
        }
        return StrKit.toCamelCase(colName);
    }
}







