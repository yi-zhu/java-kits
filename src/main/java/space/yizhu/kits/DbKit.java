package space.yizhu.kits;/* Created by yi on 11/30/2020.*/

import space.yizhu.bean.BaseModel;
import space.yizhu.record.plugin.activerecord.ActiveRecordPlugin;
import space.yizhu.record.plugin.activerecord.CaseInsensitiveContainerFactory;
import space.yizhu.record.plugin.activerecord.Model;
import space.yizhu.record.plugin.activerecord.dialect.*;
import space.yizhu.record.plugin.druid.DruidPlugin;
import space.yizhu.record.template.source.ClassPathSourceFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yi
 */
public class DbKit {

    private static ActiveRecordPlugin activeRecordPlugin = null;
    private static HashMap<String, Class<? extends Model<?>>> mappings = null;


    /**
     * @param url   jdbc:数据库别名://host:5434/db?currentSchema=db
     * @param uName 数据库账号
     * @param uPw   数据库密码
     */
    public static void init(String url, String uName, String uPw) {
        // 配置C3p0数据库连接池插件
        DruidPlugin druidPlugin = null;

        String sqlName = url.substring(0, url.indexOf("://"));

        try {
            druidPlugin = new DruidPlugin(url, uName, uPw);
        } catch (Exception e) {
            if (null != e.getMessage())
                SysKit.print(e, "数据库初始化失败");
        }

        activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
        activeRecordPlugin.getEngine().setSourceFactory(new ClassPathSourceFactory());
        if (sqlName.toLowerCase().contains("mysql"))
            activeRecordPlugin.setDialect(new MysqlDialect());
        else if (sqlName.toLowerCase().contains("sqlse"))
            activeRecordPlugin.setDialect(new SqlServerDialect());
        else if (sqlName.toLowerCase().contains("oracle"))
            activeRecordPlugin.setDialect(new OracleDialect());
        else if (sqlName.toLowerCase().contains("ansi"))
            activeRecordPlugin.setDialect(new AnsiSqlDialect());
        else if (sqlName.toLowerCase().contains("post"))
            activeRecordPlugin.setDialect(new PostgreSqlDialect());
        else if (sqlName.toLowerCase().contains("sqlite") || sqlName.toLowerCase().contains("sqllite"))
            //设置方言
            activeRecordPlugin.setDialect(new Sqlite3Dialect());
        //忽略大小写
        activeRecordPlugin.setContainerFactory(new CaseInsensitiveContainerFactory(true));

        for (Map.Entry<String, BaseModel<?>> set : BeanKit.beanMap.entrySet()) {
            if (null != set.getValue())
                activeRecordPlugin.addMapping(set.getKey(), (Class<? extends Model<?>>) set.getValue().getClass());
        }
        if (null!=mappings&&mappings.size()>0){
            for (Map.Entry<String, Class<? extends Model<?>>> enty:mappings.entrySet())
                if (null!=enty.getKey()&&null!= enty.getValue())
                activeRecordPlugin.addMapping(enty.getKey(), enty.getValue());
        }

        if (druidPlugin != null) {
            druidPlugin.start();
            activeRecordPlugin.start();
            SysKit.print("active[" + sqlName + "]数据库链接启动成功");

        }


    }

    //动态添加映射
    public static void addMapping(String table, Class<? extends Model<?>> cls) {
        if (null == cls)
            return;
        if (null == activeRecordPlugin) {
            if (null == mappings) {
                mappings = new HashMap<>();
            }
            if (null != table)
                mappings.put(table, cls);
        }
        activeRecordPlugin.stop();
        activeRecordPlugin.addMapping(table, cls);
        activeRecordPlugin.start();
    }

    //动态添加映射
    public static void addMapping(String table, List<Class<? extends Model<?>>> clss) {
        if (clss == null)
            return;
        if (null == activeRecordPlugin) {
            if (null == mappings) {
                mappings = new HashMap<>();
            }
            for (Class<? extends Model<?>> cls : clss)
                if (null != cls && null != table)
                    mappings.put(table, cls);
        }
        activeRecordPlugin.stop();
        for (Class<? extends Model<?>> cls : clss)
            if (null != cls && null != table)
                activeRecordPlugin.addMapping(table, cls);
        activeRecordPlugin.start();
    }

    public static ActiveRecordPlugin getActiveRecord() {
        return activeRecordPlugin;
    }

}
