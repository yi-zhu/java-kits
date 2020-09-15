

package space.yizhu.record.plugin.activerecord.generator;

import java.util.List;
import javax.sql.DataSource;

import space.yizhu.record.plugin.activerecord.dialect.Dialect;


public class Generator {

    protected Dialect dialect = null;
    protected MetaBuilder metaBuilder;
    protected BaseModelGenerator baseModelGenerator;
    protected ModelGenerator modelGenerator;
    protected MappingKitGenerator mappingKitGenerator;
    protected DataDictionaryGenerator dataDictionaryGenerator;
    protected boolean generateDataDictionary = false;

    
    public Generator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir, String modelPackageName, String modelOutputDir) {
        this(dataSource, new BaseModelGenerator(baseModelPackageName, baseModelOutputDir), new ModelGenerator(modelPackageName, baseModelPackageName, modelOutputDir));
    }

    
    public Generator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir) {
        this(dataSource, new BaseModelGenerator(baseModelPackageName, baseModelOutputDir));
    }

    public Generator(DataSource dataSource, BaseModelGenerator baseModelGenerator) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource can not be null.");
        }
        if (baseModelGenerator == null) {
            throw new IllegalArgumentException("baseModelGenerator can not be null.");
        }

        this.metaBuilder = new MetaBuilder(dataSource);
        this.baseModelGenerator = baseModelGenerator;
        this.modelGenerator = null;
        this.mappingKitGenerator = null;
        this.dataDictionaryGenerator = null;
    }

    
    public Generator(DataSource dataSource, BaseModelGenerator baseModelGenerator, ModelGenerator modelGenerator) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource can not be null.");
        }
        if (baseModelGenerator == null) {
            throw new IllegalArgumentException("baseModelGenerator can not be null.");
        }
        if (modelGenerator == null) {
            throw new IllegalArgumentException("modelGenerator can not be null.");
        }

        this.metaBuilder = new MetaBuilder(dataSource);
        this.baseModelGenerator = baseModelGenerator;
        this.modelGenerator = modelGenerator;
        this.mappingKitGenerator = new MappingKitGenerator(modelGenerator.modelPackageName, modelGenerator.modelOutputDir);
        this.dataDictionaryGenerator = new DataDictionaryGenerator(dataSource, modelGenerator.modelOutputDir);
    }

    
    public void setMetaBuilder(MetaBuilder metaBuilder) {
        if (metaBuilder != null) {
            this.metaBuilder = metaBuilder;
        }
    }

    
    public void setGenerateRemarks(boolean generateRemarks) {
        if (metaBuilder != null) {
            metaBuilder.setGenerateRemarks(generateRemarks);
        }
    }

    public void setTypeMapping(TypeMapping typeMapping) {
        this.metaBuilder.setTypeMapping(typeMapping);
    }

    
    public void setMappingKitGenerator(MappingKitGenerator mappingKitGenerator) {
        if (mappingKitGenerator != null) {
            this.mappingKitGenerator = mappingKitGenerator;
        }
    }

    
    public void setDataDictionaryGenerator(DataDictionaryGenerator dataDictionaryGenerator) {
        if (dataDictionaryGenerator != null) {
            this.dataDictionaryGenerator = dataDictionaryGenerator;
        }
    }

    
    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    
    public void setBaseModelTemplate(String baseModelTemplate) {
        baseModelGenerator.setTemplate(baseModelTemplate);
    }

    
    public void setGenerateChainSetter(boolean generateChainSetter) {
        baseModelGenerator.setGenerateChainSetter(generateChainSetter);
    }

    
    public void setRemovedTableNamePrefixes(String... removedTableNamePrefixes) {
        metaBuilder.setRemovedTableNamePrefixes(removedTableNamePrefixes);
    }

    
    public void addExcludedTable(String... excludedTables) {
        metaBuilder.addExcludedTable(excludedTables);
    }

    
    public void setModelTemplate(String modelTemplate) {
        if (modelGenerator != null) {
            modelGenerator.setTemplate(modelTemplate);
        }
    }

    
    public void setGenerateDaoInModel(boolean generateDaoInModel) {
        if (modelGenerator != null) {
            modelGenerator.setGenerateDaoInModel(generateDaoInModel);
        }
    }

    
    public void setGenerateDataDictionary(boolean generateDataDictionary) {
        this.generateDataDictionary = generateDataDictionary;
    }

    
    public void setMappingKitTemplate(String mappingKitTemplate) {
        if (this.mappingKitGenerator != null) {
            this.mappingKitGenerator.setTemplate(mappingKitTemplate);
        }
    }

    
    public void setMappingKitOutputDir(String mappingKitOutputDir) {
        if (this.mappingKitGenerator != null) {
            this.mappingKitGenerator.setMappingKitOutputDir(mappingKitOutputDir);
        }
    }

    
    public void setMappingKitPackageName(String mappingKitPackageName) {
        if (this.mappingKitGenerator != null) {
            this.mappingKitGenerator.setMappingKitPackageName(mappingKitPackageName);
        }
    }

    
    public void setMappingKitClassName(String mappingKitClassName) {
        if (this.mappingKitGenerator != null) {
            this.mappingKitGenerator.setMappingKitClassName(mappingKitClassName);
        }
    }

    
    public void setDataDictionaryOutputDir(String dataDictionaryOutputDir) {
        if (this.dataDictionaryGenerator != null) {
            this.dataDictionaryGenerator.setDataDictionaryOutputDir(dataDictionaryOutputDir);
        }
    }

    
    public void setDataDictionaryFileName(String dataDictionaryFileName) {
        if (dataDictionaryGenerator != null) {
            dataDictionaryGenerator.setDataDictionaryFileName(dataDictionaryFileName);
        }
    }

    public void generate() {
        if (dialect != null) {
            metaBuilder.setDialect(dialect);
        }

        long start = System.currentTimeMillis();
        List<TableMeta> tableMetas = metaBuilder.build();
        if (tableMetas.size() == 0) {
            System.out.println("TableMeta 数量为 0，不生成任何文件");
            return;
        }

        baseModelGenerator.generate(tableMetas);

        if (modelGenerator != null) {
            modelGenerator.generate(tableMetas);
        }

        if (mappingKitGenerator != null) {
            mappingKitGenerator.generate(tableMetas);
        }

        if (dataDictionaryGenerator != null && generateDataDictionary) {
            dataDictionaryGenerator.generate(tableMetas);
        }

        long usedTime = (System.currentTimeMillis() - start) / 1000;
        System.out.println("Generate complete in " + usedTime + " seconds.");
    }
}



