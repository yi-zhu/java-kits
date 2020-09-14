/**
 * Copyright (c) 2011-2019, James Zhan 詹波 (jfinal@126.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.yizhu.record.plugin.activerecord;

import java.util.concurrent.ConcurrentHashMap;

import space.yizhu.record.core.Const;
import space.yizhu.kits.ConfigFile;

/**
 * 外部存取 sql 语句的工具类.<br>
 * 示例：<br>
 * Sqls.load("mySql.txt");<br>
 * String findBlogs = Sqls.get("findBlogs");<br>
 * Blog.dao.find(findBlogs);<br><br>
 *
 * Sqls.load("otherSql.txt");<br>
 * String findUsers = Sqls.get("othersqls.txt", "findUser");<br>
 * User.dao.find(findUsers);<br>
 */
@Deprecated
public class Sqls {

    private static ConfigFile configFile = null;
    private static final ConcurrentHashMap<String, ConfigFile> map = new ConcurrentHashMap<String, ConfigFile>();

    private Sqls() {
    }

    /**
     * 加载 sql 文件.
     * 最先被加载的 sql 文件将成为默认 sql 文件，并能够被 Sqls.get(String) 使用到
     * 第一次以后 load 后的 sql 文件会被 Sqls.get(String, String) 使用到
     */
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



