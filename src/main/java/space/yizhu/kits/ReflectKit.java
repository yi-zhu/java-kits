

package space.yizhu.kits;

/**
 * 反射工具类
 */
public class ReflectKit {

    public static Object newInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}






