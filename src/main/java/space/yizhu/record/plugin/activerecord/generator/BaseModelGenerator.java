

package space.yizhu.record.plugin.activerecord.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import space.yizhu.kits.JavaKeyword;
import space.yizhu.kits.Kv;
import space.yizhu.kits.StrKit;
import space.yizhu.record.template.Engine;


public class BaseModelGenerator {

    protected Engine engine;
    protected String template = "/space/yizhu/record/plugin/activerecord/generator/base_model_template.jf";

    protected String baseModelPackageName;
    protected String baseModelOutputDir;
    protected boolean generateChainSetter = false;

    protected JavaKeyword javaKeyword = JavaKeyword.me;

    
    @SuppressWarnings("serial")
    protected Map<String, String> getterTypeMap = new HashMap<String, String>() {{
        put("java.lang.String", "getStr");
        put("java.lang.Integer", "getInt");
        put("java.lang.Long", "getLong");
        put("java.lang.Double", "getDouble");
        put("java.lang.Float", "getFloat");
        put("java.lang.Short", "getShort");
        put("java.lang.Byte", "getByte");
    }};

    public BaseModelGenerator(String baseModelPackageName, String baseModelOutputDir) {
        if (StrKit.isBlank(baseModelPackageName)) {
            throw new IllegalArgumentException("baseModelPackageName can not be blank.");
        }
        if (baseModelPackageName.contains("/") || baseModelPackageName.contains("\\")) {
            throw new IllegalArgumentException("baseModelPackageName error : " + baseModelPackageName);
        }
        if (StrKit.isBlank(baseModelOutputDir)) {
            throw new IllegalArgumentException("baseModelOutputDir can not be blank.");
        }

        this.baseModelPackageName = baseModelPackageName;
        this.baseModelOutputDir = baseModelOutputDir;

        initEngine();
    }

    protected void initEngine() {
        engine = new Engine();
        engine.setToClassPathSourceFactory();    
        engine.addSharedMethod(new StrKit());
        engine.addSharedObject("getterTypeMap", getterTypeMap);
        engine.addSharedObject("javaKeyword", javaKeyword);
    }

    
    public void setTemplate(String template) {
        this.template = template;
    }

    public void setGenerateChainSetter(boolean generateChainSetter) {
        this.generateChainSetter = generateChainSetter;
    }

    public void generate(List<TableMeta> tableMetas) {
        System.out.println("Generate base model ...");
        System.out.println("Base Model Output Dir: " + baseModelOutputDir);

        for (TableMeta tableMeta : tableMetas) {
            genBaseModelContent(tableMeta);
        }
        writeToFile(tableMetas);
    }

    protected void genBaseModelContent(TableMeta tableMeta) {
        Kv data = Kv.by("baseModelPackageName", baseModelPackageName);
        data.set("generateChainSetter", generateChainSetter);
        data.set("tableMeta", tableMeta);

        tableMeta.baseModelContent = engine.getTemplate(template).renderToString(data);
    }

    protected void writeToFile(List<TableMeta> tableMetas) {
        try {
            for (TableMeta tableMeta : tableMetas) {
                writeToFile(tableMeta);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    
    protected void writeToFile(TableMeta tableMeta) throws IOException {
        File dir = new File(baseModelOutputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String target = baseModelOutputDir + File.separator + tableMeta.baseModelName + ".java";
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(target), "UTF-8");
            osw.write(tableMeta.baseModelContent);
        } finally {
            if (osw != null) {
                osw.close();
            }
        }
    }

    public String getBaseModelPackageName() {
        return baseModelPackageName;
    }

    public String getBaseModelOutputDir() {
        return baseModelOutputDir;
    }
}






