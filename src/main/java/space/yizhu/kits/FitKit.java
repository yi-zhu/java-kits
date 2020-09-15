

package space.yizhu.kits;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import space.yizhu.record.core.Const;

/**
 * PropKit. PropKit can load properties file from CLASSPATH or File object.
 */
public class FitKit {

    private static ConfigFile configFile = null;
    private static final ConcurrentHashMap<String, ConfigFile> map = new ConcurrentHashMap<String, ConfigFile>();

    private FitKit() {
    }

    /**
     * Using the properties file. It will loading the properties file if not loading.
     * @see #use(String, String)
     */
    public static ConfigFile use(String fileName) {
        return use(fileName, Const.DEFAULT_ENCODING);
    }

    /**
     * Using the properties file. It will loading the properties file if not loading.
     * <p>
     * Example:<br>
     * PropKit.use("config.txt", "UTF-8");<br>
     * PropKit.use("other_config.txt", "UTF-8");<br><br>
     * String userName = PropKit.get("userName");<br>
     * String password = PropKit.get("password");<br><br>
     *
     * userName = PropKit.use("other_config.txt").get("userName");<br>
     * password = PropKit.use("other_config.txt").get("password");<br><br>
     *
     * PropKit.use("com/jfinal/config_in_sub_directory_of_classpath.txt");
     *
     * @param fileName the properties file's name in classpath or the sub directory of classpath
     * @param encoding the encoding
     */
    public static ConfigFile use(String fileName, String encoding) {
        ConfigFile result = map.get(fileName);
        if (result == null) {
            synchronized (FitKit.class) {
                result = map.get(fileName);
                if (result == null) {
                    result = new ConfigFile(fileName, encoding);
                    map.put(fileName, result);
                    if (FitKit.configFile == null) {
                        FitKit.configFile = result;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Using the properties file bye File object. It will loading the properties file if not loading.
     * @see #use(File, String)
     */
    public static ConfigFile use(File file) {
        return use(file, Const.DEFAULT_ENCODING);
    }

    /**
     * Using the properties file bye File object. It will loading the properties file if not loading.
     * <p>
     * Example:<br>
     * PropKit.use(new File("/var/config/my_config.txt"), "UTF-8");<br>
     * Strig userName = PropKit.use("my_config.txt").get("userName");
     *
     * @param file the properties File object
     * @param encoding the encoding
     */
    public static ConfigFile use(File file, String encoding) {
        ConfigFile result = map.get(file.getName());
        if (result == null) {
            synchronized (FitKit.class) {
                result = map.get(file.getName());
                if (result == null) {
                    result = new ConfigFile(file, encoding);
                    map.put(file.getName(), result);
                    if (FitKit.configFile == null) {
                        FitKit.configFile = result;
                    }
                }
            }
        }
        return result;
    }

    public static ConfigFile useless(String fileName) {
        ConfigFile previous = map.remove(fileName);
        if (FitKit.configFile == previous) {
            FitKit.configFile = null;
        }
        return previous;
    }

    public static void clear() {
        configFile = null;
        map.clear();
    }

    public static ConfigFile append(ConfigFile configFile) {
        synchronized (FitKit.class) {
            if (FitKit.configFile != null) {
                FitKit.configFile.append(configFile);
            } else {
                FitKit.configFile = configFile;
            }
            return FitKit.configFile;
        }
    }

    public static ConfigFile append(String fileName, String encoding) {
        return append(new ConfigFile(fileName, encoding));
    }

    public static ConfigFile append(String fileName) {
        return append(fileName, Const.DEFAULT_ENCODING);
    }

    public static ConfigFile appendIfExists(String fileName, String encoding) {
        try {
            return append(new ConfigFile(fileName, encoding));
        } catch (Exception e) {
            return FitKit.configFile;
        }
    }

    public static ConfigFile appendIfExists(String fileName) {
        return appendIfExists(fileName, Const.DEFAULT_ENCODING);
    }

    public static ConfigFile append(File file, String encoding) {
        return append(new ConfigFile(file, encoding));
    }

    public static ConfigFile append(File file) {
        return append(file, Const.DEFAULT_ENCODING);
    }

    public static ConfigFile appendIfExists(File file, String encoding) {
        if (file.exists()) {
            append(new ConfigFile(file, encoding));
        }
        return FitKit.configFile;
    }

    public static ConfigFile appendIfExists(File file) {
        return appendIfExists(file, Const.DEFAULT_ENCODING);
    }

    public static ConfigFile getConfigFile() {
        if (configFile == null) {
            throw new IllegalStateException("Load propties file by invoking PropKit.use(String fileName) method first.");
        }
        return configFile;
    }

    public static ConfigFile getProp(String fileName) {
        return map.get(fileName);
    }

    public static String get(String key) {
        return getConfigFile().get(key);
    }

    public static String get(String key, String defaultValue) {
        return getConfigFile().get(key, defaultValue);
    }

    public static Integer getInt(String key) {
        return getConfigFile().getInt(key);
    }

    public static Integer getInt(String key, Integer defaultValue) {
        return getConfigFile().getInt(key, defaultValue);
    }

    public static Long getLong(String key) {
        return getConfigFile().getLong(key);
    }

    public static Long getLong(String key, Long defaultValue) {
        return getConfigFile().getLong(key, defaultValue);
    }

    public static Boolean getBoolean(String key) {
        return getConfigFile().getBoolean(key);
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        return getConfigFile().getBoolean(key, defaultValue);
    }

    public static boolean containsKey(String key) {
        return getConfigFile().containsKey(key);
    }
}


