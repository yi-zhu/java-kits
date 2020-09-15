

package space.yizhu.record.template.expr.ast;

import java.lang.reflect.Method;


public class MethodInfoExt extends MethodInfo {

    protected Object objectOfExtensionClass;

    public MethodInfoExt(Object objectOfExtensionClass, Long key, Class<?> clazz, Method method) {
        super(key, clazz, method);
        this.objectOfExtensionClass = objectOfExtensionClass;

        
        
        
        
        
    }

    public Object invoke(Object target, Object... args) throws ReflectiveOperationException {
        Object[] finalArgs = new Object[args.length + 1];
        finalArgs[0] = target;

        if (args.length > 0) {
            System.arraycopy(args, 0, finalArgs, 1, args.length);
        }

        if (isVarArgs) {
            return invokeVarArgsMethod(objectOfExtensionClass, finalArgs);
        } else {
            return method.invoke(objectOfExtensionClass, finalArgs);
        }
    }
}







