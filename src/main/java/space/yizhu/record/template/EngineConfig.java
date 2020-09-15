

package space.yizhu.record.template;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import space.yizhu.kits.StrKit;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.expr.ast.SharedMethodKit;
import space.yizhu.record.template.ext.directive.*;
import space.yizhu.record.template.ext.sharedmethod.SharedMethodLib;
import space.yizhu.record.template.io.EncoderFactory;
import space.yizhu.record.template.io.WriterBuffer;
import space.yizhu.record.template.source.FileSource;
import space.yizhu.record.template.source.FileSourceFactory;
import space.yizhu.record.template.source.ISource;
import space.yizhu.record.template.source.ISourceFactory;
import space.yizhu.record.template.source.StringSource;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.OutputDirectiveFactory;
import space.yizhu.record.template.stat.Parser;
import space.yizhu.record.template.stat.ast.Define;
import space.yizhu.record.template.stat.ast.Output;


public class EngineConfig {

    public static final String DEFAULT_ENCODING = "UTF-8";

    WriterBuffer writerBuffer = new WriterBuffer();

    private Map<String, Define> sharedFunctionMap = createSharedFunctionMap();        
    private List<ISource> sharedFunctionSourceList = new ArrayList<ISource>();        

    Map<String, Object> sharedObjectMap = null;

    private OutputDirectiveFactory outputDirectiveFactory = OutputDirectiveFactory.me;
    private ISourceFactory sourceFactory = new FileSourceFactory();
    private Map<String, Class<? extends Directive>> directiveMap = new HashMap<String, Class<? extends Directive>>(64, 0.5F);
    private SharedMethodKit sharedMethodKit = new SharedMethodKit();

    private boolean devMode = false;
    private boolean reloadModifiedSharedFunctionInDevMode = true;
    private String baseTemplatePath = null;
    private String encoding = DEFAULT_ENCODING;
    private String datePattern = "yyyy-MM-dd HH:mm";

    public EngineConfig() {
        
        addDirective("render", RenderDirective.class);
        addDirective("date", DateDirective.class);
        addDirective("escape", EscapeDirective.class);
        addDirective("string", StringDirective.class);
        addDirective("random", RandomDirective.class);
        addDirective("number", NumberDirective.class);
        addDirective("call", CallDirective.class);

        
        addSharedMethod(new SharedMethodLib());
    }

    
    public void addSharedFunction(String fileName) {
        fileName = fileName.replace("\\", "/");
        
        ISource source = sourceFactory.getSource(baseTemplatePath, fileName, encoding);
        doAddSharedFunction(source, fileName);
    }

    private synchronized void doAddSharedFunction(ISource source, String fileName) {
        Env env = new Env(this);
        new Parser(env, source.getContent(), fileName).parse();
        addToSharedFunctionMap(sharedFunctionMap, env);
        if (devMode) {
            sharedFunctionSourceList.add(source);
            env.addSource(source);
        }
    }

    
    public void addSharedFunction(String... fileNames) {
        for (String fileName : fileNames) {
            addSharedFunction(fileName);
        }
    }

    
    public void addSharedFunctionByString(String content) {
        
        
        StringSource stringSource = new StringSource(content, false);
        doAddSharedFunction(stringSource, null);
    }

    
    public void addSharedFunction(ISource source) {
        String fileName = source instanceof FileSource ? ((FileSource) source).getFileName() : null;
        doAddSharedFunction(source, fileName);
    }

    private void addToSharedFunctionMap(Map<String, Define> sharedFunctionMap, Env env) {
        Map<String, Define> funcMap = env.getFunctionMap();
        for (Entry<String, Define> e : funcMap.entrySet()) {
            if (sharedFunctionMap.containsKey(e.getKey())) {
                throw new IllegalArgumentException("Template function already exists : " + e.getKey());
            }
            Define func = e.getValue();
            if (devMode) {
                func.setEnvForDevMode(env);
            }
            sharedFunctionMap.put(e.getKey(), func);
        }
    }

    
    Define getSharedFunction(String functionName) {
        Define func = sharedFunctionMap.get(functionName);
        if (func == null) {
            
            return null;
        }

        if (devMode && reloadModifiedSharedFunctionInDevMode) {
            if (func.isSourceModifiedForDevMode()) {
                synchronized (this) {
                    func = sharedFunctionMap.get(functionName);
                    if (func.isSourceModifiedForDevMode()) {
                        reloadSharedFunctionSourceList();
                        func = sharedFunctionMap.get(functionName);
                    }
                }
            }
        }
        return func;
    }

    
    private synchronized void reloadSharedFunctionSourceList() {
        Map<String, Define> newMap = createSharedFunctionMap();
        for (int i = 0, size = sharedFunctionSourceList.size(); i < size; i++) {
            ISource source = sharedFunctionSourceList.get(i);
            String fileName = source instanceof FileSource ? ((FileSource) source).getFileName() : null;

            Env env = new Env(this);
            new Parser(env, source.getContent(), fileName).parse();
            addToSharedFunctionMap(newMap, env);
            if (devMode) {
                env.addSource(source);
            }
        }
        this.sharedFunctionMap = newMap;
    }

