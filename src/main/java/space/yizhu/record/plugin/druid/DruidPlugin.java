

package space.yizhu.record.plugin.druid;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import space.yizhu.kits.StrKit;
import space.yizhu.record.plugin.IPlugin;
import space.yizhu.record.plugin.activerecord.IDataSourceProvider;


public class DruidPlugin implements IPlugin, IDataSourceProvider {
    
    private String name = null;

    
    private String url;
    private String username;
    private String password;
    private String publicKey;
    private String driverClass = null;    

    
    private int initialSize = 1;
    private int minIdle = 10;
    private int maxActive = 32;

    
    private long maxWait = DruidDataSource.DEFAULT_MAX_WAIT;

    
    private long timeBetweenEvictionRunsMillis = DruidDataSource.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;
    
    private long minEvictableIdleTimeMillis = DruidDataSource.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;
    
    private long timeBetweenConnectErrorMillis = DruidDataSource.DEFAULT_TIME_BETWEEN_CONNECT_ERROR_MILLIS;

    
    private String validationQuery = "select 1";
    private String connectionInitSql = null;
    private String connectionProperties = null;
    private boolean testWhileIdle = true;
    private boolean testOnBorrow = false;
    private boolean testOnReturn = false;

    
    private boolean removeAbandoned = false;
    
    private long removeAbandonedTimeoutMillis = 300 * 1000;
    
    private boolean logAbandoned = false;

    
    

    
    private int maxPoolPreparedStatementPerConnectionSize = -1;

    
    private String filters;    
    private List<Filter> filterList;

    private DruidDataSource ds;
    private volatile boolean isStarted = false;

    public DruidPlugin(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validationQuery = autoCheckValidationQuery(url);
    }

