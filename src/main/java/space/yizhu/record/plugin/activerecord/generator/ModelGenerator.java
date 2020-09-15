

package space.yizhu.record.plugin.activerecord.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import space.yizhu.kits.Kv;
import space.yizhu.kits.StrKit;
import space.yizhu.record.template.Engine;


public class ModelGenerator {

    protected Engine engine;
    protected String template = "/space/yizhu/record/plugin/activerecord/generator/model_template.jf";

    protected String modelPackageName;
    protected String baseModelPackageName;
    protected String modelOutputDir;
    protected boolean generateDaoInModel = false;

    public ModelGenerator(String modelPackageName, String baseModelPackageName, String modelOutputDir) {
        if (StrKit.isBlank(modelPackageName)) {
            throw new IllegalArgumentException("modelPackageName can not be blank.");
        }
        if (modelPackageName.contains("/") || modelPackageName.contains("\\")) {
            throw new IllegalArgumentException("modelPackageName error : " + modelPackageName);
        }
        if (StrKit.isBlank(baseModelPackageName)) {
            throw new IllegalArgumentException("baseModelPackageName can not be blank.");
        }
        if (baseModelPackageName.contains("/") || baseModelPackageName.contains("\\")) {
            throw new IllegalArgumentException("baseModelPackageName error : " + baseModelPackageName);
        }
        if (StrKit.isBlank(modelOutputDir)) {
            throw new IllegalArgumentException("modelOutputDir can not be blank.");
        }

        this.modelPackageName = modelPackageName;
        this.baseModelPackageName = baseModelPackageName;
        this.modelOutputDir = modelOutputDir;

        initEngine();
    }

    protected void initEngine() {
        engine = new Engine();
        engine.setToClassPathSourceFactory();
        engine.addSharedMethod(new StrKit());
    }

    
    public void setTemplate(String template) {
        this.template = template;
    }

    public void setGenerateDaoInModel(boolean generateDaoInModel) {
        this.generateDaoInModel = generateDaoInModel;
    }

    public void generate(List<TableMeta> tableMetas) {
        System.out.println("Generate model ...");
        System.out.println("Model Output Dir: " + modelOutputDir);

        for (TableMeta tableMeta : tableMetas) {
            genModelContent(tableMeta);
        }
        writeToFile(tableMetas);
    }

    protected void genModelContent(TableMeta tableMeta) {
        Kv data = Kv.by("modelPackageName", modelPackageName);
        data.set("baseModelPackageName", baseModelPackageName);
        data.set("generateDaoInModel", generateDaoInModel);
        data.set("tableMeta", tableMeta);

        String ret = engine.getTemplate(template).renderToString(data);
        tableMeta.modelContent = ret;
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
        File dir = new File(modelOutputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String target = modelOutputDir + File.separator + tableMeta.modelName + ".java";

        File file = new File(target);
        if (file.exists()) {
            return;    
        }

        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            osw.write(tableMeta.modelContent);
        } finally {
            if (osw != null) {
                osw.close();
            }
        }
    }

    public String getModelPackageName() {
        return modelPackageName;
    }

    public String getBaseModelPackageName() {
        return baseModelPackageName;
    }

    public String getModelOutputDir() {
        return modelOutputDir;
    }
}


