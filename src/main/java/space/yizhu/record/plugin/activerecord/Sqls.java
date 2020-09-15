

package space.yizhu.record.plugin.activerecord;

import java.util.concurrent.ConcurrentHashMap;

import space.yizhu.record.core.Const;
import space.yizhu.kits.ConfigFile;


@Deprecated
public class Sqls {

    private static ConfigFile configFile = null;
    private static final ConcurrentHashMap<String, ConfigFile> map = new ConcurrentHashMap<String, ConfigFile>();

    private Sqls() {
    }

    
    public static void load(String sqlFileName) {
        use(sqlFileName);
    }

    public static String get(String sqlKey) {
        if (configFile == null)
            throw new IllegalStateException("Init sql propties file by invoking Sqls.load(String fileName) method first.");
        return configFile.get(sqlKey);
    }

    public static String get(String slqFileName, String sqlKey) {
        ConfigFile configFile = map.get(slqFileName);
        if (configFile == null)
            throw new IllegalStateException("Init sql propties file by invoking Sqls.load(String fileName) method first.");
        return configFile.get(sqlKey);
    }

    private static ConfigFile use(String fileName) {
        return use(fileName, Const.DEFAULT_ENCODING);
    }

    private static ConfigFile use(String fileName, String encoding) {
        ConfigFile result = map.get(fileName);
        if (result == null) {
            result = new ConfigFile(fileName, encoding);
            map.put(fileName, result);
            if (Sqls.configFile == null)
                Sqls.configFile = result;
        }
        return result;
    }

    public static ConfigFile useless(String sqlFileName) {
        ConfigFile previous = map.remove(sqlFileName);
        if (Sqls.configFile == previous)
            Sqls.configFile = null;
        return previous;
    }

    public static void clear() {
        configFile = null;
        map.clear();
    }
}



