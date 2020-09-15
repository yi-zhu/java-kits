

package space.yizhu.record.log;


public abstract class Log {

    private static ILogFactory defaultLogFactory = null;

    static {
        init();
    }

    static void init() {
        if (defaultLogFactory == null) {
            try {
                Class.forName("org.apache.log4j.Logger");
                Class<?> log4jLogFactoryClass = Class.forName("space.yizhu.record.log.Log4jLogFactory");
                defaultLogFactory = (ILogFactory) log4jLogFactoryClass.newInstance();    
            } catch (Exception e) {
            }
        }
    }

    static void setDefaultLogFactory(ILogFactory defaultLogFactory) {
        if (defaultLogFactory == null) {
            throw new IllegalArgumentException("defaultLogFactory can not be null.");
        }
        Log.defaultLogFactory = defaultLogFactory;
    }

    public static Log getLog(Class<?> clazz) {
        return defaultLogFactory.getLog(clazz);
    }

    public static Log getLog(String name) {
        return defaultLogFactory.getLog(name);
    }

    public abstract void debug(String message);

    public abstract void debug(String message, Throwable t);

    public abstract void info(String message);

    public abstract void info(String message, Throwable t);

    public abstract void warn(String message);

    public abstract void warn(String message, Throwable t);

    public abstract void error(String message);

    public abstract void error(String message, Throwable t);

    public abstract void fatal(String message);

    public abstract void fatal(String message, Throwable t);

    public abstract boolean isDebugEnabled();

    public abstract boolean isInfoEnabled();

    public abstract boolean isWarnEnabled();

    public abstract boolean isErrorEnabled();

    public abstract boolean isFatalEnabled();
}

