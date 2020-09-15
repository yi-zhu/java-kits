

package space.yizhu.record.template.expr.ast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import space.yizhu.kits.SyncWriteMap;


public class FieldKit {

    private static FieldGetter[] getters = init();

    private static final HashMap<Object, FieldGetter> fieldGetterCache = new SyncWriteMap<Object, FieldGetter>(1024, 0.25F);

    
    private static FieldGetter[] init() {
        LinkedList<FieldGetter> ret = new LinkedList<FieldGetter>();

        ret.addLast(new FieldGetters.GetterMethodFieldGetter(null));
        ret.addLast(new FieldGetters.ModelFieldGetter());
        ret.addLast(new FieldGetters.RecordFieldGetter());
        ret.addLast(new FieldGetters.MapFieldGetter());
        ret.addLast(new FieldGetters.RealFieldGetter(null));
        ret.addLast(new FieldGetters.ArrayLengthGetter());
        

        return ret.toArray(new FieldGetter[ret.size()]);
    }

    public static FieldGetter getFieldGetter(Object key, Class<?> targetClass, String fieldName) {
        FieldGetter fieldGetter = fieldGetterCache.get(key);
        if (fieldGetter == null) {
            fieldGetter = doGetFieldGetter(targetClass, fieldName);    
            fieldGetterCache.putIfAbsent(key, fieldGetter);
        }
        return fieldGetter;
    }

    private static FieldGetter doGetFieldGetter(Class<?> targetClass, String fieldName) {
        FieldGetter ret;
        for (FieldGetter fieldGetter : getters) {
            ret = fieldGetter.takeOver(targetClass, fieldName);
            if (ret != null) {
                return ret;
            }
        }
        return FieldGetters.NullFieldGetter.me;
    }

    public static void addFieldGetter(int index, FieldGetter fieldGetter) {
        addFieldGetter(fieldGetter, index, true);
    }

    public static void addFieldGetterToLast(FieldGetter fieldGetter) {
        addFieldGetter(fieldGetter, null, true);
    }

    public static void addFieldGetterToFirst(FieldGetter fieldGetter) {
        addFieldGetter(fieldGetter, null, false);
    }

    
    private static synchronized void addFieldGetter(FieldGetter fieldGetter, Integer index, boolean addLast) {
        checkParameter(fieldGetter);

        LinkedList<FieldGetter> ret = getCurrentFieldGetters();
        if (index != null) {
            ret.add(index, fieldGetter);
        } else {
            if (addLast) {
                ret.addLast(fieldGetter);
            } else {
                ret.addFirst(fieldGetter);
            }
        }
        getters = ret.toArray(new FieldGetter[ret.size()]);
    }

    private static LinkedList<FieldGetter> getCurrentFieldGetters() {
        LinkedList<FieldGetter> ret = new LinkedList<FieldGetter>();
        for (FieldGetter fieldGetter : getters) {
            ret.add(fieldGetter);
        }
        return ret;
    }

    private static void checkParameter(FieldGetter fieldGetter) {
        if (fieldGetter == null) {
            throw new IllegalArgumentException("The parameter fieldGetter can not be null");
        }
        for (FieldGetter fg : getters) {
            if (fg.getClass() == fieldGetter.getClass()) {
                throw new RuntimeException("FieldGetter already exists : " + fieldGetter.getClass().getName());
            }
        }
    }

    public static synchronized void removeFieldGetter(Class<? extends FieldGetter> fieldGetterClass) {
        LinkedList<FieldGetter> ret = getCurrentFieldGetters();

        for (Iterator<FieldGetter> it = ret.iterator(); it.hasNext(); ) {
            if (it.next().getClass() == fieldGetterClass) {
                it.remove();
            }
        }

        getters = ret.toArray(new FieldGetter[ret.size()]);
    }
}








