package space.yizhu.record.aop;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


public class AopFactory {

    
    protected ConcurrentHashMap<Class<?>, Object> singletonCache = new ConcurrentHashMap<Class<?>, Object>();

    
    protected HashMap<Class<?>, Class<?>> mapping = null;

    protected static int MAX_INJECT_DEPTH = 7;            

    protected boolean singleton = true;                    
    protected boolean enhance = true;                    
    protected int injectDepth = 3;                        

    public <T> T get(Class<T> targetClass) {
        try {
            return doGet(targetClass, injectDepth);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T get(Class<T> targetClass, int injectDepth) {
        try {
            return doGet(targetClass, injectDepth);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T doGet(Class<T> targetClass, int injectDepth) throws ReflectiveOperationException {
        
        

        targetClass = (Class<T>) getMappingClass(targetClass);

        Singleton si = targetClass.getAnnotation(Singleton.class);
        boolean singleton = (si != null ? si.value() : this.singleton);

        Object ret;
        if (!singleton) {
            ret = createObject(targetClass);
            doInject(targetClass, ret, injectDepth);
            return (T) ret;
        }

        ret = singletonCache.get(targetClass);
        if (ret == null) {
            synchronized (this) {
                ret = singletonCache.get(targetClass);
                if (ret == null) {
                    ret = createObject(targetClass);
                    doInject(targetClass, ret, injectDepth);
                    singletonCache.put(targetClass, ret);
                }
            }
        }

        return (T) ret;
    }

    public <T> T inject(T targetObject) {
        try {
            doInject(targetObject.getClass(), targetObject, injectDepth);
            return targetObject;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T inject(T targetObject, int injectDepth) {
        try {
            doInject(targetObject.getClass(), targetObject, injectDepth);
            return targetObject;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    
    public <T> T inject(Class<T> targetClass, T targetObject) {
        try {
            doInject(targetClass, targetObject, injectDepth);
            return targetObject;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T inject(Class<T> targetClass, T targetObject, int injectDepth) {
        try {
            doInject(targetClass, targetObject, injectDepth);
            return targetObject;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doInject(Class<?> targetClass, Object targetObject, int injectDepth) throws ReflectiveOperationException {
        if ((injectDepth--) <= 0) {
            return;
        }

        targetClass = getUsefulClass(targetClass);
        Field[] fields = targetClass.getDeclaredFields();
        if (fields.length == 0) {
            return;
        }

        for (Field field : fields) {
            Inject inject = field.getAnnotation(Inject.class);
            if (inject == null) {
                continue;
            }

            Class<?> fieldInjectedClass = inject.value();
            if (fieldInjectedClass == Void.class) {
                fieldInjectedClass = field.getType();
            }

            Object fieldInjectedObject = doGet(fieldInjectedClass, injectDepth);
            field.setAccessible(true);
            field.set(targetObject, fieldInjectedObject);
        }
    }

    
    @SuppressWarnings("deprecation")
    protected Object createObject(Class<?> targetClass) throws ReflectiveOperationException {
        Enhance en = targetClass.getAnnotation(Enhance.class);
        boolean enhance = (en != null ? en.value() : this.enhance);

        return targetClass.newInstance();
    }

    
    protected Class<?> getUsefulClass(Class<?> clazz) {
        
        
        return (Class<?>) (clazz.getName().indexOf("$$EnhancerBy") == -1 ? clazz : clazz.getSuperclass());
    }

    
    @Deprecated
    public AopFactory setEnhance(boolean enhance) {
        this.enhance = enhance;
        return this;
    }

    
    public AopFactory setSingleton(boolean singleton) {
        this.singleton = singleton;
        return this;
    }

    public boolean isSingleton() {
        return singleton;
    }

    
    public AopFactory setInjectDepth(int injectDepth) {
        if (injectDepth <= 0) {
            throw new IllegalArgumentException("注入层数必须大于 0");
        }
        if (injectDepth > MAX_INJECT_DEPTH) {
            throw new IllegalArgumentException("为保障性能，注入层数不能超过 " + MAX_INJECT_DEPTH);
        }

        this.injectDepth = injectDepth;
        return this;
    }

    public int getInjectDepth() {
        return injectDepth;
    }

    public AopFactory addSingletonObject(Object singletonObject) {
        if (singletonObject == null) {
            throw new IllegalArgumentException("singletonObject can not be null");
        }
        if (singletonObject instanceof Class) {
            throw new IllegalArgumentException("singletonObject can not be Class type");
        }

        Class<?> type = getUsefulClass(singletonObject.getClass());
        if (singletonCache.putIfAbsent(type, singletonObject) != null) {
            throw new RuntimeException("Singleton object already exists for type : " + type.getName());
        }

        return this;
    }

    public synchronized <T> AopFactory addMapping(Class<T> from, Class<? extends T> to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("The parameter from and to can not be null");
        }

        if (mapping == null) {
            mapping = new HashMap<Class<?>, Class<?>>(128, 0.25F);
        } else if (mapping.containsKey(from)) {
            throw new RuntimeException("Class already mapped : " + from.getName());
        }

        mapping.put(from, to);
        return this;
    }

    public <T> AopFactory addMapping(Class<T> from, String to) {
        try {
            @SuppressWarnings("unchecked")
            Class<T> toClass = (Class<T>) Class.forName(to.trim());
            if (from.isAssignableFrom(toClass)) {
                return addMapping(from, toClass);
            } else {
                throw new IllegalArgumentException("The parameter \"to\" must be the subclass or implementation of the parameter \"from\"");
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    
    public Class<?> getMappingClass(Class<?> from) {
        if (mapping != null) {
            Class<?> ret = mapping.get(from);
            return ret != null ? ret : from;
        } else {
            return from;
        }
    }
}







