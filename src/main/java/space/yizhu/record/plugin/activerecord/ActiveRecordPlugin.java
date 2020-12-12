

package space.yizhu.record.plugin.activerecord;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import space.yizhu.kits.StrKit;
import space.yizhu.record.plugin.IPlugin;
import space.yizhu.record.plugin.activerecord.cache.ICache;
import space.yizhu.record.plugin.activerecord.dialect.Dialect;
import space.yizhu.record.plugin.activerecord.sql.SqlKit;
import space.yizhu.record.plugin.activerecord.cache.EhCache;
import space.yizhu.record.plugin.activerecord.dialect.MysqlDialect;
import space.yizhu.record.template.Engine;
import space.yizhu.record.template.source.ISource;


public class ActiveRecordPlugin implements IPlugin {

    protected IDataSourceProvider dataSourceProvider = null;
    protected Boolean devMode = null;

    protected Config config = null;

    protected volatile boolean isStarted = false;
    protected List<Table> tableList = new ArrayList<Table>();

    public ActiveRecordPlugin(String configName, DataSource dataSource, int transactionLevel) {
        if (StrKit.isBlank(configName)) {
            throw new IllegalArgumentException("configName can not be blank");
        }
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource can not be null");
        }
        this.config = new Config(configName, dataSource, transactionLevel);
    }

    public ActiveRecordPlugin(DataSource dataSource) {
        this(DbConfig.MAIN_CONFIG_NAME, dataSource);
    }

    public ActiveRecordPlugin(String configName, DataSource dataSource) {
        this(configName, dataSource, DbConfig.DEFAULT_TRANSACTION_LEVEL);
    }

    public ActiveRecordPlugin(DataSource dataSource, int transactionLevel) {
        this(DbConfig.MAIN_CONFIG_NAME, dataSource, transactionLevel);
    }

    public ActiveRecordPlugin(String configName, IDataSourceProvider dataSourceProvider, int transactionLevel) {
        if (StrKit.isBlank(configName)) {
            throw new IllegalArgumentException("configName can not be blank");
        }
        if (dataSourceProvider == null) {
            throw new IllegalArgumentException("dataSourceProvider can not be null");
        }
        this.dataSourceProvider = dataSourceProvider;
        this.config = new Config(configName, null, transactionLevel);
    }

    public ActiveRecordPlugin(IDataSourceProvider dataSourceProvider) {
        this(DbConfig.MAIN_CONFIG_NAME, dataSourceProvider);
    }

    public ActiveRecordPlugin(String configName, IDataSourceProvider dataSourceProvider) {
        this(configName, dataSourceProvider, DbConfig.DEFAULT_TRANSACTION_LEVEL);
    }

    public ActiveRecordPlugin(IDataSourceProvider dataSourceProvider, int transactionLevel) {
        this(DbConfig.MAIN_CONFIG_NAME, dataSourceProvider, transactionLevel);
    }

    public ActiveRecordPlugin(Config config) {
        if (config == null) {
            throw new IllegalArgumentException("Config can not be null");
        }
        this.config = config;
    }

    public ActiveRecordPlugin addMapping(String tableName, String primaryKey, Class<? extends Model<?>> modelClass) {
        tableList.add(new Table(tableName, primaryKey, modelClass));
        return this;
    }

    public ActiveRecordPlugin addMapping(String tableName, Class<? extends Model<?>> modelClass) {
        tableList.add(new Table(tableName, modelClass));
        return this;
    }

    public ActiveRecordPlugin addSqlTemplate(String sqlTemplate) {
        config.sqlKit.addSqlTemplate(sqlTemplate);
        return this;
    }

    public ActiveRecordPlugin addSqlTemplate(ISource sqlTemplate) {
        config.sqlKit.addSqlTemplate(sqlTemplate);
        return this;
    }

    public ActiveRecordPlugin setBaseSqlTemplatePath(String baseSqlTemplatePath) {
        config.sqlKit.setBaseSqlTemplatePath(baseSqlTemplatePath);
        return this;
    }

    public SqlKit getSqlKit() {
        return config.sqlKit;
    }

    public Engine getEngine() {
        return getSqlKit().getEngine();
    }

    
    public ActiveRecordPlugin setTransactionLevel(int transactionLevel) {
        config.setTransactionLevel(transactionLevel);
        return this;
    }

    public ActiveRecordPlugin setCache(ICache cache) {
        if (cache == null) {
            throw new IllegalArgumentException("cache can not be null");
        }
        config.cache = cache;
        return this;
    }

    public ActiveRecordPlugin setShowSql(boolean showSql) {
        config.showSql = showSql;
        return this;
    }

    public ActiveRecordPlugin setDevMode(boolean devMode) {
        this.devMode = devMode;
        config.setDevMode(devMode);
        return this;
    }

    public Boolean getDevMode() {
        return devMode;
    }

    public ActiveRecordPlugin setDialect(Dialect dialect) {
        if (dialect == null) {
            throw new IllegalArgumentException("dialect can not be null");
        }
        config.dialect = dialect;
        if (config.transactionLevel == Connection.TRANSACTION_REPEATABLE_READ && dialect.isOracle()) {
            
            config.transactionLevel = Connection.TRANSACTION_READ_COMMITTED;
        }
        return this;
    }

    public ActiveRecordPlugin setContainerFactory(IContainerFactory containerFactory) {
        if (containerFactory == null) {
            throw new IllegalArgumentException("containerFactory can not be null");
        }
        config.containerFactory = containerFactory;
        return this;
    }

    public ActiveRecordPlugin setDbProFactory(IDbProFactory dbProFactory) {
        if (dbProFactory == null) {
            throw new IllegalArgumentException("dbProFactory can not be null");
        }
        config.dbProFactory = dbProFactory;
        return this;
    }

    
    public void setPrimaryKey(String tableName, String primaryKey) {
        for (Table table : tableList) {
            if (table.getName().equalsIgnoreCase(tableName.trim())) {
                table.setPrimaryKey(primaryKey);
            }
        }
    }

    public boolean start() {
        if (isStarted) {
            return true;
        }
        if (config.dataSource == null && dataSourceProvider != null) {
            config.dataSource = dataSourceProvider.getDataSource();
        }
        if (config.dataSource == null) {
            throw new RuntimeException("ActiveRecord start error: ActiveRecordPlugin need DataSource or DataSourceProvider");
        }

        config.sqlKit.parseSqlTemplate();

        new TableBuilder().build(tableList, config);
        DbConfig.addConfig(config);
        isStarted = true;
        return true;
    }

    public boolean stop() {
        DbConfig.removeConfig(config.getName());
        isStarted = false;
        return true;
    }

    
    public static void useAsDataTransfer(Dialect dialect, IContainerFactory containerFactory, ICache cache) {
        if (dialect == null) {
            throw new IllegalArgumentException("dialect can not be null");
        }
        if (containerFactory == null) {
            throw new IllegalArgumentException("containerFactory can not be null");
        }
        if (cache == null) {
            throw new IllegalArgumentException("cache can not be null");
        }
        ActiveRecordPlugin arp = new ActiveRecordPlugin(new NullDataSource());
        arp.setDialect(dialect);
        arp.setContainerFactory(containerFactory);
        arp.setCache(cache);
        arp.start();
        DbConfig.brokenConfig = arp.config;
    }

    
    public static void useAsDataTransfer(IContainerFactory containerFactory) {
        useAsDataTransfer(new MysqlDialect(), containerFactory, new EhCache());
    }

    
    public static void useAsDataTransfer(Dialect dialect, IContainerFactory containerFactory) {
        useAsDataTransfer(dialect, containerFactory, new EhCache());
    }

    
    public static void useAsDataTransfer(Dialect dialect) {
        useAsDataTransfer(dialect, IContainerFactory.defaultContainerFactory, new EhCache());
    }

    
    public static void useAsDataTransfer() {
        useAsDataTransfer(new MysqlDialect(), IContainerFactory.defaultContainerFactory, new EhCache());
    }

    public Config getConfig() {
        return config;
    }
}






