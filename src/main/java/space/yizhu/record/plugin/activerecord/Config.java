

package space.yizhu.record.plugin.activerecord;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import space.yizhu.kits.LogKit;
import space.yizhu.kits.StrKit;
import space.yizhu.record.plugin.activerecord.cache.EhCache;
import space.yizhu.record.plugin.activerecord.cache.ICache;
import space.yizhu.record.plugin.activerecord.dialect.Dialect;
import space.yizhu.record.plugin.activerecord.dialect.MysqlDialect;
import space.yizhu.record.plugin.activerecord.sql.SqlKit;

public class Config {
    private final ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    String name;
    DataSource dataSource;

    Dialect dialect;
    boolean showSql;
    boolean devMode;
    int transactionLevel;
    IContainerFactory containerFactory;
    IDbProFactory dbProFactory = IDbProFactory.defaultDbProFactory;
    ICache cache;

    SqlKit sqlKit;

    
    Config(String name, DataSource dataSource, int transactionLevel) {
        init(name, dataSource, new MysqlDialect(), false, false, transactionLevel, IContainerFactory.defaultContainerFactory, new EhCache());
    }

    
    public Config(String name, DataSource dataSource, Dialect dialect, boolean showSql, boolean devMode, int transactionLevel, IContainerFactory containerFactory, ICache cache) {
        if (dataSource == null) {
            throw new IllegalArgumentException("DataSource can not be null");
        }
        init(name, dataSource, dialect, showSql, devMode, transactionLevel, containerFactory, cache);
    }

    private void init(String name, DataSource dataSource, Dialect dialect, boolean showSql, boolean devMode, int transactionLevel, IContainerFactory containerFactory, ICache cache) {
        if (StrKit.isBlank(name)) {
            throw new IllegalArgumentException("Config name can not be blank");
        }
        if (dialect == null) {
            throw new IllegalArgumentException("Dialect can not be null");
        }
        if (containerFactory == null) {
            throw new IllegalArgumentException("ContainerFactory can not be null");
        }
        if (cache == null) {
            throw new IllegalArgumentException("Cache can not be null");
        }

        this.name = name.trim();
        this.dataSource = dataSource;
        this.dialect = dialect;
        this.showSql = showSql;
        this.devMode = devMode;
        
        this.setTransactionLevel(transactionLevel);
        this.containerFactory = containerFactory;
        this.cache = cache;

        this.sqlKit = new SqlKit(this.name, this.devMode);
    }

    
    public Config(String name, DataSource dataSource) {
        this(name, dataSource, new MysqlDialect());
    }

    
    public Config(String name, DataSource dataSource, Dialect dialect) {
        this(name, dataSource, dialect, false, false, DbKit.DEFAULT_TRANSACTION_LEVEL, IContainerFactory.defaultContainerFactory, new EhCache());
    }

    private Config() {

    }

    void setDevMode(boolean devMode) {
        this.devMode = devMode;
        this.sqlKit.setDevMode(devMode);
    }

    void setTransactionLevel(int transactionLevel) {
        int t = transactionLevel;
        if (t != 0 && t != 1 && t != 2 && t != 4 && t != 8) {
            throw new IllegalArgumentException("The transactionLevel only be 0, 1, 2, 4, 8");
        }
        this.transactionLevel = transactionLevel;
    }

    
    static Config createBrokenConfig() {
        Config ret = new Config();
        ret.dialect = new MysqlDialect();
        ret.showSql = false;
        ret.devMode = false;
        ret.transactionLevel = DbKit.DEFAULT_TRANSACTION_LEVEL;
        ret.containerFactory = IContainerFactory.defaultContainerFactory;
        ret.cache = new EhCache();
        return ret;
    }

    public String getName() {
        return name;
    }

    public SqlKit getSqlKit() {
        return sqlKit;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public ICache getCache() {
        return cache;
    }

    public int getTransactionLevel() {
        return transactionLevel;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public IContainerFactory getContainerFactory() {
        return containerFactory;
    }

    public IDbProFactory getDbProFactory() {
        return dbProFactory;
    }

    public boolean isShowSql() {
        return showSql;
    }

    public boolean isDevMode() {
        return devMode;
    }

    

    
    public void setThreadLocalConnection(Connection connection) {
        threadLocal.set(connection);
    }

    public void removeThreadLocalConnection() {
        threadLocal.remove();
    }

    
    public Connection getConnection() throws SQLException {
        Connection conn = threadLocal.get();
        if (conn != null)
            return conn;
        return showSql ? new SqlReporter(dataSource.getConnection()).getConnection() : dataSource.getConnection();
    }

    
    public Connection getThreadLocalConnection() {
        return threadLocal.get();
    }

    
    public boolean isInTransaction() {
        return threadLocal.get() != null;
    }

    
    public void close(ResultSet rs, Statement st, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LogKit.error(e.getMessage(), e);
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                LogKit.error(e.getMessage(), e);
            }
        }

        if (threadLocal.get() == null) {    
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new ActiveRecordException(e);
                }
            }
        }
    }

    public void close(Statement st, Connection conn) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                LogKit.error(e.getMessage(), e);
            }
        }

        if (threadLocal.get() == null) {    
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new ActiveRecordException(e);
                }
            }
        }
    }

    public void close(Connection conn) {
        if (threadLocal.get() == null)        
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new ActiveRecordException(e);
                }
    }
}



