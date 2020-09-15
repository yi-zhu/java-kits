

package space.yizhu.record.plugin.activerecord.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import space.yizhu.kits.StrKit;
import space.yizhu.record.plugin.activerecord.SqlPara;
import space.yizhu.record.template.Engine;
import space.yizhu.record.template.Template;
import space.yizhu.record.template.source.ISource;


@SuppressWarnings({"unchecked", "rawtypes"})
public class SqlKit {

    static final String SQL_TEMPLATE_MAP_KEY = "_SQL_TEMPLATE_MAP_";
    static final String SQL_PARA_KEY = "_SQL_PARA_";
    static final String PARA_ARRAY_KEY = "_PARA_ARRAY_"; 

    private String configName;
    private boolean devMode;
    private Engine engine;
    private List<SqlSource> sqlSourceList = new ArrayList<SqlSource>();
    private Map<String, Template> sqlTemplateMap;

    public SqlKit(String configName, boolean devMode) {
        this.configName = configName;
        this.devMode = devMode;

        engine = new Engine(configName);
        engine.setDevMode(devMode);
        engine.setToClassPathSourceFactory();

        engine.addDirective("namespace", NameSpaceDirective.class);
        engine.addDirective("sql", SqlDirective.class);
        engine.addDirective("para", ParaDirective.class);
        engine.addDirective("p", ParaDirective.class);        
    }

    public SqlKit(String configName) {
        this(configName, false);
    }

    public Engine getEngine() {
        return engine;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
        engine.setDevMode(devMode);
    }

    public void setBaseSqlTemplatePath(String baseSqlTemplatePath) {
        engine.setBaseTemplatePath(baseSqlTemplatePath);
    }

    public void addSqlTemplate(String sqlTemplate) {
        if (StrKit.isBlank(sqlTemplate)) {
            throw new IllegalArgumentException("sqlTemplate can not be blank");
        }
        sqlSourceList.add(new SqlSource(sqlTemplate));
    }

    public void addSqlTemplate(ISource sqlTemplate) {
        if (sqlTemplate == null) {
            throw new IllegalArgumentException("sqlTemplate can not be null");
        }
        sqlSourceList.add(new SqlSource(sqlTemplate));
    }

    public synchronized void parseSqlTemplate() {
        Map<String, Template> sqlTemplateMap = new HashMap<String, Template>(512, 0.5F);
        for (SqlSource ss : sqlSourceList) {
            Template template = ss.isFile() ? engine.getTemplate(ss.file) : engine.getTemplate(ss.source);
            Map<Object, Object> data = new HashMap<Object, Object>();
            data.put(SQL_TEMPLATE_MAP_KEY, sqlTemplateMap);
            template.renderToString(data);
        }
        this.sqlTemplateMap = sqlTemplateMap;
    }

    private void reloadModifiedSqlTemplate() {
        engine.removeAllTemplateCache();    
        parseSqlTemplate();
    }

    private boolean isSqlTemplateModified() {
        for (Template template : sqlTemplateMap.values()) {
            if (template.isModified()) {
                return true;
            }
        }
        return false;
    }

    private Template getSqlTemplate(String key) {
        Template template = sqlTemplateMap.get(key);
        if (template == null) {    
            if (!devMode) {
                return null;
            }
            if (isSqlTemplateModified()) {
                synchronized (this) {
                    if (isSqlTemplateModified()) {
                        reloadModifiedSqlTemplate();
                        template = sqlTemplateMap.get(key);
                    }
                }
            }
            return template;
        }

        if (devMode && template.isModified()) {
            synchronized (this) {
                template = sqlTemplateMap.get(key);
                if (template.isModified()) {
                    reloadModifiedSqlTemplate();
                    template = sqlTemplateMap.get(key);
                }
            }
        }
        return template;
    }

    public String getSql(String key) {
        Template template = getSqlTemplate(key);
        return template != null ? template.renderToString(null) : null;
    }

    
    public SqlPara getSqlPara(String key, Map data) {
        Template template = getSqlTemplate(key);
        if (template == null) {
            return null;
        }

        SqlPara sqlPara = new SqlPara();
        data.put(SQL_PARA_KEY, sqlPara);
        sqlPara.setSql(template.renderToString(data));
        data.remove(SQL_PARA_KEY);    
        return sqlPara;
    }

    
    public SqlPara getSqlPara(String key, Object... paras) {
        Template template = getSqlTemplate(key);
        if (template == null) {
            return null;
        }

        SqlPara sqlPara = new SqlPara();
        Map data = new HashMap();
        data.put(SQL_PARA_KEY, sqlPara);
        data.put(PARA_ARRAY_KEY, paras);
        sqlPara.setSql(template.renderToString(data));
        
        return sqlPara;
    }

    public java.util.Set<java.util.Map.Entry<String, Template>> getSqlMapEntrySet() {
        return sqlTemplateMap.entrySet();
    }

    public String toString() {
        return "SqlKit for config : " + configName;
    }

    

    
    public SqlPara getSqlParaByString(String content, Map data) {
        Template template = engine.getTemplateByString(content);

        SqlPara sqlPara = new SqlPara();
        data.put(SQL_PARA_KEY, sqlPara);
        sqlPara.setSql(template.renderToString(data));
        data.remove(SQL_PARA_KEY);    
        return sqlPara;
    }

    
    public SqlPara getSqlParaByString(String content, Object... paras) {
        Template template = engine.getTemplateByString(content);

        SqlPara sqlPara = new SqlPara();
        Map data = new HashMap();
        data.put(SQL_PARA_KEY, sqlPara);
        data.put(PARA_ARRAY_KEY, paras);
        sqlPara.setSql(template.renderToString(data));
        
        return sqlPara;
    }
}




