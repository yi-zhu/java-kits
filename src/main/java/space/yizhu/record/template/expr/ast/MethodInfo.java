

package space.yizhu.record.template.expr.ast;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


public class MethodInfo {

    protected final Long key;
    protected final Class<?> clazz;
    protected final Method method;

    protected final boolean isVarArgs;
    protected final Class<?>[] paraTypes;

    public MethodInfo(Long key, Class<?> clazz, Method method) {
        this.key = key;
        this.clazz = clazz;
        this.method = method;
        this.isVarArgs = method.isVarArgs();
        this.paraTypes = method.getParameterTypes();
    }

    public Object invoke(Object target, Object... args) throws ReflectiveOperationException {
        if (isVarArgs) {
            return invokeVarArgsMethod(target, args);
        } else {
            return method.invoke(target, args);
        }
    }

    protected Object invokeVarArgsMethod(Object target, Object[] argValues) throws ReflectiveOperationException {
        Object[] finalArgValues = new Object[paraTypes.length];

        int fixedParaLength = paraTypes.length - 1;
        System.arraycopy(argValues, 0, finalArgValues, 0, fixedParaLength);
        Class<?> varParaComponentType = paraTypes[paraTypes.length - 1].getComponentType();
        Object varParaValues = Array.newInstance(varParaComponentType, argValues.length - fixedParaLength);
        int p = 0;
        for (int i = fixedParaLength; i < argValues.length; i++) {
            Array.set(varParaValues, p++, argValues[i]);
        }
        finalArgValues[paraTypes.length - 1] = varParaValues;
        return method.invoke(target, finalArgValues);
    }

    public Long getKey() {
        return key;
    }

    public String getName() {
        return method.getName();
    }

    public boolean isStatic() {
        return Modifier.isStatic(method.getModifiers());
    }

    public boolean isVarArgs() {
        return isVarArgs;
    }

    protected Class<?>[] getParameterTypes() {
        return paraTypes;
    }

    public String toString() {
        StringBuilder ret = new StringBuilder(clazz.getName()).append(".").append(method.getName()).append("(");
        for (int i = 0; i < paraTypes.length; i++) {
            if (i > 0) {
                ret.append(", ");
            }
            ret.append(paraTypes[i].getName());
        }
        return ret.append(")").toString();
    }
}


