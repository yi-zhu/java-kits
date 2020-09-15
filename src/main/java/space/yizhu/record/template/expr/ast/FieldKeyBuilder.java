

package space.yizhu.record.template.expr.ast;


public abstract class FieldKeyBuilder {

    public abstract Object getFieldKey(Class<?> targetClass, long fieldFnv1a64Hash);

    
    static FieldKeyBuilder instance = new StrictFieldKeyBuilder();

    public static FieldKeyBuilder getInstance() {
        return instance;
    }

    
    public static void setToFastFieldKeyBuilder() {
        instance = new FastFieldKeyBuilder();
    }

    
    public static void setFieldKeyBuilder(FieldKeyBuilder fieldKeyBuilder) {
        if (fieldKeyBuilder == null) {
            throw new IllegalArgumentException("fieldKeyBuilder can not be null");
        }
        instance = fieldKeyBuilder;
    }

    

    
    public static class FastFieldKeyBuilder extends FieldKeyBuilder {
        public Object getFieldKey(Class<?> targetClass, long fieldFnv1a64Hash) {
            return targetClass.getName().hashCode() ^ fieldFnv1a64Hash;
        }
    }

    

    
    public static class StrictFieldKeyBuilder extends FieldKeyBuilder {
        public Object getFieldKey(Class<?> targetClass, long fieldFnv1a64Hash) {
            return new FieldKey(targetClass.getName().hashCode(), fieldFnv1a64Hash);
        }
    }

    

    
    public static class FieldKey {

        final long classHash;
        final long fieldHash;

        public FieldKey(long classHash, long fieldHash) {
            this.classHash = classHash;
            this.fieldHash = fieldHash;
        }

        public int hashCode() {
            return (int) (classHash ^ fieldHash);
        }

        
        public boolean equals(Object fieldKey) {
            FieldKey fk = (FieldKey) fieldKey;
            return classHash == fk.classHash && fieldHash == fk.fieldHash;
        }

        public String toString() {
            return "classHash = " + classHash + "\nfieldHash = " + fieldHash;
        }
    }
}








