

package space.yizhu.record.plugin.activerecord;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@SuppressWarnings("rawtypes")
public final class DbKit {

    
    static Config config = null;

    
    static Config brokenConfig = Config.createBrokenConfig();

    private static Map<Class<? extends Model>, Config> modelToConfig = new HashMap<Class<? extends Model>, Config>(512, 0.5F);
    private static Map<String, Config> configNameToConfig = new HashMap<String, Config>(32, 0.25F);

    static final Object[] NULL_PARA_ARRAY = new Object[0];
    public static final String MAIN_CONFIG_NAME = "main";
    public static final int DEFAULT_TRANSACTION_LEVEL = Connection.TRANSACTION_REPEATABLE_READ;

    private DbKit() {
    }

    
    public static void addConfig(Config config) {
        if (config == null) {
            throw new IllegalArgumentException("Config can not be null");
        }
        if (configNameToConfig.containsKey(config.getName())) {
            throw new IllegalArgumentException("Config already exists: " + config.getName());
        }

        configNameToConfig.put(config.getName(), config);

        
        if (MAIN_CONFIG_NAME.equals(config.getName())) {
            DbKit.config = config;
            Db.init(DbKit.config.getName());
        }

        
        if (DbKit.config == null) {
            DbKit.config = config;
            Db.init(DbKit.config.getName());
        }
    }

    public static Config removeConfig(String configName) {
        if (DbKit.config != null && DbKit.config.getName().equals(configName)) {
            
            DbKit.config = null;
        }

        Db.removeDbProWithConfig(configName);
        return configNameToConfig.remove(configName);
    }

    static void addModelToConfigMapping(Class<? extends Model> modelClass, Config config) {
        modelToConfig.put(modelClass, config);
    }

    public static Config getConfig() {
        return config;
    }

    public static Config getConfig(String configName) {
        return configNameToConfig.get(configName);
    }

    public static Config getConfig(Class<? extends Model> modelClass) {
        return modelToConfig.get(modelClass);
    }

    static final void close(ResultSet rs, Statement st) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new ActiveRecordException(e);
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new ActiveRecordException(e);
            }
        }
    }

    static final void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new ActiveRecordException(e);
            }
        }
    }

    public static Set<Map.Entry<String, Config>> getConfigSet() {
        return configNameToConfig.entrySet();
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends Model> getUsefulClass(Class<? extends Model> modelClass) {
        
        
        return (Class<? extends Model>) (modelClass.getName().indexOf("$$EnhancerBy") == -1 ? modelClass : modelClass.getSuperclass());
    }
}




