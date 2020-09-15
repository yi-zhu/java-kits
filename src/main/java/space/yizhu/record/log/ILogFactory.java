

package space.yizhu.record.log;


public interface ILogFactory {

    Log getLog(Class<?> clazz);

    Log getLog(String name);
}
