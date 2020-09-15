

package space.yizhu.record.plugin.activerecord;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class CaseInsensitiveContainerFactory implements IContainerFactory {

    private static Boolean toLowerCase = null;

    public CaseInsensitiveContainerFactory() {
    }

    public CaseInsensitiveContainerFactory(boolean toLowerCase) {
        CaseInsensitiveContainerFactory.toLowerCase = toLowerCase;
    }

    public Map<String, Object> getAttrsMap() {
        return new CaseInsensitiveMap<Object>();
    }

    public Map<String, Object> getColumnsMap() {
        return new CaseInsensitiveMap<Object>();
    }

    public Set<String> getModifyFlagSet() {
        return new CaseInsensitiveSet();
    }

    private static String convertCase(String key) {
        if (toLowerCase != null) {
            return toLowerCase ? key.toLowerCase() : key.toUpperCase();
        } else {
            return key;
        }
    }

    
    public static class CaseInsensitiveSet extends TreeSet<String> {

        private static final long serialVersionUID = 6236541338642353211L;

        public CaseInsensitiveSet() {
            super(String.CASE_INSENSITIVE_ORDER);
        }

        public boolean add(String e) {
            return super.add(convertCase(e));
        }

        public boolean addAll(Collection<? extends String> c) {
            boolean modified = false;
            for (String o : c) {
                if (super.add(convertCase(o))) {
                    modified = true;
                }
            }
            return modified;
        }
    }

    public static class CaseInsensitiveMap<V> extends TreeMap<String, V> {

        private static final long serialVersionUID = 7482853823611007217L;

        public CaseInsensitiveMap() {
            super(String.CASE_INSENSITIVE_ORDER);
        }

        public V put(String key, V value) {
            return super.put(convertCase(key), value);
        }

        public void putAll(Map<? extends String, ? extends V> map) {
            for (Map.Entry<? extends String, ? extends V> e : map.entrySet()) {
                super.put(convertCase(e.getKey()), e.getValue());
            }
        }
    }
}

