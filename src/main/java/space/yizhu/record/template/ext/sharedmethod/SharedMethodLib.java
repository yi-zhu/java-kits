

package space.yizhu.record.template.ext.sharedmethod;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


public class SharedMethodLib {

    
    public Boolean isEmpty(Object v) {
        if (v == null) {
            return true;
        }

        if (v instanceof Collection) {
            return ((Collection<?>) v).isEmpty();
        }
        if (v instanceof Map) {
            return ((Map<?, ?>) v).isEmpty();
        }

        if (v.getClass().isArray()) {
            return Array.getLength(v) == 0;
        }

        if (v instanceof Iterator) {
            return !((Iterator<?>) v).hasNext();
        }
        if (v instanceof Iterable) {
            return !((Iterable<?>) v).iterator().hasNext();
        }

        throw new IllegalArgumentException("isEmpty(...) 方法只能接受 Collection、Map、数组、Iterator、Iterable 类型参数");
    }

    public Boolean notEmpty(Object v) {
        return !isEmpty(v);
    }
}