    public DruidPlugin(String url, String username, String password, String driverClass) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.driverClass = driverClass;
        this.validationQuery = autoCheckValidationQuery(url);
    }

    public DruidPlugin(String url, String username, String password, String driverClass, String filters) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.driverClass = driverClass;
        this.filters = filters;
        this.validationQuery = autoCheckValidationQuery(url);
    }

    
    private static String autoCheckValidationQuery(String url) {
        if (url.startsWith("jdbc:oracle")) {
            return "select 1 from dual";
        } else if (url.startsWith("jdbc:db2")) {
            return "select 1 from sysibm.sysdummy1";
        } else if (url.startsWith("jdbc:hsqldb")) {
            return "select 1 from INFORMATION_SCHEMA.SYSTEM_USERS";
        } else if (url.startsWith("jdbc:derby")) {
            return "select 1 from INFORMATION_SCHEMA.SYSTEM_USERS";
        }
        return "select 1";
    }

    
    public void setConnectionInitSql(String sql) {
        this.connectionInitSql = sql;
    }

    public final String getName() {
        return name;
    }

    
    public final void setName(String name) {
        this.name = name;
    }

    
    public DruidPlugin setFilters(String filters) {
        this.filters = filters;
        return this;
    }

    public synchronized DruidPlugin addFilter(Filter filter) {
        if (filterList == null)
            filterList = new ArrayList<Filter>();
        filterList.add(filter);
        return this;
    }

    public boolean start() {
        if (isStarted)
            return true;

        ds = new DruidDataSource();
        if (this.name != null) {
            ds.setName(this.name);
        }
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        if (driverClass != null)
            ds.setDriverClassName(driverClass);
        ds.setInitialSize(initialSize);
        ds.setMinIdle(minIdle);
        ds.setMaxActive(maxActive);
        ds.setMaxWait(maxWait);
        ds.setTimeBetweenConnectErrorMillis(timeBetweenConnectErrorMillis);
        ds.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        ds.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);

        ds.setValidationQuery(validationQuery);
        if (StrKit.notBlank(connectionInitSql)) {
            List<String> connectionInitSqls = new ArrayList<String>();
            connectionInitSqls.add(this.connectionInitSql);
            ds.setConnectionInitSqls(connectionInitSqls);
        }
        ds.setTestWhileIdle(testWhileIdle);
        ds.setTestOnBorrow(testOnBorrow);
        ds.setTestOnReturn(testOnReturn);

        ds.setRemoveAbandoned(removeAbandoned);
        ds.setRemoveAbandonedTimeoutMillis(removeAbandonedTimeoutMillis);
        ds.setLogAbandoned(logAbandoned);

        
        ds.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);

        boolean hasSetConnectionProperties = false;
        if (StrKit.notBlank(filters)) {
            try {
                ds.setFilters(filters);
                
                if (filters.contains("config")) {
                    
                    if (StrKit.isBlank(this.publicKey)) {
                        throw new RuntimeException("Druid连接池的filter设定了config时，必须设定publicKey");
                    }
                    String decryptStr = "config.decrypt=true;config.decrypt.key=" + this.publicKey;
                    String cp = this.connectionProperties;
                    if (StrKit.isBlank(cp)) {
                        cp = decryptStr;
                    } else {
                        cp = cp + ";" + decryptStr;
                    }
                    ds.setConnectionProperties(cp);
                    hasSetConnectionProperties = true;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        
        if (!hasSetConnectionProperties && StrKit.notBlank(this.connectionProperties)) {
            ds.setConnectionProperties(this.connectionProperties);
        }
        addFilterList(ds);

        isStarted = true;
        return true;
    }

    private void addFilterList(DruidDataSource ds) {
        if (filterList != null) {
            List<Filter> targetList = ds.getProxyFilters();
            for (Filter add : filterList) {
                boolean found = false;
                for (Filter target : targetList) {
                    if (add.getClass().equals(target.getClass())) {
                        found = true;
                        break;
                    }
                }
                if (!found)
                    targetList.add(add);
            }
        }
    }

    public boolean stop() {
        if (ds != null)
            ds.close();

        ds = null;
        isStarted = false;
        return true;
    }

    public DataSource getDataSource() {
        return ds;
    }

    public DruidPlugin set(int initialSize, int minIdle, int maxActive) {
        this.initialSize = initialSize;
        this.minIdle = minIdle;
        this.maxActive = maxActive;
        return this;
    }

    public DruidPlugin setDriverClass(String driverClass) {
        this.driverClass = driverClass;
        return this;
    }

    public DruidPlugin setInitialSize(int initialSize) {
        this.initialSize = initialSize;
        return this;
    }

    public DruidPlugin setMinIdle(int minIdle) {
        this.minIdle = minIdle;
        return this;
    }

    public DruidPlugin setMaxActive(int maxActive) {
        this.maxActive = maxActive;
        return this;
    }

    public DruidPlugin setMaxWait(long maxWait) {
        this.maxWait = maxWait;
        return this;
    }

    public DruidPlugin setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
        return this;
    }

    public DruidPlugin setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
        return this;
    }

    
    public DruidPlugin setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
        return this;
    }

    public DruidPlugin setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
        return this;
    }

    public DruidPlugin setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
        return this;
    }

    public DruidPlugin setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
        return this;
    }

    public DruidPlugin setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
        return this;
    }

    public final DruidPlugin setTimeBetweenConnectErrorMillis(long timeBetweenConnectErrorMillis) {
        this.timeBetweenConnectErrorMillis = timeBetweenConnectErrorMillis;
        return this;
    }

    public final DruidPlugin setRemoveAbandoned(boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
        return this;
    }

    public final DruidPlugin setRemoveAbandonedTimeoutMillis(long removeAbandonedTimeoutMillis) {
        this.removeAbandonedTimeoutMillis = removeAbandonedTimeoutMillis;
        return this;
    }

    public final DruidPlugin setLogAbandoned(boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
        return this;
    }

    public final DruidPlugin setConnectionProperties(String connectionProperties) {
        this.connectionProperties = connectionProperties;
        return this;
    }

    public final DruidPlugin setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }
}
