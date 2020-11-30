

package space.yizhu.kits;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;
import space.yizhu.record.core.Const;

/**
 * ConfigFile. ConfigFile can load properties file from CLASSPATH or File object.
 */
public class ConfigFile {

    protected Properties properties;
    private boolean isYml = false;

    /**
     * 支持 new ConfigFile().appendIfExists(...)
     */
    public ConfigFile() {
        properties = new Properties();
    }

    /**
     * ConfigFile constructor.
     * @see #ConfigFile(String, String)
     */
    public ConfigFile(String fileName) {
        this(fileName, Const.DEFAULT_ENCODING);
    }

    /**
     * ConfigFile constructor
     * <p>
     * Example:<br>
     * ConfigFile ConfigFile = new ConfigFile("my_config.txt", "UTF-8");<br>
     * String userName = ConfigFile.get("userName");<br><br>
     *
     * ConfigFile = new ConfigFile("file_in_sub_path_of_classpath.txt", "UTF-8");<br>
     * String value = ConfigFile.get("key");
     *
     * @param fileName the properties file's name in classpath or the sub directory of classpath
     * @param encoding the encoding
     */
    public ConfigFile(String fileName, String encoding) {
        InputStream inputStream = null;
        try {
            inputStream = getClassLoader().getResourceAsStream(fileName);        // properties.load(ConfigFile.class.getResourceAsStream(fileName));
            if (inputStream == null) {
                throw new IllegalArgumentException("Properties file not found in classpath: " + fileName);
            }

            properties = new Properties();
            if (fileName.endsWith("yml")){
                isYml = true;
                properties=  new Yaml().loadAs(new InputStreamReader(inputStream, encoding),Properties.class);
            }else
            properties.load(new InputStreamReader(inputStream, encoding));
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file.", e);
        } finally {
            if (inputStream != null) try {
                inputStream.close();
            } catch (IOException e) {
                LogKit.error(e.getMessage(), e);
            }
        }
    }

    private ClassLoader getClassLoader() {
        ClassLoader ret = Thread.currentThread().getContextClassLoader();
        return ret != null ? ret : getClass().getClassLoader();
    }

    /**
     * ConfigFile constructor.
     * @see #ConfigFile(File, String)
     */
    public ConfigFile(File file) {
        this(file, Const.DEFAULT_ENCODING);
    }

    /**
     * ConfigFile constructor
     * <p>
     * Example:<br>
     * ConfigFile ConfigFile = new ConfigFile(new File("/var/config/my_config.txt"), "UTF-8");<br>
     * String userName = ConfigFile.get("userName");
     *
     * @param file the properties File object
     * @param encoding the encoding
     */
    public ConfigFile(File file, String encoding) {
        if (file == null) {
            throw new IllegalArgumentException("File can not be null.");
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("File not found : " + file.getName());
        }

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            properties = new Properties();
            if (file.getName().endsWith("yml")){
                isYml = true;
                properties=  new Yaml().loadAs(new InputStreamReader(inputStream, encoding),Properties.class);
            }else
            properties.load(new InputStreamReader(inputStream, encoding));
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file.", e);
        } finally {
            if (inputStream != null) try {
                inputStream.close();
            } catch (IOException e) {
                LogKit.error(e.getMessage(), e);
            }
        }
    }

    public ConfigFile append(ConfigFile configFile) {
        if (configFile == null) {
            throw new IllegalArgumentException("ConfigFile can not be null");
        }
        properties.putAll(configFile.getProperties());
        return this;
    }

    public ConfigFile append(String fileName, String encoding) {
        return append(new ConfigFile(fileName, encoding));
    }

    public ConfigFile append(String fileName) {
        return append(fileName, Const.DEFAULT_ENCODING);
    }

    public ConfigFile appendIfExists(String fileName, String encoding) {
        try {
            return append(new ConfigFile(fileName, encoding));
        } catch (Exception e) {
            return this;
        }
    }

    public ConfigFile appendIfExists(String fileName) {
        return appendIfExists(fileName, Const.DEFAULT_ENCODING);
    }

    public ConfigFile append(File file, String encoding) {
        return append(new ConfigFile(file, encoding));
    }

    public ConfigFile append(File file) {
        return append(file, Const.DEFAULT_ENCODING);
    }

    public ConfigFile appendIfExists(File file, String encoding) {
        if (file.isFile()) {
            append(new ConfigFile(file, encoding));
        }
        return this;
    }

    public ConfigFile appendIfExists(File file) {
        return appendIfExists(file, Const.DEFAULT_ENCODING);
    }

    public String get(String key) {
        return getV(key,null)==null?null: String.valueOf(getV(key,null));
    }

    public Object getObj(String key) {

        return getV(key,null);
    }

    private Object getV(String key,String def){
        Object ret=def;
        if (isYml){
            if (null!=properties.get(key)){
                return properties.get(key).toString();
            }else {
                String[] strs=key.split("\\.");
                Object temp=null;
                String str;
                for (int i = 0; i < strs.length; i++) {
                    str = strs[i];
                    if (str.length()<1) {
                        continue;
                    }
                    if (i==0){
                        temp = properties.get(str);
                    }else if (i==strs.length-1){
                        if (temp instanceof Map){
                            if (null!=((Map) temp).get(str)) {
                                ret = ((Map) temp).get(str);
                            }
                        }
                    }else if (null!=temp){
                        if (temp instanceof Map){
                            if (null!=((Map) temp).get(str)) {
                                temp = ((Map) temp).get(str);
                            }
                        }
                    }
                }
            }

        }else {
            ret= properties.getProperty(key);
        }
        return ret;
    }


    public String get(String key, String defaultValue) {
        return getV(key,defaultValue)==null?defaultValue: String.valueOf(getV(key,defaultValue));

    }

    public Integer getInt(String key) {
        return getInt(key, null);
    }

    public Integer getInt(String key, Integer defaultValue) {
        String value = get(key);
        if (value != null) {
            return Integer.parseInt(value.trim());
        }
        return defaultValue;
    }

    public Long getLong(String key) {
        return getLong(key, null);
    }

    public Long getLong(String key, Long defaultValue) {
        String value = get(key);
        if (value != null) {
            return Long.parseLong(value.trim());
        }
        return defaultValue;
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        String value = get(key);
        if (value != null) {
            value = value.toLowerCase().trim();
            if ("true".equals(value)) {
                return true;
            } else if ("false".equals(value)) {
                return false;
            }
            throw new RuntimeException("The value can not parse to Boolean : " + value);
        }
        return defaultValue;
    }

    public boolean containsKey(String key) {
        if (isYml){
            if (null!=properties.get(key)){
                return true;
            }else {
                Object ret=null;
                String[] strs=key.split("\\.");
                Object temp=null;
                String str;
                for (int i = 0; i < strs.length; i++) {
                    str = strs[i];
                    if (str.length()<1) {
                        continue;
                    }
                    if (i==0){
                        temp = properties.get(str);
                    }else if (i==strs.length-1){
                        if (temp instanceof Map){
                            if (null!=((Map) temp).get(str)) {
                                ret = ((Map) temp).get(str);
                            }
                        }
                    }else if (null!=temp){
                        if (temp instanceof Map){
                            if (null!=((Map) temp).get(str)) {
                                temp = ((Map) temp).get(str);
                            }
                        }
                    }
                }
                return null!=ret;
            }

        }
        return properties.containsKey(key);
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public boolean notEmpty() {
        return !properties.isEmpty();
    }

    public Properties getProperties() {
        return properties;
    }
}
