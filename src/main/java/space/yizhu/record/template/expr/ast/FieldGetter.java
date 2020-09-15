

package space.yizhu.record.template.expr.ast;


public abstract class FieldGetter {

    
    public abstract FieldGetter takeOver(Class<?> targetClass, String fieldName);

    
    public abstract Object get(Object target, String fieldName) throws Exception;

    
    public boolean notNull() {
        return true;
    }
}







