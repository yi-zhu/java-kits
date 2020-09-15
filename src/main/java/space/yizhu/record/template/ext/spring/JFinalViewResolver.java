

package space.yizhu.record.template.ext.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletContext;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import space.yizhu.kits.StrKit;
import space.yizhu.record.template.Directive;
import space.yizhu.record.template.Engine;
import space.yizhu.record.template.source.ClassPathSourceFactory;
import space.yizhu.record.template.source.ISourceFactory;


public class JFinalViewResolver extends AbstractTemplateViewResolver {

    public static final Engine engine = new Engine();

    static List<String> sharedFunctionFiles = new ArrayList<String>();
    static boolean sessionInView = false;
    static boolean createSession = true;

    private static JFinalViewResolver me = null;

    
    public static JFinalViewResolver me() {
        return me;
    }

    public Engine getEngine() {
        return engine;
    }

    
    public void setDevMode(boolean devMode) {
        engine.setDevMode(devMode);
    }

    
    public void setSharedFunction(String sharedFunctionFiles) {
        if (StrKit.isBlank(sharedFunctionFiles)) {
            throw new IllegalArgumentException("sharedFunctionFiles can not be blank");
        }

        String[] fileArray = sharedFunctionFiles.split(",");
        for (String fileName : fileArray) {
            JFinalViewResolver.sharedFunctionFiles.add(fileName);
        }
    }

    
    public void setSharedFunctionList(List<String> sharedFunctionList) {
        if (sharedFunctionList != null) {
            JFinalViewResolver.sharedFunctionFiles.addAll(sharedFunctionList);
        }
    }

    
    public void addSharedFunction(String fileName) {
        
        sharedFunctionFiles.add(fileName);
    }

    
    public void addDirective(String directiveName, Class<? extends Directive> directiveClass) {
        engine.addDirective(directiveName, directiveClass);
    }

    
    @Deprecated
    public void addDirective(String directiveName, Directive directive) {
        addDirective(directiveName, directive.getClass());
    }

    
    public void addSharedObject(String name, Object object) {
        engine.addSharedObject(name, object);
    }

    
    public void addSharedMethod(Object sharedMethodFromObject) {
        engine.addSharedMethod(sharedMethodFromObject);
    }

    
    public void addSharedMethod(Class<?> sharedMethodFromClass) {
        engine.addSharedMethod(sharedMethodFromClass);
    }

    
    public static void addExtensionMethod(Class<?> targetClass, Object objectOfExtensionClass) {
        Engine.addExtensionMethod(targetClass, objectOfExtensionClass);
    }

    
    public static void addExtensionMethod(Class<?> targetClass, Class<?> extensionClass) {
        Engine.addExtensionMethod(targetClass, extensionClass);
    }

    
    public void setSourceFactory(ISourceFactory sourceFactory) {
        if (sourceFactory instanceof ClassPathSourceFactory) {
            engine.setBaseTemplatePath(null);
        }
        engine.setSourceFactory(sourceFactory);
    }

    
    public void setBaseTemplatePath(String baseTemplatePath) {
        engine.setBaseTemplatePath(baseTemplatePath);
    }

    
    public void setSessionInView(boolean sessionInView) {
        JFinalViewResolver.sessionInView = sessionInView;
    }

    
    public void setCreateSession(boolean createSession) {
        JFinalViewResolver.createSession = createSession;
    }

    
    public void setEncoding(String encoding) {
        engine.setEncoding(encoding);
    }

    
    public void setDatePattern(String datePattern) {
        engine.setDatePattern(datePattern);
    }

    

    public JFinalViewResolver() {
        synchronized (JFinalViewResolver.class) {
            if (me == null) {
                me = this;
            }
        }

        setViewClass(requiredViewClass());
        setOrder(0);
        setContentType("text/html;charset=UTF-8");
        
        
    }

    @Override
    protected Class<?> requiredViewClass() {
        return JFinalView.class;
    }

    
    protected View loadView(String viewName, Locale locale) throws Exception {
        String suffix = getSuffix();
        if (".jsp".equals(suffix) || ".ftl".equals(suffix) || ".vm".equals(suffix)) {
            return null;
        } else {
            return super.loadView(viewName, locale);
        }
    }

    
    @Override
    protected void initServletContext(ServletContext servletContext) {
        super.initServletContext(servletContext);
        super.setExposeRequestAttributes(true);

        initBaseTemplatePath(servletContext);
        initSharedFunction();
    }

    
    private void initBaseTemplatePath(ServletContext servletContext) {
        if (engine.getSourceFactory() instanceof ClassPathSourceFactory) {
            
        } else {
            if (StrKit.isBlank(engine.getBaseTemplatePath())) {
                String path = servletContext.getRealPath("/");
                engine.setBaseTemplatePath(path);
            }
        }
    }

    
    private void initSharedFunction() {
        for (String file : sharedFunctionFiles) {
            engine.addSharedFunction(file.trim());
        }
    }
}







