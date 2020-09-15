

package space.yizhu.record.template.expr.ast;

import java.lang.reflect.Array;

import space.yizhu.kits.StrKit;
import space.yizhu.record.plugin.activerecord.Model;
import space.yizhu.record.plugin.activerecord.Record;


public class FieldGetters {

    
    public static class NullFieldGetter extends FieldGetter {

        public static final NullFieldGetter me = new NullFieldGetter();

        public boolean notNull() {
            return false;
        }

        public FieldGetter takeOver(Class<?> targetClass, String fieldName) {
            throw new RuntimeException("The method takeOver(Class, String) of NullFieldGetter should not be invoked");
        }

        public Object get(Object target, String fieldName) throws Exception {
            throw new RuntimeException("The method get(Object, String) of NullFieldGetter should not be invoked");
        }
    }

    
    public static class GetterMethodFieldGetter extends FieldGetter {

        protected java.lang.reflect.Method getterMethod;

        public GetterMethodFieldGetter(java.lang.reflect.Method getterMethod) {
            this.getterMethod = getterMethod;
        }

        public FieldGetter takeOver(Class<?> targetClass, String fieldName) {
            if (MethodKit.isForbiddenClass(targetClass)) {
                throw new RuntimeException("Forbidden class: " + targetClass.getName());
            }

            String getterName = "get" + StrKit.firstCharToUpperCase(fieldName);
            java.lang.reflect.Method[] methodArray = targetClass.getMethods();
            for (java.lang.reflect.Method method : methodArray) {
                if (method.getName().equals(getterName) && method.getParameterTypes().length == 0) {
                    
                    
                    

                    return new GetterMethodFieldGetter(method);
                }
            }

            return null;
        }

        public Object get(Object target, String fieldName) throws Exception {
            return getterMethod.invoke(target, ExprList.NULL_OBJECT_ARRAY);
        }

        public String toString() {
            return getterMethod.toString();
        }
    }

    
    public static class IsMethodFieldGetter extends FieldGetter {

        protected java.lang.reflect.Method isMethod;

        
        public IsMethodFieldGetter() {
        }

        public IsMethodFieldGetter(java.lang.reflect.Method isMethod) {
            this.isMethod = isMethod;
        }

        public FieldGetter takeOver(Class<?> targetClass, String fieldName) {
            if (MethodKit.isForbiddenClass(targetClass)) {
                throw new RuntimeException("Forbidden class: " + targetClass.getName());
            }

            String isMethodName = "is" + StrKit.firstCharToUpperCase(fieldName);
            java.lang.reflect.Method[] methodArray = targetClass.getMethods();
            for (java.lang.reflect.Method method : methodArray) {
                if (method.getName().equals(isMethodName) && method.getParameterTypes().length == 0) {
                    Class<?> returnType = method.getReturnType();
                    if (returnType == Boolean.class || returnType == boolean.class) {
                        return new IsMethodFieldGetter(method);
                    }
                }
            }

            return null;
        }

        public Object get(Object target, String fieldName) throws Exception {
            return isMethod.invoke(target, ExprList.NULL_OBJECT_ARRAY);
        }

        public String toString() {
            return isMethod.toString();
        }
    }

    
    public static class ModelFieldGetter extends FieldGetter {

        
        static final ModelFieldGetter singleton = new ModelFieldGetter();

        public FieldGetter takeOver(Class<?> targetClass, String fieldName) {
            if (Model.class.isAssignableFrom(targetClass)) {
                return singleton;
            } else {
                return null;
            }
        }

        public Object get(Object target, String fieldName) throws Exception {
            return ((Model<?>) target).get(fieldName);
        }
    }

    
    public static class RecordFieldGetter extends FieldGetter {

        
        static final RecordFieldGetter singleton = new RecordFieldGetter();

        public FieldGetter takeOver(Class<?> targetClass, String fieldName) {
            if (Record.class.isAssignableFrom(targetClass)) {
                return singleton;
            } else {
                return null;
            }
        }

        public Object get(Object target, String fieldName) throws Exception {
            return ((Record) target).get(fieldName);
        }
    }

    
    public static class MapFieldGetter extends FieldGetter {

        
        static final MapFieldGetter singleton = new MapFieldGetter();

        public FieldGetter takeOver(Class<?> targetClass, String fieldName) {
            if (java.util.Map.class.isAssignableFrom(targetClass)) {
                return singleton;
            } else {
                return null;
            }
        }

        public Object get(Object target, String fieldName) throws Exception {
            return ((java.util.Map<?, ?>) target).get(fieldName);
        }
    }

    
    public static class RealFieldGetter extends FieldGetter {

        protected java.lang.reflect.Field field;

        public RealFieldGetter(java.lang.reflect.Field field) {
            this.field = field;
        }

        public FieldGetter takeOver(Class<?> targetClass, String fieldName) {
            java.lang.reflect.Field[] fieldArray = targetClass.getFields();
            for (java.lang.reflect.Field field : fieldArray) {
                if (field.getName().equals(fieldName)) {
                    return new RealFieldGetter(field);
                }
            }

            return null;
        }

        public Object get(Object target, String fieldName) throws Exception {
            return field.get(target);
        }

        public String toString() {
            return field.toString();
        }
    }

    
    public static class ArrayLengthGetter extends FieldGetter {

        
        static final ArrayLengthGetter singleton = new ArrayLengthGetter();

        public FieldGetter takeOver(Class<?> targetClass, String fieldName) {
            if ("length".equals(fieldName) && targetClass.isArray()) {
                return singleton;
            } else {
                return null;
            }
        }

        public Object get(Object target, String fieldName) throws Exception {
            return Array.getLength(target);
        }
    }
}








