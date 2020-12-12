package space.yizhu.kits;/* Created by yi on 11/30/2020.*/

import space.yizhu.bean.BaseModel;
import space.yizhu.bean.LogModel;
import space.yizhu.record.plugin.activerecord.ActiveRecordPlugin;
import space.yizhu.record.plugin.activerecord.CaseInsensitiveContainerFactory;
import space.yizhu.record.plugin.activerecord.Db;
import space.yizhu.record.plugin.activerecord.Model;
import space.yizhu.record.plugin.activerecord.dialect.*;
import space.yizhu.record.plugin.druid.DruidPlugin;
import space.yizhu.record.template.source.ClassPathSourceFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yi
 */
public class DbKit {
    private static boolean saveDblog = false;
    public static String logTableName = null;
    private static ActiveRecordPlugin activeRecordPlugin = null;
    private static HashMap<String, Class<? extends BaseModel<?>>> mappings = null;
    private static String logTableSq = "create table ${0}\n" +
            "(\n" +
            "    id          bigint auto_increment primary key comment '程序查询，更新时使用id',\n" +
            "    code        varchar(100)                         null comment '请求的方法',\n" +
            "    name        varchar(40)                         null comment '基本结构-$(表)注释和说明',\n" +
            "    creator     varchar(20)                         null comment '创建者code',\n" +
            "    mender      varchar(20)                         null comment '修改者code',\n" +
            "    create_time timestamp default CURRENT_TIMESTAMP null,\n" +
            "    modify_time timestamp default CURRENT_TIMESTAMP null,\n" +
            "    is_del      bit       default b'0'              null comment '是否被删除。1是0否',\n" +
            "    ext_i       int                                 null comment '扩展字段，存储状态或数字，最大2^32',\n" +
            "    ext_c       varchar(40)                         null comment '扩展字段，最多存储40字符长度',\n" +
            "    ext_j       json                                null comment '存储json字段',\n" +
            "    ext_t       text                                null comment '存储text字段',\n" +
"    type       tinyint       default 0               null comment '日志类型，默认0接口日志，1是数据库插入，2是数据更新，3数据库删除，数据库，旧数据是params，新数据是returned',"+
            "    heads       text                                null comment '参数头',\n" +
            "    params      text                                null comment '参数',\n" +
            "    returned      text                              null comment '返回数据',\n" +
            "    from_addr     varchar(50)                       null comment '来源地址',\n" +
            "    cost_time   int                                 null  comment '耗时ms'\n" +
            "\n" +
            ")";
    private static String logTableIndex = "create index ${0}_creator_index\n" +
            "    on ${0} (creator)";

    /**
     * @param url   jdbc:数据库别名://host:5434/db?currentSchema=db
     * @param uName 数据库账号
     * @param uPw   数据库密码
     */
    public static void init(String url, String uName, String uPw) {
        init(url, uName, uPw, null);
    }

    /**
     * @param url          jdbc:数据库别名://host:5434/db?currentSchema=db
     * @param uName        数据库账号
     * @param uPw          数据库密码
     * @param logTableName 日志表
     */
    public static void init(String url, String uName, String uPw, String logTableName) {
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
        if (null != mappings && mappings.size() > 0) {
            for (Map.Entry<String, Class<? extends BaseModel<?>>> enty : mappings.entrySet())
                if (null != enty.getKey() && null != enty.getValue())
                    activeRecordPlugin.addMapping(enty.getKey(), enty.getValue());
        }
        if (logTableName != null && logTableName.length() > 1) {
           DbKit. logTableName = logTableName;
            activeRecordPlugin.addMapping(logTableName, LogModel.class);
        }

        if (druidPlugin != null) {
            druidPlugin.start();
            activeRecordPlugin.start();
            SysKit.print("active[" + sqlName + "]数据库链接启动成功");
            if (null != logTableName && logTableName.length() > 2) {

                try {
                    Db.query("SELECT * FROM " + logTableName + " limit 1");
                } catch (Exception e) {
                    SysKit.print("日志表不存在，创建日志表");
                    Db.update(logTableSq.replace("${0}", logTableName));
                    SysKit.print("日志表" + logTableName + "创建完成");
                    Db.update(logTableIndex.replaceAll("\\$\\{0\\}", logTableName));
                    SysKit.print("日志表" + logTableName + "索引添加完成");

                }

            }

        }


    }

    //动态添加映射
    public static void addMapping(String table, Class<? extends BaseModel<?>> cls) {
        if (null == cls)
            return;
        if (null == activeRecordPlugin) {
            if (null == mappings) {
                mappings = new HashMap<>();
            }
            if (null != table)
                mappings.put(table, cls);
            return;
        }
        activeRecordPlugin.stop();
        activeRecordPlugin.addMapping(table, cls);
        activeRecordPlugin.start();
    }

    //动态添加映射
    public static void addMapping(Map<String, Class<? extends BaseModel<?>>> map) {
        if (map == null)
            return;
        if (null == activeRecordPlugin) {
            if (null == mappings) {
                mappings = new HashMap<>();
            }
            for (Map.Entry<String, Class<? extends BaseModel<?>>> cls : map.entrySet())
                if (null != cls && null != cls.getKey() && null != cls.getValue())
                    mappings.put(cls.getKey(), cls.getValue());
            return;
        }
        activeRecordPlugin.stop();
        for (Map.Entry<String, Class<? extends BaseModel<?>>> cls : map.entrySet())
            if (null != cls && null != cls.getKey() && null != cls.getValue())
                mappings.put(cls.getKey(), cls.getValue());

        activeRecordPlugin.start();
    }

    public static ActiveRecordPlugin getActiveRecord() {
        return activeRecordPlugin;
    }


    public
    static boolean isSaveDblog() {
        return saveDblog ;
    }

    public static void setSaveDblog(boolean saveDblog) {
        DbKit.saveDblog = saveDblog;
    }



    //----原


}
