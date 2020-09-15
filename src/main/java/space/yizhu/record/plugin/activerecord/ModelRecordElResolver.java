

package space.yizhu.record.plugin.activerecord;

import space.yizhu.kits.StrKit;

import javax.el.ELContext;
import javax.el.ELResolver;
import java.beans.FeatureDescriptor;
import java.lang.reflect.Method;
import java.util.Iterator;


@SuppressWarnings("rawtypes")
public class ModelRecordElResolver extends ELResolver {

    private static final Object[] NULL_ARGUMENT = new Object[0];

    private static boolean resolveBeanAsModel = false;

    
    public static void setResolveBeanAsModel(boolean resolveBeanAsModel) {
        ModelRecordElResolver.resolveBeanAsModel = resolveBeanAsModel;
    }

    
    private static boolean isWorking = true;

    public static void setWorking(boolean isWorking) {
        ModelRecordElResolver.isWorking = isWorking;
    }




    public Object getValue(ELContext context, Object base, Object property) {
        if (isWorking == false || property == null) {
            return null;
        }
        
        
        
        if (base instanceof IBean) {
            Method getter = findGetter(base, property.toString());
            if (getter != null) {
                context.setPropertyResolved(true);
                try {
                    return getter.invoke(base, NULL_ARGUMENT);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (base instanceof Model) {
            context.setPropertyResolved(true);
            return ((Model) base).get(property.toString());
        } else if (base instanceof Record) {
            context.setPropertyResolved(true);
            return ((Record) base).get(property.toString());
        }
        return null;
    }

    private Method findGetter(Object base, String property) {
        String getter = "get" + StrKit.firstCharToUpperCase(property);
        Method[] methods = base.getClass().getMethods();
        for (Method m : methods) {
            if (m.getName().equals(getter) && m.getParameterTypes().length == 0) {
                return m;
            }
        }
        return null;
    }

    public Class<?> getType(ELContext context, Object base, Object property) {
        if (isWorking == false) {
            return null;
        }
        if (resolveBeanAsModel == false && base instanceof IBean) {
            return null;
        }

        
        return (base == null) ? null : Object.class;
    }

    public void setValue(ELContext context, Object base, Object property, Object value) {
        if (isWorking == false || property == null) {
            return;
        }
        if (resolveBeanAsModel == false && base instanceof IBean) {
            return;
        }

        if (base instanceof Model) {
            context.setPropertyResolved(true);
            try {
                ((Model) base).set(property.toString(), value);
            } catch (Exception e) {
                ((Model) base).put(property.toString(), value);
            }
        } else if (base instanceof Record) {
            context.setPropertyResolved(true);
            ((Record) base).set(property.toString(), value);
        }
    }

    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (isWorking == false) {
            return false;
        }
        if (resolveBeanAsModel == false && base instanceof IBean) {
            return false;
        }

        if (base instanceof Model || base instanceof Record) {
            context.setPropertyResolved(true);
            return false;
        }
        return false;
    }

    
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (isWorking == false) {
            return null;
        }
        if (resolveBeanAsModel == false && base instanceof IBean) {
            return null;
        }

        if (base instanceof Model || base instanceof Record)
            return String.class;
        return null;
    }
}