    private Map<String, Define> createSharedFunctionMap() {
        return new HashMap<String, Define>(512, 0.25F);
    }

    public synchronized void addSharedObject(String name, Object object) {
        if (sharedObjectMap == null) {
            sharedObjectMap = new HashMap<String, Object>(64, 0.25F);
        } else if (sharedObjectMap.containsKey(name)) {
            throw new IllegalArgumentException("Shared object already exists: " + name);
        }
        sharedObjectMap.put(name, object);
    }

    Map<String, Object> getSharedObjectMap() {
        return sharedObjectMap;
    }

    
    public void setOutputDirectiveFactory(OutputDirectiveFactory outputDirectiveFactory) {
        if (outputDirectiveFactory == null) {
            throw new IllegalArgumentException("outputDirectiveFactory can not be null");
        }
        this.outputDirectiveFactory = outputDirectiveFactory;
    }

    public Output getOutputDirective(ExprList exprList, Location location) {
        return outputDirectiveFactory.getOutputDirective(exprList, location);
    }

    
    void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public boolean isDevMode() {
        return devMode;
    }

    
    void setSourceFactory(ISourceFactory sourceFactory) {
        if (sourceFactory == null) {
            throw new IllegalArgumentException("sourceFactory can not be null");
        }
        this.sourceFactory = sourceFactory;
    }

    public ISourceFactory getSourceFactory() {
        return sourceFactory;
    }

    public void setBaseTemplatePath(String baseTemplatePath) {
        
        if (baseTemplatePath == null) {
            this.baseTemplatePath = null;
            return;
        }
        if (StrKit.isBlank(baseTemplatePath)) {
            throw new IllegalArgumentException("baseTemplatePath can not be blank");
        }
        baseTemplatePath = baseTemplatePath.trim();
        baseTemplatePath = baseTemplatePath.replace("\\", "/");
        if (baseTemplatePath.length() > 1) {
            if (baseTemplatePath.endsWith("/")) {
                baseTemplatePath = baseTemplatePath.substring(0, baseTemplatePath.length() - 1);
            }
        }
        this.baseTemplatePath = baseTemplatePath;
    }

    public String getBaseTemplatePath() {
        return baseTemplatePath;
    }

    public void setEncoding(String encoding) {
        if (StrKit.isBlank(encoding)) {
            throw new IllegalArgumentException("encoding can not be blank");
        }
        this.encoding = encoding;

        writerBuffer.setEncoding(encoding);        
    }

    public void setEncoderFactory(EncoderFactory encoderFactory) {
        writerBuffer.setEncoderFactory(encoderFactory);
        writerBuffer.setEncoding(encoding);        
    }

    public void setWriterBufferSize(int bufferSize) {
        writerBuffer.setBufferSize(bufferSize);
    }

    public String getEncoding() {
        return encoding;
    }

    public void setDatePattern(String datePattern) {
        if (StrKit.isBlank(datePattern)) {
            throw new IllegalArgumentException("datePattern can not be blank");
        }
        this.datePattern = datePattern;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setReloadModifiedSharedFunctionInDevMode(boolean reloadModifiedSharedFunctionInDevMode) {
        this.reloadModifiedSharedFunctionInDevMode = reloadModifiedSharedFunctionInDevMode;
    }

    @Deprecated
    public void addDirective(String directiveName, Directive directive) {
        addDirective(directiveName, directive.getClass());
    }

    public synchronized void addDirective(String directiveName, Class<? extends Directive> directiveClass) {
        if (StrKit.isBlank(directiveName)) {
            throw new IllegalArgumentException("directive name can not be blank");
        }
        if (directiveClass == null) {
            throw new IllegalArgumentException("directiveClass can not be null");
        }
        if (directiveMap.containsKey(directiveName)) {
            throw new IllegalArgumentException("directive already exists : " + directiveName);
        }
        directiveMap.put(directiveName, directiveClass);
    }

    public Class<? extends Directive> getDirective(String directiveName) {
        return directiveMap.get(directiveName);
    }

    public void removeDirective(String directiveName) {
        directiveMap.remove(directiveName);
    }

    
    public void addSharedMethod(Object sharedMethodFromObject) {
        sharedMethodKit.addSharedMethod(sharedMethodFromObject);
    }

    
    public void addSharedMethod(Class<?> sharedMethodFromClass) {
        sharedMethodKit.addSharedMethod(sharedMethodFromClass);
    }

    
    public void addSharedStaticMethod(Class<?> sharedStaticMethodFromClass) {
        sharedMethodKit.addSharedStaticMethod(sharedStaticMethodFromClass);
    }

    
    public void removeSharedMethod(String methodName) {
        sharedMethodKit.removeSharedMethod(methodName);
    }

    
    public void removeSharedMethod(Class<?> sharedClass) {
        sharedMethodKit.removeSharedMethod(sharedClass);
    }

    
    public void removeSharedMethod(Method method) {
        sharedMethodKit.removeSharedMethod(method);
    }

    public SharedMethodKit getSharedMethodKit() {
        return sharedMethodKit;
    }
}





