package space.yizhu.record.aop;


public class Aop {

    private static AopFactory aopFactory = new AopFactory();

    public static <T> T get(Class<T> targetClass) {
        return aopFactory.get(targetClass);
    }

    public static <T> T get(Class<T> targetClass, int injectDepth) {
        return aopFactory.get(targetClass, injectDepth);
    }

    public static <T> T inject(T targetObject) {
        return aopFactory.inject(targetObject);
    }

    public static <T> T inject(T targetObject, int injectDepth) {
        return aopFactory.inject(targetObject, injectDepth);
    }

    
    public static void addSingletonObject(Object singletonObject) {
        aopFactory.addSingletonObject(singletonObject);
    }

    
    public static <T> void addMapping(Class<T> from, Class<? extends T> to) {
        aopFactory.addMapping(from, to);
    }

    
    public static <T> void addMapping(Class<T> from, String to) {
        aopFactory.addMapping(from, to);
    }
	
	

    
    public static void setAopFactory(AopFactory aopFactory) {
        if (aopFactory == null) {
            throw new IllegalArgumentException("aopFactory can not be null");
        }
        Aop.aopFactory = aopFactory;
    }

    public static AopFactory getAopFactory() {
        return aopFactory;
    }

    
    @Deprecated
    public static void setEnhance(boolean enhance) {
        aopFactory.setEnhance(enhance);
    }

    
    public static void setSingleton(boolean singleton) {
        aopFactory.setSingleton(singleton);
    }

    public static boolean isSingleton() {
        return aopFactory.isSingleton();
    }

    
    public static void setInjectDepth(int injectDepth) {
        aopFactory.setInjectDepth(injectDepth);
    }

    public static int getInjectDepth() {
        return aopFactory.getInjectDepth();
    }
}




