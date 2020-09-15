

package space.yizhu.record.template.expr.ast;

import space.yizhu.kits.HashKit;


public abstract class MethodKeyBuilder {

    
    public abstract Long getMethodKey(Class<?> targetClass, String methodName, Class<?>[] argTypes);

    
    static MethodKeyBuilder instance = new FastMethodKeyBuilder();

    public static MethodKeyBuilder getInstance() {
        return instance;
    }

    
    public static void setToStrictMethodKeyBuilder() {
        instance = new StrictMethodKeyBuilder();
    }

    
    public static void setMethodKeyBuilder(MethodKeyBuilder methodKeyBuilder) {
        if (methodKeyBuilder == null) {
            throw new IllegalArgumentException("methodKeyBuilder can not be null");
        }
        instance = methodKeyBuilder;
    }

    
    public static class FastMethodKeyBuilder extends MethodKeyBuilder {
        public Long getMethodKey(Class<?> targetClass, String methodName, Class<?>[] argTypes) {
            long hash = HashKit.FNV_OFFSET_BASIS_64;
            hash ^= targetClass.getName().hashCode();
            hash *= HashKit.FNV_PRIME_64;

            hash ^= methodName.hashCode();
            hash *= HashKit.FNV_PRIME_64;

            if (argTypes != null) {
                for (int i = 0; i < argTypes.length; i++) {
                    Class<?> type = argTypes[i];
                    if (type != null) {
                        hash ^= type.getName().hashCode();
                        hash *= HashKit.FNV_PRIME_64;
                    } else {
                        hash ^= "null".hashCode();
                        hash *= HashKit.FNV_PRIME_64;
                    }
                }
            }
            return hash;
        }
    }

    
    public static class StrictMethodKeyBuilder extends MethodKeyBuilder {
        public Long getMethodKey(Class<?> targetClass, String methodName, Class<?>[] argTypes) {
            long hash = HashKit.FNV_OFFSET_BASIS_64;

            hash = fnv1a64(hash, targetClass.getName());
            hash = fnv1a64(hash, methodName);
            if (argTypes != null) {
                for (int i = 0; i < argTypes.length; i++) {
                    Class<?> type = argTypes[i];
                    if (type != null) {
                        hash = fnv1a64(hash, type.getName());
                    } else {
                        hash = fnv1a64(hash, "null");
                    }
                }
            }

            return hash;
        }

        private long fnv1a64(long offsetBasis, String key) {
            long hash = offsetBasis;
            for (int i = 0, size = key.length(); i < size; i++) {
                hash ^= key.charAt(i);
                hash *= HashKit.FNV_PRIME_64;
            }
            return hash;
        }
    }
}



